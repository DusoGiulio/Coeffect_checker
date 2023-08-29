package ASTnodes.Exp;

import java.util.ArrayList;
import ASTnodes.Class.NodeId;

public class CallFuncObj extends Exp{

	public CallFuncObj(NodeId id, Exp lexp, ArrayList<Exp> list) {
		this.setId(id);
		this.lexp = lexp;
		this.rexp = list;
	}
	private Exp lexp;
	private ArrayList<Exp> rexp;
	private NodeId id;
	
	public Exp getLexp() {
		return lexp;
	}
	public void setLexp(Exp lexp) {
		this.lexp = lexp;
	}
	public ArrayList<Exp> getRexp() {
		return rexp;
	}
	public void setRexp(ArrayList<Exp> rexp) {
		this.rexp = rexp;
	}
	public NodeId getId() {
		return id;
	}
	public void setId(NodeId id) {
		this.id = id;
	}

	@Override
	public String toString() 
	{
		return lexp.toString()+"."+id.getName()+"("+this.rexpString()+")";
	}
	private String rexpString() {
		String s = "";
		for(Exp exp: this.rexp) 
		{
			if(this.rexp.indexOf(exp)==this.rexp.size()-1) 
			{
				s=s.concat(exp.toString());
			}else {
				s=" "+s.concat(exp.toString()+",");
			}
		}			
		return s;
	}
}
