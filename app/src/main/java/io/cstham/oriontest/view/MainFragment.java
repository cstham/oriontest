package io.cstham.oriontest.view;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import io.cstham.oriontest.adapter.NewsAdapter;
import io.cstham.oriontest.utils.MyDividerItemDecoration;
import io.cstham.oriontest.utils.NetworkConnectionCheck;
import io.cstham.oriontest.R;
import io.cstham.oriontest.utils.TextBox;
import io.cstham.oriontest.utils.RecyclerItemTouchHelper;
import io.cstham.oriontest.database.DatabaseHelper;
import io.cstham.oriontest.entity.Record;

import static io.cstham.oriontest.api.ApiConfig.NEWS_API_BASE;
import static io.cstham.oriontest.api.ApiConfig.NEWS_API_CATEGORY;
import static io.cstham.oriontest.api.ApiConfig.NEWS_API_COUNTRY;
import static io.cstham.oriontest.api.ApiConfig.NEWS_API_KEY;

@SuppressLint("ValidFragment")
public class MainFragment extends Fragment implements SearchBarResponse, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private CoordinatorLayout coordinatorLayout;
    private List<Record> newsList = new ArrayList<>();
    TextBox textBox = new TextBox();
    Thread internetThread;
    Handler internetCheckHandler = new Handler();
    NewsAdapter mainAdapter;
    SwipeRefreshLayout pullToRefresh;
    private int pageStart = 1;
    private ProgressBar progressBar;
    boolean isPulltoRefresh = false;
    boolean isOnline;
    private RecyclerView recyclerView;
    ItemTouchHelper.SimpleCallback itemTouchHelperCallback;
    private DatabaseHelper db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidNetworking.initialize(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main, container, false);

        db = new DatabaseHelper(getContext());

        coordinatorLayout = v.findViewById(R.id.coordinatorLayout);
        progressBar = v.findViewById(R.id.progressBar_cyclic);
        pullToRefresh = v.findViewById(R.id.swiperefresh);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefresh.post(new Runnable() {
                                             @Override
                                             public void run() {
                                                 isPulltoRefresh = true;
                                                 pageStart = 1;
                                                 newsList = new ArrayList<>();
                                                 db.deleteAllRecords();
                                                 getNews("1");
                                                 pullToRefresh.setRefreshing(true);
                                             }
                                         }
                );
            }
        });


        recyclerView = v.findViewById(R.id.news_main_list);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    progressBar.setVisibility(View.VISIBLE);
                    pageStart ++;
                    getNews(String.valueOf(pageStart));
                    //Toast.makeText(getActivity(), String.valueOf(pageStart), Toast.LENGTH_SHORT).show();
                }
            }
        });

        itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        //==========================================================================================
        if(NetworkConnectionCheck.isNetworkConnected(getContext())){
            isOnline = true;
            getNews("1");
        }
        else {
            isOnline = false;
            getOfflineNews();
            textBox.addTextBox_coordLayout(inflater, R.layout.custom_message_boxes, coordinatorLayout,
                    "Error", "No Internet Connection.");
            delayReconnect();
        }

        //==========================================================================================
        SearchBarFragment.searchBarResponse = this;
        return v;
    }



    @Override
    public void onRefresh(){
        isPulltoRefresh = true;
        pageStart = 1;
        getNews("1");
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof NewsAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = newsList.get(viewHolder.getAdapterPosition()).getTitle();

            // backup of removed item for undo purpose
            final Record deletedItem = newsList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mainAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Item No. " +position + " is removed from list!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    mainAdapter.restoreItem(deletedItem, deletedIndex);
                    Toast.makeText(getActivity(), "Item is restored!", Toast.LENGTH_SHORT).show();

                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();

        }
    }

    public void getOfflineNews(){
        progressBar.setVisibility(View.INVISIBLE);
        newsList.addAll(db.getAllRecords());
        setupAdapter();
    }



    public void getNews(String pageNo) {


        if (isPulltoRefresh){
            progressBar.setVisibility(View.INVISIBLE);
        }

        AndroidNetworking.get(NEWS_API_BASE)
                .addQueryParameter("country", NEWS_API_COUNTRY)
                .addQueryParameter("category", NEWS_API_CATEGORY)
                .addQueryParameter("apiKey", NEWS_API_KEY)
                .addQueryParameter("page", pageNo)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try{
                            JSONArray articlesArray = new JSONArray(response.getString("articles"));

                            for (int i = 0; i < articlesArray.length(); i++){

                                String title =  articlesArray.getJSONObject(i).get("title").toString();
                                String author = articlesArray.getJSONObject(i).get("author").toString();
                                String description = articlesArray.getJSONObject(i).get("description").toString();
                                String url = articlesArray.getJSONObject(i).get("url").toString();
                                String urlToImage = articlesArray.getJSONObject(i).get("urlToImage").toString();
                                String content = articlesArray.getJSONObject(i).get("content").toString();


                                //insert into DB
                                long id = db.insertRecord(title, author, description, url, urlToImage, content);

                                Record record = db.getRecord(id);

                                if (record != null) {
                                    newsList.add(record);
                                }

                            }


                            for (int i=0; i<newsList.size(); i++){
                                Record row = newsList.get(i);
                                System.out.println(i+"): ooo    " +row.getTitle());
                            }

                            setupAdapter();



                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }

                        progressBar.setVisibility(View.INVISIBLE);
                        pullToRefresh.setRefreshing(false);
                        isPulltoRefresh = false;
                    }
                    @Override
                    public void onError(ANError error) {
                        progressBar.setVisibility(View.INVISIBLE);
                        pullToRefresh.setRefreshing(false);
                    }
                });

    }

private void setupAdapter(){
    mainAdapter = new NewsAdapter(getContext(), newsList);
    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
    recyclerView.setLayoutManager(mLayoutManager);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setAdapter(mainAdapter);
    recyclerView.addItemDecoration(new MyDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL, 16));

    mainAdapter.notifyDataSetChanged();
    new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
}


    private void getResults(){
        if (getActivity()!=null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (internetThread != null) {
                        internetThread.interrupt();
                        internetThread = null;
                        internetCheckHandler.removeCallbacksAndMessages(null);
                    }
                    textBox.removeTextBox_coordLayout(coordinatorLayout);

                    getNews("1");
                }
            });
        }
    }

    //==============================================================================================
    //Reconnection
    private void delayReconnect(){
        internetCheckHandler = new Handler(Looper.getMainLooper());
        internetCheckHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkInternet();
            }
        }, 5000);
    }


    private void checkInternet() {
      
        if(internetThread != null) {
            internetThread.interrupt();
            internetThread = null;
        }

        internetThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if(NetworkConnectionCheck.isNetworkConnected(getContext())){
                    getResults();
                }else{
                    delayReconnect();
                }

            }
        });
        internetThread.start();
    }

}


