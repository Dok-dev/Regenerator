/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.draksterau.Regenerator.Handlers;

import com.draksterau.Regenerator.RegeneratorPlugin;
/*import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;*/

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
/* import org.bukkit.configuration.file.FileConfiguration;
 import org.bukkit.configuration.file.YamlConfiguration;*/

import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author draks
 */
public final class RChunk extends RObject {

    public int chunkX;
    public int chunkZ;
    public String worldName;
    public ResultSet resultSQL;
    private String sqlQuery;
    private Statement statementSQLcon;

    // Last activity time (in ms).
    // Default is -1, because a chunk with its activity reset will be 0. We need to be able to target chunks without their value modified to support regen on initial chunk load.
    public long lastActivity = -1;

/*    private File chunkConfigFile;
    private FileConfiguration chunkConfig;*/

    public RChunk(RegeneratorPlugin Regenerator, int chunkX, int chunkZ, String worldName) {
        super(Regenerator);
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.worldName = worldName;
    //    this.loadData();
    }

    public World getWorld() {
        return Bukkit.getServer().getWorld(worldName);
    }

    public Chunk getChunk() { return this.getWorld().getChunkAt(chunkX, chunkZ); }

    public boolean canAutoRegen() {
        RWorld world = new RWorld(this.plugin, this.getWorld());
        boolean isInactive = plugin.utils.validateChunkInactivity(this);
        boolean isWorldAllowing = world.canAutoRegen();
        if (isInactive && isWorldAllowing) return true;
        return false;
    }

    public boolean canManualRegen() {
        RWorld world = new RWorld(this.plugin, this.getWorld());
        return world.canManualRegen();
    }

    public String getWorldName() {
        return this.getWorld().getName();
    }

    public boolean isChunkLoaded() {
        return this.getChunk().isLoaded();
    }

    @Override
    void loadData() {

        // Check SQL connection and make query - activity time for chank
/*        sqlQuery = "SELECT activityTime FROM " + RConfig.sqlTablePrefix + "_" + this.worldName + " WHERE chunkX="+chunkX+" AND chunkZ="+chunkZ+";";
            try {
                statementSQLcon = RConfig.connectionSQL.createStatement();
                resultSQL = statementSQLcon.executeQuery(sqlQuery);

                resultSQL.last();
                if (resultSQL.getRow() > 0) {
                    this.lastActivity = resultSQL.getLong("activityTime");
                }
                resultSQL.close();
                statementSQLcon.close();
            } catch (SQLException sqlEx) {
                this.plugin.utils.throwMessage(MsgType.WARNING, sqlEx.getMessage());
                this.plugin.utils.throwMessage(MsgType.WARNING, "Regenerator database connection error! Stoping...");
                this.plugin.disablePlugin();
                Bukkit.getServer().shutdown();
            }*/

    }

    @Override
    void saveData() {
        //this.loadData();
        //if (this.lastActivity == 0 || this.lastActivity == -1) {sqlQuery = "DELETE FROM " + RConfig.sqlTablePrefix + "_" + this.worldName + " WHERE chunkX="+chunkX+" AND chunkZ="+chunkZ+";";
            sqlQuery = "INSERT INTO "  + RConfig.sqlTablePrefix + "_" + this.worldName + "(chunkX, chunkZ, activityTime) VALUES (" + chunkX+", "+chunkZ+", "+lastActivity+")"
                    +
                    " ON DUPLICATE KEY UPDATE activityTime="+ this.lastActivity + ";";
        try {
            statementSQLcon = RConfig.connectionSQL.createStatement();
            statementSQLcon.executeUpdate(sqlQuery);
            statementSQLcon.close();

        } catch (SQLException sqlEx) {
            this.plugin.utils.throwMessage(MsgType.WARNING, sqlEx.getMessage());
            //plugin.utils.throwMessage(MsgType.SEVERE,String.format(plugin.lang.getForKey("messages.chunkDataSaveFail"), chunkConfigFile.getAbsolutePath(), ex.getMessage()));
            this.plugin.utils.throwMessage(MsgType.WARNING, "WARNING! Regenerator saveData database connection error! Stoping...");
            this.plugin.disablePlugin();
            Bukkit.getServer().shutdown();
        }

        /* chunkConfig = YamlConfiguration.loadConfiguration(chunkConfigFile);
        chunkConfig.set("chunks." + chunkX + "," + chunkZ, this.lastActivity);
        try {
            chunkConfig.save(chunkConfigFile);
        } catch (IOException ex) {
            plugin.utils.throwMessage(MsgType.SEVERE,String.format(plugin.lang.getForKey("messages.chunkDataSaveFail"), chunkConfigFile.getAbsolutePath(), ex.getMessage()));
        }*/
    }

    public void updateActivity() {
        Long oldLastActivity = this.lastActivity;
        this.lastActivity = System.currentTimeMillis();
        this.plugin.utils.throwMessage(MsgType.DEBUG, "Updating activity for chunk : " + this.chunkX + "," + this.chunkZ + " on world: " + worldName + " from : " + oldLastActivity + " to : " + lastActivity + ".");
        this.saveData();
    }

    public void resetActivity() {
        this.plugin.utils.throwMessage(MsgType.DEBUG, "Resetting activity for chunk : " + this.chunkX + "," + this.chunkZ + " on world: " + worldName + ", previous value : " + lastActivity);
        this.lastActivity = 0;
        this.saveData();
    }

    public void addActivity() {
        RWorld world = new RWorld(this.plugin, this.getWorld());
        this.lastActivity = (this.lastActivity - world.regenInterval);
        this.plugin.utils.throwMessage(MsgType.DEBUG, "Add world regen interval to activity time for chunk : " + this.chunkX + "," + this.chunkZ + " on world: " + worldName + " to : " + lastActivity + ".");
        this.saveData();
    }

}