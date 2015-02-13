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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.sinepulse.app.R.layout;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class UserLogFragment_
    extends UserLogFragment
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
            contentView_ = inflater.inflate(layout.user_log, container, false);
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

    public static UserLogFragment_.FragmentBuilder_ builder() {
        return new UserLogFragment_.FragmentBuilder_();
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        bRoom = ((Button) hasViews.findViewById(com.sinepulse.app.R.id.bRoom));
        bDashboard = ((Button) hasViews.findViewById(com.sinepulse.app.R.id.bDashboard));
        bCamera = ((Button) hasViews.findViewById(com.sinepulse.app.R.id.bCamera));
        tvToday = ((TextView) hasViews.findViewById(com.sinepulse.app.R.id.tvToday));
        bDeliverydate = ((Button) hasViews.findViewById(com.sinepulse.app.R.id.bDeliverydate));
        tvEmptyLog = ((TextView) hasViews.findViewById(com.sinepulse.app.R.id.tvEmptyLog));
        deviceLogListView = ((ListView) hasViews.findViewById(com.sinepulse.app.R.id.lvLogList));
        etDateFrom = ((EditText) hasViews.findViewById(com.sinepulse.app.R.id.etDateFrom));
        bSearch = ((Button) hasViews.findViewById(com.sinepulse.app.R.id.bSearch));
        etDateTo = ((EditText) hasViews.findViewById(com.sinepulse.app.R.id.etDateTo));
        deviceLogProgressBar = ((ProgressBar) hasViews.findViewById(com.sinepulse.app.R.id.deviceLogProgressBar));
        tvYesterday = ((TextView) hasViews.findViewById(com.sinepulse.app.R.id.tvYesterday));
        if (hasViews.findViewById(com.sinepulse.app.R.id.bDeliverydate)!= null) {
            hasViews.findViewById(com.sinepulse.app.R.id.bDeliverydate).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    UserLogFragment_.this.onClick(view);
                }

            }
            );
        }
        if (hasViews.findViewById(com.sinepulse.app.R.id.etDateFrom)!= null) {
            hasViews.findViewById(com.sinepulse.app.R.id.etDateFrom).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    UserLogFragment_.this.onClick(view);
                }

            }
            );
        }
        if (hasViews.findViewById(com.sinepulse.app.R.id.etDateTo)!= null) {
            hasViews.findViewById(com.sinepulse.app.R.id.etDateTo).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    UserLogFragment_.this.onClick(view);
                }

            }
            );
        }
        if (hasViews.findViewById(com.sinepulse.app.R.id.tvToday)!= null) {
            hasViews.findViewById(com.sinepulse.app.R.id.tvToday).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    UserLogFragment_.this.onClick(view);
                }

            }
            );
        }
        if (hasViews.findViewById(com.sinepulse.app.R.id.tvYesterday)!= null) {
            hasViews.findViewById(com.sinepulse.app.R.id.tvYesterday).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    UserLogFragment_.this.onClick(view);
                }

            }
            );
        }
        if (hasViews.findViewById(com.sinepulse.app.R.id.bCamera)!= null) {
            hasViews.findViewById(com.sinepulse.app.R.id.bCamera).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    UserLogFragment_.this.onClick(view);
                }

            }
            );
        }
        if (hasViews.findViewById(com.sinepulse.app.R.id.bDashboard)!= null) {
            hasViews.findViewById(com.sinepulse.app.R.id.bDashboard).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    UserLogFragment_.this.onClick(view);
                }

            }
            );
        }
        if (hasViews.findViewById(com.sinepulse.app.R.id.bRoom)!= null) {
            hasViews.findViewById(com.sinepulse.app.R.id.bRoom).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    UserLogFragment_.this.onClick(view);
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

        public UserLogFragment build() {
            UserLogFragment_ fragment_ = new UserLogFragment_();
            fragment_.setArguments(args_);
            return fragment_;
        }

    }

}
