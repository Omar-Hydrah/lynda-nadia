package com.nadia;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.Nullable;
import android.preference.PreferenceFragment;

public class PrefsActivity extends AppCompatActivity{

	private static final String TAG = "PrefsActivity";

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prefs);

		getFragmentManager()
			.beginTransaction()
			.add(R.id.prefs_content, new SettingsFragment())
			.commit();
	}

	public static class SettingsFragment extends PreferenceFragment{
		@Override
		public void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.settings);
		}
	}
}