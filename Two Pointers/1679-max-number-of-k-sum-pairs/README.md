# 1679. Max Number of K-Sum Pairs

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/max-number-of-k-sum-pairs/)

`Array` · `Hash Table` · `Two Pointers` · `Sorting`

## Intuition  
When the array is sorted, the smallest remaining element and the largest remaining element are the only candidates that can possibly form a valid pair. If their sum exceeds *k* we must discard the larger one; if it falls short we must discard the smaller one. This single observation eliminates the need for a hash map or a second pass, because each element is examined at most once while the two pointers converge. The pattern employed is the classic **two‑pointer** technique on a sorted list.

## Approach  
1. **Sort the input** `nums`. After this step the array is in non‑decreasing order, which guarantees that moving the left pointer rightward increases the left value and moving the right pointer leftward decreases the right value.  
2. **Initialize** `left = 0`, `right = nums.length - 1`, and `operations = 0`.  
3. **Loop while `left < right`**:  
   - Compute `sum = nums[left] + nums[right]`.  
   - **Invariant**: all indices `< left` and `> right` have already been paired or discarded, so only the sub‑array `[left … right]` can still contribute to new operations.  
   - If `sum == k` → a valid pair is found: increment `operations`, then advance both pointers (`left++`, `right--`) to remove the used elements.  
   - Else if `sum > k` → the pair is too large; decrement `right--` to try a smaller right‑hand value.  
   - Else (`sum < k`) → the pair is too small; increment `left++` to try a larger left‑hand value.  
4. **Terminate** when `left` meets or crosses `right`; at that point no further disjoint pairs exist.  
5. **Return** `operations`.  

Edge handling: an empty or single‑element array makes the loop condition false immediately, yielding `0`. The code consistently uses `left < right` (not `<=`) because a single element cannot pair with itself under the problem’s “remove two numbers” rule. The choice of `right--` on `sum > k` and `left++` on `sum < k` follows the monotonicity guaranteed by the sorted order.

## Dry Run  
Input: `nums = [1, 2, 3, 4]`, `k = 5`

| Iteration | left (value) | right (value) | sum | operations | Note |
|-----------|--------------|---------------|-----|------------|------|
| 1 | 0 (1) | 3 (4) | 5 | 1 | `sum == k` → pair (1,4), move both pointers |
| 2 | 1 (2) | 2 (3) | 5 | 2 | `sum == k` → pair (2,3), move both pointers |
| 3 | 2 | 1 | – | 2 | Loop exits because `left >= right` |

Final state: `operations = 2`, which is the maximum number of disjoint pairs summing to 5.

## Complexity  
- **Time:** **O(n log n)** – sorting dominates; the two‑pointer scan runs at most *n/2* iterations because each step moves at least one pointer.  
- **Space:** **O(1)** – only a few integer variables are used; the sort is in‑place (or uses the language’s standard O(log n) recursion stack, which is ignored for this bound).

## Solution (Java)

```java
// class Solution {
//     public int maxOperations(int[] nums, int k) {
//         HashMap<Integer, Integer> map = new HashMap<>();
//         int operations = 0;
//         for(int num : nums){
//             int compliment = k - num;
//             if(map.getOrDefault(compliment, 0) > 0){
//                 map.put(compliment, map.get(compliment) - 1);
//                 operations++;
//             }
//             else{
//                 map.put(num, map.getOrDefault(num, 0)  + 1);
//             }
//         } 
//         return operations;
//     }
// }

//  Better 
class Solution {
    public int maxOperations(int[] nums, int k) {
        Arrays.sort(nums);
        int operations = 0;
        int left = 0, right = nums.length - 1;
        while (left < right) {
            int sum = nums[left] + nums[right];
            if (sum == k) {
                operations++;
                left++;
                right--;
            } else if (sum > k) {
                right--;
            } else {
                left++;
            }
        }
        return operations;
    }
}
```

---

**Runtime** 22 ms (beats 99.4%) · **Memory** 69 MB (beats 79.9%)

<sub>Synced by AILeetHub on 2026-10-05.</sub>
