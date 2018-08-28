package dg.gautam.deepak.notes;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import dg.gautam.deepak.notes.notes.Note;
import dg.gautam.deepak.notes.notes.NotesDatabaseHelper;
import dg.gautam.deepak.notes.notes.NotesRecyclerTouchListener;
import dg.gautam.deepak.notes.notes.NotesRecyclerViewAdapter;
import dg.gautam.deepak.notes.notes.RecyclerItemTouchHelper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NotesRecyclerViewAdapter.ItemClickListener,
        RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{
    private NotesDatabaseHelper db;
    private List<Note> notesList = new ArrayList<>();
    private  NotesRecyclerViewAdapter madapter, madapter1;
    RecyclerView recyclerView;
    SharedPreferences sharedpreferences;
    DrawerLayout drawerLayout;
    private int numberOfColumns;
    TextView noNotesView;
    TextView timeStamp;
    Boolean isMoving=false;
    BottomSheetDialog bottomSheetDialog;
    ImageView bottomSheetFav;
    TextView addOrRemoveFavourite;
    private  int longClickposition=5;
    Toolbar toolbar;
    Boolean favViewVisiable = false;
    String currentView = "allNotes";
    FloatingActionButton fab;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String columnCount = "numberOfColumns";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        noNotesView = (TextView)findViewById(R.id.empty_notes_view);
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        numberOfColumns = sharedpreferences.getInt(columnCount,1);
        db = new NotesDatabaseHelper(this);
        //db.onUpgrade(db, 1,2);
        notesList.addAll(db.getAllNotes("all"));
        toggleEmptyNotes();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AddNotes.class);
                startActivity(intent);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Log.d("columnCount", "onOptionsItemSelected: "+sharedpreferences.getInt(columnCount,1));


        //set recyclerview
        madapter = new NotesRecyclerViewAdapter(this, notesList);
        recyclerView = findViewById(R.id.rvNumbers);
        recyclerView.setHasFixedSize(true);
        madapter.setClickListener(this);
        recyclerView.setAdapter(madapter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL));

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        final ItemTouchHelper itemTouchHelper= new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.addOnItemTouchListener(new NotesRecyclerTouchListener(getApplicationContext(), recyclerView, new NotesRecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Note note = notesList.get(position);
                Intent intent = new Intent(getApplicationContext(), EditNotesActivity.class);
                intent.putExtra("note", note);
                startActivity(intent);
                //Toast.makeText(getApplicationContext(), note.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                if(!isMoving) {
                    final Note note = notesList.get(position);
                    View sheetView;
                    //Toast.makeText(getApplicationContext(), note.getTitle() + " is log pressed!", Toast.LENGTH_SHORT).show();
                    if(currentView=="trash"){
                        sheetView = MainActivity.this.getLayoutInflater().inflate(R.layout.view_bottom_sheet_trash, null);
                    }
                    else {
                        sheetView = MainActivity.this.getLayoutInflater().inflate(R.layout.view_bottom_sheet_dialog, null);
                    }
                    bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
                    bottomSheetDialog.setContentView(sheetView);
                    bottomSheetDialog.show();
                    addOrRemoveFavourite = bottomSheetDialog.findViewById(R.id.addOrRemoveFavourite);
                    if(note.getIsFavourite()==1 && currentView!="trash"){
                        addOrRemoveFavourite.setText("Remove From Favourite");
                    }
                    longClickposition=position;
                }
                isMoving=false;
            }
        }));


    }

    @Override
    public void onBackPressed() {
        shaveColumnCount();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_two_column);
        if(numberOfColumns==2){
            item.setIcon(getResources().getDrawable(R.drawable.ic_view_stream_white_24dp));
        }
        else{
            item.setIcon(getResources().getDrawable(R.drawable.ic_view_quilt_white_24dp));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent i =new Intent(this, AboutActivity.class);
            startActivity(i);
        }
        if(id == R.id.action_two_column){
            if(numberOfColumns ==1) {
                numberOfColumns =2;
                item.setIcon(getResources().getDrawable(R.drawable.ic_view_stream_white_24dp));
                Log.d("columnCount", "onOptionsItemSelected: "+sharedpreferences.getInt(columnCount,1));
            }
            else{
                numberOfColumns =1;
                item.setIcon(getResources().getDrawable(R.drawable.ic_view_quilt_white_24dp));
                Log.d("columnCount", "onOptionsItemSelected: "+sharedpreferences.getInt(columnCount,1));
            }
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.all_notes) {
            if(currentView!="allNotes"){
                fab.setVisibility(View.VISIBLE);
                currentView="allNotes";
                toolbar.setTitle("Notes");
                notesList.clear();
                notesList.addAll(db.getAllNotes("all"));
                madapter = new NotesRecyclerViewAdapter(this, notesList);
                madapter.notifyDataSetChanged();
                recyclerView.setAdapter(madapter);
                recyclerView.smoothScrollToPosition(0);
                toggleEmptyNotes();
            }
        }
        if (id == R.id.nav_favourite) {
            if(currentView!="fav") {
                fab.setVisibility(View.VISIBLE);
                currentView="fav";
                toolbar.setTitle("Favourite");
                notesList.clear();
                notesList.addAll(db.getAllNotes("fav"));
                madapter = new NotesRecyclerViewAdapter(this, notesList);
                madapter.notifyDataSetChanged();
                recyclerView.setAdapter(madapter);
                recyclerView.smoothScrollToPosition(0);
                toggleEmptyNotes();
            }
        } else if (id == R.id.nav_trash) {
            if(currentView!="trash"){
                fab.setVisibility(View.INVISIBLE);
                currentView="trash";
                toolbar.setTitle("Trash");
                notesList.clear();
                notesList.addAll(db.getAllNotes("trash"));
                madapter = new NotesRecyclerViewAdapter(this, notesList);
                madapter.notifyDataSetChanged();
                recyclerView.setAdapter(madapter);
                recyclerView.smoothScrollToPosition(0);
                toggleEmptyNotes();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClick(View view, int position) {
        //Log.i("TAG", "You clicked number " + madapter.getItem(position) + ", which is at cell position " + position);
    }

    private void toggleEmptyNotes() {
        // you can check notesList.size() > 0

        if (notesList.size() > 0) {
            noNotesView.setVisibility(View.GONE);
        } else {
            noNotesView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        notesList.clear();
        notesList.addAll(db.getAllNotes(currentView));
        madapter.notifyDataSetChanged();
        noNotesView = (TextView)findViewById(R.id.empty_notes_view);
        toggleEmptyNotes();
    }

    public void onStop(){
        shaveColumnCount();
        super.onStop();
    }

    public void shaveColumnCount(){
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(columnCount, numberOfColumns);
        editor.apply();
        return;
    }

    /**
     * callback when recycler view is swiped
     * item will be removed on swiped
     * undo option will be provided in snackbar to restore the item
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof NotesRecyclerViewAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = notesList.get(viewHolder.getAdapterPosition()).getTitle();

            // backup of removed item for undo purpose
            final Note deletedItem = notesList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            madapter.removeItem(viewHolder.getAdapterPosition());
            toggleEmptyNotes();


            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(drawerLayout, name + " removed from list!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    madapter.restoreItem(deletedItem, deletedIndex);
                    toggleEmptyNotes();
                    if(deletedIndex == 0) {
                        recyclerView.smoothScrollToPosition(0);
                    }
                    isMoving=false;
                }
            }).addCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar snackbar, int dismissType) {
                    super.onDismissed(snackbar, dismissType);

                    if(dismissType == DISMISS_EVENT_TIMEOUT || dismissType == DISMISS_EVENT_SWIPE
                            || dismissType == DISMISS_EVENT_CONSECUTIVE || dismissType == DISMISS_EVENT_MANUAL)
                        if(deletedItem.getIsInTrash()==1) {
                            db.deleteNote(deletedItem);  //delete from database if undo is not pressed
                            Toast.makeText(getApplicationContext(), "Item deleted!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                         deletedItem.setIsInTrash(1);
                         db.updateNote(deletedItem);
                         madapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(), "Item sent to trash!", Toast.LENGTH_SHORT).show();
                        }
                    toggleEmptyNotes();
                    isMoving=false;
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    public void onChildDraw(int actionState, float dX, boolean isCurrentlyActive){
        Log.d("ActionState", "onChildDraw: "+isCurrentlyActive+" "+dX);
        isMoving =true;
        if(isCurrentlyActive==false && dX==0) resetIsMoving();
    }

    public  void resetIsMoving(){
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        isMoving=false;
                    }
                },
                500
        );
    }

    public  void favButtonClicked(View view){
        Note note = notesList.get(longClickposition);
        RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(longClickposition);
        ImageView favImage;
            favImage = (ImageView)holder.itemView.findViewById(R.id.favImage);
        if(note.getIsFavourite()==0) {
            note.setIsFavourite(1);
            bottomSheetDialog.dismiss();
            favImage.setImageResource(R.drawable.ic_star_white_18dp);
            favImage.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(), "Item is Added To favourite!", Toast.LENGTH_SHORT).show();
        }
        else {
            note.setIsFavourite(0);
            bottomSheetDialog.dismiss();
            favImage.setVisibility(View.INVISIBLE);
            if(currentView=="fav"){
                madapter.removeItem(longClickposition);
                madapter.notifyDataSetChanged();
            }
            Toast.makeText(getApplicationContext(),  "Item is removed from favourite!", Toast.LENGTH_SHORT).show();
            toggleEmptyNotes();
        }
        db.updateNote(note);
    }

    public  void deleteNote(View view){
        Note deletedItem = notesList.get(longClickposition);
        if(deletedItem.getIsInTrash()==1) {
            db.deleteNote(deletedItem);  //delete from database if undo is not pressed
            Toast.makeText(getApplicationContext(), "Item deleted!", Toast.LENGTH_SHORT).show();
        }
        else {
            deletedItem.setIsInTrash(1);
            db.updateNote(deletedItem);
            Toast.makeText(getApplicationContext(), "Item sent to trash!", Toast.LENGTH_SHORT).show();
        }
        toggleEmptyNotes();
        bottomSheetDialog.dismiss();
        madapter.removeItem(longClickposition);
        madapter.notifyDataSetChanged();
    }

    public void restoreClicked(View view){
        Note note = notesList.get(longClickposition);
        note.setIsInTrash(0);
        db.updateNote(note);
        madapter.removeItem(longClickposition);
        madapter.notifyDataSetChanged();
        bottomSheetDialog.dismiss();
        toggleEmptyNotes();
    }

}
