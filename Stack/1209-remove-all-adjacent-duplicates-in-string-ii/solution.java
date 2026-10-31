class Solution {
    public String removeDuplicates(String s, int k) {
        Stack<int[]> stack = new Stack<>();
        // this will contain the char and the freq we have seen till now 
        for (char current : s.toCharArray()) {
            if (stack.isEmpty()) {
                stack.push(new int[] { current, 1 });
            } else {
                int[] last = stack.pop();
                if (last[0] == current) {
                    if (last[1] + 1 != k) {
                        stack.push(new int[] { current, last[1] + 1 });
                    }
                } else {
                    stack.push(last);
                    stack.push(new int[] { current, 1 });
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int[] curr : stack) {
            for (int i = 1; i <= curr[1]; i++) {
                sb.append((char) curr[0]);
            }
        }
        return sb.toString();
    }
}