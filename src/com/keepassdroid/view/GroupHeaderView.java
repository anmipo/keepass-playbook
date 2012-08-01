/*
 * Copyright 2009-2011 Brian Pellin.
 *     
 * This file is part of KeePassDroid.
 *
 *  KeePassDroid is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  KeePassDroid is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with KeePassDroid.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.keepassdroid.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.android.keepass.KeePass;
import com.android.keepass.R;
import com.keepassdroid.GroupActivity;

public class GroupHeaderView extends RelativeLayout {

	public GroupHeaderView(Context context) {
		this(context, null);
	}
	
	public GroupHeaderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		inflate(context);
		setupButtons();
	}
	
	private void setupButtons() {
		Button backButton = (Button) findViewById(R.id.back_button);
		if (backButton != null) {
			backButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					Activity act = (Activity)view.getContext();
					act.setResult(Activity.RESULT_CANCELED);
					act.finish();
				}
			});
		}
		
		View searchButton = findViewById(R.id.search_button);
		if (searchButton != null) {
			searchButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					((Activity)getContext()).onSearchRequested();
				}
			});
		}
	}

	private void inflate(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.group_header, this);
		
	}


}
