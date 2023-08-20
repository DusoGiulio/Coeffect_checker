package ResultGenerator;

import ASTnodes.Class.NodeId;
import Attributes.Attribute;
import Attributes.ClassAttribute;
import Coeffect.Coef;
import Coeffect.CoeffectTable.Element;
import SymbolTable.ClassSymbolTable;
import TypeDescriptor.CoeffectTypeDescriptor;
import TypeDescriptor.MethTypeDescriptor;

public class SurceCodeComposer {
	
	private String indirizzoFile;
	private ClassSymbolTable classST;
	private String codiceSorgente=" ";
	private String destinazioneFile;
	
	public SurceCodeComposer(ClassSymbolTable classST, String indirizzoFile ) {
		this.setIndirizzoFile(indirizzoFile);
		this.setDestinazioneFile(destinazioneFile);
		this.setClassST(classST);
		String sourceFilePath = indirizzoFile;
        this.codiceSorgente=this.codiceSorgente.concat("package TestText;");
        /*try {
            File file = new File(this.getIndirizzoFile());
            Scanner scanner = new Scanner(file);

            StringBuilder content = new StringBuilder();

            while (scanner.hasNextLine()) {
                content.append(scanner.nextLine());
            }

            scanner.close();

            this.codiceSorgente= this.codiceSorgente.concat(content.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
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
				this.codiceSorgente=this.codiceSorgente.concat(this.syspl(id.getName(),"\tVariabili | "));
				this.visitMethCoeffTable(cAttr.getST().lookup(id));
				this.codiceSorgente=this.codiceSorgente.concat(this.syspl("----------------", "\t------------------------"));
			}

		}	
	}

	private void visitMethCoeffTable(Attribute attr) {
		for(Element el: attr.getCoef().getCoeffectTable() )
		{
			String coeffect=this.findCoef(attr,el.id);
			this.codiceSorgente=this.codiceSorgente.concat(this.sys(el.coef.getCoefExpr()+")"+"\"","\""+"\t"+el.id+" | "+"\"+"+"\""+ coeffect +".leq("));
		}
	}

	private String findCoef(Attribute attr,String id) {
		for (NodeId nId : attr.getFormals().getSymbolTable().keySet()) {
			if (nId.getName().equals(id)) {
				return "("+((CoeffectTypeDescriptor)attr.getFormals().lookup(nId).getType()).getVarCoeff().getExpCoeff().toString()+")";
			}
		}
		for (NodeId nId : attr.getLocal().getSymbolTable().keySet()) {
			if (nId.getName().equals(id)) {
				return "("+((CoeffectTypeDescriptor)attr.getLocal().lookup(nId).getType()).getVarCoeff().getExpCoeff().toString()+")";
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

	public String getIndirizzoFile() {
		return indirizzoFile;
	}

	public void setIndirizzoFile(String file) {
		this.indirizzoFile = file;
	}

	public String getCodiceSorgente() {
		return codiceSorgente;
	}

	public void setCodiceSorgente(String codiceSorgente) {
		this.codiceSorgente = codiceSorgente;
	}

}
