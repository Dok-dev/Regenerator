/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.draksterau.Regenerator.commands;

import com.draksterau.Regenerator.Handlers.RConfig;
import com.draksterau.Regenerator.Handlers.RWorld;
import com.draksterau.Regenerator.Handlers.RLang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author draks
 */
public class reloadCommand {
    
    RegeneratorCommand command;
    
    public reloadCommand(RegeneratorCommand RegeneratorCommand) {
        this.command = RegeneratorCommand;
    }
    
    public void doCommand() {
        if (command.plugin.isParseActive) {
            command.sender.sendMessage(ChatColor.RED + "You cannot reload regenerator when the regeneration task is active...");
            return;
        }
       command.sender.sendMessage(ChatColor.GRAY + "Unloading integrations...");
       command.plugin.availableIntergrations.clear();
       command.plugin.loadedIntegrations.clear();

/*       for (RWorld world : command.plugin.loadedWorlds) {
           world.loadData();
           world.validateWorld();
       }*/
       command.sender.sendMessage(ChatColor.GRAY + "Cancelling all regen tasks...");
        Bukkit.getScheduler().cancelTasks(command.plugin);

        // Database disconnecting
        try { RConfig.statementSQLcon.close(); } catch(SQLException sqlSe) { command.sender.sendMessage(ChatColor.RED + sqlSe.getMessage()); }
        try { RConfig.connectionSQL.close(); } catch(SQLException sqlSe) { command.sender.sendMessage(ChatColor.RED + sqlSe.getMessage()); }
        RConfig.connectionSQL = null;
        command.plugin.loadedWorlds = new ArrayList<RWorld>();

       // Reinitialises configuration.
       command.plugin.config.loadData();
       command.plugin.config.validateConfig();
       command.plugin.lang = new RLang(command.plugin, command.plugin.config.language); // Reinitialises language.
        command.plugin.onEnable();
        if (command.plugin.isEnabled()) {
            command.sender.sendMessage(ChatColor.GREEN + "Regenerator has been reloaded!");
        } else {
            command.sender.sendMessage(ChatColor.RED + "Regenerator failed to reload, a restart may be required!");
        }
    }
}
