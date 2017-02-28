package com.xgheaven.litecourseschedule;


import android.content.SharedPreferences;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Course {
    private String name = "", classroom = "", id = "", teacher = "";
    private int start = 0, last = 0, day = 0;
    private boolean divide = false;

    public static final int MON = 0, TUE = 1, WED = 2, THU = 3, FRI = 4, SAT = 5, SUN = 6;

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

}
