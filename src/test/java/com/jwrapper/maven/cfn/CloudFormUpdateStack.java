/**
 * Copyright (C) 2010-2013 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.jwrapper.maven.cfn;

import org.apache.maven.plugin.MojoFailureException;

/**
 * cloud formation:
 * 
 * <b><a href=
 * "http://docs.amazonwebservices.com/AWSCloudFormation/latest/APIReference/API_UpdateStack.html"
 * >update stack</a></b>
 * 
 * based on:
 * 
 * <b>stack name</b>
 * 
 * ; wait for completion or fail ({@link #stackTimeout})
 * 
 * @goal cloud-formation-update
 * 
 * @phase prepare-package
 * 
 * @inheritByDefault true
 * 
 * @requiresDependencyResolution test
 * 
 */
public class CloudFormUpdateStack extends CloudForm {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute() throws MojoFailureException {

		try {

			getLog().info("stack update init [" + stackName() + "]");

			getLog().error("stack update TODO");

			getLog().info("stack update done [" + stackName() + "]");

		} catch (final Exception e) {

			throw new MojoFailureException("bada-boom", e);

		}

	}

}
