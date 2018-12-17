package com.zizohanto.noteapp.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

@Entity(tableName = "note")
@IgnoreExtraProperties
public class NoteEntry {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String description;
    @ColumnInfo(name = "updated_at")
    private Date updatedAt;

    @Ignore
    public NoteEntry() {
        // Default constructor required for calls to DataSnapshot.getValue(NoteEntry.class)
    }

    @Ignore
    public NoteEntry(String description, Date updatedAt) {
        this.description = description;
        this.updatedAt = updatedAt;
    }

    public NoteEntry(int id, String description, Date updatedAt) {
        this.id = id;
        this.description = description;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
