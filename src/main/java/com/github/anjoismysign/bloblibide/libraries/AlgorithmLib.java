package com.github.anjoismysign.bloblibide.libraries;

import com.intellij.openapi.project.Project;

public class AlgorithmLib {

    /**
     * Runs code until the user wants to stop.
     * This method allows to give a title to a JOptionPane.
     *
     * @param runnable what you want to run while user prefers to continue
     * @param title    JOptionPane title
     * @param message  JOptionPane message/body
     * @param project  Project to be used.
     */
    public static void dynamicRun(Runnable runnable, String title, String message, Project project) {
        boolean next = true;
        while (next) {
            runnable.run();
            next = PanelLib.requestBoolean(title, message, project);
        }
    }

    /**
     * Runs code until the user wants to stop.
     *
     * @param runnable what you want to run while user prefers to continue
     * @param message  JOptionPane message/body
     * @param project  Project to be used.
     */
    public static void dynamicRun(Runnable runnable, String message, Project project) {
        dynamicRun(runnable, "", message, project);
    }
}
