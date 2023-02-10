package com.github.anjoismysign.bloblibide.actions;

import com.github.anjoismysign.bloblibide.entities.Uber;
import com.github.anjoismysign.bloblibide.libraries.NamingConventions;
import com.github.anjoismysign.bloblibide.libraries.PanelLib;
import com.github.anjoismysign.bloblibide.libraries.PsiDirectoryLib;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static com.github.anjoismysign.bloblibide.actions.BlobPluginAction.content;

public class SetupProjectAction extends AnAction {
    @Override
    public void update(@NotNull AnActionEvent event) {
        PsiDirectoryLib.disablePresentationIfNotInsideSrcFolder(event);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Optional<PsiDirectory> currentPackageOptional = PsiDirectoryLib.getSelectedPackage(event);
        if (currentPackageOptional.isEmpty())
            return;
        final Project project = event.getProject();
        final PsiDirectory currentPackage = currentPackageOptional.get();
        final String currentPackateAddress = PsiDirectoryLib.getPackageNameOrEmpty(currentPackage);
        Uber<String> classNameInput = new Uber<>("BlobPluginNotFound");
        classNameInput.talk(PanelLib.requestString("BlobPlugin", "Enter BlobPlugin class name (without '.java')\n" +
                "Note: will be converted to pascal case\n" +
                "Example 1: 'BlobRP'", "Please provide a class name \n" +
                "Recursion will be used until then.", project));
        classNameInput.talk(NamingConventions.toPascalCase(classNameInput.thanks()));
        final String className = classNameInput.thanks();
        final String blobPluginImport = currentPackateAddress + "." + className;
        Uber<String> codeInput = new Uber<>("NamingCodeNotFound");
        codeInput.talk(PanelLib.requestString("Naming Code", "Enter naming code. Will be used for generating classes\n" +
                "Note: will be converted to pascal case\n" +
                "Example for 'RP' would be RPManager and RPManagerDirector", "Please provide a naming code.\n" +
                "Recursion will be used until then.", project));
        codeInput.talk(NamingConventions.toPascalCase(codeInput.thanks()));
        final String namingCode = codeInput.thanks();
        PsiDirectoryLib.generateClass(currentPackage, className, content(currentPackage, namingCode, className));
        Optional<PsiDirectory> directorPackageOptional = PsiDirectoryLib.createPackage(currentPackage, "director");
        if (directorPackageOptional.isEmpty())
            return;
        final PsiDirectory directorPackage = directorPackageOptional.get();
        ManagerPackageSetupAction.setup(directorPackage, namingCode, className, blobPluginImport);
    }
}
