<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<html>
			<body>
				<center>
					<h2>the notepad</h2>
					<table border="1">
						<tr>
							<td>name</td>
							<td>age</td>
							<td>tel</td>
						</tr>
						<xsl:for-each select="persons/person">
							<tr>
								<td>
									<xsl:value-of select="name" />
								</td>
								<td>
									<xsl:value-of select="age" />
								</td>
								<td>
									<xsl:value-of select="tel" />
								</td>
							</tr>
						</xsl:for-each>
					</table>
				</center>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
