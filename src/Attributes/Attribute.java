package Attributes;

import Coeffect.CoeffectTable;
import SymbolTable.SymbolTable;
import TypeDescriptor.TypeDescriptor;

public class Attribute {
	private TypeDescriptor type;
	private SymbolTable formals;
	private SymbolTable local;
	private TypeDescriptor retType;
	private CoeffectTable coef;

	
	public Attribute(TypeDescriptor type) {
		this.setType(type);
	}
	
	public TypeDescriptor getType() {
		return type;
	}
	public void setType(TypeDescriptor type) {
		this.type = type;
	}
	public SymbolTable getFormals() {
		return formals;
	}
	public void setFormals(SymbolTable formals) {
		this.formals = formals;
	}
	public SymbolTable getLocal() {
		return local;
	}
	public void setLocal(SymbolTable local) {
		this.local = local;
	}

	public TypeDescriptor getRetType() {
		return retType;
	}

	public void setRetType(TypeDescriptor retType) {
		this.retType = retType;
	}

	public CoeffectTable getCoef() {
		return coef;
	}

	public void setCoef(CoeffectTable coef) {
		this.coef = coef;
	}

}
