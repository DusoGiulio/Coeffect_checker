package ASTnodes.Exp;

public class Lenght extends Exp{
	private Exp exp;

	public Lenght(Exp exp) {
		this.exp=exp;
	}

	public Exp getExp() {
		return exp;
	}
}
