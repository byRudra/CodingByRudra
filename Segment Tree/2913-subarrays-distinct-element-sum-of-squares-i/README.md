# 2913. Subarrays Distinct Element Sum of Squares I

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/subarrays-distinct-element-sum-of-squares-i/)

`Array` · `Hash Table` · `Binary Indexed Tree` · `Segment Tree`

## Intuition  
When we extend a subarray one step to the right, the only thing that can change is whether the newly added element has been seen before. Therefore the distinct‑count of `nums[left..right]` can be obtained from the distinct‑count of `nums[left..right‑1]` by inserting `nums[right]` into a set that already holds all previous elements. The naïve way would recompute a set from scratch for every subarray, costing O(n³). By keeping a single `HashSet` that grows as `right` moves, we eliminate the inner recomputation and reduce the work to a single pass per starting index. This is the classic “two‑pointer with a hash set” pattern, except the left pointer is reset after each outer iteration.

## Approach  
1. **Initialize** `total = 0`.  
2. **Outer loop** `left` from 0 to `nums.size()‑1`. The loop stops when `left == nums.size()`.  
   - Invariant: before the inner loop starts, no element from the current left position has been processed.  
3. **Create** an empty `HashSet<Integer> seen`. This set will always contain exactly the distinct values of the current subarray `nums[left..right]`.  
4. **Inner loop** `right` from `left` to `nums.size()‑1`. The loop exits when `right` reaches the end of the array.  
   - Invariant each iteration: `seen` equals the set of distinct numbers in `nums[left..right]`.  
5. **Update set** with `seen.add(nums.get(right))`. If `nums[right]` is new, the set size grows; otherwise it stays the same.  
6. **Accumulate** `total += seen.size() * seen.size()`. The square of the current distinct count is added to the answer.  
7. After the inner loop finishes, the outer loop increments `left`, discarding the old `seen` and starting a fresh set for the next starting index.  
8. **Return** `total` after all left positions have been processed.

Edge considerations: the constraints guarantee at least one element, so the loops always execute at least once. For a single‑element array the inner loop runs once, `seen.size()` is 1, and `total` becomes 1. Duplicate values never increase `seen.size()`, which correctly keeps the distinct count unchanged. The `HashSet` choice avoids overflow and gives average O(1) insertion.

## Dry Run  

**Input**: `nums = [1, 2, 1]`

| left | right | seen (as set) | seen.size() | total (after addition) | note |
|------|-------|---------------|-------------|------------------------|------|
| 0    | 0     | {1}           | 1           | 1                      | first subarray `[1]` |
| 0    | 1     | {1,2}         | 2           | 1 + 4 = 5              | `[1,2]` adds 2² |
| 0    | 2     | {1,2}         | 2           | 5 + 4 = 9              | `[1,2,1]` distinct unchanged |
| 1    | 1     | {2}           | 1           | 9 + 1 = 10             | new left, reset set |
| 1    | 2     | {1,2}         | 2           | 10 + 4 = 14            | `[2,1]` |
| 2    | 2     | {1}           | 1           | 14 + 1 = 15            | last subarray `[1]` |

After processing all iterations `total = 15`, which matches the required sum of squares.

## Complexity  
- **Time:** **O(n²)** – the outer loop runs *n* times and the inner loop runs *n‑left* times; the total number of iterations is `n·(n+1)/2`. Each iteration performs O(1) work (`HashSet.add` and arithmetic).  
- **Space:** **O(k)** where *k* ≤ *n* is the number of distinct elements in the current window. The set is recreated for each `left`, so the extra memory never exceeds the size of the whole array, and no additional structures are kept beyond the loop.

## Solution (Java)

```java
// class Solution {
//     public int sumCounts(List<Integer> nums) {
//         List<List<Integer>> subArrays = findSubarrays(nums);
//         List<Integer> uniqueList = uniqueValues(subArrays);
//         int result = squareOfNums(uniqueList);
//         return result;

//     }
//     public int squareOfNums(List<Integer> uniqueList){
//         int result = 0;
//         for(int num : uniqueList){
//             result += num * num;
//         }
//         return result;
//     }
//     public List uniqueValues(List<List<Integer>> subArrays){
//         List<Integer> result = new ArrayList<>();
//         for(List<Integer> current : subArrays){
//             Set<Integer> set = new HashSet<>();
//             for(int num : current){
//                 set.add(num);
//             }
//             result.add(set.size());
//         }

//         return result;
//     }
//     public List findSubarrays(List<Integer> nums){
//         List<List<Integer>> result = new ArrayList<>();
//         int left = 0;
//         while(left < nums.size()){
//             List<Integer> candidate = new ArrayList<>();
//             for(int right = left; right < nums.size(); right++){
//                 candidate.add(nums.get(right));
//                 result.add(new ArrayList<>(candidate));
//             }
//             left++;
//         }
//         return result;
//     }
// }
class Solution {
    public int sumCounts(List<Integer> nums) {
        int total = 0;
        for (int left = 0; left < nums.size(); left++) {
            Set<Integer> seen = new HashSet<>();
            for (int right = left; right < nums.size(); right++) {
                seen.add(nums.get(right));
                total += seen.size() * seen.size();
            }
        }
        return total;
    }
}
```

---

**Runtime** 12 ms (beats 18.6%) · **Memory** 46.4 MB (beats 80.6%)

<sub>Synced by AILeetHub on 2026-10-15.</sub>
