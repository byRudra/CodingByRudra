class Solution {
    public int minOperations(String s) {
        int n = s.length();

        // Already sorted?
        boolean sorted = true;
        for (int i = 1; i < n; i++) {
            if (s.charAt(i) < s.charAt(i - 1)) {
                sorted = false;
                break;
            }
        }
        if (sorted) return 0;

        // Length 2 and unsorted: only single-char substrings are allowed
        if (n == 2) return -1;

        char mn = 'z', mx = 'a';
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            if (c < mn) mn = c;
            if (c > mx) mx = c;
        }

        // One pass: min already in front, or max already at the end
        if (s.charAt(0) == mn || s.charAt(n - 1) == mx) return 1;

        // Check whether min or max appears in the middle (indices 1..n-2)
        boolean minInside = false, maxInside = false;
        for (int i = 1; i < n - 1; i++) {
            char c = s.charAt(i);
            if (c == mn) minInside = true;
            if (c == mx) maxInside = true;
        }

        // Neither extreme can be placed with a single move
        if (!minInside && !maxInside) return 3;

        return 2;
    }
}