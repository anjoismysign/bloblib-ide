package com.github.anjoismysign.bloblibide.actions;

import com.github.anjoismysign.bloblibide.entities.ClassGenerator;
import com.github.anjoismysign.bloblibide.entities.ObjectAttribute;
import com.github.anjoismysign.bloblibide.libraries.NamingConventions;
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
        classGenerator.addDependencyInjection("key");
        classGenerator.getImportCollection().add("us.mytheria.bloblib.entities.BlobObject");
        classGenerator.getImportCollection().add("java.io.File");

        StringBuilder builder = new StringBuilder();
        List<ObjectAttribute> attributes = classGenerator.getDataTyper().listAttributes();
        builder.append("@Override\n").append("public File saveToFile(){ \n")
                .append("    File file = null; //TODO replace 'null' with actual file\n")
                .append("    YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);\n\n")
                .append("    //TODO check that yamlConfiguration can quickly serialize provided attributes\n")
                .append("    //quick dataTypes are List's of primitive's, org.bukkit.util.Vector,\n")
                .append("    //org.bukkit.Location, org.bukkit.Color, org.bukkit.inventory.ItemStack,\n")
                .append("    //and org.bukkit.OfflinePlayer.\n\n");
        attributes.forEach(attribute -> {
            if (attribute.getAttributeName().equals("key"))
                return;
            String pascalAttributeName = NamingConventions.toPascalCase(attribute.getAttributeName());
            builder.append("    yamlConfiguration.set(\"").append(pascalAttributeName)
                    .append("\", get").append(pascalAttributeName).append("());\n");
        });
        builder.append("    try {\n").append("            config.save(file);\n")
                .append("    } catch (Exception exception) {\n")
                .append("        exception.printStackTrace();\n")
                .append("    }")
                .append("    return file;\n")
                .append("}");
        classGenerator.getDefaultFunctions().add(builder.toString());


        classGenerator.generate();
    }
}
