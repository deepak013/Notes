package dg.gautam.deepak.notes.notes;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import dg.gautam.deepak.notes.R;

import static dg.gautam.deepak.notes.MainActivity.columnCount;

/**
 * Created by sony on 11-06-2018.
 */

public class NotesRecyclerViewAdapter extends RecyclerView.Adapter<NotesRecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private List<Note> notesList;
    private NotesRecyclerViewAdapter.ItemClickListener mClickListener;
    SharedPreferences sharedpreferences;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;
        public TextView note;
        //public TextView timestamp;
        public CardView cardView;
        public RelativeLayout viewForeground;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.noteTitle);
            note = view.findViewById(R.id.notes_dashboard_text);
            //timestamp = view.findViewById(R.id.timestamp);
            viewForeground = view.findViewById(R.id.cardRelativeLayout);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }


    public NotesRecyclerViewAdapter(Context context, List<Note> notesList) {
        this.context = context;
        this.notesList = notesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item_notes, parent, false);

        return new MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Note note = notesList.get(position);

       // holder.title.setText(note.getTitle());
        holder.title.setText(note.getTitle());

        holder.note.setText(note.getContent());
//        public static int parseColor (String note.getBackground());
        Log.d("BackgroundColor", "onBindViewHolder: "+note.getBackground());
        holder.viewForeground.setBackgroundColor(Color.parseColor(note.getBackground()));

        // Displaying dot from HTML character code
       // holder.dot.setText(Html.fromHtml("&#8226;"));

        // Formatting and displaying timestamp
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int numberOfColumns = sharedpreferences.getInt(columnCount,1);
        //holder.timestamp.setText(formatDate(note.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    /**
     * Formatting timestamp to `MMM d` format
     * Input: 2018-02-21 00:15:42
     * Output: Feb 21
     */
    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d");
            return fmtOut.format(date);
        } catch (ParseException e) {

        }

        return "";
    }

    // allows clicks events to be caught
    public void setClickListener(NotesRecyclerViewAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void removeItem(int position) {
        notesList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Note item, int position) {
        notesList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }
}
