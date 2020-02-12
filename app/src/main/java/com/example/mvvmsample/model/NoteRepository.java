package com.example.mvvmsample.model;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteRepository {
    private NoteDAO noteDAO;
    private LiveData<List<Note>> allNotes;

    public NoteRepository(Application application) {
        NoteDatabase noteDatabase = NoteDatabase.getInstance(application);
        noteDAO = noteDatabase.noteDAO();
        allNotes = noteDAO.getAllNotes();

    }

    public void insert(Note note) {
        new InsertNoteAsyncTask(noteDAO).execute(note);

    }

    public void update(Note note) {
        new UpdateNoteAsyncTask(noteDAO).execute(note);

    }

    public void delete(Note note) {
        new DeleteNoteAsyncTask(noteDAO).execute(note);

    }

    public void deleteAllNotes() {
        new DeleteAllNoteAsyncTask(noteDAO).execute();

    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    public static class InsertNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDAO noteDAO;

        public InsertNoteAsyncTask(NoteDAO noteDAO) {
            this.noteDAO = noteDAO;
        }


        @Override
        protected Void doInBackground(Note[] notes) {
            noteDAO.insert(notes[0]);
            return null;
        }
    }

    public static class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDAO noteDAO;

        public UpdateNoteAsyncTask(NoteDAO noteDAO) {
            this.noteDAO = noteDAO;
        }


        @Override
        protected Void doInBackground(Note[] notes) {
            noteDAO.update(notes[0]);
            return null;
        }
    }

    public static class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDAO noteDAO;

        public DeleteNoteAsyncTask(NoteDAO noteDAO) {
            this.noteDAO = noteDAO;
        }


        @Override
        protected Void doInBackground(Note[] notes) {
            noteDAO.delete(notes[0]);
            return null;
        }
    }

    public static class DeleteAllNoteAsyncTask extends AsyncTask<Void, Void, Void> {
        private NoteDAO noteDAO;

        public DeleteAllNoteAsyncTask(NoteDAO noteDAO) {
            this.noteDAO = noteDAO;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            noteDAO.deleteAll();
            return null;
        }
    }


}
