import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        ArrayList<Integer> myList = new ArrayList<>();
        myList.add(7);
        myList.add(3);
        myList.add(19);
        myList.add(2);
        myList.add(4);
        myList.add(23);
        myList.add(13);
        myList.add(8);
        myList.add(6);
        myList.add(12);
        myList.add(24);
        myList.add(17);

        System.out.println("original list: " + myList);

        Stream<Integer> myStream = myList.stream();

        Optional<Integer> maxVal = myStream.max(Integer::compare);

        if (maxVal.isPresent()) {
            System.out.println("Max value is: " +  maxVal.get());

        }

        Optional<Integer> result = myList.parallelStream().reduce((a, b) -> (a > b ? b:a));

        if(result.isPresent()) {
            System.out.println("Min is " + result.get());
        }

        // print a sorted stream
        myList.parallelStream().sorted().forEachOrdered((n) -> System.out.println(n + " "));

        //another way to do the reduce operation for min (like the one above)
        Integer result1 = myList.stream().reduce(Integer.MAX_VALUE, (a, b) -> (a > b ? b : a));
        System.out.println("Min is : " +  result1);

        // sum of all values
        Optional<Integer> sum1 = myList.stream().reduce((a, b) -> a + b);
        if (sum1.isPresent()) {
            System.out.println(sum1.get());
        }

        Integer sum2 = myList.stream().reduce(0, (a, b) -> a + b);
        System.out.println(sum2);

        // product of all values
        Optional<Integer> prod1 = myList.parallelStream().reduce((a, b) -> a * b);
        if (prod1.isPresent()) {
            System.out.println(prod1.get());
        }

        Integer prod2 = myList.stream().reduce(1, (a, b) -> a * b);
        System.out.println(prod2);

        // product of square roots
        Optional<Double> ps1 = myList.parallelStream().map(Math::sqrt).reduce((a, b) -> a * b);
        if (ps1.isPresent()) {
            System.out.println(ps1.get());
        }


        //product of square roots using only reduce and parallel streams.
        double ps2 = myList.parallelStream().map(Double::valueOf)
                .reduce(
                        1.0,
                        (a, b) -> Math.sqrt(a) * Math.sqrt(b),
                        (a, b) -> a * b);

        System.out.println(ps2);

    }
}
