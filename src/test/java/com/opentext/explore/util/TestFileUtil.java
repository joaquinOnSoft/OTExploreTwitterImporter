package com.opentext.explore.util;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import junit.framework.TestCase;

public class TestFileUtil extends TestCase {
	@Test
	public void testIsFile() {
		String cwd = null;
		try {
			cwd = (new File( "." )).getCanonicalPath();
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
		assertFalse(FileUtil.isFile(cwd));
	}
}
