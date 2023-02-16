package com.github.anjoismysign.bloblibide.actions;

import com.github.anjoismysign.bloblibide.configurationsection.getter.Getter;
import com.github.anjoismysign.bloblibide.configurationsection.setter.Setter;
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
        versionTwo(event);
    }

    /**
     * Will generate a BlobObject class from the selected ConfigurationSection.
     * All attributes will be final, which means that the BlobObject class will be
     * a data carrier class, similar to a Java 14 record class.
     *
     * @param event the event that triggered this action
     */
    public static void versionTwo(AnActionEvent event) {
        Optional<ObjectGenerator> optional = ObjectGenerator.dataCarrierFromAnActionInsideNewGroup(event, true);
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
//        if (dataTyper.containsDataType("ItemStack"))
//            importCollection.add("org.bukkit.inventory.ItemStack");
        if (dataTyper.containsDataType("BigInteger")) {
            importCollection.add("us.mytheria.bloblib.utilities.SerializationLib");
        }
        if (dataTyper.containsDataType("BigDecimal")) {
            importCollection.add("us.mytheria.bloblib.utilities.SerializationLib");
        }
        if (dataTyper.containsDataType("UUID")) {
            importCollection.add("us.mytheria.bloblib.utilities.SerializationLib");
        }
        if (dataTyper.containsDataType("Location")) {
            importCollection.add("org.bukkit.Location");
            importCollection.add("us.mytheria.bloblib.utilities.SerializationLib");
        }
        if (dataTyper.containsDataType("Block")) {
            importCollection.add("org.bukkit.block.Block");
            importCollection.add("us.mytheria.bloblib.utilities.SerializationLib");
        }
        if (dataTyper.containsDataType("Vector")) {
            importCollection.add("org.bukkit.util.Vector");
            importCollection.add("us.mytheria.bloblib.utilities.SerializationLib");
        }
        if (dataTyper.containsDataType("BlockVector")) {
            importCollection.add("org.bukkit.util.BlockVector");
            importCollection.add("us.mytheria.bloblib.utilities.SerializationLib");
        }
        if (dataTyper.containsDataType("Color")) {
            importCollection.add("org.bukkit.Color");
            importCollection.add("us.mytheria.bloblib.utilities.SerializationLib");
        }
        if (dataTyper.containsDataType("OfflinePlayer")) {
            importCollection.add("org.bukkit.OfflinePlayer");
            importCollection.add("us.mytheria.bloblib.utilities.SerializationLib");
        }
        if (dataTyper.containsDataType("World")) {
            importCollection.add("org.bukkit.World");
            importCollection.add("us.mytheria.bloblib.shapes.WorldShape");
            importCollection.add("us.mytheria.bloblib.utilities.SerializationLib");
        }
        if (dataTyper.containsDataType("WeatherType")) {
            importCollection.add("org.bukkit.WeatherType");
            importCollection.add("us.mytheria.bloblib.shapes.WeatherTypeShape");
            importCollection.add("us.mytheria.bloblib.utilities.SerializationLib");
        }
        if (dataTyper.containsDataType("TreeType")) {
            importCollection.add("org.bukkit.TreeType");
            importCollection.add("us.mytheria.bloblib.shapes.TreeTypeShape");
            importCollection.add("us.mytheria.bloblib.utilities.SerializationLib");
        }
        if (dataTyper.containsDataType("Particle")) {
            importCollection.add("org.bukkit.Particle");
            importCollection.add("us.mytheria.bloblib.shapes.ParticleShape");
            importCollection.add("us.mytheria.bloblib.utilities.SerializationLib");
        }
        if (dataTyper.containsDataType("MusicInstrument")) {
            importCollection.add("org.bukkit.MusicInstrument");
            importCollection.add("us.mytheria.bloblib.shapes.MusicInstrumentShape");
            importCollection.add("us.mytheria.bloblib.utilities.SerializationLib");
        }
        if (dataTyper.containsDataType("Material")) {
            importCollection.add("org.bukkit.Material");
            importCollection.add("us.mytheria.bloblib.shapes.MaterialShape");
            importCollection.add("us.mytheria.bloblib.utilities.SerializationLib");
        }
        if (dataTyper.containsDataType("Instrument")) {
            importCollection.add("org.bukkit.Instrument");
            importCollection.add("us.mytheria.bloblib.shapes.InstrumentShape");
            importCollection.add("us.mytheria.bloblib.utilities.SerializationLib");
        }
        if (dataTyper.containsDataType("GameMode")) {
            importCollection.add("org.bukkit.GameMode");
            importCollection.add("us.mytheria.bloblib.shapes.GameModeShape");
            importCollection.add("us.mytheria.bloblib.utilities.SerializationLib");
        }
        if (dataTyper.containsDataType("Fluid")) {
            importCollection.add("org.bukkit.Fluid");
            importCollection.add("us.mytheria.bloblib.shapes.FluidShape");
            importCollection.add("us.mytheria.bloblib.utilities.SerializationLib");
        }
        if (dataTyper.containsDataType("EntityType")) {
            importCollection.add("org.bukkit.entity.EntityType");
            importCollection.add("us.mytheria.bloblib.shapes.EntityTypeShape");
            importCollection.add("us.mytheria.bloblib.utilities.SerializationLib");
        }
        if (dataTyper.containsDataType("EntityEffect")) {
            importCollection.add("org.bukkit.EntityEffect");
            importCollection.add("us.mytheria.bloblib.shapes.EntityEffectShape");
            importCollection.add("us.mytheria.bloblib.utilities.SerializationLib");
        }
        if (dataTyper.containsDataType("Enchantment")) {
            importCollection.add("org.bukkit.enchantments.Enchantment");
            importCollection.add("us.mytheria.bloblib.shapes.EnchantmentShape");
            importCollection.add("us.mytheria.bloblib.utilities.SerializationLib");
        }
        if (dataTyper.containsDataType("Effect")) {
            importCollection.add("org.bukkit.Effect");
            importCollection.add("us.mytheria.bloblib.shapes.EffectShape");
            importCollection.add("us.mytheria.bloblib.utilities.SerializationLib");
        }
        if (dataTyper.containsDataType("DyeColor")) {
            importCollection.add("org.bukkit.DyeColor");
            importCollection.add("us.mytheria.bloblib.shapes.DyeColorShape");
            importCollection.add("us.mytheria.bloblib.utilities.SerializationLib");
        }
        if (dataTyper.containsDataType("Difficulty")) {
            importCollection.add("org.bukkit.Difficulty");
            importCollection.add("us.mytheria.bloblib.shapes.DifficultyShape");
            importCollection.add("us.mytheria.bloblib.utilities.SerializationLib");
        }
        if (dataTyper.containsDataType("CropState")) {
            importCollection.add("org.bukkit.CropState");
            importCollection.add("us.mytheria.bloblib.shapes.CropStateShape");
            importCollection.add("us.mytheria.bloblib.utilities.SerializationLib");
        }

        List<ObjectAttribute> attributes = objectGenerator.getDataTyper().listAttributes();
        List<ObjectAttribute> finalAttributes = objectGenerator.getFinalDataTyper().listAttributes();
        StringBuilder saveToFile = new StringBuilder();
        saveToFile.append("@Override\n").append("public File saveToFile(File directory){ \n")
                .append("    File file = new File(directory + \"/\" + getKey() + \".yml\");\n")
                .append("    YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);\n");
        attributes.forEach(attribute -> Setter.saveToConfigurationSection(attribute, "yamlConfiguration", saveToFile));
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
        attributes.forEach(attribute -> Getter.getFromConfigurationSection(attribute, "yamlConfiguration", loadFromFile));
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

    /**
     * @param event The event
     * @deprecated Use {@link #versionTwo(AnActionEvent)} instead
     */
    @Deprecated
    public static void versionOne(AnActionEvent event) {
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
