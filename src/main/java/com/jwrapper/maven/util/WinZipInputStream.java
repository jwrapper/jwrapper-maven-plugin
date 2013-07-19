package com.jwrapper.maven.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * http://stackoverflow.com/questions/7924895/how-can-i-read-from-a-winzip-self-
 * extracting-exe-zip-file-in-java
 */
public class WinZipInputStream extends FilterInputStream {

	static final byte[] HEADER = { 0x50, 0x4b, 0x03, 0x04 };

	boolean found;

	public WinZipInputStream(final InputStream is) {
		super(is);
	}

	@Override
	public int read() throws IOException {

		if (!found) {
			throw new IllegalStateException("Read 1 before headerf is found.");
		}

		return super.read();

	}

	int find() throws IOException {
		alpha: while (true) {
			super.mark(HEADER.length);
			for (final int a : HEADER) {
				final int b = super.read();
				if (b < 0) {
					return -1;
				}
				if (a != b) {
					continue alpha;
				}
			}
			found = true;
			return +1;
		}
	}

	@Override
	public int read(final byte[] b, final int off, final int len)
			throws IOException {

		System.out.println(" off=" + off + " len=" + len);

		if (!found) {
			if (find() < 0) {
				return -1;
			}
			super.reset();
		}

		return super.read(b, off, len);

	}

}
