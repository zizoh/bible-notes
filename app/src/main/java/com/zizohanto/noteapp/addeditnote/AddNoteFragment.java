package com.zizohanto.noteapp.addeditnote;

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
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.zizohanto.noteapp.AppExecutors;
import com.zizohanto.noteapp.R;
import com.zizohanto.noteapp.data.AppDatabase;
import com.zizohanto.noteapp.data.NoteEntry;
import com.zizohanto.noteapp.utils.BibleUtils;

import java.util.Date;
import java.util.List;

public class AddNoteFragment extends Fragment {
    // Extra for the note ID to be received in the intent
    public static final String EXTRA_NOTE_ID = "extraNoteId";
    // Extra for the note ID to be received after rotation
    public static final String INSTANCE_NOTE_ID = "instanceNoteId";

    // Constant for default note id to be used when not in update mode
    public static final int DEFAULT_NOTE_ID = -1;

    private static final String TAG = AddNoteFragment.class.getSimpleName();

    // Fields for views
    EditText mNote;

    private int mNoteId = DEFAULT_NOTE_ID;

    // Member variable for the Database
    private AppDatabase mLocalDb;

    public static AddNoteFragment newInstance(@Nullable int noteId) {
        Bundle arguments = new Bundle();
        arguments.putInt(EXTRA_NOTE_ID, noteId);
        AddNoteFragment fragment = new AddNoteFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLocalDb = AppDatabase.getInstance(getActivity().getApplicationContext());

        if (null != getArguments()) {
            mNoteId = getArguments().getInt(EXTRA_NOTE_ID, DEFAULT_NOTE_ID);
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AddNoteViewModelFactory factory = new AddNoteViewModelFactory(mLocalDb, mNoteId);

        final AddNoteViewModel viewModel
                = ViewModelProviders.of(this, factory).get(AddNoteViewModel.class);


        viewModel.getNote().observe(this, new Observer<NoteEntry>() {
            @Override
            public void onChanged(@Nullable NoteEntry noteEntry) {
                viewModel.getNote().removeObserver(this);
                populateUI(noteEntry);
            }
        });

        mNote.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
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
        View root = inflater.inflate(R.layout.frag_add_note, container, false);
        setHasOptionsMenu(true);
        mNote = (EditText) root.findViewById(R.id.et_note);

        // Set up floating action button
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_edit_task);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = mNote.getText().toString();
                Date date = new Date();

                final NoteEntry note = new NoteEntry(description, date);
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (mNoteId == DEFAULT_NOTE_ID) {
                            mLocalDb.noteDao().insertNote(note);
                        } else {
                            note.setId(mNoteId);
                            mLocalDb.noteDao().updateNote(note);
                        }
                    }
                });
                getActivity().finish();
            }
        });

        return root;
    }

    private void populateUI(NoteEntry note) {
        if (note == null) {
            return;
        }

        mNote.setText(note.getDescription());
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
        return mNote.getText().subSequence(mNote.getSelectionStart(), mNote.getSelectionEnd());
    }
}
