package ASTnodes.Exp;

import org.antlr.v4.runtime.tree.TerminalNode;

public class UnaryOp extends Exp{
	
	private TerminalNode op;
	private Exp exp;
	
	public UnaryOp( TerminalNode op, Exp exp) {
		this.op=op;
		this.exp=exp;
	}

	public TerminalNode getOp() {
		return op;
	}

	public Exp getExp() {
		return exp;
	}

}
