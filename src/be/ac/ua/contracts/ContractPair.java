package be.ac.ua.contracts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.tools.Diagnostic.Kind;

import be.ac.ua.processor.AnnotationProcessor;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.FuncDecl;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;
import com.microsoft.z3.StringSymbol;
import com.microsoft.z3.Symbol;
import com.microsoft.z3.Context;
import com.microsoft.z3.Z3Exception;

/**
 * Pair of contracts of the same type corresponding to both elements of a
 * hierarchy.
 * 
 * @author Luis Mayorga
 */
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

	/**
	 * Uses the SMT Solver Z3 to check wheter the contract corresponding to the
	 * element of the subclass is more restrictive, i.e. more difficult to satisfy
	 * than the one of the superclass.
	 */
	public void compare(){
		Context ctx=null;
		try {
			HashMap<String, String> cfg = new HashMap<String, String>();
			cfg.put("model", "true");
			ctx = new Context(cfg);

			// Initialize symbols
			List<Symbol> syms = new ArrayList<Symbol>();
			List<FuncDecl> funs = new ArrayList<FuncDecl>();

			String superformula = superClassContract.getContent();
			String subformula = "(not " + subClassContract.getContent() + ")";
			for (String id : superClassContract.getIdentifiers()) {
				StringSymbol sym = ctx.MkSymbol(id);
				syms.add(sym);
				funs.add(ctx.MkConstDecl(sym, ctx.MkIntSort()));
			}

			String str = "(benchmark tst :formula (and "
					+ superformula
					+ subformula
					+ ")";

			ctx.ParseSMTLIBString(str, null, null,
					(Symbol[]) syms.toArray(new Symbol[syms.size()]),
					(FuncDecl[])funs.toArray(new FuncDecl[funs.size()]));

			BoolExpr f = ctx.SMTLIBFormulas()[0];
			Solver s = ctx.MkSolver();
			s.Assert(f);
			if(s.Check().equals(Status.SATISFIABLE)){
				if(	AnnotationProcessor.getTypeUtils().isSameType(superClassContract.getAm().getAnnotationType(),
						AnnotationProcessor.getElementUtils().getTypeElement("be.ac.ua.annotations.requires").asType())){
					AnnotationProcessor.getMessager().printMessage(Kind.ERROR,
							"The precondition on the subclass is more restrictive",
									subClassContract.getEm(), subClassContract.getAm());

				}else if(AnnotationProcessor.getTypeUtils().isSameType(superClassContract.getAm().getAnnotationType(),
						AnnotationProcessor.getElementUtils().getTypeElement("be.ac.ua.annotations.invariant").asType())){

					AnnotationProcessor.getMessager().printMessage(Kind.ERROR,
							"The invariant on the subclass is more restrictive",
									subClassContract.getEm(), subClassContract.getAm());
				}
			}
			ctx.Dispose();
		} catch (Z3Exception e) {e.printStackTrace();}
	}
}
