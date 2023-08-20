package Visitor;

import java.util.ArrayList;

import ASTnodes.Class.Body;
import ASTnodes.Class.NodeAST;
import ASTnodes.Class.NodeId;
import ASTnodes.Decl.ClassDecl;
import ASTnodes.Decl.FieldDecl;
import ASTnodes.Decl.MainClass;
import ASTnodes.Decl.MethDecl;
import ASTnodes.Decl.VarDecl;
import ASTnodes.Exp.ArrElem;
import ASTnodes.Exp.BinOp;
import ASTnodes.Exp.Boolean;
import ASTnodes.Exp.CallFuncObj;
import ASTnodes.Exp.Cast;
import ASTnodes.Exp.Decimal;
import ASTnodes.Exp.Exp;
import ASTnodes.Exp.Id;
import ASTnodes.Exp.Instanceof;
import ASTnodes.Exp.Lenght;
import ASTnodes.Exp.New;
import ASTnodes.Exp.This;
import ASTnodes.Exp.UnaryOp;
import ASTnodes.Stm.ARRAYASSIGN;
import ASTnodes.Stm.ASSIGN;
import ASTnodes.Stm.IF;
import ASTnodes.Stm.Multi;
import ASTnodes.Stm.SOP;
import ASTnodes.Stm.Stm;
import ASTnodes.Stm.WHILE;
import Attributes.Attribute;
import Coeffect.Coef;
import Coeffect.Coeffect;
import Coeffect.CoeffectTable;
import Coeffect.VarCoeff;
import Exceptioin.TypeCheckingException;
import SymbolTable.ClassSymbolTable;
import TypeDescriptor.ClassTypeDescriptor;
import TypeDescriptor.CoeffectTypeDescriptor;
import TypeDescriptor.ErrorTypeDescriptor;
import TypeDescriptor.MethTypeDescriptor;
import TypeDescriptor.TypeDescriptor;

public class CoeffInference {

	private ArrayList<NodeAST> ast;
	private ClassSymbolTable classST;
	private String ActualClass;   
	private Attribute actualMeth;  

	public CoeffInference(ArrayList<NodeAST> ast, ClassSymbolTable classST) throws TypeCheckingException {
		this.ast = ast;
		this.classST = classST;
	}

	public ClassSymbolTable visitProgram(ArrayList<NodeAST> ast) throws TypeCheckingException {
		for (NodeAST klass : ast) {
			if (klass instanceof ClassDecl) {
				ClassDecl c = (ClassDecl) klass;
				if (((ClassTypeDescriptor) c.getType()).isCoeff() == Coef.NOTCOEF) {
					this.visitClass(c);
				}
			}
			if(klass instanceof MainClass) 
			{
				//System.out.println("\n\tANALISI MAIN\t\n");
				MainClass c= (MainClass) klass;
				this.visitMainClass(c);
			}
		}
		return this.getClassST();
	}
    
	private void visitMainClass(MainClass c) {
		this.classST.lookup(c.getIdClass().getName()).getST().lookup(c.getIdClass()).setCoef(this.visitExp(c.getExp()));
	}

	private void visitClass(ClassDecl c) throws TypeCheckingException {
		this.ActualClass = c.getIdClass().getName();
		for (MethDecl met : c.getMets()) {
			//System.out.println(met.getId().getName());
			this.actualMeth = this.classST.lookup(c.getIdClass().getName()).getST().lookup(met.getId());
			//Inserisco all'interno degli attributi del metodo la coeffect table corrispondente
			this.classST.lookup(c.getIdClass().getName()).getST().lookup(met.getId()).setCoef(this.visitBody(met.getBody()));
		}

	}

	private CoeffectTable visitBody(Body body) {
		// setto alla coeffectTable della classe corrente e del metodo corrente l
		// coeffectTable di ritorno dalla visita del return
		CoeffectTable CT = new CoeffectTable();
		CT.setCoeffectTable(this.visitExp(body.getRetExp()).getCoeffectTable());
		// visito ogni statement dall'ultimo al primo
		for (int i = body.getStms().size()-1; i>=0 ; i--) {

				CT=this.visitStm( body.getStms().get(i), CT);
		}
		//System.out.println(CT.toString());
		return CT;
	}

	private CoeffectTable visitStm(Stm stm, CoeffectTable CT) {

		if (stm instanceof ARRAYASSIGN) {
			String id = ((ARRAYASSIGN) stm).getId();
			CoeffectTable CT2 = this.visitExp(((ARRAYASSIGN) stm).getLExp());
			CoeffectTable CT3 = this.visitExp(((ARRAYASSIGN) stm).getRexp());
			CoeffectTable CT1 = CT2.sum(CT3);
				if (this.findAttr(id).getType() instanceof ErrorTypeDescriptor) {
				return (CT1.mult(new Coeffect("new Triv()", "Triv")));
			}
			if (CT.findElement(id) != null) {
				return (CT1.mult(CT.findElement(id))).sum(CT);
			} else {
				return CT1.sum(CT);
			}

		} else if (stm instanceof ASSIGN) {
			String id = ((ASSIGN) stm).getId();

			CoeffectTable CT2 = this.visitExp(((ASSIGN) stm).getLExp());
			if (this.findAttr(id).getType() instanceof ErrorTypeDescriptor) {
				return (CT2.mult(new Coeffect("new Triv()", "Triv"))).sum(CT);
			}
			if (CT.findElement(id) != null) {
				return (CT2.mult(CT.findElement(id))).sum(CT);
			} else {
				return CT2.sum(CT);
			}

		} else if (stm instanceof IF) {
			CoeffectTable CT1 = this.visitExp(((IF) stm).getExp());
			CoeffectTable CT3 = this.visitStm(((IF) stm).getElsestm(), CT);
			CoeffectTable CT2 = this.visitStm(((IF) stm).getIfstm(), CT);
			return CT1.sum(CT2.sup(CT3));

		} else if (stm instanceof SOP) {// ritorno dirrettamente la tabella verr� poi sommata nel body
			return CT.sum(this.visitExp(((SOP) stm).getLExp()));

		} else if (stm instanceof WHILE) {

			return new CoeffectTable();

		} else if (stm instanceof Multi) {

			CoeffectTable ctAcc = CT;
			for (int i = ((Multi) stm).getStms().size() - 1; i >= 0; i--) {
				ctAcc = this.visitStm(((Multi) stm).getStms().get(i), ctAcc);
			}
			return ctAcc;
		}
		return null;
	}

	private CoeffectTable visitExp(Exp exp) {
		// BINOP
		if (exp instanceof BinOp) {
			return this.visitExp(((BinOp) exp).getLexp()).sum(this.visitExp(((BinOp) exp).getLexp()));
		}
		// UNIOP
		else if (exp instanceof UnaryOp) {
			return this.visitExp(((UnaryOp) exp).getExp());
		}
		// BOOLEAN
		else if (exp instanceof Boolean) {
			return new CoeffectTable();
		}
		// INT
		else if (exp instanceof Decimal) {
			return new CoeffectTable();
		}
		// IDENTIFIER 
		else if (exp instanceof Id) {
			CoeffectTable ct = new CoeffectTable();
			Attribute att = this.findAttr(((Id) exp).getId().getName());
			TypeDescriptor td = att.getType();
			if (td instanceof CoeffectTypeDescriptor) {
				CoeffectTypeDescriptor cf = (CoeffectTypeDescriptor) td;
				if (cf.getVarCoeff() != null) 
				{
					ct.addElement(((Id) exp).getId().getName(),new Coeffect("Nat.one()", cf.getVarCoeff().getClassCoeff()));
				} else 
				{
					ct.addElement(((Id) exp).getId().getName(), new Coeffect("Nat.one()", "Nat"));
				}

			} else {
				ct.addElement("this", new Coeffect("Nat.one()", "Nat"));
			}
			return ct;
		}
		// THIS
		else if (exp instanceof This) {
			CoeffectTable ct = new CoeffectTable();
			ct.addElement("this", new Coeffect("Nat.one()", "Nat"));
			return ct;
		}
		// ARRAY ELEM
		else if (exp instanceof ArrElem) {
			return this.visitExp(((ArrElem) exp).getExp());
		}
		// Lenght
		else if (exp instanceof Lenght) {
			return this.visitExp(((Lenght) exp).getExp());
		}
		// CAST
		else if (exp instanceof Cast) {
			return this.visitExp(((Cast) exp).getExp());
		}
		// callfunc
		else if (exp instanceof CallFuncObj) {
			CoeffectTable cfThis = new CoeffectTable();
			CoeffectTable cfAcc = new CoeffectTable();
			int i=0;
			// coeffecttable dell'espressione sinistra
			cfThis = this.visitExp(((CallFuncObj) exp).getLexp());
			//Prendo i formals del metodo
			ArrayList<VarDecl> Formals= new ArrayList<VarDecl>();
			for (NodeAST klass : ast) {
				if(klass instanceof ClassDecl) {
					if(((ClassDecl)klass).getIdClass().getName().equals(((CoeffectTypeDescriptor) this.getTypeDesc((cfThis.getCoeffectTable().get(0).id))).getVarType().toString())) {
						for(MethDecl m: ((ClassDecl)klass).getMets()) {
							if(m.getId().getName().equals(((CallFuncObj) exp).getId().getName())) {
								Formals = m.getFormals();
							}
						}
					}
				}
			}
			if(Formals.get(i).getId().getName().equals("this")) 
			{
				ArrayList<String> str = this.visitCoeffect(((CoeffectTypeDescriptor) Formals.get(i).getId().getType()).getVarCoeff());
				cfAcc=cfThis.mult( new Coeffect(str.get(0), str.get(1)));
				i++;
			}
			for(int j=i;j<((CallFuncObj) exp).getRexp().size();j++) 
			{
				CoeffectTable cf = this.visitExp(((CallFuncObj) exp).getRexp().get(j));
				ArrayList<String> str = this.visitCoeffect(((CoeffectTypeDescriptor) Formals.get(j).getId().getType()).getVarCoeff());
				cfThis=cf.mult(new Coeffect(str.get(0), str.get(1)));
				cfAcc=cfAcc.sum(cfThis);
			}
			return cfAcc;
		}
		// NEW
		else if (exp instanceof New) {
			if ((((New) exp).getExp() != null)) {
				return this.visitExp(((New) exp).getExp());
			} else {
				CoeffectTable ct = new CoeffectTable();
				return ct;
			}

		}
		// INSTANCEOF
		else if (exp instanceof Instanceof) {
			return this.visitExp(((Instanceof) exp).getExp());
		}
		return null;
	}

	private Attribute findAttr(String id) {
		for (NodeId nId : this.actualMeth.getFormals().getSymbolTable().keySet()) {
			if (nId.getName().equals(id)) {
				return this.actualMeth.getFormals().lookup(nId);
			}
		}
		for (NodeId nId : this.actualMeth.getLocal().getSymbolTable().keySet()) {
			if (nId.getName().equals(id)) {
				return this.actualMeth.getLocal().lookup(nId);
			}
		}
		return new Attribute(new ErrorTypeDescriptor());
	}

	private ArrayList<String> visitCoeffect(VarCoeff var) {
		ArrayList<String> str = new ArrayList<String>();
		str.add(this.stringExp(var.getExpCoeff()));
		str.add(var.getClassCoeff());
		return str;
	}

	private String stringExp(Exp exp) {

		// IDENTIFIER ritorno il tipo dell'ID fra parentesi
		if (exp instanceof Id) {
			return (((Id) exp).getId().getName());
		}
		// callfunc
		else if (exp instanceof CallFuncObj) {
			String left = this.stringExp(((CallFuncObj) exp).getLexp());
			return left + "." + ((CallFuncObj) exp).getId().getName() + "()";
		} else if (exp instanceof New) {
			if (((New) exp).getExp() == null) {
				return "new " + ((New) exp).getId().getName() + "()";
			}
		}
		return "errore";

	}

	public ClassSymbolTable getClassST() {
		return classST;
	}

	private TypeDescriptor getTypeDesc(String id) {
		// se id è presente nella tabella delle classi allora ritorno il tipo della
		// classe
		for (NodeAST klass : ast) {
			if (klass instanceof ClassDecl) {
				ClassDecl c = (ClassDecl) klass;
				if (this.ActualClass.equals(c.getIdClass().getName())) {
					for (FieldDecl var : c.getVars()) {
						if (var.getId().getName().equals(id)) {
							return var.getType();
						}
					}
				}
			}
		}
		// controllo se la variabileid nella tabella del body
		for (NodeId var : this.actualMeth.getFormals().getSymbolTable().keySet()) {
			if (var.getType().getClass() != MethTypeDescriptor.class) {
				if (var.getName().equals(id)) {
					return var.getType();
				}
			}
		}
		// controllo seid presente nella tabella della classe
		for (NodeId var : this.actualMeth.getLocal().getSymbolTable().keySet()) {
			if (var.getType().getClass() != MethTypeDescriptor.class) {
				if (var.getName().equals(id)) {
					return var.getType();
				}
			}
		}

		return new ErrorTypeDescriptor();
	}
}