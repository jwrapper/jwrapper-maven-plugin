/**
 * Copyright (C) 2010-2013 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.jwrapper.maven.dns;

import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.jwrapper.maven.util.Util;

/**
 * route53:
 * 
 * <b><a href=
 * "http://docs.amazonwebservices.com/Route53/latest/DeveloperGuide/ListingRRS.html"
 * >list dns zone entires</a></b>
 * 
 * @goal route53-list-zone
 * 
 * @phase prepare-package
 * 
 * @inheritByDefault true
 * 
 * @requiresDependencyResolution test
 * 
 */
public class NameServListZone extends NameServ {

	/**
	 * dns zone name to be listed
	 * 
	 * @required
	 * @parameter default-value="default.example.com"
	 */
	private String dnsZoneName;

	/**
	 * name of the maven project.property that will contain dns name list after
	 * execution of this maven goal
	 * 
	 * @required
	 * @parameter default-value="dnsNameList"
	 */
	private String dnsResultProperty;

	/**
	 * separator character to use when building dns result name list
	 * 
	 * @required
	 * @parameter default-value=";"
	 */
	private String dnsResultSeparator;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {

		try {

			getLog().info("dns list init [" + dnsZoneName + "]");

			final CarrotRoute53 route53 = newRoute53();

			final List<String> list = route53.listZone(dnsZoneName);

			final String dnsResultNameList = Util.concatenate(list,
					dnsResultSeparator);

			getLog().info("dns name list : " + dnsResultNameList);

			project().getProperties().put(dnsResultProperty, dnsResultNameList);

			getLog().info("dns list done [" + dnsZoneName + "]");

		} catch (final Exception e) {

			throw new MojoFailureException("bada-boom", e);

		}

	}

}
