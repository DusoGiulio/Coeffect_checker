package ASTnodes.Stm;

import java.util.ArrayList;

public class Multi extends Stm{

	private ArrayList<Stm> stms;

	public Multi( ArrayList<Stm> stms) {
		this.stms = stms;
	}

	public ArrayList<Stm> getStms() {
		return stms;
	}

}
