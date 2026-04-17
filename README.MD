# Glow Tools

Glow Tools 是一款 Minecraft 辅助类模组，仅客户端安装。默认按 G + T 打开配置页面。  
Glow Tools is a client-side utility mod for Minecraft designed to streamline technical gameplay and resource management. By default, press G + T to open the config page.

## 功能 Features

### 实体生成提醒 Entity Spawn Alerts

适用于空岛生存，实时监测世界中特定实体的生成，并提醒玩家。可选择检测流浪商人、掠夺者、叼着绿宝石的狐狸、手持嗅探兽蛋的溺尸，以及自定义实体。  
Specifically designed for Skyblock survival, this feature monitors the world in real-time for specific entity spawns and alerts the player immediately.  
Monitored Targets: Wandering Traders, Pillagers, Foxes holding Emeralds, Drowned holding Sniffer Eggs, and Custom Entities (via ID).

* 通知方式：聊天栏、动作栏的消息提示，音效提示（可选择音效 id，或外部 .wav 文件）。
* Notification Methods: Supports Chat and Action Bar messages, along with sound alerts (via Minecraft sound IDs or external .wav files).

* 可设置为目标实体给予发光效果。
* Visual Highlights: Option to apply a glowing effect to the target entity.

* 可设置忽略命名的实体。  

* 可设置在发现目标实体后退出游戏世界。  
* Advanced Logic: Configurable options to ignore named entities or automatically disconnect/exit the world when a target is discovered.

### 稀有热带鱼追踪 Rare Tropical Fish Tracking

实时监测世界中稀有样式的热带鱼生成，并提醒玩家。  
Real-time monitoring for rare tropical fish variants.

* 可设置发光效果。  
* Includes support for glowing effects.

* 可设置忽略从桶中放出的鱼。  
* Option to ignore fish released from buckets to avoid false alarms.

### 附魔物品快捷转移 Enchanted Item Quick Transfer

适用于整理钓鱼物资，可快速将容器中附魔物品转移到背包。  
A powerful tool for organizing fishing loot or massive chest hauls, allowing you to rapidly move enchanted items from a container to your inventory.

* 可设置附魔类别（剑、盔甲、工具等）进行过滤。  
* Category Filtering: Filter by enchantment type, including Swords, Armor, Tools, and more.

* 可设置是否仅转移满级附魔（迅捷潜行除外）。  
* Level Logic: Toggle to transfer only max-level enchantments (with an exception for Swift Sneak).

* 可设置排除诅咒附魔。
* 可设置反转筛选条件。  
* Safety Features: Option to exclude items with curses and a toggle to invert all filter conditions.

* 可设置转移附魔书、弓、钓鱼竿或所有附魔物品。  
* Item Support: Targeted transfer for Enchanted Books, Bows, Fishing Rods, or all enchanted items.

### 杂项 Miscellaneous

* 玩家死亡提醒：当玩家死亡，提醒玩家补充末影珍珠。  
* Death Reminder: Sends a notification after respawning to remind you to restock Ender Pearls.

* 伪潜行修改：将 Tweakeroo 中的伪潜行功能修改为：在仅有一格落差时允许玩家前进。  
* Enhanced Fake Sneak: Modifies Tweakeroo's "Fake Sneak" functionality to allow players to walk off a ledge if there is only a 1-block height difference.

## 前置模组 Dependencies

[malilib](https://github.com/sakura-ryoko/malilib)

[tweakeroo](https://github.com/sakura-ryoko/tweakeroo)