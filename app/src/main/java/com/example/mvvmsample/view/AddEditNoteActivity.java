package com.example.mvvmsample.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mvvmsample.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddEditNoteActivity extends AppCompatActivity {
    public static final String TITLE_ID = "com.example.mvvmsample.view.Title_Id";
    public static final String TITLE_KEY = "com.example.mvvmsample.view.Title_Key";
    public static final String DESCRIPTION_KEY = "com.example.mvvmsample.view.Description_Key";
    @BindView(R.id.set_title)
    EditText title;

    @BindView(R.id.set_description)
    EditText description;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.linear_layout)
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent.hasExtra(TITLE_ID)) {
            toolbar.setTitle("Edit Note");
            title.setText(intent.getStringExtra(TITLE_KEY));
            description.setText(intent.getStringExtra(DESCRIPTION_KEY));
        } else {
            toolbar.setTitle("Add Note");
        }


    }

    private void saveNote() {
        String heading = title.getText().toString();
        String detail = description.getText().toString();
        if (heading.trim().isEmpty() && detail.trim().isEmpty()) {
            Snackbar.make(linearLayout, "Title and Description is empty", Snackbar.LENGTH_SHORT).show();
        } else {
            Intent data = new Intent();
            data.putExtra(TITLE_KEY, heading);
            data.putExtra(DESCRIPTION_KEY, detail);
            int id = getIntent().getIntExtra(TITLE_ID, -1);
            if (id != -1) {
                data.putExtra(TITLE_ID, id);
            }
            setResult(RESULT_OK, data);
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.addnote_toolbar, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (R.id.save_note == item.getItemId()) {
            saveNote();
        }

        return super.onOptionsItemSelected(item);
    }
}
