package com.jlox;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Lox {
    static boolean hadError = false;
    //the throws keyword can be used to declare exceptions that occur during the execution of code
    //IO exception occurs when there is an error recieving an input or while outputing something
    //String[] args is used to get input
    //functions with the static keyword can be accessed without an instance of the class it is a member of being created
    public static void main(String[] args) throws IOException {
        if(args.length > 1) {
            System.out.println("Usages: jlox [script]");
            System.exit(64);
        } else if(args.length == 1) {
            //a file path was passed in
            runFile(args[0]);
        } else {
            //if no arguments are passed in, then jlox will be ran interactively
            //this means that the user can enter and then execute code one line at a time
            runPrompt();
        }
    }
     private static void runFile(String path) throws IOException {
        //read all bytes reads all the bytes in a file and then stores it in the bytes variable
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        //turns the bytes into characters
        run(new String(bytes, Charset.defaultCharset()));
    }
    private static void runPrompt() throws IOException{
        //use the standard input stream as the source of input
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        for(;;) {
            System.out.print("> ");
            String line = reader.readLine();
            //ctrl-d was typed which is the end of file condition
            //that means that readline returns null
            if (line == null) break;
            run(line);
            hadError = false;
        }
    }
    private static void run(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();
        // For now, just print the tokens.
        //for every token in the list
        for (Token token : tokens) {
            System.out.println(token);
        }
        if(hadError) {
            System.exit(65);
        }
    }
    static void error(int line, String message) {
        report(line, "", message);
    }
    private static void report(int line, String where, String message) {
        System.err.println("[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }
    
}
