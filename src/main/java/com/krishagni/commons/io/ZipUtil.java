package com.krishagni.commons.io;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

public final class ZipUtil {
	public static void extractZipToDestination(String filename, String destination) {
		ZipInputStream zipIn = null;

		try {
			if (StringUtils.isNotBlank(destination)) {
				destination += File.separator;
			} else {
				destination = StringUtils.EMPTY;
			}

			zipIn = new ZipInputStream(new FileInputStream(filename));
			extractZip(zipIn, destination);
		} catch (IOException e) {
			throw new RuntimeException("Can not extract zip, zip may be corrupted", e);
		} finally {
			IOUtils.closeQuietly(zipIn);
		}
	}

	public static void extractZipToDestination(InputStream in, String destination) {
		ZipInputStream zipIn = null;

		try {
			if (StringUtils.isNotBlank(destination)) {
				destination += File.separator;
			} else {
				destination = StringUtils.EMPTY;
			}

			zipIn = new ZipInputStream(in);
			extractZip(zipIn, destination);
		} catch (IOException e) {
			throw new RuntimeException("Can not extract zip, zip may be corrupted", e);
		} finally {
			IOUtils.closeQuietly(zipIn);
		}
	}

	public static File zipDirectory(String srcDir, String destPath) {
		ZipOutputStream zipOut = null;
		FileOutputStream fout = null;
		File destZip = null;

		try {
			destZip = new File(destPath);
			ensureDirExists(srcDir);

			fout   = new FileOutputStream(destZip);
			zipOut = new ZipOutputStream(fout);

			addDirToZip("", srcDir, zipOut);
			zipOut.flush();
		} catch (IOException e) {
			throw new RuntimeException("Error occurred while zipping source directory", e);
		} finally {
			IOUtils.closeQuietly(zipOut);
			IOUtils.closeQuietly(fout);
		}

		return destZip;
	}


	public static File zipFiles(List<String> inputFiles, String destPath) {
		ZipOutputStream zipOut = null;
		FileOutputStream fout = null;
		File destZip = null;

		try {
			destZip = new File(destPath);
			fout = new FileOutputStream(destZip);
			zipOut = new ZipOutputStream(fout);

			for(String inputFile : inputFiles) {
				FileInputStream fin = null;
				try {
					fin = new FileInputStream(inputFile);
					zipOut.putNextEntry(new ZipEntry(inputFile));
					IOUtils.copy(fin, zipOut);
				} finally {
					IOUtils.closeQuietly(fin);
				}
			}

			zipOut.flush();
		} catch (IOException e) {
			throw new RuntimeException("Error occurred while zipping input files", e);
		} finally {
			IOUtils.closeQuietly(zipOut);
			IOUtils.closeQuietly(fout);
		}

		return destZip;
	}

	private static void extractZip(ZipInputStream zipIn, String dest)
			throws IOException {
		ZipEntry entry = null;
		while ((entry = zipIn.getNextEntry()) != null) {
			if (!entry.isDirectory()) {
				extractZipEntryToFile(zipIn, dest, entry.getName());
				zipIn.closeEntry();
			}
		}
	}

	private static void extractZipEntryToFile(ZipInputStream zipIn, String dest, String entryName)
			throws IOException {
		FileOutputStream fout = null;
		try {
			File newFile = new File(dest + entryName);
			createParentDirectories(newFile);

			fout = new FileOutputStream(newFile);
			IOUtils.copy(zipIn, fout);
		} finally {
			IOUtils.closeQuietly(fout);
		}
	}

	private static void createParentDirectories(File newFile) {
		File parentFile = newFile.getParentFile();
		if (parentFile != null && !parentFile.exists() && !parentFile.mkdirs()) {
			throw new RuntimeException("Can not create directory " + parentFile);
		}
	}

	private static void ensureDirExists(String srcDir) {
		File folder = new File(srcDir);
		if (!folder.exists() || !folder.isDirectory()) {
			throw new RuntimeException(srcDir + " does not exist. Please specify correct path");
		}
	}

	private static void addDirToZip(String path, String srcDir, ZipOutputStream zipOut)
	throws IOException {
		File srcDirFile = new File(srcDir);

		for (String fileName : srcDirFile.list()) {
			if (StringUtils.isBlank(path)) {
				addFileToZip(srcDirFile.getName(), srcDir + File.separator + fileName, zipOut);
			} else {
				addFileToZip(path + File.separator + srcDirFile.getName(), srcDir + File.separator + fileName, zipOut);
			}
		}
	}

	private static void addFileToZip(String path, String srcPath, ZipOutputStream zipOut)
	throws IOException {
		File srcFile = new File(srcPath);
		if (srcFile.isDirectory()) {
			addDirToZip(path, srcPath, zipOut);
		} else {
			FileInputStream fin = null;
			try {
				fin = new FileInputStream(srcPath);
				zipOut.putNextEntry(new ZipEntry(path + File.separator + srcFile.getName()));
				IOUtils.copy(fin, zipOut);
			} finally {
				IOUtils.closeQuietly(fin);
			}
		}
	}
}
