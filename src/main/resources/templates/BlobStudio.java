package us.mytheria.blobstudio;

import us.mytheria.bloblib.managers.BlobPlugin;
import us.mytheria.bloblib.managers.IManagerDirector;
import us.mytheria.blobstudio.director.StudioManagerDirector;

public final class BlobStudio extends BlobPlugin {
    private IManagerDirector proxy;

    @Override
    public void onEnable() {
        StudioManagerDirector director = new StudioManagerDirector(this);
        proxy = director.proxy();
    }

    public IManagerDirector getManagerDirector() {
        return proxy;
    }
}
