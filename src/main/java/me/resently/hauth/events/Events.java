package me.resently.hauth.events;

import me.resently.hauth.hAuth;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;


public class Events implements Listener {
    hAuth Plugin;
    public ArrayList<Player> JoinList = new ArrayList<>();
    public ArrayList<Player> JoinedList = new ArrayList<>();

    public Events(hAuth plugin) {
        Plugin = plugin;
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (Plugin.getConfig().getBoolean("inventory-auth")){
            if (!player.hasPermission("hauth.bypass")) {
                String InvTitle = ChatColor.translateAlternateColorCodes('&', Plugin.getConfig().getString("Inventory_Title"));
                if (!JoinedList.contains(player)){
                    JoinList.add(player);

                    if (Plugin.getConfig().getString("Inventory_Title") != null){
                        Inventory AuthInv = Bukkit.createInventory(player, 27, InvTitle);
                        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)5);
                        ItemMeta glassm = glass.getItemMeta();
                        glassm.setDisplayName(ChatColor.GREEN + "Click me");
                        glass.setItemMeta(glassm);
                        Random r = new Random();
                        int o = r.nextInt(25)+1;
                        AuthInv.setItem(o, glass);
                        (new BukkitRunnable() {
                            @Override
                            public void run() {
                                player.openInventory(AuthInv);
                            }
                        }).runTaskLater(this.Plugin, 1L);
                    }else{
                        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "ERROR: Check your config.yml file, Inventory title can't be null/in blank");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player player = e.getPlayer();
        JoinedList.remove(player);
    }

    @EventHandler
    public void clickInventory(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (Plugin.getConfig().getBoolean("inventory-auth")){
            if (JoinList.contains(player)) {
                String InvTitle = ChatColor.translateAlternateColorCodes('&', Plugin.getConfig().getString("Inventory_Title"));
                Inventory AuthInv = Bukkit.createInventory(player, 27, InvTitle);
                if (e.getCurrentItem().getType() == Material.STAINED_GLASS_PANE) {
                    JoinList.remove(player);
                    AuthInv.remove(Material.STAINED_GLASS_PANE);
                    player.closeInventory();
                    JoinedList.add(player);
                }else {
                    e.setCancelled(true);
                }
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void invClose(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        if (Plugin.getConfig().getBoolean("inventory-auth")){
            if (JoinList.contains(player)) {
                String InvTitle = ChatColor.translateAlternateColorCodes('&', Plugin.getConfig().getString("Inventory_Title"));
                if (Plugin.getConfig().getString("Inventory_Title") != null){
                    Inventory AuthInv = Bukkit.createInventory(player, 27, InvTitle);
                    (new BukkitRunnable() {
                        @Override
                        public void run() {
                            AuthInv.remove(Material.STAINED_GLASS_PANE);
                            ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)5);
                            ItemMeta glassm = glass.getItemMeta();
                            glassm.setDisplayName(ChatColor.GREEN + "Click me");
                            Random r = new Random();
                            int o = r.nextInt(25)+1;
                            glass.setItemMeta(glassm);
                            AuthInv.setItem(o, glass);
                            player.openInventory(AuthInv);
                        }
                    }).runTaskLater(this.Plugin, 1L);
                }else{
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "ERROR: Check your config.yml file, Inventory title can't be null/in blank");
                }
            }
        }
    }
}
