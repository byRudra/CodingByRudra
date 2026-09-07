class Solution {
    public int distinctSubseqII(String s) {
        final int MOD = 1_000_000_007;
        long[] count = new long[26];
        long total = 0;

        for (char ch : s.toCharArray()) {
            int idx = ch - 'a';
            long newVal = (total + 1) % MOD;
            total = (total - count[idx] + newVal + MOD) % MOD;  // +MOD guards against negative
            count[idx] = newVal;
        }

        return (int) total;
    }
}