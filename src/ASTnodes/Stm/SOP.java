package ASTnodes.Stm;

import ASTnodes.Exp.Exp;

public class SOP extends Stm {
	private Exp exp;

	public SOP( Exp exp ) {
		this.exp=exp;
	}

	public Exp getLExp() {
		return exp;
	}

}
