package dg.gautam.deepak.notes;

import android.content.Intent;
import android.icu.util.TimeZone;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import dg.gautam.deepak.notes.notes.Note;
import dg.gautam.deepak.notes.notes.NotesDatabaseHelper;
import dg.gautam.deepak.notes.notes.NotesRecyclerViewAdapter;

public class EditNotesActivity extends AppCompatActivity {
    Note note;
    EditText titleEditText;
    EditText contentEditText;
    TextView lastEditedText;
    NotesDatabaseHelper db;
    String lastEditedDate;
    private NotesRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Notes");
        Intent i = getIntent();
        note = (Note)i.getSerializableExtra("note");
        titleEditText = findViewById(R.id.editTextTitleAddNotes);
        contentEditText = findViewById(R.id.editTextContentAddNotes);
        db = new NotesDatabaseHelper(this);
        //mAdapter = new NotesRecyclerViewAdapter(this, notesList);
        lastEditedText=findViewById(R.id.lastEditedTextView);
        Calendar cal = Calendar.getInstance();

        try{
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(note.getTimestamp());
            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM dd, hh:mm aaa");
            lastEditedDate= fmtOut.format(date);
        }
        catch (ParseException p){
            Log.d("DateParseException", "onCreate: Dtae parse exception");
        }

        lastEditedText.setText("Edited "+lastEditedDate);
        titleEditText.setText(note.getTitle());
        contentEditText.setText(note.getContent());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.notes_menu,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                modifyNote();
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateNote() {
        // inserting note in db and getting
        // newly inserted note id
        Log.d("title_error", "updateNote: "+note.getTitle());
        long id = db.updateNote(note);


            // refreshing the list
           // mAdapter.notifyDataSetChanged();

            //toggleEmptyNotes();
          return;
    }

    public void onBackPressed() {
        modifyNote();
        super.onBackPressed();
    }

    public void modifyNote(){
        note.setTitle(titleEditText.getText().toString());
        note.setContent(contentEditText.getText().toString());
        Random rand = new Random();
        int randomNum= rand.nextInt(8);
        if(contentEditText.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(), "Note Content cant be empty", Toast.LENGTH_SHORT).show();
            return ;
        }
        updateNote();
    }

}
