# TuoLingAt

为 Minecraft 服务器打造的跨版本艾特插件。指令与聊天框双通道、零 NMS，音效与标题自动适配 **1.8 ~ 26.x**，装进去就能跑。

- 版本：1.0
- 支持版本：1.8 ~ 26.x
- 服务端：Spigot / Paper 及其分支 / 混合端 / 网易 Java 服
- 硬依赖：无（软依赖 PlaceholderAPI，可选）

> 本项目其实是 **HanAtPlayerPlus 的 4.0 版本**，从最早的前身算起已经迭代了 2 年。

---

## 目录

- [项目总览](#项目总览)
- [为什么选它](#为什么选它)
- [特性](#特性)
- [版本支持](#版本支持)
- [安装](#安装)
- [配置文件](#配置文件)
- [消息占位符](#消息占位符)
- [命令与权限](#命令与权限)
- [聊天框艾特](#聊天框艾特)
- [音效与标题格式](#音效与标题格式)
- [PlaceholderAPI 占位符](#placeholderapi-占位符)
- [开发者文档](#开发者文档)
- [常见问题](#常见问题)

---

## 项目总览

| 项目 | 数值 |
| --- | --- |
| 代码总数 | 1476 行 Java（32 个类，非空行 1248） |
| 框架设计 AI 率 | 0% |
| 核心逻辑 AI 率 | 0% |
| 总体 AI 率 | 30% |

框架设计与核心逻辑全部由人工完成，AI 只参与文档、注释与部分样板代码。

---

## 为什么选它

### 萌新一分钟上手

配置只有 `Message` 和 `Setting` 两块，每个选项的注释就写在旁边，照着改就行；丢进 `plugins/` 重启即生效，没有硬依赖，也不用碰代码。

### 消息不想要？删掉那行就行

某个提示不想显示了，**不用改成空字符串、也不用找开关**，直接把配置里那一行删掉即可 —— 插件取不到配置就自动跳过，不报错、不刷屏；哪天想恢复，再把那一行加回来就好。

### 代码质量经得起看

消息解析、消息发送、版本兼容、缓存、事件监听、命令各自独立成层，改动互不牵连；框架设计与核心逻辑 0% AI 参与，跨版本靠能力探测而非版本号比较。

### 性能几乎零开销

兼容实现只在启动时探测一次并缓存，运行时零反射；没有常驻定时任务、没有轮询；聊天监听按需注册，关掉即注销，不留空转。

### 可进网易 Java 服

这不是单独做的适配，而是**代码质量 + 性能**的自然结果：零 NMS、不引用任何 `net.minecraft` / `craftbukkit` 类，依赖全部 `provided` 不打进产物，不抢类、不额外占用资源，服内直接加载即可。

---

## 特性

| 特性 | 说明 |
| --- | --- |
| 跨版本自适应 | 标题按 5 参 / 2 参 `sendTitle` 自动选择实现，音效按版本自动转换名字格式，全项目零 NMS |
| 聊天框直接艾特 | 聊天里打 `@玩家名` 即可触发，名字自动变绿；支持 `@ Steve` 这种带空格的写法，大小写不敏感 |
| 批量与随机关键字 | `@all`（`@全体玩家`）群发，`@random`（`@随机玩家`）随机点名，且随机点名会在消息里直接变成真实玩家名 |
| 权限节点控制 | 批量、随机、重载各有独立权限节点；聊天框关键字与指令共用同一套权限，无法从聊天框绕过限制 |
| 分级冷却 | 冷却只作用于指令艾特与聊天框普通艾特；`@all` / `@random` 不占冷却；OP 可配置为无视冷却 |
| 对外 API | 提供 `AtEvent` 事件与 PlaceholderAPI 占位符，其他插件可监听艾特行为或读取冷却状态 |
| 多通道提醒 | 被艾特的人同时收到聊天提示、屏幕标题、音效三种反馈，且每个通道都可单独关闭 |
| 控制台可用 | 控制台也能发起艾特，此时 `%sender%` 显示为 `Console` |

---

## 版本支持

从 **1.8** 到 **26.x**（含 2026 年启用的新版本号体系）用同一份代码跑通，不用按版本换 jar，也不用额外装任何兼容插件。

适配方式不是在代码里判断版本号，而是**探测服务端到底提供了哪个方法**：标题看 `sendTitle` 是五参还是两参，音效看有没有字符串版 `playSound`、以及 `Sound#getKey()` 是否存在。因此无论用哪个分支的服务端，都能正确落档。

### 适配分档

| 版本区间 | 标题 | 音效名格式 |
| --- | --- | --- |
| 1.8 ~ 1.8.9 | 两参 `sendTitle`，淡入/停留/淡出被忽略 | `Sound` 枚举值，如 `CAT_AMBIENT` |
| 1.9 ~ 1.10.2 | 两参 `sendTitle`，动画时长同样忽略 | 枚举名，如 `ENTITY_CAT_AMBIENT` |
| 1.11 ~ 1.12.2 | 五参 `sendTitle`，支持自定义动画时长 | 枚举名，如 `ENTITY_CAT_AMBIENT` |
| 1.13 ~ 26.3 | 五参 `sendTitle`，支持自定义动画时长 | 资源路径，如 `entity.cat.ambient` |

### 逐版本覆盖清单

下面是覆盖到的 Minecraft Java 版**正式版**（Release）；快照 / 预发布版未逐个列出，同大版本内一般也能跑。

| 大版本 | 正式版 |
| --- | --- |
| 1.8 | 1.8 · 1.8.1 · 1.8.2 · 1.8.3 · 1.8.4 · 1.8.5 · 1.8.6 · 1.8.7 · 1.8.8 · 1.8.9 |
| 1.9 | 1.9 · 1.9.1 · 1.9.2 · 1.9.3 · 1.9.4 |
| 1.10 | 1.10 · 1.10.1 · 1.10.2 |
| 1.11 | 1.11 · 1.11.1 · 1.11.2 |
| 1.12 | 1.12 · 1.12.1 · 1.12.2 |
| 1.13 | 1.13 · 1.13.1 · 1.13.2 |
| 1.14 | 1.14 · 1.14.1 · 1.14.2 · 1.14.3 · 1.14.4 |
| 1.15 | 1.15 · 1.15.1 · 1.15.2 |
| 1.16 | 1.16 · 1.16.1 · 1.16.2 · 1.16.3 · 1.16.4 · 1.16.5 |
| 1.17 | 1.17 · 1.17.1 |
| 1.18 | 1.18 · 1.18.1 · 1.18.2 |
| 1.19 | 1.19 · 1.19.1 · 1.19.2 · 1.19.3 · 1.19.4 |
| 1.20 | 1.20 · 1.20.1 · 1.20.2 · 1.20.3 · 1.20.4 · 1.20.5 · 1.20.6 |
| 1.21 | 1.21 · 1.21.1 · 1.21.2 · 1.21.3 · 1.21.4 · 1.21.5 · 1.21.6 · 1.21.7 · 1.21.8 · 1.21.9 · 1.21.10 · 1.21.11 |
| 26.1 | 26.1 · 26.1.1 · 26.1.2 |
| 26.2 | 26.2 |
| 26.3 | 26.3（当前最新正式版） |

> **1.21.11 之后换了版本号规则**
> Mojang 从 2026 年起把版本号改成“年份.序号”，`26.1` 比 `1.21.11` 新，按字符串比较会得出错误结论。插件全程按能力探测适配，新旧两套命名规则都能自动落档。

> **哪些服务端可以直接用**
> Spigot、Paper 及其分支、混合端都能直接加载；网易 Java 服同样可用。插件不引用任何 `net.minecraft` / `craftbukkit` 类，依赖全部 `provided` 不打进产物，不会与服内其它插件抢类。

---

## 安装

### 环境要求

| 项目 | 要求 |
| --- | --- |
| 服务端 | Spigot / Paper 及其分支，1.8 ~ 26.x |
| Java | Java 8 及以上（与服务端版本要求一致即可） |
| 硬依赖 | 无 |
| 软依赖 | PlaceholderAPI（可选，缺失不影响任何功能） |

### 安装步骤

1. 将 `TuoLingAt-1.0.jar` 复制到服务端 `plugins/` 目录
2. 完全重启服务器
3. 首次启动会生成 `plugins/TuoLingAt/config.yml`，并在控制台打印插件横幅（含支持版本与当前服务端版本）

控制台输出示例：

```
§f§l§m—=—=—=—=-=-=-=-§e[§cTuoLingAt §f1.0§e]§f§l§m—=—=—=—=-=-=-=-
§e◈§b插件启动完成
§e◈§b支持版本: §f1.8 ~ 26.x
§e◈§b当前服务端版本: §f1.12.2
§f§l§m—=—=—=—=-=-=-=-§e[§cTuoLingAt §f1.0§e]§f§l§m—=—=—=—=-=-=-=-
```

若装了 PlaceholderAPI，控制台还会多一行 `成功加载依赖: PlaceholderAPI(版本)`；没装则打印 `加载依赖失败(没装PlaceholderAPI，不影响使用)`。

> 覆盖 jar 后请**整服重启**。使用 `/reload` 或插件管理工具常常仍会加载旧类。

---

## 配置文件

路径：`plugins/TuoLingAt/config.yml`。改完执行 `/at reload` 生效。

```yaml
#消息设置
Message:
  #发送者的消息设置
  Sender:
    AtOther: "&f[&6系统&f] 你艾特了: &e%target%"
    InCooldown: "&f[&6系统&f] 艾特的太快了!先等到 &e%cooldown% 秒后再艾特吧"
    AtWrong: "&f[&e系统&f] &c玩家不存在或者不在线."
    CannotAtSelf: "&f[&e系统&f] &c你不能艾特自己."
    #给自己播放的音效，格式: 音效名,音量,音调 (音量越大越响, 音调越高越尖)
    #音效名写法: 1.9+ 填字符串名如 ENTITY_CAT_AMBIENT, 1.8 只能填枚举名如 CAT_AMBIENT
    Sound: "ENTITY_CAT_AMBIENT,1,1"
  #接收者的消息设置
  target:
    #给对方弹出的标题，格式: 标题,副标题,淡入,停留,淡出 (时长单位 tick, 20 tick = 1 秒)
    #1.8 ~ 1.10 不支持后三个参数, 直接写 Title: "&6%sender%,&b好像在艾特你"
    Title: "&6%sender%,&b好像在艾特你,10,30,10"
    ChatBot: "&f[&e系统&f] 玩家 &b%sender% &f在艾特你,快去回复他吧."
    Sound: "ENTITY_CAT_AMBIENT,1,1"
Setting:
  #艾特的冷却(聊天框艾特和指令艾特共用一套冷却系统)
  CommandCooldown: 10
  #OP 是否无视冷却
  OpNoCooldown: true
  #是否启动聊天框艾特别人(更改此选项后执行 /at reload 即刻生效)
  EnableChatAt: true
```

### Message.Sender · 发起者提示

| 键 | 作用 | 可用占位符 |
| --- | --- | --- |
| `AtOther` | 艾特成功时给自己的反馈（艾特全员时不发，避免刷屏） | `%target%` |
| `InCooldown` | 处于冷却中时的提示 | `%cooldown%` |
| `AtWrong` | 目标不存在或不在线时的提示 | — |
| `CannotAtSelf` | 尝试艾特自己时的提示 | — |
| `Sound` | 给自己播放的音效 | — |

### Message.target · 被艾特者提示

| 键 | 作用 | 可用占位符 |
| --- | --- | --- |
| `Title` | 弹出的屏幕标题 | `%sender%` |
| `ChatBot` | 聊天框提示 | `%sender%` |
| `Sound` | 播放的音效 | — |

### Setting · 行为开关

| 键 | 默认值 | 说明 |
| --- | --- | --- |
| `CommandCooldown` | `10` | 艾特冷却秒数，指令艾特与聊天框普通艾特共用；填 `0` 关闭冷却 |
| `OpNoCooldown` | `true` | OP 是否无视冷却 |
| `EnableChatAt` | `true` | 是否启用聊天框艾特；改完执行 `/at reload` 即刻生效 |

> **删掉一行就等于关掉那条消息。** 任何消息、标题、音效配置行删除后，插件取不到配置会自动跳过，不报错也不刷屏；想恢复把那行加回来即可。

---

## 消息占位符

配置文案里可以写下面这些占位符，发送时自动替换：

| 占位符 | 含义 | 出现在 |
| --- | --- | --- |
| `%target%` | 被艾特的玩家名 | `Message.Sender.AtOther` |
| `%cooldown%` | 剩余冷却秒数 | `Message.Sender.InCooldown` |
| `%sender%` | 发起艾特的人；由控制台发起时为 `Console` | `Message.target.Title` / `ChatBot` |

颜色码用 `&` 书写（如 `&6` 金色、`&b` 青色）。

---

## 命令与权限

主命令为 `/at`，`/hanat` 是完全等效的别名。

| 命令 | 作用 | 权限 |
| --- | --- | --- |
| `/at <玩家名>` | 艾特指定玩家 | 无需权限 |
| `/at all` | 艾特所有在线玩家 | `tuolingat.atall` |
| `/at random` | 随机艾特一位在线玩家 | `tuolingat.atrandom` |
| `/at reload` | 重载配置与聊天监听 | `tuolingat.reload` |
| `/at` | 显示帮助 | 无需权限 |

补充说明：

- 控制台也能执行 `/at all`、`/at random`，此时被艾特者看到 `%sender%` 为 `Console`
- Tab 补全只补全**在线玩家名**
- 权限节点与是否 OP 无关，按 LuckPerms 等权限插件正常分配即可

---

## 聊天框艾特

玩家在聊天框打 `@名字` 即可触发艾特，触发的名字会变色。

| 写法 | 颜色 | 效果 |
| --- | --- | --- |
| `@Steve` | 绿色 | 艾特 Steve，对方收到聊天提示 + 标题 + 音效 |
| `@ Steve`（带空格） | 绿色 | 同上，且消息里的空格会被去掉，显示为 `@Steve` |
| `@all` · `@全体玩家` | 黄色 | 艾特除自己外的所有在线玩家，需要 `tuolingat.atall`；发起者不会收到逐条的“你艾特了谁”提示 |
| `@random` · `@随机玩家` | 黄色 | 随机艾特一位在线玩家；消息里会**直接显示抽到的玩家名**，需要 `tuolingat.atrandom` |
| `@自己` | 不变色 | 提示“你不能艾特自己” |
| `@不存在的人` | 不变色 | 提示“玩家不存在或者不在线”（一条消息只提示一次） |

### 冷却规则

| 入口 | 是否受冷却 |
| --- | --- |
| 指令 `/at <玩家名>` | 受冷却 |
| 指令 `/at all` · `/at random` | 受冷却 |
| 聊天框 `@玩家名` | 受冷却 |
| 聊天框 `@all` · `@全体玩家` · `@random` · `@随机玩家` | **不受、也不占用**冷却 |
| OP（`OpNoCooldown: true`） | 无条件放行 |

消息里写了多个 `@玩家名` 时，只在触发前检查一次冷却，不会因为艾特的人多而重复扣。冷却中的提示只发给发起者，不会打扰被艾特的人。

### 其他细节

- **大小写不敏感**：`@steve` 可以命中在线玩家 `Steve`
- **重复去重**：`@Steve @Steve` 只艾特一次；`@Steve @all` 时 Steve 也不会收到两遍
- **关键字优先**：若服务器真有玩家叫 `all` / `random`，这两个名字会优先按关键字处理；`全体玩家` / `随机玩家` 是中文别名，只整体匹配，不会吃掉跟在后面的中文
- **权限与指令一致**：聊天框的 `@all` / `@random` 同样校验权限，无法绕过指令限制
- **热开关**：把 `EnableChatAt` 改成 `false` 后执行 `/at reload`，插件会直接注销聊天监听（不注册即不产生开销）
- **不给自己发**：艾特自己会被拦截并提示

---

## 音效与标题格式

### 标题

格式：`标题,副标题,淡入,停留,淡出`，时长单位为 tick（20 tick = 1 秒）。

```yaml
Title: "&6%sender%,&b好像在艾特你,10,30,10"
```

1.8 ~ 1.10 的服务端不支持后三个参数，直接写：

```yaml
Title: "&6%sender%,&b好像在艾特你"
```

### 音效

格式：`音效名,音量,音调`（音量越大越响，音调越高越尖）。

```yaml
Sound: "ENTITY_CAT_AMBIENT,1,1"
```

音效名两种格式都能写，插件会按当前服务端版本自动转换：

- 枚举名：`ENTITY_CAT_AMBIENT`
- 资源路径：`entity.cat.ambient`

1.8 服务端只能填较短的枚举名（如 `CAT_AMBIENT`）。若音效名在当前版本确实不存在，插件只会在控制台打一条警告，聊天提示与标题照常发送，命令也不会因此报错。

---

## PlaceholderAPI 占位符

插件为 PlaceholderAPI 提供 `tuolingat` 标识符（软依赖，仅装上 PAPI 时注册）：

| 占位符 | 返回 |
| --- | --- |
| `%tuolingat_online%` | 在线玩家数 |
| `%tuolingat_player%` | 当前玩家名 |
| `%tuolingat_cooldown%` | 自己剩余的艾特冷却秒数 |

控制台请求（`player` 为 `null`）时返回空串或 `0`，不会抛异常。

---

## 开发者文档

### 项目结构

```
com.tuoling.tuolingat
├── TuoLingAt.java                 插件主类，负责装配与注册
├── api/event/AtEvent.java         对外艾特事件
├── cache
│   ├── PlayerCache.java           在线玩家缓存
│   └── CooldownCache.java         冷却缓存
├── command
│   ├── CommandManager.java        子命令注册与派发
│   ├── CommandTrigger.java        Executor + TabCompleter
│   ├── CommandUtil.java           参数 / 根命令判定
│   ├── AtExecUtil.java            艾特执行与提示（共享逻辑）
│   ├── AtPlayerCommand.java       at <玩家名>
│   ├── AtAllCommand.java          at all
│   ├── AtRandomCommand.java       at random
│   ├── AtReloadCommand.java       at reload
│   └── AtHelpCommand.java         at（帮助）
├── compat
│   ├── CompatManager.java         按优先级选取兼容实现
│   ├── title/                     NowTitleCompat / MidTitleCompat
│   ├── sound/                     NowSoundCompat / OldSoundCompat / SoundNameUtil
│   └── model/                     TitleModel / SoundModel
├── listener
│   ├── ListenerManager.java       常驻 / 可开关监听注册
│   ├── PlayerListener.java        进出服维护 PlayerCache
│   └── ChatAtListener.java        聊天框艾特
├── message
│   ├── MessageGetter.java         只负责“取”：解析文案
│   ├── MessageSender.java         只负责“发”：投递到通道
│   └── ParseMode.java             解析模式
├── placeholder/AtExpansion.java   PlaceholderAPI 扩展
└── utils/ConsoleMessageUtil.java  控制台横幅与分隔线
```

### 启动流程

```
onEnable()
├── saveDefaultConfig() / reloadConfig()
├── registerManagers()     CompatManager / MessageGetter / MessageSender / CommandManager
├── registerCommands()     at、hanat 的 executor 与补全
├── registerListeners()    PlayerListener 常驻 + ChatAtListener 按配置
├── registerPlaceholder()  装了 PAPI 才注册，并把加载结果打印到控制台
└── ConsoleMessageUtil.printEnable()
```

### 消息解析与发送

`ParseMode` 控制解析强度：

| ParseMode | 自定义占位符 | PlaceholderAPI |
| --- | --- | --- |
| `RAW` | 否 | 否 |
| `CUSTOM` | 是 | 否 |
| `PAPI` | 否 | 是 |
| `CUSTOM_PAPI` | 是 | 是 |

`MessageSender` 分两组方法：`sendSelf*`（发给自己）与 `sendTarget*`（发给别人），消息、标题、音效三个通道都是空值直接跳过。

### 发送顺序与异常隔离

`AtExecUtil.at()` 会**先通知对方、再给自己反馈**。这样即使自身反馈（例如音效）出现异常，也不会导致被艾特的人收不到提醒。

艾特全员走的是 `AtExecUtil.atSilent()`：只通知对方，不给发起者任何反馈。否则一次 `/at all` 会给发起者刷出与在线人数等量的“你艾特了谁”提示。

### 缓存与线程

- `PlayerCache` 用 `CopyOnWriteArrayList`：异步聊天线程读、主线程写，读写不冲突
- 聊天事件是异步的，所有 Bukkit API 消息发送都通过 `runTask` 调度回主线程
- 冷却与在线筛选都在主线程任务里判定

### 跨版本兼容层

标题与音效各由一组实现类提供，每个实现自带 `isMatch()`：通过反射探测当前服务端是否具备某个 Bukkit 公开方法，来判断自己是否适用。`CompatManager` 按优先级顺序取第一个命中的实现并缓存。

| 实现 | 判定依据 | 覆盖版本 |
| --- | --- | --- |
| `NowTitleCompat` | 存在五参 `sendTitle` | 1.11 ~ 26.x |
| `MidTitleCompat` | 无五参、但有两参 `sendTitle` | 1.8 ~ 1.10 |
| `NowSoundCompat` | 存在字符串版 `playSound` | 1.9 ~ 26.x |
| `OldSoundCompat` | 不存在字符串版 `playSound` | 1.8 |

音效名归一化：1.9 ~ 1.12 的字符串版 `playSound` 收的是枚举名，1.13+ 收的是资源路径，方法签名相同但语义不同。`SoundNameUtil` 负责两种格式互转：

```java
SoundNameUtil.toResourcePath("ENTITY_CAT_AMBIENT"); // entity.cat.ambient
SoundNameUtil.toEnumName("entity.cat.ambient");      // ENTITY_CAT_AMBIENT
```

更细的版本差异（该用哪种格式）由 `NowSoundCompat` 内部探测 `Sound#getKey()` 是否存在来判定。发送过程包了 `try/catch`，解析失败只在控制台告警，不影响其他通道。

> **为什么不用 NMS**
> 插件只调用 Bukkit 公开 API，不引用任何 `net.minecraft` / `craftbukkit` 类，因此在分支服、混合端上也能加载。

### AtEvent 事件

每次艾特真正发出通知前抛出 `AtEvent`（主线程同步触发），供其他插件获取“谁艾特了谁”。

```java
@EventHandler
public void onAt(AtEvent e) {
    Player sender = e.getSender();          // 控制台发起时为 null
    String senderName = e.getSenderName();  // 控制台发起时为 "Console"
    Player target = e.getTarget();
    if (e.isFromConsole()) {
        // 控制台发起的艾特
    }
}
```

### 构建与部署

```
mvn clean package
```

构建后 `target/` 下有两个产物：

| 文件 | 说明 |
| --- | --- |
| `TuoLingAt-1.0.jar` | **部署用**，shade 后的完整产物，放进 `plugins/` |
| `original-TuoLingAt-1.0.jar` | shade 前的原始 jar，不要部署 |

依赖 `spigot-api`、`lombok`、`placeholderapi` 全部为 `provided`，不会打进产物。

### 编码约定

- 方法名小驼峰，参数用 `sender` / `target`
- 注释用中文，写在必要处，格式统一为 `// 说明`
- 共享执行逻辑放在静态工具类（如 `AtExecUtil`）
- 命令拆成独立类，不写抽象基类
- 缓存类统一放 `cache` 包

---

## 常见问题

### 玩家打 `@名字` 没反应，也没提示

- 确认 `Setting.EnableChatAt` 是 `true`，改完要 `/at reload` 或重启
- 确认对方**在线**：名字不存在或已离线时会提示“玩家不存在或者不在线”，不会有其他动作
- 如果连“不存在”的提示都没有，说明聊天监听没生效（回到第一条）

### 冷却中对方还是收到了？

冷却只在触发前拦一次。请确认走的是哪条入口：`@all` / `@全体玩家` / `@random` / `@随机玩家` 按设计不受冷却限制。

### OP 一直没冷却，想限制怎么办

把 `Setting.OpNoCooldown` 改成 `false`，OP 就和其他玩家一样受 `CommandCooldown` 限制。

### 1.8 服务端没有声音，控制台有警告

1.8 的 `Sound` 枚举名比 1.9+ 短（例如 `CAT_AMBIENT` 而非 `ENTITY_CAT_AMBIENT`），把配置里的音效名改成 1.8 的枚举名即可。这类解析失败只影响音效，聊天提示与标题照常。

### 改了配置没生效

执行 `/at reload`，或整服重启。改 `EnableChatAt` 需要 reload 才会重新注册 / 注销聊天监听。

### 覆盖 jar 后还是旧版本行为

Bukkit 的 `/reload` 常会残留旧类，请**完全停服再启动**。

---

## 相关文档

仓库内 `docs/` 提供了一份静态文档站，直接用浏览器打开即可：

- `docs/index.html` —— 首页（项目总览、亮点、特性、版本支持、快速开始）
- `docs/wiki.html` —— 用户文档（安装、配置、命令权限、聊天艾特、跨版本、FAQ）
- `docs/dev.html` —— 开发者文档（结构、架构、AtEvent、扩展点、构建）
