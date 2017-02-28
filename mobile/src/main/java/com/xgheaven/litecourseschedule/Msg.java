package com.xgheaven.litecourseschedule;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;

public class Msg {
    public static void info(View view, int resource) {
        Snackbar.make(view, resource, Snackbar.LENGTH_SHORT).show();
    }

    public static void info(View view, CharSequence string) {
        Snackbar.make(view, string, Snackbar.LENGTH_SHORT).show();
    }

    public static void about(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setView(R.layout.about_me);
        builder.create().show();
    }
}
