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

public final class Home_
    extends Home
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
        setContentView(layout.main);
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

    public static Home_.IntentBuilder_ intent(Context context) {
        return new Home_.IntentBuilder_(context);
    }

    public static Home_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new Home_.IntentBuilder_(fragment);
    }

    public static Home_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new Home_.IntentBuilder_(supportFragment);
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        firstRowRightDown = ((TextView) hasViews.findViewById(id.firstRowRightDown));
        home_btn_fan = ((Button) hasViews.findViewById(id.home_btn_fan));
        thirdRowRightDown = ((TextView) hasViews.findViewById(id.thirdRowRightDown));
        right = ((ImageView) hasViews.findViewById(id.right));
        secondtRowRightUp = ((TextView) hasViews.findViewById(id.secondRowRightUp));
        bRoom = ((Button) hasViews.findViewById(id.bRoom));
        tvDeviceLogHeadingText = ((TextView) hasViews.findViewById(id.tvDeviceLogHeadingText));
        home_btn_room = ((Button) hasViews.findViewById(id.home_btn_room));
        tvToday = ((TextView) hasViews.findViewById(id.tvToday));
        onOffImage = ((ImageView) hasViews.findViewById(id.onOffImage));
        secondRowRightDown = ((TextView) hasViews.findViewById(id.secondRowRightDown));
        fourthtRowRightUp = ((TextView) hasViews.findViewById(id.fourthRowRightUp));
        toggleButton2 = ((ToggleButton) hasViews.findViewById(id.toggleButton2));
        deviceLogProgressBar = ((ProgressBar) hasViews.findViewById(id.deviceLogProgressBar));
        spinner1 = ((Spinner) hasViews.findViewById(id.spinner1));
        pause = ((ImageView) hasViews.findViewById(id.pause));
        firstRowRightUp = ((TextView) hasViews.findViewById(id.firstRowRightUp));
        vfDeviceType = ((ViewFlipper) hasViews.findViewById(id.vfDeviceType));
        fourthRowLeft = ((TextView) hasViews.findViewById(id.fourthRowLeft));
        ivDevice = ((ImageView) hasViews.findViewById(id.ivDevice));
        tvdeviceValue = ((TextView) hasViews.findViewById(id.tvdeviceValue));
        up = ((ImageView) hasViews.findViewById(id.up));
        list_image = ((ImageView) hasViews.findViewById(id.list_image));
        bSearch = ((Button) hasViews.findViewById(id.bSearch));
        reset = ((ImageView) hasViews.findViewById(id.reset));
        calibration = ((ImageView) hasViews.findViewById(id.calibration));
        secondRowLeft = ((TextView) hasViews.findViewById(id.secondRowLeft));
        thirdRowRightUp = ((TextView) hasViews.findViewById(id.thirdRowRightUp));
        curtainControl = ((RelativeLayout) hasViews.findViewById(id.curtainControl));
        devicelistProgressBar = ((ProgressBar) hasViews.findViewById(id.devicelistProgressBar));
        tvDeviceName = ((TextView) hasViews.findViewById(id.tvDeviceName));
        deviceListView = ((ListView) hasViews.findViewById(id.lvDeviceList));
        tvEmptyLog = ((TextView) hasViews.findViewById(id.tvEmptyLog));
        down = ((ImageView) hasViews.findViewById(id.down));
        home_btn_ac = ((Button) hasViews.findViewById(id.home_btn_ac));
        home_btn_bulb = ((Button) hasViews.findViewById(id.home_btn_bulb));
        devicePropertyProgressBar = ((ProgressBar) hasViews.findViewById(id.devicePropertyProgressBar));
        btAddDevice = ((ImageView) hasViews.findViewById(id.btAddDevice));
        left = ((ImageView) hasViews.findViewById(id.left));
        fourthRowRightDown = ((TextView) hasViews.findViewById(id.fourthRowRightDown));
        bCamera = ((Button) hasViews.findViewById(id.bCamera));
        btShowLog = ((Button) hasViews.findViewById(id.btShowLog));
        etDateTo = ((EditText) hasViews.findViewById(id.etDateTo));
        btn_total_power = ((Button) hasViews.findViewById(id.btn_total_power));
        home_btn_curtain = ((Button) hasViews.findViewById(id.home_btn_curtain));
        porda = ((RelativeLayout) hasViews.findViewById(id.porda));
        tvYesterday = ((TextView) hasViews.findViewById(id.tvYesterday));
        etDateFrom = ((EditText) hasViews.findViewById(id.etDateFrom));
        deviceLogListView = ((ListView) hasViews.findViewById(id.lvLogList));
        thirdRowLeft = ((TextView) hasViews.findViewById(id.thirdRowLeft));
        bDashboard = ((Button) hasViews.findViewById(id.bDashboard));
        dashBoardProgressBar = ((ProgressBar) hasViews.findViewById(id.dashBoardProgressBar));
        tvProgressValue = ((TextView) hasViews.findViewById(id.tvProgressValue));
        seekBar1 = ((SeekBar) hasViews.findViewById(id.seekBar1));
        btdevice_value = ((ToggleButton) hasViews.findViewById(id.btdevice_value));
        firstRowLeft = ((TextView) hasViews.findViewById(id.firstRowLeft));
        if (hasViews.findViewById(id.bCamera)!= null) {
            hasViews.findViewById(id.bCamera).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    Home_.this.onClick(view);
                }

            }
            );
        }
        if (hasViews.findViewById(id.bRoom)!= null) {
            hasViews.findViewById(id.bRoom).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    Home_.this.onClick(view);
                }

            }
            );
        }
        if (hasViews.findViewById(id.bDashboard)!= null) {
            hasViews.findViewById(id.bDashboard).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    Home_.this.onClick(view);
                }

            }
            );
        }
        if (hasViews.findViewById(id.btShowLog)!= null) {
            hasViews.findViewById(id.btShowLog).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    Home_.this.onClick(view);
                }

            }
            );
        }
        if (hasViews.findViewById(id.etDateFrom)!= null) {
            hasViews.findViewById(id.etDateFrom).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    Home_.this.onClick(view);
                }

            }
            );
        }
        if (hasViews.findViewById(id.etDateTo)!= null) {
            hasViews.findViewById(id.etDateTo).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    Home_.this.onClick(view);
                }

            }
            );
        }
        if (hasViews.findViewById(id.tvToday)!= null) {
            hasViews.findViewById(id.tvToday).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    Home_.this.onClick(view);
                }

            }
            );
        }
        if (hasViews.findViewById(id.tvYesterday)!= null) {
            hasViews.findViewById(id.tvYesterday).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    Home_.this.onClick(view);
                }

            }
            );
        }
        if (hasViews.findViewById(id.bSearch)!= null) {
            hasViews.findViewById(id.bSearch).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    Home_.this.onClick(view);
                }

            }
            );
        }
        afterViewsLoaded();
    }

    public static class IntentBuilder_ {

        private Context context_;
        private final Intent intent_;
        private android.app.Fragment fragment_;
        private android.support.v4.app.Fragment fragmentSupport_;

        public IntentBuilder_(Context context) {
            context_ = context;
            intent_ = new Intent(context, Home_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            fragment_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, Home_.class);
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            fragmentSupport_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, Home_.class);
        }

        public Intent get() {
            return intent_;
        }

        public Home_.IntentBuilder_ flags(int flags) {
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
