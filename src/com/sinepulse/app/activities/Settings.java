package com.sinepulse.app.activities;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import com.sinepulse.app.R;

public class Settings extends SherlockPreferenceActivity {

	public static ActionBar mSupportActionBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.settinglistbackground);
		//setTheme(R.style.AppTheme);
		super.onCreate(savedInstanceState);
		
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new SettingPreferenceFragment())
				.commit();
		createMenuBar();
	}

	private void createMenuBar() {
		mSupportActionBar = getSupportActionBar();
		mSupportActionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.tool_bar));
		mSupportActionBar.setIcon(R.drawable.sp_logo);
		mSupportActionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
//			MainActionbarBase.stackIndex.removeAllElements();
//			Intent i = CommonValues.getInstance().homeIntent;
//			startActivity(i);
			
		}
		return true;
	}



	/**
	 * This fragment shows the preferences for the first header.
	 */
	public static class SettingPreferenceFragment extends PreferenceFragment implements
			OnSharedPreferenceChangeListener {

		public CheckBoxPreference checkout_preference;
		public Preference serverurl_Preference;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			
			super.onCreate(savedInstanceState);
			// Load the preferences from an XML resource
			addPreferencesFromResource(R.xml.preferences);
			// new code block for preference change listener
			initializePreferenceControls();
 
		}

		/**
		 * 
		 */
		public void initializePreferenceControls() {
			checkout_preference = (CheckBoxPreference) getPreferenceManager()
					.findPreference("checkout_preference");
//			userlogin_preference = (Preference) getPreferenceManager()
//					.findPreference("loginuser");
			serverurl_Preference = getPreferenceManager()
					.findPreference("serverurl_Preference");
			
		}

		@Override
		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key) {
			
			// Let's do something when a preference value changes
			if (key.equals("serverurl_preference")) {
//				changePreferenceDependency();
			}	
//			} else if(key.equals(CommonConstraints.PREF_CUSTOMERID_KEY)){
//				findPreference(CommonConstraints.PREF_CUSTOMERID_KEY).setSummary("logget ind som :  "  +  UserInformation.Email);
//			}
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			
			return inflater.inflate(R.layout.preference_list, container, false);
		}

		@Override
		public void onResume() {
			mSupportActionBar.setTitle(R.string.settings);
			super.onResume();
			getPreferenceScreen().getSharedPreferences()
					.registerOnSharedPreferenceChangeListener(this);

		}

		@Override
		public void onDestroy() {
			super.onDestroy();
			getPreferenceManager().getSharedPreferences()
					.unregisterOnSharedPreferenceChangeListener(this);
		}
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		
	}

}

