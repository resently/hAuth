package me.resently.hauth.utils;

import me.resently.hauth.hAuth;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

public class UpdateChecker {
    private hAuth plugin;
    private int resourceid;

    public UpdateChecker(hAuth plugin, int resourceid){
        this.plugin = plugin;
        this.resourceid = resourceid;
    }

    public void getNewerVersion(Consumer<String> consumer){
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try(InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceid).openStream();
            Scanner scanner = new Scanner(inputStream)){
                if (scanner.hasNext()){
                    consumer.accept(scanner.next());
                }
            }catch(IOException e){
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "An error has ocurred with UpdateChecker, can't find an update! " + e.getMessage());

            }
        });
    }
}
