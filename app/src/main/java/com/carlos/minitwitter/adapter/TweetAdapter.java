package com.carlos.minitwitter.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.carlos.minitwitter.R;
import com.carlos.minitwitter.common.Constant;
import com.carlos.minitwitter.common.SharedPreferencesManager;
import com.carlos.minitwitter.retrofit.response.TweetResponse;

import java.text.SimpleDateFormat;
import java.util.List;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private Context context;
    private List<TweetResponse> listTweet;
    private String userName;
    private SimpleDateFormat dateFormat;

    public TweetAdapter(Context context, List<TweetResponse> listTweet) {
        this.listTweet = listTweet;
        this.context = context;
        userName = SharedPreferencesManager.getString(Constant.PREF_USERNAME);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
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
        holder.tUserName.setText(holder.tweetResponse.getUserName());
        holder.tMessage.setText(holder.tweetResponse.getMessage());
        holder.tTweetDate.setText(dateFormat.format(holder.tweetResponse.getCreatedDate()));

        if(holder.tweetResponse.getCountLikes() > 0) {
            holder.iStar.setColorFilter(context.getColor(R.color.colorRedDarken4));
            holder.tCountLikes.setText(String.valueOf(holder.tweetResponse.getCountLikes()));
            holder.tCountLikes.setTypeface(null, Typeface.BOLD);
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
        }
    }
}
