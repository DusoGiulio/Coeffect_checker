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

	@Override
	public String toString() 
	{
		if(this.isStatic) 
		{
			return "static";
		}else 
		{
			return "";
		}
	}
}
