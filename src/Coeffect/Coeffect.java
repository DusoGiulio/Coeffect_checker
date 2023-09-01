package Coeffect;

public class Coeffect {

	private String coefExpr;
	private String coefClass;

	public Coeffect(String coefExpr, String coefClass) {
		this.coefExpr = coefExpr;
		this.coefClass = coefClass;
	}

	public String getCoefExpr() {
		return this.coefExpr;
	}

	public void setCoefExpr(String coefExpr) {
		this.coefExpr = coefExpr;
	}

	public String getCoefClass() {
		return this.coefClass;
	}

	public void setCoefClass(String coefClass) {
		this.coefClass = coefClass;
	}

	public Coeffect supOne() {
		if (this.coefClass.equals("Nat")) {
			return this.op(new Coeffect("Nat.one()","Nat"), "sup");
		} else {
			return this.op(new Coeffect("Nat.one()","Nat"), "sup");
		}
	}

	public Coeffect op(Coeffect coef, String op) {
		if (this.coefClass.equals(coef.coefClass)) {
			return sameClass(op, coef);
		} else if (this.coefClass.equals("Nat") && !coef.coefClass.equals("Nat")) {
			return thisNatClass(op, coef);
		} else if (!this.coefClass.equals("Nat") && coef.coefClass.equals("Nat")) {
			return otherNatClass(op, coef);
		} else {
			return noNatClass(op, coef);
		}

	}

	private Coeffect sameClass(String op, Coeffect other) {
		return new Coeffect(this.coefExpr + "." + op + "(" + other.coefExpr + ")", other.coefClass);
	}

	private Coeffect thisNatClass(String op, Coeffect other) {
		String exp = "(" + other.coefClass + ".fromNat(" + this.coefExpr + "))." + op + "(" + other.coefExpr + ")";
		
		return new Coeffect(exp, other.coefClass);
	}

	private Coeffect otherNatClass(String op, Coeffect other) {
		String exp = this.coefExpr + "." + op + "(" + this.coefClass + ".fromNat(" + other.coefExpr + "))";
		return new Coeffect(exp,this.coefClass);
	}

	private Coeffect noNatClass(String op, Coeffect other) {
		return new Coeffect("new Triv()", "Triv");
	}
	
	@Override
	public String toString() 
	{
		return "("+this.coefExpr+")";
	}

}