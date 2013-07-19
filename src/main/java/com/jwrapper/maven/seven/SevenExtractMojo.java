/**
 * Copyright (C) 2010-2013 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.jwrapper.maven.seven;

import java.io.File;
import java.io.RandomAccessFile;

import net.sf.sevenzipjbinding.ArchiveFormat;
import net.sf.sevenzipjbinding.IArchiveExtractCallback;
import net.sf.sevenzipjbinding.ISevenZipInArchive;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.jwrapper.maven.BaseMojo;
import com.jwrapper.maven.util.SevenExtractCallback;

/**
 * @goal seven-extract
 * 
 * @phase prepare-package
 * 
 * @inheritByDefault true
 * 
 * @requiresDependencyResolution test
 * 
 * @author Andrei Pozolotin
 */
public class SevenExtractMojo extends BaseMojo {

	/**
	 * 7zip source archive file.
	 * 
	 * @required
	 * @parameter default-value=
	 *            "${java.io.tmpdir}/oracle/jre-6u45-linux-i586.bin"
	 */
	private String sevenArchive;

	protected String sevenArchive() {
		return sevenArchive;
	}

	/**
	 * 7zip target extract folder.
	 * 
	 * @required
	 * @parameter default-value= "target/extract"
	 */
	private String sevenFolder;

	protected String sevenFolder() {
		return sevenFolder;
	}

	/**
	 * 7zip override for archive format detection.
	 * 
	 * @parameter
	 */
	private String sevenFormat;

	protected ArchiveFormat sevenFormat() {
		for (final ArchiveFormat format : ArchiveFormat.values()) {
			if (format.name().equalsIgnoreCase(sevenFormat)) {
				return format;
			}
		}
		return null;
	}

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		try {

			logger().info("Extracting: {}", sevenArchive());
			logger().info("Destination: {}", sevenFolder());

			final RandomAccessFile randomAccessFile = new RandomAccessFile(
					sevenArchive(), "r");

			final ISevenZipInArchive inArchive = SevenZip.openInArchive(
					sevenFormat(), new RandomAccessFileInStream(
							randomAccessFile));

			logger().info("Archive format: {}",
					inArchive.getArchiveFormat().name());

			final int[] itemList = new int[inArchive.getNumberOfItems()];
			for (int index = 0; index < itemList.length; index++) {
				itemList[index] = index;
			}

			final IArchiveExtractCallback callback = new SevenExtractCallback(
					inArchive, new File(sevenFolder()), logger());

			inArchive.extract(itemList, false, callback);

		} catch (final Throwable e) {
			logger().error("", e);
			throw new MojoExecutionException("", e);
		}
	}

}
