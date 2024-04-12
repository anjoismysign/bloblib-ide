package us.mytheria.blobstudio.director;

import org.jetbrains.annotations.NotNull;
import us.mytheria.bloblib.entities.GenericManagerDirector;
import us.mytheria.blobstudio.BlobStudio;
import us.mytheria.blobstudio.director.manager.StudioConfigManager;

public class StudioManagerDirector extends GenericManagerDirector<BlobStudio> {

    public StudioManagerDirector(BlobStudio plugin) {
        super(plugin);
        addManager("ConfigManager",
                new StudioConfigManager(this));
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
    public final StudioConfigManager getConfigManager() {
        return getManager("ConfigManager", StudioConfigManager.class);
    }
}