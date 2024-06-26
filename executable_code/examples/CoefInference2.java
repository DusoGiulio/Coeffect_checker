
class Main {
public static void main ( String [] a ){
System.out.println (new Pair());
}
}class Pair {
	A fst;
	A snd;
	boolean t;

	Pair setFields(@Coeffect(coeffClass="Affinity", exp="Affinity.one()")Pair this, @Coeffect(coeffClass="Affinity", exp="Affinity.one()")A myFst, @Coeffect(coeffClass="Affinity", exp="Affinity.one()")A mySnd) {
		//t= 1;
		snd = mySnd;
		return this;
	}

}

class A {
	
	
	A drop1(@Coeffect(coeffClass="Affinity", exp="Affinity.zero()")A this,@Coeffect(coeffClass="Affinity", exp="Affinity.zero()")A c) {
		return new A();
	}

	
	A drop(@Coeffect(coeffClass="Affinity", exp="Affinity.zero()")A this) {
		return new A();
	}

	A identity(@Coeffect(coeffClass="Affinity", exp="Affinity.one()")A this) {
		return this;
	}

	Pair duplicate(@Coeffect(coeffClass="Affinity", exp="new Omega()")A this) {
		@Coeffect(coeffClass="Affinity", exp="Affinity.one()") Pair p;

		return p.setFields(this,this);
	}
	
	
	A metCall(@Coeffect(coeffClass="Affinity", exp="Affinity.one()") A this) {
		@Coeffect(coeffClass="Affinity", exp="new Omega()") A a1;
		@Coeffect(coeffClass="Affinity", exp="Affinity.one()") A a2;
		@Coeffect(coeffClass="Affinity", exp="Affinity.one()") A a3;
		Pair pop;
		a1 = new A();
		a3= new A();
		
		a2 = a3;
		pop=a1.duplicate();  //a4 2 volte  (a3 2 volta a1 4 volte)
		a1=a2.drop().identity();
		return a1;
	}
}
@CoeffectClass
class Affinity {

	Affinity sup(Affinity x) {
		Affinity result;
		if (this.leq(x))
			result = x;
		else
			result = this;
		return result;
	}

	boolean leq(Affinity x) {
		return true;
	}

	Affinity sum(Affinity x) {
		return x;
	}

	Affinity mult(Affinity x) {
		return this;
	}

	static Affinity zero() {
		return new ZeroA();
	}

	 static Affinity one() {
		return new One();
	}

	static Affinity fromNat(Nat n) {
		Affinity result;
		Succ succ;
		if (n instanceof Succ) {
			succ = (Succ) n;
			result = Affinity.fromNat(succ.getPred());
			result = result.sum(Affinity.one());
		}
		else
			result = Affinity.zero();
		return result;
	}
}

@CoeffectAuxClass("Affinity")
class ZeroA extends Affinity {
}

@CoeffectAuxClass("Affinity")
class One extends Affinity {
	boolean leq(Affinity x) {
		return !(x instanceof ZeroA);
	}

	Affinity sum(Affinity x) {
		Affinity result;
		if (x instanceof ZeroA)
			result = this;
		else
			result = new Omega();
		return result;
	}

	Affinity mult(Affinity x) {
		return x;
	}
}

@CoeffectAuxClass("Affinity")
class Omega extends Affinity {
	boolean leq(Affinity x) {
		return (x instanceof Omega);
	}

	Affinity sum(Affinity x) {
		return this;
	}

	Affinity mult(Affinity x) {
		Affinity result;
		if (x instanceof ZeroA)
			result = x;
		else
			result = this;
		return result;
	}
}




