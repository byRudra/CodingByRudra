# 2265. Count Nodes Equal to Average of Subtree

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/count-nodes-equal-to-average-of-subtree/)

`Tree` · `Depth-First Search` · `Binary Tree`

## Intuition  
The key observation is that the average of a subtree can be obtained from two simple aggregates: the total sum of its node values and the number of nodes it contains. If we know these two numbers for every child, we can compute them for the parent in constant time. A naïve solution might first collect all node values, then for each node traverse its entire subtree to recompute sum and count, leading to O(n²) work. By maintaining the sum‑and‑count pair during a single post‑order depth‑first search, we eliminate the extra passes and any auxiliary hash map. This pattern is a classic **post‑order DFS** that propagates information upward.

## Approach  
1. **Base case** – If `root` is `null`, return `{0, 0}` (sum = 0, count = 0). This stops recursion at leaves and supplies neutral values for missing children.  
2. **Recurse left** – Call `dfs(root.left)` and store the result in `left[]`. After the call, `left[0]` holds the sum of the left subtree and `left[1]` its node count.  
3. **Recurse right** – Call `dfs(root.right)` and store the result in `right[]` with the same meaning for the right side.  
4. **Combine** – Compute the current subtree’s aggregates:  
   * `sum = root.val + left[0] + right[0]`  
   * `count = 1 + left[1] + right[1]`  
   The invariant here is that before the combination, `left[]` and `right[]` are correct for their respective subtrees.  
5. **Check average** – Because integer division in Java truncates toward zero, `sum / count` is exactly the floor of the true average. If `root.val == sum / count`, increment the global counter `ans`. This comparison works for all node values (including zero) and avoids floating‑point errors.  
6. **Return** – Propagate the pair `{sum, count}` upward with `return new int[]{sum, count}` so the parent can use it.  

**Edge‑case handling**:  
- The tree is guaranteed to have at least one node, but the `null` guard still protects against accidental empty inputs.  
- For a single‑node tree, `left` and `right` are both `{0,0}`, yielding `sum = root.val` and `count = 1`, so the node is always counted.  
- No overflow concerns arise because `Node.val ≤ 1000` and `n ≤ 1000`, so the maximum possible sum is `10⁶`, well within `int`.  

## Dry Run  

Input tree (level order): `[4,8,5,0,1,null,6]`

| Processed node | leftSum | leftCnt | rightSum | rightCnt | sum | cnt | ans after step | Note |
|----------------|--------|---------|----------|----------|-----|-----|----------------|------|
| 0 (leaf)       | 0      | 0       | 0        | 0        | 0   | 1   | 1 (0==0/1)    | leaf returns {0,1} |
| 1 (leaf)       | 0      | 0       | 0        | 0        | 1   | 1   | 2 (1==1/1)    | leaf returns {1,1} |
| 8 (internal)   | 0      | 1       | 1        | 1        | 9   | 3   | 2 (8≠9/3)     | sum=8+0+1, cnt=1+1+1 |
| 6 (leaf)       | 0      | 0       | 0        | 0        | 6   | 1   | 3 (6==6/1)    | leaf returns {6,1} |
| 5 (internal)   | 0      | 0       | 6        | 1        | 11  | 2   | 4 (5==11/2)   | 11/2=5 (floor) |
| 4 (root)       | 9      | 3       | 11       | 2        | 24  | 6   | 5 (4==24/6)   | 24/6=4 |

After processing the root, `ans = 5`, which matches the expected answer.

## Complexity  
- **Time:** O(n) – each node is visited exactly once; the recursion performs constant work per node (`sum`/`count` computation and a single comparison).  
- **Space:** O(h) – the call stack holds at most the height `h` of the tree (≤ n in the worst case of a degenerate tree). The returned `{sum, count}` pair is reused, so no extra containers are allocated beyond the recursion overhead.

## Solution (Java)

```java
/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode() {}
 *     TreeNode(int val) { this.val = val; }
 *     TreeNode(int val, TreeNode left, TreeNode right) {
 *         this.val = val;
 *         this.left = left;
 *         this.right = right;
 *     }
 * }
 */
class Solution {
    int ans = 0;
    public int averageOfSubtree(TreeNode root) {
        dfs(root);
        return ans;
    }
    private int[]dfs(TreeNode root){
        if(root == null) return new int[]{0 , 0};

        int left[] = dfs(root.left);
        int right[] = dfs(root.right);

        int sum = root.val + left[0] + right[0];
        int count  = 1 + left[1] + right[1];

        if(root.val == sum / count) ans++;

        return new int[]{sum, count};
    }
}
```

---

**Runtime** 0 ms (beats 100.0%) · **Memory** 45.5 MB (beats 63.4%)

<sub>Synced by AILeetHub on 2026-09-10.</sub>
