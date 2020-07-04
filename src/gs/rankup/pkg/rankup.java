package gs.rankup.pkg;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.stream.Collectors;


public class rankup implements CommandExecutor {

    private static String gsHeader() {
        return ChatColor.YELLOW + "[" + ChatColor.GOLD + "Good Server" + ChatColor.YELLOW + "]" + " ";
    }

    private static void pay(double amt, Player player, Economy econ) {
        EconomyResponse resp = econ.withdrawPlayer(player, amt);
        if (resp.errorMessage == null){
            player.sendMessage(ChatColor.GREEN + "Paid " + econ.format(amt) + " to the Good Server!");
        } else {
            player.sendMessage(ChatColor.RED + "An error occurred");
        }

    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {


//        import the luckperms API
        LuckPerms api = LuckPermsProvider.get();
//        set the commandsender to be a local player var
        Player player = (Player) commandSender;
//        use LP api to load the user data
        User user = main.loadUser(player);
//        pull the vault economy api
        Economy economy = main.getEconomy();



        double bal = economy.getBalance(player);


//      get all the groups the player is in
//        ex: [default, incognito, president]
        Set<String> groups = user.getNodes().stream()
                .filter(NodeType.INHERITANCE::matches)
                .map(NodeType.INHERITANCE::cast)
                .map(InheritanceNode::getGroupName)
                .collect(Collectors.toSet());

        if (groups.contains("default") && !groups.contains("kind") && bal >= 100){
            pay(100, player, economy);
            user.data().add(Node.builder("group.kind").build());
            Bukkit.broadcastMessage(gsHeader() + ChatColor.WHITE + player.getName() + " just ranked up to" + ChatColor.GREEN + " The Kind" + ChatColor.WHITE + "!");
        } else if (groups.contains("kind") && !groups.contains("honest") && bal >= 5000){
            pay(5000, player, economy);
            user.data().add(Node.builder("group.honest").build());
            Bukkit.broadcastMessage(gsHeader() + ChatColor.WHITE + player.getName() + " just ranked up to" + ChatColor.AQUA + " The Honest" + ChatColor.WHITE + "!");
        } else if (groups.contains("honest") && !groups.contains("humble") && bal >= 100000){
            pay(100000, player, economy);
            user.data().add(Node.builder("group.humble").build());
            Bukkit.broadcastMessage(gsHeader() + ChatColor.WHITE + player.getName() + " just ranked up to" + ChatColor.LIGHT_PURPLE + " The Humble" + ChatColor.WHITE + "!");
        } else if (groups.contains("humble") && !groups.contains("leader") && bal >= 1000000) {
            pay(1000000, player, economy);
            user.data().add(Node.builder("group.leader").build());
            Bukkit.broadcastMessage(gsHeader() + ChatColor.WHITE + player.getName() + " just ranked up to" + ChatColor.DARK_PURPLE + ChatColor.BOLD + " The Leader" + ChatColor.RESET + ChatColor.WHITE + "!");
        } else if (groups.contains("leader") && !groups.contains("good") && bal >= 10000000){
            pay(10000000, player, economy);
            user.data().add(Node.builder("group.good").build());
            Bukkit.broadcastMessage(gsHeader() + ChatColor.WHITE + player.getName() + " just ranked up to" + ChatColor.GOLD + ChatColor.BOLD + " The Good" + ChatColor.RESET + ChatColor.WHITE + "!");
        } else if (groups.contains("good")){
            player.sendMessage(ChatColor.YELLOW + "You already have the highest rank. Thanks so much for playing on our server!");
        } else {
            player.sendMessage( ChatColor.RED + "You don’t have enough money to rank up yet!");
        }



        api.getUserManager().saveUser(user);





        return true;
    }


}

