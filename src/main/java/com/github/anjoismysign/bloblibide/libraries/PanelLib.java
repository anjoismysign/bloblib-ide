package com.github.anjoismysign.bloblibide.libraries;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

public class PanelLib {

    /**
     * Requests a boolean in a fancier way.
     * In case of the window being closed, recursion will be used.
     * In case of the body being written in spanish,
     * I recommend using PanelLib#esEs() one time before
     * running this method.
     * This method allows you to give a title to the JOptionPane.
     *
     * @param title   JOptionPane title.
     * @param message JOptionPane message/body.
     * @param project Project to be used.
     * @return true if the user clicked "yes", false if the user clicked "no".
     */
    public static boolean requestBoolean(String title, String message, Project project) {
        return Messages.showYesNoDialog(project, message, title, null) == 0;
    }

    /**
     * Requests a String through IntelliJ's OpenAPI.
     * Recursion will be used until provided a String.
     * This method allows you to give a title to the InputDialog.
     *
     * @param title        InputDialog title.
     * @param message      Message shown through the input field.
     * @param errorMessage Message shown when the user doesn't provide a String.
     * @param project      Project to be used.
     * @return the String
     */
    @NotNull
    public static String requestString(String title, String message, String errorMessage, Project project) {
        String input = Messages.showInputDialog(project, message, title, null);
        if (input == null || input.length() == 0) {
            showMessage("ERROR", errorMessage, project);
            return requestString(title, message, errorMessage, project);
        }
        return input;
    }

    /**
     * Displays a message through IntelliJ's OpenAPI.
     * This method allows you to give a title to the InputDialog.
     *
     * @param title   InputDialog title.
     * @param message Message to be shown.
     * @param project Project to be used.
     */
    public static void showMessage(String title, String message, Project project) {
        Messages.showMessageDialog(project, message, title, null);
    }
}
