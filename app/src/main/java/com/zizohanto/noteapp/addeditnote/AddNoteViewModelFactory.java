package com.zizohanto.noteapp.addeditnote;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.zizohanto.noteapp.data.AppDatabase;

public class AddNoteViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final AppDatabase mDb;
    private final int mNoteId;

    public AddNoteViewModelFactory(AppDatabase database, int noteId) {
        mDb = database;
        mNoteId = noteId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new AddNoteViewModel(mDb, mNoteId);
    }
}
