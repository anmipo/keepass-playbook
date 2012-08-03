package com.anmipo.playbook.filelist;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Comparator;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;

import com.android.keepass.R;
import com.keepassdroid.intents.Intents;

public class FileListActivity extends ListActivity implements OnItemClickListener {
	public static final String FILE_NAME = "filename";
	private static final String ROOT = "/";
	
	private String currentDirectory;
	private String currentFileName;
	private FileAdapter listAdapter = null;
	private EditText editText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_list);
		editText = (EditText) findViewById(R.id.filename);
		
		listAdapter = new FileAdapter(this);
		setListAdapter(listAdapter);
		
		View backButton = findViewById(R.id.back_button);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		
		View okButton = findViewById(R.id.ok);
		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intents.FILE_BROWSE);
				currentFileName = editText.getText().toString();
				intent.setData(Uri.parse("file://" + currentDirectory + currentFileName));
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		getListView().setOnItemClickListener(this);
		
		Uri intentData = getIntent().getData();
		String startPath = ROOT;
		if (intentData != null)
			startPath = intentData.getPath();
		showDirectory(startPath);
	}
	
	private String extractFileName(String path) {
		int pos = path.lastIndexOf(File.separatorChar);
		if (pos > 0)
			return path.substring(pos + 1);
		else
			return "";
	}

	private String extractDirectoryPart(String path) {
		int pos = path.lastIndexOf(File.separatorChar);
		if (pos > 0)
			return path.substring(0, pos + 1);
		else
			return ROOT;
	}

	private void showDirectory(String pathStr) {
		currentDirectory = extractDirectoryPart(pathStr);
		setFileName(extractFileName(pathStr));
		
		File dir = new File(currentDirectory);
		if (dir.isDirectory()) {
			listAdapter.setItems(dir.listFiles());
		} else {
			throw new RuntimeException("strange file object");
		}
	}
	
	private void setFileName(String fileName) {
		currentFileName = fileName;
		editText.setText(currentFileName);		
	}

	private class FileAdapter extends ArrayAdapter<String> {
		private File[] items;
		public FileAdapter(Context context) {
			super(context, android.R.layout.simple_list_item_1);
			items = null;
		}
		public void setItems(File[] items) {
			this.items = items;
			
			Arrays.sort(this.items, new Comparator<File>() {
				@Override
				//sort by name, but put directories first
				public int compare(File f1, File f2) {
					if (f1.isDirectory()) 
						return (f2.isDirectory() ? f1.compareTo(f2) : -1);
					else
						return (f2.isDirectory() ? 1 : f1.compareTo(f2));
				}
			});
			notifyDataSetChanged();
		}
		@Override
		public int getCount() {
			int nItems = (items != null) ? items.length : 0;
			return nItems + 1; // +1 is for the "[..]" item
		}
		@Override
		public String getItem(int position) {
			if (position == 0) {
				return "[..]";
			}
			File item = items[position - 1];
			if (item.isDirectory()) {
				return "[" + item.getName() + "]";
			} else {
				return item.getName();
			}
		}
		// TODO remove this when switch to BaseAdapter is complete
		public File getFileItem(int position) {
			return items[position - 1];
		}
	}

	@Override
	public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
		if (position == 0) {
			// "go to parent" clicked
			int pos = currentDirectory.lastIndexOf(File.separatorChar, 
					currentDirectory.length() - 1);
			if (pos > 0) {
				showDirectory(currentDirectory.substring(0, pos));
			} else {
				showDirectory(ROOT);
			}
		} else {
			FileAdapter adapter = (FileAdapter) listView.getAdapter(); 
			File item = adapter.getFileItem(position);
			if (item.isDirectory()) {
				showDirectory(currentDirectory + item.getName() + File.separatorChar);
			} else {
				setFileName(item.getName());
			}
		}
	}
}