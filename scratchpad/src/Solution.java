public class Solution {
    public static LinkedListNode reverse(LinkedListNode head) {
        if (head == null) {
            return null;
        }

        LinkedListNode curr = head;
        LinkedListNode prev = null;
        while (curr != null) {
            LinkedListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }

        return prev;
    }

    public static void printLLVals(LinkedListNode head) {
        LinkedListNode curr = head;
        while (curr != null) {
            System.out.println(curr.value);
            curr = curr.next;
        }
    }

    public static void main(String[] args) {
        // run your function through some test cases here
        // remember: debugging is half the battle!
        int[] input = {1, 2, 3, 4, 5};

        LinkedListNode head = null;
        LinkedListNode curr = null;

        for (int anInput : input) {
            LinkedListNode node = new LinkedListNode(anInput);
            if (head == null) {
                head = node;
            } else {
                curr.next = node;
            }
            curr = node;
        }

        printLLVals(head);
        System.out.println("Now reversing");
        printLLVals(reverse(head));
    }

    public static class LinkedListNode {

        public int value;
        public LinkedListNode next;

        public LinkedListNode(int value) {
            this.value = value;
        }
    }
}