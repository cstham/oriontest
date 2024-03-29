package io.cstham.oriontest.view;


import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import io.cstham.oriontest.adapter.HistoryAdapter;
import io.cstham.oriontest.R;
import io.cstham.oriontest.utils.MyDividerItemDecoration;
import io.cstham.oriontest.utils.HistoryRecyclerItemTouchHelper;
import io.cstham.oriontest.database.HistoryDatabaseHelper;
import io.cstham.oriontest.entity.History;
import static io.cstham.oriontest.presenter.DetailsHistoryActivity.isDetailsHistoryActivity;

@SuppressLint("ValidFragment")
public class HistoryFragment extends Fragment implements HistoryRecyclerItemTouchHelper.HistoryRecyclerItemTouchHelperListener {

    private CoordinatorLayout coordinatorLayout;

    HistoryAdapter historyAdapter;

    private RecyclerView recyclerView;
    ItemTouchHelper.SimpleCallback itemTouchHelperCallback;


    private HistoryDatabaseHelper db;
    public List<History> historyList = new ArrayList<>();

    private SharedPreferences prefs;
    boolean isStopped = false;

    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_history, container, false);

        db = new HistoryDatabaseHelper(getContext());


        coordinatorLayout = v.findViewById(R.id.coordinatorLayout);

        progressBar = v.findViewById(R.id.progressBar_cyclic);

        recyclerView = v.findViewById(R.id.history_main_list);

        itemTouchHelperCallback = new HistoryRecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);

        historyList.addAll(db.getAllHistories());


        historyAdapter = new HistoryAdapter(getContext(), historyList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(historyAdapter);
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL, 16));
        historyAdapter.notifyDataSetChanged();
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        return v;
    }

    @Override
    public void onResume(){
        super.onResume();



        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        long history_id = prefs.getLong("history_id",0);

        try{
            if ((isStopped) && (!isDetailsHistoryActivity)){
                historyList.add(0, db.getHistory(history_id));
            }
        }
        catch (SQLiteException e){

        }

        historyAdapter.notifyDataSetChanged();
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

    }





    @Override
    public void onStop() {
        super.onStop();
        isStopped = true;
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof HistoryAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            //String name = newsList.get(viewHolder.getAdapterPosition()).getTitle();

            // backup of removed item for undo purpose
            final History deletedItem = historyList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            historyAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Item No. " +position + " is removed from list!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    historyAdapter.restoreItem(deletedItem, deletedIndex);
                    Toast.makeText(getActivity(), "Item is restored!", Toast.LENGTH_SHORT).show();

                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();

        }
    }

}



