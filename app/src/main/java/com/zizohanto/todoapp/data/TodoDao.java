package com.zizohanto.todoapp.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TodoDao {
    @Query("SELECT * FROM todo")
    LiveData<List<TodoEntry>> loadAllTodos();

    @Insert
    void insertTodo(TodoEntry todoEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTodo(TodoEntry todoEntry);

    @Delete
    void deleteTodo(TodoEntry todoEntry);

    @Query("SELECT * FROM todo WHERE id = :id")
    LiveData<TodoEntry> loadTodoById(int id);
}
