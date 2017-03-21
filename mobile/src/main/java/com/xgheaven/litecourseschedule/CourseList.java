package com.xgheaven.litecourseschedule;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CourseList {
    public static String PREFERENCE_NAME = "course";

    private ArrayList<Course> courses;
    private Context context;
    private SharedPreferences preferences;

    private static CourseList _course;

    public static CourseList getInstance(Context context) {
        _course = new CourseList(context);
        return _course;
    }

    public static CourseList getInstance() {
        return _course;
    }

    private CourseList(Context context) {
        courses = new ArrayList<>(Arrays.asList(
                new Course(Course.MON), new Course(Course.TUE),
                new Course(Course.WED), new Course(Course.THU),
                new Course(Course.FRI), new Course(Course.SAT), new Course(Course.SUN)
        ));

        this.context = context;

        preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public boolean load() {
        String string = preferences.getString("courses", null);
        if (string != null) {
            load(string);
            return true;
        }
        return false;
    }

    public boolean load(String json) {
        List<Course> nCourses = JSON.parseArray(json, Course.class);
        for (Course course : nCourses) {
            _add(course);
        }
        save();
        return true;
    }

    public int size() {
        return courses.size();
    }

    private int _add(Course course) {
        int i;
        for (i=0; i<courses.size(); i++) {
            Course c = courses.get(i);
            if (course.getDay() > c.getDay()) continue;
            if (course.getDay() == c.getDay() && !c.isDivide())
                if (course.getStart() < c.getStart()) break;
            if (course.getDay() < c.getDay()) break;
        }
        courses.add(i, course);
        return i;
    }

    private int _remove(Course course) {
        int i;
        for (i=0; i<courses.size(); i++) {
            if (courses.get(i) == course) {
                courses.remove(i);
                return i;
            }
        }
        return -1;
    }

    public void save() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("courses", toJSON());
        editor.apply();
    }

    public int add(Course course) {
        int i = _add(course);
        return i;
    }

    public int remove(Course course) {
        return _remove(course);
    }

    public void clear() {
        Course course;
        for (int i=0; i<courses.size(); i++) {
            course = courses.get(i);
            if (!course.isDivide()) {
                courses.remove(i);
                i--;
            }
        }
        save();
    }

    public int indexOf(Course course) {
        return courses.indexOf(course);
    }

    public Course get(int pos) {
        return courses.get(pos);
    }

    public boolean isDivide(int pos) {
        return get(pos).isDivide();
    }

    public String toJSON() {
        List<Course> nCourses  = new ArrayList<>();

        for (Course course : courses) {
            if (!course.isDivide()) {
                nCourses.add(course);
            }
        }

        return JSON.toJSONString(nCourses);
    }

    public int getCurrentDivideIndex() {
        Calendar calendar = Calendar.getInstance();
        int day = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7;
        for (int i=0; i<courses.size(); i++) {
            Course course = courses.get(i);
            if (course.isDivide() && course.getDay() == day) {
                return i;
            }
        }
        return 0;
    }
}
