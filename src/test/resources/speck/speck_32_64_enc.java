class speck {

	public static void main(String[] a){
		int i;
		EncInt x;
		EncInt y;
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
		x = PrivateTape.read();
		y = PrivateTape.read();

		i = 0;
		while (i < rounds) {
			y = (y >> 7) | (y << 9);
	        y = y + x;
	        y = y ^ (K[i]);
	        x = (x >> 14) | (x << 2);
	        x = x ^ y;
			i = i + 1;
		}

		System.out.println(x);
		System.out.println(y);

		Processor.answer(y);
	}
}
