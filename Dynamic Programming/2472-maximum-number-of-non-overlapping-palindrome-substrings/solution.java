class Solution {
    public int maxPalindromes(String s, int k) {
        int n = s.length();
        int ans = 0;
        int i = 0;

       while (i + k <= n) {
            boolean found = false;

            // Check length k
            if (isPalindrome(s, i, i + k - 1)) {
                ans++;
                i += k;
                found = true;
            }
            // Check length k + 1
            else if (i + k + 1 <= n &&
                     isPalindrome(s, i, i + k)) {
                ans++;
                i += k + 1;
                found = true;
            }

            if (!found) {
                i++;
            }
        }

        return ans;
    }

    private boolean isPalindrome(String s, int l, int r) {
        while (l < r) {
            if (s.charAt(l++) != s.charAt(r--))
                return false;
        }
        return true;
    }
}