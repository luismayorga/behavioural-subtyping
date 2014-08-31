Behavioural Subtype Checker
===========================

Checker developed as a proof of concept for the University of Antwerp. 

Checks if a class is a behavioural subtype of another, i.e. complies with the Liskov Substitution Principle  comparing the contracts of all overriding field and methods and ensuring that they do not impose more restrictive conditions than their superclass equals. It does not guarantee that the contract specified for an element is respected in the code within it.

For the comparison, the [Z3 SMT Solver](http://z3.codeplex.com/), by Microsoft Research, is used.

While there are several tools that provide this functionality at runtime, is yet to be developed one that serves statically for the same purpose. This one diggs into that possibility.

Two kind of annotations are supported:

*     **Requires**: Preconditions for methods.
*     **Invariant**: Constraints that must be preserved inside a code block. 


As stated, this work is only a proof of concept and is not suitable for its use in a verification process.

The contracts must be written in [SMT-Lib](http://www.smtlib.org/) format and only integer values are accepted.

More information about the motivation for this work and its usage can be found in the related [paper](report.pdf).
