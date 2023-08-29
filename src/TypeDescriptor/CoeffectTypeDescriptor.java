package TypeDescriptor;

import Coeffect.VarCoeff;

public class CoeffectTypeDescriptor extends TypeDescriptor {

	private TypeDescriptor VarType;
	private VarCoeff varCoeff;
	
	
	public CoeffectTypeDescriptor(TypeDescriptor varType, VarCoeff varCoeff) {
		super();
		this.VarType = varType;
		this.varCoeff = varCoeff;
	}
	public VarCoeff getVarCoeff() {
		return varCoeff;
	}

	public TypeDescriptor getVarType() {
		return VarType;
	}
	@Override
	public String toString() 
	{
		return this.VarType.toString();
	}

}
