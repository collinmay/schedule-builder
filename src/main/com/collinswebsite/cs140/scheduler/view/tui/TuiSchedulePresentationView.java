package com.collinswebsite.cs140.scheduler.view.tui;

import com.collinswebsite.cs140.scheduler.*;
import com.collinswebsite.cs140.scheduler.view.SchedulePresentationView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class TuiSchedulePresentationView implements SchedulePresentationView {
    @Override
    public void present(Parameters parameters) {
        Scanner scanner = new Scanner(System.in);
        Iterator<Schedule> iterator = parameters.produceStream().iterator();
        Schedule schedule = iterator.hasNext() ? iterator.next() : null;
        boolean detail = false;
        while(true) {
            if(schedule != null) {
                presentSchedule(schedule, detail, parameters);
            } else {
                System.out.println("No more schedules.");
            }
            detail = false;
            System.out.println("Available actions:");
            System.out.println("  next: Advance to next schedule.");
            System.out.println("  reject <line number>: Reject a particular section.");
            System.out.println("  detail: Show more details for the current schedule.");
            System.out.println("  weight <dead|morning|days> <value>: Adjust weight for a specific metric.");
            System.out.println("  quit: Exit the program.");

            System.out.print("> ");
            System.out.flush();

            String line = scanner.nextLine();
            String[] parts = line.split(" ");

            switch(parts[0]) {
                case "next":
                    if(iterator.hasNext()) {
                        schedule = iterator.next();
                    } else {
                        schedule = null;
                    }
                    break;
                case "reject":
                    if(parts.length < 2) {
                        System.out.println("Expected line number.");
                        break;
                    }
                    Section sec = parameters.getSectionByLineNumber(Integer.parseInt(parts[1]));
                    if(sec == null) {
                        System.out.println("Section not found.");
                        break;
                    }
                    parameters.reject(sec);
                    iterator = parameters.produceStream().iterator(); // start over
                    if(iterator.hasNext()) {
                        schedule = iterator.next();
                    } else {
                        schedule = null;
                    }
                    break;
                case "detail":
                case "details": // alias
                    detail = true;
                    break;
                case "weight":
                    if(parts.length < 2) {
                        System.out.println("Expected <dead|morning|days>.");
                        break;
                    }
                    if(parts.length < 3) {
                        System.out.println("Expected new weight value.");
                        break;
                    }
                    double weight = Double.parseDouble(parts[2]);
                    switch(parts[1]) {
                        case "dead":
                            parameters.weightDeadTime = weight;
                            break;
                        case "morning":
                            parameters.weightMorningTax = weight;
                            break;
                        case "days":
                            parameters.weightNumberOfDays = weight;
                            break;
                        default:
                            System.out.println("Unknown metric: " + parts[1] + ".");
                            break;
                    }
                    iterator = parameters.produceStream().iterator(); // start over
                    if(iterator.hasNext()) {
                        schedule = iterator.next();
                    } else {
                        schedule = null;
                    }
                    break;
                case "quit":
                case "exit": // alias
                    return;
                default:
                    System.out.println("Unknown action: '" + parts[0] + "'");
                    break;
            }
        }
    }

    private static String center(String str, int width) {
        int leftPad = (width-str.length())/2;
        int rightPad = width - str.length() - leftPad;
        return String.format("%" + leftPad + "s" + "%s" + "%" + rightPad + "s", "", str, "");
    }

    public void presentSchedule(Schedule schedule, boolean detailed, Parameters parameters) {
        final int TIME_COLUMN_WIDTH = 7;
        final int COLUMN_WIDTH = 25;

        // border
        System.out.format("%" + TIME_COLUMN_WIDTH + "s ", "");
        System.out.print("+");
        for(Weekday wk : Weekday.values()) {
            for(int i = 0; i < COLUMN_WIDTH; i++) {
                System.out.print("-");
            }
            System.out.print("+");
        }
        System.out.println();

        // header
        System.out.format("%" + TIME_COLUMN_WIDTH + "s ", "");
        System.out.print("|");
        for(Weekday wk : Weekday.values()) {
            System.out.print(center(wk.getLongName(), COLUMN_WIDTH));
            System.out.print("|");
        }
        System.out.println();

        // border
        System.out.format("%" + TIME_COLUMN_WIDTH + "s ", "");
        System.out.print("+");
        for(Weekday wk : Weekday.values()) {
            for(int i = 0; i < COLUMN_WIDTH; i++) {
                System.out.print("-");
            }
            System.out.print("+");
        }
        System.out.println();

        // figure out when to begin and end, and set up our iterators
        int beginHour = 24;
        int endHour = 0;

        Map<Weekday, Iterator<TimeBlock>> dayIterators = new HashMap<>();
        Map<Weekday, TimeBlock> dayCurrentBlocks = new HashMap<>();

        for(Weekday wk : Weekday.values()) {
            // Using a classic iterator here since we mutate beginHour and endHour
            Iterator<TimeBlock> timeBlockIterator = schedule.timesOn(wk).iterator();
            while(timeBlockIterator.hasNext()) {
                TimeBlock tb = timeBlockIterator.next();
                beginHour = Math.min(beginHour, tb.getBeginHour());
                endHour = Math.max(endHour, tb.getEndHour());
            }

            // create a new iterator for printing out the table
            timeBlockIterator = schedule.timesOn(wk).iterator();
            dayIterators.put(wk, timeBlockIterator);
            if(timeBlockIterator.hasNext()) {
                dayCurrentBlocks.put(wk, timeBlockIterator.next());
            }
        }

        for(int hour = beginHour; hour <= endHour; hour++) {
            final int MINUTE_STEP = detailed ? 5 : 15;
            for(int minute = 0; minute < 60; minute+= MINUTE_STEP) {
                System.out.format("%" + TIME_COLUMN_WIDTH + "s ", String.format("%2d:%02d", hour, minute));
                boolean lastWasCorner = false;
                boolean lastHadEdge = false;
                for(Weekday wk : Weekday.values()) {
                    Iterator<TimeBlock> iterator = dayIterators.get(wk);
                    TimeBlock current = dayCurrentBlocks.get(wk);
                    if(current == null) {
                        System.out.print(lastWasCorner ? "+" : lastHadEdge ? "|" : " ");
                        System.out.format("%" + COLUMN_WIDTH + "s", "");
                        lastWasCorner = false;
                        lastHadEdge = false;
                    } else {
                        boolean isContained = current.contains(hour, minute);
                        if(isContained) {
                            int blockMinutes = (current.getEndHour() - current.getBeginHour()) * 60 + current.getEndMinute() - current.getBeginMinute();
                            int boxHeight = blockMinutes / MINUTE_STEP;
                            int boxPosition = ((hour - current.getBeginHour()) * 60 + minute - current.getBeginMinute()) / MINUTE_STEP;

                            if(boxPosition == 0) { // top border
                                System.out.print("+");
                                for(int i = 0; i < COLUMN_WIDTH; i++) {
                                    System.out.print("-");
                                }
                                lastWasCorner = true;
                                lastHadEdge = true;
                            } else if(boxPosition >= boxHeight) { // bottom border
                                System.out.print("+");
                                for(int i = 0; i < COLUMN_WIDTH; i++) {
                                    System.out.print("-");
                                }
                                lastWasCorner = true;
                                lastHadEdge = true;

                                // advance to next block
                                dayCurrentBlocks.put(wk, iterator.hasNext() ? iterator.next() : null);
                            } else {
                                // must be the middle of a box
                                System.out.print(lastWasCorner ? "+" : "|");
                                if(boxPosition == boxHeight / 2) {
                                    System.out.print(center(current.getSection().formatShort(), COLUMN_WIDTH));
                                } else if(boxPosition == boxHeight / 2 + 1) {
                                    System.out.print(center(Integer.toString(current.getSection().getLineNumber()), COLUMN_WIDTH));
                                } else if(detailed && boxPosition == 1) {
                                    System.out.format(" %-" + (COLUMN_WIDTH - 1) + "s", String.format("%02d:%02d", current.getBeginHour(), current.getBeginMinute()));
                                } else if(detailed && boxPosition == boxHeight - 1) {
                                    System.out.format(" %-" + (COLUMN_WIDTH - 1) + "s", String.format("%02d:%02d", current.getEndHour(), current.getEndMinute()));
                                } else {
                                    for(int i = 0; i < COLUMN_WIDTH; i++) {
                                        System.out.print(" ");
                                    }
                                }

                                lastWasCorner = false;
                                lastHadEdge = true;
                            }
                        } else { // no box
                            System.out.print(lastWasCorner ? "+" : lastHadEdge ? "|" : " ");
                            System.out.format("%" + COLUMN_WIDTH + "s", "");
                            lastWasCorner = false;
                            lastHadEdge = false;
                        }
                    }
                }

                System.out.println();
            }
        }

        System.out.println("Classes:");
        for(Section s : schedule.getSections()) {
            System.out.println("  - " + s.toString());
        }
        System.out.println("Dead Time: " + schedule.calculateDeadTime() + " (weighted " + parameters.weightDeadTime + ")");
        System.out.println("Morning Tax: " + schedule.morningTax() + " (weighted " + parameters.weightMorningTax + ")");
        System.out.println("Number of Days: " + schedule.numberOfDays() + " (weighted " + parameters.weightNumberOfDays + ")");
    }
}
