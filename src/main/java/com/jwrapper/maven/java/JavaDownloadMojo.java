/**
 * Copyright (C) 2010-2013 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.jwrapper.maven.java;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.jwrapper.maven.BaseMojo;
import com.jwrapper.maven.util.ProgressInputStream;

/**
 * @goal java-download
 * 
 * @phase prepare-package
 * 
 * @inheritByDefault true
 * 
 * @requiresDependencyResolution test
 * 
 * @author Andrei Pozolotin
 */
public class JavaDownloadMojo extends BaseMojo {

	/**
	 * wget --no-cookies --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com"
	 * "http://download.oracle.com/otn-pub/java/jdk/7/jdk-7-linux-x64.tar.gz"
	 */

	/**
	 * Java download artifact even if already present.
	 * 
	 * @required
	 * @parameter default-value= "false"
	 */
	private boolean javaEveryTime;

	protected boolean javaEveryTime() {
		return javaEveryTime;
	}

	/**
	 * Java download artifact file.
	 * 
	 * @required
	 * @parameter default-value= "jre-6u45-linux-i586.bin"
	 */
	private String javaArtifact;

	protected String javaArtifact() {
		return javaArtifact;
	}

	/**
	 * Java download local folder.
	 * 
	 * @required
	 * @parameter default-value= "${java.io.tmpdir}/oracle"
	 */
	private String javaFolderLocal;

	protected String javaFolderLocal() {
		return javaFolderLocal;
	}

	/**
	 * Java download remote folder.
	 * 
	 * @required
	 * @parameter default-value=
	 *            "http://download.oracle.com/otn-pub/java/jdk/6u45-b06"
	 */
	private String javaFolderRemote;

	protected String javaFolderRemote() {
		return javaFolderRemote;
	}

	/**
	 * Java download headers.
	 * 
	 * @parameter
	 */
	private volatile Map<String, String> headerMap = new HashMap<String, String>();
	{
		/** Oracle "Accept Agreement" cookie. */
		headerMap.put("Cookie", "gpw_e24=http%3A%2F%2Fwww.oracle.com");
	}

	protected String javaLocalURL() {
		return javaFolderLocal() + "/" + javaArtifact();
	}

	protected String javaRemoteURL() {
		return javaFolderRemote() + "/" + javaArtifact();
	}

	protected void setupNonVerifingSSL() throws Exception {

		// Create a trust manager that does not validate certificate chains
		final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkClientTrusted(final X509Certificate[] arg0,
					final String arg1) throws CertificateException {
			}

			@Override
			public void checkServerTrusted(final X509Certificate[] arg0,
					final String arg1) throws CertificateException {
			}
		} };

		// Install the all-trusting trust manager
		final SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		// Create all-trusting host name verifier
		final HostnameVerifier allHostsValid = new HostnameVerifier() {
			@Override
			public boolean verify(final String hostname,
					final SSLSession session) {
				return true;
			}
		};

		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

	}

	protected HttpURLConnection connection(final String locationURL)
			throws Exception {

		final URL url = new URL(locationURL);

		final HttpURLConnection connection = (HttpURLConnection) url
				.openConnection();

		connection.setUseCaches(false);
		connection.setConnectTimeout(10 * 1000);
		connection.setInstanceFollowRedirects(true);

		for (final Map.Entry<String, String> entry : headerMap.entrySet()) {
			connection.setRequestProperty(entry.getKey(), entry.getValue());
		}

		connection.connect();

		return connection;

	}

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		try {

			setupNonVerifingSSL();

			final String javaRemoteURL = javaRemoteURL();
			final String javaLocalURL = javaLocalURL();

			logger().info("javaRemoteURL: {}", javaRemoteURL);
			logger().info("javaLocalURL : {}", javaLocalURL);

			final File file = new File(javaLocalURL());

			if (!javaEveryTime() && file.exists()) {
				logger().info("Java artifact is present, skip download.");
				return;
			} else {
				logger().info("Java artifact is missing, make download.");
			}

			file.getParentFile().mkdirs();

			/** Oracle likes redirects. */
			HttpURLConnection connection = connection(javaRemoteURL());
			while (connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
				connection = connection(connection.getHeaderField("Location"));
				logger().info("redirect: {}", connection);
			}

			final ProgressInputStream input = new ProgressInputStream(
					connection.getInputStream(),
					connection.getContentLengthLong());

			final PropertyChangeListener listener = new PropertyChangeListener() {
				long current = System.currentTimeMillis();

				@Override
				public void propertyChange(final PropertyChangeEvent event) {
					if (System.currentTimeMillis() - current > 1000) {
						current = System.currentTimeMillis();
						logger().info("progress: {}", event.getNewValue());
					}
				}
			};

			input.addPropertyChangeListener(listener);

			final OutputStream output = new FileOutputStream(file);

			IOUtils.copy(input, output);

			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);

			if (file.length() < 1000 * 1000) {
				throw new IllegalStateException("Download failure.");
			}

			logger().info("Java artifact downloaded: {} bytes.", file.length());

		} catch (final Throwable e) {
			logger().error("", e);
			throw new MojoExecutionException("", e);
		}
	}
}
