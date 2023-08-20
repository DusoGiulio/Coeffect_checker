package ASTnodes.Decl;

import ASTnodes.Class.NodeId;
import ASTnodes.Exp.Exp;
import TypeDescriptor.TypeDescriptor;

public class MainClass extends Decl{
	private NodeId idClass;
	private Exp exp;
	
	public MainClass(TypeDescriptor type, NodeId name, Exp exp) {
		super(type);
		this.idClass=name;
		this.exp=exp;
	}

	public Exp getExp() {
		return exp;
	}
	public NodeId getIdClass() {
		return idClass;
	}
}
