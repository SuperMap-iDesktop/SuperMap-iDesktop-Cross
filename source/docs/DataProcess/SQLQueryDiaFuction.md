---
title: SQL语句运算符及函数
---


### 运算符号

运算符号用于构造 SQL 查询条件，包括常用的数学运算符、逻辑运算符和一些比较特殊的运算符。

**算术运算符**

 运算符           | 含义             |  示例           
 :-------------- | :--------------- | :---------------
 ＋ | 加法 | RENT + UTILITIES &lt;= 800
 - | 减法 | POP\_2002 - POP\_1994
 \* | 乘法 | DENSITY \* AREA &gt; 5000000
 / | 除法 | VALUE / POP = 50000
 \\ | 取商 | VALUE \\　POP &gt; 100
 % | 取模 | VALUE1 % VALUE2
 Mod | 取模 | VALUE1 Mod VALUE2

**比较运算符**

 运算符           | 含义             |  示例           
 :-------------- | :--------------- | :---------------
 = | 等于 | CODE = 100
 &gt; | 大于 | POP\_2002 &gt; 5000000
 &lt; | 小于 | INDUST\_GROWTH &lt; 0
 &gt;= | 大于等于 | RENT + UTILITIES &gt;= 800
 &lt;= | 小于等于 | RENT + UTILITIES &lt;= 800
 &lt;&gt; | 不等于 | VALUE &lt;&gt; 100
 ! | 非，!&lt;(不小于)、!&gt;（不大于） | VALUE !&gt; 100

**逻辑运算符**

 运算符           | 含义             |  示例           
 :-------------- | :--------------- | :---------------
 AND | 连结两个布尔型表达式并当两个表达式都为 TRUE 时返回 TRUE | CODE = 100 AND VALUE &gt; 20000
 NOT | 对任何其它布尔运算符的值取反 | NOT IsBACHELOR
 OR | 将两个条件结合起来，如果两个布尔表达式中的一个为 TRUE，那么就为 TRUE | SALES &gt; 20000 OR ORDERS &gt; 20000
 IN | 如果操作数等于表达式列表中的一个，那么就为 TRUE | PROVINCE In ("GUANGDONG")
 Between | 确定一个表达式在某个范围之内，那么就为 TRUE。一般与 AND 同时使用 | SALES Between 4095 AND 20000
 Like | 确定给定的字符串完全与指定的模式匹配 | COUNTRY Like "CANADA"

**其他**

 运算符           | 含义             |  示例           
 :-------------- | :--------------- | :---------------
 Is NULL | 确定一个表达式是为 NULL，主要是文本型字段 | CONTINENT Is NULL
 Is TRUE | 确定一个表达式为 True，主要是布尔型字段 | Value &lt; 0 Is TRUE
 Is FALSE | 确定一个表达式为 False，主要是布尔型字段 | Value &gt; 0 Is FALSE
 \# | 匹配任意单个数字字符 | World.SmID like '1\#3'
 ´ | 用于字符型字段、日期型字段等的输入 | World.COUNTRY like '\*国'
 ˆ | 异或 | World.SmID \^ World.COLOR\_MAP &gt;0
 · | 用于手动输入字段信息时使用 | World.COUNTRY
 \* | 匹配任意数量的字符。可以在字符串中的任意位置使用星号 (\*) | World.COUNTRY like '\*国'

### 聚合函数

聚合函数对一组值执行计算并得到单一的值。除 COUNT 函数之外，聚合函数忽略空值。聚合函数经常与 SELECT 语句的 GROUP BY 子句一同使用。所有聚合函数都具有确定性。任何时候用一组给定的输入值调用它们时，都得到相同的值。除 COUNT 函数外，其它聚合函数均不能对文本数据类型使用。在 SuperMap 中聚合函数中的表达式一般采用属性表的字段名。

 函数 | 中文名称 | 含义 | 示例
 :------ | :--------- | :---------------| :---------------
 Avg | 平均值（均值） | 返回在查询的指定字段（或表达式）中包含的一组值的算术平均值，空值将被忽略。 | Avg(World.POP\_1994)
 Count | 计数 | 返回各组中的记录数。 | Count(\*)
 Max | 最大值 | 返回在查询的指定字段内所包含的一组值的最大值。 | Max(World.POP\_1994)
 Min | 最小值 | 返回在查询的指定字段内所包含的一组值的最小值。 | Min(World.POP\_1994)
 Sum | 求和 | 返回在查询的指定字段中所包含的一组值的总计。 | Sum(World.POP\_1994)
 Stdev | 样本标准差 | 返回以查询的指定字段中包含的一组值作为总体样本抽样的标准偏差的估计值。对于Orcale 数据源，其函数名称为 STDDEV。 | Stdev(World.POP\_1994)
 Var | 样本方差 | 返回以查询的指定字段中包含的一组值作为总体样本抽样的方差的估计值。对于 Orcale 数据源，其函数名称为 VARIANCE。 | Var(World.POP\_1994)

### 数学函数

数学函数对数字表达式进行数学运算并返回运算结果。数学函数在 SuperMap 中的表达式一般采用属性表的字段名或其表达式，如 Cos(Slope）、Ceiling(Pop/Area）等。

 函数 | 含义 | 示例
 :------ | :--------- | :---------------| :---------------
 Abs | 返回指定数字的绝对值，适用于短整型、长整型、单精度、双精度、货币等类型字段 | Abs(World.SmID)
 Acos | 返回以弧度表示的角度值，取值范围从-π到π，该角度值的余弦为给定的 float 表达式；本函数亦称反余弦，适用于单精度或双精度类型字段，表达式的取值范围从-1到1，否则查询结果为空。 | Acos(data)
 Asin | 返回以弧度表示的角度值，取值范围从-π到π，该角度值的正弦为给定的 float 表达式；本函数亦称反正弦，适用于单精度或双精度类型字段，表达式的取值范围从-1到1，否则查询结果为空。 | Asin(data)
 Atan | 返回以弧度表示的角度值，该角度值的正切为给定的 float 表达式；本函数亦称反正切。 | Atan(data)
 Ceiling | 返回大于或等于所给数字表达式的最小整数。适用于短整型、长整型、单精度、双精度、货币等类型字段。对于 Oracle 数据源，其函数名称为 CEIL。 | Ceiling(data)
 Cos | 返回给定表达式中给定角度的三角余弦值，类型为双精度。 | Cos(World.COLOR\_MAP)
 cot | 返回给定 float 表达式中指定角度的三角余切值。 | Cot(angle)
 Degrees | 当给出以弧度为单位的角度时，返回相应的以度数为单位的角度。 | Degrees(angle)
 Exp | 返回所给的 float 表达式的以 e（约等于2.71828182845905）为底的指数值。 | Exp(data)
 Floor | 返回小于或等于所给数学表达式的最大整数。 | Floor(23.45)，Floor(-123.45)
 Log | 返回所给数学表达式的自然对数，不适合 UDB 数据源使用；此外，对于 Oracle数据源，函数格式为 Log(n,m)，表示以 n 为底，m 的对数。 | Log(World.SmID)，Log(n,m)
 Power | 返回给定表达式乘指定次方的值，第一个参数指定数值型字段，第二个参数指定次方数。 | Power(expression,2) as Area
 Radians | 对于在数字表达式中输入的度数值返回弧度值，并对弧度值进行了向下取整操作。 | Radians(angle) as NewAngle
 Rand | 返回 0 到1 之间的随机 float 值，函数输入为空。 | Rand()
 Round | 返回指定数值型字段的四舍五入到指定的小数位数的数字。第一个参数指定数值型字段），第二个参数指定小数位数。如果没有指定小数位数时，默认四舍五入为最接近的整数。 | Round(World.SmArea,2)，Round(World.SmArea)
 Sign | 返回给定表达式的正负信息，包括正 (标记为+1)、零 (标记为0) 或负 (标记为-1) 号。 | Sign(data)
 Sin | 返回给定角度（以弧度为单位）的三角正弦值，类型为双精度。 | Sin(World.COLOR\_MAP)
 Square | 返回给定表达式的平方。 | Square(Production)
 Sqrt | 返回给定表达式的平方根。 | Sqrt(Production)
 Tan | 返回输入表达式的正切值。 | Tan(World.COLOR\_MAP)

### 字符函数

字符串函数用于对字符和二进制字符串进行各种操作，它们返回对字符数据进行操作时通常所需要的值。

 函数 | 含义 | 示例
 :------ | :--------- | :---------------| :---------------
 Ascii | 返回字符表达式最左端字符的 ASCII 代码值。 格式：ASCII（character\_expression），character\_expression 为文本类型字段组成的表达式。 | Ascii(String)
 Char | 将 int 型表达式的值由 ASCII 代码转换为字符型的字符串。<br />格式：Char(integer\_expression） integer\_expression 为0～255之间整数表达式。如果整数表达式不在此范围内，将返回 NULL 值。 | Char(data)
 InStr | 返回字符串中指定表达式的起始位置。格式：InSt(\[start\_location\],expr1,expr2）start\_location 是在 expr1中搜索expr2时的起始字符位置，expr1为一个字符串型表达式，其中包含要寻找的字符，expr2为一个字段串型表达式，标识要搜索的指定序列。起始字符位置不进行设置时，默认从第一个字符开始搜索。 | InStr(1,World.CAPITAL,"京")，InStr(World.CAPITAL,"京")
 Length | 返回给定字符串表达式的长度（字符个数），空格计算在内。如果字段为空，则返回值为空。格式：Length(string\_expression) string\_expression 为要计算的字符串表达式。对 SQL 数据源，函数为 Len(string\_expression）。 | Lenth(World.CAPITAL)
 Lower | 将大写字符数据转换为小写字符数据后返回字符表达式。格式：Lower（character\_expression） character\_expression 是字符类型表达式。 | Lower(World.CAPITAL) as capital
 Ltrim | 删除指定字段串表达式的起始空格，返回无起始空格的字符串。格式：Ltrim（character\_expression） character\_exprssion 是字符类型表达式。 | Ltrim(World.CAPITAL)
 Replace | 用第三个表达式替换第一个字符串表达式中出现的所有第二个给定字符串表达式。格式：Replace('string\_expr1','string\_expr2','string\_expr3') string\_expr1是待搜索的字符串表达式；string\_expr2是待查找的字符串表达式；string\_expr3是替换用的字符串表达式。 | Replace(World.CAPITAL,"尔","而")
 Reverse | 返回字符表达式的反转。对 Oracle 数据源查询时，如果数据类型为文本型，对中文字符不处理，如果数据类型为字符型，对中文字符处理后会出现乱码。格式：Reverse(character\_expression) character\_expression 是由字符数据组成的表达式。 | Reverse(World.CAPITAL) as NewName
 Rtrim | 去除字符型表达式的所有尾随空格，返回无尾随空格的字符串。格式：Rtrim(character\_expression) character\_expression 是由字符数据组成的表达式 | Rtrim(World.CAPITAL)
 Soundex | 返回由四个字符组成的代码 (SOUNDEX) 以评估两个字符串的相似性。格式：Soundex（character\_expression） character\_expression是字符数据的字母数字表达式 | Soundex(word)
 Space | 生成由指定个数的空格组成的字符串。如果指定个数为负值，则返回空字符串。 | World.COUNTRY+Space(3)+World.CAPITAL as World\_CAPITAL
 Substr | 返回文本字符表达式的一部分。格式：Substr（expression, start, length）。expression 是字符串类型的不包含聚合函数的表达式；start 是一个整数，指定子串的开始位置；length是一个整数，指定子串的长度（要返回的字符数或字节数）。 | Substr ( string, position, 1 )
 Unicode | 按照 Unicode 标准的定义，返回输入表达式的第一个字符的整数值。格式：Unicode('ncharacter\_expression')。 | Unicode(World.CAPITAL) as Unicode
 Upper | 返回将小写字符数据转换为大写的字符表达式。对于 SDB 数据源，其函数名称为 UCase。格式：Upper(character\_expression) character\_expression 是由字符数据组成的表达式。 | Upper(World.CAPITAL)

### 日期函数

日期函数用来查询关于日期的信息。这些函数适用于日期类型字段值，并对这些值执行算术运算。可将日期函数用于可使用表达式的任何地方。

**表格：日期函数**

 日期函数 | 含义 | 示例
 :------ | :--------- | :---------------| :---------------
 DateAdd | 在给指定日期的基础上加上一段时间得到新的日期值。 格式：DateAdd（datepart, number, date） datepart 参数指定向日期哪一部分进行更新，详见下表；number 用来增加 datepart 的值；date 为日期型字段（或表达式）。 | DateAdd("yyyy",2,World.GETDATE) as EndDate
 DateDiff | 返回两个指定日期的时间差。格式：DateDiff（datepart，startdate，enddate） datepart 参数指定对日期哪一部分进行差值计算，详见下表；startdate 是开始日期，enddate 是终止日期。如果开始日期（如1990年）早于终止日期（如1994年），则结果为正，否则为负。 | DateDiff ( "yyyy", Start, End ) as DiffMonth，Datediff('yyyy','2010-02-25',World.GETDATE)
 DatePart | 返回指定日期的指定部分的整数。格式：DatePart（datepart ，date） datepart 参数标记了应返回日期的哪一部分，详见下表；date 为日期型字段（或表达式）。 | Datepart('d',World.GETDATE) as day
 Day | 返回指定日期天部分的整数，等价于 Datepart('d',data)。格式：Day(date) date 为日期型字段（或表达式）。 | Day(World.GETDATE) as Day
 GetDate | 按 SQL 标准格式返回当前系统的日期和时间，函数输入为空。 | GetDate()
 GetUtcDate | 返回表示当前 UTC 时间（格林尼治标准时间）的日期值，函数输入为空。当前的 UTC 时间得自当前的本地时间和运行 SQL Server 的计算机操作系统中的时区设置。 | GetUtcDate()
 Month | 返回指定日期月份部分的整数，等价于 DatePart("m",date)。适用日期型字段，格式：Month（date），date 为日期型字段。 | Month(World.GETDATE) as Month
 Year | 返回指定日期年份部分的整数。等价于 DatePartdate）。适用于日期型字段，格式：Year(date）date 为日期型字段。 | Year(World.GETDATE) as Year

**表格：datepart 参数设置**

 设置 | 说明
 :--- | :------
 yyyy | 年
 q | 季度
 m | 月
 y | 某年的某一天
 d | 天
 w | 工作日
 ww | 周
 h | 时
 n | 分
 s | 秒



