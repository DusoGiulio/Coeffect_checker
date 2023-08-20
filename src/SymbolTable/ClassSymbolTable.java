package SymbolTable;

import java.util.Hashtable;
import Attributes.ClassAttribute;

//classe statica
public class ClassSymbolTable {
	private Hashtable<String, ClassAttribute> SymbolTable;
	
	public ClassSymbolTable() {
		this.SymbolTable = new Hashtable<String, ClassAttribute>();
	}
	public void enter(String name,ClassAttribute attr) 
	{
		this.SymbolTable.put(name, attr);
	}

	
	public  ClassAttribute lookup(String name) 
	{

			return this.SymbolTable.get(name);
	}
	public Hashtable<String,ClassAttribute> ST()
	{
		return this.SymbolTable;
	}


}
