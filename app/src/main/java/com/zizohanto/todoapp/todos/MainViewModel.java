package com.zizohanto.todoapp.todos;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.zizohanto.todoapp.data.AppDatabase;
import com.zizohanto.todoapp.data.TodoEntry;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<TodoEntry>> todos;

    public MainViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the todos from the DataBase");
        todos = database.todoDao().loadAllTodos();
    }

    public LiveData<List<TodoEntry>> getTodos() {
        return todos;
    }
}
