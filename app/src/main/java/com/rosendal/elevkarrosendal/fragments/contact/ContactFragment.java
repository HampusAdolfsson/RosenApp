package com.rosendal.elevkarrosendal.fragments.contact;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.rosendal.elevkarrosendal.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment
public class ContactFragment extends Fragment implements View.OnClickListener{
    private static final String facebookUri = "https://facebook.com/elevkarenrosendal";
    private static final String twitterUri = "https://twitter.com/elevkarrosendal";
    private static final String instagramUri = "http://instagram.com/elevkarenrosendal";
    private static final String webpageUri = "https://sites.google.com/a/rosendalsgymnasiet.se/rosnet/";

    @ViewById(R.id.layoutFacebook)
    LinearLayout facebook;
    @ViewById(R.id.layoutTwitter)
    LinearLayout twitter;
    @ViewById(R.id.layoutInstagram)
    LinearLayout instagram;
    @ViewById(R.id.layoutWebsite)
    LinearLayout webpage;
    @ViewById(R.id.layoutFeedback)
    LinearLayout feedback;

    @AfterViews
    void setListeners() {
        facebook.setOnClickListener(this);
        twitter.setOnClickListener(this);
        instagram.setOnClickListener(this);
        webpage.setOnClickListener(this);
        feedback.setOnClickListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        if (v == facebook) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(facebookUri));
            startActivity(i);
        } else if (v == twitter) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(twitterUri));
            startActivity(i);
        } else if (v == instagram) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(instagramUri));
            startActivity(i);
        } else if (v == webpage) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(webpageUri));
            startActivity(i);
        } else if (v == feedback) {
            Intent i = new Intent(Intent.ACTION_SENDTO);
            String uri = "mailto:" + Uri.parse(getResources().getString(R.string.feedback_sub));
            i.setData(Uri.parse(uri));
            startActivity(i);
        }
    }
}
