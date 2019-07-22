package io.cstham.oriontest.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import io.cstham.oriontest.DetailsActivity;
import io.cstham.oriontest.R;
import io.cstham.oriontest.entity.Record;

public class MainAdapter extends BaseAdapter implements View.OnClickListener {

    //ArrayList<String> listItem;
    //ArrayList<String> listAddress;

    Context mContext;

    List<Record> itemListFiltered;

    //private String selectedNews;



    //constructor
    public MainAdapter(Context mContext, List<Record> itemListFiltered) {
        this.mContext = mContext;
        //this.listItem = listItem;
        //this.listAddress = listAddress;
        this.itemListFiltered = itemListFiltered;
    }

    private static class ViewHolder{
        LinearLayout rowLayout;
        TextView addresstext;
        //TextView bodytext;
    }


    public int getCount() {
        return itemListFiltered.size();
    }

    public Object getItem(int arg0) {
        return itemListFiltered.get(arg0);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onClick(View view) {


        //selectedNews = itemListFiltered.get(getAdapterPosition()).getStreamName();

        //view.get
    }


    public View getView(final int position, View arg1, ViewGroup viewGroup) {

        ViewHolder holder;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View row = inflater.inflate(R.layout.listitem_row, viewGroup, false);

        if (arg1 == null) {
            arg1 = inflater.inflate(R.layout.adapter_main_row, null);
            holder = new ViewHolder();

            holder.addresstext = arg1.findViewById(R.id.textView1);

            holder.rowLayout = arg1.findViewById(R.id.row1);

            arg1.setTag(holder);
        } else {
            holder = (ViewHolder) arg1.getTag();
        }

        //arg1.setOnClickListener(this);
            //holder.addresstext.setText(listAddress.get(position));
        holder.addresstext.setText(itemListFiltered.get(position).getTitle());

        holder.rowLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //selectedNews = itemListFiltered.get(position).getName();
                //System.out.println("selectedNews ppp: "+selectedNews);
                Intent detailsIntent = new Intent(v.getContext(), DetailsActivity.class);
                detailsIntent.putExtra("title", itemListFiltered.get(position).getTitle());
                detailsIntent.putExtra("content", itemListFiltered.get(position).getContent());
                detailsIntent.putExtra("urlImage", itemListFiltered.get(position).getImage());

                v.getContext().startActivity(detailsIntent);
            }
        });



        //To change font
        //Typeface comic_sans = Typeface.createFromAsset(mContext.getAssets(), "fonts/comic_sans.ttf");
        //bodytext.setTypeface(comic_sans);
        //addresstext.setTypeface(comic_sans);


        return arg1;
    }
}

