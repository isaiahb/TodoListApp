package com.android.list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    public static MainActivity mainActivity;
    public static String TAG = "iball";

    public static final User user = new User();
    private static final int MIN_SESSION_DURATION = 5000;
    private FirebaseAnalytics firebaseAnalytics;
    private FirebaseFirestore firestore;

    private TextView title;
    private ListFragment todoFragment, wipFragment, doneFragment, currentFragment;
    private ArrayList<Task> todoArray, wipArray, doneArray, currentArray;
    private LinearLayout header;
    private LinearLayout newTaskLayout, nameTaskLayout;
    private Button newTaskButton, cancelTaskButton, createTaskButton;
    private EditText newTaskName;
    private TaskState currentTaskState = TaskState.TODO;

    private Task parentTask;

    public BottomNavigationView navigation;

    public void moveTask(TaskView taskView, TaskState oldState, TaskState newState) {
        Task task = taskView.getTask();
        ArrayList<Task> oldArray, newArray;
        ListFragment oldFragment;

        switch (oldState) {
            case TODO:
                oldArray = todoArray;
                oldFragment = todoFragment;
                break;
            case WIP:
                oldArray = wipArray;
                oldFragment = wipFragment;
                break;
            case DONE:
                oldArray = doneArray;
                oldFragment = doneFragment;
                break;
            default:
                oldArray = todoArray;
                oldFragment = todoFragment;
                break;
        }

        switch (newState) {
            case TODO:
                newArray = todoArray;
                break;
            case WIP:
                newArray = wipArray;
                break;
            case DONE:
                newArray = doneArray;
                break;
            default:
                newArray = todoArray;
                break;
        }
        oldArray.remove(task);
        newArray.add(task);

        oldFragment.layout.removeView(taskView);
        firestore.collection("tasks").document(task.getID()).update("taskState", task.getTaskState().toString());
    }

    //Loads the different fragments
    private boolean loadFragment(ListFragment fragment) {
        if(fragment != null) {

            if (fragment.isAdded()) {
                getFragmentManager().popBackStackImmediate(fragment.title,0);
//              getFragmentManager().beginTransaction().show(fragment);

            } else {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment, fragment.title)
//                        .addToBackStack(fragment.title)
                        .commit();
                loadTasks();
            }

            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = this;
        setContentView(R.layout.activity_main);
        iBall.context = getApplicationContext();

        //wait 5 seconds before counting the app opening as a session
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        firebaseAnalytics.setMinimumSessionDuration(MIN_SESSION_DURATION);
        firestore = FirebaseFirestore.getInstance();

        header = findViewById(R.id.header);
        title = findViewById(R.id.title);
        todoFragment = new ListFragment(R.layout.todo_fragment);
        wipFragment = new ListFragment(R.layout.wip_fragment);
        doneFragment = new ListFragment(R.layout.done_fragment);
        currentFragment = todoFragment;

        todoFragment.setTitle("TODO");
        wipFragment.setTitle("WIP");
        doneFragment.setTitle("Done");

        todoArray = new ArrayList<>();
        wipArray = new ArrayList<>();
        doneArray = new ArrayList<>();
        currentArray = todoArray;

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        newTaskLayout = findViewById(R.id.new_task_linearlayout);
        nameTaskLayout = findViewById(R.id.name_new_task_linearlayout);
        newTaskButton = findViewById(R.id.new_task_button);
        cancelTaskButton = findViewById(R.id.cancel_new_task);
        createTaskButton = findViewById(R.id.create_new_task);
        newTaskName = findViewById(R.id.name_task_textbox);

        if (true) header.removeView(nameTaskLayout);

        newTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newTaskButton.setClickable(false);
                header.addView(nameTaskLayout);
            }
        });

        cancelTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newTaskButton.setClickable(true);
                header.removeView(nameTaskLayout);
            }
        });

        createTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newTaskButton.setClickable(true);
                header.removeView(nameTaskLayout);
                if (newTaskName.getText().length() <= 0) return;

                createNewTask(newTaskName.getText().toString(), parentTask);
                newTaskName.setText("");
            }
        });

        getFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, currentFragment, currentFragment.title)
                .addToBackStack(currentFragment.title).commit();
        loadTasksFromDatabase();
    }

    public void saveTask(Task task) {
        firestore.collection("tasks").document(task.getID());
    }



    public void loadTasksFromDatabase() {
        firestore.collection("tasks")
                .whereEqualTo("subtask", false)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Task mTask = new Task(document);
                                switch (mTask.getTaskState()) {
                                    case TODO:
                                        todoArray.add(mTask);
                                        break;
                                    case WIP:
                                        wipArray.add(mTask);
                                        break;
                                    case DONE:
                                        doneArray.add(mTask);
                                        break;
                                    default:
                                        todoArray.add(mTask);
                                        break;
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        loadTasks();
                    }
                });
    }
    public void createNewTask(String title, Task parentTask) {
        final Task task = new Task();
        task.setTitle(title);
        task.setID(title);
        task.setTaskState(currentTaskState);
        if (parentTask != null) {
            task.setParentTaskID(parentTask.getID());
            task.setSubtask(true);
        }
        currentArray.add(task);
        currentFragment.layout.addView(new TaskView(currentFragment.getContext()).load(task));
        firestore.collection("tasks").add(task).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                String id = documentReference.getId();
                Log.d(TAG, "DocumentSnapshot added with ID: " + id);
                task.setID(id);
                firestore.collection("tasks").document(id).update("id", id);
            }
        });
    }

    //Uses the current task array to create TaskViews and ads them to the current fragment (todoFragment, wipFragment, or doneFragment)
    public void loadTasks() {
        getFragmentManager().executePendingTransactions();
        for (Task task: currentArray) {
            Log.v("iball", task != null ? "task" : "task is null");
            currentFragment.layout.addView(new TaskView(currentFragment.getContext()).load(task));
        }
    }

    public void setTitle(String text) {
        title.setText(text);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        ListFragment fragment = null;

        //analytics
        Bundle params = new Bundle();
        params.putInt("FragmentID", item.getItemId());
        String buttonPressed = "Other";
        int id = item.getItemId();
        switch (id) {
            case R.id.navigation_todo : {
                fragment = todoFragment;
                buttonPressed = "Todo";
                navigation.setBackgroundResource(R.color.todo);
                header.setBackgroundResource(R.color.todo);
                currentArray = todoArray;
                currentTaskState = TaskState.TODO;
                break;
            }

            case R.id.navigation_wip : {
                fragment = wipFragment;
                buttonPressed = "WIP";
                navigation.setBackgroundResource(R.color.wip);
                header.setBackgroundResource(R.color.wip);
                currentArray = wipArray;
                currentTaskState = TaskState.WIP;
                break;
            }

            case R.id.navigation_done : {
                fragment = doneFragment;
                buttonPressed = "Done";
                navigation.setBackgroundResource(R.color.done);
                header.setBackgroundResource(R.color.done);
                currentArray = doneArray;
                currentTaskState = TaskState.DONE;
                break;
            }

            default:
                break;
        }

        navigation.getMenu().findItem(id).setChecked(true);
        setTitle(buttonPressed);
        currentFragment = fragment;
        loadFragment(fragment);

        firebaseAnalytics.logEvent(buttonPressed, params);

        return false;
    }

}
