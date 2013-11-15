package be.ac.ua.contracts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.FuncDecl;
import com.microsoft.z3.Solver;
import com.microsoft.z3.StringSymbol;
import com.microsoft.z3.Symbol;
import com.microsoft.z3.Context;
import com.microsoft.z3.Z3Exception;

public class ContractPair {
	Contract superClassContract;
	Contract subClassContract;

	public ContractPair(Contract superClassContract, Contract subClassContract) {
		super();
		this.superClassContract = superClassContract;
		this.subClassContract = subClassContract;
	}

	public Contract getSuperClassContract() {
		return superClassContract;
	}

	public Contract getSubClassContract() {
		return subClassContract;
	}

	public String toString(){
		return "ContractPair: " + super.toString() + "\n" +
				superClassContract.toString() + "\n" +
				subClassContract.toString();
	}

	public void compare(){
		Context ctx=null;
		try {
			HashMap<String, String> cfg = new HashMap<String, String>();
			cfg.put("model", "true");
			ctx = new Context(cfg);
			
			// Initialize symbols
			List<Symbol> syms = new ArrayList<Symbol>();
			List<FuncDecl> funs = new ArrayList<FuncDecl>();
			
			String superformula = "(not" + superClassContract.getContent() + ")";
			String subformula = subClassContract.getContent();
			for (String id : superClassContract.getIdentifiers()) {
				StringSymbol sym = ctx.MkSymbol(id);
				syms.add(sym);
				funs.add(ctx.MkConstDecl(sym, ctx.MkIntSort()));
			}
			
			String str = "(benchmark tst :formula (and "
			+ superformula
			+ subformula
			+ ")";
			System.out.println(str);
			
			ctx.ParseSMTLIBString(str, null, null,
					(Symbol[]) syms.toArray(new Symbol[syms.size()]),
					(FuncDecl[])funs.toArray(new FuncDecl[funs.size()]));
			
			BoolExpr f = ctx.SMTLIBFormulas()[0];
			System.out.println("formula: " + f);
			
			Solver s = ctx.MkSolver();
			s.Assert(f);
			System.out.println(s.Check());
			ctx.Dispose();
		} catch (Z3Exception e) {e.printStackTrace();}
	}
}
