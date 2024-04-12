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
import com.intellij.ui.JBColor;
import me.anjoismysign.anjo.swing.components.AnjoTextField;
import me.anjoismysign.anjo.swing.listeners.TextInputType;
import me.anjoismysign.hahaswing.BubbleFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

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
        final PsiDirectory psiDirectory = currentPackageOptional.get();
        AnjoTextField pluginField = AnjoTextField.build(" BlobPlugin");
        AnjoTextField codeField = AnjoTextField.build(" Naming Code");
        BubbleFactory.getInstance().controller(anjoPane -> {
                        },
                        "Project Wizard",
                        new ImageIcon(Objects.requireNonNull(SetupProjectAction.this.getClass().getResource("/icons/bloblibide.png")))
                                .getImage().getScaledInstance(256, 256, Image.SCALE_SMOOTH),
                        false,
                        null,
                        pluginField,codeField)
                .onBlow(anjoPane -> {
                    if (!anjoPane.saidYes())
                        return;

                    final String className = pluginField.getText();
                    if (className.isBlank())
                        return;
                    final String namingCode = codeField.getText();
                    if (namingCode.isBlank())
                        return;
                    String currentPackageName = PsiDirectoryLib.getPackageNameOrEmpty(psiDirectory);
                    // BlobPlugin
                    String content;
                    String pluginName = className;
                    try (InputStream inputStream = this.getClass().getResourceAsStream("/templates/BlobStudio.java")){
                        content = FileContentReplacer.getContent(Objects.requireNonNull(inputStream));
                        content = content.replace("us.mytheria.blobstudio", currentPackageName);
                        content = content.replace("Studio", namingCode);
                        PsiDirectoryLib.generateClass(psiDirectory, pluginName,
                                content, true);
                    } catch (Throwable e) {
                        e.printStackTrace();
                        return;
                    }

                    Optional<PsiDirectory> directorPackageOptional = PsiDirectoryLib.createPackage(psiDirectory, "director");
                    if (directorPackageOptional.isEmpty())
                        return;
                    final PsiDirectory directorPackage = directorPackageOptional.get();
                    String directorPackageName = PsiDirectoryLib.getPackageNameOrEmpty(directorPackage);
                    Optional<PsiDirectory> managerPackageOptional = PsiDirectoryLib.createPackage(directorPackage, "manager");
                    if (managerPackageOptional.isEmpty())
                        return;
                    final PsiDirectory managerPackage = managerPackageOptional.get();
                    String managerPackageName = PsiDirectoryLib.getPackageNameOrEmpty(managerPackage);

                    // ManagerDirector
                    String managerDirectorName = namingCode + "ManagerDirector";
                    try (InputStream inputStream = this.getClass().getResourceAsStream("/templates/director/StudioManagerDirector.java")){
                        content = FileContentReplacer.getContent(Objects.requireNonNull(inputStream));
                        content = content.replace("us.mytheria.blobstudio", currentPackageName);
                        content = content.replace("Studio", namingCode);
                        PsiDirectoryLib.generateClass(directorPackage, managerDirectorName,
                                content, true);
                    } catch (Throwable e) {
                        e.printStackTrace();
                        return;
                    }

                    // Manager
                    String managerName = namingCode + "Manager";
                    try (InputStream inputStream = this.getClass().getResourceAsStream("/templates/director/StudioManager.java")){
                        content = FileContentReplacer.getContent(Objects.requireNonNull(inputStream));
                        content = content.replace("us.mytheria.blobstudio", currentPackageName);
                        content = content.replace("Studio", namingCode);
                        PsiDirectoryLib.generateClass(directorPackage, managerName,
                                content, true);
                    } catch (Throwable e) {
                        e.printStackTrace();
                        return;
                    }

                    // ConfigManager
                    String configManagerName = namingCode + "ConfigManager";
                    try (InputStream inputStream = this.getClass().getResourceAsStream("/templates/director/manager/StudioConfigManager.java")){
                        content = FileContentReplacer.getContent(Objects.requireNonNull(inputStream));
                        content = content.replace("us.mytheria.blobstudio", currentPackageName);
                        content = content.replace("Studio", namingCode);
                        PsiDirectoryLib.generateClass(managerPackage, configManagerName,
                                content, true);
                    } catch (Throwable e) {
                        e.printStackTrace();
                        return;
                    }
                });
    }
}
