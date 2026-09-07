class Solution {
    public int distinctSubseqII(String s) {
        final int MOD = 1_000_000_007;
        long count[] = new long[26];
        long total = 0;

        for(char ch : s.toCharArray()){
            int idx = ch - 'a';
            long newValue = (total + 1) % MOD;
            total = (total - count[idx] + newValue + MOD) % MOD;
            count[idx] = newValue;
        }

        return (int) total;
    }
}