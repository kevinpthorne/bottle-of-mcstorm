# [Bottle-O-McStorm](http://web.archive.org/web/20171123075036/https://dev.bukkit.org/projects/bottle-o-mcstorm)

A [CraftBukkit](https://bukkit.org/) plugin developed in late 2012 for Minecraft Servers. 
The project ported many features from Minecraft Classic Servers like McStorm
and McForge into normal Minecraft.

Plugin had 906 downloads

Features that were implemented:
- `/impersonate <player>` - Impersonate a player in chat
- `/smite <player>` - Strikes and kills a player at their current location
- `/slap <player>` - Slaps a player sky-high. Likely falls to death.
- `/moderate` - Blocks all non-admin chat
- `/server <cmd>` - Server tools using McStorm command syntax
- `/whois <player>` - Complete player information
- `/freeze <player>` - Freezes an online player, ported directly from [IceEm](https://github.com/kevinpthorne/IceEm)
- `/crashserver` - The player who invokes jokingly is kicked saying "Server Crash!"
- `/hacks` - Kicks the invoker saying "No hacking please....thank you!"
- `/possess <player>` - Possesses an online player, ported directly from [PossessEm](https://github.com/kevinpthorne/PossessEm)
- `/hide` - Makes it appear as if the invoker has left the game. `/unhide` undoes this.
- `/mute <player>` - Bans player from chatting
- `/warn <player>` - Adds a warning to the player's record
- `/patrol` - Teleports invoker to a random player
- `/emote` - Toggles chat emotes
- `/fakerank <player>` - Sends a message to the player with a fake admin promotion
- `/invincible` - Make invoker invincible
- `/opchat <message>` - Send message to operators (admins) only
- `/title <player> [title]` - Gives player a title to reside next to name in chat
- `/color <color>` - Changes invoker's display name color to color specified
- `/jail <player>` - Teleports player to jail. `/setjail` to set location
- `/follow <player>` - Secretly follows a player
- `/players` - Displays all connected players
- `/afk` - Announces to everyone that the player is away-from-keyboard
- `/team` - Players could organize themselves into 4 different colored teams: red, blue, green, or yellow
- `/summon <player>` - Summons player to invoker
- `/summonrequest <player>` - Asks player to be summoned to invoker. Player can use `/summonaccept` or `/summondeny`
- `/tp <player>` - Teleports invoker to player.
- `/tprequest <player>` - Same logic as `summonrequest`
- `/voteban`, `/votekick`, `/votejail`, `/votemute` - Starts a vote to ban, kick, jail, or mute a player. `/accept` or `/deny` to vote
- `/p2p <player> <player>` - Teleports one player to another

Plugin featured a stable and experimental mode to disable or enable bleeding-edge commands.
