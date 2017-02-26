package com.xgheaven.litecourseschedule;

import android.support.design.widget.Snackbar;
import android.view.View;

public class Msg {
    public static void info(View view, int resource) {
        Snackbar.make(view, resource, Snackbar.LENGTH_SHORT).show();
    }

    public static void info(View view, CharSequence string) {
        Snackbar.make(view, string, Snackbar.LENGTH_SHORT).show();
    }
}
