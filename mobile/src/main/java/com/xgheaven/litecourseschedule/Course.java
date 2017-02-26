package com.xgheaven.litecourseschedule;


import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Course {
    private String name = "", classroom = "", id = "", teacher = "";
    private int start = 0, last = 0, day = 0;
    private boolean divide = false;

    public static final int MON = 0, TUE = 1, WED = 2, THU = 3, FRI = 4, SAT = 5, SUN = 6;

    private static ArrayList<Course> courses = new ArrayList<Course>(Arrays.asList(
            new Course(MON), new Course(TUE), new Course(WED), new Course(THU), new Course(FRI),
            new Course(SAT), new Course(SUN)
    ));

    public static int size() {
        return courses.size();
    }

    public static Course get(int pos) {
        return courses.get(pos);
    }

    public static boolean isDevide(int pos) {
        return get(pos).divide;
    }

    public static int add(Course course) {
        int i;
        for (i=0; i<courses.size(); i++) {
            Course c = courses.get(i);
            if (course.day > c.day) continue;
            if (course.day == c.day && !c.divide)
                if (course.start < c.start) break;
            if (course.day < c.day) break;
        }
        courses.add(i, course);
        return i;
    }

    public static void add(List<Course> courses) {
        for (Course course : courses) {
            add(course);
        }
    }

    public static void addFromJSON(String json) {
        List<Course> nCourses = JSON.parseArray(json, Course.class);
        add(nCourses);
    }

    public static void addFromJSON(byte[] string) {
        addFromJSON(new String(string));
    }

    public static int remove(Course course) {
        int i;
        for (i=0; i<courses.size(); i++) {
            if (courses.get(i) == course) {
                courses.remove(i);
                return i;
            }
        }
        return -1;
    }

    public static void removeAll() {
        Course course;
        for (int i=0; i<courses.size(); i++) {
            course = courses.get(i);
            if (course != null && !course.isDivide()) {
                courses.remove(i);
            }
        }
    }

    public static int indexOf(Course course) {
        return courses.indexOf(course);
    }

    public Course() {}

    public Course(int day) {
        divide = true;
        this.day = day;
    }

    public Course(String name, String classroom, int start, int last, int day) {
        this(name, classroom, start, last, day, "", "");
    }

    public Course(String name, String classroom, int start, int last, int day, String id, String teacher) {
        this.name = name;
        this.classroom = classroom;
        this.id = id;
        this.teacher = teacher;
        this.start = start;
        this.last = last;
        this.day = day;
    }

    public int getEnd() {
        return start + last - 1;
    }

    // auto generate
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLast() {
        return last;
    }

    public void setLast(int last) {
        this.last = last;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    // end auto generate

    public boolean isDivide() {
        return divide;
    }

    public static String toJSON() {
        List<Course> nCourses  = new ArrayList<>();

        for (Course course : courses) {
            if (!course.isDivide()) {
                nCourses.add(course);
            }
        }

        return JSON.toJSONString(nCourses);
    }

}
