package ASTnodes.Exp;

import org.antlr.v4.runtime.tree.TerminalNode;

public class UnaryOp extends Exp{
	
	private TerminalNode op;
	private Exp exp;
	
	public UnaryOp( TerminalNode op, Exp exp, int line) {
		super(line);
		this.op=op;
		this.exp=exp;
	}

	public TerminalNode getOp() {
		return op;
	}

	public Exp getExp() {
		return exp;
	}
	
	@Override
	public String toString() 
	{
		return this.op.getText()+exp.toString();
	}

}
