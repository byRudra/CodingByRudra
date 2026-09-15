class Solution {
    public int maxPalindromes(String s, int k) {
        int count = 0;
        int start = 0;
        int length = s.length();

        while (start + k <= length) {
            if (isPalindrome(s, start, start + k - 1)) {
                start += k;
                count++;
            } else if (start + k + 1 <= length && isPalindrome(s, start, start + k)) {
                start += k + 1;
                count++;
            } else {
                start++;
            }
        }
        return count;
    }

    private boolean isPalindrome(String s, int left, int right) {
        while (left < right) {
            if (s.charAt(left) != s.charAt(right))
                return false;

            left++;
            right--;
        }
        return true;
    }
}