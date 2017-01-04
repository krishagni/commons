package com.krishagni.commons.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.activation.FileTypeMap;

import org.apache.commons.io.IOUtils;
import org.springframework.mail.javamail.ConfigurableMimeFileTypeMap;

import com.krishagni.commons.io.CsvFileWriter;
import com.krishagni.commons.io.CsvWriter;

import au.com.bytecode.opencsv.CSVWriter;

public class Util {
	private static FileTypeMap fileTypesMap = null;

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

	public static String stringListToCsv(Collection<String> elements) {
		return stringListToCsv(elements.toArray(new String[0]), true);
	}

	public static String stringListToCsv(Collection<String> elements, boolean quotechar) {
		return stringListToCsv(elements.toArray(new String[0]), quotechar);
	}

	public static String stringListToCsv(String[] elements) {
		return stringListToCsv(elements, true);
	}

	public static String stringListToCsv(String[] elements, boolean quotechar) {
		StringWriter writer = new StringWriter();
		CsvWriter csvWriter = null;
		try {
			if (quotechar) {
				csvWriter = CsvFileWriter.createCsvFileWriter(writer, CSVWriter.DEFAULT_SEPARATOR);
			} else {
				csvWriter = CsvFileWriter.createCsvFileWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
			}

			csvWriter.writeNext(elements);
			csvWriter.flush();
			return writer.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (csvWriter != null) {
				try {
					csvWriter.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public static boolean isValidDateFormat(String format) {
		boolean isValid = true;
		try {
			new SimpleDateFormat(format);
		} catch (IllegalArgumentException e) {
			isValid = false;
		}

		return isValid;
	}

	public static String getFileContentType(String filename) {
		if (fileTypesMap == null) {
			synchronized (Util.class) {
				fileTypesMap = new ConfigurableMimeFileTypeMap();
			}
		}

		return fileTypesMap.getContentType(filename);
	}
}
