package us.mytheria.blobstudio.director.manager;

import org.bukkit.configuration.ConfigurationSection;
import us.mytheria.bloblib.entities.ConfigDecorator;
import us.mytheria.blobstudio.director.StudioManager;
import us.mytheria.blobstudio.director.StudioManagerDirector;

public class ConfigManager extends StudioManager {
    private boolean tinyDebug;

    public ConfigManager(StudioManagerDirector managerDirector) {
        super(managerDirector);
        reload();
    }

    @Override
    public void reload() {
        ConfigDecorator configDecorator = getPlugin().getConfigDecorator();
        ConfigurationSection settingsSection = configDecorator.reloadAndGetSection("Settings");
        tinyDebug = settingsSection.getBoolean("Tiny-Debug");
    }

    public boolean tinyDebug() {
        return tinyDebug;
    }
}