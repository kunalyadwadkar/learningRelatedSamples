public class Test {
    public static void main(String[] args) {
        AllPossibleWords underTest = new AllPossibleWords();
        underTest.getValidPartitions("hometeapotato").forEach(System.out::println);
    }
}
