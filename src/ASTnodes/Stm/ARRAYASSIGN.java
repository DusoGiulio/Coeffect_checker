package ASTnodes.Stm;

import ASTnodes.Exp.Exp;

public class ARRAYASSIGN extends ASSIGN{
	private Exp rexp;

	public ARRAYASSIGN( Exp lexp, Exp rexp, String id) {
		super(lexp,id);
		this.rexp = rexp;
	}


	public Exp getRexp() {
		return rexp;
	}
}
