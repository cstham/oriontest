package io.cstham.oriontest.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import io.cstham.oriontest.DetailsHistoryActivity;
import io.cstham.oriontest.R;
import io.cstham.oriontest.entity.History;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    private Context context;
    private List<History> historyList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout rowLayout, viewBackground;
        public TextView titleTxtView;
        public TextView dot;

        public MyViewHolder(View view) {
            super(view);

            rowLayout= view.findViewById(R.id.row1);
            viewBackground = view.findViewById(R.id.view_background);
            titleTxtView = view.findViewById(R.id.textView1);
            dot = view.findViewById(R.id.dot);

        }
    }


    public HistoryAdapter(Context context, List<History> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_history_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        History history = historyList.get(position);
        
        //System.out.println("lll lolzz: "+record.getTitle());
        holder.titleTxtView.setText(history.getTitle());
        //holder.note.setText(note.getNote());

        // Displaying dot from HTML character code
        holder.dot.setText(Html.fromHtml("&#8226;"));

        // Formatting and displaying timestamp
        //holder.timestamp.setText(formatDate(note.getTimestamp()));

        holder.rowLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //selectedNews = itemListFiltered.get(position).getName();
                //System.out.println("selectedNews ppp: "+selectedNews);
                Intent detailsHistoryIntent = new Intent(v.getContext(), DetailsHistoryActivity.class);
                detailsHistoryIntent.putExtra("title", historyList.get(position).getTitle());
                detailsHistoryIntent.putExtra("content", historyList.get(position).getContent());
                detailsHistoryIntent.putExtra("urlImage", historyList.get(position).getImage());

                detailsHistoryIntent.putExtra("author", historyList.get(position).getAuthor());
                detailsHistoryIntent.putExtra("description", historyList.get(position).getDescription());
                detailsHistoryIntent.putExtra("url", historyList.get(position).getUrl());

                v.getContext().startActivity(detailsHistoryIntent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public void removeItem(int position) {
        historyList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyDataSetChanged();
        notifyItemRemoved(position);
    }

    public void restoreItem(History history, int position) {
        historyList.add(position, history);
        // notify item added by position
        notifyDataSetChanged();
        notifyItemInserted(position);
    }

}
