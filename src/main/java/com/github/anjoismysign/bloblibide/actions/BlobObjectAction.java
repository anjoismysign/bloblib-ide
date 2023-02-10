package com.github.anjoismysign.bloblibide.actions;

import com.github.anjoismysign.bloblibide.entities.ClassGenerator;
import com.github.anjoismysign.bloblibide.entities.ImportCollection;
import com.github.anjoismysign.bloblibide.entities.ObjectAttribute;
import com.github.anjoismysign.bloblibide.libraries.ConfigurationSectionLib;
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
        Optional<ClassGenerator> optional = ClassGenerator.fromAnActionInsideNewGroup(event, true);
        if (optional.isEmpty())
            return;
        ClassGenerator classGenerator = optional.get();
        classGenerator.setClassDeclaration(className -> "public class " + className + " implements BlobObject {");
        classGenerator.getDataTyper().add("String", "key");
        ImportCollection importCollection = classGenerator.getImportCollection();
        importCollection.add("us.mytheria.bloblib.entities.BlobObject");
        importCollection.add("global.warming.commons.io.FilenameUtils");
        importCollection.add("org.bukkit.configuration.file.YamlConfiguration");
        importCollection.add("java.io.File");
        if (classGenerator.getDataTyper().containsKey("ItemStack"))
            importCollection.add("org.bukkit.inventory.ItemStack");
        if (classGenerator.getDataTyper().containsKey("Location"))
            importCollection.add("org.bukkit.Location");
        if (classGenerator.getDataTyper().containsKey("Vector"))
            importCollection.add("org.bukkit.util.Vector");
        if (classGenerator.getDataTyper().containsKey("Color"))
            importCollection.add("org.bukkit.Color");
        if (classGenerator.getDataTyper().containsKey("OfflinePlayer"))
            importCollection.add("org.bukkit.OfflinePlayer");

        List<ObjectAttribute> attributes = classGenerator.getDataTyper().listAttributes();
        StringBuilder saveToFile = new StringBuilder();
        saveToFile.append("@Override\n").append("public File saveToFile(File directory){ \n")
                .append("    File file = new File(directory + \"/\" + getKey() + \".yml\");\n");
        attributes.forEach(attribute -> {
            if (attribute.getAttributeName().equals("key"))
                return;
            ConfigurationSectionLib.saveToConfigurationSection(attribute,
                    "yamlConfiguration", saveToFile);
        });
        saveToFile.append("    try {\n").append("            yamlConfiguration.save(file);\n")
                .append("    } catch (Exception exception) {\n")
                .append("        exception.printStackTrace();\n")
                .append("    }")
                .append("    return file;\n")
                .append("}");
        classGenerator.getDefaultFunctions().add(saveToFile.toString());
        StringBuilder loadFromFile = new StringBuilder();
        loadFromFile.append("public static ").append(classGenerator.getClassName()).append(" fromFile(File file){ \n")
                .append("    String fileName = file.getName();\n")
                .append("    YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);");
        attributes.forEach(attribute -> {
            if (attribute.getAttributeName().equals("key"))
                return;
            ConfigurationSectionLib.getFromConfigurationSection(attribute,
                    "yamlConfiguration", loadFromFile);
        });
        loadFromFile.append("    String key = FilenameUtils.removeExtension(fileName);\n");
        loadFromFile.append("    return new ").append(classGenerator.getClassName()).append("(");
        attributes.forEach(attribute -> loadFromFile.append(attribute.getAttributeName()).append(", "));
        loadFromFile.delete(loadFromFile.length() - 2, loadFromFile.length());
        loadFromFile.append(");\n")
                .append("}");
        classGenerator.getDefaultFunctions().add(loadFromFile.toString());

        classGenerator.generate();
    }
}
