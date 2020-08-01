class ArrayTest1 {

    public static void main(String[] a){
        EncInt[] b;
        b = new EncInt[10];
        b[0] = PrivateTape.read();
        b[9] = (b[0]) * 2;
        System.out.println(b.length);
        System.out.println(b[0]);
        System.out.println(b[9]);
        Processor.answer(0);
    }

}
