package nodomain.applepies.explosiveenhancement.config;

import nodomain.applepies.explosiveenhancement.ExplosiveEnhancement;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

import static nodomain.applepies.explosiveenhancement.config.Config.saveConfig;

public class ConfigCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "explosiveenhancement";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        ExplosiveEnhancement.nextScreen = new ConfigScreen();
        saveConfig();
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}