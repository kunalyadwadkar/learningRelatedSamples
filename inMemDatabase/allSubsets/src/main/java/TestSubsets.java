import java.util.List;

public class TestSubsets {

    public static void main(String[] args) {
        AllSubsets allSubsets = new AllSubsets();
        List<List<Integer>> subsets = allSubsets.allSubsets(new Integer[]{1, 2, 3, 4});

        subsets.forEach(n -> {
            System.out.println(n.toString());
        });
    }
}
