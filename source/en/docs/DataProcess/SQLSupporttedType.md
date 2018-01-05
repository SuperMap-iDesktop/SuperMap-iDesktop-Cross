---
title: Engines Supported by SQL Queries
---

　　SQL query allows you to select the data that meets the specific criteria from existing data through a written statement or logical expression. For datasources of different engines, supported functions are different.

### Aggregation Functions

 Function | UDB | Oracle Plus | SQL Plus | PostgreSQL | DB2
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

### Math Functions

Function | UDB | Oracle Plus | SQL Plus | PostgreSQL | DB2
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
 Round | √ | √ |   | √(Specifing a decimal number is not allowed) | √(Specifing a decimal number is not allowed)
 Sign |   | √ | √ | √ | √
 Square |   |   | √ | √ | √
 Sqrt |   | √ | √ | √ | √
 Fix |   |   |   |   | √

### String Functions

 Function | UDB | Oracle Plus | SQL Plus | PostgreSQL | DB2
 :----: | :---: | :---: | :---: | :---: | :---: 
 Ascii |   | √ | √ |   | √
 Char |   | √(Chr) | √ | √(Chr) | √(Chr)
 Length | √ | √ | √(Len) | √ | √ (the number of bytes)
 Lower | √ | √ | √ | √ | √
 Ltrim | √ | √ | √ | √ | √
 Replace | √ |   |   |   |  
 Reverse |   | √ | √ |   |  
 Rtrim | √ | √ | √ | √ | √
 Soundex |   | √ | √ |   | √
 Space |   |   | √ |   |  
 Substr | √ | √(doesn't apply to the type text) |   | √ |  
 Unicode |   |   | √ |   |  
 Upper | √ | √ | √ | √ | √

### Date Functions

 Function | UDB | Oracle Plus | SQL Plus | PostgreSQL | DB2
 :----: | :---: | :---: | :---: | :---: | :---: 
 Day |   |   | √ |   | √
 GetDate |   |   | √ |   |  
 GetUtcDate |   |   | √ |   |  
 Month |   |   | √ |   | √
 Year |   |   | √ |   | √

### Operators Expression

　　For the same operation, the expressions between the file engines and database engines supported by SuperMap are different. See the table for details.

 Operator | UDB | Oracle Plus | SQL Plus | PostgreSQL | DB2
 :----: | :---: | :---: | :---: | :---: | :---: 
 concatenation operator | &#124;&#124; | &#124;&#124; | + | &#124;&#124; | &#124;&#124;
 modulo | a % b | Mod(a,b) | a % b | a % b | Mod(a,b)
 Loge(m) |   | Ln | Log | Ln | Log/Ln
 Log10(m) |   | Log(10，m) | Log10 | Log | Log10
 Logn(m) |   | Log(n,m) |   | Log(n,m) |  
