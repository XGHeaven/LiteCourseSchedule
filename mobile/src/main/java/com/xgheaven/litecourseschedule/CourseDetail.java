package com.xgheaven.litecourseschedule;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Interpolator;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;

public class CourseDetail extends AppCompatActivity {

    private TextInputEditText name, classroom, start, last;
    private Spinner day;
    private Course course;
    private FloatingActionButton fab;
    private int courseIndex;

    private int courseNameColor;
    private int courseEditNameColor = Color.BLACK;
    private Drawable defaultBackground;
    private AnimatedVectorDrawable fabAni;

    private boolean edited = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        courseNameColor = getResources().getColor(R.color.courseName);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        name = (TextInputEditText) findViewById(R.id.course_name);
        classroom = (TextInputEditText) findViewById(R.id.course_classroom);
        start = (TextInputEditText) findViewById(R.id.course_start);
        last = (TextInputEditText) findViewById(R.id.course_last);
        day = (Spinner) findViewById(R.id.course_day);
        fab = (FloatingActionButton) findViewById(R.id.fab_course_edit);

        defaultBackground = name.getBackground();

        Bundle data = getIntent().getExtras();
        courseIndex = data.getInt("courseIndex");
        course = CourseList.getInstance().get(courseIndex);
        collapsingToolbarLayout.setTitle(course.getName());

        name.setText(course.getName());
        classroom.setText(course.getClassroom());
        start.setText(Integer.toString(course.getStart()));
        last.setText(Integer.toString(course.getLast()));
        day.setSelection(course.getDay());

        fabAni = (AnimatedVectorDrawable) getDrawable(R.drawable.ic_edit_animate);
        fab.setImageDrawable(fabAni);

        disable();
        fab.setOnClickListener(new View.OnClickListener() {
            boolean mode = true;
            @Override
            public void onClick(View v) {
                if (mode) {
                    enable();
                    fabAni.start();
                } else {
                    disable();
                    save();
                    try {
                        fabAni.getClass().getMethod("reverse").invoke(fabAni);
                    } catch (Exception e) {
                    }
                }
                mode = !mode;
            }
        });
    }

    private void enable() {
        name.setTextColor(courseEditNameColor);
        classroom.setTextColor(courseEditNameColor);
        start.setTextColor(courseEditNameColor);
        last.setTextColor(courseEditNameColor);

        name.setInputType(InputType.TYPE_CLASS_TEXT);
        classroom.setInputType(InputType.TYPE_CLASS_TEXT);
        start.setInputType(InputType.TYPE_CLASS_NUMBER);
        last.setInputType(InputType.TYPE_CLASS_NUMBER);

        name.setBackground(defaultBackground.getConstantState().newDrawable());
        classroom.setBackground(defaultBackground.getConstantState().newDrawable());
        start.setBackground(defaultBackground.getConstantState().newDrawable());
        last.setBackground(defaultBackground.getConstantState().newDrawable());
        day.setEnabled(true);
    }

    private void disable() {
        name.setTextColor(courseNameColor);
        classroom.setTextColor(courseNameColor);
        start.setTextColor(courseNameColor);
        last.setTextColor(courseNameColor);

        name.setInputType(InputType.TYPE_NULL);
        classroom.setInputType(InputType.TYPE_NULL);
        start.setInputType(InputType.TYPE_NULL);
        last.setInputType(InputType.TYPE_NULL);

        name.setBackgroundColor(Color.TRANSPARENT);
        classroom.setBackgroundColor(Color.TRANSPARENT);
        start.setBackgroundColor(Color.TRANSPARENT);
        last.setBackgroundColor(Color.TRANSPARENT);
        day.setEnabled(false);

    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void save() {
        course.setName(name.getText().toString());
        course.setClassroom(classroom.getText().toString());
        course.setStart(Integer.valueOf(start.getText().toString()));
        course.setLast(Integer.valueOf(last.getText().toString()));
        course.setDay((int)day.getSelectedItemId());

        edited = true;


        Intent intent = new Intent();
        intent.putExtra("editedIndex", edited ? courseIndex : -1);
        setResult(100, intent);
    }
}

