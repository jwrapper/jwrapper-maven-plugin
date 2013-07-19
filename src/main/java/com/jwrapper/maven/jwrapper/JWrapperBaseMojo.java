/**
 * Copyright (C) 2010-2013 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.jwrapper.maven.jwrapper;

import com.jwrapper.maven.BaseMojo;

/**
 * base for elastic compute goals
 */
public abstract class JWrapperBaseMojo extends BaseMojo {

	/**
	 * JWrapper download artifact file.
	 * 
	 * @required
	 * @parameter default-value= "jwrapper-00020654351.jar"
	 */
	private String wrapperArtifact;

	/**
	 * JWrapper download local folder.
	 * 
	 * @required
	 * @parameter default-value= "${java.io.tmpdir}/jwrapper"
	 */
	private String wrapperFolderLocal;

	/**
	 * JWrapper download remote location.
	 * 
	 * @required
	 * @parameter default-value= "http://simple-help.com/media/static/jwrapper"
	 */
	private String wrapperFolderRemote;

	protected String wrapperArtifact() {
		return wrapperArtifact;
	}

	protected String wrapperFolderLocal() {
		return wrapperFolderLocal;
	}

	protected String wrapperFolderRemote() {
		return wrapperFolderRemote;
	}

	protected String wrapperLocalURL() {
		return wrapperFolderLocal() + "/" + wrapperArtifact();
	}

	protected String wrapperRemoteURL() {
		return wrapperFolderRemote() + "/" + wrapperArtifact();
	}

}
