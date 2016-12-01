import java.util.ArrayList;
import java.util.List;

public class AllSubsets {

    public List<List<Integer>> allSubsets(Integer[] integers) {
        List<List<Integer>> out = new ArrayList<>();
        backTrack(integers, out, new Boolean[integers.length], -1);
        return out;
    }

    private void backTrack(Integer[] integers, List<List<Integer>> out, Boolean[] a, int k) {
        if (isASolution(k+1, integers.length)) {
            addSolution(a, out, integers);
        } else {
            k++;
            Boolean[] candidates = getCandidates(k);
            for (int i = 0; i < candidates.length; i++) {
                a[k] = candidates[i];
                backTrack(integers, out, a, k);
            }
        }
    }

    private void addSolution(Boolean[] a, List<List<Integer>> out, Integer[] integers) {
        List<Integer> outList = new ArrayList<>();
        for (int i = 0; i < a.length; i++) {
            if (a[i]) {
                outList.add(integers[i]);
            }
        }

        out.add(outList);
    }

    private Boolean[] getCandidates(int k) {
        return new Boolean[]{true, false};
    }


    boolean isASolution(int k, int len) {
        return k == len;
    }

}