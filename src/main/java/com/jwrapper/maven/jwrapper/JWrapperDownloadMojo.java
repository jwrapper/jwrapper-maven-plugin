/**
 * Copyright (C) 2010-2013 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.jwrapper.maven.jwrapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * @goal wrapper-download
 * 
 * @phase prepare-package
 * 
 * @inheritByDefault true
 * 
 * @requiresDependencyResolution test
 * 
 * @author Andrei Pozolotin
 */
public class JWrapperDownloadMojo extends JWrapperBaseMojo {

	/**
	 * JWrapper download artifact even if already present.
	 * 
	 * @required
	 * @parameter default-value= "false"
	 */
	private boolean wrapperEveryTime;

	protected boolean wrapperEveryTime() {
		return wrapperEveryTime;
	}

	@Override
	public void execute() throws MojoExecutionException {
		try {

			final String wrapperRemoteURL = wrapperRemoteURL();
			final String wrapperLocalURL = wrapperLocalURL();

			logger().info("wrapperRemoteURL: {}", wrapperRemoteURL);
			logger().info("wrapperLocalURL : {}", wrapperLocalURL);

			final File file = new File(wrapperLocalURL());

			if (!wrapperEveryTime() && file.exists()) {
				logger().info("JWrapper artifact is present, skip download.");
				return;
			} else {
				logger().info("JWrapper artifact is missing, make download.");
			}

			file.getParentFile().mkdirs();

			final InputStream input = new URL(wrapperRemoteURL()).openStream();
			final OutputStream output = new FileOutputStream(file);

			IOUtils.copy(input, output);

			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);

			if (file.length() < 1000 * 1000) {
				throw new IllegalStateException("Download failure.");
			}

			logger().info("JWrapper artifact downloaded: {} bytes.",
					file.length());

		} catch (final Throwable e) {
			logger().error("", e);
			throw new MojoExecutionException("", e);
		}
	}

}
