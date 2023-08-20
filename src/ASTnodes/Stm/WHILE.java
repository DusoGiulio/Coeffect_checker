package ASTnodes.Stm;

import ASTnodes.Exp.Exp;

public class WHILE extends Stm {

	private Exp exp;
	private Stm stm1;
	
	public WHILE( Exp exp, Stm stm1) {
		this.exp = exp;
		this.stm1 = stm1;
	}

	public Exp getExp() {
		return exp;
	}

	public Stm getStm1() {
		return stm1;
	}


}
