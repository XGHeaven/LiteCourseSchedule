package com.xgheaven.litecourseschedule;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.DefaultOffsettingHelper;
import android.support.wearable.view.WearableRecyclerView;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataItemBuffer;
import com.google.android.gms.wearable.Wearable;

public class MainActivity extends WearableActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, DataApi.DataListener{

    private WearableRecyclerView courseView;
    private CourseAdapter adapter;
    private GoogleApiClient google;
    private CourseList courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        google = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();

        courseList = new CourseList(getApplicationContext());
        courseList.load();
        courseView = (WearableRecyclerView)findViewById(R.id.course_list);
        courseView.setCenterEdgeItems(true);
        courseView.setOffsettingHelper(new DefaultOffsettingHelper());
        courseView.setCircularScrollingGestureEnabled(true);
        adapter = new CourseAdapter(courseList);
        courseView.setAdapter(adapter);

        updateCourse();
    }

    @Override
    protected void onStop() {
        super.onStop();
        google.disconnect();
        Wearable.DataApi.removeListener(google, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        google.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("GoogleApi", "Connected");
        Wearable.DataApi.addListener(google, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("GoogleApi", i + "");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("GoogleApi", "Connection Failed," + connectionResult);
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        Log.i("GoogleApi", "Data Changed");
        for (DataEvent event : dataEventBuffer) {
            DataItem dataItem = event.getDataItem();
            if (dataItem.getUri().getPath().equals("/courses")) {
                courseList.clear();
                courseList.load(new String(dataItem.getData()));
                adapter.notifyDataSetChanged();
            }
        }
        dataEventBuffer.release();
    }

    public void updateCourse() {
        Log.i("CourseData", "Update");
        PendingResult<DataItemBuffer> result = Wearable.DataApi.getDataItems(google);
        result.setResultCallback(new ResultCallback<DataItemBuffer>() {
            @Override
            public void onResult(@NonNull DataItemBuffer dataItems) {
                for (DataItem data : dataItems) {
                    Log.d("CourseData", data.getUri().getPath());
                    if (data.getUri().getPath().equals("/courses")) {
                        courseList.clear();
                        courseList.load(new String(data.getData()));
                        adapter.notifyDataSetChanged();
                        Log.i("CourseData", "Updated");
                    }
                }
                dataItems.release();
            }
        });
    }
}
