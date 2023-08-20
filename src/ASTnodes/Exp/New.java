package ASTnodes.Exp;

import ASTnodes.Class.NodeId;


public class New extends Exp{
	private Exp exp;
	private NodeId id;

	
	public New(NodeId id, Exp exp) {
		this.id=id;
		this.exp = exp;
	}
	

	public Exp getExp() {
		return exp;
	}


	public NodeId  getId() {
		return id;
	}


	public void setID(NodeId id) {
		this.id = id;
	}
	
	@Override
	public String toString() 
	{
		if(exp!=null) 
		{
			return "new "+ id.getName()+"("+exp.toString()+")";
		}else 
		{
			return "new "+ id.getName()+"()";
		}
		
	}
}
