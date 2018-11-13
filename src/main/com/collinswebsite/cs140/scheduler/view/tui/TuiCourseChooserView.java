package com.collinswebsite.cs140.scheduler.view.tui;

import com.collinswebsite.cs140.scheduler.Course;
import com.collinswebsite.cs140.scheduler.view.CourseChooserView;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A text-based implementation of CourseChooserView
 */
public class TuiCourseChooserView implements CourseChooserView {
    private final Scanner scanner;

    public TuiCourseChooserView() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public Set<Course> chooseCourses(Map<String, Course> courses) {
        System.out.println("Detected " + courses.size() + " courses.");
        Set<Course> selectedCourses = new HashSet<>();

        while(true) {
            System.out.println("Enter 'done' to finish, enter a course ID to select a course,");
            System.out.println("enter the same course ID again to remove a course,");
            System.out.print("or enter a keyword to search for courses: ");
            System.out.flush();
            String keyword = scanner.nextLine().toUpperCase();

            if(keyword.equals("DONE")) {
                break;
            }

            if(courses.containsKey(keyword)) {
                Course c = courses.get(keyword);
                if(selectedCourses.contains(c)) {
                    selectedCourses.remove(c);
                    System.out.println("Removed '" + c.getId() + "'.");
                } else {
                    selectedCourses.add(c);
                    System.out.println("Added '" + c.getId() + "'.");
                }
            } else { // treat as keyword
                List<Course> filtered = courses.values().stream().filter((course) -> {
                    // convert everything to uppercase so that we don't match case
                    return course.getId().contains(keyword) || course.getTitle().toUpperCase().contains(keyword);
                }).sorted(Comparator.comparing(Course::getId)).collect(Collectors.toList());

                if(filtered.isEmpty()) {
                    System.out.println("No courses were found that matched \"" + keyword + "\".");
                } else {
                    System.out.println("Found " + filtered.size() + " courses matching \"" + keyword + "\".");
                    filtered.forEach((course) -> System.out.println("  " + course.getId() + ": " + course.getTitle()));
                    continue; // skip current course listing
                }
            }

            System.out.println();
            System.out.println("Currently selected courses:");
            selectedCourses.forEach((c) -> System.out.println("  " + c.getId() + ": " + c.getTitle()));
            System.out.println();
        }

        return selectedCourses;
    }
}
