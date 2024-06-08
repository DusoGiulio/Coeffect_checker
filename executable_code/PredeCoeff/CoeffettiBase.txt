@CoeffectClass
abstract class Nat {
	abstract boolean leq(Nat x) ;

	Nat sup(Nat x) {
		Nat result;
		if (this.leq(x)){
			result = x;}
		else{
			result = this;}
		return result;
	}

	abstract Nat sum(Nat x) ;

	abstract Nat mult(Nat x);

	static Nat zero() {
		return new Zero();
	}

	static Nat one(){
		Nat result;
		result = new Succ().setPred(Nat.zero());
		return result;
	}
	
	static Nat fromNat(Nat n) {
		return n;
	}
}


@CoeffectAuxClass("Nat")
class Succ extends Nat {
	Nat pred;

	Nat setPred(Nat p) {
		pred = p;
		return this;
	}
	
	Nat getPred() {
		return pred;
	}

	boolean leq(Nat x) {
		boolean result;
		if (x instanceof Zero)
			result = false;
		else
			result = pred.leq(((Succ) x).getPred());
		return result;
	}

	Nat sum(Nat x) {
		Nat result;
		result = new Succ().setPred(pred.sum(x));
		return result;
	}

	Nat mult(Nat x) {
		Nat result;
		if (x instanceof Zero)
			result = x;
		else
			result = pred.mult(x).sum(x);
		return result;
	}
	

}

@CoeffectAuxClass("Nat")
 class Zero extends Nat {
 	Nat sum(Nat x) {
		return x;
	}

	Nat mult(Nat x) {
		return this;
	}
		boolean leq(Nat x) {
		return true;
	}

	 }


@CoeffectClass
class Triv {
	boolean leq(Triv t) {
		return true;
	}

	Triv sup(Triv t) {
		return this;
	}

	Triv sum(Triv t) {
		return this;
	}

	Triv mult(Triv t) {
		return this;
	}

	static Triv zero() {
		return new Triv();
	}

	static Triv one() {
		return new Triv();
	}
	
	static Triv fromNat(Nat n) {
		return new Triv();
	}
}

