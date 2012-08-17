package com.keepassdroid.search;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.android.keepass.R;

public class SearchDialog extends Dialog implements OnEditorActionListener {
	
	public SearchDialog(Context context) {
		super(context);
		setContentView(R.layout.search_dialog);
		setTitle(android.R.string.search_go);

		EditText searchText = (EditText) findViewById(R.id.search_text);
		searchText.setOnEditorActionListener(this);
		
		View searchButton = findViewById(R.id.search_button);
		searchButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				performSearch();
			}
		});
	}
	private void performSearch() {
		SearchDialog.this.dismiss();
		Context context = SearchDialog.this.getContext();
		Intent intent = new Intent(context, SearchResults.class);
		intent.setAction(Intent.ACTION_SEARCH);
		intent.putExtra(SearchManager.QUERY, getQueryText());
		context.startActivity(intent);
	}
	private String getQueryText() {
		EditText searchText = (EditText) findViewById(R.id.search_text);
		return searchText.getText().toString();
	}
	
	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_NULL) {
			// Enter key was pressed
			performSearch();
			return true;
		} else {
			return false;
		}
	}
}
