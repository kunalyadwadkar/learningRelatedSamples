public class TestAllParens {
    public static void main(String[] args) {
        AllParens allParens = new AllParens();

        allParens.parenPermute(3).forEach(System.out::println);
    }
}
