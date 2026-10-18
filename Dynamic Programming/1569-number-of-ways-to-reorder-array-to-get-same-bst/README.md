# 1569. Number of Ways to Reorder Array to Get Same BST

![Hard](https://img.shields.io/badge/Difficulty-Hard-ff375f?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/number-of-ways-to-reorder-array-to-get-same-bst/)

`Array` · `Math` · `Divide and Conquer` · `Dynamic Programming` · `Tree` · `Union-Find` · `Binary Search Tree` · `Memoization` · `Combinatorics` · `Binary Tree` · `Fermat's Little Theorem`

## Intuition  
When we insert the numbers of `nums` into a BST, every node’s left subtree must contain exactly the values smaller than the node and the right subtree the larger ones, regardless of insertion order. Therefore, once the tree shape is fixed, any valid permutation is just a shuffle of the left‑subtree elements, the right‑subtree elements, and the root (which must stay first). The number of shuffles equals  

\[
\frac{(\text{size of subtree})!}{\prod\text{(size of each child subtree)!}}
\]

for every node, and the total ways are the product of these fractions over all nodes. Computing each subtree size once gives us a product `prod = ∏ subtreeSize`. The obvious way would be to enumerate all permutations (exponential) or to keep a DP table of counts per node (still O(n²)). The insight that the count factorises into factorials lets us replace the whole DP with a single division `n! / prod`, which we evaluate modulo a prime using Fermat’s little theorem.

The pattern used is **divide‑and‑conquer on a BST with combinatorial counting**.

## Approach  
1. **Initialise helper arrays** `left` and `right` of length `n+1` (values are `1..n`, `0` means “no child”).  
2. **Build the BST** – start with `root = nums[0]`. For each subsequent value `v`  
   * set `cur = root`.  
   * while `true`  
     * if `v < cur` go left: if `left[cur] == 0` assign `left[cur] = v` and break; otherwise `cur = left[cur]`.  
     * else go right: analogous with `right`.  
   * Loop invariant: `cur` is always the node where the next comparison will be made; the subtree rooted at `cur` already respects BST ordering.  
   * Handles empty or single‑element input automatically because the loop body is never entered.  
3. **Compute subtree sizes and the product** `prod` by a post‑order recursion `size(node)`.  
   * Base case: `node == 0` returns `0`.  
   * Recursive step: `s = 1 + size(left[node]) + size(right[node])`.  
   * Update `prod = prod * s % MOD`.  
   * Invariant: after returning from a call, `prod` equals the product of sizes of all subtrees processed so far.  
4. **Pre‑compute `n!`** in a loop `fact = fact * i % MOD`.  
5. **Obtain the division** `n! / prod` modulo `MOD` by computing the modular inverse of `prod` with fast exponentiation (`power(prod, MOD‑2)`).  
6. **Subtract the original ordering** (which is not counted) and normalise: `(ways - 1 + MOD) % MOD`.  

Edge cases:  
* When `n = 1`, `prod = 1`, `fact = 1`, so `ways = 1` and the final answer becomes `0`.  
* The code uses `<=` in the exponent loop (`exp > 0`) because we need to process the last set bit.  
* All arithmetic is performed with `long` to avoid overflow before the modulo.

## Dry Run  
Input: `nums = [2,1,3]`

| Call (node) | left[node] | right[node] | returned size `s` | prod after update | Change |
|-------------|------------|-------------|-------------------|-------------------|--------|
| size(2)     | 1          | 3           | –                 | –                 | entering node 2 |
| size(1)     | 0          | 0           | 1                 | 1                 | leaf 1 contributes 1 |
| size(3)     | 0          | 0           | 1                 | 1                 | leaf 3 contributes 1 |
| size(2)     | –          | –           | 1+1+1 = 3         | 3                 | subtree rooted at 2 contributes 3 |

After the recursion `prod = 3`. `fact = 3! = 6`.  
`ways = 6 * inv(3) mod MOD = 6 * 333333336 ≡ 2`.  
Final answer = `2‑1 = 1`, matching the expected result.

## Complexity  
- **Time:** O(n) – building the BST scans each element once, the post‑order size computation visits each node once, and the factorial/inverse loops are linear in `n`.  
- **Space:** O(n) – the two auxiliary arrays `left` and `right` store one entry per possible value; recursion depth is at most the height of the BST (≤ n), but no additional data structures are allocated.

## Solution (Java)

```java
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
```

---

**Runtime** 5 ms (beats 95.6%) · **Memory** 45.7 MB (beats 100.0%)

<sub>Synced by AILeetHub on 2026-10-18.</sub>
