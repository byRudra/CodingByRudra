class Solution {
    public int scoreOfParentheses(String s) {
        int depth = 0;
        int score = 0;

        for (int i = 0; i < s.length(); i++) {

            if (s.charAt(i) == '(') {
                depth++;
            } else {
                depth--;

                // Check if this ')' immediately closes '('
                if (s.charAt(i - 1) == '(') {
                    score += Math.pow(2, depth);
                }
            }
        }

        return score;
    }
}