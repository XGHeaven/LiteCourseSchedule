package com.xgheaven.litecourseschedule;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.BaseViewHolder> {
    private static final int DIVIDE_TYPE = 0, DATA_TYPE = 1;

    public static abstract class BaseViewHolder extends RecyclerView.ViewHolder {

        protected Context context;

        public BaseViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            itemView.setTag(this);
        }

        abstract public void setData(Course course);
    }

    private static class CourseViewHolder extends BaseViewHolder {
        public LinearLayout body;
        public TextView name, info;
        public FrameLayout more_action;
        private boolean isExpand = false;
        private Course course;
        private RecyclerView.Adapter adapter;
        public CourseViewHolder(View itemView, final Context context, final RecyclerView.Adapter adapter) {
            super(itemView, context);
            body = (LinearLayout)itemView;
            name = (TextView) itemView.findViewById(R.id.course_name);
            info = (TextView) itemView.findViewById(R.id.course_info);
            more_action = (FrameLayout) itemView.findViewById(R.id.course_more_action);
            this.adapter = adapter;

            itemView.findViewById(R.id.more_action_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CourseViewHolder.this.toggleExpand();
                }
            });

            more_action.findViewById(R.id.list_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = Course.remove(course);
                    CourseViewHolder.this.adapter.notifyItemRemoved(position);
                    ((MainActivity)CourseViewHolder.this.context).syncWithWear();
                    Msg.info(((MainActivity)CourseViewHolder.this.context).course_list, R.string.course_delete_success);
                }
            });

            more_action.findViewById(R.id.list_edit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CourseAdd dialog = new CourseAdd(context, course);
                    final int oPos = Course.indexOf(course);
                    dialog.show(new CourseAdd.CourseAddedCallback() {
                        @Override
                        public void run(int position) {
                            adapter.notifyItemMoved(oPos, position);
                            adapter.notifyItemChanged(position);
                            ((MainActivity)CourseViewHolder.this.context).syncWithWear();
                            Msg.info(((MainActivity)CourseViewHolder.this.context).course_list, R.string.course_edit_success);
                        }
                    });
                }
            });

        }

        @Override
        public void setData(Course course) {
            this.course = course;
            name.setText(course.getName());
            info.setText(course.getClassroom() + " （" + course.getStart() + "-" + course.getEnd() + "）");
            unExpand();
        }

        public void toggleExpand() {
            if (isExpand) unExpand();
            else doExpand();
        }

        public void doExpand() {
            more_action.setVisibility(View.VISIBLE);
            isExpand = true;
        }

        public void unExpand() {
            more_action.setVisibility(View.GONE);
            isExpand = false;
        }
    }

    private static class DivideViewHolder extends BaseViewHolder {

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

    public CourseAdapter() {}

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder vh = null;
        View v;

        switch (viewType) {
            case DATA_TYPE:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_item, parent, false);
                vh = new CourseViewHolder(v, parent.getContext(), this);
                break;
            case DIVIDE_TYPE:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_subheader, parent, false);
                vh = new DivideViewHolder(v, parent.getContext());
        }

        Log.d("CourseAdapter", viewType + "");
        return vh;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.setData(Course.get(position));
    }

    @Override
    public int getItemCount() {
        Log.i("CourseAdapter", Course.size() + "");
        return Course.size();
    }

    @Override
    public int getItemViewType(int position) {
        return Course.isDevide(position) ? DIVIDE_TYPE : DATA_TYPE;
    }
}
