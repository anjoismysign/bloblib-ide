package com.github.anjoismysign.bloblibide.actions;

import com.github.anjoismysign.bloblibide.entities.ClassGenerator;
import com.github.anjoismysign.bloblibide.entities.ObjectAttribute;
import com.github.anjoismysign.bloblibide.libraries.PsiDirectoryLib;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class BlobObjectAction extends AnAction {

    @Override
    public void update(@NotNull AnActionEvent event){
        PsiDirectoryLib.disablePresentationIfNotInsideSrcFolder(event);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event){
        Optional<ClassGenerator> optional = ClassGenerator.fromAnActionInsideNewGroup(event, true);
        if (optional.isEmpty())
            return;
        ClassGenerator classBuilder = optional.get();
        classBuilder.setClassDeclaration(className->"public class " + className + " implements BlobObject {");
        classBuilder.getDefaultAttributes().add(new ObjectAttribute("Document", "data"));
        classBuilder.getDefaultAttributes().add(new ObjectAttribute("String", "key"));
        classBuilder.getImportCollection().add("org.bson.Document");
        classBuilder.getImportCollection().add("us.mytheria.bloblib.entities.BlobObject");
        classBuilder.getImportCollection().add("java.io.File");

        classBuilder.getDefaultFunctions().add("@Override\n" +
                "public File saveToFile(){ \n" +
                "    File file = null; //TODO replace 'null' with actual file\n" +
                "    YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);\n" +
                "    //TODO check that yamlConfiguration can correctly serialize provided attributes" +
                "    //valid dataTypes are List's of primitive's, org.bukkit.util.Vector," +
                "    //" +


        classBuilder.generate();
    }
}
