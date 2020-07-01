package me.resently.hauth.manager;


import me.resently.hauth.hAuth;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {
    //File Setup
    public File userdata;
    //FileConfig Setup
    public FileConfiguration userdatacfg;
    hAuth plugin;

    public ConfigManager(hAuth plugin) {
        this.plugin = plugin;
    }

    public void setup() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        userdata = new File(plugin.getDataFolder(), "userdata.yml");
        if (!userdata.exists()) {
            userdata.getParentFile().mkdirs();
            plugin.saveResource("userdata.yml", false);
        }

        userdatacfg = YamlConfiguration.loadConfiguration(userdata);
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "userdata.yml was loaded");
    }

    public FileConfiguration getUserData() {
        return userdatacfg;
    }

    public void saveuserdata() {
        try {
            userdata.createNewFile();
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage("Could not save userdata.yml " + e);
        }
    }
}

