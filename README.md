
请勿滥用，本项目仅用于学习和测试！请勿滥用.

# 一个项目   
### 为啥要做这个
    啊  我喜欢的抖音视频怎么被下架了！怎么失效了！可恶  手机内存又满了！
### 简介
    一个视频下载汇聚类功能  
    将视频下载地址推送给 应用目前已经支持的下载器 
    目前仅支持Aria2 
    并建立视频资源缓存 后台可进行管理查询查看等 
    后期预计增加视频墙功能

### 功能点
    已实现部分
        配置下载器
        多用户管理
        视频资源缓存查看
    未实现部分
        视频墙
        .....
### 技术框架
spring boot 2.7.10
spring boot jpa
sqlite 

### 预计目标实现及支持平台

## 支持平台
    抖音  已实现
    B站   未实现
### 部署
    任意java 环境或者docker
    
    docker 部署下 容器端口 为20801
    必须挂载的目录 为
    /app/resources/video       请注意此处  挂载目录必须与系统配置用推送给Aria2的下载目录一致
    /app/resources/cover
    /app/db
    /tmp
### 后台默认帐号密码
    后台地址是 http://ip:port/admin/login
    默认帐号为admin 默认密码为123456
### 意见反馈
    
### 更新方向
    暂无
