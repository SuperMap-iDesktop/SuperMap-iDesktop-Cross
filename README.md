# SuperMap iDesktop Cross Docs

　　SuperMap iDesktop Cross 是一款开源的 GIS 产品，是一款支持跨平台、全开源的桌面GIS应用与开发平台系统，可在 Windows 和 Linux 系统上运行，其源代码获取地址为：[http://git.oschina.net/supermap/SuperMap-iDesktop-Cross](http://git.oschina.net/supermap/SuperMap-iDesktop-Cross)。

　　SuperMap iDesktop Cross 的帮助文档是基于 Hexo 生成的静态网站，文档的主要内容为 SuperMap iDesktop Cross 的产品介绍、功能描述、使用说明、新特性等。其源代码会实时更新至 OSChina 中，日常的文档更新会实时推送至 GitHub 的 gh-pages 服务上，可通过以下链接查看： [http://supermap-idesktop.github.io/SuperMap-iDesktop-Cross/](http://supermap-idesktop.github.io/SuperMap-iDesktop-Cross/)。同时，SuperMap iDesktop Cross 发布新版本时，相应的联机帮助也会更新部署到 SuperMap 官网的资源中心处，具体链接为 [http://support.supermap.com.cn/SuperMap-iDesktop-Cross/](http://support.supermap.com.cn/SuperMap-iDesktop-Cross/)。

## 简介

　　SuperMap iDesktop Cross 帮助文档是基于 Hexo 生成的静态网站，源文件为 Markdown 文件，使用主题为 navy。目前帮助文档主要分为 `Docs` 和 `News` 两大块内容，`Docs` 的主要内容为 SuperMap iDesktop Cross 的功能介绍和使用说明，`News` 为产品的新特性。

## 环境配置

* **配置 Node.js 环境**

通过 Hexo 编译文档需要提前配置好 Node.js 软件环境，Windows/Linux 系统可选择安装相应的 [Node.js](https://nodejs.org/en/) 版本。


* **安装Git**

   - Windows：下载安装[Git](https://git-scm.com/downloads)
   - [Linux](https://git-scm.com/download/linux)


* **配置 Hexo 环境**

安装 Hexo，打开命令行窗口，在命令行中执行：

     $ npm install -g hexo-cli

抓取文档源代码之后，在源代码所在的目录处打开命令行窗口，并执行以下命令：

     $ npm install hexo --save

可在命令行中输入以下命令，检验 Hexo 是否安装成功：

    $ hexo -v

安装好 Hexo 之后，需再安装 Hexo 运行所需的依赖工具，在命令行中执行以下命令：

    $ npm install


## 生成网站

配置好环境之后可基于源代码编译生成静态网站，在命令行中输入：

    $ hexo g

编译之后通过以下命令启动本地服务，可在 `http://localhost:4000` 网站预览生成的博客：

    $ hexo s

通过键盘中的 `Ctrl+C` 即可停止本地服务。

## 部署网站

Hexo 提供了快速方便的部署功能，部署之前需要先安装 `hexo-deployer-git`，通过命令行 `npm install hexo-deployer-git --save` 进行安装，并在_config.yml 文件中将 deploy 的配置参数设置为：

    deploy:
    type: git
    repo: <repository url>
    branch: [branch]

再通过以下命令将编译结果可部署至 GitHub 的指定分支中：

    $ hexo d

Hexo 的详细操作说明请参见[Hexo官方文档](https://hexo.io)。

## 目录结构

- **_config.yml**

　　网站的配置文件，网站的大部分全局配置信息都可在此设置，例如：title、author、root、theme、deploy等参数。

- **package.json**

　　应用程序的相关信息，及其依赖工具。

- **scaffolds**

　　模板文件夹，在新建文章时，Hexo 会根据 scaffolds 文件夹内相对应的文件来建立文件，例如：`$ hexo new photo "My Gallery"`在执行这行指令时，Hexo 会尝试在 scaffolds 文件夹中寻找 photo.md，并根据其内容建立文章。　

- **script**

　　存放网站的脚本文件。

- **source**

　　用于存放用户资源，生成网站时，除 `_posts` 文件夹之外，开头命名为 `_` (下划线)的文件、文件夹和隐藏的文件将会被忽略，Markdown 和 HTML 文件会被解析并放到 `public` 文件夹，而其他文件会被拷贝过去。

- **themes**

　　主题文件夹，Hexo 会根据文件夹中的主题生成静态网站。

　　
