/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.jwrapper.maven.script;

import java.io.File;

import org.apache.maven.project.MavenProject;

import com.jwrapper.maven.BaseMojo;
import com.jwrapper.maven.util.MavenProps;

/**
 * base for groovy script goals
 */
public abstract class GroovyBaseMojo extends BaseMojo {

	public static String singleLine(String text) {

		text = text.replaceAll("\n", ";");
		text = text.replaceAll("\\s+", " ");

		return text;
	}

	/**
	 * groovy external script file; if present, executed before
	 * {@link #groovyText}
	 * 
	 * @required
	 * @parameter default-value="./target/script.groovy"
	 */
	protected File groovyFile;

	/**
	 * groovy script text in pom.xml; if present, executed after
	 * {@link #groovyFile}
	 * 
	 * @parameter default-value=""
	 */
	protected String groovyText;

	/**
	 * should load all system properties from {@link System#getenv()}
	 * {@link System#getProperties()} into project.properties during script
	 * execution?
	 * 
	 * @parameter default-value="true"
	 */
	protected boolean groovyIsSystemProperties;

	/**
	 * should load all user properties (a.k.a maven command line properties)
	 * into project.properties during script execution?
	 * 
	 * @parameter default-value="true"
	 */
	protected boolean groovyIsCommandProperties;

	/**
	 * should load all project properties from current pom.xml model state into
	 * project.properties during script execution?
	 * 
	 * @parameter default-value="true"
	 */
	protected boolean groovyIsProjectProperties;

	protected GroovyRunner newRunner() throws Exception {

		final MavenProps mavenProps = new MavenProps( //
				session(), project(), //
				groovyIsSystemProperties, //
				groovyIsCommandProperties, //
				groovyIsProjectProperties //
		);

		final MavenProject project = new MavenProjectAdaptor(mavenProps);

		final GroovyRunner runner = new GroovyRunner(project);

		return runner;

	}

}
