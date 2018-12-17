package com.zizohanto.noteapp.addeditnote;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.zizohanto.noteapp.data.AppDatabase;
import com.zizohanto.noteapp.data.NoteEntry;

public class AddNoteViewModel extends ViewModel {
    private LiveData<NoteEntry> mNote;

    public AddNoteViewModel(AppDatabase database, int noteId) {
        mNote = database.noteDao().loadNoteById(noteId);
    }

    public LiveData<NoteEntry> getNote() {
        return mNote;
    }
}

