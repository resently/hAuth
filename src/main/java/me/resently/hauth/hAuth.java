package me.resently.hauth;

import me.resently.hauth.events.Events;
import me.resently.hauth.utils.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class hAuth extends JavaPlugin {
    public PluginManager pm;
    public String Prefix = getConfig().getString("Prefix");
    @Override
    public void onEnable() {
        //Update Checker
        new UpdateChecker(this,80862).getNewerVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + this.getDescription().getName() + " is up to date :D");
            }else{
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + this.getDescription().getName() + " has an update available");
            }
        });

        // Plugin startup logic
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "   __ _____ ");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "  / // / _ |" + ChatColor.GREEN + "    hAuth " + this.getDescription().getVersion());
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + " / _  / __ |" + ChatColor.DARK_GRAY + "    by Resently");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "/_//_/_/ |_|");

        pm = getServer().getPluginManager();
        registerEvents();
        loadConfig();
    }

    public void registerEvents(){
        pm.registerEvents(new Events(this), this);
    }

    public void loadConfig(){
       getConfig().options().copyDefaults(true);
       saveConfig();
    }
}
