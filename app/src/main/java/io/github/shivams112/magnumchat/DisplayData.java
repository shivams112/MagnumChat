package io.github.shivams112.magnumchat;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.github.shivams112.magnumchat.model.InstantMessage;

public class DisplayData extends RecyclerView.Adapter<DisplayData.ItemViewHolder> {

    private List<InstantMessage> mUserLsit=new ArrayList<>();
    private Context mContext;

    @NonNull
    @Override
    public DisplayData.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_msg_row,parent,false);
      return new ItemViewHolder(view);
    }

    public DisplayData(){}

    public DisplayData(Context mContext,List<InstantMessage> mUserLsit) {
        this.mContext=mContext;
        this.mUserLsit = mUserLsit;
    }

    @Override
    public void onBindViewHolder(@NonNull DisplayData.ItemViewHolder holder, int position) {
          InstantMessage msg = mUserLsit.get(position);
          try{

              boolean  isItMe = msg.getUser().equals("John");
              if(isItMe){
                  holder.muser.setTextColor(Color.GREEN);
                  holder.muser.setGravity(Gravity.END);
                  holder.myMesssage.setGravity(Gravity.END);
                  holder.myMesssage.setBackgroundResource(R.drawable.bubble2);
                  holder.muser.setText("You");
              }
              else{
                  holder.muser.setTextColor(Color.BLUE);
                  holder.myMesssage.setBackgroundResource(R.drawable.bubble1);
                  holder.muser.setGravity(Gravity.START);
                  holder.myMesssage.setGravity(Gravity.START);
                  holder.muser.setText(msg.getUser());
              }

              holder.myMesssage.setText(msg.getInputMsg());
          holder.itemView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                 // Toast.makeText(mContext, "You Clicked", Toast.LENGTH_SHORT).show();

              }
          });}
          catch (NullPointerException e){e.printStackTrace();}
    }

    @Override
    public int getItemCount() {
        return mUserLsit.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView muser, myMesssage;

        public ItemViewHolder(View itemView) {
            super(itemView);
            muser = itemView.findViewById(R.id.author);
            myMesssage = itemView.findViewById(R.id.message);

        }
    }
}


