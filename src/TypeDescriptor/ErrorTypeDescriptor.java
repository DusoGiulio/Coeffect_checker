package TypeDescriptor;

public class ErrorTypeDescriptor extends TypeDescriptor{
	
	public ErrorTypeDescriptor(int line) {
		super();
		this.line = line;
	}

	private int line;

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}
	
	

}
