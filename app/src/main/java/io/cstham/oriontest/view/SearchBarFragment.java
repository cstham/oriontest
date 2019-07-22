package io.cstham.oriontest.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.androidnetworking.AndroidNetworking;

import io.cstham.oriontest.R;
import io.cstham.oriontest.utils.tabproperties.Utils;


public class SearchBarFragment extends Fragment implements View.OnClickListener, TextWatcher {

    private ImageView mIvBack, mClear;

    public static ImageView mRefresh;

    private EditText mEtSearchText;
    private int numberOfColumns;
    private Handler handler;
    private String query;

    private RecyclerView recyclerView;

    private boolean incrementSearch;

    //callback variables
    public static SearchBarResponse searchBarResponse;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_box, container, false);

        mIvBack = view.findViewById(R.id.iv_back);
        mRefresh = view.findViewById(R.id.refresh);
        mEtSearchText = view.findViewById(R.id.et_search_text);
        mClear = view.findViewById(R.id.clear);

        mIvBack.setOnClickListener(this);
        mRefresh.setOnClickListener(this);

        mClear.setOnClickListener(this);
        mClear.setVisibility(View.INVISIBLE);

        //mEtSearchText.setOnTouchListener(this);
        mEtSearchText.addTextChangedListener(this);

        incrementSearch = true;

        if (getContext() != null) {
            AndroidNetworking.initialize(getContext());
        }

        //spin refresh on inital onCreate
        refreshAnimation(mRefresh);

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
/*
        //to get findViewById from rootView
        if (getActivity()!=null) {
            avi = getActivity().findViewById(R.id.avi);
            recyclerView = getActivity().findViewById(R.id.recycler_view);
        }
        */
    }


    @Override
    public void onClick(View v) {
        final int id = v.getId();

        if (id == R.id.iv_back) {
            resetSearchBox();
            performBackAction();
        }
        else if(id == R.id.clear){
            resetSearchBox();
        }
        else if(id == R.id.refresh){
            refreshAnimation(mRefresh);
            searchBarResponse.onRefresh();
        }
    }
/*
    private void enableSearchField() {
        mIvBack.setImageResource(R.drawable.ic_arrow_back);
    }
*/

    private void refreshAnimation(ImageView mRefresh){
        /*
        AdditiveAnimator.animate(mRefresh).setDuration(4000)
                .withLayer()
                .rotationBy(270)
                .start();
                */
    }

    private void resetSearchBox() {
        mEtSearchText.setText("");
    }

    protected void performBackAction() {
        mIvBack.setImageResource(R.drawable.ic_search);
        mEtSearchText.clearFocus();
        if (getActivity()!=null){
            Utils.hideKeyboardFromDialog(getActivity(), mEtSearchText);
        }
    }

    /*
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP){
            //startActivity( new Intent(getActivity(), CommonTabActivity.class));
            enableSearchField();
            client.sendMessage("Hello World");
        }
        return false;
    }
    */

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        /*

        query = s.toString().toLowerCase().trim();

        if ((query.length() == 1) && (incrementSearch)){
            new FadeInAnimation(mClear).animate();
            //mClear.setVisibility(View.VISIBLE);
        }

        //avi.smoothToShow();
        Runnable run = new Runnable() {
            @Override
            public void run() {
                avi.smoothToHide();
                searchStuff(query);
            }
        };
        // only canceling the network calls will not help, you need to remove all callbacks as well
        // otherwise the pending callbacks and messages will again invoke the handler and will send the request
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        } else {
            handler = new Handler();
        }
        handler.postDelayed(run, 500);     //700ms
*/
    }

    private void searchStuff(String query){
        /*
        if (gaggeredList!=null) {
            incrementSearch = false;
            final List<FrameObjects> filteredList = new ArrayList<>();

            for (FrameObjects item : gaggeredList) {
                //To filter by stream name
                if (item.getStreamName().toLowerCase().contains(query)) {
                    filteredList.add(item);
                }
            }

            //if searchbar is empty, show default recyclerview adapter
            if (query.matches("")) {
                //new FadeOutAnimation(mClear).setDuration(50).animate();
                incrementSearch = true;
                mClear.setVisibility(View.INVISIBLE);
                numberOfColumns = 2;
            } else {
                numberOfColumns = 3;
            }

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
            SolventRecyclerViewAdapter rcAdapter = new SolventRecyclerViewAdapter(getActivity(), filteredList);
            recyclerView.setAdapter(rcAdapter);

            rcAdapter.notifyDataSetChanged();
        }
        */
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}
