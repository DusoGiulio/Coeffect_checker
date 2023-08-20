package ASTnodes.Exp;

import ASTnodes.Class.NodeId;

public class Id extends Exp {

	private NodeId id;
	
	public Id(NodeId id) {
		this.id=id;
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
		return id.getName();
	}
}
