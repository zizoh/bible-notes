package com.zizohanto.todoapp.addedittodo;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.zizohanto.todoapp.data.AppDatabase;

public class AddTodoViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final AppDatabase mDb;
    private final int mTodoId;

    public AddTodoViewModelFactory(AppDatabase database, int taskId) {
        mDb = database;
        mTodoId = taskId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new AddTodoViewModel(mDb, mTodoId);
    }
}
