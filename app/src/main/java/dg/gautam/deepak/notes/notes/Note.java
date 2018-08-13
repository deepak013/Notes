package dg.gautam.deepak.notes.notes;

import java.io.Serializable;

/**
 * Created by sony on 11-06-2018.
 */

public class Note implements Serializable{
    public static final String TABLE_NAME = "notes";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_BACKGROUND = "background";
    public static final String COLUMN_ISFAVOURITE = "isFavorite";
    public static final String COLUMN_ISINTRASH = "isInTrash";
    public static final String COLUMN_ISDELETED = "isDeleted";
    public static final String COLUMN_LASTEDITED = "lastEdited";

    private int id;
    private String content;
    private String title;
    private String timestamp;
    private  String background;
    private int isFavourite;
    private int isInTrash;
    private int isDeleted;
    private String lastEdited;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_TITLE + " TEXT,"
                    + COLUMN_CONTENT + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
                    + COLUMN_BACKGROUND + " TEXT, "
                    + COLUMN_ISFAVOURITE + " INTEGER, "
                    + COLUMN_ISINTRASH  + " INTEGER, "
                    + COLUMN_ISDELETED + " INTEGER, "
                    + COLUMN_LASTEDITED + " TEXT"
                    + ")";

    public Note() {
    }

    public Note(int id, String title, String note, String timestamp, String background, int favBool,int trashBool,int deleteBool, String lastEdit) {
        this.id = id;
        this.title= title;
        this.content = note;
        this.timestamp = timestamp;
        this.background = background;
        this.isFavourite = favBool;
        this.isDeleted =deleteBool;
        this.isInTrash =trashBool;
        this.lastEdited= lastEdit;
    }

    public int getId() {
        return id;
    }

    public String getTitle(){
        return  title;
    }

    public String getContent() {
        return content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getBackground() {
        return background;
    }

    public int getIsFavourite() {
        return isFavourite;
    }

    public int getIsInTrash() {
        return isInTrash;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public  String getLastEdited(){return  lastEdited;}

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public void setIsFavourite(int isFavourite) {
        this.isFavourite = isFavourite;
    }

    public void setIsInTrash(int isInTrash) {
        this.isInTrash = isInTrash;
    }

    public void setIsDeleted(int isDeleted) {this.isDeleted=isDeleted;}

    public void setLastEdited(String lastEdited) {
        this.lastEdited = lastEdited;
    }
}
