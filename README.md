# 极简版抖音

## 💡项目介绍

项目完成了抖音的简单后端功能，主要实现的功能有：用户的注册与登陆，视频feed流，视频投稿，用户关注，聊天等功能

## 🚀功能介绍

功能按照**服务模块**进行划分

- **基础模块（basic）**
  - 用户的注册与登陆
  - 获取用户信息
  - 视频feed流
  - 视频投稿
  - 获取视频发布列表
- 互动模块
  - 视频点赞
  - 视频评论
  - 获取视频喜欢列表
  - 获取视频评论列表
- 社交模块
  - 关注用户
  - 获取关注列表
  - 获取粉丝列表
  - 获取好友列表
  - 向好友发送消息
  - 获取聊天记录

## 💻技术栈

- SpringBoot
- SpringCloud
- Gateway
- OpenFeign
- Nacos
- Mybatis-plus
- SpringCache
- Redis
- CompletableFuture
- OSS
- Kafka
- JWT

## 📖项目难点

- OSS存储图片视频对于视频流的处理
- JWT单点登录的利用反射实现注解AOP开发
- 使用Kafka进行不同服务不同数据库的同步功能
- 使用Redis缓存进行聊天记录的存储

## 🖊️开发环境及中间件部署

- **电脑一**
  - linux系统
  - docker部署Redis
  - docker部署Kafka
  - docker部署MySql
  - docker部署Nacos

- **电脑二**
  - Carey的Mac
  - 运行Gateway
  - 运行Interact

- **电脑三**
  - Captian0305的Mac
  - 运行basic
  - 运行socialize

## 🔗接口文档

接口文档使用了Apifox进行接口管理和团队协作

以下是我们接口文档的链接：https://72dbomzouh.apifox.cn



![image](https://github.com/captain0305/tiktok/assets/63387697/82c84baa-b2f3-4dff-a0f5-bc87decd732d)
