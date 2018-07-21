package dg.gautam.deepak.notes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import dg.gautam.deepak.notes.notes.Note;
import dg.gautam.deepak.notes.notes.NotesDatabaseHelper;
import dg.gautam.deepak.notes.notes.NotesRecyclerViewAdapter;

public class EditNotesActivity extends AppCompatActivity {
    Note note;
    EditText titleEditText;
    EditText contentEditText;
    TextView lastEditedText;
    NotesDatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);
        Intent i = getIntent();
        note = (Note)i.getSerializableExtra("note");
        titleEditText = findViewById(R.id.editTextTitleAddNotes);
        contentEditText = findViewById(R.id.editTextContentAddNotes);
        db = new NotesDatabaseHelper(this);
        //mAdapter = new NotesRecyclerViewAdapter(this, notesList);
        lastEditedText=findViewById(R.id.lastEditedTextView);
        SimpleDateFormat f = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, hh:mm aaa");
        Date d =new Date();
        try{d = f.parse(note.getTimestamp());}
        catch (ParseException p){
            Log.d("DateParseException", "onCreate: Dtae parse exception");
        }
        String lastEditedDateandTime = sdf.format(d);
        lastEditedText.setText("Edited "+lastEditedDateandTime.toString());
        titleEditText.setText(note.getTitle());
        contentEditText.setText(note.getTitle());

    }
}
