package ASTnodes.Decl;

import java.util.ArrayList;
import ASTnodes.Class.NodeId;
import TypeDescriptor.TypeDescriptor;

public class ClassDecl  extends Decl{
	private ArrayList<FieldDecl> vars;
	private ArrayList<MethDecl> mets;
	private NodeId IdClass;
	private NodeId Idextends;
	
	public ClassDecl(TypeDescriptor type, NodeId nome, NodeId parent,ArrayList<FieldDecl> vars,ArrayList<MethDecl> met, int line) {
		super(type,line);
		this.vars = vars;
		this.mets = met;
		this.IdClass=nome;
		this.Idextends=parent;
	}
	
	public ArrayList<FieldDecl> getVars() {
		return vars;
	}
	public ArrayList<MethDecl> getMets() {
		return mets;
	}
	public NodeId getIdClass() {
		return IdClass;
	}
	public NodeId getIdextends() {
		return Idextends;
	}
	
	@Override
	public String toString() 
	{
		String acc= new String();
		if(this.Idextends!=null) 
		{
			acc=acc.concat("\n class "+this.IdClass.toString()+" extends "+this.Idextends.toString()+" { \n");
		}else 
		{
			acc=acc.concat("\n class "+this.IdClass.toString()+" {\n ");
		}
		
		for(FieldDecl f:this.vars) {
			acc=acc.concat("\n"+f.toString()+";\n");
		}
		for(MethDecl m:this.mets) {
			acc=acc.concat("\n"+m.toString()+"\n");
		}
		acc=acc.concat("\n}");
		return acc;
	}

}
