package dg.gautam.deepak.notes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dg.gautam.deepak.notes.notes.Note;
import dg.gautam.deepak.notes.notes.NotesDatabaseHelper;
import dg.gautam.deepak.notes.notes.NotesGridLayoutItemDecorator;
import dg.gautam.deepak.notes.notes.NotesRecyclerViewAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NotesRecyclerViewAdapter.ItemClickListener {
    private NotesDatabaseHelper db;
    private List<Note> notesList = new ArrayList<>();
    private  NotesRecyclerViewAdapter madapter;
    RecyclerView recyclerView;
    SharedPreferences sharedpreferences;
    private int numberOfColumns;
    TextView noNotesView;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String columnCount = "numberOfColumns";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        noNotesView = (TextView)findViewById(R.id.empty_notes_view);
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        numberOfColumns = sharedpreferences.getInt(columnCount,1);
        //get notes from DB
        db = new NotesDatabaseHelper(this);
        //db.onUpgrade(db, 1,2);
        notesList.addAll(db.getAllNotes());
        toggleEmptyNotes();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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

//        // set up the RecyclerView
//        String[] data = {"1", "2", "3", "4", "5", "6"};
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvNumbers);
//        //recyclerView.addItemDecoration(new MarginDecoration(this));
//        recyclerView.setHasFixedSize(true);

        Log.d("columnCount", "onOptionsItemSelected: "+sharedpreferences.getInt(columnCount,1));
//        //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
//        //recyclerView.setLayoutManager(mLayoutManager);
//        adapter = new RecyclerViewAdapter(this, data);
//        adapter.setClickListener(this);
//        recyclerView.setAdapter(adapter);


        //set recyclerview
        madapter = new NotesRecyclerViewAdapter(this, notesList);
        recyclerView = findViewById(R.id.rvNumbers);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setHasFixedSize(true);
        madapter.setClickListener(this);
        recyclerView.setAdapter(madapter);

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
        sharedpreferences = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sharedpreferences.getInt(columnCount,1);
        int numberOfColumns = sharedpreferences.getInt(columnCount,1);
//        if(numberOfColumns==2){
//            menu.findItem(R.id.action_single_column).setVisible(true);
//            menu.findItem(R.id.action_two_column).setVisible(false);
//        }
//        else{
//            menu.findItem(R.id.action_single_column).setVisible(false);
//            menu.findItem(R.id.action_two_column).setVisible(true);
//        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.action_two_column){
//            sharedpreferences = this.getSharedPreferences("ColumnCount", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedpreferences.edit();
//            int numberOfColumns = sharedpreferences.getInt(columnCount,1);
            if(numberOfColumns ==1) {
//                editor.putInt(columnCount, 2);
//                editor.apply();
                numberOfColumns =2;
                Log.d("columnCount", "onOptionsItemSelected: "+sharedpreferences.getInt(columnCount,1));
                //MainActivity.this.invalidateOptionsMenu();
            }
            else{
//                editor.putInt(columnCount, 1);
//                editor.apply();
                numberOfColumns =1;
                Log.d("columnCount", "onOptionsItemSelected: "+sharedpreferences.getInt(columnCount,1));
                //MainActivity.this.invalidateOptionsMenu();
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

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


        if (db.getNotesCount() > 0) {
            noNotesView.setVisibility(View.GONE);
        } else {
            noNotesView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        notesList.clear();
        notesList.addAll(db.getAllNotes());
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
}
