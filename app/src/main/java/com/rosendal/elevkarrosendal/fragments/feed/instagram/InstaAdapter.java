package com.rosendal.elevkarrosendal.fragments.feed.instagram;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rosendal.elevkarrosendal.R;

import java.util.ArrayList;
import java.util.Calendar;

class InstaAdapter extends RecyclerView.Adapter<InstaAdapter.ViewHolder> {
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView username, day, likes, caption;
        ImageView image;
        LinearLayout comments;
        String link;
        FrameLayout divider;

        public ViewHolder(View v) {
            super(v);

            username = (TextView) v.findViewById(R.id.username);
            day = (TextView) v.findViewById(R.id.date);
            likes = (TextView) v.findViewById(R.id.likes);
            caption = (TextView) v.findViewById(R.id.caption);
            comments = (LinearLayout) v.findViewById(R.id.llComments);
            image = (ImageView) v.findViewById(R.id.image);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (link != null && !link.isEmpty()) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(link));
                        c.startActivity(i);
                    }
                }
            });
            divider = (FrameLayout) v.findViewById(R.id.divider);
        }
    }

    private ArrayList<InstaPost> data;
    private Context c;

    public InstaAdapter(ArrayList<InstaPost> posts, Context context) {
        data = posts;
        c = context;
    }

    public void setData(ArrayList<InstaPost> list) {
        data = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.insta_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        InstaPost post = data.get(position);

        holder.likes.setText(post.likes + " likes");
        holder.day.setText(getTimeAgo(post.created, Calendar.getInstance().getTimeInMillis()));
        holder.username.setText(post.user);
        holder.link = post.link;
        holder.divider.setVisibility(post.comments.length > 0 ? View.VISIBLE : View.GONE);
        Glide.with(c).load(post.urlStandard).placeholder(R.drawable.loading).into(holder.image);

        if (post.caption != null && !post.caption.isEmpty()) {
            String caption = post.user + " " + post.caption;
            Spannable spannable = new SpannableString(caption);
            spannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(c, R.color.primary300)), 0, post.user.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.caption.setText(spannable, TextView.BufferType.SPANNABLE);
        }
        holder.comments.removeAllViews();
        for (Comment comment : post.comments) {
            holder.comments.addView(createCommentView(comment));
        }
    }

    private View createCommentView(Comment comment) {
        TextView view = new TextView(c);
        String text = comment.user + " " + comment.text;
        Spannable spannable = new SpannableString(text);
        spannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(c, R.color.primary300)), 0, comment.user.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        view.setText(spannable, TextView.BufferType.SPANNABLE);
        view.setPadding(0, 0, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, c.getResources().getDisplayMetrics()));
        return view;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;


    public String getTimeAgo(long time, long time2) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        if (time > time2 || time <= 0) {
            return null;
        }

        final long diff = time2 - time;
        if (diff < MINUTE_MILLIS) {
            return "just nu";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + "m";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + "h";
        } else if (diff < 6 * DAY_MILLIS) {
            return diff / DAY_MILLIS + "d";
        } else {
            java.text.DateFormat format = DateFormat.getDateFormat(c);
            return format.format(time);
        }
    }
}
