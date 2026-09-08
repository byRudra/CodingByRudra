class Solution {
    public String minRemoveToMakeValid(String s) {
        Stack<Integer> stack = new Stack<>();
        boolean[] remove = new boolean[s.length()];

        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);

            if (ch == '(') {
                stack.push(i);
            } else if (ch == ')') {
                if (!stack.isEmpty()) {
                    stack.pop();
                } else {
                    remove[i] = true;
                }
            }
        }

        // Remaining  (  in the stack
        while (!stack.isEmpty())
            remove[stack.pop()] = true;

        StringBuilder ans = new StringBuilder();

        for (int i = 0; i < s.length(); i++) {
            if (remove[i])
                continue;
            ans.append(s.charAt(i));
        }
        return ans.toString();
    }
}