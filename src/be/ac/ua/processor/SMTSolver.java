package be.ac.ua.processor;

import com.microsoft.z3.Context;
import com.microsoft.z3.Z3Exception;

public class SMTSolver {
	
	private static SMTSolver me;
	private Context ctx;

	private SMTSolver() {
	}
	
	public static SMTSolver getInstance(){
		if(me==null){
			me = new SMTSolver();
		}
		return me;
	}
	
	boolean isMoreRestrictiveThan(String A, String B){
		try {
			ctx = new Context();
		} catch (Z3Exception e) {e.printStackTrace();}
		ctx.Dispose();
		return false;
	}
	
}
