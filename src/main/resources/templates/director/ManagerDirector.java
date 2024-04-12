package us.mytheria.blobstudio.director;

import org.jetbrains.annotations.NotNull;
import us.mytheria.bloblib.entities.GenericManagerDirector;
import us.mytheria.blobstudio.BlobStudio;
import us.mytheria.blobstudio.director.manager.ConfigManager;

public class ManagerDirector extends GenericManagerDirector<BlobStudio> {

    public ManagerDirector(BlobStudio plugin) {
        super(plugin);
        addManager("ConfigManager",
                new ConfigManager(this));
    }

    /**
     * From top to bottom, follow the order.
     */
    @Override
    public void reload() {
        getConfigManager().reload();
    }

    @Override
    public void unload() {
    }

    @NotNull
    public final ConfigManager getConfigManager() {
        return getManager("ConfigManager", ConfigManager.class);
    }
}