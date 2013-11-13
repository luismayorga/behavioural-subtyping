package be.ac.ua.parser;

import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.annotations.SuppressSubnodes;
import org.parboiled.support.Var;



@BuildParseTree
public class ContractParser extends BaseParser<AstNode>{

	@SuppressSubnodes
	public Rule Contract() {
		Var<AstNode> n=new Var<AstNode>(new AstNode("Contract"));
		return Sequence(String("a"),
				EOI);
	}

}


