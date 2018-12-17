package com.zizohanto.todoapp.addedittodo;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.zizohanto.todoapp.R;
import com.zizohanto.todoapp.utils.ActivityUtils;

public class AddTodoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        int todoId = getIntent().getIntExtra(AddTodoFragment.EXTRA_TODO_ID, AddTodoFragment.DEFAULT_TODO_ID);

        AddTodoFragment addTodoFragment = (AddTodoFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (addTodoFragment == null) {
            addTodoFragment = AddTodoFragment.newInstance(todoId);

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    addTodoFragment, R.id.contentFrame);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
