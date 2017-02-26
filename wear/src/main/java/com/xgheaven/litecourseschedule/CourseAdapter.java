package com.xgheaven.litecourseschedule;

import android.content.Context;
import android.support.wearable.view.WearableRecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by xgheaven on 26/02/2017.
 */

public class CourseAdapter extends WearableRecyclerView.Adapter<CourseAdapter.BaseViewHolder> {

    private final int DIVIDE_TYPE = 0, DATA_TYPE = 1;

    public static abstract class BaseViewHolder extends WearableRecyclerView.ViewHolder {

        protected View view;
        protected Context context;

        public BaseViewHolder(View itemView, Context context) {
            super(itemView);
            view = itemView;
            this.context = context;
        }

        public abstract void setData(Course course);
    }

    private static class DivideViewHolder extends BaseViewHolder {

        TextView text;

        public DivideViewHolder(View itemView, Context context) {
            super(itemView, context);
            text = (TextView) view.findViewById(R.id.list_subheader);
        }

        @Override
        public void setData(Course course) {
            String[] res = context.getResources().getStringArray(R.array.week_name);
            text.setText(res[course.getDay()]);
        }
    }

    private static class DataViewHolder extends BaseViewHolder {

        private TextView name, classroom, start, last;

        public DataViewHolder(View itemView, Context context) {
            super(itemView, context);
            name = (TextView)view.findViewById(R.id.course_name);
            classroom = (TextView) view.findViewById(R.id.course_classroom);
            start = (TextView) view.findViewById(R.id.course_start);
            last = (TextView) view.findViewById(R.id.course_last);
        }

        @Override
        public void setData(Course course) {
            name.setText(course.getName());
            classroom.setText(course.getClassroom());
            start.setText(course.getStart() + "");
            last.setText(course.getLast() + "");
        }
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder vh = null;
        View v = null;
        switch (viewType) {
            case DIVIDE_TYPE:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_subheader, null);
                vh = new DivideViewHolder(v, parent.getContext());
                break;
            case DATA_TYPE:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_item, null);
                vh = new DataViewHolder(v, parent.getContext());
                break;
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.setData(Course.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return Course.isDevide(position) ? DIVIDE_TYPE : DATA_TYPE;
    }

    @Override
    public int getItemCount() {
        return Course.size();
    }

}
