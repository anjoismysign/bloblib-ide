package us.mytheria.blobstudio.director;

import us.mytheria.bloblib.entities.GenericManager;
import us.mytheria.blobstudio.BlobStudio;

public class StudioManager extends GenericManager<BlobStudio, StudioManagerDirector> {

    public StudioManager(StudioManagerDirector managerDirector) {
        super(managerDirector);
    }
}