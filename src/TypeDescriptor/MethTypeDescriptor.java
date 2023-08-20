package TypeDescriptor;



public class MethTypeDescriptor extends TypeDescriptor {
	private boolean isStatic;

	public MethTypeDescriptor( boolean isStatic) {
		super();
		this.isStatic= isStatic;
	}

	public boolean isStatic() {
		return this.isStatic;
	}

}
