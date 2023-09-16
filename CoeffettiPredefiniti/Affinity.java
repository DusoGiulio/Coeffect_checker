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
			result = fromNat(succ.getPred());
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
