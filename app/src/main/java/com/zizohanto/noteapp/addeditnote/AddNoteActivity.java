package com.zizohanto.noteapp.addeditnote;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.zizohanto.noteapp.R;
import com.zizohanto.noteapp.utils.ActivityUtils;

public class AddNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        int noteId = getIntent().getIntExtra(AddNoteFragment.EXTRA_NOTE_ID, AddNoteFragment.DEFAULT_NOTE_ID);

        if (noteId == AddNoteFragment.DEFAULT_NOTE_ID) {
            actionBar.setTitle("Add Note");
        } else {
            actionBar.setTitle("Edit Note");
        }

        AddNoteFragment addNoteFragment = (AddNoteFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (addNoteFragment == null) {
            addNoteFragment = AddNoteFragment.newInstance(noteId);

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    addNoteFragment, R.id.contentFrame);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
