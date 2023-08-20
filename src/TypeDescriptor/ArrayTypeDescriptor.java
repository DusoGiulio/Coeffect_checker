package TypeDescriptor;

public class ArrayTypeDescriptor extends TypeDescriptor {
	private TypeDescriptor element;
	public ArrayTypeDescriptor( TypeDescriptor element) {
		this.setElement(element);
	}
	public TypeDescriptor getElement() {
		return element;
	}
	public void setElement(TypeDescriptor element) {
		this.element = element;
	}

}
