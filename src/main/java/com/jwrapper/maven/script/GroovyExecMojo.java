/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.jwrapper.maven.script;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

/**
 * groovy script executor; has access to {@link MavenProject};
 * <p>
 * for example:
 * 
 * <pre>
 * def value = project.properties['key'] // read from project
 * project.properties['key'] = value + 1 // save into project
 * </pre>
 * 
 * @goal groovy-execute-script
 * 
 * @phase prepare-package
 * 
 * @inheritByDefault true
 * 
 * @requiresDependencyResolution test
 * 
 * @author Andrei Pozolotin
 * 
 */
public class GroovyExecMojo extends GroovyBaseMojo {

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {

		try {

			logger().info("groovy exec init ");

			final GroovyRunner runner = newRunner();

			if (groovyFile != null && groovyFile.exists()) {

				logger().info("groovy exec file : " + groovyFile);

				runner.execute(groovyFile);

			}

			if (groovyText != null && groovyText.length() != 0) {

				logger().info("groovy exec text : " + singleLine(groovyText));

				runner.execute(groovyText);

			}

			logger().info("groovy exec done ");

		} catch (final Exception e) {

			throw new MojoFailureException("bada-boom", e);

		}

	}

}
