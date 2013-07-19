<xsl:transform xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fun="http://function/names"
	exclude-result-prefixes="xs fun" version="2.0">

	<xsl:import href="identity.xsl" />

	<xsl:output method="xml" indent="yes" encoding="utf-8"
		omit-xml-declaration="yes" />

	<xsl:param name="target" />
	<xsl:param name="dependencies" />
	<xsl:param name="sevenLinuxExtract" />
	<xsl:param name="sevenWindowsExtract" />

	<xsl:template match="JWrapper">
		<xsl:result-document href="$target/result.xml">
			<xsl:apply-templates />
		</xsl:result-document>
	</xsl:template>

	<xsl:template match="Linux32JRE">
		<Linux32JRE>
			<xsl:value-of select="$sevenLinuxExtract" />
		</Linux32JRE>
	</xsl:template>

	<xsl:template match="Windows32JRE">
		<Windows32JRE>
			<xsl:value-of select="$sevenWindowsExtract" />
		</Windows32JRE>
	</xsl:template>

	<xsl:template match="FileList">
		<xsl:for-each
			select="collection(concat($dependencies,'/?select=*.jar;recurse=yes;on-error=warning'))">
			<File classpath='yes'>
				<xsl:value-of select="document-uri(.)" />
			</File>
		</xsl:for-each>
	</xsl:template>

</xsl:transform>
