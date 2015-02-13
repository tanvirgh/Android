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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;
import com.sinepulse.app.R.layout;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class RoomManagerFragment_
    extends RoomManagerFragment
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
            contentView_ = inflater.inflate(layout.room_status, container, false);
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

    public static RoomManagerFragment_.FragmentBuilder_ builder() {
        return new RoomManagerFragment_.FragmentBuilder_();
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        tvEmptyLog = ((TextView) hasViews.findViewById(com.sinepulse.app.R.id.tvEmptyLog));
        deviceLogProgressBar = ((ProgressBar) hasViews.findViewById(com.sinepulse.app.R.id.deviceLogProgressBar));
        tvDeviceLogHeadingText = ((TextView) hasViews.findViewById(com.sinepulse.app.R.id.tvDeviceLogHeadingText));
        pause = ((ImageView) hasViews.findViewById(com.sinepulse.app.R.id.pause));
        list_image = ((ImageView) hasViews.findViewById(com.sinepulse.app.R.id.list_image));
        up = ((ImageView) hasViews.findViewById(com.sinepulse.app.R.id.up));
        down = ((ImageView) hasViews.findViewById(com.sinepulse.app.R.id.down));
        ivDevice = ((ImageView) hasViews.findViewById(com.sinepulse.app.R.id.ivDevice));
        vfRoom = ((ViewFlipper) hasViews.findViewById(com.sinepulse.app.R.id.vfRoom));
        deviceLogListView = ((ListView) hasViews.findViewById(com.sinepulse.app.R.id.lvLogList));
        right = ((ImageView) hasViews.findViewById(com.sinepulse.app.R.id.right));
        porda = ((RelativeLayout) hasViews.findViewById(com.sinepulse.app.R.id.porda));
        roomlistProgressBar = ((ProgressBar) hasViews.findViewById(com.sinepulse.app.R.id.roomlistProgressBar));
        spinner1 = ((Spinner) hasViews.findViewById(com.sinepulse.app.R.id.spinner1));
        reset = ((ImageView) hasViews.findViewById(com.sinepulse.app.R.id.reset));
        streamingButton = ((Button) hasViews.findViewById(com.sinepulse.app.R.id.MyStreamButton));
        bSearch = ((Button) hasViews.findViewById(com.sinepulse.app.R.id.bSearch));
        left = ((ImageView) hasViews.findViewById(com.sinepulse.app.R.id.left));
        tvToday = ((TextView) hasViews.findViewById(com.sinepulse.app.R.id.tvToday));
        roomListView = ((ListView) hasViews.findViewById(com.sinepulse.app.R.id.lvRoomList));
        deviceListView = ((ListView) hasViews.findViewById(com.sinepulse.app.R.id.lvDeviceList));
        etDateFrom = ((EditText) hasViews.findViewById(com.sinepulse.app.R.id.etDateFrom));
        devicePropertyProgressBar = ((ProgressBar) hasViews.findViewById(com.sinepulse.app.R.id.devicePropertyProgressBar));
        spCamera = ((Spinner) hasViews.findViewById(com.sinepulse.app.R.id.spinner_camera));
        bRoom = ((Button) hasViews.findViewById(com.sinepulse.app.R.id.bRoom));
        btShowLog = ((Button) hasViews.findViewById(com.sinepulse.app.R.id.btShowLog));
        btAddRoom = ((Button) hasViews.findViewById(com.sinepulse.app.R.id.btAddRoom));
        etDateTo = ((EditText) hasViews.findViewById(com.sinepulse.app.R.id.etDateTo));
        seekBar1 = ((SeekBar) hasViews.findViewById(com.sinepulse.app.R.id.seekBar1));
        calibration = ((ImageView) hasViews.findViewById(com.sinepulse.app.R.id.calibration));
        btAddDevice = ((Button) hasViews.findViewById(com.sinepulse.app.R.id.btAddDevice));
        tvDeviceName = ((TextView) hasViews.findViewById(com.sinepulse.app.R.id.tvDeviceName));
        toggleButton = ((ToggleButton) hasViews.findViewById(com.sinepulse.app.R.id.toggleButton));
        tvYesterday = ((TextView) hasViews.findViewById(com.sinepulse.app.R.id.tvYesterday));
        devicelistProgressBar = ((ProgressBar) hasViews.findViewById(com.sinepulse.app.R.id.devicelistProgressBar));
        toggleButton2 = ((ToggleButton) hasViews.findViewById(com.sinepulse.app.R.id.toggleButton2));
        if (hasViews.findViewById(com.sinepulse.app.R.id.bCamera)!= null) {
            hasViews.findViewById(com.sinepulse.app.R.id.bCamera).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    RoomManagerFragment_.this.onClick(view);
                }

            }
            );
        }
        if (hasViews.findViewById(com.sinepulse.app.R.id.bRoom)!= null) {
            hasViews.findViewById(com.sinepulse.app.R.id.bRoom).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    RoomManagerFragment_.this.onClick(view);
                }

            }
            );
        }
        if (hasViews.findViewById(com.sinepulse.app.R.id.bDashboard)!= null) {
            hasViews.findViewById(com.sinepulse.app.R.id.bDashboard).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    RoomManagerFragment_.this.onClick(view);
                }

            }
            );
        }
        if (hasViews.findViewById(com.sinepulse.app.R.id.btShowLog)!= null) {
            hasViews.findViewById(com.sinepulse.app.R.id.btShowLog).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    RoomManagerFragment_.this.onClick(view);
                }

            }
            );
        }
        if (hasViews.findViewById(com.sinepulse.app.R.id.etDateFrom)!= null) {
            hasViews.findViewById(com.sinepulse.app.R.id.etDateFrom).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    RoomManagerFragment_.this.onClick(view);
                }

            }
            );
        }
        if (hasViews.findViewById(com.sinepulse.app.R.id.etDateTo)!= null) {
            hasViews.findViewById(com.sinepulse.app.R.id.etDateTo).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    RoomManagerFragment_.this.onClick(view);
                }

            }
            );
        }
        if (hasViews.findViewById(com.sinepulse.app.R.id.tvToday)!= null) {
            hasViews.findViewById(com.sinepulse.app.R.id.tvToday).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    RoomManagerFragment_.this.onClick(view);
                }

            }
            );
        }
        if (hasViews.findViewById(com.sinepulse.app.R.id.tvYesterday)!= null) {
            hasViews.findViewById(com.sinepulse.app.R.id.tvYesterday).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    RoomManagerFragment_.this.onClick(view);
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

        public RoomManagerFragment build() {
            RoomManagerFragment_ fragment_ = new RoomManagerFragment_();
            fragment_.setArguments(args_);
            return fragment_;
        }

    }

}
