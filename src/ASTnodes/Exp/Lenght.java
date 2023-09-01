package ASTnodes.Exp;

public class Lenght extends Exp{
	private Exp exp;

	public Lenght(Exp exp,int line) {
		super(line);
		this.exp=exp;
	}

	public Exp getExp() {
		return exp;
	}
	
	@Override
	public String toString() 
	{
		return exp.toString()+".lenght";
	}
}
