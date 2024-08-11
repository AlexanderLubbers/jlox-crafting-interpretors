package com.tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

//this is a script that will create a Expr.java file that holds the syntax trees
public class GenerateAst {
    public static void main(String[] args) throws IOException {
        if(args.length != 1) {
            // inform the user that they are not using the command line tool correctly
            System.out.println("Usage: generage_ast <output directory>");
            System.exit(64);
        }
        String outputDir = args[0];
        //the list follows a format of name and then a list of the fields
        defineAst(outputDir, "Expr", Arrays.asList(
        "Binary : Expr left, Token operator, Expr right",
            "Grouping : Expr expression",
            "Literal : Object value",
            "Unary : Token operator, Expr right"
        ));
    }
    public static void defineAst(String outputDir, String baseName, List<String> types) throws IOException {
        String path = outputDir + "/" + baseName + ".java";
        //creates a writer that writes to the given output path
        PrintWriter printWriter = new PrintWriter(path, "UTF-8");
        printWriter.println("package com.jlox;");
        printWriter.println("");
        //and an import statement at the top of the new file
        printWriter.println("import java.util.List;");
        //declare a class
        printWriter.println("abstract class " + baseName + "{");
        // The base accept() method.
        printWriter.println();
        printWriter.println(" abstract <R> R accept(Visitor<R> visitor);");
        defineVisitor(printWriter, baseName, types);
        //loop through the entire type list
        for(String type : types) {
            //get the className by getting everything before the colon not including it
            //the trim function removes all the white space from the beginning and end of the string
            String className = type.split(":")[0].trim();
            //get the field list by getting everything after the colon not including it
            //the trim function removes all the white space from the beginning and end of the string
            String fieldList = type.split(":")[1].trim();
            defineType(printWriter, baseName, className, fieldList);
        }
        
        printWriter.println("}");
        printWriter.close();
    }
    private static void defineType(PrintWriter writer, String baseName, String className, String fieldList) {
        writer.println("    static class " + className + " extends " + baseName + " {");
        //create a constructor
        writer.println("        " + className + "(" + fieldList + ") {");
        //store the fields
        String[] fields = fieldList.split(", ");
        for(String field : fields) {
            String name = field.split(" ")[1];
            writer.println("            this." +  name + " = " + name + ";");
        }
        writer.println("        }");
        writer.println(" ");
        // Visitor pattern.
        writer.println();
        writer.println("        @Override");
        writer.println("        <R> R accept(Visitor<R> visitor) {");
        writer.println("            return visitor.visit" +
            className + baseName + "(this);");
        writer.println("        }");
        for(String field : fields) {
            writer.println("        final " + field + ";");
        }
        writer.println("    }");
    }
    private static void defineVisitor(PrintWriter writer, String baseName, List<String> types) {
        //R stands for any return type. This is important because multiple expressions could lead to
        writer.println("    interface Visitor<R> {");
        for(String type: types) {
            String typeName = type.split(":")[0].trim();
            writer.println("        R visit" + typeName + baseName + "(" +
                typeName + " " + baseName.toLowerCase() + ");");

        }
        writer.println("    }");
    }
}
//problems:
//final ___ is in accept function
//abstract is at the bottom
//return visitor . . . needs one more tab
//