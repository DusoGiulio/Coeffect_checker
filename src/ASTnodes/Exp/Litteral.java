package ASTnodes.Exp;

public abstract class Litteral extends Exp {
	private String Value;
	public Litteral(String Value,int line) 
	{
		super(line);
		this.Value=Value;
	}

	public String getValue() {
		return Value;
	}

	public void setValue(String value) {
		Value = value;
	}

	@Override
	public String toString() {
		return this.Value;
	}
}
