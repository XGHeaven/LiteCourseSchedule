package com.xgheaven.litecourseschedule;


import android.support.v4.util.Pair;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Course {
    private String name = "", classroom = "", id = "", teacher = "";
    private int start = 0, last = 0, day = 0;
    private boolean divide = false;

    public static final int MON = 0, TUE = 1, WED = 2, THU = 3, FRI = 4, SAT = 5, SUN = 6;

    public static List<Pair<String, String>> COURSE_TIMES = new ArrayList<>();

    static {
        // am
        COURSE_TIMES.add(new Pair<>("08:05", "08:50"));
        COURSE_TIMES.add(new Pair<>("8:55", "09:40"));
        COURSE_TIMES.add(new Pair<>("10:00", "10:45"));
        COURSE_TIMES.add(new Pair<>("10:50", "11:35"));
        COURSE_TIMES.add(new Pair<>("11:40", "12:25"));
        // pm
        COURSE_TIMES.add(new Pair<>("13:30", "14:15"));
        COURSE_TIMES.add(new Pair<>("14:20", "15:05"));
        COURSE_TIMES.add(new Pair<>("15:15", "16:00"));
        COURSE_TIMES.add(new Pair<>("16:05", "16:50"));
        // night
        COURSE_TIMES.add(new Pair<>("18:30", "19:15"));
        COURSE_TIMES.add(new Pair<>("19:20", "20:05"));
        COURSE_TIMES.add(new Pair<>("20:10", "20:55"));
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

    /**
     *
     * @return -1 finished 0 running 1 future
     */
    public int isCurrent() {
        Calendar now = Calendar.getInstance();
        int nDay = (now.get(Calendar.DAY_OF_WEEK) + 5) % 7;
        if (nDay > day) return 1;
        if (nDay < day) return -1;
        String time = new SimpleDateFormat("HH:mm").format(now.getTime());
        if (time.compareTo(COURSE_TIMES.get(getEnd() - 1).second) > 0) return 1;
        if (time.compareTo(COURSE_TIMES.get(start - 1).first) < 0) return -1;
        return 0;
    }

}
