---
title: SQL查询对不同引擎的支持
---

　　SQL查询功能通过对数据集的属性进行限定，从已有的数据中查询出满足特定条件的数据（记录数的子集，属性字段的子集，相关的统计等）。对于不同引擎的数据源，不同函数的适用情况及函数用法有所不同，在此给出SQL查询功能对不同引擎支持情况的列表。

### 聚合函数

 函数名称 | UDB | Oracle Plus | SQL Plus | PostgreSQL | DB2
 :------ | :---: | :----: | :----: | :------: | :----: 
 Avg | √ | √ | √ | √ | √
 Count | √ | √ | √ | √ | √
 Max | √ | √ | √ | √ | √
 Min | √ | √ | √ | √ | √
 Sum | √ | √ | √ | √ | √
 Stdev | √ | √ | √ | √ | √
 Stdevp | √ | √ | √ | √ |
 Var | √ | √ | √ | √ | √
 Varp |   | √ | √ | √ | √

### 数学函数

函数名称 | UDB | Oracle Plus | SQL Plus | PostgreSQL | DB2
 :----: | :---: | :---: | :---: | :---: | :---: 
 Abs | √ | √ | √ | √ | √
 Acos |   | √ | √ | √ | √
 Asin |   | √ | √ | √ | √
 Atan |   | √ | √ | √ | √ 
 Ceiling |   | √ | √ | √ | √
 Cos | √ | √ | √ | √ | √ 
 Sin | √ | √ | √ | √ | √ 
 Cot |   |   | √ | √ | √
 Tan |   | √ | √ | √ | √ 
 Degrees |   |   | √ | √ | √
 Exp |   | √ | √ | √ | √ 
 Floor |   | √ | √ | √ | √
 Log |   | √(Ln) | √ | √(Ln) | √ 
 Log10 |   | √(Log(10,m)) | √ | √(Log) | √
 PI |   |   | √ | √ |  
 Power |   | √ |   | √ |  
 Radians |   |   | √ | √ | √
 Rand |   |   | √ |   | √
 Round | √ | √ |   | √(不可指定小数位数) | √(不可指定小数位数)
 Sign |   | √ | √ | √ | √
 Square |   |   | √ | √ | √
 Sqrt |   | √ | √ | √ | √
 Fix |   |   |   |   | √

### 字符串函数

 函数名称 | UDB | Oracle Plus | SQL Plus | PostgreSQL | DB2
 :----: | :---: | :---: | :---: | :---: | :---: 
 Ascii |   | √ | √ |   | √
 Char |   | √(Chr) | √ | √(Chr) | √(Chr)
 Length | √ | √ | √(Len) | √ | √（字节个数）
 Lower | √ | √ | √ | √ | √
 Ltrim | √ | √ | √ | √ | √
 Replace | √ |   |   |   |  
 Reverse |   | √(中文字符乱码) | √ |   |  
 Rtrim | √ | √ | √ | √ | √
 Soundex |   | √ | √ |   | √
 Space |   |   | √ |   |  
 Substr | √ | √(文本类型不适用) |   | √ |  
 Unicode |   |   | √ |   |  
 Upper | √ | √ | √ | √ | √

### 日期函数

 函数名称 | UDB | Oracle Plus | SQL Plus | PostgreSQL | DB2
 :----: | :---: | :---: | :---: | :---: | :---: 
 Day |   |   | √ |   | √
 GetDate |   |   | √ |   |  
 GetUtcDate |   |   | √ |   |  
 Month |   |   | √ |   | √
 Year |   |   | √ |   | √

### 符号表达

　　SuperMap支持的文件类型和数据库类型的引擎对相同含义的表达方式有所不同，如下表所示：

 字符连接符 | UDB | Oracle Plus | SQL Plus | PostgreSQL | DB2
 :----: | :---: | :---: | :---: | :---: | :---: 
 字符连接符 | &#124;&#124; | &#124;&#124; | + | &#124;&#124; | &#124;&#124;
 取模 | a % b | Mod(a,b) | a % b | a % b | Mod(a,b)
 Loge(m) |   | Ln | Log | Ln | Log/Ln
 Log10(m) |   | Log(10，m) | Log10 | Log | Log10
 Logn(m) |   | Log(n,m) |   | Log(n,m) |  
