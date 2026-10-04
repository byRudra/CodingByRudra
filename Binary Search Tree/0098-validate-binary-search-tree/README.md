# 98. Validate Binary Search Tree

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/validate-binary-search-tree/)

`Tree` · `Depth-First Search` · `Binary Search Tree` · `Binary Tree`

## Intuition  
The BST condition can be expressed as a range constraint: every node must lie strictly between a lower bound (all ancestors that are smaller) and an upper bound (all ancestors that are larger). If we propagate these bounds down the tree, each node’s value either satisfies the constraint or immediately disproves the tree’s validity. A naïve solution might perform an in‑order traversal and then check monotonicity, which requires an extra pass or auxiliary storage. By maintaining the permissible interval during a single depth‑first walk, we eliminate the need for any additional data structures or passes. This is the classic **range‑checking recursion** pattern.

## Approach  
1. Call `validate(root, Long.MIN_VALUE, Long.MAX_VALUE)`.  
2. **Base case:** If `root == null`, return `true` – an empty subtree is always valid.  
3. **Violation test:** If `root.val <= min` **or** `root.val >= max`, return `false`. This enforces the strict inequality required by a BST.  
4. Recurse on the **right** subtree with updated bounds: `validate(root.right, root.val, max)`. The current node becomes the new lower bound because every node in the right subtree must be greater than it.  
5. Recurse on the **left** subtree with updated bounds: `validate(root.left, min, root.val)`. The current node becomes the new upper bound because every node in the left subtree must be smaller than it.  
6. Return the logical **AND** of the two recursive calls; the whole tree is valid only if both subtrees are valid under their respective intervals.  

*Loop invariant:* At the start of each recursive call, `min` and `max` represent the exclusive interval that all nodes in the current subtree must occupy. The invariant holds because the parent call supplies the correct bounds based on its own value.  

*Edge‑case handling:*  
- The initial bounds use `Long.MIN_VALUE` / `Long.MAX_VALUE` to safely accommodate node values equal to `Integer.MIN_VALUE` or `Integer.MAX_VALUE` without overflow.  
- The strict comparisons (`<=` and `>=`) guarantee that duplicate values cause failure, matching the problem’s “strictly less/greater” rule.  
- The order of recursive calls (`right` then `left`) is arbitrary; the `&&` ensures both are evaluated, but short‑circuiting stops early on the first `false`, saving work.

## Dry Run  
Input tree: `[5,1,4,null,null,3,6]`  

| Call (node) | min                | max                | Action / Note                              |
|-------------|--------------------|--------------------|--------------------------------------------|
| (5)         | Long.MIN_VALUE     | Long.MAX_VALUE     | 5 ∈ (−∞,∞) → continue                       |
| (4) right   | 5                  | Long.MAX_VALUE     | 4 ≤ 5 → **false** (violates lower bound)   |
| (1) left    | Long.MIN_VALUE     | 5                  | 1 ∈ (−∞,5) → continue                       |
| (null)      | 1                  | 5                  | empty → true                               |
| (null)      | Long.MIN_VALUE     | 1                  | empty → true                               |

The recursion aborts when evaluating the right child of 5 because 4 is not greater than 5. The final result is `false`, which matches the expected answer.

## Complexity  
- **Time:** O(n) – each node is visited exactly once; the recursive call stack advances one level per node.  
- **Space:** O(h) – recursion depth equals the height `h` of the tree (worst‑case O(n) for a degenerate tree), not counting the output.

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
    public boolean isValidBST(TreeNode root) {
        return validate(root, Long.MIN_VALUE, Long.MAX_VALUE);

    }
    private boolean validate(TreeNode root, long min, long max){
        if(root == null) return true;
        if(root.val <= min  || root.val >= max) return false;
        return validate(root.right, root.val, max) && validate(root.left, min, root.val);
    }
}
```

---

**Runtime** 0 ms (beats 100.0%) · **Memory** 45 MB (beats 65.0%)

<sub>Synced by AILeetHub on 2026-10-04.</sub>
