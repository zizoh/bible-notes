package com.zizohanto.todoapp.addedittodo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.zizohanto.todoapp.data.AppDatabase;
import com.zizohanto.todoapp.data.TodoEntry;

public class AddTodoViewModel extends ViewModel {
    private LiveData<TodoEntry> todo;

    public AddTodoViewModel(AppDatabase database, int todoId) {
        todo = database.todoDao().loadTodoById(todoId);
    }

    public LiveData<TodoEntry> getTodo() {
        return todo;
    }
}

