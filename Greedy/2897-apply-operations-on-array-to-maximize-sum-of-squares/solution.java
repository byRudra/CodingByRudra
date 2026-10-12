import java.util.List;

class Solution {
    public int maxSum(List<Integer> nums, int k) {
        final long MOD = 1_000_000_007L;
        final int BITS = 31;

        // cnt[p] = how many numbers have the 2^p place set (counted with / and %)
        int[] cnt = new int[BITS];
        for (int num : nums) {
            int x = num;
            int p = 0;
            while (x > 0) {
                if (x % 2 == 1) {
                    cnt[p]++;
                }
                x /= 2;
                p++;
            }
        }

        long[] weight = new long[BITS];
        weight[0] = 1;
        for (int p = 1; p < BITS; p++) {
            weight[p] = weight[p - 1] * 2;
        }

        // Greedily build the k largest numbers by pooling set bits together.
        long ans = 0;
        for (int i = 0; i < k; i++) {
            long val = 0;
            for (int p = 0; p < BITS; p++) {
                if (cnt[p] > 0) {
                    cnt[p]--;
                    val += weight[p];
                }
            }
            if (val == 0) {
                break;
            }
            ans = (ans + (val * val) % MOD) % MOD;
        }
        return (int) ans;
    }
}