package me.shanodekono.nostrip.commands;

import me.shanodekono.nostrip.utils.ConfigUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class NoStripCommand implements CommandExecutor {

    private ConfigUtils cfgUtils;

    public NoStripCommand(ConfigUtils configUtils) {
        cfgUtils = configUtils;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length != 1) {
            if (cfgUtils.allowHelpMenu) {
                sender.sendMessage(cfgUtils.color(""));
                sender.sendMessage(cfgUtils.color("                &c&l⦸ &4&lNo&c&lStrip &c&l⦸"));
                sender.sendMessage(cfgUtils.color(""));
                sender.sendMessage(cfgUtils.color("&c/nostrip toggle"));
                sender.sendMessage(cfgUtils.color(""));
                sender.sendMessage(cfgUtils.color("&e- &oTurns Log Stripping/Debarking &a&oOn&e&o|&c&oOff"));
                sender.sendMessage(cfgUtils.color
                        ("&e- &oMust be toggled &a&oon/enabled &e&oto strip with permitted axes"));
                sender.sendMessage(cfgUtils.color
                        ("&e- &oToggle &c&ooff/disabled&e&o = log stripping &c&oOff &e&ofor all axes"));
                sender.sendMessage(cfgUtils.color(""));

                if (sender.hasPermission("nostrip.reload")) {
                    sender.sendMessage(cfgUtils.color("&c/nostrip reload"));
                    sender.sendMessage(cfgUtils.color(""));
                    sender.sendMessage(cfgUtils.color("&e- &oReloads The Config File"));
                    sender.sendMessage(cfgUtils.color(""));
                }
                return true;
            }

            sender.sendMessage(cfgUtils.color(cfgUtils.prefix + cfgUtils.unknownCommand));
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("nostrip.reload")) {
                cfgUtils.reloadConfig(sender);
            }
            String permission;
            if (!sender.hasPermission("nostrip.reload")) {
                permission = "&cnostrip.reload";
                sender.sendMessage(cfgUtils.color(cfgUtils.prefix + cfgUtils.noReloadPermission
                        .replace("{permission}", permission)));
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("toggle")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(cfgUtils.color(cfgUtils.prefix + cfgUtils.mustBePlayer));
                return true;
            }

            Player player = (Player) sender;
            String permission;
            if (!player.hasPermission("nostrip.toggle")) {
                permission = "&cnostrip.toggle";
                player.sendMessage(cfgUtils.color(cfgUtils.prefix + cfgUtils.noTogglePermission
                        .replace("{permission}", permission)));
                return true;
            }

            // If player isn't in toggle list which prevents stripping, add them
            if (!cfgUtils.toggle.contains(player.getUniqueId())) {
                cfgUtils.toggle.add(player.getUniqueId());
                player.sendMessage(cfgUtils.color(cfgUtils.prefix + cfgUtils.toggleMessage
                        .replace("{toggle}", cfgUtils.disabled)));
                return true;
            }
            // Player removed from toggle means to allow stripping
            if (cfgUtils.toggle.contains(player.getUniqueId())) {
                cfgUtils.toggle.remove(player.getUniqueId());
                player.sendMessage(cfgUtils.color(cfgUtils.prefix + cfgUtils.toggleMessage
                        .replace("{toggle}", cfgUtils.enabled)));
                return true;
            }
        }
        sender.sendMessage(cfgUtils.color(cfgUtils.prefix + cfgUtils.unknownCommand));
        return true;
    }

}