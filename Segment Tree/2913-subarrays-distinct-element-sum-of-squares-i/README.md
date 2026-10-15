# 2913. Subarrays Distinct Element Sum of Squares I

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/subarrays-distinct-element-sum-of-squares-i/)

`Array` · `Hash Table` · `Binary Indexed Tree` · `Segment Tree`

## Intuition  
The distinct count of a subarray can be obtained by simply collecting its elements in a hash set – the set’s size is exactly the number of different values. Because the array length is at most 100, generating every possible contiguous segment (there are n·(n+1)/2 of them) and counting its uniques fits comfortably in the time limit. This observation eliminates the need for sophisticated data structures such as BITs or segment trees; a straightforward double loop plus a per‑subarray set is sufficient. The overall pattern is a brute‑force enumeration with a hash‑based deduplication step.

## Approach  
1. **Generate all subarrays** – Initialise `left = 0`. While `left < nums.size()`, create an empty `candidate`. For each `right` from `left` to `nums.size()‑1`, append `nums.get(right)` to `candidate` and store a *copy* of `candidate` in `result`. The copy (`new ArrayList<>(candidate)`) preserves the current window before it grows.  
   *Invariant*: At the start of each inner iteration, `candidate` holds the elements `nums[left..right‑1]`. The loop stops when `right` reaches the end of the array.  
2. **Compute distinct counts** – For every subarray `current` in `subArrays`, instantiate an empty `HashSet<Integer> set`. Insert each `num` of `current` into `set`; after the inner loop, `set.size()` equals the distinct count of that subarray. Append this size to `uniqueList`.  
   *Invariant*: After processing the `i`‑th element of `current`, `set` contains exactly the distinct values seen so far. The loop ends when all elements of `current` have been examined.  
3. **Square and accumulate** – Iterate over `uniqueList`. For each `num` (which is a distinct count), add `num * num` to `result`. Finally return `result`.  
   *Invariant*: After processing the first `k` entries of `uniqueList`, `result` equals the sum of squares of those `k` distinct counts.  

Edge handling:  
- An empty input never occurs because the constraints guarantee at least one element.  
- Single‑element arrays produce one subarray; the loops naturally handle `left == right == 0`.  
- The inner `right` loop uses `<=` on the index bound (`right < nums.size()`) to include the last element; using `<` would miss the final subarray ending at the array’s tail.  

## Dry Run  
**Input**: `[1, 2, 1]`

| left | right | candidate (after push) | setSize (after counting) | result (running sum) | note |
|------|-------|------------------------|--------------------------|----------------------|------|
| 0    | 0     | [1]                    | 1                        | 1                    | first subarray `[1]` |
| 0    | 1     | [1,2]                  | 2                        | 5 (=1+4)             | `[1,2]` distinct = 2 |
| 0    | 2     | [1,2,1]                | 2                        | 9 (=5+4)             | `[1,2,1]` distinct = 2 |
| 1    | 1     | [2]                    | 1                        | 10 (=9+1)            | new left, reset candidate |
| 1    | 2     | [2,1]                  | 2                        | 14 (=10+4)           | `[2,1]` distinct = 2 |
| 2    | 2     | [1]                    | 1                        | 15 (=14+1)           | last subarray `[1]` |

After the loops finish, `result = 15`, which matches the required sum of squares.

## Complexity  
- **Time:** **O(n³)** – The outer `while` and inner `for` together generate O(n²) subarrays; for each subarray we traverse its elements to fill a `HashSet`, costing up to O(n) in the worst case, yielding O(n³) overall.  
- **Space:** **O(n²)** – `subArrays` stores every subarray copy, totaling O(n²) elements; the auxiliary `HashSet` and `uniqueList` are O(n) each and do not dominate the bound. (The output integer itself is excluded from the space count.)

## Solution (Java)

```java
class Solution {
    public int sumCounts(List<Integer> nums) {
        List<List<Integer>> subArrays = findSubarrays(nums);
        List<Integer> uniqueList = uniqueValues(subArrays);
        int result = squareOfNums(uniqueList);
        return result;

    }
    public int squareOfNums(List<Integer> uniqueList){
        int result = 0;
        for(int num : uniqueList){
            result += num * num;
        }
        return result;
    }
    public List uniqueValues(List<List<Integer>> subArrays){
        List<Integer> result = new ArrayList<>();
        for(List<Integer> current : subArrays){
            Set<Integer> set = new HashSet<>();
            for(int num : current){
                set.add(num);
            }
            result.add(set.size());
        }

        return result;
    }
    public List findSubarrays(List<Integer> nums){
        List<List<Integer>> result = new ArrayList<>();
        int left = 0;
        while(left < nums.size()){
            List<Integer> candidate = new ArrayList<>();
            for(int right = left; right < nums.size(); right++){
                candidate.add(nums.get(right));
                result.add(new ArrayList<>(candidate));
            }
            left++;
        }
        return result;
    }
}
```

---

**Runtime** 164 ms (beats 5.7%) · **Memory** 53.6 MB (beats 11.4%)

<sub>Synced by AILeetHub on 2026-10-15.</sub>
