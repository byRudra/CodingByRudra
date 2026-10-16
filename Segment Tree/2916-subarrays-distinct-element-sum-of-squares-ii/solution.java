// import java.util.*;

// class Solution {
//     public int sumCounts(int[] nums) {
//         int MOD = 1_000_000_007;
//         int n = nums.length;
//         long ans = 0;

//         for (int i = 0; i < n; i++) {
//             Map<Integer, Integer> freq = new HashMap<>();
//             int distinct = 0;

//             for (int j = i; j < n; j++) {
//                 if (freq.getOrDefault(nums[j], 0) == 0) {
//                     distinct++;
//                 }

//                 freq.put(nums[j], freq.getOrDefault(nums[j], 0) + 1);

//                 ans = (ans + (long) distinct * distinct) % MOD;
//             }
//         }

//         return (int) ans;
//     }
// }

// BKB LEETCODE KI
import java.util.*;

class Solution {
    static final long MOD = 1_000_000_007L;
    long[] sum, sq, lazy;
    int n;

    public int sumCounts(int[] nums) {
        n = nums.length;
        sum = new long[4 * n];
        sq = new long[4 * n];
        lazy = new long[4 * n];

        Map<Integer, Integer> last = new HashMap<>();
        long ans = 0;

        for (int r = 0; r < n; r++) {
            int prev = last.getOrDefault(nums[r], -1);
            update(1, 0, n - 1, prev + 1, r, 1);
            ans = (ans + sq[1]) % MOD;
            last.put(nums[r], r);
        }
        return (int) ans;
    }

    void apply(int node, int len, long d) {
        sq[node] = (sq[node] + 2 * d % MOD * sum[node] + d * d % MOD * len) % MOD;
        sum[node] = (sum[node] + d * len) % MOD;
        lazy[node] = (lazy[node] + d) % MOD;
    }

    void push(int node, int l, int r) {
        if (lazy[node] != 0) {
            int mid = (l + r) / 2;
            apply(2 * node, mid - l + 1, lazy[node]);
            apply(2 * node + 1, r - mid, lazy[node]);
            lazy[node] = 0;
        }
    }

    void update(int node, int l, int r, int ql, int qr, long d) {
        if (qr < l || r < ql) return;
        if (ql <= l && r <= qr) {
            apply(node, r - l + 1, d);
            return;
        }
        push(node, l, r);
        int mid = (l + r) / 2;
        update(2 * node, l, mid, ql, qr, d);
        update(2 * node + 1, mid + 1, r, ql, qr, d);
        sum[node] = (sum[2 * node] + sum[2 * node + 1]) % MOD;
        sq[node] = (sq[2 * node] + sq[2 * node + 1]) % MOD;
    }
}