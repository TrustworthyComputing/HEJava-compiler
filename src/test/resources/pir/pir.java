class pir {

	public static void main(String[] a){
		int i;
		int key;
		EncInt k;
		EncInt val;
		int size;
		size = PublicTape.read();
		key = PublicTape.read();
		i = 0;
		while (i < size) {
			k = PrivateTape.read();
			val = PrivateTape.read();
			if (k == key) {
				Processor.answer(val);
			}
			i = i + 1;
		}
		Processor.answer(0);
	}
}
