package TypeDescriptor;

import Coeffect.Coef;

public class ClassTypeDescriptor  extends TypeDescriptor{

	private String name;
	private Coef isCoeff;

	public ClassTypeDescriptor( String nameclass) {

		this.name=nameclass;
		this.isCoeff=Coef.NOTCOEF;
	}



	public String getName() {
		return name;
	}

	public Coef isCoeff() {
		return isCoeff;
	}

	public void setCoeff(Coef boolean1) {
		this.isCoeff = boolean1;
	}
	
	@Override
	public String toString() 
	{
		return "";
	}
}
