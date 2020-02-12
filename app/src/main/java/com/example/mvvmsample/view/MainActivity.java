package com.example.mvvmsample.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvmsample.R;
import com.example.mvvmsample.model.Note;
import com.example.mvvmsample.viewModel.NoteViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_NOTE_CODE = 1;
    public static final int REQUEST_EDIT_CODE = 2;
    @BindView(R.id.noteList_recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.button_addnote)
    FloatingActionButton addnote;

    @BindView(R.id.homelist_linearlayout)
    LinearLayout linearLayout;

    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Observable observable=Observable.just("A","B","C");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final RecyclerViewAdapter adapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.setNotes(notes);
                Toast.makeText(MainActivity.this, "inside onChanged", Toast.LENGTH_SHORT).show();
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {


                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));

                Snackbar.make(linearLayout, "Deleted:", Snackbar.LENGTH_LONG).show();

            }
        }).attachToRecyclerView(recyclerView);

        adapter.SetOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                intent.putExtra(AddEditNoteActivity.TITLE_ID,note.getId());
                intent.putExtra(AddEditNoteActivity.TITLE_KEY,note.getTitle());
                intent.putExtra(AddEditNoteActivity.DESCRIPTION_KEY,note.getDescription());
                noteViewModel.insert(note);
                startActivityForResult(intent,REQUEST_EDIT_CODE);
            }
        });

    }

    @OnClick(R.id.button_addnote)
    void addNote() {
        Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
        startActivityForResult(intent, REQUEST_NOTE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_NOTE_CODE && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditNoteActivity.TITLE_KEY);
            String description = data.getStringExtra(AddEditNoteActivity.DESCRIPTION_KEY);

            Note note = new Note(title, description);
            noteViewModel.insert(note);
        }
        else if (requestCode == REQUEST_EDIT_CODE && resultCode == RESULT_OK)
        {
            int id=data.getIntExtra(AddEditNoteActivity.TITLE_ID,-1);
            String title = data.getStringExtra(AddEditNoteActivity.TITLE_KEY);
            String description = data.getStringExtra(AddEditNoteActivity.DESCRIPTION_KEY);
            Note note=new Note(title,description);
            note.setId(id);
            noteViewModel.update(note);
        }
    }
}
