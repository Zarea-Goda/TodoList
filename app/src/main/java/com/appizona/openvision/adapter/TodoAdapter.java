package com.appizona.openvision.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appizona.openvision.R;
import com.appizona.openvision.callback.OnTodoClickListener;
import com.appizona.openvision.model.Todo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yehia on 15/09/17.
 */

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoHolder> {

    private Context mContext;
    private List<Todo> list;

    private OnTodoClickListener onTodoClickListener;

    public TodoAdapter(Context context, List<Todo> todos) {
        this.mContext = context;
        this.list = todos;
    }

    public void setOnTodoClickListener(OnTodoClickListener onTodoClickListener) {
        this.onTodoClickListener = onTodoClickListener;
    }

    public void updateTodo(List<Todo> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public TodoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false);
        return new TodoHolder(view);
    }

    @Override
    public void onBindViewHolder(final TodoHolder holder, int position) {
        final Todo todo = list.get(position);
        holder.title.setText(todo.getTitle());
        holder.timeStamp.setText(getDate(Long.parseLong(todo.getTimeStamp()), "dd/MM/yyyy hh:mm:ss.SSS"));
        holder.dueDate.setText(todo.getDueDate());
        holder.description.setText(todo.getDescription());

        if (todo.getDescription().length() < 20) {
            holder.showMore.setVisibility(View.GONE);
            holder.description.setText(todo.getDescription());
        } else {
            holder.showMore.setVisibility(View.VISIBLE);
            String tmpDescription = todo.getDescription().substring(0, 20);
            holder.description.setText(tmpDescription + "...");
        }


        holder.showMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.showMore.setVisibility(View.GONE);
                holder.description.setText(todo.getDescription());
            }
        });


        if (todo.isDone().equals("true")) {
            holder.doneCB.setChecked(true);
        } else {
            holder.doneCB.setChecked(false);
        }


        holder.doneCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                Toast.makeText(mContext, "Changed!", Toast.LENGTH_SHORT).show();
            }
        });

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTodoClickListener.onTodoClicked(todo);
            }
        });

    }

    private String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class TodoHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.todo_title)
        TextView title;
        @BindView(R.id.todo_done_CB)
        CheckBox doneCB;
        @BindView(R.id.todo_time_stamp)
        TextView timeStamp;
        @BindView(R.id.todo_due_date)
        TextView dueDate;
        @BindView(R.id.todo_description)
        TextView description;
        @BindView(R.id.todo_show_more)
        TextView showMore;
        @BindView(R.id.root)
        LinearLayout root;

        public TodoHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
