# 人脸识别

> 这是简单的通过虹软免费SDK做的一套人脸识别系统的Java实现,根据虹软实现的 人脸检测、追踪、识别 的JAVA解决方案

## 目录简介

----images 用于测试图片存储空间
----libs 项目包所需要的依赖
----src/main
    ----java/com/test java源码
         ----facecheck 人脸检测源码
         ----facematch 人脸检测与比对源码
         ----facetrack 人脸追踪源码
     ----resources 项目的配置文件,用于存放自己的sdk密匙设置
     
## 使用说明

### 一、拉取源码

### 二、获取KEY和SDK

#### 1、注册虹软账号

- 打开网址：https://ai.arcsoft.com.cn/index.html 点击注册账号。

#### 2、登录并且申请各种KEY。

- 注册完后登陆,选择应用管理,点击添加SDK,选择ArcFace,再选择windows版本下。

#### 3、下载SDK替换掉libs文件下的依赖包

- 在申请好SDK后会提供一系列串的KEY和SDK下载地址,下载SDK压缩包，打开里面的lib文件夹，把里面的内容放置到人脸识别项目下的libs目录 替换掉项目下的库文件。

#### 4、测试项目是否能正常加载SDK

- 打开项目下的engine包,启动里面的引擎类,如果输出SDK版本号就代表SDK加载成功

- 如果发生错误，如：

    face detection init fail！ERROR--:0x7001 APP_ID错误

    face detection init fail！ERROR--:0x7002 SDK_KEY错误

    如果发生加载lib包异常,先检测本系统是否安装vc2010 2012 2013等等一系列依赖包

### 三、启动项目

运行facecheck包下的Application.class 来启动人脸检测项目

运行facematch包下的Application.class 来启动人脸检测与比对项目

运行facetrack包下的Application.class 来启动人脸追踪项目
