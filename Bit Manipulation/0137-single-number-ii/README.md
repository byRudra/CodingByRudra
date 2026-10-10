# 137. Single Number II

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/single-number-ii/)

`Array` · `Bit Manipulation`

## Intuition  
If the array is ordered, equal numbers appear consecutively. Because every value except one occurs exactly three times, the unique element will be the only position where a value is **not** followed by an identical neighbor. Sorting therefore turns the “three‑times” property into a simple linear scan: we can jump over each triple in one step and stop as soon as the pattern breaks. The naïve way—counting frequencies with a hash map—needs O(n) extra space, while sorting costs O(n log n) time but only O(1) additional memory, satisfying the constant‑space requirement.

## Approach  
1. **Sort the array** – `Arrays.sort(nums)`. After this call, any value that appears three times occupies three consecutive indices.  
2. **Initialize a pointer** – `int index = 0`. This pointer will walk through the sorted array from left to right.  
3. **Loop while a pair exists** – `while (index < nums.length - 1)`. The condition guarantees that `index + 1` is a valid index for the comparison inside the loop.  
4. **Check the current pair** – `if (nums[index] == nums[index + 1])`.  
   * **If equal**, the current element belongs to a triple, so we skip the whole group: `index += 3`. The invariant after each iteration is that `index` points to the first element of the next unprocessed group.  
   * **If not equal**, we have found the element that does not have a duplicate immediately after it; this must be the single number, so we `return nums[index]`.  
5. **Handle the tail case** – If the loop finishes without returning, the unique element is the last array entry (possible when the single number is the maximum). The code returns `nums[nums.length - 1]`.  
   * Edge cases:  
     * **Length = 1** – the loop condition is false immediately, and step 5 returns the only element.  
     * **Even vs. odd length** – because every triple consumes three slots, the loop never overshoots; the `-1` guard prevents an out‑of‑bounds read when the array ends with a triple.  

## Dry Run  

Input: `nums = [2, 2, 3, 2]`  

| iteration | index | nums[index] | nums[index+1] | action / note                |
|-----------|-------|-------------|---------------|------------------------------|
| 1         | 0     | 2           | 2             | equal → `index += 3` (skip) |
| 2         | 3     | 2           | – (out of range) | loop condition fails        |
| exit      | –     | –           | –             | return `nums[3] = 2`? **No** – after sorting the array becomes `[2,2,2,3]`; redo with sorted view: |
| 1 (sorted) | 0   | 2           | 2             | equal → `index = 3`          |
| 2 (sorted) | 3   | 3           | –             | loop ends → return `nums[3]=3` |

After sorting, the scan skips the triple `2,2,2` and lands on the lone `3`, which is returned as the answer.

## Complexity  
- **Time:** O(n log n) – dominated by `Arrays.sort(nums)`, which sorts `n` elements; the subsequent linear scan runs at most n/3 iterations.  
- **Space:** O(1) – only a few primitive variables (`index`) are used; sorting is in‑place for primitive `int[]` in Java, so no extra allocation beyond the input array.

## Solution (Java)

```java
class Solution {
    public int singleNumber(int[] nums) {
        Arrays.sort(nums);
        int index = 0;
        while(index < nums.length - 1){
            if(nums[index] == nums[index + 1])
                index += 3;
            else
                return nums[index];
        }
        return nums[nums.length - 1];
    }
}
```

---

**Runtime** 8 ms (beats 12.5%) · **Memory** 45.8 MB (beats 31.4%)

<sub>Synced by AILeetHub on 2026-10-10.</sub>
