<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/appData">
    <html>
    <head>
        <title>Recipes XSLT View</title>
        <style>
            table {
                border-collapse: collapse;
                width: 100%;
            }
            th, td {
                border: 1px solid #ddd;
                padding: 8px;
            }
            th {
                background-color: #f2f2f2;
                text-align: left;
            }
        </style>
    </head>
    <body>
        <h2>Recipes View (XSLT)</h2>
        <a href="/">Back to Home</a>
        <br/><br/>
        <table>
            <tr>
                <th>Title</th>
                <th>Cuisine 1</th>
                <th>Cuisine 2</th>
                <th>Difficulty</th>
            </tr>
            <xsl:variable name="userSkill" select="users/user[1]/skillLevel"/>
            <xsl:for-each select="recipes/recipe">
                <tr>
                    <xsl:choose>
                        <xsl:when test="difficulty = $userSkill">
                            <xsl:attribute name="style">background-color: yellow;</xsl:attribute>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:attribute name="style">background-color: lightgreen;</xsl:attribute>
                        </xsl:otherwise>
                    </xsl:choose>
                    <td><xsl:value-of select="title"/></td>
                    <td><xsl:value-of select="cuisine1"/></td>
                    <td><xsl:value-of select="cuisine2"/></td>
                    <td><xsl:value-of select="difficulty"/></td>
                </tr>
            </xsl:for-each>
        </table>
    </body>
    </html>
</xsl:template>

</xsl:stylesheet>
