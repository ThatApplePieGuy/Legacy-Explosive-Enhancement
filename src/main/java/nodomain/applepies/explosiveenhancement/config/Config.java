package nodomain.applepies.explosiveenhancement.config;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class Config {

    private static Configuration config;

    private static boolean enabled;
    public static boolean getEnabled() { return enabled; }
    public static void setEnabled(boolean enabled) { Config.enabled = enabled; saveConfig(); }

    private static double scale;
    public static double getScale() { return scale; }
    public static void setScale(double scale) { Config.scale =  Math.floor(scale * 10) / 10; saveConfig(); }

    private static boolean smoke;
    public static boolean getSmoke() { return smoke; }
    public static void setSmoke(boolean smoke) { Config.smoke = smoke; saveConfig(); }

    public static void loadConfig(File file) {
        config = new Configuration(file);
        config.load();

        enabled = config.get("general", "enabled", true).getBoolean();
        scale = config.get("general", "scale", 1.0).getDouble();
        smoke = config.get("general", "smoke", true).getBoolean();
    }

    public static void saveConfig() {
        config.get("general", "enabled", true).setValue(enabled);
        config.get("general", "scale", 1.0).setValue(scale);
        config.get("general", "smoke", true).setValue(smoke);

        if (config.hasChanged()) {
            config.save();
        }
    }

}