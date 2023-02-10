package com.github.anjoismysign.bloblibide.actions;

import com.github.anjoismysign.bloblibide.entities.Uber;
import com.github.anjoismysign.bloblibide.libraries.NamingConventions;
import com.github.anjoismysign.bloblibide.libraries.PanelLib;
import com.github.anjoismysign.bloblibide.libraries.PsiDirectoryLib;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class BlobPluginAction extends AnAction {

    @Override
    public void update(@NotNull AnActionEvent event) {
        PsiDirectoryLib.disablePresentationIfNotInsideSrcFolder(event);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Optional<PsiDirectory> directorPackageOptional = PsiDirectoryLib.createPackage(event, "director");
        if (directorPackageOptional.isEmpty())
            return;
        final Project project = event.getProject();
        final PsiDirectory directorPackage = directorPackageOptional.get();
        Uber<String> classNameInput = new Uber<>("BlobPluginNotFound");
        classNameInput.talk(PanelLib.requestString("BlobPlugin", "Enter BlobPlugin class name (without '.java')\n" +
                "Note: will be converted to pascal case\n" +
                "Example 1: 'BlobRP'", "Please provide a class name \n" +
                "Recursion will be used until then.", project));
        classNameInput.talk(NamingConventions.toPascalCase(classNameInput.thanks()));
        String className = classNameInput.thanks();
        Uber<String> codeInput = new Uber<>("NamingCodeNotFound");
        codeInput.talk(PanelLib.requestString("Naming Code", "Enter naming code. Will be used for generating classes\n" +
                "Note: will be converted to pascal case\n" +
                "Example for 'RP' would be RPManager and RPManagerDirector", "Please provide a naming code.\n" +
                "Recursion will be used until then.", project));
        codeInput.talk(NamingConventions.toPascalCase(codeInput.thanks()));
        String namingCode = codeInput.thanks();
        PsiDirectoryLib.generateClass(directorPackage, namingCode + "Manager", managerContent(directorPackage, namingCode));
        ReadAction.run(() -> ApplicationManager.getApplication().runWriteAction(() -> {
            try {
                final PsiDirectory managerPackage = directorPackage.createSubdirectory("manager");
                PsiDirectoryLib.generateClass(managerPackage, "ConfigManager", configManagerContent(managerPackage, namingCode, className));
                PsiDirectoryLib.generateClass(managerPackage, "ListenerManager", listenerManagerContent(managerPackage, namingCode));
                PsiDirectoryLib.generateClass(directorPackage, namingCode + "ManagerDirector",
                        managerDirectorContent(directorPackage, namingCode, className), true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }

    private String getPackageOrEmpty(PsiDirectory directorPackage) {
        return PsiDirectoryLib.getPackageName(directorPackage).orElse("");
    }

    private String listenerManagerContent(PsiDirectory psiManagerPackage, String namingCode) {
        String managerPackage = getPackageOrEmpty(psiManagerPackage);
        String directorPackage = managerPackage.substring(0, managerPackage.length() - 8);
        managerPackage = "package " + managerPackage + ";\n\n";
        return managerPackage + "import " + directorPackage + "." + namingCode + "Manager;\n" +
                "import " + directorPackage + "." + namingCode + "ManagerDirector;\n" +
                "\n" +
                "public class ListenerManager extends " + namingCode + "Manager {\n" +
                "\n" +
                "    public ListenerManager(" + namingCode + "ManagerDirector managerDirector) {\n" +
                "        super(managerDirector);\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public void loadInConstructor() {\n" +
                "        //logics\n" +
                "    }\n" +
                "}";
    }

    private String configManagerContent(PsiDirectory psiManagerPackage, String namingCode, String className) {
        String managerPackage = getPackageOrEmpty(psiManagerPackage);
        String directorPackage = managerPackage.substring(0, managerPackage.length() - 8);
        managerPackage = "package " + managerPackage + ";\n\n";
        return managerPackage + "import org.bukkit.configuration.ConfigurationSection;\n" +
                "import org.bukkit.configuration.file.FileConfiguration;\n" +
                "import us.mytheria.bloblib.entities.SimpleEventListener;\n" +
                "import " + directorPackage + "." + namingCode + "Manager;\n" +
                "import " + directorPackage + "." + namingCode + "ManagerDirector;\n" +
                "\n" +
                "public class ConfigManager extends " + namingCode + "Manager {\n" +
                "    private SimpleEventListener<Boolean> listenerExample;\n" +
                "\n" +
                "    public ConfigManager(" + namingCode + "ManagerDirector managerDirector) {\n" +
                "        super(managerDirector);\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public void loadInConstructor() {\n" +
                "        " + className + " main = " + className + ".getInstance();\n" +
                "        FileConfiguration config = main.getConfig();\n" +
                "        config.options().copyDefaults(true);\n" +
                "        ConfigurationSection listeners = config.getConfigurationSection(\"Listeners\");\n" +
                "        listenerExample = SimpleEventListener.BOOLEAN(\n" +
                "                (listeners.getConfigurationSection(\"UseUUIDs\")), \"State\");\n" +
                "        main.saveConfig();\n" +
                "    }\n" +
                "\n" +
                "    public SimpleEventListener<Boolean> useUUIDs() {\n" +
                "        return listenerExample;\n" +
                "    }\n" +
                "}";
    }

    private String managerContent(PsiDirectory psiDirectorPackage, String namingCode) {
        String className = namingCode + "Manager";
        String directorPackage = getPackageOrEmpty(psiDirectorPackage);
        directorPackage = "package " + directorPackage + ";\n\n";
        return directorPackage +
                "import us.mytheria.bloblib.managers.Manager;\n" +
                "\n" +
                "public class " + className + " extends Manager {\n" +
                "\n" +
                "    public " + className + "(" + namingCode + "ManagerDirector managerDirector) {\n" +
                "        super(managerDirector);\n" +
                "    }\n" +
                "@Override\n" + "    public " + className + "Director getManagerDirector() {\n" + "        return (" +
                className + "Director) super.getManagerDirector();\n" +
                "    }\n" +
                "}";
    }

    private String managerDirectorContent(PsiDirectory psiDirectorPackage, String namingCode, String className) {
        String directorPackage = getPackageOrEmpty(psiDirectorPackage);
        String managerPackage = directorPackage + ".manager";
        directorPackage = "package " + directorPackage + ";\n\n";
        return directorPackage + "import us.mytheria.bloblib.managers.ManagerDirector;\n" +
                "import " + managerPackage + ".ConfigManager;\n" +
                "import " + managerPackage + ".ListenerManager;\n" +
                "\n" +
                "public class " + namingCode + "ManagerDirector extends ManagerDirector {\n" +
                "    public static " + namingCode + "ManagerDirector getInstance() {\n" +
                "        return " + className + ".getInstance().getManagerDirector();\n" +
                "    }\n" +
                "\n" +
                "    public " + namingCode + "ManagerDirector() {\n" +
                "        super(" + className + ".getInstance());\n" +
                "        addManager(\"ConfigManager\", new ConfigManager(this));\n" +
                "        addManager(\"ListenerManager\", new ListenerManager(this));\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * From top to bottom, follow the order.\n" +
                "     */\n" +
                "    @Override\n" +
                "    public void reload() {\n" +
                "        //reload directors\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public void unload() {\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public void postWorld() {\n" +
                "    }\n" +
                "\n" +
                "    public final ConfigManager getConfigManager() {\n" +
                "        return (ConfigManager) getManager(\"ConfigManager\");\n" +
                "    }\n" +
                "\n" +
                "    public final ListenerManager getListenerManager() {\n" +
                "        return (ListenerManager) getManager(\"ListenerManager\");\n" +
                "    }\n" +
                "}";
    }
}
