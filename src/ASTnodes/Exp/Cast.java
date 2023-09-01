package ASTnodes.Exp;

import ASTnodes.Class.NodeId;

public class Cast extends Exp {

	public Cast( Exp exp, NodeId id,int line) {
		super(line);
		this.exp = exp;
		this.id=id;
	}

	private Exp exp;
	private NodeId id;

	public Exp getExp() {
		return exp;
	}

	public NodeId getId() {
		return id;
	}
	
	@Override
	public String toString() 
	{
		return "(("+this.id.toString()+")"+this.exp.toString()+")";
	}

}
