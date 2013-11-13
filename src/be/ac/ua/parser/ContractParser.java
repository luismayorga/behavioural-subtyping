package be.ac.ua.parser;

import java.util.Vector;

import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.annotations.DontLabel;
import org.parboiled.annotations.MemoMismatches;
import org.parboiled.annotations.SuppressNode;
import org.parboiled.annotations.SuppressSubnodes;
import org.parboiled.support.StringVar;
import org.parboiled.support.Var;



@BuildParseTree
public class ContractParser extends BaseParser<AstNode>{

	@SuppressSubnodes
 	public Rule Contract() {
		Var<AstNode> n=new Var<AstNode>(new AstNode("Contract"));
		return Sequence(ZeroOrMore(Sequence(ConditionalOrExpression(),
											n.get().addChild("has", pop()))),
						EOI,
						push(n.get()));
	}
 	
//	Rule Expression() {
//		Var<AstNode> n=new Var<AstNode>(new AstNode("If"));
//		return Sequence(
//				ConditionalOrExpression(),
//				Optional(QUERY, Expression(), COLON, ConditionalOrExpression(),
//				n.get().addChild("else", pop()), n.get().addChild("if", pop()), n.get().addChild("condition", pop()))
//		);
//	}
	
	Rule ConditionalOrExpression() {
		Var<AstNode> n=new Var<AstNode>(new AstNode("Operation"));
		Var<Vector<AstNode>> params=new Var<Vector<AstNode>>(new Vector<AstNode>());
		return Sequence(
				ConditionalAndExpression(),
				ZeroOrMore(OROR, n.get().addAttribute("operator", match().trim()), params.get().add(pop()),
				ConditionalAndExpression(), params.get().add(pop()), addParams(n.get(), params.get(), 1), push(n.get()), n.set(new AstNode("Operation")))
		);
	}
	
	Rule ConditionalAndExpression() {
		Var<AstNode> n=new Var<AstNode>(new AstNode("Operation"));
		Var<Vector<AstNode>> params=new Var<Vector<AstNode>>(new Vector<AstNode>());
		return Sequence(
				EqualityExpression(),
				ZeroOrMore(ANDAND, n.get().addAttribute("operator", match().trim()), params.get().add(pop()),
				EqualityExpression(), params.get().add(pop()), addParams(n.get(), params.get(), 1), push(n.get()), n.set(new AstNode("Operation")))
		);
	}
	
	Rule EqualityExpression() {
		Var<AstNode> n=new Var<AstNode>(new AstNode("Operation"));
		Var<Vector<AstNode>> params=new Var<Vector<AstNode>>(new Vector<AstNode>());
		return Sequence(
				RelationalExpression(),
				ZeroOrMore(FirstOf(EQUAL, NOTEQUAL), n.get().addAttribute("operator", match().trim()), params.get().add(pop()),
				RelationalExpression(), params.get().add(pop()), addParams(n.get(), params.get(), 1), push(n.get()), n.set(new AstNode("Operation")))
		);
	}
	
	Rule RelationalExpression() {
		Var<AstNode> n=new Var<AstNode>(new AstNode("Operation"));
		Var<Vector<AstNode>> params=new Var<Vector<AstNode>>(new Vector<AstNode>());
		return Sequence(
				AdditiveExpression(),
				ZeroOrMore(FirstOf(LE, GE, LT, GT), n.get().addAttribute("operator", match().trim()), params.get().add(pop()),
						AdditiveExpression(), params.get().add(pop()), addParams(n.get(), params.get(), 1), push(n.get()), n.set(new AstNode("Operation")))
		);
	}
	
	Rule AdditiveExpression() {
		Var<AstNode> n=new Var<AstNode>(new AstNode("Operation"));
		Var<Vector<AstNode>> params=new Var<Vector<AstNode>>(new Vector<AstNode>());
		return Sequence(
				MultiplicativeExpression(),
				ZeroOrMore(FirstOf(PLUS, MINUS), n.get().addAttribute("operator", match().trim()), params.get().add(pop()),
				MultiplicativeExpression(), params.get().add(pop()), addParams(n.get(), params.get(), 1), push(n.get()), n.set(new AstNode("Operation")))
		);
	}
	
	Rule MultiplicativeExpression() {
		Var<AstNode> n=new Var<AstNode>(new AstNode("Operation"));
		Var<Vector<AstNode>> params=new Var<Vector<AstNode>>(new Vector<AstNode>());
		return Sequence(
				UnaryExpression(),
				ZeroOrMore(FirstOf(STAR, DIV, MOD), n.get().addAttribute("operator", match().trim()), params.get().add(pop()),
				UnaryExpression(), params.get().add(pop()), addParams(n.get(), params.get(), 1),  push(n.get()), n.set(new AstNode("Operation")))
		);
	}
	
	Rule UnaryExpression() {
		Var<AstNode> n=new Var<AstNode>(new AstNode("Operation"));
		Var<Vector<AstNode>> params=new Var<Vector<AstNode>>(new Vector<AstNode>());
		return FirstOf(
				Sequence(Terminal("!"), UnaryExpression(), 
						params.get().add(pop()), addParams(n.get(), params.get(), 1), n.get().addAttribute("operator", "!"), push(n.get())),
				Primary()
		);
	}
	
	Rule StringLiteral() {
		Var<AstNode> n=new Var<AstNode>(new AstNode("String"));
		return Sequence(
				'"',
				ZeroOrMore(Sequence(TestNot(AnyOf("\r\n\"\\")), ANY)).suppressSubnodes(),
				n.get().addAttribute("value", match()), push(n.get()),
				'"');
	}
	
	Rule Primary() {
		return FirstOf(
				StringLiteral(),
				IntegerLiteral(),
				BooleanLiteral(),
				IdentifierNode()
		);
	}
	
	@SuppressSubnodes
	Rule IntegerLiteral() {
		Var<AstNode> n=new Var<AstNode>(new AstNode("Integer"));
		return Sequence(FirstOf('0', Sequence(CharRange('1', '9'), ZeroOrMore(Digit()))),
				n.get().addAttribute("value", match().trim(), "int"), push(n.get()));
	}
	
	Rule BooleanLiteral() {
		Var<AstNode> n=new Var<AstNode>(new AstNode("Boolean"));
		return Sequence(FirstOf(Keyword("true"), Keyword("false")),
				n.get().addAttribute("value", match().trim(), "bool"), push(n.get()));
	}
	
	Rule Digit() {
		return CharRange('0', '9');
	}
	
	@SuppressSubnodes
	@MemoMismatches
	Rule IdentifierNode() {
		Var<AstNode> n=new Var<AstNode>(new AstNode("Identifier"));
		StringVar str=new StringVar();
		return Sequence(Identifier(str), n.get().addAttribute("name", str.get()), push(n.get()));
	}
	
	@SuppressSubnodes
	@MemoMismatches
	Rule Identifier(StringVar str) {
		return Sequence(TestNot(Keyword()), Sequence(Letter(), ZeroOrMore(LetterOrDigit())), str.set(match()), Spacing());
	}
	
	@MemoMismatches
	Rule Keyword() {
		return Sequence(
				FirstOf("class", "static", "if", "while", "this", "print", "var", "return", "new", "quote",
						"unquote", "try", "catch", "throw"),
						TestNot(LetterOrDigit())
		);
	}
	
	@SuppressNode
	@DontLabel
	Rule Keyword(String keyword) {
		return Terminal(keyword, LetterOrDigit());
	}
	
	Rule Letter() {
		// switch to this "reduced" character space version for a ~10% parser performance speedup
		//return FirstOf(CharRange('a', 'z'), CharRange('A', 'Z'), '_', '$');
		return new LetterMatcher();
	}
	
	@MemoMismatches
	Rule LetterOrDigit() {
		// switch to this "reduced" character space version for a ~10% parser performance speedup
		//return FirstOf(CharRange('a', 'z'), CharRange('A', 'Z'), CharRange('0', '9'), '_', '$');
		return new LetterOrDigitMatcher();
	}
	
	@SuppressNode
	Rule Spacing() {
		return 
				// whitespace
				OneOrMore(AnyOf(" \t\f").label("Whitespace"));
	}
	
	@SuppressNode
	@DontLabel
	Rule Terminal(String string) {
		return Sequence(Spacing(), string, Spacing()).label('\'' + string + '\'');
	}
	
	@SuppressNode
	@DontLabel
	Rule Terminal(String string, Rule mustNotFollow) {
		return Sequence(Spacing(), Sequence(string, TestNot(mustNotFollow)).label('\'' + string + '\''), Spacing());
	}
	
	/*
	 * Adds a list of parameters to a node
	 * @param node			the node
	 * @param params		the list of parameters; will be cleared at the end of the method
	 * @param startIndex	the starting index of the first parameter (usually 1)
	 * @return				true
	 */
	protected boolean addParams(AstNode node, Vector<AstNode> params, int startIndex) {
		for(int i=0;i<params.size();++i){
			node.addChild("param", params.get(i));
			params.get(i).addAttribute("parnum", new Integer(i+startIndex).toString(), "int");
		}
		params.clear();
		return true;
	}
	
	final Rule ANDAND = Terminal("&&");
	final Rule ANDEQU = Terminal("&=");
	final Rule COLON = Terminal(":");
	final Rule COMMA = Terminal(",");
	final Rule DIV = Terminal("/", Ch('='));
	final Rule DOT = Terminal(".");
	final Rule EQU = Terminal("=", Ch('='));
	final Rule EQUAL = Terminal("==");
	final Rule GE = Terminal(">=");
	final Rule GT = Terminal(">", AnyOf("=>"));
	final Rule LBRK = Terminal("[");
	final Rule LE = Terminal("<=");
	final Rule LPAR = Terminal("(");
	final Rule LPOINT = Terminal("<");
	final Rule LT = Terminal("<", AnyOf("=<"));
	final Rule LCURLY = Terminal("{");
	final Rule MINUS = Terminal("-", AnyOf("=-"));
	final Rule MINUSEQU = Terminal("-=");
	final Rule MOD = Terminal("%", Ch('='));
	final Rule NOTEQUAL = Terminal("!=");
	final Rule OROR = Terminal("||");
	final Rule PLUS = Terminal("+", AnyOf("=+"));
	final Rule QUERY = Terminal("?");
	final Rule RBRK = Terminal("]");
	final Rule RPAR = Terminal(")");
	final Rule RPOINT = Terminal(">");
	final Rule RCURLY = Terminal("}");
	final Rule SEMI = Terminal(";");
	final Rule SL = Terminal("<<", Ch('='));
	final Rule STAR = Terminal("*", Ch('='));
}
