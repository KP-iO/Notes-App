package com.example.notesapp;
/**
 * Created by khrishawn
 */
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Editor extends AppCompatActivity {

    private String action;
    private EditText editor;
    private String noteFilter;
    private String oldText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editor = findViewById(R.id.editText);

        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(NotesProvider.CONTENT_ITEM_TYPE);

        if(uri == null) {
                action = Intent.ACTION_INSERT;
                setTitle(getString(R.string.new_note));
       } else {
            action = Intent.ACTION_EDIT;
           noteFilter = DBOpenHelper.NOTE_ID + "=" + uri.getLastPathSegment();

           Cursor cursor = getContentResolver().query(uri,
                   DBOpenHelper.ALL_COLUMNS, noteFilter,null,null);

           cursor.moveToFirst();
           oldText = cursor.getString(cursor.getColumnIndex(DBOpenHelper.NOTE_TEXT));

           editor.setText(oldText);
           editor.requestFocus();
        }
    }
// Creates menu option in top right
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(action.equals(Intent.ACTION_EDIT)) {
            getMenuInflater().inflate(R.menu.menu_editor, menu);
        }
        return true;
    }
// tells app what to do in each case if a certain button is pressed
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishedEditing();
                break;
            case R.id.action_delete:
                deleteNote();
                break;
            case R.id.action_Share:
                Intent Intent = new Intent();
                Intent.setAction(Intent.ACTION_SEND);
                Intent.putExtra(Intent.EXTRA_TEXT, editor.getText().toString());
                Intent.setType("text/plain");
                startActivity(Intent);
                break;
        }
        return true;
    }
// Function to delete notes
    private void deleteNote() {
        getContentResolver().delete(NotesProvider.CONTENT_URI,
                noteFilter, null);
        Toast.makeText(this, R.string.note_deleted, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }
// Function when user is finished editing
    private void finishedEditing() {

        String newText = editor.getText().toString().trim();

        switch (action) {
            case Intent.ACTION_INSERT:
                if(newText.length() == 0) {
                    setResult(RESULT_CANCELED);
                } else {
                    insertNote(newText);
                }
                break;
            case Intent.ACTION_EDIT:
                if (newText.length() == 0) {
                    deleteNote();
                } else if (oldText.equals(newText)) {
                    setResult(RESULT_CANCELED);
                } else {
                    updateNote(newText);
                }
        }
        finish();
    }
// function to update notes
    private void updateNote(String noteText) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT, noteText);
        getContentResolver().update(NotesProvider.CONTENT_URI, values, noteFilter,null);
        Toast.makeText(this, R.string.note_updated, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }
// function that inserts notes into database
    private void insertNote(String noteText) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT, noteText);

        getContentResolver().insert(NotesProvider.CONTENT_URI, values);
        setResult(RESULT_OK);
    }
// function which tells app what to do when back is pressed
    @Override
    public void onBackPressed() {
        finishedEditing();
    }


}
