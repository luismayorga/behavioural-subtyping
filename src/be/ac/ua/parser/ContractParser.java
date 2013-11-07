package be.ac.ua.parser;

import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.support.Var;

public class ContractParser extends BaseParser<AstNode>{

	// Contract -> Exp
	public Rule Contract(){
		Var<AstNode> n=new Var<AstNode>(new AstNode("Contract"));
		return Sequence(Optional(Expression()),
				push(n.get()));
	}
	
	// Exp -> Exp | (Exp) | Exp op Exp | Lit op Lit 
	Rule Expression(){
		return FirstOf(Sequence(Spacing(),Expression(),Spacing()),
				Sequence(OPENPAR,Expression(),CLOSEPAR),
				Sequence(Expression(),Operator(),Expression()),
				Sequence(Literal(),Operator(),Literal()));
	}


	// Lit -> LogicalValue | Number | String
	Rule Literal(){
		return FirstOf(LogicalValue(),
				Number(),
				StringLit());
	}
	
	// Operator -> || | && | = | == | + | - | / | *
	Rule Operator(){
		Var<AstNode> n=new Var<AstNode>(new AstNode("Operator"));
		return Sequence(FirstOf(ACCOP,STAROP),
				n.get().addAttribute("value", match().trim(), "string"),
				push(n.get()));
	}
	
	// Literals

	// LogicalValue -> true | false
	Rule LogicalValue(){
		Var<AstNode> n=new Var<AstNode>(new AstNode("Boolean"));
		return Sequence(FirstOf(String("true"), String("false")),
				n.get().addAttribute("value", match().trim(), "bool"),
				push(n.get()));
	}
	
	// Number -> {Digit}[.{Digit}]
	Rule Number(){
		Var<AstNode> n=new Var<AstNode>(new AstNode("Number"));
		return Sequence(
				OneOrMore(Digit()),	Optional(Sequence(ACCOP, OneOrMore(Digit()))),
				n.get().addAttribute("value", match().trim(), "int"),
				push(n.get()));
	}
	
	// String -> "[^"]"
	Rule StringLit(){
		return Sequence(String("\""),
				TestNot(String("\"")),
						String("\""));
	}

	// Basic types

	// Digit -> 0-9
	Rule Digit(){
		return CharRange('0', '9');
	}

	// IdValidChar -> a-zA-Z$_
	Rule IdentifierValidChar(){
		return new LetterMatcher();
	}

	// Spacing -> "\"* | t*
	Rule Spacing(){
		return OneOrMore(AnyOf("\t ").label("Whitespace"));
	}

	Rule ACCOP = String(".");
	Rule STAROP = String("*");
	Rule OPENPAR = String("(");
	Rule CLOSEPAR = String(")");
}
