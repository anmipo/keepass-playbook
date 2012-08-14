/*
 * Copyright 2012 Andrei Popleteev.
 *     
 * This file is part of KeePass for BlackBerry PlayBook.
 *
 *  KeePass for BlackBerry PlayBook is free software: you can redistribute it 
 *  and/or modify it under the terms of the GNU General Public License 
 *  as published by the Free Software Foundation, either version 2 
 *  of the License, or (at your option) any later version.
 *
 *  KeePass for BlackBerry PlayBook is distributed in the hope that 
 *  it will be useful, but WITHOUT ANY WARRANTY; without even the implied 
 *  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 *  See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with KeePass for BlackBerry PlayBook.  
 *  If not, see <http://www.gnu.org/licenses/>.
 */
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
