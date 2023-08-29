package ASTnodes.Stm;

import ASTnodes.Exp.Exp;

public class IF extends Stm{

	public IF( Exp exp2, Stm stmif, Stm stmelse) {
		exp = exp2;
		this.ifstm=stmif;
		this.elsestm=stmelse;
	}
	private Exp exp;
	private Stm ifstm;
	private Stm elsestm;
	public Exp getExp() {
		return exp;
	}

	public Stm getIfstm() {
		return ifstm;
	}

	public Stm getElsestm() {
		return elsestm;
	}

	 @Override
	 public String toString() {
		 return "if ("+this.getExp().toString()+")"+"{\n"+this.ifstm.toString()+"\n}else{\n"+this.getElsestm().toString()+"\n}";
	 }
}

