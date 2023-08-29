package ASTnodes.Exp;


public class ArrElem extends Exp{

	private Exp expEl;
	private Exp ArrExp;

	public ArrElem( Exp exp, Exp expEl) {
		this.expEl=expEl;
		this.setExp(exp);
	}

	public Exp getExpEl() {
		return expEl;
	}

	public Exp getExp() {
		return ArrExp;
	}

	public void setExp(Exp arrExp) {
		ArrExp = arrExp;
	}
	
	@Override
	public String toString() 
	{
		return this.ArrExp.toString()+"["+this.expEl+"]";
	}
}
