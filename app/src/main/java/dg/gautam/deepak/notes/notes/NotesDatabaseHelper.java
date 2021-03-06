package dg.gautam.deepak.notes.notes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sony on 11-06-2018.
 */

public class NotesDatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "notes_db";


    public NotesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(Note.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Note.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertNote(Note note) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        Log.d("title_debug_notesdb", "createNote: "+note.getTitle());
        values.put(Note.COLUMN_TITLE,note.getTitle());
        values.put(Note.COLUMN_CONTENT,note.getContent() );
        values.put(Note.COLUMN_BACKGROUND,note.getBackground());
        values.put(Note.COLUMN_ISFAVOURITE,note.getIsFavourite());
        values.put(Note.COLUMN_ISINTRASH,note.getIsInTrash());
        values.put(Note.COLUMN_ISDELETED,note.getIsDeleted());
        values.put(Note.COLUMN_LASTEDITED,note.getLastEdited());
        Log.d("timestamp....", "createNote: "+note.getTimestamp());
//        if(note.getTimestamp()!=null){  //code to handle undo delete case
//            values.put(Note.COLUMN_TIMESTAMP,note.getTimestamp());
//        }

        // insert row
        long id = db.insert(Note.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public Note getNote(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Note.TABLE_NAME,
                new String[]{Note.COLUMN_ID, Note.COLUMN_TITLE, Note.COLUMN_CONTENT, Note.COLUMN_TIMESTAMP,
                        Note.COLUMN_BACKGROUND, Note.COLUMN_ISFAVOURITE,Note.COLUMN_ISINTRASH,Note.COLUMN_ISDELETED,
                        Note.COLUMN_LASTEDITED},
                Note.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        Note note = new Note(
                cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_CONTENT)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_TITLE)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIMESTAMP)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_BACKGROUND)),
                cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ISFAVOURITE)),
                cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ISINTRASH)),
                cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ISDELETED)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_LASTEDITED))
        );

        // close the db connection
        cursor.close();

        return note;
    }

    public List<Note> getAllNotes(String filter) {
        List<Note> notes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Note.TABLE_NAME + " ORDER BY " +
                Note.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndex(Note.COLUMN_TITLE)));
                note.setContent(cursor.getString(cursor.getColumnIndex(Note.COLUMN_CONTENT)));
                note.setTimestamp(cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIMESTAMP)));
                Log.d("SetBackground", "getAllNotes: "+cursor.getString(cursor.getColumnIndex(Note.COLUMN_BACKGROUND)));
                note.setBackground(cursor.getString(cursor.getColumnIndex(Note.COLUMN_BACKGROUND)));
                note.setIsFavourite(cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ISFAVOURITE)));
                note.setIsInTrash(cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ISINTRASH)));
                note.setIsDeleted(cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ISDELETED)));
                note.setLastEdited(cursor.getString(cursor.getColumnIndex(Note.COLUMN_LASTEDITED)));
                if(filter=="fav"){
                    if(note.getIsInTrash()==0 && note.getIsFavourite()==1)notes.add(note);
                }
                else if(filter=="trash"){
                    if(note.getIsInTrash()==1)notes.add(note);
                }
                else if(note.getIsInTrash()==0)notes.add(note);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return notes;
    }

    public int getNotesCount() {
        String countQuery = "SELECT  * FROM " + Note.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public int updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Note.COLUMN_TITLE, note.getTitle());
        values.put(Note.COLUMN_CONTENT, note.getContent());
        values.put(Note.COLUMN_BACKGROUND, note.getBackground());
        values.put(Note.COLUMN_ISFAVOURITE, note.getIsFavourite());
        values.put(Note.COLUMN_ISINTRASH, note.getIsInTrash());
        values.put(Note.COLUMN_ISDELETED, note.getIsDeleted());
        values.put(Note.COLUMN_LASTEDITED, note.getLastEdited());

        // updating row
        return db.update(Note.TABLE_NAME, values, Note.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
    }

    public void deleteNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Note.TABLE_NAME, Note.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
    }

    public void sendNoteToTrash(Note note){
        note.setIsInTrash(1);
    }

    public void toggleFavourite(Note note){
        if(note.getIsFavourite() ==0){
            note.setIsFavourite(1);
        }
        else note.setIsFavourite(0);
    }
}

