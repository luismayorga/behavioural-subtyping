Behavioural Subtype Checker
===========================

##Overview

Checker developed as a proof of concept for the University of Antwerp. 

Checks if a class is a behavioural subtype of another, i.e. complies with the Liskov Substitution Principle  comparing the contracts of all overriding field and methods and ensuring that they do not impose more restrictive conditions than their superclass equals. While there are several tools that provide this functionality at runtime, is yet to be developed one that serves statically for the same purpose. This one diggs into that possibility.

The contracts are written by the developer in formal logic as Java annotations. They are then composed in a more complex formula and feeded to Microsoft Research's [Z3 SMT Solver](http://z3.codeplex.com/).

Two kind of annotations are supported:

*     **Requires**: Preconditions for methods.
*     **Invariant**: Constraints that must be preserved inside a code block. 

##Usage

The contracts must be written in [SMT-Lib](http://www.smtlib.org/) format. As this is a proof of concept of static verification of the LSP it should be used as a basis for future work but it is not suitable for its use in a production environment.
Some of the limitations that posseses are that values used in contracts must be integer values and are not checked against the method definition. It does not guarantee that the contract specified for an element is respected within its code neither.

Some examples of how to attach a contract to a method are shown below.

```Java
//x must always be equal to 2
@requires("(= x 2)")
int foo(int x){...}
```

```Java
///x must be between 0 and 2 and y must be less than x
@requires("(and (and (> x 0) (< x 2)) (< y x))")
int bar(int x, int y){...}
```

If any of these methods were overrided and the behaviour specified within it would imply breaking the LSP, the tool will alert the user at compile time.

## Further information

More information about the motivation for this work and its usage can be found in the related [paper](report.pdf).
