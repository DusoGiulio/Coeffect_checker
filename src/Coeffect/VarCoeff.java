package Coeffect;

import ASTnodes.Exp.Exp;

public class VarCoeff {
	
	private String classCoeff;
	private Exp expCoeff;

	public VarCoeff( Exp exp, String classCoeff) {

		this.expCoeff=exp;
		this.classCoeff=classCoeff;
	}

	public String getClassCoeff() {
		return classCoeff;
	}

	public Exp getExpCoeff() {
		return expCoeff;
	}
	
	@Override
	public String toString() 
	{
		return " ";
	}
}
