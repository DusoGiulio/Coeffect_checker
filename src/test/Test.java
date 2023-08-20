package test;

import static org.junit.Assert.*;
import org.junit.jupiter.api.*;

import Coeffect.*;

class Test {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}
	//Testa il metodo op, che eseguira nello stesso modo sum e sup
	@org.junit.jupiter.api.Test
	void Coeffect_sum() {
		Coeffect cf=new Coeffect( "Nat.One()","Nat"); 
		Coeffect cf1=new Coeffect( "Nat.One()","Nat");
		Coeffect cf2=new Coeffect( "new Omega()","Omega");
		Coeffect cf3=new Coeffect( "Affinity.One()","Affinity");
		//somma fra due classi uguali
		cf=cf.op(cf1, "sum");
		assertEquals(cf.getCoefExpr(), "Nat.One().sum(Nat.One())");
		//somma fra una classe nat e una non nat
		cf=cf1.op(cf2, "sum");
		assertEquals(cf.getCoefExpr(), "(Omega.fromNat(Nat.One()) ).sum(new Omega())");
		//somma fra una classe nat e una non nat
		cf=cf2.op(cf1, "sum");
		assertEquals(cf.getCoefExpr(), "new Omega().sum(Omega.fromNat(Nat.One())");
		//somma fra una classe non nat e una non nat
		cf=cf2.op(cf3, "sum");
		assertEquals(cf.getCoefExpr(), "newTriv()");	
	}
	//Testa il metodo op, che eseguira mult
		@org.junit.jupiter.api.Test
		void Coeffect_mult() {
			Coeffect cf=new Coeffect( "Nat.One()","Nat"); 
			Coeffect cf1=new Coeffect( "Nat.Zero()","Nat");
			Coeffect cf2=new Coeffect( "new Omega()","Omega");
			Coeffect cf3=new Coeffect( "Affinity.One()","Affinity");
			//moltiplico fra due classi uguali
			cf=cf.op(cf1, "mult");
			assertEquals(cf.getCoefExpr(), "Nat.One().sup(Nat.one()).mult(Nat.Zero().sup(Nat.one()))");
			//moltiplico fra una classe nat e una non nat
			cf=cf1.op(cf2, "mult");
			assertEquals(cf.getCoefExpr(), "(Omega.fromNat(Nat.Zero().sup(Nat.one())) ).mult(new Omega().sup(Omega.fromNat(Nat.one()))");
			//moltiplico fra una classe nat e una non nat
			cf=cf2.op(cf1, "mult");
			assertEquals(cf.getCoefExpr(), "new Omega().sup(Omega.fromNat(Nat.one()).mult(Omega.fromNat(Nat.Zero().sup(Nat.one()).sup(Nat.one()))");
			//moltiplico fra una classe non nat e una non nat
			cf=cf2.op(cf3, "mult");
			assertEquals(cf.getCoefExpr(), "newTriv()");	
		}
	//Testa i metodi di findElement, addElement 
	@org.junit.jupiter.api.Test
	void CoeffectTable_find_add() {
		CoeffectTable cft= new CoeffectTable();
		cft.addElement("a",new Coeffect( "Nat.One()","Nat"));
		cft.addElement("b",new Coeffect( "Nat.One()","Nat"));
		assertEquals(cft.findElement("a").getCoefExpr(),"Nat.One()");
		assertNotNull(cft.findElement("a"));
		assertNull(cft.findElement("c"));
		
	}
	//Testa  se i metodi mult,sup,sum se invocati ritornino un'altro oggetto
	@org.junit.jupiter.api.Test
	void CoeffectTable_op() {
		CoeffectTable cft0= new CoeffectTable();
		CoeffectTable cft1= new CoeffectTable();
		CoeffectTable cft2= cft0.sum(cft1);
		assertNotEquals(cft2,cft0);	
	}

}
