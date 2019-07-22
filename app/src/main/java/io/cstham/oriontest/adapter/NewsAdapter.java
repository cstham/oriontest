package io.cstham.oriontest.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import io.cstham.oriontest.presenter.DetailsActivity;
import io.cstham.oriontest.R;
import io.cstham.oriontest.entity.Record;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    private Context context;
    private List<Record> newsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //public TextView note;
        //public TextView dot;
        //public TextView timestamp;

        public RelativeLayout rowLayout, viewBackground;
        public TextView titleTxtView;

        public MyViewHolder(View view) {
            super(view);
            //note = view.findViewById(R.id.note);
            //dot = view.findViewById(R.id.dot);
            //timestamp = view.findViewById(R.id.timestamp);
            rowLayout= view.findViewById(R.id.row1);
            viewBackground = view.findViewById(R.id.view_background);
            titleTxtView = view.findViewById(R.id.textView1);


        }
    }


    public NewsAdapter(Context context, List<Record> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_main_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Record record = newsList.get(position);


        //System.out.println("lll lolzz: "+record.getTitle());
        holder.titleTxtView.setText(record.getTitle());
        //holder.note.setText(note.getNote());

        // Displaying dot from HTML character code
        //holder.dot.setText(Html.fromHtml("&#8226;"));

        // Formatting and displaying timestamp
        //holder.timestamp.setText(formatDate(note.getTimestamp()));

        holder.rowLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //selectedNews = itemListFiltered.get(position).getName();
                //System.out.println("selectedNews ppp: "+selectedNews);
                Intent detailsIntent = new Intent(v.getContext(), DetailsActivity.class);
                detailsIntent.putExtra("title", newsList.get(position).getTitle());
                detailsIntent.putExtra("content", newsList.get(position).getContent());
                detailsIntent.putExtra("urlImage", newsList.get(position).getImage());

                detailsIntent.putExtra("author", newsList.get(position).getAuthor());
                detailsIntent.putExtra("description", newsList.get(position).getDescription());
                detailsIntent.putExtra("url", newsList.get(position).getUrl());

                v.getContext().startActivity(detailsIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public void removeItem(int position) {
        newsList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyDataSetChanged();
        notifyItemRemoved(position);
    }

    public void restoreItem(Record record, int position) {
        newsList.add(position, record);
        // notify item added by position
        notifyDataSetChanged();
        notifyItemInserted(position);
    }

}
