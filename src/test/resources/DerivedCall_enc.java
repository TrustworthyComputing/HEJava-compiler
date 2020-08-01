class DerivedCall {
	public static void main(String[] x) {
		EncInt i;
		B b;
		F f;
		f = new F();
		b = new B();
		i = f.foo(b);
		Processor.answer(0);
	}
}

class A {
	int a;
}

class B extends A {
	int b;
}

class F {
	public EncInt foo(A a) {
		EncInt x;
		x = PrivateTape.read();
		return x;
	}
}
