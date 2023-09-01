package ASTnodes.Decl;

import ASTnodes.Class.NodeAST;
import TypeDescriptor.TypeDescriptor;

public abstract  class Decl extends NodeAST {

	private TypeDescriptor type;

	private int line ;
	
	public Decl(TypeDescriptor type, int line) {
		this.setType(type);
		this.line = line;
	}

	public TypeDescriptor getType() {
		return type;
	}

	public void setType(TypeDescriptor type) {
		this.type = type;
	}
	
	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}
	
}
