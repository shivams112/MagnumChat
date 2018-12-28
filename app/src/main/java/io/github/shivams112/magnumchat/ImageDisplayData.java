package io.github.shivams112.magnumchat;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.github.shivams112.magnumchat.model.GetImage;

public class ImageDisplayData extends RecyclerView.Adapter<ImageDisplayData.ItemViewHolder> {

    private List<GetImage> mUserLsit=new ArrayList<>();
    private Context mContext;

    @NonNull
    @Override
    public ImageDisplayData.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.img_msg_row,parent,false);
        return new ItemViewHolder(view);
    }

    public ImageDisplayData(){}

    public ImageDisplayData(Context mContext,List<GetImage> mUserLsit) {
        this.mContext=mContext;
        this.mUserLsit = mUserLsit;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageDisplayData.ItemViewHolder holder, int position) {
        GetImage msg = mUserLsit.get(position);
        try{

            boolean  isItMe = msg.getUser().equals("John");
            if(isItMe){
                holder.muser.setTextColor(Color.GREEN);
                //holder.myImage.setBackgroundResource(R.drawable.bubble2);
                holder.muser.setText("You");
            }
            else{
                holder.muser.setTextColor(Color.BLUE);
                //holder.myImage.setBackgroundResource(R.drawable.bubble1);
                holder.muser.setText(msg.getUser());
            }

            Picasso.get().load(msg.getLink()).into(holder.myImage);

         }
        catch (NullPointerException e){e.printStackTrace();}
    }

    @Override
    public int getItemCount() {
        return mUserLsit.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView muser;
        ImageView myImage;

        public ItemViewHolder(View itemView) {
            super(itemView);
            muser = itemView.findViewById(R.id.author_img);
            myImage = itemView.findViewById(R.id.img);

        }
    }
}
