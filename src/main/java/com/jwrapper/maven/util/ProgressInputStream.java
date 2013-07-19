package com.jwrapper.maven.util;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ProgressInputStream extends FilterInputStream {

	private final PropertyChangeSupport propertyChangeSupport;
	private final long maxNumBytes;
	private volatile long totalNumBytesRead;

	public ProgressInputStream(final InputStream in, final long maxNumBytes) {
		super(in);
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		this.maxNumBytes = maxNumBytes;
	}

	public long getMaxNumBytes() {
		return maxNumBytes;
	}

	public long getTotalNumBytesRead() {
		return totalNumBytesRead;
	}

	public void addPropertyChangeListener(final PropertyChangeListener l) {
		propertyChangeSupport.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(final PropertyChangeListener l) {
		propertyChangeSupport.removePropertyChangeListener(l);
	}

	@Override
	public int read() throws IOException {
		final int b = super.read();
		updateProgress(1);
		return b;
	}

	// @Override
	// public int read(final byte[] b) throws IOException {
	// return (int) updateProgress(super.read(b));
	// }

	@Override
	public int read(final byte[] b, final int off, final int len)
			throws IOException {
		return (int) updateProgress(super.read(b, off, len));
	}

	@Override
	public long skip(final long n) throws IOException {
		return updateProgress(super.skip(n));
	}

	@Override
	public void mark(final int readlimit) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void reset() throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean markSupported() {
		return false;
	}

	private long updateProgress(final long numBytesRead) {
		if (numBytesRead > 0) {
			final long oldTotalNumBytesRead = this.totalNumBytesRead;
			this.totalNumBytesRead += numBytesRead;
			propertyChangeSupport.firePropertyChange("totalNumBytesRead",
					oldTotalNumBytesRead, this.totalNumBytesRead);
		}

		return numBytesRead;
	}
}