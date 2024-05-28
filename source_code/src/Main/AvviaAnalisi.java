package Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import coeffectChecking.CoeffDefinitioinCheck;
import coeffectChecking.CoeffInference;
import coeffectChecking.ResultGenerator.Compile_Execute;
import coeffectChecking.ResultGenerator.SurceCodeComposer;
import parser.ASTGenerator;
import parser.ANTLR4.miniJavaLexer;
import parser.ANTLR4.miniJavaParser;
import parser.ANTLR4.miniJavaParser.ProgramContext;
import parser.ASTnodes.Class.NodeAST;
import typeChecking.Fill_STC_STM;
import typeChecking.TypeChecking;
import typeChecking.Exceptioin.SemanticException;
import typeChecking.Exceptioin.TypeCheckingException;

public class AvviaAnalisi {

	FileInputStream inputStream=null;
	String sep= FileSystems.getDefault().getSeparator();
	String file="analizedFile.txt";
	
	public AvviaAnalisi(File indirizzo) throws IOException {
	String indirizzoCompleto=indirizzo.toString()+sep+file;
	try {
		inputStream = new FileInputStream(indirizzoCompleto);
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}
	//creo uno scanner utilizzando il lexer generato automaticamente da ANTLR4
	miniJavaLexer lexer = new miniJavaLexer(new  ANTLRInputStream(inputStream ));
	//creo una sequenza di token usando lo scanner
	CommonTokenStream tokens = new CommonTokenStream(lexer);
	tokens.fill();
	//Creo un parser usando il parser di ANTLR immettendo la sequenza dei token
	miniJavaParser parser = new miniJavaParser(tokens);
	// creo un albero di parsing partendo dal nodo prog che � il nodo di partenza della grammatica
	ProgramContext p= parser.program();
	if(parser.getNumberOfSyntaxErrors()==0)
	{
		System.out.println("\tPARSING PASSATO\n");
		//istanzio un nuovo visitor
		try 
		{
		ASTGenerator visitor= new ASTGenerator();
		//eseguo la visita sull'albero
		ArrayList<NodeAST> AST=visitor.visitProgram(p);
		if(visitor.getNumberOfError()==0){

		
		try 
		{
			Fill_STC_STM firstvisit = new Fill_STC_STM(AST);
			try 
			{
				 new TypeChecking(AST, firstvisit.getClassST());
				System.out.println("\tTYPE CHECKING PASSATO\n");
				new CoeffDefinitioinCheck(AST);
				CoeffInference secondVisit=new CoeffInference(AST,firstvisit.getClassST());
				secondVisit.visitProgram(AST);				
		        String nomeCartella = indirizzo.toString(); 
		        String nomeFile = "CoeffectResult.java";
		        String percorsoCompleto = nomeCartella + sep + nomeFile;
		        SurceCodeComposer codiceSorgente=new SurceCodeComposer(AST,secondVisit.getClassST(),indirizzoCompleto);
		        System.out.println("ESEGUO "+nomeFile+" nella cartella "+nomeCartella+"\n");	
		        new Compile_Execute(codiceSorgente.getCodiceSorgente(),percorsoCompleto,sep);
		
			} catch (TypeCheckingException  | SemanticException e) 
			{
				System.err.println(e.getMessage());
				System.err.println("//////////////////////////////////////\n\tTYPE CHECKING FALLITO");
			}
		} catch (TypeCheckingException e1) {
			System.err.println(e1.getMessage());
		} catch (SemanticException e1) {
			System.err.println(e1.getMessage());
		}
		}else {
			System.err.println("//////////////////////////////////////\n\tPARSING FALLITO");
		}
		}catch(NullPointerException e) 
		{	
			System.err.println("//////////////////////////////////////\n\tERRORE SEMANTICO");
			System.err.println(e.getMessage());
		}
	}
	else 
	{
	System.err.println("//////////////////////////////////////\n\tPARSING FALLITO");
	}
	
	}
}
