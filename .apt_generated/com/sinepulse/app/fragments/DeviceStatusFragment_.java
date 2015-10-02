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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import com.sinepulse.app.R.layout;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class DeviceStatusFragment_
    extends DeviceStatusFragment
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
            contentView_ = inflater.inflate(layout.device_status, container, false);
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

    public static DeviceStatusFragment_.FragmentBuilder_ builder() {
        return new DeviceStatusFragment_.FragmentBuilder_();
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        checkoutBasketButton = ((Button) hasViews.findViewById(com.sinepulse.app.R.id.bCheckoutBasket));
        ivStep1 = ((TextView) hasViews.findViewById(com.sinepulse.app.R.id.step1imagetext));
        flipper = ((ViewFlipper) hasViews.findViewById(com.sinepulse.app.R.id.vfBasket));
        ivStep2 = ((TextView) hasViews.findViewById(com.sinepulse.app.R.id.step2imagetext));
        basketHeading = ((TextView) hasViews.findViewById(com.sinepulse.app.R.id.tvBasketHeadingText));
        basketListView = ((ListView) hasViews.findViewById(com.sinepulse.app.R.id.lvBasket));
        basketLayout = ((LinearLayout) hasViews.findViewById(com.sinepulse.app.R.id.basketview));
        if (hasViews.findViewById(com.sinepulse.app.R.id.step1imagetext)!= null) {
            hasViews.findViewById(com.sinepulse.app.R.id.step1imagetext).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    DeviceStatusFragment_.this.onClick(view);
                }

            }
            );
        }
        if (hasViews.findViewById(com.sinepulse.app.R.id.step2imagetext)!= null) {
            hasViews.findViewById(com.sinepulse.app.R.id.step2imagetext).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    DeviceStatusFragment_.this.onClick(view);
                }

            }
            );
        }
        if (hasViews.findViewById(com.sinepulse.app.R.id.bContinueShopping)!= null) {
            hasViews.findViewById(com.sinepulse.app.R.id.bContinueShopping).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    DeviceStatusFragment_.this.onClick(view);
                }

            }
            );
        }
        afterViewsLoaded();
    }

    public static class FragmentBuilder_ {

        private Bundle args_;

        private FragmentBuilder_() {
            args_ = new Bundle();
        }

        public DeviceStatusFragment build() {
            DeviceStatusFragment_ fragment_ = new DeviceStatusFragment_();
            fragment_.setArguments(args_);
            return fragment_;
        }

    }

}
