package com.zizohanto.todoapp.addedittodo;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.zizohanto.todoapp.AppExecutors;
import com.zizohanto.todoapp.R;
import com.zizohanto.todoapp.data.AppDatabase;
import com.zizohanto.todoapp.data.TodoEntry;
import com.zizohanto.todoapp.utils.BibleUtils;

import java.util.Date;
import java.util.List;

public class AddTodoFragment extends Fragment {
    // Extra for the to-do ID to be received in the intent
    public static final String EXTRA_TODO_ID = "extraTodoId";
    // Extra for the to-do ID to be received after rotation
    public static final String INSTANCE_TODO_ID = "instanceTodoId";

    // Constant for default to-do id to be used when not in update mode
    public static final int DEFAULT_TODO_ID = -1;

    private static final String TAG = AddTodoFragment.class.getSimpleName();

    // Fields for views
    EditText mEditText;

    private int mTodoId = DEFAULT_TODO_ID;

    // Member variable for the Database
    private AppDatabase mLocalDb;

    public static AddTodoFragment newInstance(@Nullable int todoId) {
        Bundle arguments = new Bundle();
        arguments.putInt(EXTRA_TODO_ID, todoId);
        AddTodoFragment fragment = new AddTodoFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLocalDb = AppDatabase.getInstance(getActivity().getApplicationContext());

        if (null != getArguments()) {
            mTodoId = getArguments().getInt(EXTRA_TODO_ID, DEFAULT_TODO_ID);
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.e(TAG, String.valueOf(mTodoId));
        AddTodoViewModelFactory factory = new AddTodoViewModelFactory(mLocalDb, mTodoId);

        final AddTodoViewModel viewModel
                = ViewModelProviders.of(this, factory).get(AddTodoViewModel.class);


        viewModel.getTodo().observe(this, new Observer<TodoEntry>() {
            @Override
            public void onChanged(@Nullable TodoEntry todoEntry) {
                viewModel.getTodo().removeObserver(this);
                populateUI(todoEntry);
            }
        });

        mEditText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            // Called when the action mode is created; startActionMode() was called
            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                // Inflate a menu resource providing context menu items
                MenuInflater inflater = actionMode.getMenuInflater();
                inflater.inflate(R.menu.edit_note_context_menu, menu);
                return true;
            }

            // Called each time the action mode is shown. Always called after onCreateActionMode, but
            // may be called multiple times if the mode is invalidated.
            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false; // Return false if nothing is done
            }

            // Called when the user selects a contextual menu item
            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_bible:
                        openBible();
                        actionMode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            // Called when the user exits the action mode
            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_add_todo, container, false);
        setHasOptionsMenu(true);
        mEditText = (EditText) root.findViewById(R.id.editTextTaskDescription);

        // Set up floating action button
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_edit_task);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = mEditText.getText().toString();
                Date date = new Date();

                final TodoEntry todo = new TodoEntry(description, date);
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (mTodoId == DEFAULT_TODO_ID) {
                            mLocalDb.todoDao().insertTodo(todo);
                        } else {
                            todo.setId(mTodoId);
                            mLocalDb.todoDao().updateTodo(todo);
                        }
                    }
                });
                getActivity().finish();
            }
        });

        return root;
    }

    private void populateUI(TodoEntry todo) {
        if (todo == null) {
            return;
        }

        mEditText.setText(todo.getDescription());
    }

    private void openBible() {
        CharSequence selectedText = getUserSelectedText();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(BibleUtils.getBibleUrl(selectedText)));

        // Verify there's at least one app installed that can handle the intent
        PackageManager packageManager = getActivity().getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        boolean isIntentSafe = activities.size() > 0;

        // Start an activity if it's safe
        if (isIntentSafe) {
            startActivity(intent);
        }
    }

    private CharSequence getUserSelectedText() {
        return mEditText.getText().subSequence(mEditText.getSelectionStart(), mEditText.getSelectionEnd());
    }
}
