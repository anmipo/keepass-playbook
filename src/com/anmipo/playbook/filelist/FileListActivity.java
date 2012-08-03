package com.anmipo.playbook.filelist;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.keepass.R;
import com.keepassdroid.intents.Intents;

public class FileListActivity extends ListActivity implements OnItemClickListener {
	public static final String FILE_NAME = "filename";
	private static final String ROOT = "/";
	
	private String currentDirectory;
	private String currentFileName;
	private FileAdapter listAdapter = null;
	private EditText editTextView;
	private TextView pathView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_list);
		editTextView = (EditText) findViewById(R.id.filename);
		pathView = (TextView) findViewById(R.id.path);
		
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
				currentFileName = editTextView.getText().toString();
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
		setFileName(FileUtils.extractFileName(startPath));
		
		String startDir = FileUtils.extractDirectoryPart(startPath);
		if (FileUtils.isReadable(startDir))	{
			showDirectory(startPath);
		} else {
			Toast.makeText(this, R.string.error_invalid_path, 
					Toast.LENGTH_SHORT).show();
			showDirectory(ROOT);
		}
	}
		
	private void showDirectory(String pathStr) {
		String requestedDir = FileUtils.extractDirectoryPart(pathStr);
		
		// make sure the directory is accessible
		if (!FileUtils.isReadable(requestedDir)) {
			Toast.makeText(this, R.string.error_invalid_path, 
					Toast.LENGTH_SHORT).show();
			return;
		}

		File dir = new File(requestedDir);
		if (dir.isDirectory()) {
			File parent = dir.getParentFile();
			if (parent != null) {
				currentDirectory = requestedDir;
			} else {
				currentDirectory = ROOT;
			}
			pathView.setText(currentDirectory);
			listAdapter.setItems(dir.listFiles(), parent);
			setSelection(0);
		} else {
			throw new RuntimeException("strange file object");
		}
	}
	
	private void setFileName(String fileName) {
		currentFileName = fileName;
		editTextView.setText(currentFileName);		
	}

	
	@Override
	public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
		FileAdapter adapter = (FileAdapter) listView.getAdapter(); 
		File item = adapter.getItem(position);
		if (item.isDirectory()) {
			showDirectory(item.getAbsolutePath() + File.separatorChar);
		} else {
			setFileName(item.getName());
		}
	}
	
	private class FileAdapter extends BaseAdapter {
		private File[] items;
		private File parent;
		private Drawable upIcon, folderIcon, fileIcon, kdbIcon; 
		
		public FileAdapter(Context context) {
			items = null;
			parent = null;
			upIcon = getResources().getDrawable(R.drawable.ic_file_parent); 
			folderIcon = getResources().getDrawable(R.drawable.ic_file_folder); 
			fileIcon = getResources().getDrawable(R.drawable.ic_file_generic); 
			kdbIcon = getResources().getDrawable(R.drawable.ic_file_kdb);
		}
		public void setItems(File[] items, File parent) {
			this.items = items;
			this.parent = parent;
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
			int result = 0;
			if (items != null) 
				result = items.length;
			if (parent != null) 
				result++;
			return result;
		}
		@Override
		public File getItem(int position) {
			if (parent != null) {
				return (position == 0) ? parent : items[position - 1];
			} else {
				return items[position];
			}
		}
		private String getItemName(File item) {
			if (item.isDirectory()) {
				String name = item.equals(parent) ? ".." : item.getName(); 
				return "[" + name + "]";
			} else {
				return item.getName();
			}
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			FileViewHolder viewHolder;
			View rowView = convertView;
			if (rowView == null) {
				rowView = getLayoutInflater().inflate(
						R.layout.file_list_item, null);
				viewHolder = new FileViewHolder();
				viewHolder.name = (TextView) rowView.findViewById(R.id.name);
				viewHolder.detail = (TextView) rowView.findViewById(R.id.detail);
				viewHolder.icon = (ImageView) rowView.findViewById(R.id.icon);
				rowView.setTag(viewHolder);
			} else {
				viewHolder = (FileViewHolder) convertView.getTag();
			}
			
			File item = getItem(position);
			viewHolder.name.setText(getItemName(item));
			viewHolder.detail.setText(getItemDetail(item));
			viewHolder.icon.setBackgroundDrawable(getItemIcon(item));
			
			return rowView;
		}

		private String getItemDetail(File file) {
			String detailStr; 
			if (file.equals(parent)) {
				detailStr = null;
			} else {
				detailStr = FileUtils.formatFileDate(file.lastModified()); 
				if (file.isFile()) {
					detailStr = FileUtils.formatFileSize(file.length()) + 
							",   " + detailStr; 
				}
			}
			return detailStr;
		}
		private Drawable getItemIcon(File item) {
			Drawable result;
			if (item.isDirectory()) {
				result = (item.equals(parent) ? upIcon : folderIcon);
			} else {
				String fileName = item.getName();
				if (fileName.endsWith(".kdb") || fileName.endsWith(".kdbx")) {
					result = kdbIcon;
				} else {
					result = fileIcon;
				}
			}
			return result;
		}
		private class FileViewHolder {
			public TextView name;
			public TextView detail;
			public ImageView icon;
		}
	}
}