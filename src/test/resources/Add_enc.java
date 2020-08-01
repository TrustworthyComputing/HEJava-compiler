class Add {

	public static void main(String[] a) {
		EncInt x;
		x = PrivateTape.read();
		x = x - 2;
		x += 15;
		Processor.answer(x);
	}

}
