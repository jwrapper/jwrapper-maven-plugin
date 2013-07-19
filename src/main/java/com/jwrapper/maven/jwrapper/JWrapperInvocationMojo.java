/**
 * Copyright (C) 2010-2013 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.jwrapper.maven.jwrapper;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * @author Andrei Pozolotin
 */
public class JWrapperInvocationMojo extends JWrapperBaseMojo {

	/**
	 * JWrapper download artifact even if already present.
	 * 
	 * @required
	 * @parameter default-value= "false"
	 */
	private boolean wrapperDownloadEveryTime;

	protected boolean wrapperDownloadEveryTime() {
		return wrapperDownloadEveryTime;
	}

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		try {

		} catch (final Throwable e) {
			logger().error("", e);
			throw new MojoExecutionException("", e);
		}
	}

}
