class Main {
	public static void main(String[] a) {
		System.out.println(new Pair());
	}
}


class Pair {
	A fst;
	A snd;
	boolean t;

	Pair setFields(@Coeffect(coeffClass = "Affinity", exp = "Affinity.one()") Pair this,
			@Coeffect(coeffClass = "Triv", exp = "new Triv()") A myFst,
			@Coeffect(coeffClass = "Triv", exp = "new Triv()") A mySnd) {
		snd = mySnd;
		fst =myFst;
		return this;
	}

}

class A extends Pair {

	A zero(@Coeffect(coeffClass = "Nat", exp = "Nat.one()") A this,
			@Coeffect(coeffClass = "Nat", exp = "Nat.zero()") A prova) {
		@Coeffect(coeffClass = "Nat", exp = "Nat.one()")
		int b;
		@Coeffect(coeffClass = "Nat", exp = "Nat.zero()")
		int c;
		c = b;
		return fst;
	}

	A drop(@Coeffect(coeffClass = "Affinity", exp = "Affinity.zero()") A this) {
		return new A();
	}

	A identity(@Coeffect(coeffClass = "Affinity", exp = "Affinity.one()") A this) {
		return this;
	}

	A duplicate(@Coeffect(coeffClass = "Affinity", exp = "new Omega()") A this) {
		@Coeffect(coeffClass = "Affinity", exp = "Affinity.one()") A x;
		x=this; x=this; return x;
	}
	


	int xy() {
		@Coeffect(coeffClass = "Affinity", exp = "new Omega()")
		int x;
		@Coeffect(coeffClass = "Affinity", exp = "Affinity.one()")
		int y;
		x = y;
		x = 7;
		return x + x;
	}

}

@CoeffectClass
abstract class Affinity {

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
		} else
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
