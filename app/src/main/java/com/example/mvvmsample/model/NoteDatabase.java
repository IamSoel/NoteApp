package com.example.mvvmsample.model;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = Note.class, version = 1, exportSchema = false)
public abstract class NoteDatabase extends RoomDatabase {
    public static NoteDatabase instance;

    public static synchronized NoteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), NoteDatabase.class, "note_database.db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .addCallback(roomCallback)
                    .build();

        }
        return instance;
    }

    public abstract NoteDAO noteDAO();

    private static RoomDatabase.Callback roomCallback=new RoomDatabase.Callback()
    {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void>
    {
        private NoteDAO noteDAO;
        private PopulateDbAsyncTask(NoteDatabase db)
        {
            noteDAO=db.noteDAO();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDAO.insert(new Note("First Note","Description 1"));
            noteDAO.insert(new Note("Second Note","Description 2"));
            return null;
        }
    }

}
