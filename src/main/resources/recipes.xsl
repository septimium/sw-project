<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:param name="userPos" select="1"/>
<xsl:variable name="safePos" select="number($userPos)"/>

<xsl:template match="/appData">
    <html>
    <head>
        <title>Recipes XSLT View</title>
        <style>
            body { font-family: Arial, sans-serif; background: #f4f4f9; padding: 20px; }
            table {
                border-collapse: collapse;
                width: 100%;
                background-color: white;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            }
            th, td {
                border: 1px solid #ddd;
                padding: 12px;
            }
            th {
                background-color: #007bff;
                color: white;
                text-align: left;
            }
            a.btn { display: inline-block; padding: 10px 15px; margin-bottom: 20px; background: #6c757d; color: white; text-decoration: none; border-radius: 4px; }
            a.btn:hover { background: #5a6268; }
        </style>
    </head>
    <body>
        <h2>Recipes View (XSLT)</h2>
        <a href="/" class="btn">Back to Home</a>
        <br/><br/>

        <table>
            <thead>
            <tr>
                <th>Title</th>
                <th>Cuisine 1</th>
                <th>Cuisine 2</th>
                <th>Difficulty</th>
            </tr>
            </thead>
            <tbody>
            <xsl:variable name="userSkill" select="/appData/users/user[number($userPos)]/skillLevel"/>
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
            </tbody>
        </table>
    </body>
    </html>
</xsl:template>

</xsl:stylesheet>
