package Visitor;

import java.util.ArrayList;
import ASTnodes.Class.*;
import ASTnodes.Decl.*;
import ASTnodes.Exp.*;
import ASTnodes.Exp.Boolean;
import ASTnodes.Stm.*;
import Attributes.Attribute;
import Attributes.ClassAttribute;
import Coeffect.Coef;
import Coeffect.VarCoeff;
import Exceptioin.TypeCheckingException;
import SymbolTable.*;
import TypeDescriptor.*;

public class TypeCheking {
	private ArrayList<NodeAST> ast;
	private ClassSymbolTable classST;
	private SymbolTable bTable;
	private SymbolTable cTable;
	private ClassDecl actualClass;
	private boolean arrelem;
	private boolean isStatic;

	public TypeCheking(ArrayList<NodeAST> ast, ClassSymbolTable classST)  throws TypeCheckingException {
		this.ast=ast;
		this.classST=classST;
		this.arrelem=false;
			if(this.visitProgram(this.ast)instanceof ErrorTypeDescriptor) 
			{
				throw new TypeCheckingException ();
			}

	}
//se ogni classe non ritorna errore allora ritorno non lancio eccezioni, senno lancio un eccezione di tipo TypeCheckingexception
	private TypeDescriptor visitProgram(ArrayList<NodeAST> ast) throws TypeCheckingException{
		for(NodeAST klass :  ast) 
		{
			if(klass instanceof ClassDecl) 
			{
				ClassDecl c= (ClassDecl) klass;
				//System.out.println("\n\tANALISI CLASSE "+c.getIdClass().getName()+"\t\n");
				this.setActualClass(c);
				TypeDescriptor type=this.visitClass(c);
				if(type instanceof ErrorTypeDescriptor ) 
				{
					System.err.println("Nella Classe "+c.getIdClass().getName()+ " è presente un errore");
					return  new ErrorTypeDescriptor();
				}
			}
		}
		//se tutte le clasi sono ok analizzo la classe main
		for(NodeAST klass : ast) 
		{
			if(klass instanceof MainClass) 
			{
				//System.out.println("\n\tANALISI MAIN\t\n");
				MainClass c= (MainClass) klass;
				TypeDescriptor type=this.visitMain(c);
				if(type instanceof ErrorTypeDescriptor ) 
					{
						System.err.println("Nella Classe "+c.getIdClass().getName()+ " è presente un errore");
						return  new ErrorTypeDescriptor();
					}
			}

		}	
		return  new BoolTypeDescriptor();
	}
//Se l'exp riotorna errore allora ritornerò errore se ritorna un tipo allora sarà ok
	private TypeDescriptor visitMain(MainClass c) {
		TypeDescriptor t= this.visitExp(c.getExp());
		this.getClassST().lookup(c.getIdClass().getName()).getST().enter(c.getIdClass(), new Attribute(t));
		if(t!=null) 
		{	
			this.getClassST().lookup(c.getIdClass().getName()).getST().enter(c.getIdClass(), new Attribute(t));
			return t;
		}
		return new IntTypeDescriptor();
		
	}
//se tutti i body dei metodi sono ok allora ritorno il tipo della classe senno ritono errore
	private TypeDescriptor visitClass(ClassDecl c) throws TypeCheckingException {
		this.cTable= this.getClassST().lookup(c.getIdClass().getName()).getST();
		for(MethDecl met :c.getMets()) 
		{	//System.out.println("      Metodo:"+met.getId().getName());
			//creo una nuova symboltable per il body
			this.bTable=new SymbolTable();
			this.isStatic= ((MethTypeDescriptor)met.getType()).isStatic();
			//inserisco in questa symboltable le variabili dichiarate nell'intestazione del metodo
			for(VarDecl var: met.getFormals()) 
			{
				NodeId vId=var.getId();
				TypeDescriptor vType= var.getType();
				
				if(vType instanceof CoeffectTypeDescriptor) 
				{
					if(((CoeffectTypeDescriptor)var.getType()).getVarCoeff()!=null) 
					{
						this.visitCoeffect(var);
					}
					this.classST.lookup(c.getIdClass().getName()).getST().lookup(met.getId()).getFormals().lookup(vId).setType(vType);
					vType= ((CoeffectTypeDescriptor)vType).getVarType();
				}
				if(vId.getName().equals("this") ) 
				{
					if(vType instanceof IdTypeDescriptor) 
					{
						if(!((IdTypeDescriptor)vType).getNomeTipo().equals(c.getIdClass().getName()))
						{
							System.err.println("Il tipo del campo this del metodo "+met.getId().getName()+" nella classe "+c.getIdClass().getName()+" è errato");
							throw new TypeCheckingException();
						}
						else 
						{
							this.bTable.enter(vId, new Attribute(vType));
						}
					}else 
					{
						System.err.println("Il tipo del campo this del metodo "+met.getId().getName()+" nella classe "+c.getIdClass().getName()+" è errato");
						throw new TypeCheckingException();
					}
				}
				this.bTable.enter(vId, new Attribute(vType));
				
				
			}
			//invoco la visita del body, passandogli il body e la symboltable ad esso associata
			Attribute local= this.visitBody(met.getBody(), this.classST.lookup(c.getIdClass().getName()).getST().lookup(met.getId()));
			TypeDescriptor type= local.getType();
			if(type instanceof IdTypeDescriptor) 
			{
				type= this.getTypeDesc(((IdTypeDescriptor)type).getNomeTipo());
			}
			if(type instanceof BoolTypeDescriptor) 
			{
				if(met.getRetType().getClass()!= BoolTypeDescriptor.class) 
				{
					type=new ErrorTypeDescriptor();
				}
			}else if(type instanceof IntTypeDescriptor) 
			{
				if(met.getRetType().getClass()!= IntTypeDescriptor.class) 
				{
					type=new ErrorTypeDescriptor();
				}
			}else if(type instanceof ClassTypeDescriptor) 
			{
				if(((ClassTypeDescriptor)type).getName().equals(((IdTypeDescriptor)met.getRetType()).getNomeTipo())==true)
				{
					type= met.getRetType();
					
				}else if(this.getClassST().lookup(((ClassTypeDescriptor)type).getName()).getListofextclass()!=null) 
				{
					if(this.getClassST().lookup(((ClassTypeDescriptor)type).getName()).getListofextclass().contains(((IdTypeDescriptor)met.getRetType()).getNomeTipo())==true ) 
					{
						type= met.getRetType();
					}
				}
			}
			//svuoto la symboltable temporanea
			this.bTable.closeScope();
			this.classST.lookup(c.getIdClass().getName()).getST().lookup(met.getId()).setLocal(local.getLocal());
			if(type instanceof ErrorTypeDescriptor ) 
			{				
				return new ErrorTypeDescriptor();
			}else if(type.getClass() != met.getRetType().getClass()) 
			{
				return new ErrorTypeDescriptor();
			}
			this.isStatic=false;
		}
		return c.getType();
	}
		

	//il body può ritornare eerore oppure se è tutto corretto ritornera il tipo di ritorno dell'exp
	private Attribute visitBody(Body body, Attribute formals) throws TypeCheckingException {
		//per ogni variabile presente nel body la aggiungo alla symboltable del body solo se non è già stata dichiarata
		Attribute local= new Attribute(null);
		local.setLocal(new SymbolTable());
		for(VarDecl var: body.getDecs()) 
		{//se la variabile non esiste nella Symboltable della classe e in quella del body
			if(!this.Exist(var,formals.getFormals()) && !this.Exist(var,this.cTable) && !this.Exist(var,local.getLocal())) 
			{
				if(((CoeffectTypeDescriptor)var.getType()).getVarCoeff()!=null) 
				{
					this.visitCoeffect(var);
				}
				this.bTable.enter(var.getId(), new Attribute( var.getType()));
				local.getLocal().enter(var.getId(),new Attribute( var.getType()));
			}else 
			{
				local.setType(new ErrorTypeDescriptor());
			}
		}
		
		for(Stm stm: body.getStms()) 
		{
			TypeDescriptor type= this.visitStm(stm);
			if(type instanceof ErrorTypeDescriptor ) 
			{
				local.setType(new ErrorTypeDescriptor());
			}
		}
		//System.out.println("\tRETURN\t");
		TypeDescriptor Type=this.visitExp(body.getRetExp());
		Type=this.typeCoeff(Type);
		local.setType(Type);
		return local;		
	}

//ritona il suo tipo oppure err
		private TypeDescriptor visitStm(Stm stm) {
			/*algoritmo da eseguire sia per ARRAYASSIGN che per ASSIGN; se this.visitExp(dotExp) ritorna un tipo classe allora devo controllare
			 * che l'identifcatore sia uguale ad una variabile presente nella symboltable solo se è VarDecl, se così è ritorno il tipo come già
			 * faccio senno esco da tutto e rilascio errore 
			 * */
			if(stm instanceof ARRAYASSIGN ) 
			{
				if(this.visitExp(((ARRAYASSIGN) stm).getLExp()) instanceof IntTypeDescriptor)
				{
					//System.out.println("\tARRAYASSIGN\t");
					TypeDescriptor  type= this.visitExp(((ARRAYASSIGN) stm).getRexp());
					type=this.typeCoeff(type);
					if(((ArrayTypeDescriptor)this.getTypeDesc(((ARRAYASSIGN) stm).getId())).getElement().getClass() == type.getClass() ) 
					{
						return type; 
					}
				}
			}else if(stm instanceof ASSIGN ) 
			{
				//System.out.println("\tASSIGN(=)\t");
				TypeDescriptor  type= this.visitExp(((ASSIGN) stm).getLExp());	
				TypeDescriptor idType=this.getTypeDesc(((ASSIGN) stm).getId());;
				
				idType=this.typeCoeff(idType);
				type=this.typeCoeff(type);
				if(idType.getClass() == type.getClass() ) 
				{
					return type; 
				}
				if(type instanceof ClassTypeDescriptor && idType instanceof IdTypeDescriptor) 
				{
					IdTypeDescriptor id=(IdTypeDescriptor) idType;
					if(id.getNomeTipo().equals(((ClassTypeDescriptor)type).getName())) 
					{
						return type;
					}else if(this.getClassST().lookup(((ClassTypeDescriptor)type).getName()).getListofextclass().contains(id.getNomeTipo())) 
					{
						return type;
					}
				}				
			}else if(stm instanceof IF) 
			{
				//System.out.println("\tIF-ELSE\t");
				if(this.visitExp(((IF) stm).getExp()) instanceof BoolTypeDescriptor && (this.visitStm(((IF) stm).getIfstm())).getClass()!=ErrorTypeDescriptor.class && (this.visitStm(((IF) stm).getElsestm())).getClass()!=ErrorTypeDescriptor.class)
				{
					return new BoolTypeDescriptor();
				}
			}else if(stm instanceof SOP) 
			{
				//System.out.println("\tSystemOutPrintln\t");
				if(this.visitExp(((SOP) stm).getLExp()).getClass()!=ErrorTypeDescriptor.class) 
				{
					return new BoolTypeDescriptor();
				}
			}else if(stm instanceof WHILE) 
			{
				//System.out.println("\tWHILE\t");
				if((this.visitExp(((WHILE) stm).getExp()) instanceof BoolTypeDescriptor && this.visitStm(((WHILE) stm).getStm1()).getClass()!=ErrorTypeDescriptor.class ))
				{				
					return new BoolTypeDescriptor();
				}
			}else if(stm instanceof Multi) 
			{
				//System.out.println("\tMULTI\t");
				for(Stm s: ((Multi)stm).getStms()) 
				{
					if(this.visitStm(s).getClass()== ErrorTypeDescriptor.class) 
					{
						return new ErrorTypeDescriptor();
					}
				}
				return new BoolTypeDescriptor();
			}
			return new ErrorTypeDescriptor();
		}
				
		//ritona il suo tipo oppure err
		private TypeDescriptor visitExp(Exp exp) {
			//BINOP se i tip a dx e sx sono uguali e fra di loro esiste <,>,&&,|| allora ritorn un tipo booleano, in caso contrario cioè se esiste
			// fra le due espressioni +,-,*,/,= allora ritorno un tipo intero, se i due tipi delle espressioni a dx e sinistra non sono uguali allora 
			//ritorno errore
			if(exp instanceof BinOp ) 
			{
				TypeDescriptor TypeLexp=this.visitExp(((BinOp)exp).getLexp());
				TypeDescriptor TypeRexp=this.visitExp(((BinOp)exp).getRexp());
				TypeLexp= this.typeCoeff(TypeLexp);
				TypeRexp= this.typeCoeff(TypeRexp);
				if(TypeLexp.getClass() == TypeRexp.getClass())
				{
					if((((BinOp)exp).getOp().getText()).equals("=")) 
					{					//System.out.println("BinOp--> Arr "+((BinOp)exp).getOp());
										return this.visitExp(((BinOp)exp).getLexp());
					}
					else if( TypeLexp instanceof IntTypeDescriptor && (((BinOp)exp).getOp().getText()).equals(">") || (((BinOp)exp).getOp().getText()).equals("<") ) 
					{
						//System.out.println("BinOp--> Bool "+((BinOp)exp).getOp());
						return new BoolTypeDescriptor();
					}
					else if(TypeLexp instanceof BoolTypeDescriptor &&  (((BinOp)exp).getOp().getText()).equals("&&") || (((BinOp)exp).getOp().getText()).equals("||")) 
					{
						//System.out.println("BinOp--> Bool "+((BinOp)exp).getOp());
						return new BoolTypeDescriptor();
					}
					else 
					{
						//System.out.println("BiOp--> Int "+((BinOp)exp).getOp());
						return new IntTypeDescriptor();
					}

				}
			}
			//UNIOP se il tipo di ritorno dell'espressione è booleano allora posso ritornare un tipo booleano, senò tornerò un tipo errore
			else if(exp instanceof UnaryOp) 
			{
				if(this.visitExp(((UnaryOp)exp).getExp()) instanceof BoolTypeDescriptor) 
				{
					//System.out.println("Uniop");
					return new BoolTypeDescriptor();
				}
			}
			//BOOLEAN
			else if(exp instanceof Boolean) 
			{
				//System.out.println("TypeBool");
				return new BoolTypeDescriptor();
			}
			//INT
			else if(exp instanceof Decimal) 
			{
				//System.out.println("TypeInt");
				return new IntTypeDescriptor();
			}
			//IDENTIFIER ritorno il tipo dell'ID fra parentesi 
			else if(exp instanceof Id) 
			{
				//System.out.println("TypeID:"+(((Id)exp).getId().getName()));
				if(this.getTypeDesc(((Id)exp).getId().getName()) instanceof ArrayTypeDescriptor) 
				{
					this.arrelem=true;
				}
				//ritorno il tipo dell'identificatore se non esiste ritornerò un <error Type descriptor
				return this.getTypeDesc(((Id)exp).getId().getName());
			}
			//THIS deve ritornare il tipo della classe che attulmente viene scansionanta
			else if(exp instanceof This) 
			{	//System.out.println("This");
				if(!this.isStatic) 
				{	
					return this.getActualClass().getType();
				}
			}
			//ARRAY ELEM
			else if(exp instanceof ArrElem) 
			{
			//se il tipo descritto dalla prima espressine è di tipo Array e la posizione indicata nella seconda è intero ritorno
				//il tipo contenuto all'interno dell'array
				TypeDescriptor TypeExp=this.visitExp(((ArrElem)exp).getExp());
				
				if( TypeExp instanceof ArrayTypeDescriptor && this.visitExp(((ArrElem)exp).getExpEl()) instanceof IntTypeDescriptor) 
				{	//System.out.println("ArrElem");
					return ((ArrayTypeDescriptor)TypeExp).getElement();
				}
				//Lenght
			}else if(exp instanceof Lenght) 
			{//System.out.println("Lenght");
			
				if(this.visitExp(((Lenght)exp).getExp()).getClass() != ErrorTypeDescriptor.class && this.arrelem) 
				{	
					return new IntTypeDescriptor();
				}
			//CAST
			}else if(exp instanceof Cast) 
			{
				//System.out.println("CAST  ");
			
				TypeDescriptor TypeExp=this.visitExp(((Cast)exp).getExp());
				TypeExp= typeCoeff(TypeExp);
				if(TypeExp instanceof IdTypeDescriptor)
				{
					//se  l'Id (dx)fra parentesi è una classe
					if(this.getClassST().lookup(((Cast)exp).getId().getName()) !=null)
						{
							//prendo il nome della classe dx
							String classdx=((IdTypeDescriptor)TypeExp).getNomeTipo(); 
							if(((Cast)exp).getId().getName().equals(classdx))
								{
									return this.getTypeDesc(((Cast)exp).getId().getName());
								}
							//se la classe a destra non ha una lista di superclassi	
							if( this.getClassST().lookup(((Cast)exp).getId().getName()).getListofextclass() !=null) 
								{//se il nome della classe a dx è un supertipo della classe a sx
									if((this.getClassST().lookup(((Cast)exp).getId().getName()).getListofextclass().contains(classdx))) 
										{
											return this.getTypeDesc(((Cast)exp).getId().getName());
										}
								}
							//se la classe a dx è sottotipo  della classe di sx
							if((this.getClassST().lookup(classdx).getListofextclass()).contains(((Cast)exp).getId().getName()))
							{
								return this.getTypeDesc(((Cast)exp).getId().getName());
							}
						}
				}
			}
			//callfunc
			else if(exp instanceof CallFuncObj) 
			{
				TypeDescriptor ctl = this.visitExp(((CallFuncObj)exp).getLexp());
				ctl=this.typeCoeff(ctl);
				if(ctl instanceof IdTypeDescriptor) 
				{
					ctl= this.getTypeDesc(((IdTypeDescriptor)ctl).getNomeTipo());
				}
				
				if(ctl instanceof ClassTypeDescriptor)
				{
					ClassTypeDescriptor ctd= (ClassTypeDescriptor) ctl;
					for(NodeAST klass :  this.ast) 
					{
						if(klass instanceof ClassDecl) 
						{
							ClassDecl c= (ClassDecl) klass;
							boolean contains=false;
							//se la classe corrente ha un insieme di classi estese
							if( (this.getClassST().lookup(ctd.getName()).getListofextclass())!=null) 
							{//allora setto il risultato della domanda "nella lista delle classi estese appartenenti alla classe identificata da this è presente la classe c?"
								contains= this.getClassST().lookup(ctd.getName()).getListofextclass().contains(c.getIdClass().getName());
							}
							//se il nome dellla classe è uguale
							if(c.getIdClass().getName().equals(ctd.getName()) || contains ) 
							{				
								for(MethDecl met: c.getMets()) 
								{	
									//se il nome del metodo è uguale
									if(met.getId().getName().equals(((CallFuncObj)exp).getId().getName())) 
									{
										int i =0;
										boolean flag= false;
										//se non ci sono espressioni dichiarate in Rexp , ma  formals ha dimensione 1 allora il paramentro dovrà essere this
										if(((CallFuncObj)exp).getRexp().size()==0 && met.getFormals().size()==1) 
										{
											if(!met.getFormals().get(0).getId().getName().equals("this"))
											{	
												flag=true;
											}
										}
										//se invece ci sono expressioni in Rexp e la dimensione dei formals è maggiore di uno
										else if(met.getFormals().size()>1) 
										{
											//se il primo paramentro è uguale a this
											if(met.getFormals().get(0).getId().getName().equals("this"))
											{	//incremento i di 1 così che i parametri verranno paragonati escludendo this
												i++;
												//se la dimensione dei formals -1 non è ugule a quelle dei rexp allora ci sarà un errore
												if(met.getFormals().size()-1!=((CallFuncObj)exp).getRexp().size()) 
												{
													flag=true;
												}else 
												{
													for(Exp expession : ((CallFuncObj)exp).getRexp()) 
													{	
															TypeDescriptor t1=this.visitExp(expession);
															TypeDescriptor t2=met.getFormals().get(i).getType();
															t1=this.typeCoeff(t1);
															t2=this.typeCoeff(t2);
															if(t1 instanceof IdTypeDescriptor || t2 instanceof IdTypeDescriptor ) 
															{
																if(this.classST.lookup(((IdTypeDescriptor)t2).getNomeTipo())!=null)
																{
																	t2= this.getTypeDesc(((IdTypeDescriptor)t2).getNomeTipo());
																}
																if(t1 instanceof IdTypeDescriptor) 
																{
																	if(this.classST.lookup(((IdTypeDescriptor)t1).getNomeTipo())!=null)
																	{
																		t1= this.getTypeDesc(((IdTypeDescriptor)t1).getNomeTipo());
																	}
																}
															}
															if(t1 instanceof IntTypeDescriptor && t2 instanceof IntTypeDescriptor) {i++;}
															else if(t1 instanceof BoolTypeDescriptor && t2 instanceof BoolTypeDescriptor){i++;}
															else if(((ClassTypeDescriptor)t1).getName().equals(((ClassTypeDescriptor)t2).getName())){i++;}
															else{flag=true;i++;}
													}
												}
											}
											
										}
										
										if(flag==false) 
										{
											
											if(met.getRetType() instanceof IdTypeDescriptor) 
											{
												
												return this.getTypeDesc(((IdTypeDescriptor)met.getRetType()).getNomeTipo());
											}
											return met.getRetType();
										}
									}
								}
							}

						}
							
					}				
				}
			}else if (exp instanceof New) 
			{	
				if(((New)exp).getExp()==null) 
				{
					//System.out.println("NEW class type ");
					return this.getTypeDesc((((New)exp).getId().getName()));
				}else 
				{
					TypeDescriptor TypeExp= this.visitExp(((New)exp).getExp());
					if(TypeExp instanceof IntTypeDescriptor || TypeExp instanceof BoolTypeDescriptor) 
					{//System.out.println("NEW  type ");
						return ((New)exp).getId().getType();
					}
				}
			} else if(exp instanceof Instanceof) 
			{
				//System.out.println("instanceof");
				if(this.getClassST().lookup(((Instanceof)exp).getId().getName())!= null) 
				{	TypeDescriptor type=this.visitExp(((Instanceof)exp).getExp());
					type=this.typeCoeff(type);
					if( type instanceof IdTypeDescriptor) 
					{	
						return new BoolTypeDescriptor();
					}
				}
			}
			return new ErrorTypeDescriptor();
			
		}

		private TypeDescriptor typeCoeff(TypeDescriptor t1) {
			if(t1 instanceof CoeffectTypeDescriptor) 
			{
				t1=((CoeffectTypeDescriptor)t1).getVarType();
			}
			return t1;
		}
		
		private TypeDescriptor getTypeDesc(String id) {
			//se id è presente nella tabella delle classi allora ritorno il tipo della classe
			if(this.classST.lookup(id)!= null) 
			{
				for(NodeAST klass :  ast) 
				{
					if(klass instanceof ClassDecl) 
					{
						ClassDecl c= (ClassDecl) klass;
						if(id.equals(c.getIdClass().getName())) 
						{
							return c.getType();
						}
					}
				}
			}
			//controllo se la variabile è nella tabella del body
				for(NodeId var: this.bTable.getSymbolTable().keySet()) 
				{
					if(var.getType().getClass()!= MethTypeDescriptor.class) 
					{
						if(var.getName().equals(id)) 
						{
							return var.getType();
						}
					}
				}
				//controllo se à presente nella tabella della classe
				for(NodeId var: this.cTable.getSymbolTable().keySet()) 
				{
					if(var.getType().getClass()!= MethTypeDescriptor.class) 
					{
						if(var.getName().equals(id)) 
						{
							return var.getType();
						}
					}
				}

				return new ErrorTypeDescriptor();
		}
		
		
		private void visitCoeffect(VarDecl var) throws TypeCheckingException {
			
			//controllo che la classe scritta come identificatore sia una classe e che tale classe sia di tipo coeffetto
			String classCoeff=((VarCoeff)((CoeffectTypeDescriptor)var.getType()).getVarCoeff()).getClassCoeff();
			if(this.classST.lookup(classCoeff)!=null) 
			{
				ClassAttribute attr= this.classST.lookup(classCoeff);
				
				if( attr.getIsCoeff() == Coef.COEFF || attr.getIsCoeff() == Coef.AUXCOEF ) 
				{
					//visito exp
					TypeDescriptor expType=this.visitExp(((CoeffectTypeDescriptor)var.getType()).getVarCoeff().getExpCoeff());
					
					if(!(expType instanceof ClassTypeDescriptor) || ((ClassTypeDescriptor)expType).isCoeff().equals(Coef.NOTCOEF)) 
					{
						throw new TypeCheckingException();
					}
				}else 
				{
					throw new TypeCheckingException();
				}
			}else 
			{
				throw new TypeCheckingException();
			}
		}
	
		private boolean Exist(VarDecl var, SymbolTable sTable) {
			if(sTable==null) 
			{
				return false;
			}
			for(NodeId p:sTable.getSymbolTable().keySet()) 
			{//se il nome di p e di var sono uguali e p non è un istanza di metodo						
				if((var.getId().getName()).equals(p.getName()) && p.getType().getClass() != MethTypeDescriptor.class )
				{
					return true;
				}
			}
			return false;
		}
		
	public ClassSymbolTable getClassST() {
		return classST;
	}
	public ClassDecl getActualClass() {
		return actualClass;
	}
	public void setActualClass(ClassDecl actualClass) {
		this.actualClass = actualClass;
	}
}
//to string espressioni, analisi espressioni, togliere typedescriptor nell'ast'