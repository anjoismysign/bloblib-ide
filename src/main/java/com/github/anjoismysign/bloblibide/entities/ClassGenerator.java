package com.github.anjoismysign.bloblibide.entities;

import com.github.anjoismysign.bloblibide.libraries.AlgorithmLib;
import com.github.anjoismysign.bloblibide.libraries.NamingConventions;
import com.github.anjoismysign.bloblibide.libraries.PanelLib;
import com.github.anjoismysign.bloblibide.libraries.PsiDirectoryLib;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ClassGenerator {
    private final PsiDirectory selectedDirectory;
    private final String className;
    private final DataTyper dataTyper;
    private final ImportCollection importCollection;
    private Function<String, String> classDeclaration;
    private final DefaultAttributes defaultAttributes;
    private final List<String> defaultFunctions;

    public static Optional<ClassGenerator> fromAnActionInsideNewGroup(AnActionEvent event, boolean dynamicDataTyper) {
        Project project = event.getProject();
        if (project == null) {
            return Optional.empty();
        }
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
        PsiDirectory selectedDirectory = uberSelectedDirectory.thanks();
        Uber<String> input = new Uber<>("ClassNameNotFound");
        input.talk(PanelLib.requestString("Class name", "Enter the class/object name below", "Please provide a class name. \n" +
                "Recursion will be used until then.", project));
        input.talk(NamingConventions.toPascalCase(input.thanks()));
        String className = input.thanks();
        DataTyper dataTyper;
        if (dynamicDataTyper) {
            dataTyper = new DataTyper();
            AlgorithmLib.dynamicRun(() -> {
                String dataType = PanelLib.requestString("Enter Attribute/s", "Example 1: 'String name,lastName'\n" +
                                "Example 2: 'boolean isDeceased'\n" +
                                "Example 3: 'HashMap<String,Integer> records' (notice to not leave a ' ' space between\n" +
                                "the comma of the key and value of the HashMap)",
                        "Provide valid attribute/s. Recursion will be used until then.", project);
                dataTyper.parseDataType(dataType);
            }, "Enter Attribute", "Do you wish to add another attribute?", project);
        } else {
            String raw = PanelLib.requestString("Enter Attribute/s", "Example 1: 'String name,lastName'\n" +
                    "Example 2: 'boolean isVaccined,impoverished ; String pharmaceutical'\n" +
                    "Example 4: 'String name,lastname;int age;double income,expenses'", "Invalid input. Recursion will be used until then.", project);
            dataTyper = DataTyper.fromRaw(raw);
        }
        return Optional.of(new ClassGenerator(selectedDirectory, className, dataTyper));
    }

    public ClassGenerator(PsiDirectory selectedDirectory,
                          String className,
                          DataTyper dataTyper) {
        this.selectedDirectory = selectedDirectory;
        this.className = className;
        this.dataTyper = dataTyper;
        importCollection = new ImportCollection();
        classDeclaration = name -> "public class " + name + " {";
        defaultAttributes = new DefaultAttributes();
        defaultFunctions = new ArrayList<>();
    }

    private String content() {
        StringBuilder builder = new StringBuilder();
        Optional<String> hasPackageName = PsiDirectoryLib.getPackageName(selectedDirectory);
        String declarePackage = hasPackageName.map(s -> "package " + s + ";\n\n").orElse("");
        builder.append(declarePackage);
        builder.append(getImportCollection().importPackages());
        builder.append(getClassDeclaration().apply(getClassName())).append("\n\n");
        getDefaultAttributes().encapsulate().forEach(builder::append);
        getDataTyper().encapsulate().forEach(builder::append);
        builder.append("\n");
        builder.append("public ").append(getClassName()).append("(").append(getDataTyper().getDependencyInjection()).append(") {\n");
        builder.append(getDataTyper().injectDependency());
        getDefaultAttributes().initialize().forEach(builder::append);
        builder.append("}\n\n");
        getDefaultAttributes().forEach(attribute -> builder.append(attribute.getter()).append(attribute.setter()));
        getDataTyper().listAttributes().forEach(attribute ->
                builder.append(attribute.getter()).append(attribute.setter()));
        getDefaultFunctions().forEach(string -> builder.append(string).append("\n"));
        builder.deleteCharAt(builder.length() - 1);
        builder.append("}");
        return builder.toString();
    }

    /**
     * Will attempt to create the class file in the selected package (since currently
     * it only allows to create a class inside a package).
     * Will then navigate/open to the newly created/generated class.
     * Will then reformat the class.
     */
    public void generate() {
        ReadAction.run(() -> ApplicationManager.getApplication().runWriteAction(() -> {
            try {
                PsiFile psiFile = selectedDirectory.createFile(className + ".java");
                psiFile.getContainingFile().getVirtualFile().setBinaryContent(content().getBytes(StandardCharsets.UTF_8));
                WriteCommandAction.runWriteCommandAction(psiFile.getProject(), () -> {
                    CodeStyleManager.getInstance(psiFile.getProject()).reformat(psiFile);
                });
                selectedDirectory.getVirtualFile().refresh(false, true);
                psiFile.navigate(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }

    public String getClassName() {
        return className;
    }

    private Function<String, String> getClassDeclaration() {
        return classDeclaration;
    }

    /**
     * Sets the class declaration. Starts with "public" and ends with "{".
     * The default is "public class {name} {"
     *
     * @param classDeclaration the class declaration
     */
    public void setClassDeclaration(Function<String, String> classDeclaration) {
        this.classDeclaration = classDeclaration;
    }

    /**
     * Gets the default attributes. The default attributes are the attributes that are
     * automatically generated when the class is created.
     *
     * @return the default attributes
     */
    public DefaultAttributes getDefaultAttributes() {
        return defaultAttributes;
    }

    /**
     * Gets the import collection. The import collection is the collection of
     * imports that are automatically generated when the class is created.
     * Whenever added a new import, the String should be the package name.
     * It must not include the "import" keyword nor the semicolon.
     * Example: "java.util.ArrayList"
     *
     * @return the import collection
     */
    public ImportCollection getImportCollection() {
        return importCollection;
    }

    /**
     * Gets the default functions. The default functions are the functions that are
     * automatically generated when the class is created.
     * Whenever added a new function, the String should include annotations,
     * visibility, return type, name, parameters and body.
     * Example 1: "public void setName(String name) { this.name = name; }"
     * Example 2: "@Override public String toString() { return name; }"
     *
     * @return the default functions
     */
    public List<String> getDefaultFunctions() {
        return defaultFunctions;
    }

    /**
     * Retrieves the data typer.
     *
     * @return the data typer
     */
    public DataTyper getDataTyper() {
        return dataTyper;
    }
}
