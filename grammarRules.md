# Grammar Rules
* The asterisk means that the items in the parenthesis can be repeated one or more times.
* the items covered go from lowest on the heirarchy to the highest.
## Heirarchy
precedence is from lowest to highest
```
Name Operators Associates
Equality     == !=         Left
Comparison   > >= < <=     Left
Term         - +           Left
Factor       / *           Left
Unary        ! -           Right
```
## Expression
```
expression → equality ;
```
* states that an expression consists of an equality.
* the phrase equality can be replaced with another item that is higher up on the heirarchy.
## Equality
```
equality → comparison ( ( "!=" | "==" ) comparison )* ;
```
* states that an equality consists of a comparison followed by one or more occurances of an equality operaton and another operator.
* the phrase comparison can be replaced with another item that is higher up on the heirarchy.
ex: 
```
a == b
a == b == c
a != b == c
```
## Comparison
```
comparison → term ( ( ">" | ">=" | "<" | "<=" ) term )* ;
```
* states that a comparison consists of a term followed by one or more occurances of a comparison operator followed by another term
* the phrase term can be replaced with another item that is higher up on the heirarchy.
ex:
```
a >= b
a > b < c
```
## Term
```
term → factor ( ( "-" | "+" ) factor )* ;
```
* states that a term is a factor followed one or more occurances of "-" or "+" followed by another factor.
* the phrase factor can be replaced with another item that is higher up on the heirarchy.
## Factor
```
factor → unary ( ( "/" | "*" ) unary )* ;
```
* states that a factor is a unary folowed by one or more "/" or "*" followed by another unary
* unary can be replaced by an item that is higher up on the heirarchy
## Unary
```
unary → ( "!" | "-" ) unary
    | primary ;
```
## Primary
```
primary → NUMBER | STRING | "true" | "false" | "nil"
    | "(" expression ")" ;
```
## More Examples
```
3 > 2
```
This matches the grammar for the comparison rule. That rule states that a comparison is composed of term followed by one or more occurances of a comparison operator followed by another term. because "3" and "2" are primaries, they are also valid terms which means that 3 > 2 is a valid comparison expression