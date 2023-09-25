class speck {

	public static void main(String[] a){
		int i;
		int x;
		int y;
		EncInt X;
		EncInt Y;
		EncInt key_temp;
		int rounds;
		EncInt[] K;
		rounds = 22;
		K = new EncInt[rounds];
		i = 0;
		while (i < rounds) {
			key_temp = PrivateTape.read();
			K[i] = key_temp;
			i = i + 1;
		}
		x = PublicTape.read();
		y = PublicTape.read();
	  Y = y;
		X = x;

		i = 0;
		while (i < rounds) {
			Y = (Y >> 7) | (Y << 9);
	        Y = Y + X;
	        Y = Y ^ (K[i]);
	        X = (X >> 14) | (X << 2);
	        X = X ^ Y;
			i = i + 1;
		}

		System.out.println(X);
		System.out.println(Y);

		Processor.answer(Y);
	}
}
