//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.0-SNAPSHOT.
//


package com.sinepulse.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import com.sinepulse.app.R.layout;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class CameraStreamFragment_
    extends CameraStreamFragment
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();
    private View contentView_;

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView_ = super.onCreateView(inflater, container, savedInstanceState);
        if (contentView_ == null) {
            contentView_ = inflater.inflate(layout.mediaplayer, container, false);
        }
        return contentView_;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    public View findViewById(int id) {
        if (contentView_ == null) {
            return null;
        }
        return contentView_.findViewById(id);
    }

    public static CameraStreamFragment_.FragmentBuilder_ builder() {
        return new CameraStreamFragment_.FragmentBuilder_();
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        bCamera = ((Button) hasViews.findViewById(com.sinepulse.app.R.id.bCamera));
        surface = ((LiveSurface) hasViews.findViewById(com.sinepulse.app.R.id.surface));
        streamingButton = ((Button) hasViews.findViewById(com.sinepulse.app.R.id.MyStreamButton));
        spCamera = ((Spinner) hasViews.findViewById(com.sinepulse.app.R.id.spinner_camera));
        if (hasViews.findViewById(com.sinepulse.app.R.id.MyStreamButton)!= null) {
            hasViews.findViewById(com.sinepulse.app.R.id.MyStreamButton).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    CameraStreamFragment_.this.onClick(view);
                }

            }
            );
        }
        if (hasViews.findViewById(com.sinepulse.app.R.id.bCamera)!= null) {
            hasViews.findViewById(com.sinepulse.app.R.id.bCamera).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    CameraStreamFragment_.this.onClick(view);
                }

            }
            );
        }
        if (hasViews.findViewById(com.sinepulse.app.R.id.bRoom)!= null) {
            hasViews.findViewById(com.sinepulse.app.R.id.bRoom).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    CameraStreamFragment_.this.onClick(view);
                }

            }
            );
        }
        if (hasViews.findViewById(com.sinepulse.app.R.id.bDashboard)!= null) {
            hasViews.findViewById(com.sinepulse.app.R.id.bDashboard).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    CameraStreamFragment_.this.onClick(view);
                }

            }
            );
        }
        afterViewLoaded();
    }

    public static class FragmentBuilder_ {

        private Bundle args_;

        private FragmentBuilder_() {
            args_ = new Bundle();
        }

        public CameraStreamFragment build() {
            CameraStreamFragment_ fragment_ = new CameraStreamFragment_();
            fragment_.setArguments(args_);
            return fragment_;
        }

    }

}
