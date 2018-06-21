//package dg.gautam.deepak.notes.notes;
//
//import android.graphics.Canvas;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//
//import  android.support.v7.widget.helper.ItemTouchHelper;
//
///**
// * Created by sony on 20-06-2018.
// */
//
//public class NotesRecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
//    private RecyclerItemTouchHelperListener listener;
//
//    public NotesRecyclerItemTouchHelper(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener) {
//        super(dragDirs, swipeDirs);
//        this.listener = listener;
//    }
//
//    @Override
//    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//        return true;
//    }
//
//    @Override
//    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
//        if (viewHolder != null) {
//            final View foregroundView = ((NotesRecyclerViewAdapter.MyViewHolder) viewHolder).viewForeground;
//
//            getDefaultUIUtil().onSelected(foregroundView);
//        }
//    }
//
//    @Override
//    public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
//                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
//                                int actionState, boolean isCurrentlyActive) {
//        final View foregroundView = ((NotesRecyclerViewAdapter.MyViewHolder) viewHolder).viewForeground;
//        getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
//                actionState, isCurrentlyActive);
//    }
//
//    @Override
//    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
//        final View foregroundView = ((NotesRecyclerViewAdapter.MyViewHolder) viewHolder).viewForeground;
//        getDefaultUIUtil().clearView(foregroundView);
//    }
//
//    @Override
//    public void onChildDraw(Canvas c, RecyclerView recyclerView,
//                            RecyclerView.ViewHolder viewHolder, float dX, float dY,
//                            int actionState, boolean isCurrentlyActive) {
//        final View foregroundView = ((NotesRecyclerViewAdapter.MyViewHolder) viewHolder).viewForeground;
//
//        getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
//                actionState, isCurrentlyActive);
//    }
//
//    @Override
//    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//        listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
//    }
//
//    @Override
//    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
//        return super.convertToAbsoluteDirection(flags, layoutDirection);
//    }
//
//    public interface RecyclerItemTouchHelperListener {
//        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
//    }
//}
