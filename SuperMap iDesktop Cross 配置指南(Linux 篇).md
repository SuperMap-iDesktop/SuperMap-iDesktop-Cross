# JDK #

    本文以 64位 Ubuntu 16.04 操作系统为例。

## 获取 ##

SuperMap iDesktop Cross 产品所使用的 JDK 版本为 **JDK 1.7.0_80** 即 **Java SE Development Kit 7u80**，请前往 [Java 官方网站](http://www.oracle.com/technetwork/java/javase/downloads/java-archive-downloads-javase7-521261.html "Java 官方网站") 获取指定 JDK。请根据您操作系统来选择下载对应的包，本文 Ubuntu 操作系统为例，这里下载 **Linux x64 - jdk-7u80-linux-x64.tar.gz**。

## 安装 ##

1. 打开终端，将当前工作路径更改为您想要安装的路径，举例如下：

	    $ cd Documents/java/

2. 移动下载好的 tar.gz文件包至当前工作路径；
3. 解压并安装，命令如下：

		$ tar zxvf jdk-7u80-linux-x64.tar.gz

4. 删除 tar.gz文件包。

更多详情请参见 [官方 Linux 64位JDK 安装说明](http://www.java.com/en/download/help/linux_x64_install.xml "Linux 64位jdk 安装说明")。

	后文以 JAVA_HOME 代指 JDK根目录。

# 开发工具 #

SuperMap iDesktop Cross 产品是基于 Java 语言的桌面 GIS 平台，产品源代码使用 **Maven** 进行项目管理，同时支持 **IntelliJ IDEA**、**elcipse** 等多种主流 Java 开发平台。

## IntelliJ IDEA（推荐） ##

IntelliJ IDEA 拥有极其丰富且人性化的开发调试工具，并自带 **Maven** 项目管理工具，推荐使用 IntelliJ IDEA 作为项目开发工具。请前往 [IntelliJ IDEA 官方网站](https://www.jetbrains.com/idea/ "IntelliJ IDEA 官方网站") 获取 **IntelliJ IDEA**，然后安装至任意目录。
> 注意：
> 
>     最新版本 IntelliJ IDEA 需要 JDK 1.8 及以上版本支持。如果需要最新版本 IntelliJ IDEA，请同时安装 JDK 1.8 及以上版本。

## Eclipse ##

### 获取 ###

请前往 [Eclipse 官方网站](http://www.eclipse.org/downloads/packages/ "Eclipse 官方网站") 获取 Eclipse Linux版本，然后安装至任意目录。建议使用 **Eclipse for RCP and RAP Developers** 进行开发。举例说明，命令如下：

	$ tar zxvf eclipse-rcp-mars-linux.tar.gz

> 注意：
> 
>     最新版本 Eclipse 也许需要 JDK 1.8 及以上版本支持。如果需要最新版本 Eclipse，请同时安装 JDK 1.8 及以上版本。

### 配置 ###

查看当前目录，进入解压过后的 eclipse 目录，打开 **eclipse.ini** 文件。在文件头，添加如下指令：

	-vm
	JAVA_HOME/jre/bin
	
保存文件，关闭，然后启动 eclipse。

# SuperMap iObjects Java #

## 获取 ##

SuperMap iDesktop Cross 产品是基于 SuperMap iObejcts Java 的二次开发产品，开发以及运行本产品的基本前提是购买并配置了 SuperMap iObjects Java 的许可。
> 注意：
> 
> 1. SuperMap iDesktop Cross 开源并免费，SuperMap iObjects Java 是收费产品，该产品可以一定时间的免费试用，之后如有需要请前往 [SuperMap 官方网站](http://www.supermap.com/ "SuperMap 官方网站") 咨询购买；
> 2. 外部用户扩展开发，请使用 Tag 以及与之版本相对应的 SuperMap iObjects Java 产品。否则程序可能无法运行。

请前往 [SuperMap 官方网站](http://www.supermap.com/ "SuperMap 官方网站") 获取需要的 SuperMap iObjects Java 组件产品，并安装至任意目录。这里下载 SuperMap iObjects Java 8C SP1 Rumtime for Linux。安装命令如下：
	
	$ tar zxvf SMO_Java_801_RumTime_13228_53527_64_x64_linux_gcc_CHS.tar.gz

	后文以 **OBJECTS_HOME** 代指组件产品根目录，请根据您机器操作系统是 32位还是 64位来选择对应的 Java 组件。

## 许可配置 ##

	如需完整部署 SuperMap iObjects Java 产品，或需要许可配置的更多详细信息，请参阅 OBJECTS_HOME 目录下的 InstallationGuide.pdf 文件。

初次使用 SuperMap 产品，需要进行许可的配置安装。Linux 环境下，要验证许可并运行产品，都必须安装许可驱动程序。安装完成即可获得 90天的 SuperMap GIS 系列产品的试用许可。

许可驱动程序位于组件产品包 `OBJECTS_HOME/Support/` 目录下，名称为： `aksusbd-2.0.1-i386.tar`。

安装许可驱动之前，需要以 root 身份进行。在 Ubuntu中，请参考以下步骤切换为 root用户。

	1. 打开终端。
	2. 设置 root 密码，已经设置则忽略，命令：$ sudo passwd root 
	3. 切换为 root 用户，命令：$ sudo -s

进入组件产品包 `OBJECTS_HOME/Support/` 目录，参考以下步骤安装驱动程序。

	1. 解压驱动程序包，命令：$ tar -xvf aksusbd-2.0.1-i386.tar
	2. 进入解压后的目录，命令：$ cd aksusbd-2.2.1-i386
	3. 安装驱动程序，命令：$ sh dinst

如果安装失败，终端输出以下信息

	dpkg-query: no packages found matching aksusbd
	The 32bit support is missing. Please install the x86 compatibility
	packages required by your distribution and retry the installation.
	See the installation guide for more details.
	Aborting...

按以下步骤，安装缺少的支持库。

	1. $ apt-get install libc6-i386
	2. $ apt-get install ia32-libs

安装成功之后，再次执行驱动安装命令即可。

# 获取代码 #

请使用 [Git](https://git-scm.com/download/ "Git") 获取代码，源代码地址如下：

- [OSChina 项目地址: http://git.oschina.net/supermap/SuperMap-iDesktop-Cross](http://git.oschina.net/supermap/SuperMap-iDesktop-Cross "OSChina 项目地址")
- [CSDN 项目地址: https://code.csdn.net/SuperMapDesktop/supermap-idesktop-cross/](https://code.csdn.net/SuperMapDesktop/supermap-idesktop-cross/ "CSDN 项目地址")

SuperMap iDesktop Cross 分支说明：

- develop：项目团队开发用分支，使用了未发布的 iObjects Java 组件产品，外部用户无法基于此版本进行扩展开发。该分支为项目默认分支；
- master：主分支，维护最近一次正式发布版本的代码；
- tag：master 分支上各历史发布版本。

打开终端，进入想要的目录，使用以下命令抓取代码（以 OSChina 远程仓库为例），进入抓取的 SuperMap-iDesktop-Cross 目录，即可浏览整个项目的相关内容。

    $ git clone https://git.oschina.net/supermap/SuperMap-iDesktop-Cross.git

使用此命令将会抓取整个远程仓库，默认为 develop 分支，而外部用户扩展开发需要使用 tag版本，因此请参照以下步骤切换为您想要的 tag版本。

	1. 进入抓取的 SuperMap-iDesktop-Cross 目录，命令：$ cd SuperMap-iDesktop-Cross；
	2. 查看仓库的所有 tag版本，命令：$ git tag -l;
	3. 选择想要的 tag版本，命令：git checkout tags/<tag_name>
	
	注意：tag版本是当前仓库的快照版本，无法做修改提交，因此如果需要提交到当前仓库，请执行以下命令替代步骤3。
		 命令：$ git checkout tags/<tag_name> -b <branch_name>	

	后文以 **CROSS_HOME** 代指项目根目录。

	如果 CROSS_HOME 右下角有锁，当前用户没有读写权限，请切换为 root 用户进行操作，
	或打开终端，进入 CROSS_HOME 同级目录，使用以下命令更改读写权限：
	$ sudo chmod -R 777 SuperMap-iDesktop-Cross/

# 运行程序 #

## IntelliJ IDEA ##

1. 拷贝 `OBJECTS_HOME/Bin/` 或者 `OBJECTS_HOME/Bin_x64/` 下所有文件至 `CROSS_HOME/Bin/` 目录下，如果 `CROSS_HOME` 目录不存在，自行新建。
2. 进入 `CROSS_HOME` 目录，双击 **SuperMap iDesktop Cross.ipr** 启动 IntelliJ IDEA，加载项目；
3. 查看 IntelliJ IDEA 右边侧边栏，打开 Maven Projects 管理面板，展开 **iDesktop.cross - Lifecycle**，双击 **install** 安装依赖，等待完成；
4. 查看 IntelliJ IDEA 顶部工具条，选中 **iDesktop Startup Linux** 启动项，点击其后的 **Run（Shift + F10）/Debug（Shift + F9**）即可运行 SuperMap iDesktop Cross。

## Eclipse ##

1. 拷贝 `OBJECTS_HOME/Bin/` 或者 `OBJECTS_HOME/Bin_x64/` 下所有文件至 `CROSS_HOME/Bin/` 目录下，如果 `CROSS_HOME` 目录不存在，自行新建。
2. 打开 Eclipse，选择一个新的 Eclipse 工作环境；
3. 导入项目。依次选择 **File - Import - Maven - Exitsting Maven Projects Next - Browse**，在弹出的文件窗口选中 **CROSS_HOME**， 点击 **Finish** 导入项目；
4. 配置 Installed JREs。依次选择 **Window - Preferences - Java - Installed JREs**，点击界面右侧 **Add**，弹出向导界面，依次点击 **Standard VM - Next - Directory**，选中 `JDK 1.7.0_80` 安装目录根目录，点击 **Finish**，勾选刚添加的 **JDK 1.7.0_80**，点击 OK 完成配置。
5. 右键选中 `idesktop.cross`并单击打开右键菜单，依次选择 **Run As - Maven install** 安装依赖，等待完成；
6. 展开 `idesktop.cross`，右键选中并单击 `Startup-Eclipse-Linux.launch` 打开右键菜单，依次选择 **Run As - Startup-Eclipse** 即可运行 SuperMap iDesktop Cross。

> 注意：
>
> 如果出现许可不可用等相关问题，请参见前文 **SuperMap iObjects Java - 许可配置** 部分内容。

# 扩展开发 #

> 本部分内容以 IntelliJ IDEA 为例，eclipse 等其他 IDE 除新建项目等操作略有不同之外，项目组织和 pom 文件配置等均一致。

## 新建项目 ##

- 选中 `SuperMap-iDesktop-Cross` 根节点并右键单击打开右键菜单，依次选择 **New - Module** 打开新建向导界面；
- 选中左边栏 **Maven**，点击 **Next** 进入下一步；
- 输入任意 ArtifactId，此处输入 MyPlugin，其他参数默认即可，点击 **Next** 进入下一步；
- 在 **Content root** 后添加 **"\MyPlugin"**,点击 **Finish** 完成创建。

## 插件注册 ##

新建的工程 `MyPlugin` 的初始化状态需要注册为 SuperMap iDesktop Cross 的插件，才能正确运行。查看工程代码，找到并打开 **Activator.java** 文件，进行以下修改。

    public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;

		// 注册 SuperMap iDesktop Cross 插件
		Application.getActiveApplication().getPluginManager().addPlugin("MyPlugin", bundleContext.getBundle());

		// 插件注册成功
		System.out.println("MyPlugin Start.");
	}
注册成功！接下来就可以随心所欲的进行开发了！

## 工程配置 ##

- 展开 `SuperMap-iDesktop-Cross` 根节点；
- 打开文件 `module-pom-template.xml`，这是扩展开发插件的 pom 配置模板文件，其中由 `**` 包围的配置项为必填自定义配置；
- 展开 `MyPlugin` 工程，打开 `pom.xml` 文件，参照模板文件进行配置；
- 查看 IntelliJ IDEA 右边侧边栏，打开 Maven Projects 管理面板，依次展开 **MyPlugin - Plugins - bundle**，双击 **bundle:manifest** 生成 manifest 文件，等待完成。

>注意：
>
>模板提供 pom 配置信息只是最小核心配置，为必备配置。其余更为详细丰富的 pom 工程配置按需求自行完成。
>
>manifest 文件内有 OSGI 插件运行所需的元数据信息，如有 OSGI 依赖的变动，需在运行前执行最后一步重新生成。

## 依赖说明 ##

### 工程依赖 ###

 `Core` 和 `Controls` 作为项目的核心库，是所有插件的必填依赖。

    Core 和 Controls 工程是整个项目的核心工程。
	Core 依赖并导出 SuperMap iObjects Java 以及其他第三方库的类库。
	Controls 依赖 Core，这两个工程共同构建了整个 Cross 项目的核心架构。
	扩展开发自己的插件，必须依赖 Core 和 Controls 工程。

### OSGI 依赖 ###

	本项目基于 OSGI R4 规范的框架 Felix 进行实现，不同的 Bundle 间相互依赖有独特的规则。
	本程序已在工程的 Maven 配置中做了详尽的配置。详情请参见互联网相关资料。