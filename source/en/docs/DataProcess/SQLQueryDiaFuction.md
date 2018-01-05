---
title: The Operators and Functions in SQL
---


### Operators

The operators are used to onstruct SQL expression. Including commonly mathematical operators, logical operators and some special operators.

**Mathematical operators**

 Operators           | Description             |  Example           
 :-------------- | :--------------- | :---------------
 ＋ | Addition  | RENT + UTILITIES &lt;= 800
 - | Subtraction | POP\_2002 - POP\_1994
 \* | Multiplication | DENSITY \* AREA &gt; 5000000
 / | Division | VALUE / POP = 50000
 \\ | Quotient | VALUE \\　POP &gt; 100
 % | Modulus | VALUE1 % VALUE2
 Mod | Modulus | VALUE1 Mod VALUE2

**Comparison**

 Operators           | Description             |  Example           
 :-------------- | :--------------- | :---------------
 = | Equal to | CODE = 100
 &gt; | Greater than | POP\_2002 &gt; 5000000
 &lt; | Less than | INDUST\_GROWTH &lt; 0
 &gt;= | Greater than or equal | RENT + UTILITIES &gt;= 800
 &lt;= | Less than or equal | RENT + UTILITIES &lt;= 800
 &lt;&gt; | Not equal to | VALUE &lt;&gt; 100
 ! | Logical NOT,!&lt;(not less than)、!&gt;(not greater than) | VALUE !&gt; 100

**Logical**

 Operators           | Description             |  Example           
 :-------------- | :--------------- | :---------------
 AND | Combines two Boolean expressions and returns TRUE if both expressions are TRUE. | CODE = 100 AND VALUE &gt; 20000
 NOT | Negates a Boolean value. | NOT IsBACHELOR
 OR | Combines two Boolean expressions and returns TRUE if at least one expression is TRUE. | SALES &gt; 20000 OR ORDERS &gt; 20000
 IN | Determines if the operand matches a certain value in the expression list. | PROVINCE In ("GUANGDONG")
 Between | Determines whether an expression is within a certain range. It is often used together with AND. | SALES Between 4095 AND 20000
 Like | Determines whether the given string matches wholly a specified pattern. | COUNTRY Like "CANADA"

**Others**

 Operators           | Description             |  Example           
 :-------------- | :--------------- | :---------------
 Is NULL | Determines whether or not a given expression is NULL | CONTINENT Is NULL
 Is TRUE | Determines whether or not a given expression is TRUE. | Value &lt; 0 Is TRUE
 Is FALSE | Determines whether or not a given expression is FALSE. | Value &gt; 0 Is FALSE
 \# | Matches a single character of any value. | World.SmID like '1\#3'
 ´ | Used for the input of char or date fields. | World.COUNTRY like '\*Country'
 ˆ | Logical Exclusive XOR | World.SmID \^ World.COLOR\_MAP &gt;0
 · | Used for specifying a field of a dataset | World.COUNTRY
 \* | Matches multiple characters. You can insert the asterisk (\*) at any position in a string. | World.COUNTRY like '\*Country'

### Aggregate Functions

Aggregate functions perform a calculation on a set of values and return a single value. Except for the function COUNT, aggregate functions ignore null values. Aggregate functions are often used with the GROUP BY clause of the SELECT statement. All aggregate functions are deterministic; they return the same value any time they are called with a given set of input values. Except for Count, aggregate functions can not be applied to data in text type. The expressions of aggregate functions can be the names of fields of datasets in SuperMap.	

 Functions | Name | Description | Example
 :------ | :--------- | :---------------| :---------------
 Avg | Average | Returns the average of the values in a group. Null values are ignored.| Avg(World.POP\_1994)
 Count | Count | Returns the total number of items in a group. | Count(\*)
 Max | MaxValue | Returns the maximum value in a set of values. | Max(World.POP\_1994)
 Min | MinValue | Returns the minimum in a set of values. | Min(World.POP\_1994)
 Sum | Sum | Returns the sum of a set of values.| Sum(World.POP\_1994)
 Stdev | Standard Deviation | Returns the statistical standard deviation of all values in the given expression. For Oracle datasources, the function is STDDEV. | Stdev(World.POP\_1994)
 Var | Variance | Returns the statistical variance of all values in the given expression. For Oracle datasources, the function is VARIANCE. | Var(World.POP\_1994)

### Mathematical Functions

Mathematical functions perform operations on mathematical expression and return the results. The expressions of mathematical functions are often composed of fields of datasets or the expressions composed by these types of fields, such as Cos(Slope), Ceiling(Pop/Area), etc.

 Functions | Description | Example
 :------ | :--------- | :---------------| :---------------
 Abs | Returns the absolute value of the expression. Applicable to fields (or expressions) in Short, Long, Single, or Double type. | Abs(World.SmID)
 Acos | Returns the arccosine of the expression as an angle in radians ranging from -p to p. Applicable to fields or expressions in Single or Double type. The value of the expression ranges from -1 to 1. | Acos(data)
 Asin | Returns the arcsine of the expression as an angle in radians ranging from -p to p. Applicable to fields or expressions in Single or Double type. The value of the expression ranges from -1 to 1. | Asin(data)
 Atan | Returns the arctangent of the expression as an angle in radians. For SDB datasources, the function is Atn. | Atan(data)
 Ceiling | Returns the smallest integer greater than or equal to the expression. Applicable to fields (or expressions) in Short, Long, Single, or Double type. For Oracle datasources, the function is CEIL. | Ceiling(data)
 Cos | Returns the cosine of the expression as an angle in radians. | Cos(World.COLOR\_MAP)
 cot | Returns the cotangent of the expression as an angle in radians. | Cot(angle)
 Degrees | Converts from radians to degrees. | Degrees(angle)
 Exp | Returns a Double value containing e (the base of natural logarithms) raised to the specified power. | Exp(data)
 Floor | Returns the largest number that is less than or equal to the specified number. | Floor(23.45)，Floor(-123.45)
 Log | Returns the natural logarithm of the specified expression. Not applicable to UDB datasources. For Oracle datasources, the function is Log(n, m), representing the logarithm of m with base n. | Log(World.SmID)，Log(n,m)
 Power | Returns the value of parameter 1 to the power of parameter 2. Not applicable to SDB datasources. | Power(expression,2) as Area
 Radians | Converts degrees to radians. | Radians(angle) as NewAngle
 Rand | Returns a float value less than 1, but greater than or equal to zero. | Rand()
 Round | Returns a number rounded to a specified number of decimal places. Parameter 1 and 2 respectively specify the number to round and the decimal places. The number nearest the specified value will be returned if you didn't specify the decimal places. | Round(World.SmArea,2)，Round(World.SmArea)
 Sign | Returns -1, 0, or +1 when the expression value is negative, zero, or positive respectively. | Sign(data)
 Sin | Returns the sine of the expression value as an angle in radians. | Sin(World.COLOR\_MAP)
 Square | Returns the square of the expression value. | Square(Production)
 Sqrt | Returns the square root of the expression value. | Sqrt(Production)
 Tan | Returns the tangent of the expression value. | Tan(World.COLOR\_MAP)

### String Functions

String functions are used to perform many types of manipulation on string data.

 Functions | Description | Example
 :------ | :--------- | :---------------| :---------------
 Ascii | Returns the ASCII code value of the leftmost character of a character expression. Syntax: ASCII( character_expression) character_expression is an expression of the type text. | Ascii(String)
 Char | Converts an int ASCII to a character. For SDB datasources, the function is Chr. Syntax: Char( integer_expression) integer_expression is an integer expression with value ranging from 0 to 255. NULL will be returned if the integer expression is not in this range.| Char(data)
 InStr | Returns the location of a substring in a string. Syntax: InSt([start_location],expr1,expr2) expr1 is the string to search. expr2 is the substring to search for in expr1. start_location is the position in string1 where the search will start. This argument is optional. If omitted, it defaults to 1. | InStr(1,World.CAPITAL,"Jing")，InStr(World.CAPITAL,"Jing")
 Length | Returns the length of the specified string. If the specified string1 is NULL, then the function returns NULL. Syntax: Length(string_expression) string_expression is the string to return the length for. For SDB and SQL datasources, the function is Len(string_expression). | Lenth(World.CAPITAL)
 Lower | LowerConverts all letters in the specified string to lowercase. Syntax: Lower(character_expression) character_expression is the string to convert to lowercase. | Lower(World.CAPITAL) as capital
 Ltrim | Returns a string containing a copy of a specified string with no leading spaces. Syntax: Ltrim(character_expression) character_exprssion is the string to trim. | Ltrim(World.CAPITAL)
 Replace | Replaces a sequence of characters in a string with another set of characters. Syntax: Replace('string_expr1','string_expr2','string_expr3') string_expr1 is the string to replace a sequence of characters with another set of characters. string_expr2 is the string that will be searched for in string_expr1. string_expr3 is the string to replace string_expr2. | Replace(World.CAPITAL,"liu","lia")
 Reverse | Returns string converted in the reverse order. Syntax: Reverse( character_expression) character_expression is the string to reverse. | Reverse(World.CAPITAL) as NewName
 Rtrim | Returns a string containing a copy of a specified string with no trailing spaces. Syntax: Rtrim(character_expression) character_expression is any valid string expression. | Rtrim(World.CAPITAL)
 Soundex | Returns a phonetic representation (the way it sounds) of a string. Syntax: Soundex(character_expression) character_expression the string whose phonetic value will be returned. | Soundex(word)
 Space | Returns a string consisting of the specified number of spaces. If it is negative, NULL will be returned. | World.COUNTRY+Space(3)+World.CAPITAL as World\_CAPITAL
 Substr | Returns a part of text expression. Syntax: Substr(expression, start, length). expression do not contain the aggregate function; start is an integer, pointing the start position of substring; length is an integer, pointing the length of substring. | Substr ( string, position, 1 )
 Unicode | Returns an integer containing the Unicode code point of the first character in the string, or NULL if the first character is not a valid encoding. Syntax: Unicode('ncharacter_expression') ncharacter_expression is the string whose first character is to be converted to an integer. | Unicode(World.CAPITAL) as Unicode
 Upper | Converts all letters in the specified string to uppercase. For SDB datasources, the function is LCase. Syntax: Upper(character_expression) character_expression is any valid string expression. | Upper(World.CAPITAL)

### Date Functions

Date functions are used to get information on date and time. These scalar functions perform an operation on a date and time input value and return a string, numeric, or date and time value. The date functions can be used anywhere in the expression

**Tab. Date Functions**

 Date Functions | Description | Example
 :------ | :--------- | :---------------| :---------------
 DateAdd | Returns a new date value based on adding an interval to the specified date. Syntax: DateAdd( datepart, number, date) datepart is the parameter that specifies on which part of the date to return a new value. number is the value used to increment datepart. date is an expression that returns a date or smalldatetime value, or a character string in date type. | DateAdd("yyyy",2,World.GETDATE) as EndDate
 DateDiff | Returns the number of date and time boundaries crossed between two specified dates. Syntax: DateDiff( datepart,startdate,enddate) datepart is the parameter that specifies on which part of the date to calculate the difference. startdate is the beginning date. enddate is the ending date. The result would be negative if the enddate is earlier than the startdate. | DateDiff ( "yyyy", Start, End ) as DiffMonth，Datediff('yyyy','2010-02-25',World.GETDATE)
 DatePart | Returns an integer representing the specified datepart of the specified date. Syntax: DatePart( datepart ,date)) datepart is the parameter that specifies the part of the date to return. date is a filed or expression of date type. | Datepart('d',World.GETDATE) as day
 Day | Returns an integer representing the day datepart of the specified date. Syntax: DatePart( datepart ,date)) date is an expression of type date. | Day(World.GETDATE) as Day
 GetDate | Gets current date and time in standard SQL Syntax. | GetDate()
 GetUtcDate | Returns the date value representing the current UTC time (Universal Time Coordinate or Greenwich Mean Time). The current UTC time is derived from the current local time and the time zone setting in the operating system of the computer on which SQL Server is running. | GetUtcDate()
 Month | Returns an integer that represents the month part of a specified date. It equals to DatePart( "m" , date). Applicable to date fields. Syntax: Month( date) date is a field or expression of date type. | Month(World.GETDATE) as Month
 Year | Returns an integer that represents the year part of a specified date. It equals to DatePartdate. Applicable to date fields. Syntax: Year(date) date is a date field. | Year(World.GETDATE) as Year

**Tab: datepart values**

 Value | Description
 :--- | :------
 yyyy | Year
 q | Quarter
 m | Month
 y | Day of a year
 d | Day
 w | Weekday
 ww | Week
 h | Hour
 n | Minute
 s | Second



