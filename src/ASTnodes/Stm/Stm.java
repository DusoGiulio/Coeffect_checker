package ASTnodes.Stm;

import ASTnodes.Class.NodeAST;

public abstract class Stm extends NodeAST{
	public Stm(int line) {
		super();
		this.line = line;
	}

	private int line;

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}
}
