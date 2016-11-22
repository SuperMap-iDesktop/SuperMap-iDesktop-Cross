title: SuperMap iDesktop Cross 常见问题解答
---
**Q1： 程序在运行过程中的信息会输出到日志吗？日志存放在哪里？**

　　**答**：输出的日志有两种，一个是Cross的log，一个是iObjects的log，分别记录了Cross和组件运行输出的信息，日志中的内容包括程序输出窗口的输出信息、异常信息等内容。日志存放在根目录下的“bin”目录下的“log”文件夹中。

**Q2：在Linux系统上通过桌面使用Oracle数据源，提示打开或新建失败是什么原因呢？**

　　**答**：该现象是没有设置Oracle环境变量的原因，解决方案为：每次启动Cross之前，在Terminal中将Oracle客户端临时设置到环境变量中，例如，通过profile文件设置环境变量，将以下命令写到profile文件中，再在Terminal中执行source profile。

　　`#!/bin/bash`
　　`export ORACLE_HOME=/opt/oracleClient（客户端所在路径）`
　　`export PATH=$PATH:$ORACLE_HOME`
　　`export LD_LIBRARY_PATH=$ORACLE_HOME:$LD_LIBRARY_PATH`

　　Oracle客户端环境变量设置好之后在该Terminal中重新启动Cross，即可正常使用Oracle数据源。
中桌面要新建或者链接一个oracle数据源需要在运行桌面的环境中配置了oracle客户端，将客户端添加到LD_LIBRARY_PATH，可以给一个profile示例。并配置tnsnames.ora。也可以用EZConnect方式连接，则可以不配置tnsnames。
 
**Q3： 项目使用的 iObjects Java 是哪个版本？OSCHINA 上的 develop 分支使用官网最新下载的 iObjects 无法编译通过，如何解决？**

　　**答**：develop分支中的源代码依赖的是还在开发中未发布的iObjects Java，用户无法获取到最新版本的iObjects，因此会导致代码编译出错。建议用户抓取Tags中固定版本的源代码，同时在SuperMap官网下载对应的iObjects Java版本进行编译即可。

**Q4： 启动桌面时无法启动，并提示"许可检查失败，请确认许可是否安装成功。"。**

　　**答**：出现该情况的原因有三个，一是没有配置许可，二是许可未配置成功，三是许可到期。若是前两个原因，则解决方案为重新配置许可，Windows和Linux操作系统的配置方式不同，具体操作请分别参见[Windows配置指南](http://git.oschina.net/supermap/SuperMap-iDesktop-Cross/blob/develop/SuperMap%20iDesktop%20Cross%20%E9%85%8D%E7%BD%AE%E6%8C%87%E5%8D%97&lsaquo;Windows%20%E7%AF%87/&rsaquo;.md?dir=0&filepath=SuperMap+iDesktop+Cross+%E9%85%8D%E7%BD%AE%E6%8C%87%E5%8D%97%28Windows+%E7%AF%87%29.md&oid=3710b6285443d7fd579b3024005bd2ea00d766ab&sha=00f388ee9ca8db2d0fc01f935b1adae511a5a6aa)和[Linux配置指南](http://git.oschina.net/supermap/SuperMap-iDesktop-Cross/blob/develop/SuperMap%20iDesktop%20Cross%20%E9%85%8D%E7%BD%AE%E6%8C%87%E5%8D%97&lsaquo;Linux%20%E7%AF%87&rsaquo;.md?dir=0&filepath=SuperMap+iDesktop+Cross+%E9%85%8D%E7%BD%AE%E6%8C%87%E5%8D%97%28Linux+%E7%AF%87%29.md&oid=8d10edcf1db38491ad05ec5925bfa17275194795&sha=00f388ee9ca8db2d0fc01f935b1adae511a5a6aa)中的许可配置.

**Q5： 为什么在 SUSE Linux Enterprise 11 SP2 操作系统上无法启动Cross？**

　　**答**：在 SUSE 上需要安装第三方数据库客户端才能正确加载sdx数据引擎。一个sdx加载失败会影响其他sdx数据引擎的加载，从而导致无法打开任何数据源或系统崩溃。需要依赖第三方数据库客户端的sdx数据引擎文件有：libSuEngineSRDB.sdx、libSuEngineOracle.sdx、libSuEngineDMCI.sdx、libSuEnginePG.sdx、libSuEngineOsp.sdx、libSuEngineAltibase.sdx、libSuEngineDB2.sdx、libSuEngineKDB.sdx。
　　解决方法是：删除 SuperMap Objects Java_HOME\bin 中不需要的数据库引擎文件（上述列表中的*.sdx）。例如：需要使用 Oracle 数据源，则首先安装 Oracle 客户端，然后删除 libSuEngineOracle.sdx之外的上述所有*.sdx，再启动 iServer 服务。以此类推，如果需要使用其他数据库引擎，也进行相应的处理。 

**Q6：编译运行代码时抛异常了。**

　　**答**：git上代码分为2个分支：master和develop。其中master分支为主版本分支，每次发布稳定版本时会推送到Master分支上。develop分支是开发分支，每天修改的代码都会推送，但不保证代码的稳定性。因此，develop版本可能会出现抛异常的情况。

**Q7：在Linux系统中运行Cross，界面中的字体显示为方块是怎么回事呢。**

　　**答**：这是Linux系统缺失中文字体导致的，在产品包目录下的jre/jre/lib/fonts文件夹中下放置一个中文字体，程序界面文字即可正常显示。

**Q8：在Linux系统下，地图中的文本标签显示异常。**

　　**答**：在地图可视化过程中，会使用到一些特殊的字体，比如“微软雅黑”等，这部分字体在 Linux操作系统中可能没有，从而导致地图注记等文本要素显示异常。请查看附件[fonts_1.zip](http://git.oschina.net/supermap/SuperMap-iDesktop-Cross/attach_files)以及[fonts_2.zip](http://git.oschina.net/supermap/SuperMap-iDesktop-Cross/attach_files)，其中包含了主流常见字体，在Linux操作系统使用本程序或源码时，请下载并解压到"根目录/support/fonts/"目录（如果没有，请自行创建）中，并添加环境变量 SUPERMAP_ROOT,该环境变量值设置为"根目录/support/"。然后参照[《SuperMap iDesktop Cross 8C 扩展开发指南.doc》](http://git.oschina.net/supermap/SuperMap-iDesktop-Cross)进行环境配置，即可正常显示文本要素。

**Q9：没有中文环境的Linux系统中发布带中文名称的地图服务，浏览器中地图预览图片显示异常。**

　　**答**：若Linux没有中文环境，则发布的地图名称建议改成英文名称；或在有中文环境的Linux系统中发布含中文名称的地图服务。

