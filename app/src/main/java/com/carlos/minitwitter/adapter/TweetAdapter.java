package com.carlos.minitwitter.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.carlos.minitwitter.R;
import com.carlos.minitwitter.common.Constant;
import com.carlos.minitwitter.common.SharedPreferencesManager;
import com.carlos.minitwitter.data.TweetViewModel;
import com.carlos.minitwitter.retrofit.response.TweetResponse;

import java.text.SimpleDateFormat;
import java.util.List;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private Context context;
    private List<TweetResponse> listTweet;
    private String userName;
    private SimpleDateFormat dateFormat;
    private TweetViewModel tweetViewModel;

    private static final String TAG = "TweetAdapter";

    public TweetAdapter(Context context, List<TweetResponse> listTweet) {
        this.listTweet = listTweet;
        this.context = context;
        userName = SharedPreferencesManager.getString(Constant.PREF_USERNAME);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
        tweetViewModel = new ViewModelProvider((FragmentActivity) this.context).get(TweetViewModel.class);
    }

    @NonNull
    @Override
    public TweetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_tweet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TweetAdapter.ViewHolder holder, int position) {

        if(listTweet == null)
            return;

        holder.tweetResponse = listTweet.get(position);
        holder.tUserName.setText("@".concat(holder.tweetResponse.getUserName()));
        holder.tMessage.setText(holder.tweetResponse.getMessage());
        holder.tTweetDate.setText(dateFormat.format(holder.tweetResponse.getCreatedDate()));

        if(holder.tweetResponse.getMyLike() > 0) {
            //estrella seleccionada
            Glide.with(context)
                    .load(R.drawable.ic_baseline_star_24_pink)
                    .into(holder.iStar);

            holder.iStar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tweetViewModel.unlike(holder.tweetResponse.getIdTweet());
                    Log.d(TAG, "unlike tweet");
                }
            });

        } else {
            //estrella no seleccionada
            Glide.with(context)
                    .load(R.drawable.ic_baseline_star_border_24)
                    .into(holder.iStar);

            holder.iStar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tweetViewModel.like(holder.tweetResponse.getIdTweet());
                    Log.d(TAG, "like tweet");
                }
            });
        }

        if(holder.tweetResponse.getCountLikes() > 0) {
            holder.tCountLikes.setText(String.valueOf(holder.tweetResponse.getCountLikes()));
            holder.tCountLikes.setTypeface(null, Typeface.BOLD);
        } else {
            holder.tCountLikes.setText("0");
            holder.tCountLikes.setTypeface(null, Typeface.NORMAL);
        }

        if(holder.tweetResponse.getUserName().equals(userName)) {
            holder.iShowMenuTweet.setVisibility(View.VISIBLE);
            holder.iShowMenuTweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tweetViewModel.openDialogMenuTweet(context, holder.tweetResponse.getIdTweet());
                }
            });
        } else {
            holder.iShowMenuTweet.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return listTweet == null ? 0 : listTweet.size();
    }

    public void setData(List<TweetResponse> listTweet) {
        this.listTweet = listTweet;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View view;
        public final ImageView iAvatar;
        public final ImageView iStar;
        public final TextView tUserName;
        public final TextView tMessage;
        public final TextView tCountLikes;
        public final TextView tTweetDate;
        public final ImageView iShowMenuTweet;
        public TweetResponse tweetResponse;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.view = view;
            iAvatar = view.findViewById(R.id.iAvatar);
            iStar = view.findViewById(R.id.iStar);
            tUserName = view.findViewById(R.id.tUserName);
            tMessage = view.findViewById(R.id.tMessage);
            tCountLikes = view.findViewById(R.id.tCountLikes);
            tTweetDate = view.findViewById(R.id.tTweetDate);
            iShowMenuTweet = view.findViewById(R.id.iShowMenuTweet);
        }
    }
}
