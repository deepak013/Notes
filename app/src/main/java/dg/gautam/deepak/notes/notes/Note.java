package dg.gautam.deepak.notes.notes;

/**
 * Created by sony on 11-06-2018.
 */

public class Note {
    public static final String TABLE_NAME = "notes";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_BACKGROUND = "background";

    private int id;
    private String content;
    private String title;
    private String timestamp;
    private  String background;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_TITLE + " TEXT,"
                    + COLUMN_CONTENT + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
                    + COLUMN_BACKGROUND + " TEXT"
                    + ")";

    public Note() {
    }

    public Note(int id, String title, String note, String timestamp, String background) {
        this.id = id;
        this.title= title;
        this.content = note;
        this.timestamp = timestamp;
        this.background = background;
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
}
