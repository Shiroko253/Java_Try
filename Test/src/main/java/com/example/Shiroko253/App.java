package com.example.Shiroko253;

import net.kyori.adventure.text.Component;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;

public class App extends JavaPlugin implements Listener {
    private FileConfiguration config;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = getConfig();
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("CustomMusicPlayer 已啟動");
    }

    @Override
    public void onDisable() {
        getLogger().info("CustomMusicPlayer 已停用");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK &&
            event.getClickedBlock() != null &&
            event.getClickedBlock().getType() == Material.JUKEBOX) {

            Player player = event.getPlayer();
            openMusicGUI(player);
            event.setCancelled(true);
        }
    }

    private void openMusicGUI(Player player) {
        String guiTitle = config.getString("gui-title", "\u00a7a自訂音樂播放器");
        Inventory gui = Bukkit.createInventory(null, 54, Component.text(guiTitle));

        ItemStack border = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = border.getItemMeta();
        meta.displayName(Component.text(" "));
        border.setItemMeta(meta);
        for (int i = 0; i < 9; i++) gui.setItem(i, border);
        for (int i = 45; i < 54; i++) gui.setItem(i, border);

        File soundsFolder = new File(getDataFolder().getParentFile(), "sounds");
        if (soundsFolder.exists() && soundsFolder.isDirectory()) {
            File[] files = soundsFolder.listFiles((dir, name) -> name.endsWith(".ogg") || name.endsWith(".mp3"));
            if (files != null) {
                int slot = 9;
                for (File f : files) {
                    if (slot >= 45) break; // 避免超過GUI大小
                    ItemStack musicItem = new ItemStack(Material.MUSIC_DISC_13); // 任選唱片圖示
                    ItemMeta musicMeta = musicItem.getItemMeta();
                    musicMeta.displayName(Component.text(f.getName()));
                    musicMeta.lore(Arrays.asList(Component.text("點我播放此音樂")));
                    musicItem.setItemMeta(musicMeta);
                    gui.setItem(slot++, musicItem);
                }
            }
        }

        player.openInventory(gui);
    }
}
