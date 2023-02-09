package com.github.anjoismysign.bloblibide.actions;

import com.github.anjoismysign.bloblibide.entities.ClassBuilder;
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
        Optional<ClassBuilder> optional = ClassBuilder.fromAnActionInsideNewGroup(event, true);
        if (optional.isEmpty())
            return;
        ClassBuilder classBuilder = optional.get();
        classBuilder.setClassDeclaration(className->"public class " + className + " implements BlobObject {");
        classBuilder.getDefaultAttributes().add(new ObjectAttribute("Document", "data"));
        classBuilder.getImportCollection().add("org.bson.Document");
        classBuilder.getImportCollection().add("us.mytheria.bloblib.entities.BlobObject");
        classBuilder.getImportCollection().add("java.io.File");

        classBuilder.create();
    }
}
