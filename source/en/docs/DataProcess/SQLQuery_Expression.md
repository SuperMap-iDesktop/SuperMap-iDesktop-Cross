---
title: Constructing SQL Query Statements
---

　　Structured Query Language (SQL) is a standard computer language for accessing and managing databases. SuperMap uses SQL to build SQL expressions for data query. Generally, the syntax of the SQL expression is select Ouput_Field from Dataset_Name where Query_Condition (order by Order_Field ascending/descending). The Ouput_Field, Dataset_Name and Order_Field can be directly selected from the list or drop-down list in the SQL Query dialog box, while the where clause needs to be built by us. In this section, some commonly used query expressions are introduced.

　　Please note that the wildcards may be different when building SQL expressions for file and database datasources because the attribute information of the file datasources are stored in Access format.

### Numeric query

   The numeric query can make use of the operators like ＝, &lt;&gt;, &gt;, &lt;, &lt;=, &gt;=, Between...and.

   For example:

    World.Pop_1994 Between 10000000 and 20000000

   Queries the countries with the population between 10000000 and 20000000 in 1994.

### Fuzzy query

   The fuzzy query uses the keyword like. The wildcards for different types of data sources are different.

   For example:

   1 Partial match employs the wildcard of "\*" (**Note: The wildcards for database and UDB datasources are '%'**).

    World.CAPITAL like "a*"，World.CAPITAL like 'a%'

   Query the countries with their names begin with a;

   2 Fully match (**Database datasources can only use the single quotation marks, UDB datasources can use both single and double quotation mark**).

    World.CAPITAL like "Beijing"，World.CAPITAL like 'Beijing'

   Query the country with the capital is Beijing;.

   3 Single character match employs the wildcard of "\_" (**The wildcards for database and UDB datasources are \_ **).

    World.CAPITAL like 'A_'

   Query the records where the ISO_2 field value is only two-character and must start with A in World_World dataset.

### Query specified value

   The keyword "in" is used to determine whether the value of the expression is equivalent to any value in the specified list.

   For example:

    World_World.ISO_2 in ('AD','BS','PH')

   Queries the countries with the values of AD, BS and PH in the ISO_2 field.

    World.SmID in (1,4)

   Queries the countries with the SmID values is 1 and 4 in the World dataset.

　　 **Note**: For character fields, the query values must be enclosed by a pair of single quotation marks or double quotation marks. While the numeric values don't need quotation marks. For database datasources, the query values can only be bracketed with a pair of single quotation marks.

### Query null value

   is NULL (is not NULL)

   For example:

    World.CAPITAL Is NULL

   Queries the records with null value in the CAPITAL field. (It is likely that some countries haven't been specified the capital name.)

### Query by building expressions

   For example:

    World.SmArea > World.SmPerimeter * 50000

   Queries the countries with the area greater than 50,000 times of the perimeter.

   The brackets can be used to arrange the priority. For example: 

    World.POP_1994 / (World.SmArea/1000000) > 500

   Queries the countries from World where the population per square kilometer is greater than 500 in 1994.

### Query by combining expressions

   The keyword "and" is used to combine two or more expressions.

   For example:

    World.SmArea > 5000000000000 AND World.POP_1994 < 100000000

   Queries the countries with the area greater than 5,000,000 square kilometers and the population less than 100 million in 1994.

### Apply comparison operators in string query

   The comparison operators include &gt;, &lt;, &gt;=, &lt;=, &lt;&gt;

   For example:

    World.Country >= "N"

   Queries the countries with the name starting from N to Z in the World dataset. For database datasources, the query values can only be bracketed with a pair of single quotation marks.

### Query date values

   When querying a date field, use the following:

    World.T_date = #1994-08-10 12:25:00#，World.T_date like #1994-08-10 %#

    **Note:**

   - For the SQL(SQL+) datasource, the query statement is World.T\_date = '1994-08-10 12:25:00';
   - For the ORACLE datasource, the query statement is World.T\_date = TO\_DATE('1994-08-10', 'YYYY-MM-DD');
   - For the UDB datasource, the query statement is World.T\_date = to\_date(1994-08-10 12:25:00)

   Queries all records whose values of the T\_date field are August 10, 1994, AM 12:25:00 in the World dataset.

    World.T_date Between #2010-01-01 0:0:0# and #2010-12-31 0:0:0#

   Queries all records in 2010.

### Boolean Field Query

   Boolean field value is either True or False. For SQL query, 1 represents True and 0 represents False.

   For example:

    City_R.Coastal = 1

   Queries cities along the sea in City\_R dataset.

### Temporary field

   For example:

    Pop_1994 / SmArea

   The temporary filed will be listed in the attribute table of the query results. You can also specify an alias for the temporary field, as shown below.

    Pop_1994/ SmArea as Pop_Density

   The alias is displayed on column heading in the attribute table to mark the corresponding field. The expression will be used if you don't specify an alias for a temporary field. Use the comma to separate temporary fields if you want to define many.

### ![](../img/note.png)Notes

When querying the Double, and using "=", you may not get the result because of the precision. It isn't recommended for users to use "=".


