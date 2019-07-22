package io.cstham.oriontest.utils;

import android.support.design.widget.CoordinatorLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import io.cstham.oriontest.R;

public class TextBox {

    public View dynamicMessageView;

    public void addTextBox(LayoutInflater inflater, int resource, final RelativeLayout original_layout,
                           String title, String message){

        //final View dynamicMessageView = getLayoutInflater().inflate(R.layout.custom_message_boxes, presents_layout, false);
        dynamicMessageView = inflater.inflate(resource, original_layout, false);

        TextView title_textView = dynamicMessageView.findViewById(R.id.title);
        TextView message_textView = dynamicMessageView.findViewById(R.id.message);

        Button dismissBtn = dynamicMessageView.findViewById(R.id.dismissBtn);
        //dismissBtn.setOnClickListener(this);
        dismissBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                original_layout.removeView(dynamicMessageView);

            }
        });

        title_textView.setText(title);
        message_textView.setText(message);
        original_layout.addView(dynamicMessageView);
    }

    public void removeTextBox(RelativeLayout original_layout){
        if (dynamicMessageView!=null){
            original_layout.removeView(dynamicMessageView);
        }
    }
    //==============================================================================================
    public void addTextBox_coordLayout(LayoutInflater inflater, int resource, final CoordinatorLayout original_layout,
                           String title, String message){

        //final View dynamicMessageView = getLayoutInflater().inflate(R.layout.custom_message_boxes, presents_layout, false);
        dynamicMessageView = inflater.inflate(resource, original_layout, false);

        TextView title_textView = dynamicMessageView.findViewById(R.id.title);
        TextView message_textView = dynamicMessageView.findViewById(R.id.message);

        Button dismissBtn = dynamicMessageView.findViewById(R.id.dismissBtn);
        //dismissBtn.setOnClickListener(this);
        dismissBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                original_layout.removeView(dynamicMessageView);

            }
        });

        title_textView.setText(title);
        message_textView.setText(message);
        original_layout.addView(dynamicMessageView);
    }

    public void removeTextBox_coordLayout(CoordinatorLayout original_layout){
        if (dynamicMessageView!=null){
            original_layout.removeView(dynamicMessageView);
        }
    }









}
