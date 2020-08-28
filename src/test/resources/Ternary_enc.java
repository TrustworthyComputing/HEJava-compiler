class Ternary {
	public static void main(String[] a){
		EncInt x;
		EncInt y;
		EncInt res;
		x = PrivateTape.read();
		y = PrivateTape.read();

		res = ( x > y ) ? 1 : 13;
		Processor.answer(res);
	}
}
