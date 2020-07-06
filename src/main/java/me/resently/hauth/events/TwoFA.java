package me.resently.hauth.events;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import me.resently.hauth.hAuth;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.UUID;

public class TwoFA implements Listener {
    private ArrayList<UUID> locked = new ArrayList<>();
    hAuth plugin;

    public TwoFA(hAuth plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        if (plugin.getConfig().getBoolean("2FA-users")){
            Player player = e.getPlayer();
            if (!player.hasPlayedBefore()) {
                if (!plugin.getconfigManager.getUserData().contains("authcodes." + player.getUniqueId())) {
                    GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
                    GoogleAuthenticatorKey googleAuthenticatorKey = googleAuthenticator.createCredentials();

                    player.sendMessage(ChatColor.GREEN + "Your Google authenticator code is : " + googleAuthenticatorKey.getKey());
                    player.sendMessage(ChatColor.YELLOW + "Please enter this code in the Google Authenticator App before leaving the server");

                    plugin.getconfigManager.getUserData().set("authcodes." + player.getUniqueId(), googleAuthenticatorKey.getKey());
                    plugin.getconfigManager.saveuserdata();
                } else {
                    locked.add(player.getUniqueId());
                    player.sendMessage(ChatColor.RED + "Open Google Authenticator app and provide the code!");
                }
            }
        }

        if (plugin.getConfig().getBoolean("2FA-staff")){
            Player player = e.getPlayer();
            if (player.hasPermission("2FA.staff")){
                if (!plugin.getconfigManager.getUserData().contains("authcodes." + player.getUniqueId())){
                    GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
                    GoogleAuthenticatorKey googleAuthenticatorKey = googleAuthenticator.createCredentials();

                    player.sendMessage(ChatColor.GREEN + "Your Google authenticator code is : " + googleAuthenticatorKey.getKey());
                    player.sendMessage(ChatColor.YELLOW + "Please enter this code in the Google Authenticator App before leaving the server");

                    plugin.getconfigManager.getUserData().set("authcodes." + player.getUniqueId(), googleAuthenticatorKey.getKey());
                    plugin.getconfigManager.saveuserdata();
                }else {
                    locked.add(player.getUniqueId());
                    player.sendMessage(ChatColor.RED + "Open Google Authenticator app and provide the code!");
                }
            }
        }
    }

    private boolean playercode(Player player, int code){
        String secretkey = plugin.getconfigManager.getUserData().getString("authcodes." + player.getUniqueId());

        GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
        boolean correct = googleAuthenticator.authorize(secretkey, code);

        if (correct){
            locked.remove(player.getUniqueId());
            return true;
        }

        return correct;
    }

    @EventHandler
    public void playerChats(AsyncPlayerChatEvent e){
        Player player = e.getPlayer();
        String message = e.getMessage();

        if (locked.contains(player.getUniqueId())){
            try {
                Integer code = Integer.parseInt(message);
                if (playercode(player, code)){
                    locked.remove(player.getUniqueId());
                    player.sendMessage(ChatColor.GREEN +"Access granted! Welcome" + player.getDisplayName());
                }else {
                    player.sendMessage(ChatColor.RED + "Incorrect or expired code...");
                }
            }catch (Exception exception){
                Bukkit.getConsoleSender().sendMessage("Error: " + exception);
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void playermoves(PlayerMoveEvent e){
        Player player = e.getPlayer();
        if (locked.contains(player.getUniqueId())){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void playerbreaksblock(BlockBreakEvent e){
        Player player = e.getPlayer();
        if (locked.contains(player.getUniqueId())){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void playerplaceblock(BlockPlaceEvent e){
        Player player = e.getPlayer();
        if (locked.contains(player.getUniqueId())){
            e.setCancelled(true);
        }
    }

}
