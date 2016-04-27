# JDK #

SuperMap iDesktop Cross 产品所使用的 JDK 版本为 **JDK 1.7.0_80** 即 **Java SE Development Kit 7u80**，请前往 [Java 官方网站](http://www.oracle.com/technetwork/java/javase/downloads/java-archive-downloads-javase7-521261.html "Java 官方网站") 获取指定 JDK。

# Eclipse #

SuperMap iDesktop Cross 产品是基于 Java 语言的桌面 GIS 平台，建议使用 **Eclipse for RCP and RAP Developers** 进行开发。请前往 [Eclipse 官方网站](http://www.eclipse.org/downloads/packages/ "Eclipse 官方网站") 获取 Eclipse，然后解压至任意目录。
> 注意：
> 
>     最新版本 Eclipse 也许需要 JDK 1.8 及以上版本支持。如果需要最新版本 Eclipse，请同时安装 JDK 1.8 及以上版本。

# SuperMap iObjects Java #

## 获取 ##

SuperMap iDesktop Cross 产品是基于 SuperMap iObejcts Java 的二次开发产品，开发以及运行本产品的基本前提是购买并配置了 SuperMap iObjects Java 的许可。
> 注意：
> 
> 1. SuperMap iDesktop Cross 开源并免费，SuperMap iObjects Java 是收费产品，该产品配置有一定时间的免费试用，之后如有需要请前往 [SuperMap 官方网站](http://www.supermap.com/ "SuperMap 官方网站") 咨询购买；
> 2. 外部用户扩展开发，请使用 Tag 以及与之版本相对应的 SuperMap iObjects Java 产品。否则程序可能无法运行。

请前往 [SuperMap 官方网站](http://www.supermap.com/ "SuperMap 官方网站") 获取需要的 SuperMap iObjects Java 组件产品，并安装至任意目录。

	后文以 **OBJECTS_HOME** 代指组件产品根目录，请根据您机器操作系统是 32位还是 64位来选择对应的 Java 组件。

## 许可配置 ##

初次使用 SuperMap 产品，需要进行许可的配置安装。SuperMap 许可中心（SuperMap License Center）用来管理 SuperMap 相关产品的许可使用情况。SuperMap 许可中心以 zip包的形式提供，请前往 [SuperMap 技术资源中心](http://support.supermap.com.cn/DownloadCenter/ProductPlatform.aspx "SuperMap 许可中心下载") 获取许可中心。

- 将下载的许可中心解压至任意目录，双击运行 **许可中心根目录/Tools/Setup.bat** 批处理文件，安装许可服务。
- 等待许可服务部署成功之后，即可运行 **许可中心根目录** 下的 **SuperMapLicenseCenter.exe** 启动许可中心，首次启动许可中心，默认安装一个90天的试用许可。

# 获取代码 #

请使用 [Git](https://git-scm.com/download/ "Git") 获取代码，源代码地址如下：

- [OSChina 项目地址: http://git.oschina.net/supermap/SuperMap-iDesktop-Cross](http://git.oschina.net/supermap/SuperMap-iDesktop-Cross "OSChina 项目地址")
- [CSDN 项目地址: https://code.csdn.net/SuperMapDesktop/supermap-idesktop-cross/](https://code.csdn.net/SuperMapDesktop/supermap-idesktop-cross/ "CSDN 项目地址")

SuperMap iDesktop Cross 分支说明：

- develop：项目团队开发用分支，使用了未发布的 iObjects Java 组件产品，外部用户无法基于此版本进行扩展开发；
- master：主分支，维护最近一次正式发布版本的代码；
- tag：master 分支上各历史发布版本。

在任意目录下，右键打开 **Git Bash**，使用以下命令抓取代码（以 OSChina 远程仓库为例）

    $ git clone https://git.oschina.net/supermap/SuperMap-iDesktop-Cross.git
打开 SuperMap-iDesktop-Cross 目录，即可浏览整个项目的相关内容。

	后文以 **CROSS_HOME** 代指项目根目录。

# 运行程序 #

1. 拷贝 `OBJECTS_HOME/Bin/` 或者 `OBJECTS_HOME/Bin_x64/` 下所有文件至 `CROSS_HOME/Bin/` 目录下，如果 `CROSS_HOME` 没有这个目录，自行新建。
2. 拷贝 `OBJECTS_HOME/Bin/` 或者 `OBJECTS_HOME/Bin_x64/` 下所有 jar 文件至 `CROSS_HOME/Core/lib/` 目录下。
3. 打开 Eclipse，选择一个新的 Eclipse 工作环境；
4. 导入项目。依次选择 **File – Import – General – Exitsting Project into Workspace – Browse**，在弹出的文件窗口选中 **CROSS_HOME**， 点击 **Finish** 导入项目；
5. 配置 Installed JREs。依次选择 **Window - Preferences - Java - Installed JREs**，点击界面右侧 **Add**，弹出向导界面，依次点击 **Standard VM - Next - Directory**，选中 `JDK 1.7.0_80` 安装目录根目录，点击 **Finish**，勾选刚添加的 **JDK 1.7.0_80**，点击 OK 完成配置。
6. 编辑 OSGI 运行配置文件并运行。
	- 展开 `iDesktop` 工程；
	- 右键选择 **iDesktop Frame Configuration.launch**，点击 **Run As - Run Configurations**，在弹出的配置界面左侧，选中 **OSGI Framework - iDesktop Frame Configuration**，在右侧详细配置界面上，选择 **Environment** 选项卡，点击 **New** 按钮新建启动环境变量，**Name** 填写 `path`，**Value** 填写 `CROSS_HOME/Bin`，配置完毕点击 **Apply**，关闭界面；
	- 右键选择 **iDesktop Frame Configuration.launch**，点击 **Run As - iDesktop Frame Configuration**，程序即可运行。

> 注意：
>
> 如果出现许可不可用等相关问题，请参见前文 **SuperMap iObjects Java - 许可配置** 部分内容。更多许可相关的详细信息，请查阅 SuperMap iObjects Java 产品包根目录下的 **InstallationGuide.pdf** 文件。

# 扩展开发 #

## 新建项目 ##

### 新建 ###

依次点击 **File – New - Project**，打开 **New Project** 窗口。选择 **Plug-in Development** 目录下的 **Plug-in Project**，点击 **Next** 输入工程名称 `MyPlugin`, 其中** OSGI framework** 选择 **Equinox** ,点击 **Next**。**Execution Environment** 设置为 `JavaSE-1.7`，最后点击 **Finish** 按钮即可。

### 配置插件加载优先级 ###

- 展开 `iDesktop` 工程；
- 右键选择 **iDesktop Frame Configuration.launch**，点击 **Run As - Run Configurations**，在弹出的配置界面左侧，选中 **OSGI Framework - iDesktop Frame Configuration**，在右侧详细配置界面上，选择 **Bundles** 选项卡，找到 `MyPlugin`，勾选，然后设置 **Start Level**，数值越大优先级越低，一般默认即可。


## 依赖 ##

### 工程依赖 ###

添加对 `Core` 和 `Controls` 两个工程的依赖。

    Core 和 Controls 工程是整个项目的核心工程。
	Core 依赖并导出 SuperMap iObjects Java 以及其他第三方库的类库。
	Controls 依赖 Core，这两个工程共同构建了整个 Cross 项目的核心架构。
	扩展开发自己的插件，必须依赖 Core 和 Controls 工程。

- 在新建的项目上右键，依次点击 **Bulid Path - Configure Bulid Path**，弹出操作界面；
- 选择 **Libraries** 选项卡，选中 **Plug-in Dependencies**，依次点击 **Remove - Apply**，移除该依赖；
- 选择 **Projects** 选项卡，点击 **Add**，在弹出操作界面上选择 `Core` 和 `Controls` 工程，依次点击 OK - Apply；
- 完成添加，关闭界面。

### OSGI 依赖 ###

    上文的 Eclipse 工程依赖仅是配置 Eclipse 的开发调试环境。
	而 OSGI 规范实现的程序，不同的 Bundle 间相互依赖有独特的规则。详情请参见互联网相关资料。

- 双击打开新建工程的 **MANIFEST.MF** 文件，打开配置窗口；
- 选择 Dependencies 选项卡，点击 **Required Plug-ins** 页面的 **Add** 按钮，添加 `Core` 和 `Controls` 工程。

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