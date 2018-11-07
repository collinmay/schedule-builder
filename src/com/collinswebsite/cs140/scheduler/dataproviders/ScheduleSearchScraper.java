package com.collinswebsite.cs140.scheduler.dataproviders;

import com.collinswebsite.cs140.scheduler.Course;
import com.collinswebsite.cs140.scheduler.Section;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.body.MultipartBody;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A DataProvider implementation that rips lists of courses and sections from the Schedule Search web application.
 */
public class ScheduleSearchScraper implements DataProvider {
    private static final String SCHEDULE_SEARCH_URL = "https://mywcc.whatcom.edu/ScheduleSearch/ScheduleSearch.aspx";
    private static final String SEARCH_RESULTS_URL = "https://mywcc.whatcom.edu/ScheduleSearch/SearchResults.aspx";
    private static final Map<String, String> defaultParameters = new TreeMap<>();
    private static final Pattern guidPattern = Pattern.compile("guid: '([0-9a-f\\-]+)'");
    private static final Pattern sectionPattern = Pattern.compile("\\A([A-Z\\-]+ *&? *[0-9A-Z]+) +(.*)\\z");

    private String viewstate;
    private String viewstategenerator;
    private String eventvalidation;

    public ScheduleSearchScraper() throws DataRetrievalException {
        // send off a first request to populate viewstate
        try {
            HttpResponse<String> response = Unirest.get(SCHEDULE_SEARCH_URL).asString();
            Document doc = Jsoup.parse(response.getBody());
            mutateViewState(doc);
        } catch(UnirestException e) {
            throw new DataRetrievalException(e);
        }

        defaultParameters.put("ctl00$FeaturedContent$Quarter", "B893");
        defaultParameters.put("ctl00$FeaturedContent$ClassStatus", "");
        defaultParameters.put("ctl00$FeaturedContent$CourseNumber", "");
        defaultParameters.put("ctl00$FeaturedContent$ClassTitle", "");
        defaultParameters.put("ctl00$FeaturedContent$Department", "");
        defaultParameters.put("ctl00$FeaturedContent$Designator", "");
        defaultParameters.put("ctl00$FeaturedContent$ItemNumber", "");
        defaultParameters.put("ctl00$FeaturedContent$SearchButton", "Search");
        defaultParameters.put("ctl00$FeaturedContent$Wdgs", "");
    }

    @Override
    public Map<String, Course> getCourseList() throws DataRetrievalException {
        // TODO: quarter

        Map<String, Course> courses = new HashMap<>();

        Element table = sendRequest(null).getElementById("FeaturedContent_resultsTable");
        table.getElementsByTag("tbody").first().children().forEach((row) -> {
            List<String> fields = row.children().eachText();
            int lineNo = Integer.parseInt(fields.get(0));
            String title = fields.get(1);

            Matcher m = sectionPattern.matcher(fields.get(2));
            if(!m.find()) {
                System.out.println("invalid section: " + fields.get(2));
                return;
            }

            String normalizedCourseId = Course.normalizeCourseId(m.group(1));

            Course course;
            if(courses.containsKey(normalizedCourseId)) {
                course = courses.get(normalizedCourseId);
            } else {
                course = new Course(normalizedCourseId, title);
                courses.put(normalizedCourseId, course);
            }
            course.addSection(new Section(course, lineNo, m.group(2)));
        });

        return courses;
    }

    private Document sendRequest(Map<String, String> parameters) throws DataRetrievalException {
        try {
            // set up form values
            Map<String, Object> formData = new TreeMap<>();
            defaultParameters.forEach(formData::putIfAbsent); // add default form values
            if (parameters != null) {
                parameters.forEach(formData::putIfAbsent); // add user form values
            }

            // submit form
            Document intermediate = Jsoup.parse(addHeaders(Unirest.post(SCHEDULE_SEARCH_URL).fields(formData)).asString().getBody());

            // this takes us to a "Retrieving results" page that uses some javascript to redirect to the actual results
            String javascript = intermediate.getElementById("ctl01").getElementsByTag("script").first().data();

            // pull the request guid out of the javascript
            Matcher m = guidPattern.matcher(javascript);
            if(!m.find()) {
                System.out.println("bad javascript: " + javascript);
                throw new InvalidPageException();
            }
            String guid = m.group(1);

            // actually get our results
            return Jsoup.parse(Unirest.post(SEARCH_RESULTS_URL).field("guid", guid).asString().getBody());
        } catch(UnirestException e) {
            throw new DataRetrievalException(e);
        }
    }

    private MultipartBody addHeaders(MultipartBody body) {
        body.field("__EVENTTARGET", "");
        body.field("__EVENTARGUMENT", "");
        if(viewstate != null) {
            body.field("__VIEWSTATE", viewstate);
        }
        if(viewstategenerator != null) {
            body.field("__VIEWSTATEGENERATOR", viewstategenerator);
        }
        if(eventvalidation != null) {
            body.field("__EVENTVALIDATION", eventvalidation);
        }
        return body;
    }

    private Document mutateViewState(Document doc) {
        viewstate = doc.getElementById("__VIEWSTATE").attributes().get("value");
        viewstategenerator = doc.getElementById("__VIEWSTATEGENERATOR").attributes().get("value");
        eventvalidation = doc.getElementById("__EVENTVALIDATION").attributes().get("value");

        return doc;
    }

    public class InvalidPageException extends DataRetrievalException {

    }

    public class InvalidSectionException extends DataRetrievalException {
        public InvalidSectionException(String section) {
            super("Invalid section: " + section);
        }
    }
}
