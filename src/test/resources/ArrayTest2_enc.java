class ArrayTest2 {
    public static void main(String[] a){
        boolean n;
        n = new Test().start(5);
        Processor.answer(n);
    }
}


class Test {

	public boolean start(int sz){
		EncInt[] b;
		int l;
		int i;
		b = new EncInt[sz];
		l = b.length;
		i = 0;
		while (i < (l)) {
			b[i] = PrivateTape.read();
			System.out.println(b[i]);
			i = i + 1;
		}
        System.out.println(l);
		return true;
	}

}
