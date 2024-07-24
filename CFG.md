# Context-Free Grammar
* used to describe the syntax of a programming language
* Components:
    * Terminals: symbols that are used to create strings ex (`1`, `+`, `;`, `variable`)
        * strings are valid sequences of terminals
        * they are sequences of characters that are arranged in the format of the rules of the grammar
        * termnals can be thought of as an endpoint because they do not lead to anything else
    * Non-terminals: represent the patterns of terminals and non-terminals defining the structure of the language
        * always will reference another rule in the grammar
    * Production Rules: the rules for how non-terminals can be replaced with terminals and non-terminals
    * Start Symbol: a special non-terminal that represents where the parsing starts

## Ambiguous Grammar
* CFG that can be turned into more than one parse tree