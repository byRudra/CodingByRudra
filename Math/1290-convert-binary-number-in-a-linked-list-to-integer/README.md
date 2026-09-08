# 1290. Convert Binary Number in a Linked List to Integer

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/convert-binary-number-in-a-linked-list-to-integer/)

`Linked List` · `Math`

## Intuition  
When a binary number is read from most‑significant to least‑significant bit, each new bit is equivalent to shifting the current value left by one position (multiply by 2) and then adding the bit’s value. The naïve way would be to first collect all bits, reverse them, or use a power‑of‑two lookup, which would require extra passes or storage. Recognizing that the running total can be updated on‑the‑fly eliminates the need for any auxiliary data structures. This is a classic **two‑state (accumulator) traversal** of a linked list.

## Approach  
1. **Initialize** `int ans = 0`. This holds the decimal value built so far.  
2. **Iterate** while `head != null`:  
   - **Invariant** before each iteration: `ans` equals the decimal value of the prefix of bits already visited.  
   - **Shift left**: `ans *= 2;` – equivalent to moving all previously processed bits one position toward higher significance.  
   - **Add current bit**: `ans += head.val;` – incorporates the current node’s binary digit (0 or 1).  
   - **Advance**: `head = head.next;` – move to the next node.  
   - **Exit condition** is reaching a `null` reference; at that point every node has contributed exactly once.  
3. **Return** `ans` as the final decimal representation.

**Edge‑case handling**  
- The list is guaranteed non‑empty, so the loop runs at least once.  
- A single node containing `0` yields `ans = 0` after the first iteration (`0 * 2 + 0`).  
- No overflow concerns for the given constraint (≤ 30 bits) because the maximum value fits in a 32‑bit signed integer.  
- The code deliberately uses `*= 2` and `+=` rather than bitwise shifts to stay clear and avoid sign‑extension pitfalls; both are equivalent for non‑negative values.

## Dry Run  

Input: `head = [1,0,1,1]`  

| Iteration | head.val | ans before | ans after (`ans*2 + val`) | head moves to |
|-----------|----------|------------|---------------------------|----------------|
| 1         | 1        | 0          | 1                         | node 2 (`0`)   |
| 2         | 0        | 1          | 2                         | node 3 (`1`)   |
| 3         | 1        | 2          | 5                         | node 4 (`1`)   |
| 4         | 1        | 5          | 11                        | null           |

After the fourth iteration `head` becomes `null`, the loop stops, and `ans = 11`, which is the decimal value of binary 1011.

## Complexity  
- **Time:** O(n) – the while‑loop visits each of the `n` nodes exactly once, performing only constant‑time arithmetic per iteration.  
- **Space:** O(1) – only two integer variables (`ans` and the loop pointer `head`) are used; the output integer is not counted as extra space.

## Solution (Java)

```java
/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
class Solution {
    public int getDecimalValue(ListNode head) {
        int ans = 0;
        while(head != null){
            ans *= 2;
            ans += head.val;
            head = head.next;
        }
        return ans;
    }
}
```

---

**Runtime** 0 ms (beats 100.0%) · **Memory** 43 MB (beats 17.9%)

<sub>Synced by AILeetHub on 2026-09-08.</sub>
