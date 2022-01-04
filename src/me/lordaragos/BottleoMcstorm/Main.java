package me.lordaragos.BottleoMcstorm;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.JavaPlugin;


/**
 * TODO 
`[passwords]
---follow
---`[jail]
---afk
---news
---oprules
----players
^ctf
---emote 
---fakerank
?flipheads
---invincible
---opchat
?roll
---slap
tcolor
---title
---color
---team
---[server]
`[ranks]
---xban
---votemute
---voteban
---votejail
---votekick
---joker
---[tp]
---[summon]
^?cuboid
^?write
^?gun
---kill
^`?[zone]
`[awards]
`tmessage
`inbox
`send
---p2p
tempban

infection
timed messages -> tmessage
remote console o-o
ranks
mail -> inbox, send
bots???
griefer labeling
+undo
+about

To come with ranks:
adminchat
perbuild
pervisit(zones)
temprank




^ Block editing ones
` Config-dependent
? need info on
+ needing sqlite
 *
 * @author kevint.
 *         Created Jun 20, 2012.
 */
@SuppressWarnings({ "javadoc", "unused" })
public class Main extends JavaPlugin{
	public static Main plugin;
	public final Logger logger = Logger.getLogger("Minecraft");
	public final ServerEventHandler eventhandler = new ServerEventHandler(this);
	
	public File mainFile = new File("plugins/Bottle-O-McStorm");
	public File updatefolder = new File(this.mainFile.getAbsolutePath() + "/updates");
	public File configfile = new File("plugins/PlugInn/config.yml");
	public File tmessage_File = new File(this.mainFile.getAbsolutePath() + "/tmessages.txt");
	public FileInputStream read_tmessage;
	public BufferedReader read_stmessage;
	public InputStream stmessagefile;
	public FileOutputStream write_tmessage;
	public FileOutputStream write_stmessage;
	public ArrayList<String> tmessages = new ArrayList<String>();
	
	public Map<Player, Integer> warnings = new HashMap<Player, Integer>();
	
	public String tmessagerip1;
	public String tmessagerip2;
	
	public FileConfiguration blah;
	

	public boolean tmessagesenabled;
	
	public String cheap_msg;
	
	
	
	public boolean moderate = false;
	public boolean possesed = false;
	public boolean emote;
	
	public boolean summonrequestopen = false;
	public boolean summonrequestaccept;
	
	public boolean tprequestopen = false;
	public boolean tprequestaccept;
	
	
	
	
	int freezer = 0;
	int possessor = 0;
	int follower = 0;
	
	public ArrayList<String> freeze_list = new ArrayList<String>();
	public ArrayList<String> possessed_list = new ArrayList<String>();
	public ArrayList<String> followed_list = new ArrayList<String>();
	
	
	
	public ArrayList<String> voice_list = new ArrayList<String>();
	public ArrayList<String> mute_list = new ArrayList<String>();
	public ArrayList<String> voicename_list = new ArrayList<String>();
	
	
	
	public ArrayList<String> nonop_list = new ArrayList<String>();
	public ArrayList<String> op_list = new ArrayList<String>();
	
	
	
	public ArrayList<String> inv_list = new ArrayList<String>();
	public ArrayList<String> jailed_list = new ArrayList<String>();
	public ArrayList<String> afk_list = new ArrayList<String>();
	
	
	
	public ArrayList<String> redteam_list = new ArrayList<String>();
	public ArrayList<String> blueteam_list = new ArrayList<String>();
	public ArrayList<String> greenteam_list = new ArrayList<String>();
	public ArrayList<String> yellowteam_list = new ArrayList<String>();
	
	private int cmdloop;
	private int tmessageloop;
	
	public Location playerlocation;
	
	
	
	
	public int vm_yea_count = 0;
	public int vm_nay_count = 0;
	public boolean votemuteopen = false;
	public boolean votemutetrue;
	
	public int timer = 0;
	public boolean timesup = false;
	
	
	public int vk_yea_count = 0;
	public int vk_nay_count = 0;
	public boolean votekickopen = false;
	public boolean votekicktrue;
	
	
	public int vj_yea_count = 0;
	public int vj_nay_count = 0;
	public boolean votejailopen = false;
	public boolean votejailtrue;
	
	
	public int vb_yea_count = 0;
	public int vb_nay_count = 0;
	public boolean votebanopen = false;
	public boolean votebantrue;
	
	
	Player player;
	Player joker;
	
	public Location jail = new Location(null, 0, 0, 0);
	Random randomgen = new Random();
	public boolean stable_mode;
	public boolean debug;
	
	public boolean disguised = false;
	public int mdloop = -15;
	
	
	
	@Override
	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info("Saving Configuration...");
		getConfig().set("Jail_Location.x", this.jail.getX());
		getConfig().set("Jail_Location.y", this.jail.getY());
		getConfig().set("Jail_Location.z", this.jail.getZ());
		getConfig().set("Timed_Messages_Enabled", this.tmessagesenabled);
		getConfig().set("Custom_Messages.Inv_Msg", this.cheap_msg);
		getConfig().set("Emotes", this.emote);
		this.saveConfig();
		this.logger.info("Saved Configuration Successfully.");
		
		this.logger.info(pdfFile.getName() + " is now disabled.");
	}
	
	@Override
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this.eventhandler, this);
		PluginDescriptionFile pdfFile = this.getDescription();
		
		
		for(Player ppl : getServer().getOnlinePlayers()){
			if(ppl.isOp()){
				this.op_list.add(ppl.getDisplayName());
			} else {
				this.nonop_list.add(ppl.getDisplayName());
			}
		}
		this.cmdloop = this.getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable(){
			
			@Override
			public void run() {
				
				for(String sinvplayer : Main.this.inv_list) {
					Player invplayer = getServer().getPlayer(sinvplayer);
					int currenthealth = invplayer.getHealth();
					int currentstarve = invplayer.getFoodLevel();
					
					if(invplayer.getHealth() <= currenthealth){
						invplayer.setHealth(currenthealth);
					}
					if(invplayer.getFoodLevel() <= currentstarve) {
						invplayer.setFoodLevel(currentstarve);
					}
				}
				for(String sjailedplayer : Main.this.jailed_list) {
					Player jailedplayer = getServer().getPlayer(sjailedplayer);
					jailedplayer.setHealth(1);
					jailedplayer.setFoodLevel(5);
				}
				for(Player player : getServer().getOnlinePlayers()) {
					Main.this.playerlocation = player.getLocation();
					
				}
				for(Player ppl : getServer().getOnlinePlayers()) {
					if(ppl.isOp()) {
						if(Main.this.op_list.contains(ppl.getDisplayName())){
							int x = 1;
						} else {
							Main.this.op_list.add(ppl.getDisplayName());
						}
					} else {
						if(Main.this.nonop_list.contains(ppl.getDisplayName())) {
							int x = 1;
						} else {
							Main.this.nonop_list.add(ppl.getDisplayName());
						}
					}
					if(!plugin.warnings.containsKey(ppl)) {
						plugin.warnings.put(ppl, 0);
					}
					
				}
			}
		}, 60L, 1L);
		
		this.mainFile.mkdir();
		if(this.mainFile.exists()){
			int x = 1;
		} else {
			this.mainFile.mkdir();
		}
		this.logger.info("[" + pdfFile.getName() +"]" +" Loading Configuration.");
		if(this.configfile.exists()){int x = 1;}
		else{
			this.logger.info("[" + pdfFile.getName() +"]" +" Creating Configuration.");
			getConfig().options().copyDefaults(true);
		}
		if(getConfig().getString("Version").equals(null) || getConfig().getStringList("Rules").equals(null)) {
			this.logger.info("[" + pdfFile.getName() +"]" +" Reincarnating Configuration.");
			getConfig().options().copyDefaults(true);
			this.logger.warning("[" + pdfFile.getName() +"]" +" Your config.yml file was just re-written for stablility purposes to comply with the new version");
			this.logger.warning("[" + pdfFile.getName() +"]" +" Since you are upgrading from v0.1, all fields were *set back* to default, sorry about that.");
			this.saveConfig();
		}
		//TODO: keep updating this
		int x = getConfig().getInt("Jail_Location.x");
		int y = getConfig().getInt("Jail_Location.y");
		int z = getConfig().getInt("Jail_Location.z");
		this.jail.setX(x);
		this.jail.setY(y);
		this.jail.setZ(z);
		this.tmessagesenabled = getConfig().getBoolean("Timed_Messages_Enabled");
		this.cheap_msg = getConfig().getString("Custom_Messages.Inv_Msg");
		this.emote = getConfig().getBoolean("Emotes");
		this.stable_mode = getConfig().getBoolean("Stable_Mode");
		List<String> news = getConfig().getStringList("Custom_Messages.News");
		List<String> rules = getConfig().getStringList("Custom_Messages.Rules");
		List<String> oprules = getConfig().getStringList("Custom_Messages.opRules");
		//TODO: keep updating this too
		if(pdfFile.getVersion().equals(getConfig().getString("Version"))){
			this.debug = getConfig().getBoolean("Debug");
			this.saveConfig();
		} else {
			this.configfile.delete();
			this.logger.info("[" + pdfFile.getName() +"]" +" Re-writing Configuration.");
			this.getConfig().options().copyDefaults(true);
			this.saveConfig();
			this.saveDefaultConfig();
			this.logger.warning("[" + pdfFile.getName() +"]" +" Your config.yml file was just re-written for stablility purposes to comply with the new version");
			this.getConfig().set("Version", pdfFile.getVersion().replace("'",""));
			getConfig().set("Jail_Location.x", this.jail.getX());
			getConfig().set("Jail_Location.y", this.jail.getY());
			getConfig().set("Jail_Location.z", this.jail.getZ());
			getConfig().set("Timed_Messages_Enabled", this.tmessagesenabled);
			getConfig().set("Custom_Messages.Inv_Msg", this.cheap_msg);
			getConfig().set("Emotes", this.emote);
			getConfig().set("Custom_Messages.News", news);
			getConfig().set("Custom_Messages.Rules", rules);
			getConfig().set("Custom_Messages.opRules", oprules);
			this.saveConfig();
			this.saveDefaultConfig();
			this.logger.info("[" + pdfFile.getName() +"]" +" New Configuration Updated.");
			
		}
		this.saveConfig();
		this.logger.info("[" + pdfFile.getName() +"]" +" Configuration Loaded Successfully.");
		if (this.stable_mode == false){
			this.logger.warning("[" + pdfFile.getName() +"]" + " Your not running this plugin in a stable setting. If you did not mean to do this, stop the server, and review the config.yml");
		}
		else {
			this.logger.info("[" + pdfFile.getName() +"]" +" Running in Stable Mode");
		}
		if(this.debug) {
			this.logger.warning("[" + pdfFile.getName() +"]" + " Running in debug mode");
		}
		this.logger.info("["+pdfFile.getName()+"] "+pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled.");
		
		/*this.tmessageloop = this.getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable(){
			
			@Override
			public void run() {	
				for(String message : plugin.tmessages) {
					if(plugin.tmessagesenabled == true){
						plugin.getServer().broadcastMessage(message);
					} else {
						int x = 1;
					}
				}
				
			}
		}, 60L, 60L);*/
	}
	
	
	@SuppressWarnings({ "null", "cast"})
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, final String[] args) {
		boolean isplayer;
		World world = null;
		if (sender instanceof Player) {
			this.player = (Player) sender;
			isplayer = true;
			world = this.player.getWorld();
		} else {
			this.player = null;
			
			isplayer = false;
		}
		
		
		String command = commandLabel;
		if (isplayer == true) {
			if(this.jailed_list.contains(this.player.getDisplayName())) {
				// TODO: Get it so ALL cmds aren't available to the jailed.
				this.player.sendMessage("You are jailed. No commands are available to you.");
			} else {
				if(command.equalsIgnoreCase("impersonate")){
					if(this.player.isOp()){
						if (args.length == 0) {
							this.player.sendMessage("/impersonate <player> <message>");
							this.player.sendMessage(ChatColor.RED + "Error: Too few arguments, you need a target player and message.");
							
						} else if(args.length ==1){
							this.player.sendMessage("/impersonate <player> <message>");
							this.player.sendMessage(ChatColor.RED + "Error: Too few arguments, you need a target message.");
						} else if(args.length > 1){
						
							if (this.player.getServer().getPlayer(args[0]) != null){
								Player targetplayer = this.player.getServer().getPlayer(args[0]);
								
								String message = args.toString();
								String msg = message.replace(targetplayer.getDisplayName(), "");
								targetplayer.chat(msg);
							} else {
								this.player.sendMessage(ChatColor.RED + "Error: The player specified is offline, isn't that a shame?");
							}
						}
					} else {
						this.player.sendMessage(ChatColor.RED + "Error: You have no permission to do this.");
					}
				} else if(command.equalsIgnoreCase("BOM-cmds")) {
					this.player.sendMessage("So you be needing commands? Here you go:");
					this.player.sendMessage("http://dev.bukkit.org/server-mods/bottle-o-mcstorm/pages/commands/");
				}
				else if(command.equalsIgnoreCase("smite")){
					if(this.player.isOp()) {
						if(args.length == 0) {
							Block targetblock = this.player.getTargetBlock(null, 50);
							Location location = targetblock.getLocation();
							world.strikeLightning(location);
							world.createExplosion(location, 2);
						} else if(args.length == 1) {
							if (this.player.getServer().getPlayer(args[0]) != null){
								Player targetplayer = this.player.getServer().getPlayer(args[0]);
								Location location = targetplayer.getLocation();
								world.strikeLightning(location);
								world.createExplosion(location, 2);
								this.player.sendMessage(ChatColor.GRAY + "Smiting player: " + targetplayer.getDisplayName());
								targetplayer.sendMessage("Ouch, thats gotta hurt.");
							} else {
								this.player.sendMessage(ChatColor.RED + "Error: The player specified is offline.");
							}
						} else if(args.length > 1){
							this.player.sendMessage(ChatColor.RED + "Error: Too many arguments!");
						}
					} else {
						this.player.sendMessage(ChatColor.RED + "Error: You have no permission to do this.");
					}
				}
				else if(command.equalsIgnoreCase("moderate")){
					if(this.player.isOp()) {
						if(this.moderate == false){
							getServer().broadcastMessage("Chat Moderation is on. Silence the peableans!");
							this.moderate = true;
						} else {
							getServer().broadcastMessage("Chat Moderation is off. All may speak");
							this.moderate = false;
						}
					} else {
						this.player.sendMessage(ChatColor.RED + "Error: You have no permission to do this.");
					}
					
				}
				else if(command.equalsIgnoreCase("server")) {
					if (this.player.isOp()) {
						if (args.length == 0) {
							this.player.sendMessage("--------- Server Tool (1/2) ----------");
							this.player.sendMessage("    shutdown: shuts down the server   ");
							this.player.sendMessage("      reload: reloads the server      ");
							this.player.sendMessage(" newworld <world>: creates a new world");
							this.player.sendMessage("info: display basic server information");
							this.player.sendMessage(" time: displays server real world time");
							this.player.sendMessage("--------------------------------------");
							
						} else if(args.length == 1) {
							this.player.sendMessage(args[0]);
							this.logger.info(args[0]);
							if(args[0].equalsIgnoreCase("shutdown")) {
								getServer().broadcastMessage("[" + ChatColor.RED + this.player.getDisplayName() + ": Stopping the server.]");
								getServer().shutdown();
							} else if(args[0].equalsIgnoreCase("reload")) {
								getServer().broadcastMessage("[" + ChatColor.GOLD + this.player.getDisplayName() + ": Reloading the server.]");
								getServer().reload();
								getServer().broadcastMessage("[" + ChatColor.GOLD + "Reload Successfull.]");
							} else if(args[0].equalsIgnoreCase("info")){
								this.player.sendMessage("Server Name: "+getServer().getName());
								this.player.sendMessage("IP Address: "+getServer().getIp());
								this.player.sendMessage("Port: " + getServer().getPort());
								this.player.sendMessage("Bukkit Version: " + getServer().getBukkitVersion());
								this.player.sendMessage("Max Players: " + getServer().getMaxPlayers());
								
								
							} else if(args[0].equalsIgnoreCase("time")){
								DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
								Date date = new Date();
								this.player.sendMessage(dateFormat.format(date));
							} else if(args[0].equalsIgnoreCase("newworld") && args.length == 2){
								this.player.sendMessage("Not implemented quite yet...Sorry.");
								
							} else if(args[0].equals("2")) {
								this.player.sendMessage("--------- Server Tool (2/2) ----------");
								this.player.sendMessage("     ops: lists Server Operators.     ");
								this.player.sendMessage("    banned: lists banned players.     ");
								this.player.sendMessage("      ipbans: lists banned-ips.       ");
								this.player.sendMessage("        The End: " + getServer().getAllowEnd());
								this.player.sendMessage("      The Nether: "+ getServer().getAllowNether());
								this.player.sendMessage("--------------------------------------");
							} else {
								this.player.sendMessage(ChatColor.RED + "Error: Command not found");
							}
						} else if(args.length > 1){
							this.player.sendMessage(ChatColor.RED + "Error: Too many arguments!");
						}
					} else {
						this.player.sendMessage(ChatColor.RED + "Error: You have no permission to do this.");
					}
				}
				else if(command.equalsIgnoreCase("whois")) {
					if (args.length == 0) {
						this.player.sendMessage(ChatColor.RED + "/whois <player>");
						this.player.sendMessage(ChatColor.RED + "Error: Specify Player name.");
					} else if(args.length == 1) {
						Player p = this.player.getServer().getPlayer(args[0]);
						String playername = p.getDisplayName();
						long lastplayed = p.getLastPlayed();
						int health = p.getHealth();
						int foodlevel = p.getFoodLevel();
						int level = p.getLevel();
						long firstplayed = p.getFirstPlayed();
						boolean isop = p.isOp();
						String ip = p.getAddress().toString();
						boolean isonline = p.isOnline();
						boolean isbanned = p.isBanned();
						
						this.player.sendMessage("Name: "+ playername);
						this.player.sendMessage("First Played: "+ firstplayed);
						this.player.sendMessage("Last Played: "+ lastplayed);
						this.player.sendMessage("Online: " + isonline);
						this.player.sendMessage("IP: " + ip);
						this.player.sendMessage("OP: " + isop);
						this.player.sendMessage("Banned: " + isbanned);
						this.player.sendMessage("");
						if(p.getGameMode() == GameMode.CREATIVE){
							String gamemode = "Creative";
							this.player.sendMessage("Game Mode: " + gamemode);
						} else {
							String gamemode = "Survival";
							this.player.sendMessage("Game Mode: " + gamemode);
						}
						this.player.sendMessage("Health Level: " + health);
						this.player.sendMessage("Food Level: " + foodlevel);
						this.player.sendMessage("Level: " + level);
						
					} else if(args.length > 1) {
						this.player.sendMessage(ChatColor.RED + "Error: Too many arguments!");
					}
				}
				else if(command.equalsIgnoreCase("freeze")) {
					if(this.player.isOp()) {
						if (args.length == 0) {
							this.player.sendMessage(ChatColor.RED + "/freeze <player>");
							this.player.sendMessage(ChatColor.RED + "Error: Specify Player name.");
						} else if(args.length == 1){
							if(this.stable_mode == false){
								if (this.player.getServer().getPlayer(args[0]) != null){
									this.freeze_list.remove(this.player.getDisplayName());
									Player pf = this.player.getServer().getPlayer(args[0]);
									if(this.freeze_list.contains(pf.getDisplayName())){
										this.freeze_list.remove(pf.getDisplayName());
										getServer().broadcastMessage(pf.getDisplayName() + " has been defrosted.");
									} else {
										this.freeze_list.add(pf.getDisplayName());
										getServer().broadcastMessage(pf.getDisplayName() + " has been frozen.");
										this.freeze_list.remove(this.player.getDisplayName());
									}
								} else {
									this.player.sendMessage(ChatColor.RED + "Error: The player specified is offline, isn't that a shame?");
								}
							} else {
								this.player.sendMessage(ChatColor.RED + "Error: This is considered unstable and stable mode is enabled.");
								this.player.sendMessage(ChatColor.RED + "This can be fixed by changing the Stable_Mode field in the config.yml");
							}
						} else if(args.length > 1) {this.player.sendMessage(ChatColor.RED + "Error: Too many arguments!");}
					} else {
						this.player.sendMessage(ChatColor.RED + "Error: You have no permission to do this.");
					}
				}
				// both of these below are purely jokes from McStorm.
				else if(command.equalsIgnoreCase("crashserver")) {
					this.player.kickPlayer("Server Crash! Fatal Error at me.lordaragos.BottleoMcstorm.Main.java:508.");
				}else if(command.equalsIgnoreCase("hacks")) {
					this.player.kickPlayer("No Hacking please....Thank you!");
				}
				//TODO: fix buggyness
				else if(command.equalsIgnoreCase("possess")) {
					if(this.player.isOp()) {
						if(args.length == 0){
							this.player.sendMessage(ChatColor.RED + "/possess <player>");
							this.player.sendMessage(ChatColor.RED + "Error: Specify Player name.");
						} else if (args.length == 1) {
							if(this.stable_mode == false) {
								Player targetp = this.player.getServer().getPlayer(args[0]);
								if(targetp.isOnline() == true){
									if(this.possessed_list.contains(targetp.getDisplayName())) {
										this.possessed_list.remove(this.player.getDisplayName());
										for(Player other : getServer().getOnlinePlayers()){
											other.showPlayer(this.player);
										}
										this.player.showPlayer(targetp);
										Player p = this.player.getServer().getPlayer(args[0]);
										p.sendMessage("You were just possessed by: " + this.player.getDisplayName());
										this.possessed_list.remove(p.getDisplayName());
										this.player.sendMessage("Possesion Released");
										this.getServer().getScheduler().cancelTask(this.possessor);
									} else {
										this.possessed_list.remove(this.player.getDisplayName());
										this.possessed_list.add(targetp.getDisplayName());
										this.player.hidePlayer(targetp);
										for(Player other : getServer().getOnlinePlayers()){
											other.hidePlayer(this.player);
										}
										
										this.possessor = this.getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable(){
											
											@Override
											public void run() {
												Main.this.possessed_list.remove(Main.this.player.getDisplayName());
												Player p = Main.this.player.getServer().getPlayer(args[0]);
												Location playerlocation = Main.this.player.getLocation();
												if(p.isOnline()){
													p.teleport(playerlocation);
												} else {
													Main.this.player.sendMessage("Possessed Player left the game.");
													Main.this.getServer().getScheduler().cancelTask(Main.this.possessor);
													
												}
												
											}
										}, 30L, 1L);
									}
								} else {
									this.player.sendMessage(ChatColor.RED + "Error: Player not online.");
									this.getServer().getScheduler().cancelTask(this.possessor);
									this.player.showPlayer(targetp);
									for(Player other : getServer().getOnlinePlayers()){
										other.showPlayer(this.player);
									}
								}
							} else {
								this.player.sendMessage(ChatColor.RED + "Error: This is considered unstable and stable mode is enabled.");
								this.player.sendMessage(ChatColor.RED + "This can be fixed by changing the Stable_Mode field in the config.yml");
							}
						} else if(args.length > 1) {this.player.sendMessage(ChatColor.RED + "Error: Too many arguments!");}	
					} else {
						this.player.sendMessage(ChatColor.RED + "Error: You have no permission to do this.");
					}
					
				}
				else if(command.equalsIgnoreCase("hide")){
					if(this.player.isOp()) {

						for(Player other : getServer().getOnlinePlayers()){
							other.hidePlayer(this.player);
						}
						getServer().broadcastMessage(this.player.getDisplayName() + " has left the game.");
						this.player.sendMessage("You are now hidden.");
						for(Player ops:getServer().getOnlinePlayers()){
							if(ops.isOp() == true){
								ops.sendMessage(this.player.getDisplayName() + " has hidden from the other players.");
							}
						}
					} else {
						this.player.sendMessage(ChatColor.RED + "Error: You have no permission to do this.");
					}
				}
				else if(command.equalsIgnoreCase("unhide")) {
					if(this.player.isOp()) {
						for(Player other : getServer().getOnlinePlayers()){
							other.showPlayer(this.player);
						}
						
						getServer().broadcastMessage(this.player.getDisplayName() + " has joined the game.");
						this.player.sendMessage("You have been unhidden.");
						for(Player ops:getServer().getOnlinePlayers()){
							if(ops.isOp() == true){
								ops.sendMessage(this.player.getDisplayName() + " is no longer hidden.");
							}
						}
					} else {
						this.player.sendMessage(ChatColor.RED + "Error: You have no permission to do this.");
					}
					
				}
				// TODO: fix me
				else if(command.equalsIgnoreCase("voice")) {
					if(this.player.isOp()) {
						if (args.length == 0) {
							this.player.sendMessage(ChatColor.RED + "/voice <player>");
							this.player.sendMessage(ChatColor.RED + "Error: Specify Player name.");
						} else if (args.length == 1) {
							if (this.stable_mode == false ) {
								if (this.player.getServer().getPlayer(args[0]) != null){
									Player voicedplayer = getServer().getPlayer(args[0]);
									if (this.voice_list.contains("++" + voicedplayer.getDisplayName())){
										for(String nn : this.voicename_list){
											nn.replace("+", "");
											voicedplayer.setDisplayName(nn);
										}
										this.voicename_list.remove("++" + voicedplayer.getDisplayName());
										this.voice_list.remove(voicedplayer.getDisplayName());
									} else {
										this.voice_list.add(voicedplayer.getDisplayName());
										this.voicename_list.add("++" + voicedplayer.getDisplayName());
										voicedplayer.setDisplayName("++" + voicedplayer.getDisplayName());
									}
								} else {
									this.player.sendMessage(ChatColor.RED + "Error: The player specified is offline, isn't that a shame?");
								}
							} else {
								this.player.sendMessage(ChatColor.RED + "Error: This is considered unstable and stable mode is enabled.");
								this.player.sendMessage(ChatColor.RED + "This can be fixed by changing the Stable_Mode field in the config.yml");
							}
						}
					} else {
						this.player.sendMessage(ChatColor.RED + "Error: You have no permission to do this.");
					}
				}
				// TODO: fix me
				else if(command.equalsIgnoreCase("mute")) {
					if (this.player.isOp()) {
						if (args.length == 0) {
							this.player.sendMessage(ChatColor.RED + "/mute <player>");
							this.player.sendMessage(ChatColor.RED + "Error: Specify Player name.");
						} else if (args.length == 1) {
							if (this.stable_mode == false){
								if (getServer().getPlayer(args[0]) != null){
									this.mute_list.remove(this.player.getDisplayName());
									Player mutedplayer = getServer().getPlayer(args[0]);
									if(mutedplayer == this.player) {
										Exception exception = null;
										try {
											throw exception;
										} catch (Exception exception1) {
											exception1.printStackTrace();
										}
										exception.printStackTrace();
									}
									if (this.mute_list.contains(mutedplayer.getDisplayName())){
										mutedplayer.sendMessage("You have been "+ ChatColor.BLACK + "unmuted");
										this.mute_list.remove(mutedplayer.getDisplayName());
									} else {
										mutedplayer.sendMessage("You have been "+ ChatColor.BLACK + "muted");
										this.mute_list.add(mutedplayer.getDisplayName());
										this.mute_list.remove(this.player.getDisplayName());
									}
								} else {
									this.player.sendMessage(ChatColor.RED + "Error: The player specified is offline, isn't that a shame?");
								}
							} else {
								this.player.sendMessage(ChatColor.RED + "Error: This is considered unstable and stable mode is enabled.");
								this.player.sendMessage(ChatColor.RED + "This can be fixed by changing the Stable_Mode field in the config.yml");
							}
						}
					} else {
						this.player.sendMessage(ChatColor.RED + "Error: You have no permission to do this.");
					}
				}
				else if(command.equalsIgnoreCase("warn")) {
					if(this.player.isOp()) {
						if (args.length == 0) {
							this.player.sendMessage(ChatColor.RED + "/warn <player>");
							this.player.sendMessage(ChatColor.RED + "Error: Specify Player name.");
						} else if (args.length == 1) {
							// TODO: load limited warns from config
							int maxwarns = 3;
							Player warnedplayer = this.player.getServer().getPlayer(args[0]);
							warnedplayer.sendMessage("You have recieved a warning from: " + this.player.getDisplayName());
							for (Entry<Player, Integer> entry : this.warnings.entrySet()) {
					            if (entry.getValue().equals(maxwarns)) {
					                Player soontobekicked = entry.getKey();
					                soontobekicked.kickPlayer("You have recieved too many warnings");
					            } else if (entry.getValue().equals(maxwarns-1)) {
					            	Player doublewarnedplayer = entry.getKey();
					                doublewarnedplayer.sendMessage(ChatColor.YELLOW + "You will be kicked the next warning.");
					            }
					        }
						}
					} else {
						this.player.sendMessage(ChatColor.RED + "Error: You have no permission to do this.");
					}
					
				}
				else if(command.equalsIgnoreCase("patrol")) {
					if(this.player.isOp()) {
						int randomnum = this.randomgen.nextInt(this.player.getServer().getOnlinePlayers().length);
						Player nonop = this.player.getServer().getPlayer(this.nonop_list.get(randomnum));
						Location playerloc = nonop.getLocation();
						this.player.teleport(playerloc);
					} else {
						this.player.sendMessage(ChatColor.RED + "Error: You have no permission to do this.");
					}
				}
				else if(command.equalsIgnoreCase("slap")) {
					if (this.player.isOp()) {
						if(args.length == 0){
							this.player.sendMessage(ChatColor.RED + "/slap <player>");
							this.player.sendMessage(ChatColor.RED + "Error: Player not specified, you need to launch SOMEBODY ^_^");
						} else if(args.length == 1){
							if (this.player.getServer().getPlayer(args[0]) != null){
								Player slapplayer = this.player.getServer().getPlayer(args[0]);
								Location spcloc = slapplayer.getLocation();
								spcloc.add(0,1000,0);
								slapplayer.teleport(spcloc);
								this.player.getServer().broadcastMessage(slapplayer.getDisplayName() + " has just been slapped by " + this.player.getDisplayName());
								
							} else {
								this.player.sendMessage(ChatColor.RED + "Error: The player specified is offline, isn't that a shame?");
							}
						}
					} else {
						this.player.sendMessage(ChatColor.RED + "Error: You have no permission to do this.");
					}
				}
				else if(command.equalsIgnoreCase("emote")) {
					if (this.player.isOp()) {
						PluginDescriptionFile pdfFile = this.getDescription();
						if(this.emote == true) {
							this.emote = false;
							this.player.getServer().broadcastMessage("["+pdfFile.getName() +"] Emotes have been turned off");
						} else {
							this.emote = true;
							this.player.getServer().broadcastMessage("["+pdfFile.getName() +"] Emotes have been turned on");
						}
					} else {
						this.player.sendMessage(ChatColor.RED + "Error: You have no permission to do this.");
					}
				}
				else if(command.equalsIgnoreCase("fakerank")) {
					if(this.player.isOp()) {
						if(args.length == 0) {
							this.player.sendMessage(ChatColor.RED + "/fakerank <player>");
							this.player.sendMessage(ChatColor.RED + "Error: Player not specified, or there's no player with the name of _");
						} else if(args.length == 1){
							if (this.player.getServer().getPlayer(args[0]) != null){
								Player target = this.player.getServer().getPlayer(args[0]);
								target.sendMessage("You are now op!");
								this.player.sendMessage("FakeRank message sent to: " + target.getDisplayName());
								
							} else {
								this.player.sendMessage(ChatColor.RED + "Error: The player specified is offline, isn't that a shame?");
							}
						}
					}
				}
				else if(command.equalsIgnoreCase("invincible")) {
					if(this.player.isOp()) {
						String cheaptxt = getConfig().getString("Cheap_Inv_Msg");
						String stoptxt = " is no longer immortal";
						if(this.inv_list.contains(this.player.getDisplayName())) {
							this.inv_list.contains(this.player.getDisplayName());
							this.player.getServer().broadcastMessage(this.player.getDisplayName() + stoptxt);
						} else {
							this.inv_list.add(this.player.getDisplayName());
							this.player.getServer().broadcastMessage(this.player.getDisplayName() + cheaptxt);
						}
					}
					
				}
				else if(command.equalsIgnoreCase("opchat")) {
					if (this.player.isOp()) {
						if(args.length == 0 ){
							this.player.sendMessage(ChatColor.RED + "/opchat <message>");
							this.player.sendMessage(ChatColor.RED + "Error: No message specified. Odd.....");
						} else if(args.length > 0){
							for(String sops : this.op_list) {
								Player op = this.player.getServer().getPlayer(sops);
								op.sendMessage(ChatColor.BLUE + "[OP-CHAT] "+ChatColor.WHITE+"<"+this.player.getDisplayName() + ">: "+ args.toString());
							}
						}
					} else {
						this.player.sendMessage(ChatColor.RED + "Error: You have no permission to do this.");
					}
				}
				else if(command.equalsIgnoreCase("title")) {
					if(this.player.isOp()) {
						if(args.length == 0) {
							this.player.sendMessage(ChatColor.RED + "/title <player> [title]");
							this.player.sendMessage(ChatColor.RED + "Error: Player not specified");
						} else if(args.length == 1) {
							Player tplayer = this.player.getServer().getPlayer(args[0]);
							String tplayeroldname = tplayer.getName();
							tplayer.setDisplayName(tplayeroldname);
							this.player.getServer().broadcastMessage(tplayer.getDisplayName() + " had their title removed");
							this.player.sendMessage("Title has been removed");
						} else if(args.length == 2) {
							Player tplayer = this.player.getServer().getPlayer(args[0]);
							String title = "[" + args[1] + "]";
							String newname = title + tplayer.getDisplayName();
							tplayer.setDisplayName(newname);
							this.player.getServer().broadcastMessage(tplayer.getName() + " has been given the title of " + title);
							this.player.sendMessage("Title set");
							
						}
					} else {
						this.player.sendMessage(ChatColor.RED + "Error: You have no permission to do this.");
					}
				}
				// TODO: fix buggyness \/
				else if(command.equalsIgnoreCase("color")) {
					if(args.length == 0) {
						this.player.sendMessage(ChatColor.RED + "/color <color>");
						this.player.sendMessage(ChatColor.RED + "Error: Color not specified");
					} else if(args.length == 1) {
						String color = args[0];
						ChatColor chatcolor = null;
						if(color.equalsIgnoreCase("black")) {
							chatcolor = ChatColor.BLACK;
						} else if(color.equalsIgnoreCase("purple")) {
							chatcolor = ChatColor.LIGHT_PURPLE;
						} else if(color.equalsIgnoreCase("red")) {
							chatcolor = ChatColor.RED;
						} else if(color.equalsIgnoreCase("yellow")) {
							chatcolor = ChatColor.YELLOW;
						} else if(color.equalsIgnoreCase("green")) {
							chatcolor = ChatColor.GREEN;
						} else if(color.equalsIgnoreCase("blue")) {
							chatcolor = ChatColor.BLUE;
						} else if(color.equalsIgnoreCase("gray")) {
							chatcolor = ChatColor.GRAY;
						} else if(color.equalsIgnoreCase("gold")) {
							chatcolor = ChatColor.GOLD;
						} else if(color.equalsIgnoreCase("")) {
							chatcolor = ChatColor.WHITE;
						} else {
							this.player.sendMessage(ChatColor.RED + "Error: Color specified not found");
						}
						String newname = chatcolor + this.player.getDisplayName();
						this.player.getServer().broadcastMessage(this.player.getDisplayName() + " has set their color to " + chatcolor + color + ChatColor.WHITE);
						this.player.setDisplayName(newname);
					}
				}
				else if(command.equalsIgnoreCase("setjail")) {
					if(this.player.isOp()) {
						Location newjail = this.player.getLocation();
						this.jail = newjail;
						this.player.sendMessage("New Jail Position Set");
						this.player.sendMessage(this.jail.toString());
					} else {
						this.player.sendMessage(ChatColor.RED + "Error: You have no permission to do this.");
					}
					
				}
				else if(command.equalsIgnoreCase("jail")) {
					if(this.player.isOp()) {
						if(args.length == 0) {
							this.player.sendMessage(ChatColor.RED + "/jail <player>");
							this.player.sendMessage(ChatColor.RED + "Error: Player not specified");
						} else if(args.length == 1) {
							if(this.player.getServer().getPlayer(args[0]) != null) {
								Player jailedPlayer = this.player.getServer().getPlayer(args[0]);
								this.jailed_list.add(jailedPlayer.getDisplayName());
								jailedPlayer.teleport(this.jail);
							} else {
								this.player.sendMessage(ChatColor.RED + "Error: The player specified is offline, isn't that a shame?");
							}
						}
					} else {
						this.player.sendMessage(ChatColor.RED + "Error: You have no permission to do this.");
					}
				}
				else if(command.equalsIgnoreCase("follow")) {
					if(this.player.isOp()) {
						if(args.length == 0) {
							this.player.sendMessage(ChatColor.RED + "Error: Player not specified");
						} else if(args.length == 1) {
							if(this.player.getServer().getPlayer(args[0]) != null) {
								Player followedplayer = getServer().getPlayer(args[0]);
								if(this.followed_list.contains(followedplayer.getDisplayName())) {
									this.followed_list.remove(followedplayer.getDisplayName());
									this.player.showPlayer(followedplayer);
									for(Player others : getServer().getOnlinePlayers()) {
										others.showPlayer(this.player);
										if(others.isOp() == true) {
											others.sendMessage(this.player.getDisplayName() + " has stopped following " + followedplayer.getDisplayName());
											
										} else {
											others.sendMessage(this.player.getDisplayName() + " has joined the game.");
										}
									}
									this.getServer().getScheduler().cancelTask(this.follower);
								} else {
									this.followed_list.add(followedplayer.getDisplayName());
									this.player.hidePlayer(followedplayer);
									for(Player others : getServer().getOnlinePlayers()) {
										others.hidePlayer(this.player);
										if(others.isOp() == true){
											others.sendMessage(this.player.getDisplayName() + " has started following " + followedplayer.getDisplayName());
										} else {
											others.sendMessage(this.player.getDisplayName() + " has left the game.");
										}
									}
									this.follower = this.getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable(){
										
										@Override
										public void run() {
											Main.this.followed_list.remove(Main.this.player.getDisplayName());
											Player p = Main.this.player.getServer().getPlayer(args[0]);
											Location playerlocation = p.getLocation();
											if(p.isOnline()){
												Main.this.player.teleport(playerlocation);
											} else {
												Main.this.player.sendMessage("Followed Player left the game.");
												Main.this.getServer().getScheduler().cancelTask(Main.this.follower);
													
											}
												
										}
									}, 30L, 1L);
									
								}
							} else {
								this.player.sendMessage(ChatColor.RED + "Error: The player specified is offline, isn't that a shame?");
							}
						}
					} else {
						this.player.sendMessage(ChatColor.RED + "Error: You have no permission to do this.");
					}
				} else if (command.equalsIgnoreCase("players")) {
					this.player.sendMessage("Online Players:");
					this.player.sendMessage(this.player.getServer().getOnlinePlayers().toString());
				} else if (command.equalsIgnoreCase("afk")) {
					if(this.afk_list.contains(this.player.getDisplayName())) {
						this.afk_list.remove(this.player.getDisplayName());
						this.player.getServer().broadcastMessage(this.player.getDisplayName() + " is no longer AFK.");
					} else {
						this.afk_list.add(this.player.getDisplayName());
						this.player.getServer().broadcastMessage(this.player.getDisplayName() + " is now AFK.");
						
					}
				} else if(command.equalsIgnoreCase("team")) {
					boolean helmets = this.getConfig().getBoolean("Games.Teams.Change_Helmet");
					// TODO: fix me!
					String redname = ChatColor.RED+this.player.getDisplayName();
					String bluename = ChatColor.BLUE+this.player.getDisplayName();
					String greenname = ChatColor.GREEN+this.player.getDisplayName();
					String yellowname = ChatColor.YELLOW+this.player.getDisplayName();
					if(args.length == 0) {
						if(this.redteam_list.contains(this.player.getDisplayName())) {
							this.redteam_list.remove(this.player.getDisplayName());
							this.player.setDisplayName(this.player.getName());
							this.player.getServer().broadcastMessage(this.player.getDisplayName() + " has left the " + ChatColor.RED + "Red Team!");
							if(helmets) {
								this.player.getInventory().setHelmet(new ItemStack(0));
							}
						} else if (this.blueteam_list.contains(this.player.getDisplayName())) {
							this.blueteam_list.remove(this.player.getDisplayName());
							this.player.setDisplayName(this.player.getName());
							this.player.getServer().broadcastMessage(this.player.getDisplayName() + " has left the " + ChatColor.BLUE + "Blue Team!");
							if(helmets) {
								this.player.getInventory().setHelmet(new ItemStack(0));
							}
						} else if (this.greenteam_list.contains(this.player.getDisplayName())) {
							this.greenteam_list.remove(this.player.getDisplayName());
							this.player.setDisplayName(this.player.getName());
							this.player.getServer().broadcastMessage(this.player.getDisplayName() + " has left the " + ChatColor.GREEN + "Green Team!");
							if(helmets) {
								this.player.getInventory().setHelmet(new ItemStack(0));
							}
						} else if (this.yellowteam_list.contains(this.player.getDisplayName())) {
							this.yellowteam_list.remove(this.player.getDisplayName());
							this.player.setDisplayName(this.player.getName());
							this.player.getServer().broadcastMessage(this.player.getDisplayName() + " has left the " + ChatColor.YELLOW + "Yellow Team!");
							if(helmets) {
								this.player.getInventory().setHelmet(new ItemStack(0));
							}
						} else {
							this.player.sendMessage(ChatColor.RED + "Error: You are not on a team.");
						}
					} else if(args.length == 1) {
						if(args[0].equals("red")) {
							this.player.getServer().broadcastMessage(this.player.getDisplayName() + " has joined the " + ChatColor.RED + "Red Team!");
							this.player.setDisplayName(ChatColor.RED +"[RED]"+ this.player.getDisplayName() + ChatColor.WHITE);
							this.redteam_list.add(this.player.getDisplayName());
							//14
							if (helmets) {
								this.player.getInventory().setHelmet(new ItemStack(Material.WOOL, 1, (short) 14));
							}
						} else if(args[0].equals("blue")) {
							this.player.getServer().broadcastMessage(this.player.getDisplayName() + " has joined the " + ChatColor.BLUE + "Blue Team!");
							this.player.setDisplayName(ChatColor.BLUE +"[BLUE]"+ this.player.getDisplayName() + ChatColor.WHITE);
							this.blueteam_list.add(this.player.getDisplayName());
							//11
							if (helmets) {
								this.player.getInventory().setHelmet(new ItemStack(Material.WOOL, 1, (short) 11));
							}
						} else if(args[0].equals("green")) {
							this.player.getServer().broadcastMessage(this.player.getDisplayName() + " has joined the " + ChatColor.GREEN + "Green Team!");
							this.player.setDisplayName(ChatColor.GREEN +"[GREEN]"+ this.player.getDisplayName() + ChatColor.WHITE);
							this.greenteam_list.add(this.player.getDisplayName());
							//13
							if (helmets) {
								this.player.getInventory().setHelmet(new ItemStack(Material.WOOL, 1, (short) 13));
							}
						} else if(args[0].equals("yellow")) {
							this.player.getServer().broadcastMessage(this.player.getDisplayName() + " has joined the " + ChatColor.YELLOW + "Yellow Team!");
							this.player.setDisplayName(ChatColor.YELLOW +"[YELLOW]"+ this.player.getDisplayName() + ChatColor.WHITE);
							this.yellowteam_list.add(this.player.getDisplayName());
							//4
							if (helmets) {
								this.player.getInventory().setHelmet(new ItemStack(Material.WOOL, 1, (short) 4));
							}
							
						} else {
							this.player.sendMessage(ChatColor.RED + "Error: Team Color specified not found.");
						}
					} else {
						this.player.sendMessage(ChatColor.RED + "Error: Too many arguments!");
					}
				} else if(command.equalsIgnoreCase("summon")) {
					if(this.player.isOp()) {
						if(args.length == 0) {
							this.player.sendMessage(ChatColor.RED + "/summon <player>");
							this.player.sendMessage(ChatColor.RED + "Error: No Player specified.");
						} else if(args.length == 1){
							Player summonedplayer = this.player.getServer().getPlayer(args[0]);
							Location ploc = this.player.getLocation();
							this.player.sendMessage("Summoning " + summonedplayer.getDisplayName() + "...");
							summonedplayer.teleport(ploc);
							summonedplayer.sendMessage("You have been summoned by " + this.player.getDisplayName());
						} 
					} else {
						this.player.sendMessage(ChatColor.RED + "Error: You have no permission to do this.");
					}
				} else if(command.equalsIgnoreCase("summonrequest")) {
					if(args.length == 0) {
						this.player.sendMessage(ChatColor.RED + "/summonrequest <player>");
						this.player.sendMessage(ChatColor.RED + "Error: No Player specified.");
					} else if(args.length == 1) {
						if(this.player.getServer().getPlayer(args[0]) != null) {
							Player summonedplayer = this.player.getServer().getPlayer(args[0]);
							this.player.sendMessage("Sending Request...");
							summonedplayer.sendMessage("Summoning Request:");
							summonedplayer.sendMessage(this.player.getDisplayName() + " is requesting your summoning.");
							summonedplayer.sendMessage("Type /summonaccept to Accept, /summondeny to Deny.");
							this.summonrequestopen = true;
							if(this.summonrequestaccept == true) {
								this.player.sendMessage(summonedplayer.getDisplayName() + " has accepted the request. Summoning...");
								Location ploc = this.player.getLocation();
								summonedplayer.teleport(ploc);
								this.summonrequestopen = false;
								this.summonrequestaccept = (Boolean) null;
							} else {
								this.player.sendMessage(summonedplayer.getDisplayName() + " has denied your request");
								this.summonrequestaccept = (Boolean) null;
								this.summonrequestopen = false;
							}
						} else {
							this.player.sendMessage(ChatColor.RED + "Error: The player specified is offline, isn't that a shame?");
						}
						
					}
				} else if(command.equalsIgnoreCase("tp")) {
					if(this.player.isOp()) {
						if(args.length == 0){
							this.player.sendMessage(ChatColor.RED + "/tp <player>");
							this.player.sendMessage(ChatColor.RED + "Error: No Player specified.");
						} else if(args.length == 1){
							Player tpplayer = this.player.getServer().getPlayer(args[0]);
							Location tploc = tpplayer.getLocation();
							this.player.sendMessage("Teleporting to player...");
							tpplayer.sendMessage(this.player.getDisplayName() + " is teleporting to your location.");
							this.player.teleport(tploc);
						}
					} else {
						this.player.sendMessage(ChatColor.RED + "Error: You have no permission to do this.");
					}
				} else if(command.equalsIgnoreCase("tprequest")) {
					if(args.length == 0) {
						this.player.sendMessage(ChatColor.RED + "/tprequest <player>");
						this.player.sendMessage(ChatColor.RED + "Error: No Player specified.");
					} else if(args.length == 1){
						if(this.player.getServer().getPlayer(args[0]) != null) {
							Player tpplayer = this.player.getServer().getPlayer(args[0]);
							this.player.sendMessage("Sending Request...");
							tpplayer.sendMessage("Teleport Request:");
							tpplayer.sendMessage(this.player.getDisplayName() + " is requesting your teleporting.");
							tpplayer.sendMessage("Type /tpaccept to Accept, /tpdeny to Deny.");
							this.tprequestopen = true;
							if(this.tprequestaccept == true) {
								this.player.sendMessage(tpplayer.getDisplayName() + " has accepted the request. Summoning...");
								Location ploc = this.player.getLocation();
								tpplayer.teleport(ploc);
								this.tprequestopen = false;
								this.tprequestaccept = (Boolean) null;
							} else {
								this.player.sendMessage(tpplayer.getDisplayName() + " has denied your request");
								this.tprequestaccept = (Boolean) null;
								this.tprequestopen = false;
							}
						} else {
							this.player.sendMessage(ChatColor.RED + "Error: The player specified is offline, isn't that a shame?");
						}
					}
				}
				
				
				
				
				else if(command.equalsIgnoreCase("summonaccept")) {
					if(this.summonrequestopen == true) {
						this.player.sendMessage("Summon Accepted. Notifying Sender...");
						this.summonrequestaccept = true;
					} else {
						this.player.sendMessage("Summon request is not open.");
					}
				} else if(command.equalsIgnoreCase("summondeny")) {
					if(this.summonrequestopen == true) {
						this.player.sendMessage("Summoning Denied. Notifying Sender...");
						this.summonrequestaccept = false;
					} else {
						this.player.sendMessage("Summon request is not open.");
					}
				} else if(command.equalsIgnoreCase("tpaccept")) {
					if(this.tprequestopen == true) {
						this.player.sendMessage("Teleport Accepted. Notifying Sender...");
						this.summonrequestaccept = true;
					} else {
						this.player.sendMessage("Teleport request is not open.");
					}
				} else if(command.equalsIgnoreCase("tpdeny")) {
					if(this.tprequestopen == true) {
						this.player.sendMessage("Teleport Denied. Notifying Sender...");
						this.summonrequestaccept = false;
					} else {
						this.player.sendMessage("Teleport request is not open.");
					}
				}
				
				
				
				else if(command.equalsIgnoreCase("xban")) {
					if(this.player.isOp()) {
						if(args.length == 0) {
							this.player.sendMessage(ChatColor.RED + "/xban <player>");
							this.player.sendMessage(ChatColor.RED + "Error: You gotta have someone to xban silly.");
						} else if(args.length == 1) {
							Player xbanplayer = this.player.getServer().getPlayer(args[0]);
							String xbanplayerip  = xbanplayer.getAddress().toString();
							xbanplayerip.replace("/", "");
							String [] ipandport = xbanplayerip.split(":");
							String ip = ipandport[0].replace("/", "");
							this.player.getServer().banIP(ip);
							xbanplayer.setBanned(true);
							this.player.getServer().broadcastMessage(getConfig().getString("Custom_Messages.Banning.Server_Msg"));
							this.player.getServer().broadcastMessage(ChatColor.BLACK + xbanplayer.getDisplayName() + ChatColor.WHITE + " has been banned by " + ChatColor.YELLOW + this.player.getDisplayName());
							xbanplayer.kickPlayer(getConfig().getString("Custom_Messages.Banning.Kick_Reason"));
						}
					} else {
						this.player.sendMessage(ChatColor.RED + "Error: You have no permission to do this.");
					}
				} else if(command.equalsIgnoreCase("votemute") && this.stable_mode == false) {
					if(args.length == 0) {
						this.player.sendMessage(ChatColor.RED + "/votemute <player>");
						this.player.sendMessage(ChatColor.RED + "Error: I think you need a player to votemute, just a little bit.");
					} else if(args.length == 1){
						if(this.player.getServer().getPlayer(args[0]) != null) {
							Player votemute = this.player.getServer().getPlayer(args[0]);
							this.player.getServer().broadcastMessage(this.player.getDisplayName() + " has issued a VoteMute on " + votemute.getDisplayName());
							this.player.getServer().broadcastMessage("Type /accept to Accept, /deny to Deny");
							this.votemuteopen = true;
							this.timer = 60;
							if (this.votemuteopen == true) {
								this.getServer().getScheduler()
										.scheduleAsyncRepeatingTask(this, new Runnable()  {
											@Override
											public void run() {
												if (plugin.timer != -1) {
													if (plugin.timer != 0) {
														plugin.timer--;
													} else {
														plugin.timesup = true;
														plugin.timer--;
													}
												} else {
													plugin.votemuteopen = false;
												}
											}
										}, 0L, 20L);
							}
							if(this.timesup == true && this.votemuteopen == false) {
								this.player.getServer().broadcastMessage("The Vote is in!");
								if(this.vm_yea_count >= this.vm_nay_count) {
									this.votemutetrue = true;
								} else {
									this.votemutetrue = false;
								}
								if(this.votemutetrue == true){
									this.player.getServer().broadcastMessage("The Player is to be muted!");
									this.player.getServer().broadcastMessage("Y: " + this.vm_yea_count + " N: " + this.vm_nay_count);
									votemute.sendMessage("You have been "+ ChatColor.BLACK + "muted");
									this.mute_list.add(votemute.getDisplayName());
									this.votemutetrue = (Boolean) null;
									this.votemuteopen = false;
									this.vm_yea_count = 0;
									this.vm_nay_count = 0;
									this.timer = 0;
								} else {
									this.player.getServer().broadcastMessage("The Player is not to be muted!");
									this.player.getServer().broadcastMessage("Y: " + this.vm_yea_count + " N: " + this.vm_nay_count);
									this.votemutetrue = (Boolean) null;
									this.votemuteopen = false;
									this.vm_yea_count = 0;
									this.vm_nay_count = 0;
									this.timer = 0;
								}
								
							}
						} else {
							this.player.sendMessage(ChatColor.RED + "Error: The player specified is offline, isn't that a shame?");
						}
					}
				} else if(command.equalsIgnoreCase("accept")) {
					if(this.votemuteopen == true) {
						this.vm_yea_count += 1;
						this.player.sendMessage("Vote to Accept Entered.");
					} else if(this.votekickopen == true){
						this.vk_yea_count += 1;
						this.player.sendMessage("Vote to Accept Entered.");
					} else if(this.votejailopen == true){
						this.vj_yea_count += 1;
						this.player.sendMessage("Vote to Accept Entered.");
					} else if(this.votebanopen == true){
						this.vb_yea_count += 1;
						this.player.sendMessage("Vote to Accept Entered.");
					} else {
						this.player.sendMessage(ChatColor.RED + "Error: A Vote is not open currently.");
					}
				} else if(command.equalsIgnoreCase("deny")) {
					if(this.mdloop != -15) {
						plugin.getServer().getScheduler().cancelTask(this.mdloop);
					}
					if(this.votemuteopen == true) {
						this.vm_nay_count += 1;
						this.player.sendMessage("Vote to Deny Entered.");
					} else if(this.votekickopen == true){
						this.vk_nay_count += 1;
						this.player.sendMessage("Vote to Deny Entered.");
					} else if(this.votejailopen == true){
						this.vj_nay_count += 1;
						this.player.sendMessage("Vote to Deny Entered.");
					} else if(this.votebanopen == true){
						this.vb_yea_count += 1;
						this.player.sendMessage("Vote to Deny Entered.");
					} else {
						this.player.sendMessage(ChatColor.RED + "Error: A Vote is not open currently.");
					}
				} else if(command.equalsIgnoreCase("votekick")&& this.stable_mode == false) {
					// TODO: fix me!
					if(args.length == 0) {
						this.player.sendMessage(ChatColor.RED + "/votekick <player>");
						this.player.sendMessage(ChatColor.RED + "Error: You'll need a player to votekick, trust me.");
					} else if(args.length == 1){
						if(this.player.getServer().getPlayer(args[0]) != null) {
							Player votekick = this.player.getServer().getPlayer(args[0]);
							this.player.getServer().broadcastMessage(this.player.getDisplayName() + " has issued a VoteKick on " + votekick.getDisplayName());
							this.player.getServer().broadcastMessage("Type /accept to Accept, /deny to Deny");
							this.votekickopen = true;
							this.timer = 1000;
							this.timer = this.timer - 1;
							if(this.timer == -100) {
								this.player.getServer().broadcastMessage("The Vote is in!");
								if(this.vk_yea_count >= this.vk_nay_count) {
									this.votekicktrue = true;
								} else {
									this.votekicktrue = false;
								}
								if(this.votekicktrue == true){
									this.player.getServer().broadcastMessage("The Player is to be kicked!");
									this.player.getServer().broadcastMessage("Y: " + this.vk_yea_count + " N: " + this.vk_nay_count);
									votekick.kickPlayer("You have been kicked by the masses!");
									this.votekicktrue = (Boolean) null;
									this.votekickopen = false;
									this.vk_yea_count = 0;
									this.vk_nay_count = 0;
									this.timer = 0;
								} else {
									this.player.getServer().broadcastMessage("The Player is not to be kicked!");
									this.player.getServer().broadcastMessage("Y: " + this.vk_yea_count + " N: " + this.vk_nay_count);
									this.votekicktrue = (Boolean) null;
									this.votekickopen = false;
									this.vk_yea_count = 0;
									this.vk_nay_count = 0;
									this.timer = 0;
								}
								
							}
						} else {
							this.player.sendMessage(ChatColor.RED + "Error: The player specified is offline, isn't that a shame?");
						}
					}
				} else if(command.equalsIgnoreCase("votejail")&& this.stable_mode == false) {
					// TODO: fix me!
					if(args.length == 0) {
						this.player.sendMessage(ChatColor.RED + "/votejail <player>");
						this.player.sendMessage(ChatColor.RED + "Error: You'll need a player to votejail, trust me.");
					} else if(args.length == 1){
						if(this.player.getServer().getPlayer(args[0]) != null) {
							Player jailp = this.player.getServer().getPlayer(args[0]);
							this.player.getServer().broadcastMessage(this.player.getDisplayName() + " has issued a VoteJail on " + jailp.getDisplayName());
							this.player.getServer().broadcastMessage("Type /accept to Accept, /deny to Deny");
							this.votejailopen = true;
							this.timer = 1000;
							this.timer = this.timer - 1;
							if(this.timer == -100) {
								this.player.getServer().broadcastMessage("The Vote is in!");
								if(this.vj_yea_count >= this.vj_nay_count) {
									this.votejailtrue = true;
								} else {
									this.votejailtrue = false;
								}
								if(this.votejailtrue == true){
									this.player.getServer().broadcastMessage("The Player is to be jailed!");
									this.player.getServer().broadcastMessage("Y: " + this.vj_yea_count + " N: " + this.vj_nay_count);
									this.jailed_list.add(jailp.getDisplayName());
									jailp.teleport(this.jail);
									this.votejailtrue = (Boolean) null;
									this.votejailopen = false;
									this.vj_yea_count = 0;
									this.vj_nay_count = 0;
									this.timer = 0;
								} else {
									this.player.getServer().broadcastMessage("The Player is not to be jailed!");
									this.player.getServer().broadcastMessage("Y: " + this.vj_yea_count + " N: " + this.vj_nay_count);
									this.votejailtrue = (Boolean) null;
									this.votejailopen = false;
									this.vj_yea_count = 0;
									this.vj_nay_count = 0;
									this.timer = 0;
								}
								
							}
						} else {
							this.player.sendMessage(ChatColor.RED + "Error: The player specified is offline, isn't that a shame?");
						}
					}
				} else if(command.equalsIgnoreCase("voteban")&& this.stable_mode == false) {
					// TODO: fix me!
					if(args.length == 0) {
						this.player.sendMessage(ChatColor.RED + "/votejail <player>");
						this.player.sendMessage(ChatColor.RED + "Error: You'll need a player to votejail, trust me.");
					} else if(args.length == 1){
						if(this.player.getServer().getPlayer(args[0]) != null) {
							Player banp = this.player.getServer().getPlayer(args[0]);
							this.player.getServer().broadcastMessage(this.player.getDisplayName() + " has issued a VoteBan on " + banp.getDisplayName());
							this.player.getServer().broadcastMessage("Type /accept to Accept, /deny to Deny");
							this.votebanopen = true;
							this.timer = 1000;
							this.timer = this.timer - 1;
							if(this.timer == -100) {
								this.player.getServer().broadcastMessage("The Vote is in!");
								if(this.vb_yea_count >= this.vb_nay_count) {
									this.votebantrue = true;
								} else {
									this.votebantrue = false;
								}
								if(this.votebantrue == true){
									this.player.getServer().broadcastMessage("The Player is to be banned!!");
									this.player.getServer().broadcastMessage("Y: " + this.vb_yea_count + " N: " + this.vb_nay_count);
									banp.setBanned(true);
									this.votebantrue = (Boolean) null;
									this.votebanopen = false;
									this.vb_yea_count = 0;
									this.vb_nay_count = 0;
									this.timer = 0;
								} else {
									this.player.getServer().broadcastMessage("The Player is not to be banned!");
									this.player.getServer().broadcastMessage("Y: " + this.vb_yea_count + " N: " + this.vb_nay_count);
									this.votebantrue = (Boolean) null;
									this.votebanopen = false;
									this.vb_yea_count = 0;
									this.vb_nay_count = 0;
									this.timer = 0;
								}
								
							}
						} else {
							this.player.sendMessage(ChatColor.RED + "Error: The player specified is offline, isn't that a shame?");
						}
					}
				} else if(command.equalsIgnoreCase("joker")&& this.stable_mode == false) {
					if (this.player.isOp()) {
						if(args.length == 0) {
							this.player.sendMessage(ChatColor.RED + "/joker <player>");
							this.player.sendMessage(ChatColor.RED + "Error: A Player needs to be named Joker!");
						} else if(args.length == 1){
							this.player.sendMessage(ChatColor.RED + "Error: Still being implemented. Sorry");
							/*
							if(args[0].equalsIgnoreCase("none")) {
								this.joker = null;
							} else {
								if(this.player.getServer().getPlayer(args[0]) != null) {
									this.joker = this.player.getServer().getPlayer(args[0]);
								} else {
									this.player.sendMessage(ChatColor.RED + "Error: The player specified is offline, isn't that a shame?");
								}
							}*/
						}
					} else {
						this.player.sendMessage(ChatColor.RED + "Error: You have no permission to do this.");
					}
				} else if(command.equalsIgnoreCase("kill")) {
					if (this.player.isOp()) {
						if(args.length == 0) {
							this.player.sendMessage(ChatColor.RED + "/kill <player>");
							this.player.sendMessage(ChatColor.RED + "Error: A Player needs to be named to kill.");
						} else if(args.length == 1) {
							if(this.player.getServer().getPlayer(args[0]) != null) {
								Player killedplayer = this.player.getServer().getPlayer(args[0]);
								killedplayer.sendMessage("Ouch. Thats gotta hurt.");
								killedplayer.damage(1000);
								this.player.sendMessage("Ouch. Thats gotta hurt.");
							} else {
								this.player.sendMessage(ChatColor.RED + "Error: The player specified is offline, isn't that a shame?");
							}
						}
					} else {
						this.player.sendMessage(ChatColor.RED + "Error: You have no permission to do this.");
					}
				} else if(command.equalsIgnoreCase("p2p")) {
					if(this.player.isOp()) {
						if(args.length == 0) {
							this.player.sendMessage(ChatColor.RED + "/p2p <player1> <player2>");
							this.player.sendMessage(ChatColor.RED + "Error: You need to specify two player");
						} else if(args.length == 1) {
							this.player.sendMessage(ChatColor.RED + "/p2p <player1> <player2>");
							this.player.sendMessage(ChatColor.RED + "Error: You need to specify two player");
						} else if(args.length == 2) {
							if(this.player.getServer().getPlayer(args[0]) != null) {
								if(this.player.getServer().getPlayer(args[1]) != null) {
									Player player1 = this.player.getServer().getPlayer(args[0]);
									Player player2 = this.player.getServer().getPlayer(args[1]);
									Location player2loc = player2.getLocation();
									player1.sendMessage("Sending you to " + player2.getDisplayName());
									player2.sendMessage("Coming to you: " + player1.getDisplayName());
									player1.teleport(player2loc);
								} else {
									this.player.sendMessage(ChatColor.RED + "Error: One of the players specified is offline, isn't that a shame?");
								}
							} else {
								this.player.sendMessage(ChatColor.RED + "Error: One of the players specified is offline, isn't that a shame?");
							}
						}
					} else {
						this.player.sendMessage(ChatColor.RED + "Error: You have no permission to do this.");
					}
				} else if(command.equalsIgnoreCase("news")) {
					List<String> newslist = getConfig().getStringList("Custom_Messages.News");
					for(String line : newslist){
						this.player.sendMessage(line);
					}
				} else if(command.equalsIgnoreCase("oprules")) {
					if (this.player.isOp()) {
						List<String> oprules = getConfig().getStringList("Custom_Messages.opRules");
						for(String line : oprules){
							this.player.sendMessage(line);
						}
					} else {
						this.player.sendMessage(ChatColor.RED + "Error: You have no permission to do this.");
					}
				} else if(command.equalsIgnoreCase("rules")) {
					List<String> rulelist = getConfig().getStringList("Custom_Messages.Rules");
					for(String line : rulelist){
						this.player.sendMessage(line);
					}
				} else if(command.equalsIgnoreCase("md") && this.player.isOp() && this.debug){
					//mdapi.disguisePlayer(this.player, "zombie");
					this.disguised = true;
					final LivingEntity zombiecloak = world.spawnCreature(this.player.getLocation(), EntityType.ZOMBIE);
					int zombieid = zombiecloak.getEntityId();
					this.player.chat("/hide");
					this.mdloop = this.getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable(){
							@Override
							public void run() {
								if(Main.this.disguised == false) {
									zombiecloak.remove();
									Main.this.getServer().getScheduler().cancelTask(plugin.mdloop);
								} else {
									zombiecloak.teleport(Main.this.player.getLocation());
									
									if(Main.this.player == null) {
										Main.this.disguised = false;
									}
								}
							}
							
					}, 0L, 1L);
					
				}
				else {
					this.player.sendMessage(ChatColor.RED + "Error: Hmm, Command Specified Not Found, odd...");
					if(this.player.isOp()) {
						if(this.stable_mode == false) {
							this.player.sendMessage(ChatColor.BLUE + "Note: You are not running in Stable-Mode.");
						} else {
							this.player.sendMessage(ChatColor.BLUE + "Note: You are running in Stable-Mode.");
						}
					}
				}
			}
		} else {
			ConsoleCommandSender console = (ConsoleCommandSender) sender;
			console.sendMessage(ChatColor.RED + "Error: Commands only available to in-game players for now...sorry about that");
			console.sendMessage(ChatColor.YELLOW + "Note: We'll have an update with Console Commands soon");
		}
		return false;
	}
}

/* old code -- just to be safe
 * 
 * if(this.frozen == false){

this.frozen = true;
final Player p = player.getServer().getPlayer(args[0]);
final Location plocation = p.getLocation();
getServer().broadcastMessage(p.getDisplayName() + " has been frozen.");
//disable blockies here

p.teleport(plocation);
this.freezer = this.getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable(){
	@Override
	public void run() {
		p.teleport(plocation);
	}
}, 60L, 1L);

} else {
Player p = player.getServer().getPlayer(args[0]);
getServer().broadcastMessage(p.getDisplayName() + " has been defrosted.");
this.getServer().getScheduler().cancelTask(this.freezer);
this.frozen = false;
}*/
