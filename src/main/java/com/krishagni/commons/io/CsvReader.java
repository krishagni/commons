package com.krishagni.commons.io;

import java.io.Closeable;

public interface CsvReader extends Closeable {
	String[] getColumnNames();

	boolean isColumnPresent(String columnName);

	String getColumn(String columnName);

	String getColumn(int columnIndex);

	String[] getRow();

	boolean next();
}