package ASTnodes.Decl;

import ASTnodes.Class.NodeId;
import TypeDescriptor.TypeDescriptor;

public class FieldDecl  extends Decl{
	
	public FieldDecl(TypeDescriptor type, NodeId id) {
		super(type);//Definisce il tipo di variabile asociato all'ID
		this.id = id;//Contiene la stringa associata al nodo e il suo descrittore di tipo
	}
	
	private NodeId id;
	
	public NodeId getId() {
		return id;
	}
	
	@Override
	public String toString() 
	{
		return this.getType().toString()+" "+this.getId().toString()+"";
	}
}
