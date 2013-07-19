/**
 * Copyright (C) 2010-2013 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.jwrapper.maven;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.project.MavenProject;
import org.apache.maven.settings.Settings;
import org.slf4j.Logger;
import org.slf4j.impl.MavenLoggerFactory;

/**
 * A base plug-in mojo.
 */
public abstract class BaseMojo extends AbstractMojo {

	protected Logger logger() {
		return MavenLoggerFactory.getLogger(getClass(), getLog());
	}

	/**
	 * Do not use during class init.
	 */
	protected Logger logger(final Class<?> klaz) {
		return MavenLoggerFactory.getLogger(klaz, getLog());
	}

	/**
	 * The Maven Session *
	 * 
	 * @required
	 * @readonly
	 * @parameter expression="${session}"
	 */
	private MavenSession session;

	protected MavenSession session() {
		return session;
	}

	/**
	 * @readonly
	 * @required
	 * @parameter expression="${project}"
	 */
	private MavenProject project;

	protected MavenProject project() {
		return project;
	}

	/**
	 * @readonly
	 * @required
	 * @parameter expression="${settings}"
	 */
	private Settings settings;

	protected Settings settings() {
		return settings;
	}

	/**
	 * @return if propName present - "project.propName", otherwise "propValue"
	 */
	protected String projectValue(final String propValue, final String propName) {
		if (propName == null) {
			return propValue;
		} else {
			return (String) project().getProperties().get(propName);
		}

	}

}
