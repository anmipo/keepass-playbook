/*
 * Copyright 2011 Brian Pellin.
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

import android.content.Context;
import android.text.ClipboardManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.keepass.R;

public class EntrySection extends RelativeLayout {
	
	public EntrySection(Context context) {
		this(context, null);
	}
	
	public EntrySection(Context context, AttributeSet attrs) {
		this(context, attrs, null, null);
	}
	
	public EntrySection(Context context, AttributeSet attrs, String title, String value) {
		super(context, attrs);
		
		inflate(context, title, value);
	}

	private void inflate(Context context, String title, String value) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.entry_section, this);
		
		setText(R.id.title, title);
		setText(R.id.value, value);
		setupCopyButton(value);
	}

	private void setupCopyButton(String value) {
		Button copyButton = (Button) findViewById(R.id.copy_button);
		final String text = value;
		copyButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				ClipboardManager cbdMgr = (ClipboardManager) getContext()
						.getSystemService(Context.CLIPBOARD_SERVICE);
				cbdMgr.setText(text);
				Toast.makeText(getContext(), R.string.copy_entry_performed, 
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void setText(int resId, String str) {
		if (str != null) {
			TextView tvTitle = (TextView) findViewById(resId);
			tvTitle.setText(str);
		}
		
	}
}
