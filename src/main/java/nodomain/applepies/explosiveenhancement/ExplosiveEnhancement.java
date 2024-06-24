package nodomain.applepies.explosiveenhancement;

import nodomain.applepies.explosiveenhancement.config.Config;
import nodomain.applepies.explosiveenhancement.config.ConfigCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod(modid = "explosiveenhancement", version="1.0.1", useMetadata=true, clientSideOnly=true)

public class ExplosiveEnhancement {

    public static GuiScreen nextScreen = null;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Config.loadConfig(event.getSuggestedConfigurationFile());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new ConfigCommand());
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) return;
        if (nextScreen != null) {
            Minecraft.getMinecraft().displayGuiScreen(nextScreen);
            nextScreen = null;
        }
    }

    public static final String modid = "explosiveenhancement";

    public static int getFrameIndex(int age, int maxAge, int frames) {
        return (age * (frames - 1) / maxAge);
    }
}
