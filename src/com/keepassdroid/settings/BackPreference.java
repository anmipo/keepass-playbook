package com.keepassdroid.settings;

import android.content.Context;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.keepass.R;

public class BackPreference extends Preference {

	public BackPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupLayout();
	}
	public BackPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setupLayout();
	}
	private void setupLayout() {
		setLayoutResource(R.layout.preference_back);
		setSelectable(false);
	}
	
	@Override
	protected void onBindView(View prefView) {
		super.onBindView(prefView);
		Button backButton = (Button) prefView.findViewById(R.id.back_button);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View buttonView) {
				// [AP] I could not find a way to close a PreferenceScreen,
				// so closing the whole PreferenceActivity
				((PreferenceActivity)buttonView.getContext()).finish();
			}
		});
	}
}
