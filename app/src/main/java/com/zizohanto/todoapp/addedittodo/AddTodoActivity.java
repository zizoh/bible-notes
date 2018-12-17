package com.zizohanto.todoapp.addedittodo;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zizohanto.todoapp.R;
import com.zizohanto.todoapp.data.TodoEntry;
import com.zizohanto.todoapp.todos.MainViewModel;
import com.zizohanto.todoapp.utils.ActivityUtils;

import java.util.List;

public class AddTodoActivity extends AppCompatActivity {

    public static final String ANONYMOUS = "anonymous";
    private String mUsername;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mFBRef;

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

//        initViews();
//
//        mLocalDb = AppDatabase.getInstance(getApplicationContext());
//
//        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_TODO_ID)) {
//            mTodoId = savedInstanceState.getInt(INSTANCE_TODO_ID, DEFAULT_TODO_ID);
//        }
//
//        Intent intent = getIntent();
//        if (intent != null && intent.hasExtra(EXTRA_TODO_ID)) {
//            //mButton.setText(R.string.update_button);
//            if (mTodoId == DEFAULT_TODO_ID) {
//                // populate the UI
//                mTodoId = intent.getIntExtra(EXTRA_TODO_ID, DEFAULT_TODO_ID);
//
//                AddTodoViewModelFactory factory = new AddTodoViewModelFactory(mLocalDb, mTodoId);
//
//                final AddTodoViewModel viewModel
//                        = ViewModelProviders.of(this, factory).get(AddTodoViewModel.class);
//
//
//                viewModel.getTodo().observe(this, new Observer<TodoEntry>() {
//                    @Override
//                    public void onChanged(@Nullable TodoEntry todoEntry) {
//                        viewModel.getTodo().removeObserver(this);
//                        populateUI(todoEntry);
//                    }
//                });
//            }
//        }
//
//        mEditText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
//            // Called when the action mode is created; startActionMode() was called
//            @Override
//            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
//                // Inflate a menu resource providing context menu items
//                MenuInflater inflater = actionMode.getMenuInflater();
//                inflater.inflate(R.menu.edit_note_context_menu, menu);
//                return true;
//            }
//
//            // Called each time the action mode is shown. Always called after onCreateActionMode, but
//            // may be called multiple times if the mode is invalidated.
//            @Override
//            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
//                return false; // Return false if nothing is done
//            }
//
//            // Called when the user selects a contextual menu item
//            @Override
//            public boolean onActionItemClicked(ActionMode actionMode, MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.menu_bible:
//                        openBible();
//                        actionMode.finish();
//                        return true;
//                    default:
//                        return false;
//                }
//            }
//
//            // Called when the user exits the action mode
//            @Override
//            public void onDestroyActionMode(ActionMode actionMode) {
//            }
//        });

        /*// Set default username is anonymous.
        mUsername = ANONYMOUS;

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser != null) {
            mUsername = mFirebaseUser.getDisplayName();
        }

        mFirebaseDatabase = FirebaseDatabase.getInstance();*/
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_sync) {
            //syncToFirebase();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void syncToFirebase() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getTodos().observe(this, new Observer<List<TodoEntry>>() {
            @Override
            public void onChanged(@Nullable List<TodoEntry> todoEntries) {
                mFBRef = mFirebaseDatabase.getReference("user");
                for (TodoEntry entry : todoEntries) {
                    mFBRef.child("users").child(mUsername).setValue(entry);
                }
//                Log.d(TAG, "Syncing todos to Firebase");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.todos_activity_menu, menu);
        return true;
    }
}
