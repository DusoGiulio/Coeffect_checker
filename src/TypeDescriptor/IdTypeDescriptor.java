package TypeDescriptor;

public class IdTypeDescriptor  extends TypeDescriptor{

	public IdTypeDescriptor(String type) {
		super();
		this.nomeTipo=type;
	}

	private String nomeTipo;

	public String getNomeTipo() {
		return nomeTipo;
	}

	public void setNomeTipo(String nomeTipo) {
		this.nomeTipo = nomeTipo;
	}
	@Override
	public String toString() 
	{
		return this.nomeTipo;
	}
}
