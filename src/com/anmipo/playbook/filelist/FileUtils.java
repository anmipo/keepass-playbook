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
package com.anmipo.playbook.filelist;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;

public class FileUtils {
	
	public static String formatFileSize(long fileSize) {
	    if (fileSize <= 0) 
	    	return "0";
	    final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
	    int digitGroups = (int) (Math.log10(fileSize) / Math.log10(1024));
	    return new DecimalFormat("#,##0.#")
	    	.format(fileSize / Math.pow(1024, digitGroups)) 
	    	+ units[digitGroups];
	}

	public static String formatFileDate(long date) {
		DateFormat format = SimpleDateFormat.getDateTimeInstance(
				SimpleDateFormat.MEDIUM, SimpleDateFormat.SHORT);
		return format.format(new Date(date));
	}

	/** Checks if file/directory content can be read */
	public static boolean isReadable(String path) {
		File file = new File(path);
		return file.canRead();
	}
	
	/**
	 * Returns the file-name part of the given path, if any
	 * @param path
	 *         relative or absolute path; 
	 * @return
	 *         null, if <i>path</i> is null;
	 *         empty string, if there is no file-name part.
	 */
	public static String extractFileName(String path) {
		if (path == null) 
			return null;
		
		int pos = path.lastIndexOf(File.separatorChar);
		if (pos > 0)
			return path.substring(pos + 1);
		else
			return "";
	}

	/**
	 * Returns the directory names of the given path (that is, 
	 * the original path without file name part).
	 * @param path
	 *         relative or absolute path; 
	 * @return
	 *         null, if <i>path</i> is null;
	 *         empty string, if there is no directory part.
	 */
	public static String extractDirectoryPart(String path) {
		if (path == null)
			return null;
		
		int pos = path.lastIndexOf(File.separatorChar);
		if (pos >= 0)
			return path.substring(0, pos + 1);
		else
			return "";
	}

}
