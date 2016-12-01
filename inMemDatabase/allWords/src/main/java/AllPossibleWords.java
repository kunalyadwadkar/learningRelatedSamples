import java.util.*;
import java.util.regex.Pattern;

public class AllPossibleWords {
    public static final Set<String> DICTIONARY;

    static {
        DICTIONARY = new HashSet<>();
        DICTIONARY.add("store");
        DICTIONARY.add("ore");
        DICTIONARY.add("tore");
        DICTIONARY.add("home");
        DICTIONARY.add("depot");
        DICTIONARY.add("pot");
        DICTIONARY.add("to");
        DICTIONARY.add("pots");
        DICTIONARY.add("tea");
        DICTIONARY.add("to");
        DICTIONARY.add("teapot");
        DICTIONARY.add("a");
        DICTIONARY.add("potato");
        DICTIONARY.add("homete");
    }

    public List<String> getValidPartitions(String in) {
        List<String> allPartitions = getAllPartitions(in);

        List<String> returnVal = new ArrayList<>();
        for (String allPartition : allPartitions) {
            if (firstPartitionIsInDict(allPartition)) {
                returnVal.add(allPartition);
            }
        }

        return returnVal;
    }

    private List<String> getAllPartitions(String in) {

        if (in == null) {
            return new ArrayList<>();
        }

        if (in.length() == 1) {
            return Collections.singletonList(in);
        }

        List<String> partialSolutions = getAllPartitions(in.substring(1));

        List<String> returnVal = new ArrayList<>();
        for (String s : partialSolutions) {
            if (!hasPartitions(s)) {
                if (isInDictionary(s)) {
                    returnVal.add(String.format("%s %s", in.charAt(0), s));
                }
                returnVal.add(String.format("%s%s", in.charAt(0), s));
            } else {
                if (firstPartitionIsInDict(s)) {
                    returnVal.add(String.format("%s %s", in.charAt(0), s));
                }
                returnVal.add(String.format("%s%s", in.charAt(0), s));
            }
        }


        return returnVal;
    }

    private boolean hasPartitions(String s) {
        Pattern partitions = Pattern.compile(".* .*");
        return partitions.matcher(s).find();
    }

    private boolean isInDictionary(String s) {
        return DICTIONARY.contains(s);
    }

    private boolean firstPartitionIsInDict(String s) {
        String[] strings = s.split(" ");
        return DICTIONARY.contains(strings[0]);
    }
}
