package be.ac.ua.contracts;

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
			ctx = new Context();
		} catch (Z3Exception e) {e.printStackTrace();}
		ctx.Dispose();
		
	}
}
