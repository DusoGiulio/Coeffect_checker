package ASTnodes.Stm;

import ASTnodes.Exp.Exp;

public class SOP extends Stm {
	private Exp exp;

	public SOP( Exp exp , int line) {
		super(line);
		this.exp=exp;
	}

	public Exp getLExp() {
		return exp;
	}

	@Override
	public String toString() 
	{
		return "System.out.println("+this.exp.toString()+");";
	}
}
