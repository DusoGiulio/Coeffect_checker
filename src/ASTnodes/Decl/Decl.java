package ASTnodes.Decl;

import ASTnodes.Class.NodeAST;
import TypeDescriptor.TypeDescriptor;

public abstract  class Decl extends NodeAST {

	private TypeDescriptor type;
	
	public Decl(TypeDescriptor type) {
		this.setType(type);
	}

	public TypeDescriptor getType() {
		return type;
	}

	public void setType(TypeDescriptor type) {
		this.type = type;
	}
	
}
