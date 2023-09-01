package ASTnodes.Stm;

import ASTnodes.Exp.Exp;

public class ASSIGN extends Stm{
	
	private String id;
	private Exp exp;
	
	public ASSIGN(Exp exp, String id, int line) {
		super(line);
		this.exp=exp;
		this.id= id;

	}

	public Exp getLExp() {
		return exp;
	}

	public String getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return this.id+"="+this.getLExp().toString()+";";
	}
}
