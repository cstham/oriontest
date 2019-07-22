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
import io.cstham.oriontest.router.DetailsRouter;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    private Context context;
    private List<Record> newsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout rowLayout, viewBackground;
        public TextView titleTxtView;

        public MyViewHolder(View view) {
            super(view);

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

        holder.titleTxtView.setText(record.getTitle());

        holder.rowLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DetailsRouter.startActivity(v.getContext(),
                        newsList.get(position).getTitle(),
                        newsList.get(position).getContent(),
                        newsList.get(position).getImage(),
                        newsList.get(position).getAuthor(),
                        newsList.get(position).getDescription(),
                        newsList.get(position).getUrl()
                        );
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public void removeItem(int position) {
        newsList.remove(position);
        notifyDataSetChanged();
        notifyItemRemoved(position);
    }

    public void restoreItem(Record record, int position) {
        newsList.add(position, record);
        notifyDataSetChanged();
        notifyItemInserted(position);
    }

}
