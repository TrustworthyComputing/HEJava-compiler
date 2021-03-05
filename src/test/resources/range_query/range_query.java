class range_query {

	public static void main(String[] a){
		int min;
		int max;
		EncInt val;
		EncInt cond;
		min = PublicTape.read();
		max = PublicTape.read();
		val = PrivateTape.read();
		cond = (min <= val) & (val <= max);
		Processor.answer(cond);
	}
}
