package Attributes;

import java.util.ArrayList;

import Coeffect.Coef;
import SymbolTable.SymbolTable;

public class ClassAttribute {
	
	public ClassAttribute(SymbolTable sT, String extendClass) {
		super();
		ST = sT;
		this.extendClass = extendClass;
		this.visited=false;
		this.listofextclass=null;
		this.isCoeff=Coef.NOTCOEF;
		}
	private SymbolTable ST;
	private String extendClass;
	private boolean visited ;
	private  ArrayList<String> listofextclass;
	private Coef isCoeff;

	
	public SymbolTable getST() {
		return ST;
	}

	public String getExtendClass() {
		return extendClass;
	}

	public boolean isVisited() {
		return visited;
	}
	public void setVisited(boolean visited) {
		this.visited = visited;
	}
	public ArrayList<String> getListofextclass() {
		return listofextclass;
	}
	public void setListofextclass(ArrayList<String> listofextclass) {
		this.listofextclass = listofextclass;
	}

	public Coef getIsCoeff() {
		return isCoeff;
	}

	public void setIsCoeff(Coef isCoeff) {
		this.isCoeff = isCoeff;
	}
}
