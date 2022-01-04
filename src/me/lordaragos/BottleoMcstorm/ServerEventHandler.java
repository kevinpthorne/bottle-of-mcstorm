package me.lordaragos.BottleoMcstorm;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerChatEvent;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * TODO Events blah blah blah
 *
 * @author kevint.
 *         Created Jun 20, 2012.
 */
@SuppressWarnings("javadoc")
public class ServerEventHandler implements Listener {

	public static Main plugin;
	public ServerEventHandler(Main instance){
		plugin = instance;	
	}
	public boolean sendJokerMessage = false;
	public boolean enableJoker = false;
	
	@SuppressWarnings("unused")
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent chat) {
		if(plugin.joker == chat.getPlayer()) {
			this.enableJoker = true;
		}
		if(plugin.moderate == true){
			for(Player ppl : plugin.getServer().getOnlinePlayers()){
				if(plugin.voice_list.contains(ppl.getDisplayName())) {
					chat.setCancelled(false);
				} else {
					if(chat.getMessage().contains("")){
						chat.setCancelled(true);
						Player sender = chat.getPlayer();
						sender.sendMessage("Chat Moderation is on, you may not speak");
					}
				}
			}
		}
		if(this.enableJoker == true) {
			ChatColor r = ChatColor.RED;
			ChatColor b = ChatColor.BLUE;
			ChatColor g = ChatColor.GREEN;
			ChatColor y = ChatColor.YELLOW;
			ChatColor p = ChatColor.LIGHT_PURPLE;
			ChatColor o = ChatColor.GOLD;
			ChatColor a = ChatColor.AQUA;
			ChatColor w = ChatColor.WHITE;
			String pchat = chat.getMessage();
			Player sender = chat.getPlayer();
			this.enableJoker = false;
			plugin.getServer().broadcastMessage(r+"!"+o+"!"+y+"!"+g+"J"+b+"o"+p+"k"+a+"e"+w+"r"+r+"!"+o+"!"+y+"! " + w);
			sender.chat(pchat);
			this.enableJoker = false;
			
		}
		else {int x = 1;}
		for(String mp : plugin.mute_list) {
			if(plugin.mute_list.contains(mp)){
				chat.setCancelled(true);
				Player sender = chat.getPlayer();
				sender.sendMessage("You are muted");
			}
		}
		if(plugin.emote == true) {
			int x = 1;
			/*
			Player sender = chat.getPlayer();
			String message = chat.getMessage();
			if(message.contains(":)")){
				message.replace(":)", "");
				chat.setCancelled(true);
				sender.chat(message);
			}
			if(message.contains(":(")){
				message.replace(":(", "•n•");
				chat.setCancelled(true);
				sender.chat(message);
			}
			if(message.contains(":D")){
				message.replace(":D", "•⌂•");
				chat.setCancelled(true);
				sender.chat(message);
			}*/
		}
	}
	@SuppressWarnings("unused")
	@EventHandler
	public void onBlockChange(BlockBreakEvent blockbreak){
		for(String pf : plugin.freeze_list) {
			blockbreak.setCancelled(true);
		}
		for(String pp : plugin.possessed_list){
			blockbreak.setCancelled(true);
		}
		
	}
	@SuppressWarnings("unused")
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent movement) {
		for(String pf : plugin.freeze_list) {
			movement.setCancelled(true);
		}
		for(String afkp : plugin.afk_list) {
			if(movement.equals(true)) {
				Player afkplayer = movement.getPlayer();
				plugin.afk_list.remove(afkp);
				plugin.getServer().broadcastMessage(afkplayer.getDisplayName() + " is no longer AFK.");
			}
			
		}
		for(Player player : plugin.getServer().getOnlinePlayers()) {
			Location playerlocation1 = player.getLocation();
			if(playerlocation1 == plugin.playerlocation) {
				plugin.afk_list.add(player.getDisplayName());
			}
		}
	}
	@SuppressWarnings({ "unused" })
	@EventHandler
	public void onEntityDamage(EntityDamageEvent owy) {
		for(String invp : plugin.inv_list) {
			owy.setCancelled(true);
		}
	}
	@EventHandler
	public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
		Player p = event.getPlayer();
		if(plugin.jailed_list.contains(p.getDisplayName())) {
			p.sendMessage("You are jailed. No commands are available to you.");
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String rawmotd = plugin.getConfig().getString("Custom_Messages.MOTD");
		String motd;
		if(rawmotd.contains("&p")) {
			motd = rawmotd.replace("&p", player.getDisplayName());
		} else {
			motd = rawmotd;
		}
		player.sendMessage(motd);
		if(player.isOp()) {
			player.sendMessage("[Bottle-O-McStorm]: Stability Mode: " + plugin.getConfig().getBoolean("Stable_Mode"));
		}
	}
}

