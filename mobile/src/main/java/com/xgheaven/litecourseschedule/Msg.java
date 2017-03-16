package com.xgheaven.litecourseschedule;

import android.content.Context;
import android.content.DialogInterface;
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

    public static void confirm(Context context, String title, String message, DialogInterface.OnClickListener yes, DialogInterface.OnClickListener no) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setPositiveButton(android.R.string.yes, yes);
        builder.setNegativeButton(android.R.string.no, no);
        builder.create().show();
    }

    public static void confirm(Context context, int title, int message, DialogInterface.OnClickListener yes, DialogInterface.OnClickListener no) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setPositiveButton(android.R.string.yes, yes);
        builder.setNegativeButton(android.R.string.no, no);
        builder.create().show();
    }
}
