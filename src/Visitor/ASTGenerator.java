package Visitor;

import java.util.ArrayList;

import ANTLR4.miniJavaBaseVisitor;
import ANTLR4.miniJavaParser.*;
import ASTnodes.Class.*;
import ASTnodes.Decl.*;
import ASTnodes.Exp.*;
import ASTnodes.Exp.Boolean;
import ASTnodes.Stm.*;
import Coeffect.Coef;
import Coeffect.VarCoeff;
import TypeDescriptor.*;

public class ASTGenerator extends miniJavaBaseVisitor<Object>{

	private ArrayList<NodeAST> AST;
	public ASTGenerator() 
	{
		this.setAST(new ArrayList<NodeAST>());
	}
	public ArrayList<NodeAST> getAST() {
		return AST;
	}

	public void setAST(ArrayList<NodeAST> aST) {
		AST = aST;
	}

//////////////////////////////////////////////////Nodi AST//////////////////////////////////////////////////
	
	
	//Ritorna l'AST composto dai vari array di ritorno dalla classe main a dalle altre classi del programma
    @Override
    public ArrayList<NodeAST> visitProgram(ProgramContext ctx) {
    	//aggiungo ad AST la classe MAIN
    	this.getAST().add(this.visitMainClass(ctx.mainClass()));
    	//aggiungo tutte le classi del programma all'AST
        for (ClassDeclContext c : ctx.classDecl()) {
        	this.getAST().add(this.visitClassDecl(c));
        }
        return this.getAST();
	}
    
    
//Gestionemain
    @Override
	public MainClass visitMainClass(MainClassContext ctx) {
    	//Prendo il nome che identifica la classe
		String mainIdtxt=ctx.IDENTIFIER(0).getText();
		//Instanzio un nuovo tipo di  mainClass pcon una nuova symboltable 
		//e i nomi della classe ereditata e ol tipo della classe ereditata pari a null
		ClassTypeDescriptor mainType= new ClassTypeDescriptor( mainIdtxt);
		//Prendo il nodo exp contenuto all'interno della classe main
		Exp exp= this.visitExp(ctx.exp());
		//creo un nuovo node id che identificherà il nome della classe e il suo tipo
		NodeId mainId= new NodeId(mainType,mainIdtxt);
		//ritorno un nodo mainclass
		return new MainClass(mainType,mainId,exp);
	}
    
    
//Gestione Classe
	@Override
	public ClassDecl visitClassDecl(ClassDeclContext ctx) {
		//inizio gestioine extends class
		String extendTxtClass= null;
		NodeId extendId=null;
		ClassTypeDescriptor extendTypeClass =null;
		if(ctx.IDENTIFIER(1)!=null) 
		{
			extendTxtClass=ctx.IDENTIFIER(1).getText();
			extendTypeClass= new ClassTypeDescriptor (extendTxtClass );
			extendId= new NodeId(extendTypeClass,extendTxtClass);
		}
		//fine gestione extends class
		//---------------------------------------------------------------------------------------------//
		//inizio gestione class
		String classTxt = ctx.IDENTIFIER(0).getText();
		ClassTypeDescriptor ClassType= new ClassTypeDescriptor(classTxt);
		if(ctx.coefClass()!=null) 
		{
			ClassType.setCoeff(this.visitCoefClass(ctx.coefClass()));
		}
		NodeId classId=new NodeId(ClassType,classTxt);

		//fine gestione class
		//---------------------------------------------------------------------------------------------//
		//inizio gestione variabili
		ArrayList<FieldDecl> listField =new ArrayList<FieldDecl>();
		for(FieldDeclContext var: ctx.fieldDecl()) 
		{
			//aggiungo var alla lista delle variabili
			listField.add(this.visitFieldDecl(var));
		}
		//fine gestione variabili
		//---------------------------------------------------------------------------------------------//
		//inizio gestione metodi
		ArrayList<MethDecl> listMets =new ArrayList<MethDecl>();
		for(MethodDeclContext met: ctx.methodDecl()) 
		{
			//aggiungo met alla lista dei metopdi
			MethDecl m =this.visitMethodDecl(met);
			listMets.add(m);
		}
		//fine  gestione metodi
		//---------------------------------------------------------------------------------------------//
		//ritorno nodo della classe
		return new ClassDecl(ClassType,classId,extendId,listField,listMets);
		//---------------------------------------------------------------------------------------------//
	}
	@Override
	public FieldDecl visitFieldDecl (FieldDeclContext ctx) {
		//---------------------------------------------------------------------------------------------//
		//prendo il nome della variabile
		String name = ctx.IDENTIFIER().getText();
		//prendo il typo della variabile
		TypeDescriptor type= this.visitType(ctx.type());
		//creo il nodo che identifica la variabile
		NodeId varId= new NodeId(type, name);
		//ritorno il nododell'AST vardecl
		return new FieldDecl(type,varId);
		//---
	}
	
	
//Gestione dichiarazione delle variabili
	@Override
	public VarDecl visitVarDecl(VarDeclContext ctx) {
		//---------------------------------------------------------------------------------------------//
		//prendo il nome della variabile
		String name = ctx.IDENTIFIER().getText();
		//prendo il typo della variabile
		TypeDescriptor type= this.visitTypeCoeff(ctx.typeCoeff());
		//creo il nodo che identifica la variabile
		NodeId varId= new NodeId(type, name);
		//ritorno il nododell'AST vardecl
		return new VarDecl(type,varId);
		//---------------------------------------------------------------------------------------------//
	}
	
	
	@Override
	public VarDecl  visitVarDeclp(VarDeclpContext ctx) {
		//---------------------------------------------------------------------------------------------//
		//prendo il nome della variabile
		String name= ctx.IDENTIFIER().getText();
		//prendo il typo della variabile
		TypeDescriptor type= this.visitTypeCoeff(ctx.typeCoeff());
		//creo il nodo che identifica la variabile
		NodeId varId= new NodeId(type, name);
		//ritorno il nododell'AST vardecl
		return new VarDecl(type,varId);
		//---------------------------------------------------------------------------------------------//
	}
	@Override
	public  CoeffectTypeDescriptor visitTypeCoeff(TypeCoeffContext ctx) 
	{ 
		TypeDescriptor type=this.visitType(ctx.type());
		VarCoeff varCoeff=null;
		if(ctx.coeffect()!=null) 
		{
			varCoeff= this.visitCoeffect(ctx.coeffect());
		}
		return new CoeffectTypeDescriptor(type, varCoeff);
	}
	@Override
	public VarCoeff visitCoeffect(CoeffectContext ctx) 
	{
		String idClass=ctx.IDENTIFIER().getText();
		Exp expCoeff= this.visitExp(ctx.exp());
		return new VarCoeff(expCoeff, idClass);
	}
	
	
//Gestione metodi della classe	
	@Override
	public MethDecl visitMethodDecl(MethodDeclContext ctx)  {
		
		//Intestazione metodo
		//---------------------------------------------------------------------------------------------//
		//Prendo il nome del metodo
		String methName= ctx.IDENTIFIER(0).getText();
		//tipo del metodo
		TypeDescriptor methRetType= this.visitType(ctx.type());
		
		
		//inizio gestione variabili nell'intestazione del metodo
		ArrayList<VarDecl> listVar =new ArrayList<VarDecl>();
		boolean isThis= false;
		if(ctx.THIS()!=null) 
		{
			isThis=true;
			listVar.add(new VarDecl(this.visitTypeCoeff(ctx.typeCoeff()),new NodeId(this.visitTypeCoeff(ctx.typeCoeff()),ctx.THIS().getText()) ));
		}
		if(ctx.typeCoeff()!=null && ctx.IDENTIFIER(1)!=null) 
		{
			listVar.add(new VarDecl(this.visitTypeCoeff(ctx.typeCoeff()),new NodeId(this.visitTypeCoeff(ctx.typeCoeff()),ctx.IDENTIFIER(1).getText()) ));
		}

		for(VarDeclpContext var: ctx.varDeclp()) 
		{
			//aggiungo var alla lista delle variabili
			listVar.add(this.visitVarDeclp(var));
		}
		//fine gestione variabili
		//---------------------------------------------------------------------------------------------//
		//Body
		ArrayList<VarDecl> listVarBody =new ArrayList<VarDecl>();
		for(VarDeclContext var: ctx.varDecl()) 
		{
			//aggiungo var alla lista delle variabili
			listVarBody.add(this.visitVarDecl(var));
		}
		ArrayList<Stm> stmList= new ArrayList<Stm>();
		for(StatementContext stm: ctx.statement()) 
		{
			//aggiungo stm alla lista degli stm
			stmList.add(this.visitStatement(stm));
		}
		//creo il tipo exp per il body
		Exp exp = this.visitExp(ctx.exp());
		//creo il body del metodo
		Body body= new Body(null,listVarBody,stmList,exp);//null perchè non conosco ancora il tipo di ritorno effettivo del metodo
		boolean isStatic= false;
		if(ctx.STATIC()!=null) 
		{
			isStatic=true;
		}
		if(isStatic==true && isThis==true) 
		{
			System.err.println("Errore sintattico in un metodo non può essere dichiarato metodo Statico  con This");
			return null;
		}
		MethTypeDescriptor methType= new MethTypeDescriptor(isStatic);
		//Creo il nodeId del metodo
		NodeId methId= new NodeId(methType, methName);
		//riotno il nodo del metodo
		return new MethDecl(methType,listVar,methRetType,methId,body);
		//---------------------------------------------------------------------------------------------//
	}
	@Override
	public TypeDescriptor visitType(TypeContext ctx) {
		//---------------------------------------------------------------------------------------------//
		// In base al tipo creo e ritorno il nodo adatto
		TypeDescriptor type =new ErrorTypeDescriptor();
		
		if(ctx.IDENTIFIER()!=null) 
		{
			type= new IdTypeDescriptor(ctx.IDENTIFIER().getText());
		}
		else if(ctx.INT()!=null) 
		{
			type = new IntTypeDescriptor();
		}
		else if(ctx.BOOLEAN()!=null) 
		{
			type = new BoolTypeDescriptor();
		}
		if(ctx.LBRACK()!=null) 
		{
			type= new ArrayTypeDescriptor(type);
		}
		
		return type;
		//---------------------------------------------------------------------------------------------//
	}
	
	@Override
	public Stm visitStatement(StatementContext ctx) {
		//---------------------------------------------------------------------------------------------//
		// in base al tipo dichiaro un nodo differente di tipo stm
		Stm stm=null;
		if(ctx.LBRACE()!=null) 
		{
			ArrayList<Stm> multiStm= new ArrayList<Stm>();
			for(StatementContext s: ctx.statement()) 
			{
				multiStm.add(this.visitStatement(s));
			}
			stm=new Multi(multiStm);
		}
		else if(ctx.IF()!=null)
		{
			Exp exp = this.visitExp(ctx.exp(0));
			Stm ifstm= this.visitStatement(ctx.statement(0));
			Stm elsestm=this.visitStatement(ctx.statement(1));
			
			stm= new IF( exp, ifstm, elsestm);
		}
		else if(ctx.WHILE()!=null) 
		{
			Exp exp = this.visitExp(ctx.exp(0));
			Stm wstm= this.visitStatement(ctx.statement(0));
			
			stm= new WHILE(exp, wstm);
		}
		else if(ctx.SOP()!=null) 
		{
			Exp exp= this.visitExp(ctx.exp(0));
			
			stm= new SOP(exp);
		}
		else if(ctx.IDENTIFIER()!=null) 
		{
			Exp expl = this.visitExp(ctx.exp(0));
			String id= ctx.IDENTIFIER().getText();
			Exp expr=null;

			{
				if(ctx.LBRACK()!=null) 
				{
					expr = this.visitExp(ctx.exp(1));
					stm= new ARRAYASSIGN(expl,expr,id);
				}else 
				{
					stm= new ASSIGN(expl,id);	
				}
			}

		}
		return stm;
		//---------------------------------------------------------------------------------------------//
	}
	
	@Override
	public Exp visitExp(ExpContext ctx) {
		//---------------------------------------------------------------------------------------------//
		// in base al tipo dichiaro un nodo differente di tipo exp
		if(ctx==null) {return null;}
		//INSTACEOF typedescriptor id a dx, exp classe sx
		if(ctx.INSTANCEOF()!=null) 
		{
			return new Instanceof(new NodeId(new IdTypeDescriptor(ctx.IDENTIFIER().getText()),ctx.IDENTIFIER().getText()),this.visitExp(ctx.exp(0)));
		}
		//UNIOP
		if( !ctx.exp().isEmpty() && ctx.BANG()!=null)
		{
			if(ctx.BANG()!=null) 
			{
				return new UnaryOp(ctx.BANG(),this.visitExp(ctx.exp(0)));
			}
		}
		//BINOP
		else if(ctx.MUL()!= null || ctx.DIV()!= null || ctx.ADD()!= null || ctx.SUB()!= null ||   ctx.AND()!= null || ctx.OR()!= null || ctx.GT()!= null || ctx.LT()!= null ||ctx.ASSIGN()!= null  && !ctx.exp().isEmpty()) 
		{
			Exp lexp= this.visitExp(ctx.exp(0));
			Exp rexp= this.visitExp(ctx.exp(1));
			if(ctx.ADD()!=null) 
			{
				return new BinOp(ctx.ADD(),lexp,rexp);
			}
			if(ctx.SUB()!=null) 
			{
				return new BinOp(ctx.SUB(),lexp,rexp);
			}
			if(ctx.MUL()!=null) 
			{
				return new BinOp(ctx.MUL(),lexp,rexp);
			}
			if(ctx.AND()!=null) 
			{
				return new BinOp(ctx.AND(),lexp,rexp);
			}
			if(ctx.OR()!=null) 
			{
				return new BinOp(ctx.OR(),lexp,rexp);
			}
			if(ctx.GT()!=null) 
			{
				return new BinOp(ctx.GT(),lexp,rexp);
			}
			if(ctx.LT()!=null) 
			{
				return new BinOp(ctx.LT(),lexp,rexp);
			}
			if(ctx.ASSIGN()!=null) 
			{
				return new BinOp(ctx.ASSIGN(),lexp,rexp);
			}
			if(ctx.DIV()!=null) 
			{
				return new BinOp(ctx.DIV(),lexp,rexp);
			}
		}
		//NEW
		else if( ctx.NEW()!=null ) 
		{
			if(ctx.INT()!=null) 
			{
				if(ctx.LBRACK()!=null) 
				{
					return new New(new NodeId(new ArrayTypeDescriptor(new IntTypeDescriptor()),ctx.INT().getText()),this.visitExp(ctx.exp(0)));
				}
				return new New(new NodeId(new IntTypeDescriptor(), ctx.INT().getText()),this.visitExp(ctx.exp(0)));
			}
			else if(ctx.BOOLEAN()!=null) 
			{
				if(ctx.LBRACK()!=null) 
				{
					return new New(new NodeId(new ArrayTypeDescriptor(new BoolTypeDescriptor()),ctx.BOOLEAN().getText()),this.visitExp(ctx.exp(0)));
				}
				return new New(new NodeId(new BoolTypeDescriptor(),ctx.BOOLEAN().getText()),this.visitExp(ctx.exp(0)));
			}
			else if(ctx.IDENTIFIER()!=null && ctx.exp().isEmpty()) 
			{
				if(ctx.LBRACK()!=null) 
				{
					return new New(new NodeId(new ArrayTypeDescriptor(new IdTypeDescriptor(ctx.IDENTIFIER().getText())), ctx.IDENTIFIER().getText()),this.visitExp(ctx.exp(0)));
				}
				return new New(new NodeId(new IdTypeDescriptor(ctx.IDENTIFIER().getText()),ctx.IDENTIFIER().getText()),this.visitExp(ctx.exp(0)));
			}
			else 
			{
				return new New(new NodeId(new IdTypeDescriptor(ctx.IDENTIFIER().getText()),ctx.IDENTIFIER().getText()),null);
			}
		}
		//ARRELEM accede ad un elelemnto di un array
		else if(ctx.exp(1)!=null && ctx.LBRACK()!=null && ctx.NEW()==null) 
		{
			
			return new ArrElem(this.visitExp(ctx.exp(0)),this.visitExp(ctx.exp(1)));
		}
		
		//LENGHT
		else if(ctx.LENGTH()!=null) 
		{
			return new Lenght(this.visitExp(ctx.exp(0)));
		}
		//PAREN
		else if(ctx.exp()!=null && ctx.LPAREN()!=null && ctx.RPAREN()!=null && ctx.IDENTIFIER()==null) 
		{
			return this.visitExp(ctx.exp(0));
		}
		//CAST
		else if(ctx.exp()!=null && ctx.LPAREN()!=null && ctx.RPAREN()!=null && ctx.IDENTIFIER()!=null && ctx.DOT()==null) 
		{
			return new Cast(this.visitExp(ctx.exp(0)),new NodeId(new IdTypeDescriptor(ctx.IDENTIFIER().getText()), ctx.IDENTIFIER().getText()));
		}
		//THIS
		else if(ctx.THIS()!=null) 
		{
			return new This();
		}
		//chiamata a funzione da oggetto exp DOT IDENTIFIER LPAREN exp* RPAREN
		else if(ctx.IDENTIFIER()!= null && ctx.DOT()!=null) 
		{
			ArrayList<Exp> list= new ArrayList<Exp>();
			Exp expr = null;
			int i=0;
			for( @SuppressWarnings("unused") ExpContext exp:ctx.exp()) 
			{
				if(i==0)
				{
					expr=this.visitExp(ctx.exp(0));
					i=1;
				}else 
				{
					list.add(this.visitExp(ctx.exp(i)));
					i++;
				}
				
			}
			return new CallFuncObj(new NodeId(new IdTypeDescriptor(ctx.IDENTIFIER().getText()),ctx.IDENTIFIER().getText()), expr,list);
		}
		//IDENTIFIER
		else if(ctx.IDENTIFIER()!=null && ctx.exp()!=null && ctx.NEW()==null) 
		{
			return new Id(new NodeId(new IdTypeDescriptor(ctx.IDENTIFIER().getText()),ctx.IDENTIFIER().getText()));
		}
		//DEC_BOOL
		else if(ctx.BOOL_LITERAL()!=null || ctx.DECIMAL_LITERAL()!=null) 
		{
			if(ctx.BOOL_LITERAL()!=null) 
			{
				return new Boolean(ctx.BOOL_LITERAL().getText());
				
			}
			else 
			{
				return new Decimal(ctx.DECIMAL_LITERAL().getText());
			}
		}
		System.err.println("Errore");
		return null;
		//---------------------------------------------------------------------------------------------//
	}
	public Coef visitCoefClass(CoefClassContext ctx) 
	{
		if(ctx.COEFCLASS()!=null) 
		{
			return Coef.COEFF;
		}else if(ctx.COEFSUBCLASS()!=null) 
		{
			return Coef.AUXCOEF;
		}
		return Coef.NOTCOEF;
		
	}
}
