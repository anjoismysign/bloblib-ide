package com.github.anjoismysign.bloblibide.libraries;

import com.github.anjoismysign.bloblibide.entities.Uber;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.codeStyle.CodeStyleManager;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class PsiDirectoryLib {

    public static void generateClass(PsiDirectory managerPackage, String className, String content, boolean navigate) {
        ReadAction.run(() -> ApplicationManager.getApplication().runWriteAction(() -> {
            try {
                PsiFile psiFile = managerPackage.createFile(className + ".java");
                psiFile.getContainingFile().getVirtualFile().setBinaryContent(content.getBytes(StandardCharsets.UTF_8));
                WriteCommandAction.runWriteCommandAction(psiFile.getProject(), () -> {
                    CodeStyleManager.getInstance(psiFile.getProject()).reformat(psiFile);
                });
                managerPackage.getVirtualFile().refresh(false, true);
                if (navigate)
                    psiFile.navigate(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }

    public static void generateClass(PsiDirectory managerPackage, String className, String content) {
        generateClass(managerPackage, className, content, false);
    }

    public static Optional<PsiDirectory> createPackage(AnActionEvent event, String append) {
        Optional<PsiDirectory> selectedPackage = getSelectedPackage(event);
        if (selectedPackage.isEmpty())
            return Optional.empty();
        final PsiDirectory psiDirectory = selectedPackage.get();
        Uber<PsiDirectory> uberCreatedPackage = new Uber<>(null);
        ReadAction.run(() -> {
            ApplicationManager.getApplication().runWriteAction(() -> {
                try {
                    uberCreatedPackage.talk(psiDirectory.createSubdirectory(append));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
        return Optional.ofNullable(uberCreatedPackage.thanks());

    }

    public static Optional<PsiDirectory> getSelectedPackage(AnActionEvent event) {
        Project project = event.getProject();
        if (project == null)
            return Optional.empty();
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
        if (uberSelectedDirectory.thanks() == null)
            return Optional.empty();
        return Optional.of(uberSelectedDirectory.thanks());
    }

    public static boolean isInsideSrcFolder(PsiDirectory directory) {
        VirtualFile virtualFile = directory.getVirtualFile();
        String path = virtualFile.getPath();
        return path.contains("/src/") || path.contains("/java/");
    }

    public static Optional<String> getPackageName(PsiDirectory directory) {
        if (!isInsideSrcFolder(directory))
            return Optional.empty();
        String path = directory.getVirtualFile().getPath();
        String[] split;
        if (path.contains("/java/")) {
            split = path.split("/java/");
            String packageName = split[1].replace("/", ".");
            return Optional.of(packageName);
        }
        split = path.split("/src/");
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
