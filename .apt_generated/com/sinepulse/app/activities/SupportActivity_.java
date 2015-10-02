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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.ViewFlipper;
import com.sinepulse.app.R.id;
import com.sinepulse.app.R.layout;
import org.androidannotations.api.SdkVersionHelper;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class SupportActivity_
    extends SupportActivity
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
        setContentView(layout.help_page);
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

    public static SupportActivity_.IntentBuilder_ intent(Context context) {
        return new SupportActivity_.IntentBuilder_(context);
    }

    public static SupportActivity_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new SupportActivity_.IntentBuilder_(fragment);
    }

    public static SupportActivity_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new SupportActivity_.IntentBuilder_(supportFragment);
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        etStatus = ((EditText) hasViews.findViewById(id.etStatus));
        etTicketNo = ((EditText) hasViews.findViewById(id.etTicketNo));
        pbSingleTicket = ((ProgressBar) hasViews.findViewById(id.pbSingleTicket));
        etMessage = ((EditText) hasViews.findViewById(id.etMessage));
        ticketListView = ((ListView) hasViews.findViewById(id.lvTicketList));
        etDate = ((EditText) hasViews.findViewById(id.etDate));
        bCamera = ((Button) hasViews.findViewById(id.bCamera));
        bRoom = ((Button) hasViews.findViewById(id.bRoom));
        bCreateTIcket = ((Button) hasViews.findViewById(id.bCreateTIcket));
        btSubmitTicket = ((Button) hasViews.findViewById(id.btSubmitTicket));
        etTktMessage = ((EditText) hasViews.findViewById(id.etTktMessage));
        vfTicket = ((ViewFlipper) hasViews.findViewById(id.vfTicket));
        pbTicket = ((ProgressBar) hasViews.findViewById(id.pbTicket));
        pbCreateTicket = ((ProgressBar) hasViews.findViewById(id.pbCreateTicket));
        etSubject = ((EditText) hasViews.findViewById(id.etSubject));
        spType = ((Spinner) hasViews.findViewById(id.spType));
        etTktSubject = ((EditText) hasViews.findViewById(id.etTktSubject));
        bDashboard = ((Button) hasViews.findViewById(id.bDashboard));
        if (hasViews.findViewById(id.bCamera)!= null) {
            hasViews.findViewById(id.bCamera).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    SupportActivity_.this.onClick(view);
                }

            }
            );
        }
        if (hasViews.findViewById(id.bDashboard)!= null) {
            hasViews.findViewById(id.bDashboard).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    SupportActivity_.this.onClick(view);
                }

            }
            );
        }
        if (hasViews.findViewById(id.bRoom)!= null) {
            hasViews.findViewById(id.bRoom).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    SupportActivity_.this.onClick(view);
                }

            }
            );
        }
        if (hasViews.findViewById(id.bCreateTIcket)!= null) {
            hasViews.findViewById(id.bCreateTIcket).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    SupportActivity_.this.onClick(view);
                }

            }
            );
        }
        if (hasViews.findViewById(id.btSubmitTicket)!= null) {
            hasViews.findViewById(id.btSubmitTicket).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    SupportActivity_.this.onClick(view);
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
            intent_ = new Intent(context, SupportActivity_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            fragment_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, SupportActivity_.class);
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            fragmentSupport_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, SupportActivity_.class);
        }

        public Intent get() {
            return intent_;
        }

        public SupportActivity_.IntentBuilder_ flags(int flags) {
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
