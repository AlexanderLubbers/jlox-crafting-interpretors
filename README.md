# jlox-crafting-interpretors
This is the jlox interpreter from the book Crafting Interpreters. The actual idea for this language and interpretor is not my own, but I am going to make sure to put my own little spin on it.
# my interpretation of how the interpretor works
1. a new instance of the Lox.java class is created when the interpretor is run and then the user is given an option to pass in file that they would like to run or run it in interactive mode. If there is more than one arguement then the interpretor will inform the user how to use it (by passing in a script or running it interactively) and then close
2. Then the user input is passed into the run function and the input first goes through the scanner class
3. in the scanner class, the start character (where character is a letter in the user input) and the current character are kept track of and while the end of the input is not reached, the scanner class will call the scan token function which will use a switch statement to search for tokens
4. if something like a comma is detected then a comma token will be added to the token list and then the start variable will be set equal to the current variable to "consume" the characters that make up the token

# things I added to the interpretor
* C-style block comments
* reverse polish notation
