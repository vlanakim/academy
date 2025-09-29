public class Main {
    public static void main(String[] args) {
        MyStringBuilder msb = new MyStringBuilder("Hello");

        msb.append(" World");
        System.out.println(msb);

        msb.delete(5, 11);
        System.out.println(msb);

        msb.undo();
        System.out.println(msb);

        msb.undo();
        System.out.println(msb);
    }
}
