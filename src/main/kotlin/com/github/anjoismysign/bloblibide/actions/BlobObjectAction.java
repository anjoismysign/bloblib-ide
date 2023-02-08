package com.github.anjoismysign.bloblibide.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

public class BlobObjectAction extends com.intellij.openapi.actionSystem.AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        String input = Messages.showInputDialog(e.getProject(), "Message", "Title", null);
    }
}
