class Solution {
    private Set<String> result = new HashSet<>();

    public List<String> removeInvalidParentheses(String s) {
        // Find min no of removal required
        int right = 0, left = 0;
        for (char ch : s.toCharArray()) {
            if (ch == '(')
                left++;
            else if (ch == ')'){
                if (left > 0)
                    left--;
                else
                    right++;
            }
        }

        // now we have the min no so lets backTrack

        dfs(s, 0, 0, left, right, new StringBuilder());
        return new ArrayList<>(result);
    }

    public void dfs(
            String s,
            int index,
            int balance,
            int removeLeft,
            int removeRight,
            StringBuilder current) {
        // Early pruning 
        if (balance < 0)
            return;

        // now check if the current is the one we need
        if (index == s.length()) { // checks if we have gone through the whole string or not
            if (balance == 0 && removeLeft == 0 && removeRight == 0) 
                result.add(current.toString());
            return;
        }

        char ch = s.charAt(index);
        if (ch != '(' && ch != ')') {
            // means it is a character so we include it any way and move forward
            current.append(ch);

            dfs(s, index + 1, balance, removeLeft, removeRight, current);

            // Now we remove this char and backtrack again
            current.deleteCharAt(current.length() - 1);

            return;
        }
        // Removing '(' OR ')'
        if (ch == '(' && removeLeft > 0) {
            dfs(
                    s,
                    index + 1,
                    balance,
                    removeLeft - 1,
                    removeRight,
                    current);
        }
        if (ch == ')' && removeRight > 0) {
            dfs(
                    s,
                    index + 1,
                    balance,
                    removeLeft,
                    removeRight - 1,
                    current);
        }

        // Now we take these as RemoveLeft OR RemoveRight is = 0
        // Hii Best Of Luck For Zomato
        current.append(ch);

        if (ch == '(') {
            dfs(
                    s,
                    index + 1,
                    balance + 1,
                    removeLeft,
                    removeRight,
                    current);
        } else {
            if (balance > 0) {
                dfs(
                        s,
                        index + 1,
                        balance - 1,
                        removeLeft,
                        removeRight,
                        current);
            }
        }

        // BackTrack again
        current.deleteCharAt(current.length() - 1);

    }
}