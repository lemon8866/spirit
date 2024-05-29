### 更新记录

```
2024/05/29
抖音ck不再二次处理简化
新增单视频企业微信Bot通知
新增启动校验防止CK等环境基础变量未加载到内存而导致提交的任务下载失败
新增企业微信Bot 请求地址配置 不配置默认不发送解析完成通知 目前仅下载成功发送通知
```

```
2023/11/20
更新小程序版本 支持多服务器设置
新增视频查看模式
修改菜单icon
```

```
2023/11/18
暂时屏蔽htmlclient备选方案
```

```
2023/11/15
收藏夹任务新增任务名称
升级yt-dlp
优化定时任务归零问题
```

```
2023/10/27
彻底移除远程xBogus
```

```
2023/10/24
steam 分支升级基础镜像
```


```
2023/10/23
后台更换UI
新增 静态资源 添加验证 需登录 或者请求地址追加?apptoken=xxxx 参数
修复系统配置中定时器无法保存的bug
优化收藏夹监控功能
```


```
2023/10/20
Dev 分支新增 推特  YouTube ins 解析  
Dockerfile 中引入Python3
Dockerfile 中引入yt-dlp
需要特殊网络才可以解析下载
本容器可以做到解析成功 但是 不监控Aria2下载是否成功  在开发阶段 ins 视频有概率失败  但是重启Aria2容器 任务会重新开始 并下载成功 暂时不知道是为什么
后期考虑 在发送请求是给Aria2 发送yt-dlp返回的请求头尝试一下
本功能暂时为合并到 steam 分支及latest 分支 
```


```
2023/10/18
修复 bilibili下载中 分辨率写错判断 导致视频被降级而无法下载的bug
修复 bilibili中 需要ffmpeg合并时  当视频重复下载时 会造成 ffmpeg覆盖问题导致终端卡住的问题
修复 bilibili中 当视频为普通视频时 取的是dash 而不是durl 造成视频下载失败问题
```


```
2023/10/10
新增desktop-win 桌面提交工具
修复latest分支下无bash 问题导致ffmpeg合并失败的问题
优化部分存储路径
优化app 中因文件名包含中文或特殊符号无法播放视频的问题

```


```
2023/09/12
优化以下两个目录
/app/resources/video
/app/resources/cover

优化成
/app/resources/{platform}/{yyyy}/{MM}/{name}/{name}.mp4
/app/resources/{platform}/{yyyy}/{MM}/{name}/{name}.jpg

也就是说将 图片和视频 放在一个文件夹下 方便手工管理
新增steam分支

```

```
修复抖音下载问题 如需使用抖音必须填写登录后的cookie 否则无法下载.移除远程服务器生成Xbogus
如果不填写COOKIE 单个视频将使用htmlclient方式获视频信息,然后就会有内存占用高的问题
所以建议填写Cookie 使用接口获取视频信息
```


```
新增java 生成Xbogus  不再需要调用node的接口  后续稳定后会合并到latest分支  默认情况下 会先本地生成
本地生成无效是会在调用api类型  不过应该不会 所以基本上只会走本地调用 所以 解析地址和qingfeng2336/spirit-assist-node无需在关注
```

```
忘记哪天的了
vercel函数部署的接口 由于所在服务器地址问题可自行搭建 并在系统配置中修改解析地址http://ip:port
docker run --name spirit-assist -d -p 53123:80 qingfeng2336/spirit-assist-node:latest
```

```
20230511一支持哔哩收藏夹下载 同时优化了系统日志 方便排错  优化了提交状态

有点前言要说 本项目目前处于一天一更的状态,如果使用的话 尽可能的每天都看一下docker状态
作为一个解析类项目 我觉得下载任务还是有必要完全交给下载器去做  所以在最新的版本中 Aria2也支持Bili的下载
最新版本中已经移除Bili对htmlunit的依赖 所以只有解析抖音的时候才会增加内存开销
20230408最新版本中 dy解析方式 已变更为优先API解析 API解析失败是才会调用htmlunit
为了不给docker中在加一个node环境   API 为 nodejs  vercel函数部署
后续会考虑出一个 docker 中自带node 并部署解析接口API
有问题请提交issues
```