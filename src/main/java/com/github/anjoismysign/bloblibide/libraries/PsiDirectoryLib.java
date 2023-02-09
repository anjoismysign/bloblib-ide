package com.github.anjoismysign.bloblibide.libraries;

import com.github.anjoismysign.bloblibide.entities.Uber;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;

import java.util.Optional;

public class PsiDirectoryLib {

    public static boolean isInsideSrcFolder(PsiDirectory directory) {
        VirtualFile virtualFile = directory.getVirtualFile();
        String path = virtualFile.getPath();
        return path.contains("/src/") || path.contains("/java/");
    }

    public static Optional<String> getPackageName(PsiDirectory directory) {
        if (!isInsideSrcFolder(directory))
            return Optional.empty();

        String path = directory.getVirtualFile().getPath();
        String[] split = path.split("/src/");
        if (split.length == 1) {
            split = path.split("/java/");
            String packageName = split[1].replace("/", ".");
            return Optional.of(packageName);
        }
        String packageName = split[1].replace("/", ".");
        return Optional.of(packageName);
    }

    public static void disablePresentationIfNotInsideSrcFolder(AnActionEvent event) {
        Project project = event.getProject();
        if (project == null) {
            return;
        }

        // Get the selected directory (package) in the Project Tool Window
        Uber<PsiDirectory> uberSelectedDirectory = new Uber<>(null);
        DataContext dataContext = event.getDataContext();
        PsiElement psiElement = CommonDataKeys.PSI_ELEMENT.getData(dataContext);
        if (psiElement instanceof PsiDirectory) {
            uberSelectedDirectory.talk((PsiDirectory) psiElement);
        } else {
            VirtualFile virtualFile = CommonDataKeys.VIRTUAL_FILE.getData(dataContext);
            if (virtualFile != null && virtualFile.isDirectory()) {
                PsiManager psiManager = PsiManager.getInstance(project);
                uberSelectedDirectory.talk(psiManager.findDirectory(virtualFile));
            }
        }
        if (uberSelectedDirectory.thanks() != null) {
            PsiDirectory selectedDirectory = uberSelectedDirectory.thanks();
            boolean isInsideJavaPackage = PsiDirectoryLib.isInsideSrcFolder(selectedDirectory);
            event.getPresentation().setEnabled(isInsideJavaPackage);
        }
    }
}
