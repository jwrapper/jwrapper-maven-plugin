package com.jwrapper.maven.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;

import net.sf.sevenzipjbinding.ExtractAskMode;
import net.sf.sevenzipjbinding.ExtractOperationResult;
import net.sf.sevenzipjbinding.IArchiveExtractCallback;
import net.sf.sevenzipjbinding.ISequentialOutStream;
import net.sf.sevenzipjbinding.ISevenZipInArchive;
import net.sf.sevenzipjbinding.PropID;
import net.sf.sevenzipjbinding.SevenZipException;

import org.slf4j.Logger;

/**
 * http://sevenzipjbind.sourceforge.net/basic_snippets.html#extracting-archive-
 * std-int-callback
 */
public class SevenExtractCallback implements IArchiveExtractCallback {

	private volatile int hash;
	private volatile int size;
	private volatile int index;
	private volatile boolean skipExtraction;

	private final ISevenZipInArchive inArchive;
	private final File folder;
	private final Logger logger;

	public SevenExtractCallback(final ISevenZipInArchive inArchive,
			final File folder, final Logger logger) {
		this.inArchive = inArchive;
		this.folder = folder;
		this.logger = logger;
	}

	@Override
	public ISequentialOutStream getStream(final int index,
			final ExtractAskMode extractAskMode) throws SevenZipException {

		this.index = index;

		skipExtraction = (Boolean) inArchive.getProperty(index,
				PropID.IS_FOLDER);

		if (skipExtraction || extractAskMode != ExtractAskMode.EXTRACT) {
			return null;
		}

		final String path = (String) inArchive.getProperty(index, PropID.PATH);

		logger.debug("Extracting path: {}", path);

		final File file = new File(folder, path);

		file.getParentFile().mkdirs();

		final FileOutputStream output;
		try {
			output = new FileOutputStream(file);
		} catch (final Exception e) {
			throw new SevenZipException(e);
		}

		return new ISequentialOutStream() {
			@Override
			public int write(final byte[] data) throws SevenZipException {
				hash ^= Arrays.hashCode(data);
				size += data.length;
				try {
					output.write(data);
				} catch (final Exception e) {
					throw new SevenZipException(e);
				}
				return data.length; // Return amount of proceed data
			}
		};

	}

	@Override
	public void prepareOperation(final ExtractAskMode extractAskMode)
			throws SevenZipException {
	}

	@Override
	public void setOperationResult(
			final ExtractOperationResult extractOperationResult)
			throws SevenZipException {

		if (skipExtraction) {
			return;
		}

		if (extractOperationResult == ExtractOperationResult.OK) {
			logger.debug("Extraction success. hash={} size={}",
					String.format("%08x", hash), size);
		} else {
			logger.error("Extraction failure.");
		}

		hash = 0;
		size = 0;
	}

	@Override
	public void setCompleted(final long completeValue) throws SevenZipException {
	}

	@Override
	public void setTotal(final long total) throws SevenZipException {
		logger.info("Extracted total: {} bytes.", total);
	}
}