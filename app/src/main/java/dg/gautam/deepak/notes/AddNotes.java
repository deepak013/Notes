package dg.gautam.deepak.notes;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import dg.gautam.deepak.notes.notes.Note;
import dg.gautam.deepak.notes.notes.NotesDatabaseHelper;
import dg.gautam.deepak.notes.notes.NotesRecyclerViewAdapter;

public class AddNotes extends AppCompatActivity {
    NotesDatabaseHelper db;
    private NotesRecyclerViewAdapter mAdapter;
    private Note note;
    private List<Note> notesList = new ArrayList<>();
    EditText titleEditText;
    EditText contentEditText;
    String hexColorArray[]={"#F7786B", "#20b2aa", "#800080", "#009B77", "#9B2335", "#4C6A92", "#8a2be2", "#B76BA3"};
    TextView lastEditedText;
    String currentDateandTime;
    SharedPreferences sharedPreferences;
    int numberForColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Notes");
        titleEditText = findViewById(R.id.editTextTitleAddNotes);
        contentEditText = findViewById(R.id.editTextContentAddNotes);
        db = new NotesDatabaseHelper(this);
        mAdapter = new NotesRecyclerViewAdapter(this, notesList);
        lastEditedText=findViewById(R.id.lastEditedTextView);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, hh:mm aaa");
        currentDateandTime = sdf.format(new Date());
        lastEditedText.setText("Edited "+currentDateandTime.toString());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        numberForColor = sharedPreferences.getInt("color",0);
        numberForColor%=8;
        note = new Note();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.add_edit_menu,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                makeNote();
                this.finish();
                return true;
            case R.id.action_save_note:
                makeNote();
                this.finish();
                return true;
            case R.id.action_discard_note:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createNote() {
        // inserting note in db and getting
        // newly inserted note id
        Log.d("title_error", "createNote: "+note.getTitle());
        long id = db.insertNote(note);

        // get the newly inserted note from db
        Note n = db.getNote(id);

        if (n != null) {
            // adding new note to array list at 0 position
            notesList.add(0, n);

            // refreshing the list
            mAdapter.notifyDataSetChanged();

            //toggleEmptyNotes();
        }
        numberForColor++;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("color", numberForColor);
        editor.apply();
    }

    public void onBackPressed() {
        makeNote();
        super.onBackPressed();
    }

    public void makeNote(){
        String titleString = titleEditText.getText().toString();
        String contentString = contentEditText.getText().toString();
        note.setTitle(titleString);
        note.setContent(contentString);
        Random rand = new Random();
        int randomNum= rand.nextInt(8);
        note.setBackground(hexColorArray[numberForColor]);
        note.setIsFavourite(0);
        note.setIsInTrash(0);
        note.setIsDeleted(0);
        note.setLastEdited(currentDateandTime.toString());
        if(contentString.trim().equals("")){
            Toast.makeText(getApplicationContext(), "Note Content can't be empty", Toast.LENGTH_SHORT).show();
            return ;
        }
        createNote();
    }


}
