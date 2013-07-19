/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.jwrapper.maven.script;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.File;

import org.apache.maven.project.MavenProject;

/**
 * groovy script executor which exposes maven project
 */
public class GroovyRunner {

	private final MavenProject project;

	public GroovyRunner(final MavenProject project) {
		this.project = project;
	}

	public Binding binding() {

		final Binding binding = new Binding();

		binding.setProperty("project", project);

		return binding;

	}

	public Object execute(final File script) throws Exception {

		final GroovyShell shell = new GroovyShell(binding());

		final Object result = shell.evaluate(script);

		return result;

	}

	public Object execute(final String script) throws Exception {

		final GroovyShell shell = new GroovyShell(binding());

		final Object result = shell.evaluate(script);

		return result;

	}

}
