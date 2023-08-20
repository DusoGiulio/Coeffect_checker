package ASTnodes.Exp;

import ASTnodes.Class.NodeId;

public class Cast extends Exp {

	public Cast( Exp exp, NodeId id) {
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


}
