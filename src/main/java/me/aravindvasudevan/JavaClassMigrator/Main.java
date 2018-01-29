package me.aravindvasudevan.JavaClassMigrator;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        ArrayList methodList = null;
        List<String> propertyList = null;

        if (args.length < 3) {
            System.err.println("Invalid args.");
            System.exit(1);
        }

        // ClassMigrator takes input class location and output class location
        ClassMigrator cc = new ClassMigrator(args[0], args[1]);

        // Check third argument to determine which nodes to move
        switch (args[2]) {
            case "-m":
                methodList = new ArrayList();
                for (int i = 2; i < args.length; i++) {
                    methodList.add(args[i]);
                }
                break;

            case "-p":
                propertyList = new ArrayList();
                for (int i = 2; i < args.length; i++) {
                    propertyList.add(args[i]);
                }
                break;

            default:
                System.err.println("Invalid flag.");
                System.exit(1);
        }

        cc.movePackageDeclaration();
        cc.movePropertyDeclarations(propertyList);
        cc.moveMethodDeclarations(methodList);
        cc.save();

        System.out.println("done.");
    }
}
