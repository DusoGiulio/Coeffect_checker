package ASTnodes.Exp;

import ASTnodes.Class.NodeAST;

public abstract class Exp extends NodeAST{
	
	public Exp(int line) {
		super();
		this.line = line;
	}

	private int line ;

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}
	
}
