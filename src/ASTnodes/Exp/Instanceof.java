package ASTnodes.Exp;

import ASTnodes.Class.NodeId;

public class Instanceof extends Exp{
	
	private Exp exp;
	private NodeId id;

	public Instanceof(NodeId id, Exp exp) {
		this.id=id;
		this.exp = exp;
	}

	public Exp getExp() {
		return exp;
	}

	public NodeId getId() {
		return id;
	}

	public void setId(NodeId id) {
		this.id = id;
	}
	@Override
	public String toString() 
	{
		return "("+exp.toString()+" instanceof "+id.toString()+")";
	}

}

