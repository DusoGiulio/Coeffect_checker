package ResultGenerator;

import java.util.ArrayList;

import ASTnodes.Class.NodeAST;
import ASTnodes.Class.NodeId;
import ASTnodes.Decl.ClassDecl;
import Attributes.Attribute;
import Attributes.ClassAttribute;
import Coeffect.Coef;
import Coeffect.Coeffect;
import Coeffect.CoeffectTable.Element;
import Coeffect.VarCoeff;
import SymbolTable.ClassSymbolTable;
import TypeDescriptor.ClassTypeDescriptor;
import TypeDescriptor.CoeffectTypeDescriptor;
import TypeDescriptor.MethTypeDescriptor;

public class SurceCodeComposer {
	
	private ClassSymbolTable classST;
	private String codiceSorgente=" ";
	private String destinazioneFile;
	private ArrayList<NodeAST> AST;
	
	public SurceCodeComposer(ArrayList<NodeAST> AST, ClassSymbolTable classST, String indirizzoFile ) {
		this.setDestinazioneFile(destinazioneFile);
		this.setClassST(classST);
        this.codiceSorgente=this.codiceSorgente.concat("package ResultGenerator;");
        this.AST=AST;
        for(NodeAST node: this.AST) 
        {
        	if(node instanceof ClassDecl) 
        	{
        		ClassDecl c= (ClassDecl) node;


				if(((ClassTypeDescriptor)c.getType()).isCoeff()== Coef.COEFF  || ((ClassTypeDescriptor)c.getType()).isCoeff()== Coef.AUXCOEF) 
				{
					this.codiceSorgente=this.codiceSorgente.concat(c.toString());
				}
        	}
        }
		this.codiceSorgente=this.codiceSorgente.concat("public class CoeffectResult {\n" +"\tpublic static void main(String[] args) {\n");
		this.visitClassST();
		
	}
	
	private void visitClassST() {
		for(String id : this.classST.ST().keySet()) 
		{	//aggiungo a codice sorgente una sistem.out.println del nome della classe
			if(this.classST.lookup(id).getIsCoeff()== Coef.NOTCOEF) 
			{
				this.codiceSorgente=this.codiceSorgente.concat(this.syspl("------------------------", "------------------------"));
				this.codiceSorgente=this.codiceSorgente.concat(this.syspl(id, "Classe | "));
				this.codiceSorgente=this.codiceSorgente.concat(this.syspl("------------------------", "------------------------"));
				this.visitSTOfClass(this.classST.lookup(id));
			}
			
		}
		this.codiceSorgente=this.codiceSorgente.concat("\t}\n}\n");
		//System.out.println(this.codiceSorgente);
		
	}

	private void visitSTOfClass(ClassAttribute cAttr) {
		for(NodeId id: cAttr.getST().getSymbolTable().keySet()) 
		{
			if(cAttr.getST().lookup(id).getType() instanceof MethTypeDescriptor) 
			{
				//aggiungo a codice sorgente una sistem.out.println del nome del metodo
				this.codiceSorgente=this.codiceSorgente.concat(this.syspl(id.getName(),"\tMetodo | "));
				this.codiceSorgente=this.codiceSorgente.concat(this.syspl("----------------", "\t------------------------"));
				this.codiceSorgente=this.codiceSorgente.concat(this.syspl("","\tVariabili | "));
				this.codiceSorgente=this.codiceSorgente.concat(this.syspl("----------------", "\t------------------------"));
				this.visitMethCoeffTable(cAttr.getST().lookup(id));
				this.codiceSorgente=this.codiceSorgente.concat(this.syspl("----------------", "\t------------------------"));
			}

		}	
	}

	private void visitMethCoeffTable(Attribute attr) {
		for(Element el: attr.getCoef().getCoeffectTable() )
		{
			VarCoeff coeffect=this.findCoef(attr,el.id);
			Coeffect cf =new Coeffect(coeffect.getExpCoeff().toString(),coeffect.getClassCoeff().toString());
			if(el.coef.getCoefClass().toString().contains("Triv")) 
			{
				this.codiceSorgente=this.codiceSorgente.concat(this.sys("\"\t"+el.id+" |\""+"+"+"true"+"",""));
				this.codiceSorgente=this.codiceSorgente.concat(this.syspl("----------------", "\t------------------------"));
			}else if(cf.getClass().toString().contains("Triv")) 
			{
				this.codiceSorgente=this.codiceSorgente.concat(this.sys("\"\t"+el.id+" |\""+"+"+"false"+"",""));
				this.codiceSorgente=this.codiceSorgente.concat(this.syspl("----------------", "\t------------------------"));
			}else 
			{
				this.codiceSorgente=this.codiceSorgente.concat(this.sys("\"\t"+el.id+" |\""+"+"+el.coef.op(cf, "leq"), " "));
				this.codiceSorgente=this.codiceSorgente.concat(this.sys("\""+"\tcoeffetto variabile: \""+"+"+"\""+cf.getCoefExpr()+"\"", ""));
				this.codiceSorgente=this.codiceSorgente.concat(this.sys("\""+"\tcoeffetto inferito: \""+"+"+"("+el.coef+")"+".getClass().getSimpleName()", ""));
				this.codiceSorgente=this.codiceSorgente.concat(this.syspl("----------------", "\t------------------------"));
			}

		}
	}

	private VarCoeff findCoef(Attribute attr,String id) {
		for (NodeId nId : attr.getFormals().getSymbolTable().keySet()) {
			if (nId.getName().equals(id)) {
				return ((CoeffectTypeDescriptor)attr.getFormals().lookup(nId).getType()).getVarCoeff();
			}
		}
		for (NodeId nId : attr.getLocal().getSymbolTable().keySet()) {
			if (nId.getName().equals(id)) {
				return ((CoeffectTypeDescriptor)attr.getLocal().lookup(nId).getType()).getVarCoeff();
			}
		}
		return null;
	}

	private String syspl(String id, String tipo) {
		return "\t\tSystem.out.println(\""+tipo+id+"\");\n";
	}
	private String sys(String id, String tipo) {
		return "\t\tSystem.out.println("+tipo+id+");\n";
	}

	public String getDestinazioneFile() {
		return destinazioneFile;
	}

	public void setDestinazioneFile(String destinazioneFile) {
		this.destinazioneFile = destinazioneFile;
	}

	public ClassSymbolTable getClassST() {
		return classST;
	}
	public void setClassST(ClassSymbolTable classST) {
		this.classST = classST;
	}


	public String getCodiceSorgente() {
		return codiceSorgente;
	}

	public void setCodiceSorgente(String codiceSorgente) {
		this.codiceSorgente = codiceSorgente;
	}

}
