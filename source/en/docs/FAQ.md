title: SuperMap iDesktop Cross FAQ
---
**Q1: If the information produced during the program process is output into a log file? Where is the log file?**

　　**A**: There are two kinds of log files, one is for Cross, another one is for iObjects, the contents in these log files include output information in the Output Windows, exception information and so on. These log files are located in the folder "log"under the "bin" directory.

**Q2: Why does the application prompt opening or creating failed when I open an Oracle datasource on Linux OS?**

　　**A**: You maybe forget to set up the Oracle environment variables. Before you enable Cross, you should set oracle environment variables in the Terminal temporarily. For example, write following commands into a profile file, and then execute source profile in Terminal.

　　`#!/bin/bash`
　　`export ORACLE_HOME=/opt/oracleClient (The path where the Oracle client is located in)`
　　`export PATH=$PATH:$ORACLE_HOME`
　　`export LD_LIBRARY_PATH=$ORACLE_HOME:$LD_LIBRARY_PATH`

　　After the environment variables are set up, restart Cross in the Terminal to use the Oracle datasource.
The oracle environment must be configured in the environment where the Cross is run if you want to create or link an oracle datasource. Set the LD_LIBRARY_PATH as the oracle client path, you can give a profile instance and set tnsnames.ora, or connect oracle by EZConnect without setting tnsnames.

**Q3: iDesktop fails to start , and prompts "The license check fails, please verify that the license was installed successfully."**

　　**A**: The problem happens for three reasons. 1. Your application has no license. 2. It was failed to configure license. 3. Your license has been expired. You can re-configure license for first two reasons, for the detail configuration guide on Windows or Linux OS, please refer to [Windows Configuration Guide](http://git.oschina.net/supermap/SuperMap-iDesktop-Cross/blob/develop/SuperMap%20iDesktop%20Cross%20%E9%85%8D%E7%BD%AE%E6%8C%87%E5%8D%97&lsaquo;Windows%20%E7%AF%87/&rsaquo;.md?dir=0&filepath=SuperMap+iDesktop+Cross+%E9%85%8D%E7%BD%AE%E6%8C%87%E5%8D%97%28Windows+%E7%AF%87%29.md&oid=3710b6285443d7fd579b3024005bd2ea00d766ab&sha=00f388ee9ca8db2d0fc01f935b1adae511a5a6aa) and [Linux Configuration Guide](http://git.oschina.net/supermap/SuperMap-iDesktop-Cross/blob/develop/SuperMap%20iDesktop%20Cross%20%E9%85%8D%E7%BD%AE%E6%8C%87%E5%8D%97&lsaquo;Linux%20%E7%AF%87&rsaquo;.md?dir=0&filepath=SuperMap+iDesktop+Cross+%E9%85%8D%E7%BD%AE%E6%8C%87%E5%8D%97%28Linux+%E7%AF%87%29.md&oid=8d10edcf1db38491ad05ec5925bfa17275194795&sha=00f388ee9ca8db2d0fc01f935b1adae511a5a6aa)

**Q4: Why Cross fails to start on SUSE Linux Enterprise 11 SP2 OS?**

　　**A**: You must install a third-party database client to load sdx data engine correctly. The failure of loading a sdx can lead to that any datasource can not be opened or even the system crash. The sdx engine files include: libSuEngineSRDB.sdx, libSuEngineOracle.sdx, libSuEngineDMCI.sdx, libSuEnginePG.sdx, libSuEngineOsp.sdx, libSuEngineAltibase.sdx, libSuEngineDB2.sdx, libSuEngineKDB.sdx. To resolve the problem, you need to delete the unnecessary files (*.sdx files mentioned above). For example, if you need to open an Oracle datasource, you need install the Oracle client first, second, delete all files under SuperMap Objects Java_HOME\bin except libSuEngineOracle.sdx, and then start Cross.

**Q5: Why the characters turn into little squares when running Cross on Linux OS? **

　　**A**: The reason is the lack of the related font. You can place the needed font under the path jre/jre/lib/fonts, and then the interface of Cross can be displayed normally.

**Q6: The text labels on a map are displayed incorrectly on Linux OS.**

　　**A**: During the visualization of a map, some special fonts are required, such as "Microsoft YaHei", while Linux OS lacks these special fonts resulting to the display of labels exceptional. Please see the attachment [fonts_1.zip](http://git.oschina.net/supermap/SuperMap-iDesktop-Cross/attach_files) and [fonts_2.zip](http://git.oschina.net/supermap/SuperMap-iDesktop-Cross/attach_files) where the mainstream fonts can be found. When using Cross on Linux OS, please download and unzip them to the directory "root directory/support/fonts/", and add the environment variable SUPERMAP_ROOT with its value is "root directory/support/".

**Q7: A map server with Chinese name is published on Linux OS without Chinese environment, the display of map is  abnormal when previewing the map in a browser.**

　　**A**: It is suggested that changing the map name from Chinese to English or publishing the map server on Linux OS with Chinese environment.

**Q8: If the version of jre configured in centos5.7 system is older than 1.7, SuperMap iDesktop Cross fails to start and report errors**

  ![](img/jreError.png)

　　**A**: Cross can't start successfully because of the version of jre is too old, there are two ways to handle this problem:
- Method 1: Uninstall the older jre, and install jre 1.7 or newer version.
- Method 2: There is jre 1.7 in Cross product packet, you can modify "export PATH=$SUPERMAP_ROOT:$LD_LIBRARY_PATH:$JRE_PATH:$PATH" to "export PATH=$JRE_PATH:$SUPERMAP_ROOT:$LD_LIBRARY_PATH:$PATH" in the script startup.sh without having to delete the local jre, then execute startup.sh to start Cross.

　　**Note**: The problem has been resolved in Cross 9D.

**Q9: When I run Cross, the error "java.lang.UnsatisfiedLinkError: /opt/SuperMap_iDesktop_Cross_8.1.1_bin_linux64/bin/ libWrapjGeo.so: /usr/lib64/libstdc++.so.6: version `GLIBCXX_3.4.9' not found (required by ./bin/ libSuToolkit.so)" occurs?**

　　**A**: Copy the file "libstdc++.so.6" from the support folder to the Bin folder under the product packet directory.

**Q10: When starting Cross with SecureCRT.exe in centos5.7 system, the following error are reported.**

  ![](img/UIError.png)

　　**A**: Cross relies on iObjects, its starting and operations need performing in graphic interface, hence you can use other remote control tools (such as vnc, xmanager) to connect virtual machines. 

　　**Note**: If you have the problem in other Linux operation systems, you can use the same solution.
