package com.zizohanto.noteapp.notes;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.zizohanto.noteapp.data.AppDatabase;
import com.zizohanto.noteapp.data.NoteEntry;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<NoteEntry>> mNotes;

    public MainViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the notes from the DataBase");
        mNotes = database.noteDao().loadAllNotes();
    }

    public LiveData<List<NoteEntry>> getNotes() {
        return mNotes;
    }
}
