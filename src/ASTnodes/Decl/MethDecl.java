package ASTnodes.Decl;

import java.util.ArrayList;

import ASTnodes.Class.Body;
import ASTnodes.Class.NodeId;
import TypeDescriptor.TypeDescriptor;

public class MethDecl extends Decl{
	
	
	private ArrayList<VarDecl> formals;
	private  TypeDescriptor retType;
	private NodeId id;
	private Body body;
	private int lastLine;
	
	public MethDecl(TypeDescriptor type, ArrayList<VarDecl> formals, TypeDescriptor retType, NodeId name, Body body,int line,int lastline) {
		super(type,line);
		this.formals = formals;
		this.retType = retType;
		this.id = name;
		this.body = body;
		this.lastLine=lastline;
		
	}

	public ArrayList<VarDecl> getFormals() {
		return formals;
	}
	public TypeDescriptor getRetType() {
		return retType;
	}
	public NodeId getId() {
		return id;
	}
	public Body getBody() {
		return body;
	}
	
	@Override
	public String toString() {
		String acc= new String();
		acc=acc.concat("public "+ this.getType()+" "+this.retType.toString()+" "+this.id.getName()+"( ");
		for(VarDecl v: this.getFormals()) 
		{
			if(this.getFormals().indexOf(v)==this.getFormals().size()-1){
				acc=acc.concat(" "+v.toString()+" ");
			}else {
				acc=acc.concat(" "+v.toString()+",");
			}
		}
		
		acc=acc.concat("){\n"+this.body.toString()+"\n}");
		return acc;
	}

	public int getLastLine() {
		return lastLine;
	}

	public void setLastLine(int lastLine) {
		this.lastLine = lastLine;
	}
}
