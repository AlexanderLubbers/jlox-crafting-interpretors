# The Visitor Pattern
* the visitor pattern allows you to add more operations to an object without actually changing the object
* an interface should be declared and all the visit functions will be stored in this interface
* The overall class (abstract class _______) should also contain an overidable accept function.
* to accept a visitor, the accept method should be called while passing in the visitor interface. The visitor interface will be used to to call the visit function
## example provided in the book:
```
interface PastryVisitor {
    void visitBeignet(Beignet beignet);
    void visitCruller(Cruller cruller);
}
abstract class Pastry {
    abstract void accept(PastryVisitor visitor);
}
class Beignet extends Pastry {
    @Override
    void accept(PastryVisitor visitor) {
        visitor.visitBeignet(this);
    }
}
class Cruller extends Pastry {
    @Override
    void accept(PastryVisitor visitor) {
        visitor.visitCruller(this);
    }
}
```
