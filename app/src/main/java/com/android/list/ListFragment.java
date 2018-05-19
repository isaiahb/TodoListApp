package com.android.list;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/*
 * Created by isaiah on 2018-04-10.
 */

@SuppressLint("ValidFragment")
public class ListFragment extends Fragment {
    public String title = "Title";
    public final int ID;
    public LinearLayout layout;

    public ListFragment(int id) {
        ID = id;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ScrollView fragment = (ScrollView)inflater.inflate(R.layout.todo_fragment, null);
        layout = fragment.findViewById(R.id.task_holder);
        return fragment;
    }
    public void setTitle(String title) {this.title = title;}
}