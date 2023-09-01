package ASTnodes.Exp;

public class This extends Exp {
	
	public This(int line) {
		super(line);
	}
	
	@Override
	public String toString() {
		return "this";
	}
}
