# 445. Add Two Numbers II

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/add-two-numbers-ii/)

`Linked List` · `Math` · `Stack`

## Intuition  
If we could read the digits from right to left, the addition becomes the classic “add with carry” that proceeds from the least‑significant digit upward. Pushing every node value onto a stack automatically reverses the traversal order, so the top of each stack always holds the next digit to add. The naïve way would be to reverse the linked lists (or recurse to the end), which either mutates the input or uses extra recursion depth. By using two stacks we keep the original lists intact and avoid a second pass, achieving the same effect with a single linear scan and a simple loop.

## Approach  
1. **Fill the first stack** – While `l1 != null`, push `l1.val` onto `nums1` and advance `l1 = l1.next`.  
   *Exit condition*: `l1` becomes `null`.  
   *Invariant*: after each iteration `nums1` contains exactly the values of the processed prefix of `l1` in original order, so the top is the next digit from the right side.  

2. **Fill the second stack** – Analogous to step 1 for `l2` and `nums2`.  

3. **Initialize result construction** – Set `head = null` and `carry = 0`.  

4. **Main addition loop** – Continue while `!nums1.isEmpty() || !nums2.isEmpty() || carry != 0`.  
   - Compute `sum = carry`.  
   - If `!nums1.isEmpty()`, add `nums1.pop()` to `sum`.  
   - If `!nums2.isEmpty()`, add `nums2.pop()` to `sum`.  
   - Update `carry = sum / 10`.  
   - Create `node = new ListNode(sum % 10)`.  
   - Insert the new node at the front: `node.next = head; head = node;`.  
   *Invariant*: At the start of each iteration `carry` holds the overflow from the previous less‑significant addition, and the stacks contain exactly the remaining higher‑order digits.  

5. **Return** – After the loop finishes, `head` points to the most‑significant digit of the summed number, which is returned.

*Edge handling*: The loops work for single‑node lists because the stacks receive one element each; an empty stack is simply ignored. The condition `carry != 0` guarantees that a final extra digit (e.g., 999 + 1 = 1000) is emitted. Using `<=` versus `<` is irrelevant here because we explicitly test `isEmpty()`; the loop’s guard covers all three termination possibilities.

## Dry Run  
Input: `l1 = [7,2,4,3]`, `l2 = [5,6,4]`

| Iter | sum (before carry) | carry → new | node.val | head (as list) | Change |
|------|-------------------|------------|----------|----------------|--------|
| 1    | 0 + 3 + 4 = 7     | 0          | 7        | [7]            | popped 3,4 |
| 2    | 0 + 4 + 6 = 10    | 1          | 0        | [0,7]          | popped 4,6 |
| 3    | 1 + 2 + 5 = 8     | 0          | 8        | [8,0,7]        | popped 2,5 |
| 4    | 0 + 7 = 7         | 0          | 7        | [7,8,0,7]      | popped 7, nums2 empty |

The loop ends because both stacks are empty and `carry` is 0. `head` now represents `[7,8,0,7]`, the correct sum.

## Complexity  
- **Time:** `O(n + m)` – each node of `l1` and `l2` is visited once to fill the stacks, and each stack element is popped exactly once in the addition loop.  
- **Space:** `O(n + m)` – the two stacks store all digits of the input lists; the output list itself is not counted against the auxiliary space.

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
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        Stack<Integer> nums1 = new Stack<>();
        Stack<Integer> nums2 = new Stack<>();

         while (l1 != null) {
            nums1.push(l1.val);
            l1 = l1.next;
        }

        while (l2 != null) {
            nums2.push(l2.val);
            l2 = l2.next;
        }

        ListNode head = null;
        int carry = 0;

        while(!nums1.isEmpty() || !nums2.isEmpty() || carry != 0){
            int sum = carry;

            if(!nums1.isEmpty()){
                sum += nums1.pop();
            }

            if(!nums2.isEmpty()){
                sum += nums2.pop();
            }

            carry = sum / 10;

            ListNode node = new ListNode(sum % 10);
            node.next = head;
            head = node; 
        }
        return head;

    }
}
```

---

**Runtime** 5 ms (beats 8.5%) · **Memory** 46.3 MB (beats 84.7%)

<sub>Synced by AILeetHub on 2026-10-23.</sub>
