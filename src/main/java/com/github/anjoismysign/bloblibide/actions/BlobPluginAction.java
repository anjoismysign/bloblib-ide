package com.github.anjoismysign.bloblibide.actions;

import com.github.anjoismysign.bloblibide.entities.FileContentReplacer;
import com.github.anjoismysign.bloblibide.entities.Uber;
import com.github.anjoismysign.bloblibide.libraries.NamingConventions;
import com.github.anjoismysign.bloblibide.libraries.PanelLib;
import com.github.anjoismysign.bloblibide.libraries.PsiDirectoryLib;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;

public class BlobPluginAction extends AnAction {

    @Override
    public void update(@NotNull AnActionEvent event) {
        PsiDirectoryLib.disablePresentationIfNotInsideSrcFolder(event);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event){
        Optional<PsiDirectory> currentPackageOptional = PsiDirectoryLib.getSelectedPackage(event);
        if (currentPackageOptional.isEmpty())
            return;
        final Project project = event.getProject();
        final PsiDirectory psiDirectory = currentPackageOptional.get();
        Uber<String> classNameInput = new Uber<>("BlobPluginNotFound");
        classNameInput.talk(PanelLib.requestString("BlobPlugin", "Enter BlobPlugin class name (without '.java')\n" +
                "Note: will be converted to pascal case\n" +
                "Example 1: 'BlobRP'", "Please provide a class name \n" +
                "Recursion will be used until then.", project));
        classNameInput.talk(NamingConventions.toPascalCase(classNameInput.thanks()));
        final String className = classNameInput.thanks();
        Uber<String> codeInput = new Uber<>("NamingCodeNotFound");
        codeInput.talk(PanelLib.requestString("Naming Code", "Enter naming code. Will be used for generating classes\n" +
                "Note: will be converted to pascal case\n" +
                "Example for 'RP' would be RPManager and RPManagerDirector", "Please provide a naming code.\n" +
                "Recursion will be used until then.", project));
        codeInput.talk(NamingConventions.toPascalCase(codeInput.thanks()));
        final String namingCode = codeInput.thanks();
        String currentPackage = PsiDirectoryLib.getPackageNameOrEmpty(psiDirectory);
        String content;
        try (InputStream inputStream = this.getClass().getResourceAsStream("/templates/BlobPlugin.java")){
            content = FileContentReplacer.getContent(Objects.requireNonNull(inputStream));
            content = content.replace("us.mytheria.blobstudio", currentPackage);
            content = content.replace("Studio", namingCode);
            PsiDirectoryLib.generateClass(psiDirectory, className,
                    content, true);
        } catch (Throwable e) {
            e.printStackTrace();
            return;
        }
    }

//    @Override
//    public void actionPerformed(@NotNull AnActionEvent event) {
//        Optional<PsiDirectory> currentPackageOptional = PsiDirectoryLib.getSelectedPackage(event);
//        if (currentPackageOptional.isEmpty())
//            return;
//        final Project project = event.getProject();
//        final PsiDirectory currentPackage = currentPackageOptional.get();
//        Uber<String> classNameInput = new Uber<>("BlobPluginNotFound");
//        classNameInput.talk(PanelLib.requestString("BlobPlugin", "Enter BlobPlugin class name (without '.java')\n" +
//                "Note: will be converted to pascal case\n" +
//                "Example 1: 'BlobRP'", "Please provide a class name \n" +
//                "Recursion will be used until then.", project));
//        classNameInput.talk(NamingConventions.toPascalCase(classNameInput.thanks()));
//        final String className = classNameInput.thanks();
//        Uber<String> codeInput = new Uber<>("NamingCodeNotFound");
//        codeInput.talk(PanelLib.requestString("Naming Code", "Enter naming code. Will be used for generating classes\n" +
//                "Note: will be converted to pascal case\n" +
//                "Example for 'RP' would be RPManager and RPManagerDirector", "Please provide a naming code.\n" +
//                "Recursion will be used until then.", project));
//        codeInput.talk(NamingConventions.toPascalCase(codeInput.thanks()));
//        final String namingCode = codeInput.thanks();
//        PsiDirectoryLib.generateClass(currentPackage, className,
//                content(currentPackage, namingCode, className), true);
//    }

    protected static String content(PsiDirectory psiManagerPackage, String namingCode, String className) {
        String currentPackage = PsiDirectoryLib.getPackageNameOrEmpty(psiManagerPackage);
        String importPackage = "package " + currentPackage + ";\n\n";
        return importPackage + "import org.bukkit.Bukkit;\n" +
                "import us.mytheria.bloblib.managers.BlobPlugin;\n" +
                "import " + currentPackage + ".director." + namingCode + "ManagerDirector;\n" +
                "\n" +
                "public final class " + className + " extends BlobPlugin {\n" +
                "\n" +
                "    private " + namingCode + "ManagerDirector director;\n" +
                "\n" +
                "    public static " + className + " instance;\n" +
                "\n" +
                "    public static " + className + " getInstance() {\n" +
                "        return instance;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public void onEnable() {\n" +
                "        instance = this;\n" +
                "        director = new " + namingCode + "ManagerDirector();\n" +
                "        Bukkit.getScheduler().runTask(this, () ->\n" +
                "                director.postWorld());\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public void onDisable() {\n" +
                "        unregisterFromBlobLib();\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public " + namingCode + "ManagerDirector getManagerDirector() {\n" +
                "        return director;\n" +
                "    }\n" +
                "}";
    }
}
