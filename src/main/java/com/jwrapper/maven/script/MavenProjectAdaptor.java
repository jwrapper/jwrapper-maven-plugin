/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.jwrapper.maven.script;

import java.util.Properties;

import com.jwrapper.maven.util.MavenProjectDelegate;
import com.jwrapper.maven.util.MavenProps;

/**
 * maven project proxy with custom properties processor
 */
public class MavenProjectAdaptor extends MavenProjectDelegate {

	private final Properties properties;

	public MavenProjectAdaptor(final MavenProps mavenProps) {

		super(mavenProps.project);

		this.properties = new MavenPropertiesAdaptor(mavenProps);

	}

	@Override
	public Properties getProperties() {

		return properties;

	}

}
