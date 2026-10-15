# 2913. Subarrays Distinct Element Sum of Squares I

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/subarrays-distinct-element-sum-of-squares-i/)

`Array` · `Hash Table` · `Binary Indexed Tree` · `Segment Tree`

## Intuition  
The distinct count of a subarray grows only when a value that has never appeared in the current window is seen. If we fix the left endpoint and slide the right endpoint one step at a time, we can keep a running count of how many new values have been introduced. The naïve way would rebuild a set for every subarray, costing O(n³) overall, but the observation that the count is monotonic for a fixed left removes the need for a fresh set each time. This leads to a classic **two‑nested‑loops with a constant‑size frequency bitmap** pattern.

## Approach  
1. Initialise `result = 0` and `n = nums.size()`.  
2. Outer loop: `for (int i = 0; i < n; i++)` – chooses the left boundary of the subarray.  
   * Before the inner loop we reset `count = 0` and create `boolean[] visited = new boolean[101]`. The array size matches the problem’s value bound, giving O(1) extra memory.  
3. Inner loop: `for (int j = i; j < n; j++)` – expands the right boundary one element at a time.  
   * Invariant: `visited[x]` is true iff value `x` has appeared in `nums[i..j]`; `count` equals the number of true entries, i.e., the distinct count of the current window.  
   * Retrieve `int num = nums.get(j)`.  
   * If `!visited[num]` then `visited[num] = true` and `count++`. This is the only place `count` can change, guaranteeing monotonic growth.  
   * Add the square of the current distinct count: `result += count * count`.  
4. After the inner loop finishes, the next iteration of the outer loop starts a fresh window, so the bitmap is cleared again.  
5. Return `result` after all `(i, j)` pairs have been processed.

Edge handling:  
* When `n == 1` the outer loop runs once, the inner loop runs once, `count` becomes 1 and `result` receives 1², which is correct.  
* The bitmap size of 101 safely covers the maximal element value (100); using `<= 100` would be off‑by‑one, so the code allocates `new boolean[101]`.  
* No overflow guard is needed because the maximum possible sum fits in a 32‑bit signed integer for the given constraints (n ≤ 100, distinct count ≤ 100).

## Dry Run  

**Input:** `nums = [1, 2, 1]`

| i (left) | j (right) | num | visited[1] | visited[2] | count | result (cumulative) | change |
|----------|-----------|-----|------------|------------|-------|----------------------|--------|
| 0        | 0         | 1   | true       | false      | 1     | 1                    | count ↑, add 1² |
| 0        | 1         | 2   | true       | true       | 2     | 5 (=1+4)             | count ↑, add 2² |
| 0        | 2         | 1   | true       | true       | 2     | 9 (=5+4)             | count unchanged, add 2² |
| 1        | 1         | 2   | false→true| false      | 1     | 10 (=9+1)            | new bitmap, count ↑ |
| 1        | 2         | 1   | true       | true       | 2     | 14 (=10+4)           | count ↑, add 2² |
| 2        | 2         | 1   | true       | false      | 1     | 15 (=14+1)           | new bitmap, count ↑ |

After processing all windows, `result = 15`, which matches the required sum of squares.

## Complexity  
- **Time:** O(n²) – the outer loop runs *n* times and the inner loop runs at most *n – i* times, giving a triangular series of ≈ n·(n+1)/2 iterations; each iteration does O(1) work (bitmap check, possible update, arithmetic).  
- **Space:** O(1) – the only extra storage is the fixed‑size `boolean[101]` and a few scalar variables, independent of *n* (output integer excluded).

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
        int result = 0;
        int n = nums.size();

        for (int i = 0; i < n; i++) {
            int count = 0;
            boolean[] visited = new boolean[101];
            for (int j = i; j < n; j++) {
                int num = nums.get(j);
                if (!visited[num]) {
                    visited[num] = true;
                    count++;
                }
                result += count * count;
            }

        }

        return result;
    }
}
```

---

**Runtime** 3 ms (beats 99.6%) · **Memory** 44.9 MB (beats 99.2%)

<sub>Synced by AILeetHub on 2026-10-15.</sub>
