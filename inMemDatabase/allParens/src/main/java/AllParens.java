import java.util.ArrayList;
import java.util.List;

public class AllParens {
    int numLeftParensUsed = 0;
    int numParens = 0;
    int numRightParensUsed = 0;

    public List<String> parenPermute(int numParens) {
        List<String> out = new ArrayList<>();
        this.numParens = numParens;
        backTrack(new String[2*numParens], -1, out);
        return out;
    }


    void backTrack(String[] a, int k, List<String> out) {

        if (isASolution(k + 1)) {
            processSolution(a, out);
        } else {
            k++;
            List<String> candidateK = getCandidates(k);
            for (String s : candidateK) {

                preProcess(s);
                a[k] = s;
                backTrack(a, k, out);
                postProcess(s);
            }
        }

    }

    private void preProcess(String s) {
        if (s.equals("(")) {
            numLeftParensUsed++;
        }
        if (s.equals(")")) {
            numRightParensUsed++;
        }
    }

    private void postProcess(String s) {
        if (s.equals("(")) {
            numLeftParensUsed--;
        }
        if (s.equals(")")) {
            numRightParensUsed--;
        }
    }

    private void processSolution(String[] a, List<String> out) {
        StringBuilder strBuf = new StringBuilder();
        for (String anA : a) {
            strBuf.append(anA);
        }
        out.add(strBuf.toString());
    }

    boolean isASolution(int k) {
        return k == (2 * numParens);
    }

    List<String> getCandidates(int k) {
        List<String> returnVal = new ArrayList<>();
        if (numLeftParensUsed > 0 && (numLeftParensUsed > numRightParensUsed)) {
            returnVal.add(")");
        }

        if (numLeftParensUsed < numParens) {
            returnVal.add("(");
        }

        return returnVal;
    }

}
