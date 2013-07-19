/**
 * Copyright (C) 2010-2013 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.jwrapper.maven.seven;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.jwrapper.maven.BaseMojo;

/**
 * @goal seven-move-folder
 * 
 * @phase prepare-package
 * 
 * @inheritByDefault true
 * 
 * @requiresDependencyResolution test
 * 
 * @author Andrei Pozolotin
 */
public class SevenMoveFolderMojo extends BaseMojo {

	/**
	 * Source folder to move form.
	 * 
	 * @parameter
	 */
	private String sevenMoveSource;

	protected String sevenMoveSource() {
		return sevenMoveSource;
	}

	/**
	 * Target folder to move into.
	 * 
	 * @parameter
	 */
	private String sevenMoveTarget;

	protected String sevenMoveTarget() {
		return sevenMoveTarget;
	}

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		try {

			logger().info("Move source: {}", sevenMoveSource());
			logger().info("Move target: {}", sevenMoveTarget());

			FileUtils.moveDirectory(new File(sevenMoveSource()), new File(
					sevenMoveTarget()));

		} catch (final Throwable e) {
			logger().error("", e);
			throw new MojoExecutionException("", e);
		}
	}

}
