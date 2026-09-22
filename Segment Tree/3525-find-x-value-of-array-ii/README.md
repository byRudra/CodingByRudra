# 3525. Find X Value of Array II

![Hard](https://img.shields.io/badge/Difficulty-Hard-ff375f?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/find-x-value-of-array-ii/)

`Array` · `Math` · `Segment Tree`

## Intuition  
The product of any suffix can be expressed as the product of the whole remaining array multiplied by the product of a prefix inside it. If we know, for every segment, the product modulo k and how many prefixes of that segment yield each remainder, we can combine two adjacent segments in O(k) time. A naïve solution would recompute products for each query, costing O(n) per query, which is too slow for 10⁵ elements and 2·10⁴ queries. Storing the needed information in a segment tree and merging children on‑the‑fly gives the answer in logarithmic time – a classic **segment‑tree** pattern.

## Approach  
1. **Build the tree** (`build(node,l,r,nums)`).  
   - Base case `l==r`: store `prod[node] = nums[l] % k` and set `cnt[node][prod[node]] = 1`.  
   - Recursive case: build left and right children, then `pull(node)` to combine them.  
   - Invariant: after `pull`, `prod[node]` equals the product of the whole interval `[l,r]` modulo k, and `cnt[node][r]` counts prefixes of `[l,r]` whose product ≡ r (mod k).  

2. **Combine children** (`pull(node)`).  
   - `prod[node] = (prod[left] * prod[right]) % k`.  
   - Copy left’s prefix counts unchanged (they stay prefixes wholly inside the left part).  
   - For each remainder `r` from the right child, a prefix that extends into the right has remainder `(prod[left] * r) % k`; add its count to `cnt[node][newRem]`.  
   - This respects the invariant that `cnt[node]` now represents all prefixes of the merged interval.

3. **Point update** (`update(node,l,r,index,value)`).  
   - Descend to the leaf where `l==r==index`. Replace `prod[node]` with `value % k` and reset its `cnt` array, then set the count for the new remainder to 1.  
   - While unwinding, call `pull(node)` to refresh ancestors.  
   - Handles empty or single‑element intervals automatically; the tree size guarantees no overflow.

4. **Range query** (`query(node,l,r,ql,qr)`).  
   - If the current node is completely inside `[ql,qr]`, return a copy of its `prod` and `cnt`.  
   - If the query lies entirely in one child, recurse there.  
   - Otherwise, query both children, then `merge(left,right)`.  
   - `merge` repeats the logic of `pull` but works on two independent `Node` objects, preserving the same invariant for the combined segment.

5. **Answer extraction**.  
   - After updating, query the segment `[start, n‑1]`. The returned node’s `cnt[x]` is exactly the number of suffixes whose product leaves remainder x, because each suffix corresponds to a prefix of this queried segment.

## Dry Run  
Input: `nums=[1,2,3]`, `k=3`, single query `index=1, value=5, start=0, x=2`.

| step | node interval | prod | cnt[0] cnt[1] cnt[2] | changed |
|------|---------------|------|----------------------|---------|
| 1 | build leaf 0 `[0,0]` | 1%3=1 | [0,1,0] | init |
| 2 | build leaf 1 `[1,1]` | 2%3=2 | [0,0,1] | init |
| 3 | build leaf 2 `[2,2]` | 3%3=0 | [1,0,0] | init |
| 4 | pull node `[0,1]` | (1*2)%3=2 | left cnt + shifted right: [0,1,1] | combine |
| 5 | pull root `[0,2]` | (2*0)%3=0 | [1,1,1] | combine |
| 6 | update index 1 to 5 → 5%3=2 | leaf `[1,1]` cnt becomes [0,0,1] | leaf changed |
| 7 | pull node `[0,1]` again → prod= (1*2)%3=2, cnt=[0,1,1] | recompute |
| 8 | pull root again → prod= (2*0)%3=0, cnt=[1,1,1] | final tree |

Query `[0,2]` returns `cnt[2]=1`, meaning one suffix (the whole array) gives remainder 2, which matches the expected answer.

## Complexity  
- **Time:** O((n + q) · log n · k) → building is O(n log n · k), each point update and range query traverses O(log n) nodes and merges O(k) remainders per node.  
- **Space:** O(n · k) for the segment‑tree arrays (`prod` and `cnt`). The recursion stack adds O(log n) extra space, which is excluded from the output size.

## Solution (Java)

```java
class Solution {

    int n, k;
    int[][] cnt;
    int[] prod;

    public int[] resultArray(int[] nums, int k, int[][] queries) {
        this.n = nums.length;
        this.k = k;

        cnt = new int[4 * n][k];
        prod = new int[4 * n];

        build(1, 0, n - 1, nums);

        int[] ans = new int[queries.length];

        for (int i = 0; i < queries.length; i++) {

            int index = queries[i][0];
            int value = queries[i][1];
            int start = queries[i][2];
            int x = queries[i][3];

            // Persistent update
            update(1, 0, n - 1, index, value);

            // Query nums[start ... n-1]
            Node res = query(1, 0, n - 1, start, n - 1);

            ans[i] = res.cnt[x];
        }

        return ans;
    }

    // Stores information about a segment
    class Node {
        int product;
        int[] cnt;

        Node(int product, int[] cnt) {
            this.product = product;
            this.cnt = cnt;
        }
    }

    void build(int node, int l, int r, int[] nums) {

        if (l == r) {
            prod[node] = nums[l] % k;
            cnt[node][prod[node]] = 1;
            return;
        }

        int mid = l + (r - l) / 2;

        build(node * 2, l, mid, nums);
        build(node * 2 + 1, mid + 1, r, nums);

        pull(node);
    }

    void pull(int node) {

        int left = node * 2;
        int right = node * 2 + 1;

        prod[node] = (prod[left] * prod[right]) % k;

        // Prefixes completely inside left
        for (int r = 0; r < k; r++) {
            cnt[node][r] = cnt[left][r];
        }

        // Prefixes that extend into right
        for (int r = 0; r < k; r++) {

            int newRem = (prod[left] * r) % k;

            cnt[node][newRem] += cnt[right][r];
        }
    }

    void update(int node, int l, int r, int index, int value) {

        if (l == r) {

            prod[node] = value % k;

            for (int i = 0; i < k; i++) {
                cnt[node][i] = 0;
            }

            cnt[node][prod[node]] = 1;

            return;
        }

        int mid = l + (r - l) / 2;

        if (index <= mid) {
            update(node * 2, l, mid, index, value);
        } else {
            update(node * 2 + 1, mid + 1, r, index, value);
        }

        pull(node);
    }

    Node query(int node, int l, int r, int ql, int qr) {

        if (ql <= l && r <= qr) {

            int[] copy = new int[k];

            for (int i = 0; i < k; i++) {
                copy[i] = cnt[node][i];
            }

            return new Node(prod[node], copy);
        }

        int mid = l + (r - l) / 2;

        if (qr <= mid) {
            return query(node * 2, l, mid, ql, qr);
        }

        if (ql > mid) {
            return query(node * 2 + 1, mid + 1, r, ql, qr);
        }

        Node left = query(node * 2, l, mid, ql, qr);
        Node right = query(node * 2 + 1, mid + 1, r, ql, qr);

        return merge(left, right);
    }

    Node merge(Node left, Node right) {

        int[] resCnt = new int[k];

        // Prefixes entirely in left
        for (int r = 0; r < k; r++) {
            resCnt[r] += left.cnt[r];
        }

        // Prefixes extending from left into right
        for (int r = 0; r < k; r++) {

            int rem = (left.product * r) % k;

            resCnt[rem] += right.cnt[r];
        }

        int resProduct = (left.product * right.product) % k;

        return new Node(resProduct, resCnt);
    }
}
```

---

**Runtime** 249 ms (beats 42.1%) · **Memory** 196 MB (beats 84.2%)

<sub>Synced by AILeetHub on 2026-09-22.</sub>
