package ASTnodes.Decl;

import java.util.ArrayList;
import ASTnodes.Class.NodeId;
import TypeDescriptor.TypeDescriptor;

public class ClassDecl  extends Decl{
	private ArrayList<FieldDecl> vars;
	private ArrayList<MethDecl> mets;
	private NodeId IdClass;
	private NodeId Idextends;
	
	public ClassDecl(TypeDescriptor type, NodeId nome, NodeId parent,ArrayList<FieldDecl> vars,ArrayList<MethDecl> met) {
		super(type);
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

}
