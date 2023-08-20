package SymbolTable;

import java.util.Hashtable;

import ASTnodes.Class.NodeId;
import Attributes.Attribute;


public class SymbolTable {
	private Hashtable<NodeId,Attribute> SymbolTable;
	
	public SymbolTable() {
		this.SymbolTable = new Hashtable<NodeId, Attribute>();
	}
	
	public void closeScope()
	{
		this.SymbolTable.clear();
	}
	public void enter(NodeId name, Attribute attr) 
	{
		this.SymbolTable.put(name, attr);
	}
	
	public  Attribute lookup(NodeId name) 
	{

			return this.SymbolTable.get(name);
	}

	public Hashtable<NodeId, Attribute> getSymbolTable() {
		return SymbolTable;
	}

	public void setSymbolTable(Hashtable<NodeId, Attribute> symbolTable) {
		SymbolTable = symbolTable;
	}


	
}
