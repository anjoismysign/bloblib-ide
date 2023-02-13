package com.github.anjoismysign.bloblibide.libraries;

import com.github.anjoismysign.bloblibide.entities.ConfigurationSectionAllowed;
import com.github.anjoismysign.bloblibide.entities.DataTyper;
import com.github.anjoismysign.bloblibide.entities.ImportCollection;

import java.util.Set;

public class ImportLib {

    public static void applyConfigurationSectionImports(ImportCollection importCollection, DataTyper dataTyper) {
        Set<String> mapKeys = dataTyper.getMapKeys();
        for (String mapKey : mapKeys) {
            if (mapKey.equals("String"))
                continue;
            try {
                ConfigurationSectionAllowed configurationSectionAllowed = ConfigurationSectionAllowed.fromName(mapKey);
                importCollection.addImport("us.mytheria.bloblib.utilities.MapLib");
            } catch (IllegalArgumentException ignored) {
            }
        }
        Set<String> mapValues = dataTyper.getMapValues();
        for (String mapValue : mapValues) {
            try {
                ConfigurationSectionAllowed configurationSectionAllowed = ConfigurationSectionAllowed.fromName(mapValue);
                importCollection.addImport("us.mytheria.bloblib.utilities.ConfigurationSectionLib");
            } catch (IllegalArgumentException ignored) {
            }
        }
        Set<String> listValues = dataTyper.getListValues();
        for (String listValue : listValues) {
            try {
                ConfigurationSectionAllowed configurationSectionAllowed = ConfigurationSectionAllowed.fromName(listValue);
                if (configurationSectionAllowed.needsCustomConversion())
                    importCollection.addImport("us.mytheria.bloblib.utilities.ConfigurationSectionLib");
            } catch (IllegalArgumentException ignored) {
            }
        }
    }
}
