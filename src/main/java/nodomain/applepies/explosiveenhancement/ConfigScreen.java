package nodomain.applepies.explosiveenhancement;

import nodomain.applepies.explosiveenhancement.config.Config;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiSlider;

public class ConfigScreen extends GuiScreen {
    private static String prettyBool(boolean onOff) {return (onOff) ? "§aOn" : "§cOff";}

    @Override
    public void initGui() {
        super.initGui();

        this.buttonList.add(new GuiButton(0, width / 2 - 49, height / 2 - 48, 98, 20, "Enabled: " + prettyBool(Config.getEnabled())));
        this.buttonList.add(new GuiSlider(1, width / 2 - 49, height / 2 - 24, 98, 20, "Scale: ", "", 0.0, 2.0, Config.getScale(), true, true));
        this.buttonList.add(new GuiButton(2, width / 2 - 49, height / 2, 98, 20, "Smoke: " + prettyBool(Config.getSmoke())));
        this.buttonList.add(new GuiButton(3, width / 2 - 49, height / 2 + 24, 98, 20, "Done"));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                Config.setEnabled(!Config.getEnabled());
                button.displayString = "Enabled: " + prettyBool(Config.getEnabled());
                Config.saveConfig();
                break;
            case 2:
                Config.setSmoke(!Config.getSmoke());
                button.displayString = "Smoke: " + prettyBool(Config.getSmoke());
                Config.saveConfig();
                break;
            case 3:
                this.mc.displayGuiScreen(null);
                break;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(fontRendererObj, "Config", width / 2, height / 2 - 48, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void onGuiClosed() {
        double scale = ((GuiSlider) buttonList.get(1)).getValue();
        Config.setScale(scale);
    }
}
