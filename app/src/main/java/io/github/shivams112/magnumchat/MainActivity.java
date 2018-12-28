package io.github.shivams112.magnumchat;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jintin.mixadapter.MixAdapter;

import java.util.ArrayList;
import java.util.List;

import io.github.shivams112.magnumchat.model.GetImage;
import io.github.shivams112.magnumchat.model.InstantMessage;

public class MainActivity extends AppCompatActivity implements CustomDialogFragment.OnInputListener {

    private RecyclerView mRecyclerView;
    private ImageButton mImageButton;
    private final List<InstantMessage> mMessages = new ArrayList<>();
    private final List<GetImage> mImages = new ArrayList<>();
    DatabaseReference table_user;
    FirebaseDatabase firebaseDatabase;
    private ShimmerFrameLayout mShimmerViewContainer;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "Tap to load chats/Press Cam icon to load images", Toast.LENGTH_LONG).show();
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        mShimmerViewContainer.setVisibility(View.GONE);
        mRecyclerView = findViewById(R.id.chat_list);
        mImageButton = findViewById(R.id.loadimg);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(MainActivity.this, mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // do whatever
                        // Toast.makeText(MainActivity.this, "You Clicked", Toast.LENGTH_SHORT).show();
                        onTapped(view);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        mImageButton.setOnClickListener(new View.OnClickListener() {     //to load image msg
            @Override
            public void onClick(View v) {

                disableUI();
                mShimmerViewContainer.setVisibility(View.VISIBLE);
                mShimmerViewContainer.startShimmerAnimation();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (checkInternet()) {
                            Toast.makeText(MainActivity.this, "Internet is Active", Toast.LENGTH_SHORT).show();
                            initImageViews();
                            enableUI();

                        } else {
                            enableUI();
                            Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                            CustomDialogFragment fragment = new CustomDialogFragment();   //fragment starts
                            fragment.show(getFragmentManager(), "FragmentStarts");
                        }

                    }
                }, 500);

            }
        });

    }

    public void onTapped(View view) {          //When Screen is Tapped
        //Toast.makeText(this, "Screen Tapped for "+ i, Toast.LENGTH_SHORT).show();
        disableUI();
        mShimmerViewContainer.setVisibility(View.VISIBLE);
        mShimmerViewContainer.startShimmerAnimation();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (checkInternet()) {
                    Toast.makeText(MainActivity.this, "Internet is Active", Toast.LENGTH_SHORT).show();
                    initViews();
                    enableUI();

                } else {
                    enableUI();
                    Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    CustomDialogFragment fragment = new CustomDialogFragment();   //fragment starts
                    fragment.show(getFragmentManager(), "FragmentStarts");
                }

            }
        }, 500);

    }

    private boolean checkInternet() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
            return connected;
        } else
            connected = false;
        return connected;
    }

    public void initViews() {           //load text messages

        firebaseDatabase = FirebaseDatabase.getInstance();
        table_user = firebaseDatabase.getReference("message");
        mRecyclerView = findViewById(R.id.chat_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.scrollToPosition(mMessages.size() - 1);
        table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (mMessages.size() == 20) {
                    Toast.makeText(MainActivity.this, "No More Chats", Toast.LENGTH_SHORT).show();
                } else {
                    InstantMessage msg = dataSnapshot.child(String.valueOf(i)).getValue(InstantMessage.class);
                    mMessages.add(msg);
                    DisplayData data = new DisplayData(MainActivity.this, mMessages);
                    ImageDisplayData data1 = new ImageDisplayData(MainActivity.this, mImages);
                    MixAdapter<RecyclerView.ViewHolder> adapter = new MixAdapter<>();
                    adapter.addAdapter(data);
                    adapter.addAdapter(data1);
                    mRecyclerView.setAdapter(adapter);
                    mRecyclerView.scrollToPosition(data.getItemCount() - 1);
                    data.notifyDataSetChanged();
                }
                mShimmerViewContainer.setVisibility(View.GONE);
                mShimmerViewContainer.stopShimmerAnimation();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        i++;
    }

    private void disableUI() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void enableUI() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void sendInput() {
        if (checkInternet()) {
            initViews();
        } else {
            CustomDialogFragment fragment = new CustomDialogFragment();
            fragment.show(getFragmentManager(), "FragmentStarts");
        }
    }

    private void initImageViews() {            //load images

        firebaseDatabase = FirebaseDatabase.getInstance();
        table_user = firebaseDatabase.getReference("image");
        mRecyclerView = findViewById(R.id.chat_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.scrollToPosition(mMessages.size() - 1);
        table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //mMessages.clear();
                if (mMessages.size() == 20) {
                    Toast.makeText(MainActivity.this, "No More Chats", Toast.LENGTH_SHORT).show();
                } else {
                    GetImage msg = dataSnapshot.child(String.valueOf(i)).getValue(GetImage.class);
                    mImages.add(msg);
                    ImageDisplayData data = new ImageDisplayData(MainActivity.this, mImages);
                    DisplayData data1 = new DisplayData(MainActivity.this, mMessages);
                    MixAdapter<RecyclerView.ViewHolder> adapter = new MixAdapter<>();
                    adapter.addAdapter(data);
                    adapter.addAdapter(data1);
                    mRecyclerView.setAdapter(adapter);
                    mRecyclerView.scrollToPosition(data1.getItemCount() - 1);
                    data.notifyDataSetChanged();
                }
                mShimmerViewContainer.setVisibility(View.GONE);
                mShimmerViewContainer.stopShimmerAnimation();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        i++;


    }
}
