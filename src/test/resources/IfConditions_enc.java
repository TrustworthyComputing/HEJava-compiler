class IfConditions {

    public static void main(String [] a) {
        EncInt x;
        EncInt y;
        EncInt z;

        x = PrivateTape.read();
        y = PrivateTape.read();

        z = (x < y);
        System.out.println(z);

        z = (x <= y);
        System.out.println(z);

        z = (x > y);
        System.out.println(z);

        z = (x >= y);
        System.out.println(z);

        z = (x == y);
        System.out.println(z);

        z = (x != y);
        System.out.println(z);

        Processor.answer(0);
    }

}
