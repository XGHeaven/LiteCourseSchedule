package com.xgheaven.litecourseschedule;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {
    public RecyclerView course_list;
    private Toolbar toolbar;
    private CourseAdapter courseAdapter = new CourseAdapter();

    private GoogleApiClient google;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                CourseAdd dialog = new CourseAdd(this);
                dialog.show(new CourseAdd.CourseAddedCallback() {
                    @Override
                    public void run(int position) {
                        courseAdapter.notifyItemInserted(position);
                        syncWithWear();
                        Msg.info(course_list, R.string.course_add_success);
                    }
                });
                break;
            case R.id.menu_add_from_scan:
                IntentIntegrator intentIntegrator = new IntentIntegrator(this);
                intentIntegrator.initiateScan();
                Log.i("Scan", "Start Scan");
                break;
            case R.id.menu_sync:
                syncWithWear();
                break;
            case R.id.menu_about:
                break;
            default:
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        google = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        google.connect();

        toolbar = (Toolbar)findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        course_list = (RecyclerView)findViewById(R.id.main_course_list);
        course_list.setLayoutManager(new LinearLayoutManager(this));
        course_list.setAdapter(courseAdapter);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (result == null) {
            Log.e("Scan", "Can't Scan");
            Msg.info(course_list, R.string.not_found_scanner);
            return;
        }

        String string = result.getContents();
        Log.d("Scan", string);

        Course.removeAll();
        Course.addFromJSON(string);
        courseAdapter.notifyDataSetChanged();

        Msg.info(course_list, R.string.course_add_success);
    }

    public void syncWithWear() {
        Log.d("Sync", "Start Sync");
        PutDataRequest putDataRequest = PutDataRequest.create("/courses");
        putDataRequest.setData(Course.toJSON().getBytes());
        PendingResult<DataApi.DataItemResult> result = Wearable.DataApi.putDataItem(google, putDataRequest);
        result.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
            @Override
            public void onResult(@NonNull DataApi.DataItemResult dataItemResult) {
                Log.d("Sync", "Synced");
            }
        });
    }
}
