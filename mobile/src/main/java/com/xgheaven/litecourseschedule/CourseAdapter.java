package com.xgheaven.litecourseschedule;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.BaseViewHolder> {
    private static final int DIVIDE_TYPE = 0, DATA_TYPE = 1;
    private CourseList courseList;

    public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

        protected Context context;

        public BaseViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            itemView.setTag(this);
        }

        abstract public void setData(Course course);
    }

    private class CourseViewHolder extends BaseViewHolder {
        public LinearLayout body;
        public TextView name, info, startTime, endTime, progress;
        public FrameLayout more_action;
        private Course course;
        private RecyclerView.Adapter adapter;
        public CourseViewHolder(View itemView, final Context context) {
            super(itemView, context);
            name = (TextView) itemView.findViewById(R.id.course_name);
            info = (TextView) itemView.findViewById(R.id.course_info);
            startTime = (TextView) itemView.findViewById(R.id.course_start_time);
            endTime = (TextView) itemView.findViewById(R.id.course_end_time);
            progress = (TextView) itemView.findViewById(R.id.course_progress);
            this.adapter = CourseAdapter.this;

            itemView.findViewById(R.id.course_item_body).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Msg.confirm(context, R.string.course_delete_title, R.string.course_delete_message, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int pos = CourseList.getInstance().remove(course);
                            adapter.notifyItemRemoved(pos);
                            Msg.info(((MainActivity)context).findViewById(R.id.main_course_list), R.string.course_delete_success);
                        }
                    }, null);
                    Log.d("Adapter", "LongClick");
                    return true;
                }
            });

            itemView.findViewById(R.id.course_item_body).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent transitionIntent = new Intent(context, CourseDetail.class);
                    transitionIntent.putExtra("courseIndex", CourseList.getInstance().indexOf(course));

                    TextView name = (TextView) v.findViewById(R.id.course_name);
                    Pair<View, String> namePair = Pair.create((View) name, "course_name");

                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((MainActivity)context, namePair);
                    ActivityCompat.startActivityForResult((MainActivity)context, transitionIntent, 100, options.toBundle());
                }
            });
        }

        @Override
        public void setData(Course course) {
            this.course = course;
            name.setText(course.getName());
            info.setText(course.getClassroom() + " （" + course.getStart() + "-" + course.getEnd() + "）");
            startTime.setText(Course.COURSE_TIMES.get(course.getStart() - 1).first);
            endTime.setText(Course.COURSE_TIMES.get(course.getEnd() - 1).second);

            int res = 0;
            switch (course.isCurrent()) {
                case -1:
                    res = R.color.courseAccessing;
                    break;
                case 0:
                    res = R.color.courseProgressing;
                    break;
                case 1:
                    res = R.color.courseFinished;
                    break;
            }

            progress.setBackgroundResource(res);
        }
    }

    private class DivideViewHolder extends BaseViewHolder {

        public TextView header;

        public DivideViewHolder(View itemView, Context context) {
            super(itemView, context);
            header = (TextView) itemView.findViewById(R.id.list_subheader);
        }

        @Override
        public void setData(Course course) {
            String[] res = context.getResources().getStringArray(R.array.week_name);
            header.setText(res[course.getDay()]);
        }
    }

    public CourseAdapter(CourseList courseList) {
        this.courseList = courseList;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder vh = null;
        View v;

        switch (viewType) {
            case DATA_TYPE:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_item, parent, false);
                vh = new CourseViewHolder(v, parent.getContext());
                break;
            case DIVIDE_TYPE:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_subheader, parent, false);
                vh = new DivideViewHolder(v, parent.getContext());
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.setData(courseList.get(position));
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return courseList.isDivide(position) ? DIVIDE_TYPE : DATA_TYPE;
    }
}
