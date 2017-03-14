package com.xgheaven.litecourseschedule;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {
    public RecyclerView courseView;
    private Toolbar toolbar;
    private CourseAdapter courseAdapter;
    private CourseList courseList;

    private GoogleApiClient google;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        google.connect();
    }

    @Override
    protected void onStop() {
        google.disconnect();
        courseList.save();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        finishAndRemoveTask();
    }

    @Override
    protected void onDestroy() {
        Log.i("Main", "Destroy");
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                CourseAdd dialog = new CourseAdd(this);
                dialog.show(new CourseAdd.CourseAddedCallback() {
                    @Override
                    public void run(Course course, boolean isEdit) {
                        int pos = courseList.add(course);
                        courseAdapter.notifyItemInserted(pos);
                        syncWithWear();
                        Msg.info(courseView, R.string.course_add_success);
                    }
                });
                break;
            case R.id.menu_add_from_scan:
                Log.i("Scan", "Start Scan");
                IntentIntegrator intentIntegrator = new IntentIntegrator(this);
                intentIntegrator.initiateScan();
                break;
            case R.id.menu_sync:
                syncWithWear();
                break;
            case R.id.menu_about:
                Msg.about(this);
                break;
            default:
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        courseList = CourseList.getInstance(this);
        courseList.load();

        google = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();

        toolbar = (Toolbar)findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        courseAdapter = new CourseAdapter(courseList);
        courseView = (RecyclerView)findViewById(R.id.main_course_list);
        courseView.setLayoutManager(new LinearLayoutManager(this));
        courseView.setAdapter(courseAdapter);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        System.out.println(intent);

        if (requestCode == 100) {
            if (intent != null) {
                int index = intent.getIntExtra("editedIndex", -1);
                if (index != -1) {
                    courseAdapter.notifyItemChanged(index);
                }
            }
            return;
        }

        // qrcode scan

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (result == null) {
            Log.e("Scan", "Can't Scan");
            Msg.info(courseView, R.string.not_found_scanner);
            return;
        }


        String string = result.getContents();
        try {
            Log.d("Scan", string);
            courseList.clear();
            courseList.load(string);
            courseAdapter.notifyDataSetChanged();
            Msg.info(courseView, R.string.course_add_success);
        } catch (Exception e) {
            Msg.info(courseView, R.string.course_scan_failed);
        }
    }

    public void syncWithWear() {
        Log.d("Sync", "Start Sync");
        courseList.save();
        Msg.info(courseView, R.string.course_syncing);
        PutDataRequest putDataRequest = PutDataRequest.create("/courses");
        putDataRequest.setData(courseList.toJSON().getBytes());
        PendingResult<DataApi.DataItemResult> result = Wearable.DataApi.putDataItem(google, putDataRequest);
        result.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
            @Override
            public void onResult(@NonNull DataApi.DataItemResult dataItemResult) {
                Log.d("Sync", "Synced");
                Msg.info(courseView, R.string.course_sync_success);
            }
        });
    }
}
