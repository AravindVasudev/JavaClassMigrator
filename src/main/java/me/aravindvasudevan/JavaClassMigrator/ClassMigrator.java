package me.aravindvasudevan.JavaClassMigrator;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public class ClassMigrator {
    private String inputClassLocation;
    private CompilationUnit inputClassCU;

    private String outputClassLocation;
    private String outputClassName;
    private CompilationUnit outputClassCU;
    private ClassOrInterfaceDeclaration outputClass;

    public ClassMigrator(String inputClassLocation, String outputClassLocation) throws Exception {
        this.inputClassLocation  = inputClassLocation;
        this.outputClassLocation = outputClassLocation;

        // Parse Input Class File
        inputClassCU = JavaParser.parse(new FileInputStream(inputClassLocation));

        // Extract classname from path, if file exists, use it else create new CompilationUnit
        File op = new File(outputClassLocation);

        outputClassName = op.getName();
        outputClassName = outputClassName.substring(0, outputClassName.lastIndexOf("."));

        if (op.exists() && !op.isDirectory()) {
            outputClassCU = JavaParser.parse(op);
            outputClass = outputClassCU.getClassByName(outputClassName).get();
        } else {
            outputClassCU = new CompilationUnit();
            outputClass = outputClassCU.addClass(outputClassName);
        }
    }

    private boolean doesContainProperty(ClassOrInterfaceDeclaration cl, FieldDeclaration fd) {
        return cl.getFieldByName(fd.getVariables().get(0).getNameAsString()).isPresent();
    }

    private class MethodVisitor extends VoidVisitorAdapter<List<String>> {
        @Override
        public void visit(MethodDeclaration n, List<String> methodList) {
            if (methodList.contains(n.getName().toString()) && !outputClass.containsWithin(n)) {
                outputClass.addMember(n);
            }
        }
    }

    private class PropertyVisitor extends VoidVisitorAdapter<List<String>> {
        @Override
        public void visit(FieldDeclaration fd, List<String> propertyList) {
            if (propertyList.contains(fd.getVariables().get(0).getNameAsString()) && !doesContainProperty(outputClass, fd)) {
                outputClass.addMember(fd);
            }
        }
    }

    public void movePackageDeclaration() {
        outputClassCU.setPackageDeclaration(inputClassCU.getPackageDeclaration().get());
    }

    public void movePropertyDeclarations(List<String> propertyList) {
        if (propertyList == null) return;
        inputClassCU.accept(new PropertyVisitor(), propertyList);
    }

    public void moveMethodDeclarations(List<String> methodList) {
        if (methodList == null) return;
        inputClassCU.accept(new MethodVisitor(), methodList);
    }

    public boolean save() {
        try (PrintWriter out = new PrintWriter(outputClassLocation)) {
            out.println(outputClassCU.toString());
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }
}