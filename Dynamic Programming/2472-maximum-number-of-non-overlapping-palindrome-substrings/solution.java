class Solution {
    public int maxPalindromes(String s, int k) {
        int n = s.length();
                int count = 0;
        int start = 0;

        while (start < n) {
            boolean found = false;

            // Earliest ending position
            for (int r = start + k - 1; r < n; r++) {

                // Try every possible starting point
                for (int l = start; l <= r - k + 1; l++) {

                    if (isPalindrome(s, l, r)) {
                        count++;
                        start = r + 1;
                        found = true;
                        break;
                    }
                }

                if (found) break;
            }

            if (!found) break;
        }

        return count;
    }

    private boolean isPalindrome(String s, int l, int r) {
        while (l < r) {
            if (s.charAt(l++) != s.charAt(r--))
                return false;
        }
        return true;
    }
}