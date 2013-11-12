package be.ac.ua.parser;

import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.support.Var;

public class ContractParser extends BaseParser<AstNode>{

	// Contract -> Exp
	public Rule Contract(){
		Var<AstNode> n=new Var<AstNode>(new AstNode("Contract"));
		return Sequence(
				Optional(Expression()),
				push(n.get()));
	}

	// Exp -> (Exp) | Exp op Exp | Lit op Lit 
	Rule Expression(){
		return FirstOf(
				Sequence(OPENPAR,Expression(),CLOSEPAR),
				Sequence(Literal(),Operator(),Literal(),
				Sequence(Expression(),Operator(),Expression())));
	}

	// Lit -> LogicalValue | Number | String | NullVal | (Lit)
	Rule Literal(){
		return FirstOf(LogicalValue(),
				Number(),
				StringLit(),
				NullValue(),
				Sequence(OPENPAR,
						Literal(),
						CLOSEPAR));
	}

	// Operator -> || | && | = | == | + | - | / | *
	Rule Operator(){
		return FirstOf(
				ORDOP,
				ANDOP,
				EQOP,
				EQEQOP,
				PLUSOP,
				MINOP,
				DIVOP,
				STAROP
				);
	}

	//Identifier
	Rule Identifier(){
		return Sequence(IdentifierValidChar(),
				ZeroOrMore(FirstOf(Digit(),
						IdentifierValidChar())));
	}

	// Literals

	// LogicalValue -> true | false
	Rule LogicalValue(){
		return FirstOf(
				String("true"), 
				String("false"));
	}


	// SimpleNum -> Digit+
	Rule SimpleNum(){
		return OneOrMore(Digit());
	}

	// Number -> Digit+[.Digit+]
	Rule Number(){
		return FirstOf(
				Sequence(HEXPREFIX,SimpleNum()),
				Sequence(SimpleNum(),ACCOP,SimpleNum()));
	}

	// String -> "[^"]"
	Rule StringLit(){
		return Sequence(String("\""),
				TestNot(String("\"")),
				String("\""));
	}

	// Basic types

	//NullVal -> "null"
	Rule NullValue(){
		return String("null");
	}
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
		return OneOrMore(AnyOf("\t "));
	}

	Rule ACCOP = String(".");
	Rule STAROP = String("*");
	Rule ANDOP = String("&&");
	Rule ORDOP = String("||");
	Rule EQOP = String("=");
	Rule EQEQOP = String("==");
	Rule PLUSOP = String("+");
	Rule MINOP = String("-");
	Rule DIVOP = String("/");
	Rule OPENPAR = String("(");
	Rule CLOSEPAR = String(")");
	Rule HEXPREFIX = String("0x");
}
