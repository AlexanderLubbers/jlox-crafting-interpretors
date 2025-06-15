# Programming languages
## Lexical Analysis / Lexing
* first part of developing a programming language
* takes a stream of characters and turns them into tokens
* each group of characters is called a lexeme
## Parsing
* the part where syntax gets to compose larger expressions and gets to have meaning
* the parser takes the tokens created from the lexing process and builds a tree structure
* this tree structure represents the meaning of the grammar
* these trees can be caled parse tree or abstract syntax tree
* must also report syntax errors
## Static analysis
* resolution: find where each identifer is defined and use the definiton in the parse tree
* statically typed programming languages are programming languages where the type must be specified (c, c++)
* type checks if applicable occur during this time
* information attained during this time are stored as attributes in the syntax tree, a look up table(symbol table) with the keys being the identifiers, and data structures
## Intermediate representations
* serves as an interface between the language the user uses and the language of machines
## Optimization
* make a programming langauge use resources more efficiently
* key words: "constant propogation", "common subexpression elimination", "loop invariant code motion", "global value numbering", "strength reduction", "scalar replacement of aggregates", "dead code elimination", "loop unrolling"
## Code generation
* convert the code to a language a machine can understand
* should the instructions be for a virtual or real CPU
* code for an idealized machine (VM) is called bytecode
* code that runs on a VM can be thought of as "a dense, binary encoding of a languages low level operations".
## Virtual machines
* represents an idealized chip that supports a certain kind of architecture
* VMs are slow. This is because the code has to be emualated
* advantages: simplicity and portability
## Transpilers
* translate the language into another programming language and then the code is run
## Tree-walk interpreters
* slow
* execute the code as soon as it is parsed
## Single pass compilers
* parsing, lexical analysis, and code generation all occur in the parser
* restrict the design of the language
* no intermediate data strucutures. This means that the interpretor or compiler must know enough information to run the code as soon as it sees it
## Just in time compilation
* dangerous
* fast
* compiles to machine code
# Miscellaneous
## comma expressions
* evaluates expressions from left to right
* then returns the right most expression
example:
```
a++, array.pop(), add(1, 2)
```
returns:
```
3
```


