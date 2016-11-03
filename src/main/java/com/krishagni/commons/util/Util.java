package com.krishagni.commons.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;

public class Util {
	public static void unzip(String zipFilePath, String destDirPath) {
		FileInputStream fin = null;
		try {
			fin = new FileInputStream(zipFilePath);
			unzip(fin, destDirPath);
		} catch (Exception e) {
			throw new RuntimeException("Error opening zip file: " + zipFilePath, e);
		} finally {
			IOUtils.closeQuietly(fin);
		}
	}

	public static void unzip(InputStream in, String destDirPath) {
		File destDir = new File(destDirPath);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}

		ZipInputStream zipIn = null;
		try {
			zipIn = new ZipInputStream(in);

			ZipEntry entry = null;
			while ((entry = zipIn.getNextEntry()) != null) {
				String filePath = destDirPath + File.separator + entry.getName();
				if (!entry.isDirectory()) {
					copyToFile(zipIn, filePath);
				} else {
					new File(filePath).mkdirs();
				}

				zipIn.closeEntry();
			}
		} catch (Exception e) {
			throw new RuntimeException("Error inflating zip input stream", e);
		} finally {
			IOUtils.closeQuietly(zipIn);
		}
	}

	public static void copyToFile(InputStream in, String filePath)
	throws IOException {
		BufferedOutputStream bos = null;
		try {
			File file = new File(filePath);
			if (file.getParentFile() != null && !file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}

			bos = new BufferedOutputStream(new FileOutputStream(filePath));
			IOUtils.copy(in, bos);
		} finally {
			IOUtils.closeQuietly(bos);
		}
	}
}
