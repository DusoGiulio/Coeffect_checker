package ASTnodes.Decl;

import ASTnodes.Class.NodeId;
import TypeDescriptor.TypeDescriptor;

public class VarDecl extends Decl{

	public VarDecl(TypeDescriptor type, NodeId id) {
		super(type);//Definisce il tipo di variabile asociato all'ID
		this.id = id;//Contiene la stringa associata al nodo e il suo descrittore di tipo
	}
	
	private NodeId id;
	
	public NodeId getId() {
		return id;
	}
}
