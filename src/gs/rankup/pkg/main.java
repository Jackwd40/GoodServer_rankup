package gs.rankup.pkg;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin {

    //    initalizies the econonmy as vault recommends
    private static Economy econ = null;


    @Override
    public void onEnable() {
        getLogger().info("Good Server First Plugin has been enabled");

//        enable economy as vault recommends
        if (!setupEconomy() ) {
            System.out.println("No economy plugin found. Disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        } else if (getServer().getPluginManager().getPlugin("LuckPerms") == null){
            System.out.println("LuckPerms was not found. Disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }


        getCommand("rankup").setExecutor(new rankup());
    }

    //    setup econ as vault recommends
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    @Override
    public void onDisable() {
        getLogger().info("Good Server First Plugin has been disabled");
    }

    //    get method as vault recommends
    public static Economy getEconomy() {
        return econ;
    }



    public static User loadUser(Player player){
        if (!player.isOnline()){
            throw new IllegalStateException("Player is offline.");
        }


        LuckPerms api = LuckPermsProvider.get();
        return api.getUserManager().getUser(player.getUniqueId());
    }
}

