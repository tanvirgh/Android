//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.0-SNAPSHOT.
//


package com.sinepulse.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.sinepulse.app.R.layout;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class HomeFragment_
    extends HomeFragment
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
            contentView_ = inflater.inflate(layout.main, container, false);
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

    public static HomeFragment_.FragmentBuilder_ builder() {
        return new HomeFragment_.FragmentBuilder_();
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        bRoom = ((Button) hasViews.findViewById(com.sinepulse.app.R.id.bRoom));
        btn_total_power = ((Button) hasViews.findViewById(com.sinepulse.app.R.id.btn_total_power));
        thirdRowRightDown = ((TextView) hasViews.findViewById(com.sinepulse.app.R.id.thirdRowRightDown));
        thirdtRowRightUp = ((TextView) hasViews.findViewById(com.sinepulse.app.R.id.thirdRowRightUp));
        home_btn_bulb = ((Button) hasViews.findViewById(com.sinepulse.app.R.id.home_btn_bulb));
        bDashboard = ((Button) hasViews.findViewById(com.sinepulse.app.R.id.bDashboard));
        bCamera = ((Button) hasViews.findViewById(com.sinepulse.app.R.id.bCamera));
        firstRowRightUp = ((TextView) hasViews.findViewById(com.sinepulse.app.R.id.firstRowRightUp));
        home_btn_curtain = ((Button) hasViews.findViewById(com.sinepulse.app.R.id.home_btn_curtain));
        secondtRowRightUp = ((TextView) hasViews.findViewById(com.sinepulse.app.R.id.secondRowRightUp));
        home_btn_room = ((Button) hasViews.findViewById(com.sinepulse.app.R.id.home_btn_room));
        home_btn_fan = ((Button) hasViews.findViewById(com.sinepulse.app.R.id.home_btn_fan));
        secondRowRightDown = ((TextView) hasViews.findViewById(com.sinepulse.app.R.id.secondRowRightDown));
        secondRowLeft = ((TextView) hasViews.findViewById(com.sinepulse.app.R.id.secondRowLeft));
        firstRowLeft = ((TextView) hasViews.findViewById(com.sinepulse.app.R.id.firstRowLeft));
        thirdRowLeft = ((TextView) hasViews.findViewById(com.sinepulse.app.R.id.thirdRowLeft));
        firstRowRightDown = ((TextView) hasViews.findViewById(com.sinepulse.app.R.id.firstRowRightDown));
        afterViewsLoaded();
    }

    public static class FragmentBuilder_ {

        private Bundle args_;

        private FragmentBuilder_() {
            args_ = new Bundle();
        }

        public HomeFragment build() {
            HomeFragment_ fragment_ = new HomeFragment_();
            fragment_.setArguments(args_);
            return fragment_;
        }

    }

}
