import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        int[] in = {1, 7, 3, 4};

        Arrays.stream(productOfInts(in)).forEach(System.out::println);
    }

    static int[] productOfInts(int[] ints) {
        int len = ints.length;
        int product = 1;
        int[] returnints = new int[len];

        for(int i=0; i<len; i++) {
            int t = ints[i];
            returnints[i] = product;
            product *= t;
        }

        product = 1;
        for (int i = len - 1; i >= 0; i--) {
            int t = ints[i];
            returnints[i] *= product;
            product *= t;
        }

        return returnints;
    }
}
