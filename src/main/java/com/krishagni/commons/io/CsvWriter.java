package com.krishagni.commons.io;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

public interface CsvWriter extends Closeable {
	void writeAll(List<String[]> allLines);

	void writeNext(String[] nextLine);

	void flush() throws IOException;
}