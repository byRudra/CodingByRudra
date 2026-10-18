class Solution {
    private static final long MOD = 1_000_000_007L;
    private int[] left, right;
    private long prod = 1;

    public int numOfWays(int[] nums) {
        int n = nums.length;
        left = new int[n + 1];   // values are 1..n, 0 means "no child"
        right = new int[n + 1];

        // Build the BST
        int root = nums[0];
        for (int i = 1; i < n; i++) {
            int cur = root, v = nums[i];
            while (true) {
                if (v < cur) {
                    if (left[cur] == 0) { left[cur] = v; break; }
                    cur = left[cur];
                } else {
                    if (right[cur] == 0) { right[cur] = v; break; }
                    cur = right[cur];
                }
            }
        }

        // prod = product of all subtree sizes
        size(root);

        long fact = 1;
        for (int i = 2; i <= n; i++) fact = fact * i % MOD;

        long ways = fact * power(prod, MOD - 2) % MOD;   // n! / prod via Fermat inverse
        return (int) ((ways - 1 + MOD) % MOD);
    }

    private int size(int node) {
        if (node == 0) return 0;
        int s = 1 + size(left[node]) + size(right[node]);
        prod = prod * s % MOD;
        return s;
    }

    private long power(long base, long exp) {
        long result = 1;
        base %= MOD;
        while (exp > 0) {
            if ((exp & 1) == 1) result = result * base % MOD;
            base = base * base % MOD;
            exp >>= 1;
        }
        return result;
    }
}