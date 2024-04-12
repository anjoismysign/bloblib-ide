package us.mytheria.blobstudio.director;

import us.mytheria.bloblib.entities.GenericManager;
import us.mytheria.blobstudio.BlobStudio;

public class Manager extends GenericManager<BlobStudio, StudioManagerDirector> {

    public Manager(StudioManagerDirector managerDirector) {
        super(managerDirector);
    }
}