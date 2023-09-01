package ASTnodes.Class;

import java.util.ArrayList;

import ASTnodes.Decl.Decl;
import ASTnodes.Decl.VarDecl;
import ASTnodes.Exp.Exp;
import ASTnodes.Stm.Stm;
import TypeDescriptor.TypeDescriptor;

public class Body extends Decl {
	private ArrayList<VarDecl> decs;
	private ArrayList<Stm> stms;
	private Exp retExp;

	public Body(TypeDescriptor type, ArrayList<VarDecl> decs, ArrayList<Stm> stms, Exp retExp) {
		super(type,0);
		this.decs = decs;
		this.stms = stms;
		this.retExp = retExp;
	}
	
	public ArrayList<VarDecl> getDecs() {
		return decs;
	}
	public ArrayList<Stm> getStms() {
		return stms;
	}
	public Exp getRetExp() {
		return retExp;
	}
	
	@Override
	public String toString() {
		String acc= new String();
		for(VarDecl v: this.decs) 
		{
			acc=acc.concat(v.toString()+";\n");
		}
		for(Stm s: this.stms) 
		{
			acc=acc.concat(s.toString());
		}
		acc=acc.concat("\n return "+this.retExp.toString()+";");
		return  acc;
	}

}
