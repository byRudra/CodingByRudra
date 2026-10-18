class Solution {
    public boolean backspaceCompare(String s, String t) {
        StringBuilder res1 = new StringBuilder();
        StringBuilder res2 = new StringBuilder();

        for (char ch : s.toCharArray()) {
            if (ch == '#') {
                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
            } else {
                res1.append(ch);
            }
        }

        for (char ch : t.toCharArray()) {
            if (ch == '#') {
                if (res2.length() > 0) {
                    res2.deleteCharAt(res2.length() - 1);
                }
            } else {
                res2.append(ch);
            }
        }
        return res1.toString().equals(res2.toString());

    }
}