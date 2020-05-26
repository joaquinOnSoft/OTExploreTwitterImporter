package com.opentext.explore.util;

import java.io.File;
import java.net.URL;

public class FileUtil {
	/**
	 * Get file from classpath, resources folder
	 * SEE: Java – Read a file from resources folder
	 * https://www.mkyong.com/java/java-read-a-file-from-resources-folder/
	 * @param fileName
	 * @return
	 */
	public static File getFileFromResources(String fileName) {
        URL resource = FileUtil.class.getClassLoader().getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            return new File(resource.getFile());
        }

    }	
	
	public static boolean isFile(String path) {
		boolean isFile = false;
		
		File f = new File(path);
		if(f.exists() && !f.isDirectory()) { 
		    isFile = true;
		}
		
		return isFile;
	}
	
	public static boolean deleteFile(String path) {
		boolean deleted = false;
		
		File f = new File(path);
		if(f.exists()) { 
		    deleted = f.delete();
		}
		
		return deleted;
	
	}
}
