package com.github.anjoismysign.bloblibide.actions;

import com.github.anjoismysign.bloblibide.entities.DataTyper;
import com.github.anjoismysign.bloblibide.entities.ImportCollection;
import com.github.anjoismysign.bloblibide.entities.ObjectAttribute;
import com.github.anjoismysign.bloblibide.entities.ObjectGenerator;
import com.github.anjoismysign.bloblibide.libraries.ConfigurationSectionLib;
import com.github.anjoismysign.bloblibide.libraries.ImportLib;
import com.github.anjoismysign.bloblibide.libraries.PsiDirectoryLib;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class BlobObjectAction extends AnAction {

    @Override
    public void update(@NotNull AnActionEvent event) {
        PsiDirectoryLib.disablePresentationIfNotInsideSrcFolder(event);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Optional<ObjectGenerator> optional = ObjectGenerator.fromAnActionInsideNewGroup(event, true);
        if (optional.isEmpty())
            return;
        ObjectGenerator objectGenerator = optional.get();
        DataTyper dataTyper = objectGenerator.getDataTyper();
        objectGenerator.setClassDeclaration(className -> "public class " + className + " implements BlobObject {");
        objectGenerator.getFinalDataTyper().add("String", "key");
        ImportCollection importCollection = objectGenerator.getImportCollection();
        ImportLib.applyConfigurationSectionImports(importCollection, dataTyper);
        importCollection.add("us.mytheria.bloblib.entities.BlobObject");
        importCollection.add("global.warming.commons.io.FilenameUtils");
        importCollection.add("org.bukkit.configuration.file.YamlConfiguration");
        importCollection.add("java.io.File");
        if (objectGenerator.getDataTyper().containsKey("ItemStack"))
            importCollection.add("org.bukkit.inventory.ItemStack");
        if (objectGenerator.getDataTyper().containsKey("Location"))
            importCollection.add("org.bukkit.Location");
        if (objectGenerator.getDataTyper().containsKey("Block"))
            importCollection.add("org.bukkit.block.Block");
        if (objectGenerator.getDataTyper().containsKey("Vector"))
            importCollection.add("org.bukkit.util.Vector");
        if (objectGenerator.getDataTyper().containsKey("BlockVector"))
            importCollection.add("org.bukkit.util.BlockVector");
        if (objectGenerator.getDataTyper().containsKey("Color"))
            importCollection.add("org.bukkit.Color");
        if (objectGenerator.getDataTyper().containsKey("OfflinePlayer"))
            importCollection.add("org.bukkit.OfflinePlayer");

        List<ObjectAttribute> attributes = objectGenerator.getDataTyper().listAttributes();
        List<ObjectAttribute> finalAttributes = objectGenerator.getFinalDataTyper().listAttributes();
        StringBuilder saveToFile = new StringBuilder();
        saveToFile.append("@Override\n").append("public File saveToFile(File directory){ \n")
                .append("    File file = new File(directory + \"/\" + getKey() + \".yml\");\n")
                .append("    YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);\n");
        attributes.forEach(attribute -> ConfigurationSectionLib.saveToConfigurationSection(attribute,
                "yamlConfiguration", saveToFile));
        saveToFile.append("    try {\n").append("            yamlConfiguration.save(file);\n")
                .append("    } catch (Exception exception) {\n")
                .append("        exception.printStackTrace();\n")
                .append("    }")
                .append("    return file;\n")
                .append("}");
        objectGenerator.getDefaultFunctions().add(saveToFile.toString());
        StringBuilder loadFromFile = new StringBuilder();
        loadFromFile.append("public static ").append(objectGenerator.getClassName()).append(" fromFile(File file){ \n")
                .append("    String fileName = file.getName();\n")
                .append("    YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);");
        attributes.forEach(attribute -> ConfigurationSectionLib.getFromConfigurationSection(attribute,
                "yamlConfiguration", loadFromFile));
        loadFromFile.append("    String key = FilenameUtils.removeExtension(fileName);\n");
        loadFromFile.append("    return new ").append(objectGenerator.getClassName()).append("(");
        attributes.forEach(attribute -> loadFromFile.append(attribute.getAttributeName()).append(", "));
        finalAttributes.forEach(attribute -> loadFromFile.append(attribute.getAttributeName()).append(", "));
        loadFromFile.delete(loadFromFile.length() - 2, loadFromFile.length());
        loadFromFile.append(");\n")
                .append("}");
        objectGenerator.getDefaultFunctions().add(loadFromFile.toString());

        objectGenerator.generate();
    }
}
