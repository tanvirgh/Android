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
import android.widget.ProgressBar;
import com.sinepulse.app.R.layout;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class ChangePasswordFragment_
    extends ChangePasswordFragment
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
            contentView_ = inflater.inflate(layout.change_password, container, false);
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

    public static ChangePasswordFragment_.FragmentBuilder_ builder() {
        return new ChangePasswordFragment_.FragmentBuilder_();
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        etOldPass = ((EditText) hasViews.findViewById(com.sinepulse.app.R.id.etOldPass));
        etConfirmPassword = ((EditText) hasViews.findViewById(com.sinepulse.app.R.id.etConfirmPassword));
        bSavePassword = ((Button) hasViews.findViewById(com.sinepulse.app.R.id.bSavePassword));
        pbChangePass = ((ProgressBar) hasViews.findViewById(com.sinepulse.app.R.id.pbChangePass));
        etNewPassword = ((EditText) hasViews.findViewById(com.sinepulse.app.R.id.etNewPassword));
        if (hasViews.findViewById(com.sinepulse.app.R.id.bSavePassword)!= null) {
            hasViews.findViewById(com.sinepulse.app.R.id.bSavePassword).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    ChangePasswordFragment_.this.onClick(view);
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

        public ChangePasswordFragment build() {
            ChangePasswordFragment_ fragment_ = new ChangePasswordFragment_();
            fragment_.setArguments(args_);
            return fragment_;
        }

    }

}
