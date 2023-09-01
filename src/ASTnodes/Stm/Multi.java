package ASTnodes.Stm;

import java.util.ArrayList;

public class Multi extends Stm{

	private ArrayList<Stm> stms;

	public Multi( ArrayList<Stm> stms,int line) {
		super(line);
		this.stms = stms;
	}

	public ArrayList<Stm> getStms() {
		return stms;
	}
	@Override
	public String toString() {
		String acc= new String();
		for(Stm s: this.stms) 
		{
			acc=acc.concat("\n"+s.toString()+"\n");
		}
		return acc;
	}
}
