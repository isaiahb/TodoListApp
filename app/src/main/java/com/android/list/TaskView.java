package com.android.list;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * TODO: document your custom view class.
 */
public class TaskView extends LinearLayout {
    private TextView title;
    private Button moveStateButton;
    private Button editButton;
    private Task task;


    public TaskView(Context context) {
        super(context);
        init(null, 0);
    }

    public TaskView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public TaskView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        Context context = getContext();
        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Task, defStyle, 0);
        array.recycle();

        LayoutInflater.from(getContext()).inflate(R.layout.task_view, this);

        moveStateButton = (Button) findViewById(R.id.move);
        editButton = (Button) findViewById(R.id.edit);
        title = (TextView) findViewById(R.id.title);

        setPadding(0,iBall.dpToPx(1),0,iBall.dpToPx(1));

        final TaskView taskView = this;
        moveStateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (task == null) return;
                TaskState oldState, newState;
                oldState = task.getTaskState();
                switch (task.getTaskState()) {
                    case TODO:
                        newState = TaskState.WIP;
                        break;
                    case WIP:
                        newState = TaskState.DONE;
                        break;
                    case DONE:
                        newState = TaskState.TODO;
                        break;
                    default:
                        newState = TaskState.TODO;
                        break;
                }
                task.setTaskState(newState);

                MainActivity.mainActivity.moveTask(taskView, oldState, newState);
            }
        });

        editButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    public TaskView load(Task task) {
        title.setText(task.getTitle());
        this.task = task;

        switch (task.getTaskState()) {
            case TODO: {
                moveStateButton.setText("start");
                moveStateButton.setBackgroundResource(R.color.wip);
                break;
            }
            case WIP: {
                moveStateButton.setText("finish");
                moveStateButton.setBackgroundResource(R.color.done);
                break;

            }
            case DONE: {
                moveStateButton.setText("reopen");
                moveStateButton.setBackgroundResource(R.color.todo);
                break;

            }
        }

        return this;
    }

    public TaskView load() {
        if (task != null) load(task);
        return this;
    }


    public Task getTask() {
        return task;
    }
}
