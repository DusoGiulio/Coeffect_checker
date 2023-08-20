package ASTnodes.Exp;

public abstract class Litteral extends Exp {
	private String Value;
	public Litteral(String Value) 
	{
		this.Value=Value;
	}

	public String getValue() {
		return Value;
	}

	public void setValue(String value) {
		Value = value;
	}

}
