# SuperMap iDesktop Cross Docs

　　SuperMap iDesktop Cross a cross-platform and open source desktop GIS application and development platform system, which can be run on Windows and Linux, the address for accessing source code is: [http://git.oschina.net/supermap/SuperMap-iDesktop-Cross](http://git.oschina.net/supermap/SuperMap-iDesktop-Cross).

　　SuperMap iDesktop Cross Help Document is statistic site based on Hexo, the main contents are SuperMap iDesktop Cross product introduction, function description, instructions, new features and so on. Its source code will be updated to OSChina in real time, and the update of the document will be send to gh-pages service of GitHub, you can see the details by the link: [http://supermap-idesktop.github.io/SuperMap-iDesktop-Cross/](http://supermap-idesktop.github.io/SuperMap-iDesktop-Cross/). At the same time, when  new version of SuperMap iDesktop Cross is published, the corresponding online help will be updated to Resource Center on SuperMap official website, detail link is: [http://support.supermap.com.cn/SuperMap-iDesktop-Cross/](http://support.supermap.com.cn/SuperMap-iDesktop-Cross/).

## Introduction

　　The source file of SuperMap iDesktop Cross Help Document is Markdown file, the used theme is navy. The document is divided into two sections: "Docs" and "News" currently. "Docs" is about the function introduction and instructions, "News" is for the new features of product.

## Environment Configuration

* **Configuring Node.js environment**

You need configure software environment "Node.js" in advance for compiling document by Hexo. For Windows/Linuxs system, you can install corresponding version [Node.js](https://nodejs.org/en/).


* **Installing Git**

   - Windows: Downloading and installing [Git](https://git-scm.com/downloads)
   - [Linux](https://git-scm.com/download/linux)


* **Configuring Hexo environment**

Installing Hexo. Opening command-line interface, and then performing:

     $ npm install -g hexo-cli

After successfully getting the source code, open command-line interface at the directory of source code and install required dependent tools for running Hexo by executing following command:

    $ npm install


## Generating website

Based on source code to compile to generate statistic site after configuring environment, entering following command:

    $ hexo g

Start local service by following command after compiling, and then you can preview the generated blog on website "http://localhost:4000"

    $ hexo s

You can stop the local service by pressing "Ctrl+C".

## Deploying website

Hexo provides fast and convenient deployment functions. Before deploying, you need install "hexo-deployer-git" by the command line "npm install hexo-deployer-git --save" and set the configuration parameters in _config.yml as: 

    deploy:
    type: git
    repo: <repository url>
    branch: [branch]

 And then deploying the compiled result to specified branch of GitHub by following command:

    $ hexo d

For detailed operation instruction about Hexo, please reference [Hexo official document](https://hexo.io).

## Directory Structure

- **_config.yml**

　　Configuration file of website. Most of the site's global configuration information can be set here, such as parameter: title, author, root, theme, deploy, and so on.

- **package.json**

　　Related information and dependent tool of application program.

- **scaffolds**

Template folder. When build a new article, Hexo will build file according to the file corresponded with scaffolds folder, such as: when executing this command '$ hexo new photo "My Gallery"', Hexo will try to find photo.md in the folder "scaffolds" and build an article on the basis of its contents.

- **script**

　　Store the script file for the site.

- **source**

　　Used to store user resource, when generating site, these files or folders whose names start with "_" and the hidden files are ignored, except for folder "_posts". Markdown and HTML file will be parsed and put into folder "public", while other files will be copied to the folder.

- **themes**

　　Theme folder, Hexo can generate the statistic site according to theme in folder.

　　
