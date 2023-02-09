package com.github.anjoismysign.bloblibide.entities;

import com.github.anjoismysign.bloblibide.libraries.AlgorithmLib;
import com.github.anjoismysign.bloblibide.libraries.NamingConventions;
import com.github.anjoismysign.bloblibide.libraries.PanelLib;
import com.github.anjoismysign.bloblibide.libraries.PsiDirectoryLib;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.function.Function;

public class ClassBuilder {
    private final PsiDirectory selectedDirectory;
    private final String className;
    private final DataTyper dataTyper;
    private final ImportCollection importCollection;
    private Function<String, String> classDeclaration;
    private final DefaultAttributes defaultAttributes;

    public static Optional<ClassBuilder> fromAnActionInsideNewGroup(AnActionEvent event, boolean dynamicDataTyper){
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
        if (dynamicDataTyper){
            dataTyper = new DataTyper();
            AlgorithmLib.dynamicRun(() -> {
                String dataType = PanelLib.requestString("Enter Attribute/s","Example 1: 'String name,lastName'\n" +
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
        return Optional.of(new ClassBuilder(selectedDirectory, className, dataTyper));
    }

    public ClassBuilder(PsiDirectory selectedDirectory,
                        String className,
                        DataTyper dataTyper) {
        this.selectedDirectory = selectedDirectory;
        this.className = className;
        this.dataTyper = dataTyper;
        importCollection = new ImportCollection();
        classDeclaration = name -> "public class " + name + " {";
        defaultAttributes = new DefaultAttributes();
    }

    private String content(){
        StringBuilder builder = new StringBuilder();
        Optional<String> hasPackageName = PsiDirectoryLib.getPackageName(selectedDirectory);
        String declarePackage = hasPackageName.map(s -> "package " + s + ";\n\n").orElse("");
        builder.append(declarePackage);
        importCollection.importPackages();
        builder.append(classDeclaration.apply(className)).append("\n\n");
        defaultAttributes.encapsulate().forEach(builder::append);
        dataTyper.encapsulate().forEach(builder::append);
        builder.append("\n");
        builder.append("public ").append(className).append("() {\n");
        defaultAttributes.initialize().forEach(builder::append);
        builder.append("}\n\n");
        defaultAttributes.forEach(attribute -> builder.append(attribute.getter()).append(attribute.setter()));
        dataTyper.listAttributes().forEach(attribute ->
                builder.append(attribute.getter()).append(attribute.setter()));
        builder.deleteCharAt(builder.length() - 1);
        builder.append("}");
        return builder.toString();
    }

    public void create(){
        ApplicationManager.getApplication().runWriteAction(() ->{
            try {
                PsiFile psiFile = selectedDirectory.createFile(className + ".java");
                psiFile.getContainingFile().getVirtualFile().setBinaryContent(content().getBytes(StandardCharsets.UTF_8));
                selectedDirectory.getVirtualFile().refresh(false, true);
                psiFile.navigate(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private Function<String, String> getClassDeclaration() {
        return classDeclaration;
    }

    /**
     * Sets the class declaration. Starts with "public" and ends with "{".
     * The default is "public class {name} {"
     * @param classDeclaration the class declaration
     */
    public void setClassDeclaration(Function<String, String> classDeclaration) {
        this.classDeclaration = classDeclaration;
    }

    public DefaultAttributes getDefaultAttributes() {
        return defaultAttributes;
    }

    public ImportCollection getImportCollection() {
        return importCollection;
    }
}
