package Coeffect;

import java.util.ArrayList;

public class CoeffectTable {
	private ArrayList<Element> coeffectTable;
	
	public CoeffectTable() 
	{
		this.coeffectTable=new ArrayList<Element>();
	}

	public ArrayList<Element> getCoeffectTable() {
		return this.coeffectTable;
	}

	public void setCoeffectTable(ArrayList<Element>coeffectTable) {
		this.coeffectTable = coeffectTable;
	}
	public void addElement(String id, Coeffect coef) 
	{
		this.coeffectTable.add(new Element(id, coef));
	}
	public	Coeffect findElement(String id) 
	{
		for(Element el: this.coeffectTable) 
		{
			if(el.id.equals(id)) 
			{
				return el.coef;
			}
		}
		return null;
	}
	
	private CoeffectTable action(CoeffectTable cft, String op) 
	{
		CoeffectTable retTab= new CoeffectTable();

		for(Element el: this.coeffectTable) 
		{
			for(Element elem: cft.getCoeffectTable()) 
			{
				if(elem.id.equals(el.id)) {
					elem.coef=this.findElement(el.id).op(cft.findElement(elem.id),op);
					if(retTab.findElement(elem.id)!=null) 
					{
						retTab.findElement(elem.id).setCoefExpr(elem.coef.getCoefExpr());
						retTab.findElement(elem.id).setCoefClass(elem.coef.getCoefClass());
					}else
					{
						retTab.getCoeffectTable().add(elem);
					}
				}else 
				{
					if(retTab.findElement(el.id)==null) 
					{
						retTab.getCoeffectTable().add(el);
					}
					if(retTab.findElement(elem.id)==null) 
					{
						retTab.getCoeffectTable().add(elem);
					}
				}
				
			}
		}
		if(this.coeffectTable.size()==0 && cft.coeffectTable.size()!=0 ) 
		{
			retTab.coeffectTable.addAll(cft.coeffectTable);
		}
		if(cft.coeffectTable.size()==0 && this.coeffectTable.size()!=0) 
		{
			retTab.coeffectTable.addAll(this.coeffectTable);
		}
		return retTab;
	}

	public CoeffectTable sum(CoeffectTable cft) 
	{
		return this.action(cft, "sum");
	}
	public CoeffectTable sup(CoeffectTable cft) 
	{
		return this.action(cft, "sup");
	}
	public CoeffectTable mult(Coeffect cf) 
	{
		CoeffectTable retTab= new CoeffectTable();
		for(Element el: this.coeffectTable) 
		{
			Coeffect coef=cf.op(el.coef,"mult");
			el.coef.setCoefExpr(coef.getCoefExpr());
			el.coef.setCoefClass(coef.getCoefClass());
			retTab.getCoeffectTable().add(el);
		}
		return retTab;
	}
	public CoeffectTable leq(Coeffect cf) 
	{
		CoeffectTable retTab= new CoeffectTable();
		for(Element el: this.coeffectTable) 
		{
			Coeffect coef=cf.op(el.coef,"leq");
			el.coef.setCoefExpr(coef.getCoefExpr());
			el.coef.setCoefClass(coef.getCoefClass());
			retTab.getCoeffectTable().add(el);
		}
		return retTab;
	}
	
	@Override
	public String toString() 
	{
		return this.getCoeffectTable().toString();
	}
	
	
	public static class Element 
	{
		public String id;
		public Coeffect coef;
		
		public Element(String id, Coeffect coef) {
			this.id=id;
			this.coef=coef;
		}	
		@Override
		public String toString() 
		{
			return "["+this.id+":"+coef.toString()+"]";
		}
	}

}
