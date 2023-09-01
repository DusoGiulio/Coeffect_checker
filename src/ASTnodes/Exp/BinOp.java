package ASTnodes.Exp;

import org.antlr.v4.runtime.tree.TerminalNode;

public class BinOp extends Exp {
	
	private TerminalNode op;
	private Exp lexp;
	private Exp rexp;
	
	public BinOp(TerminalNode op, Exp lexp, Exp rexp,int line ) {
		super(line);
		this.op = op;
		this.lexp = lexp;
		this.rexp = rexp;
	}

	public TerminalNode getOp() {
		return op;
	}

	public Exp getLexp() {
		return lexp;
	}

	public Exp getRexp() {
		return rexp;
	}
	
	@Override 
	public String toString() {
		return this.lexp.toString()+op.getText()+this.rexp.toString();
	}
}
