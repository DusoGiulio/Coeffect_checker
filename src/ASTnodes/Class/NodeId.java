package ASTnodes.Class;

import TypeDescriptor.TypeDescriptor;

public class NodeId extends NodeAST {

	private String name;	
	private TypeDescriptor type;


	
	public NodeId(TypeDescriptor type,String name) {
		this.setName(name);
		this.setType(type);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public TypeDescriptor getType() {
		return type;
	}

	public void setType(TypeDescriptor type) {
		this.type = type;
	}
	
	@Override
	public String toString() 
	{
		return this.name;
	}

}
