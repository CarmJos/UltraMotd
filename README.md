```text
   __  ______                __  _______  __________ 
  / / / / / /__________ _   /  |/  / __ \/_  __/ __ \
 / / / / / __/ ___/ __ `/  / /|_/ / / / / / / / / / /
/ /_/ / / /_/ /  / /_/ /  / /  / / /_/ / / / / /_/ / 
\____/_/\__/_/   \__,_/  /_/  /_/\____/ /_/ /_____/  
```

# UltraMOTD 超级介绍展示

[![CodeFactor](https://www.codefactor.io/repository/github/CarmJos/UltraMOTD/badge?s=b76fec1f64726b5f19989aace6adb5f85fdab840)](https://www.codefactor.io/repository/github/CarmJos/UltraMOTD)
![CodeSize](https://img.shields.io/github/languages/code-size/CarmJos/UltraMOTD)
[![Download](https://img.shields.io/github/downloads/CarmJos/UltraMOTD/total)](https://github.com/CarmJos/UltraMOTD/releases)
[![Java CI with Maven](https://github.com/CarmJos/UltraMOTD/actions/workflows/maven.yml/badge.svg?branch=master)](https://github.com/CarmJos/UltraMOTD/actions/workflows/maven.yml)
![Support](https://img.shields.io/badge/Minecraft-Java%201.8--Latest-blue)
![](https://visitor-badge.glitch.me/badge?page_id=UltraMOTD.readme)

为 BungeeCord 制作的MOTD(消息介绍)插件，支持定制各类消息、分IP反馈介绍内容、随机消息轮换等功能。

项目代码符合开发规范，适合新手开发者学习BungeeCord-API，制作属于自己的插件。

## 功能与优势

### 当前功能

### 优势

## [依赖](https://github.com/CarmJos/UltraMOTD/network/dependencies)

- **[必须]** 插件本体基于 [BungeeCord]()。
  实现。
- **[自带]** 消息格式基于 [MineDown](https://github.com/Phoenix616/MineDown) 实现。
    - 所有 messages.yml 均支持 MineDown 语法。

## 指令

以下指令的主指令为 `/UltraMOTD` 或 `/motd`。

- 必须参数 `<参数>`
- 可选参数 `[参数]`

```text
# reload
@ 管理指令 (UltraMOTD.admin)
- 重载插件配置文件。
```

## 配置

### 插件配置文件 ([config.yml]())

详见源文件。

### 消息配置文件 ([messages.yml]())

支持 [MineDown 语法](https://wiki.phoenix616.dev/library:minedown:syntax)，详见源文件。

## 使用统计

[![bStats](https://bstats.org/signatures/bukkit/UltraMOTD.svg)](https://bstats.org/plugin/bukkit/UltraMOTD/18596)

## 支持与捐赠

若您觉得本插件做的不错，您可以捐赠支持我，感谢您成为开源项目的支持者！
Many thanks to Jetbrains for kindly providing a license for me to work on this and other open-source projects.  
[![](https://resources.jetbrains.com/storage/products/company/brand/logos/jb_beam.svg)](https://www.jetbrains.com/?from=https://github.com/CarmJos/UserPrefix)

## 开源协议

本项目源码采用 [GNU General Public License v3.0](https://opensource.org/licenses/GPL-3.0) 开源协议。
<details>
<summary>关于 GPL 协议</summary>

> GNU General Public Licence (GPL) 有可能是开源界最常用的许可模式。GPL 保证了所有开发者的权利，同时为使用者提供了足够的复制，分发，修改的权利：
>
> #### 可自由复制
> 你可以将软件复制到你的电脑，你客户的电脑，或者任何地方。复制份数没有任何限制。
> #### 可自由分发
> 在你的网站提供下载，拷贝到U盘送人，或者将源代码打印出来从窗户扔出去（环保起见，请别这样做）。
> #### 可以用来盈利
> 你可以在分发软件的时候收费，但你必须在收费前向你的客户提供该软件的 GNU GPL 许可协议，以便让他们知道，他们可以从别的渠道免费得到这份软件，以及你收费的理由。
> #### 可自由修改
> 如果你想添加或删除某个功能，没问题，如果你想在别的项目中使用部分代码，也没问题，唯一的要求是，使用了这段代码的项目也必须使用
> GPL 协议。
>
> 需要注意的是，分发的时候，需要明确提供源代码和二进制文件，另外，用于某些程序的某些协议有一些问题和限制，你可以看一下
> @PierreJoye 写的 Practical Guide to GPL Compliance 一文。使用 GPL 协议，你必须在源代码代码中包含相应信息，以及协议本身。
>
> *以上文字来自 [五种开源协议GPL,LGPL,BSD,MIT,Apache](https://www.oschina.net/question/54100_9455) 。*
</details>