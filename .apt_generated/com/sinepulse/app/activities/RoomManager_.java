//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.0-SNAPSHOT.
//


package com.sinepulse.app.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
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
import com.sinepulse.app.R.id;
import com.sinepulse.app.R.layout;
import org.androidannotations.api.SdkVersionHelper;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class RoomManager_
    extends RoomManager
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(layout.room_status);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (((SdkVersionHelper.getSdkInt()< 5)&&(keyCode == KeyEvent.KEYCODE_BACK))&&(event.getRepeatCount() == 0)) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    public static RoomManager_.IntentBuilder_ intent(Context context) {
        return new RoomManager_.IntentBuilder_(context);
    }

    public static RoomManager_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new RoomManager_.IntentBuilder_(fragment);
    }

    public static RoomManager_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new RoomManager_.IntentBuilder_(supportFragment);
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        bRoom = ((Button) hasViews.findViewById(id.bRoom));
        devicelistProgressBar = ((ProgressBar) hasViews.findViewById(id.devicelistProgressBar));
        spCamera = ((Spinner) hasViews.findViewById(id.spinner_camera));
        ivDevice = ((ImageView) hasViews.findViewById(id.ivDevice));
        calibration = ((ImageView) hasViews.findViewById(id.calibration));
        porda = ((RelativeLayout) hasViews.findViewById(id.porda));
        bCamera = ((Button) hasViews.findViewById(id.bCamera));
        down = ((ImageView) hasViews.findViewById(id.down));
        deviceListView = ((ListView) hasViews.findViewById(id.lvDeviceList));
        deviceLogProgressBar = ((ProgressBar) hasViews.findViewById(id.deviceLogProgressBar));
        tvToday = ((TextView) hasViews.findViewById(id.tvToday));
        devicePropertyProgressBar = ((ProgressBar) hasViews.findViewById(id.devicePropertyProgressBar));
        toggleButton2 = ((ToggleButton) hasViews.findViewById(id.toggleButton2));
        right = ((ImageView) hasViews.findViewById(id.right));
        tvDeviceName = ((TextView) hasViews.findViewById(id.tvDeviceName));
        streamingButton = ((Button) hasViews.findViewById(id.MyStreamButton));
        btAddRoom = ((Button) hasViews.findViewById(id.btAddRoom));
        seekBar1 = ((SeekBar) hasViews.findViewById(id.seekBar1));
        list_image = ((ImageView) hasViews.findViewById(id.list_image));
        tvDeviceLogHeadingText = ((TextView) hasViews.findViewById(id.tvDeviceLogHeadingText));
        reset = ((ImageView) hasViews.findViewById(id.reset));
        spinner1 = ((Spinner) hasViews.findViewById(id.spinner1));
        pause = ((ImageView) hasViews.findViewById(id.pause));
        deviceLogListView = ((ListView) hasViews.findViewById(id.lvLogList));
        btAddDevice = ((Button) hasViews.findViewById(id.btAddDevice));
        left = ((ImageView) hasViews.findViewById(id.left));
        roomListView = ((ListView) hasViews.findViewById(id.lvRoomList));
        up = ((ImageView) hasViews.findViewById(id.up));
        etDateFrom = ((EditText) hasViews.findViewById(id.etDateFrom));
        roomlistProgressBar = ((ProgressBar) hasViews.findViewById(id.roomlistProgressBar));
        tvProgressValue = ((TextView) hasViews.findViewById(id.tvProgressValue));
        tvEmptyLog = ((TextView) hasViews.findViewById(id.tvEmptyLog));
        vfRoom = ((ViewFlipper) hasViews.findViewById(id.vfRoom));
        btShowLog = ((Button) hasViews.findViewById(id.btShowLog));
        tvYesterday = ((TextView) hasViews.findViewById(id.tvYesterday));
        etDateTo = ((EditText) hasViews.findViewById(id.etDateTo));
        bSearch = ((Button) hasViews.findViewById(id.bSearch));
        if (hasViews.findViewById(id.bCamera)!= null) {
            hasViews.findViewById(id.bCamera).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    RoomManager_.this.onClick(view);
                }

            }
            );
        }
        if (hasViews.findViewById(id.bRoom)!= null) {
            hasViews.findViewById(id.bRoom).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    RoomManager_.this.onClick(view);
                }

            }
            );
        }
        if (hasViews.findViewById(id.bDashboard)!= null) {
            hasViews.findViewById(id.bDashboard).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    RoomManager_.this.onClick(view);
                }

            }
            );
        }
        if (hasViews.findViewById(id.btShowLog)!= null) {
            hasViews.findViewById(id.btShowLog).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    RoomManager_.this.onClick(view);
                }

            }
            );
        }
        if (hasViews.findViewById(id.etDateFrom)!= null) {
            hasViews.findViewById(id.etDateFrom).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    RoomManager_.this.onClick(view);
                }

            }
            );
        }
        if (hasViews.findViewById(id.etDateTo)!= null) {
            hasViews.findViewById(id.etDateTo).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    RoomManager_.this.onClick(view);
                }

            }
            );
        }
        if (hasViews.findViewById(id.tvToday)!= null) {
            hasViews.findViewById(id.tvToday).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    RoomManager_.this.onClick(view);
                }

            }
            );
        }
        if (hasViews.findViewById(id.tvYesterday)!= null) {
            hasViews.findViewById(id.tvYesterday).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    RoomManager_.this.onClick(view);
                }

            }
            );
        }
        afterViewLoaded();
    }

    public static class IntentBuilder_ {

        private Context context_;
        private final Intent intent_;
        private android.app.Fragment fragment_;
        private android.support.v4.app.Fragment fragmentSupport_;

        public IntentBuilder_(Context context) {
            context_ = context;
            intent_ = new Intent(context, RoomManager_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            fragment_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, RoomManager_.class);
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            fragmentSupport_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, RoomManager_.class);
        }

        public Intent get() {
            return intent_;
        }

        public RoomManager_.IntentBuilder_ flags(int flags) {
            intent_.setFlags(flags);
            return this;
        }

        public void start() {
            context_.startActivity(intent_);
        }

        public void startForResult(int requestCode) {
            if (fragmentSupport_!= null) {
                fragmentSupport_.startActivityForResult(intent_, requestCode);
            } else {
                if (fragment_!= null) {
                    fragment_.startActivityForResult(intent_, requestCode);
                } else {
                    if (context_ instanceof Activity) {
                        ((Activity) context_).startActivityForResult(intent_, requestCode);
                    } else {
                        context_.startActivity(intent_);
                    }
                }
            }
        }

    }

}
