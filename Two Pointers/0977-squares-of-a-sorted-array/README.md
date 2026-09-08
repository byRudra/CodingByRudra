# 977. Squares of a Sorted Array

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/squares-of-a-sorted-array/)

`Array` · `Two Pointers` · `Sorting`

## Intuition  
The key observation is that after squaring, the largest values must come from the elements farthest from zero: either the most negative number on the left or the most positive number on the right. If we compare the squares at both ends, we can place the larger one at the end of the result array and shrink the corresponding side. This eliminates the need for a separate sorting pass, turning an O(n log n) approach into a single linear scan. The pattern used is the classic **two‑pointer** technique.

## Approach  
1. **Initialize pointers and output.**  
   ```java
   int[] ans = new int[nums.length];
   int left = 0;
   int right = nums.length - 1;
   ```  
   `left` starts at the smallest index, `right` at the largest. `ans` will be filled from the back.

2. **Iterate backwards over `ans`.**  
   ```java
   for (int i = nums.length - 1; i >= 0; i--) { … }
   ```  
   *Exit condition:* the loop stops when `i` becomes –1, i.e., after `nums.length` iterations.  
   *Invariant:* before each iteration, the sub‑array `ans[i+1 … end]` already contains the `nums.length‑i‑1` largest squares in correct order.

3. **Compute candidate squares.**  
   ```java
   int leftSquare = nums[left] * nums[left];
   int rightSquare = nums[right] * nums[right];
   ```  
   These are the only two values that can become the next largest square because any interior element is closer to zero and therefore yields a smaller square.

4. **Choose the larger square and move the corresponding pointer.**  
   - If `leftSquare > rightSquare`, assign `ans[i] = leftSquare` and increment `left`.  
   - Otherwise assign `ans[i] = rightSquare` and decrement `right`.  
   The `>` (rather than `>=`) ensures that when the squares are equal we prefer the right side, which keeps the algorithm stable for duplicate values.

5. **Return the filled array.**  
   After the loop, `ans` holds all squares in non‑decreasing order.

Edge cases such as a single‑element array are handled automatically: the loop runs once, compares the same element on both sides, and places its square into `ans[0]`. No extra bounds checks are needed because the pointers move strictly inward and the loop count guarantees they never cross before the array is fully populated.

## Dry Run  

Input: `[-4, -1, 0, 3, 10]`

| i | left | right | leftSquare | rightSquare | ans[i] | note |
|---|------|-------|------------|-------------|--------|------|
| 4 | 0    | 4     | 16         | 100         | 100    | rightSquare larger → place at ans[4], right-- |
| 3 | 0    | 3     | 16         | 9           | 16     | leftSquare larger → place at ans[3], left++ |
| 2 | 1    | 3     | 1          | 9           | 9      | rightSquare larger → place at ans[2], right-- |
| 1 | 1    | 2     | 1          | 0           | 1      | leftSquare larger → place at ans[1], left++ |
| 0 | 2    | 2     | 0          | 0           | 0      | equal → choose right side, place at ans[0] |

Final `ans = [0, 1, 9, 16, 100]`, which is the correctly sorted list of squares.

## Complexity  
- **Time:** O(n) – the for‑loop runs exactly `n` times, and each iteration performs O(1) work (two multiplications and a constant‑time comparison).  
- **Space:** O(n) – the algorithm allocates a new array `ans` of size `n`; all other variables use O(1) extra space. (The input array is not modified.)

## Solution (Java)

```java
// O(n logn)
// class Solution {
//     public int[] sortedSquares(int[] nums) {
//         for(int i = 0; i < nums.length; i++){
//             nums[i] *= nums[i];
//         }
//         Arrays.sort(nums);
//         return nums;
//     }
// }

// O(n)
class Solution {
    public int[] sortedSquares(int[] nums) {
        int ans[] = new int[nums.length];

        int left = 0;
        int right = nums.length - 1;
        for(int i = nums.length - 1; i >= 0; i--){
            int leftSquare = nums[left] * nums[left];
            int rightSquare = nums[right] * nums[right];

            if(leftSquare > rightSquare){
                ans[i] = leftSquare;
                left++;
            }
            else{
                ans[i] = rightSquare;
                right--;
            }
        }
        return ans;
    }
}
```

---

**Runtime** 1 ms (beats 100.0%) · **Memory** 47.3 MB (beats 63.7%)

<sub>Synced by AILeetHub on 2026-09-08.</sub>
