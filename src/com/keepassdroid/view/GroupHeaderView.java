/*
 * Copyright 2009-2011 Brian Pellin.
 * Modifications for BlackBerry PlayBook - Copyright 2012 Andrei Popleteev.
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
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.android.keepass.R;
import com.keepassdroid.search.SearchResults;
import com.keepassdroid.utils.EmptyUtils;

public class GroupHeaderView extends RelativeLayout {

	private EditText queryEdit;

	public GroupHeaderView(Context context) {
		this(context, null);
	}
	
	public GroupHeaderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		inflate(context);
		setupControls();
	}
	
	private void setupControls() {
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
		queryEdit = (EditText) findViewById(R.id.search_text);
		queryEdit.requestFocus();
		queryEdit.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				boolean handled = false;
				if (actionId == EditorInfo.IME_NULL) { // if Enter pressed
					String query = queryEdit.getText().toString();
					if (!EmptyUtils.isNullOrEmpty(query)) {
						performSearch(query);
						handled = true;
					}
				}
				return handled;
			}
		});
	}
	private void performSearch(String query) {		
		Context ctx = getContext();
		Intent intent = new Intent(ctx, SearchResults.class);
		intent.setAction(Intent.ACTION_SEARCH);
		intent.putExtra(SearchManager.QUERY, query);
		ctx.startActivity(intent);
	}
	
	private void inflate(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.group_header, this);
	}
}
