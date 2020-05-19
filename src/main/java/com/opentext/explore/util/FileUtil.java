package com.opentext.explore.util;

import java.io.File;

public class FileUtil {
	public static boolean isFile(String path) {
		boolean isFile = false;
		
		File f = new File(path);
		if(f.exists() && !f.isDirectory()) { 
		    isFile = true;
		}
		
		return isFile;
	}
}
