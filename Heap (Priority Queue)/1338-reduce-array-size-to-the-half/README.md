# 1338. Reduce Array Size to The Half

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/reduce-array-size-to-the-half/)

`Array` · `Hash Table` · `Greedy` · `Sorting` · `Heap (Priority Queue)`

## Intuition  
The key observation is that removing numbers with the highest occurrence first reduces the array size most quickly. If we know how many distinct values appear exactly *k* times, we can “greedily” take whole groups of size *k* until we have discarded at least half of the elements. A naïve solution would sort the whole array or use a priority queue, both costing *O(n log n)*. By counting frequencies and then counting how many values share each frequency (a bucket‑sort style histogram), we eliminate the need for any extra log factor and achieve a single linear pass.

## Approach  
1. **Count raw frequencies** – `int count[] = new int[100001];`  
   Iterate over `arr`; for each `num` do `count[num]++`. After this loop `count[v]` equals the number of occurrences of value `v`.  
2. **Build frequency‑of‑frequency buckets** – `int freq[] = new int[n + 1];`  
   Scan the `count` array; whenever `c > 0` increment `freq[c]`. Thus `freq[k]` tells how many distinct values appear exactly `k` times.  
3. **Greedy removal from largest bucket downwards** – initialize `removed = 0, size = 0`.  
   Loop `for (int f = n; f >= 1 && removed < n/2; f--)`.  
   *Invariant*: before each iteration, `removed` is the total number of elements already eliminated, and all frequencies larger than `f` have been exhausted.  
   Inside the loop, while `freq[f] > 0 && removed < n/2` we:  
   - `removed += f;` // we delete one whole value that occurs `f` times  
   - `size++;`    // we have added one distinct number to the answer set  
   - `freq[f]--;`  // one less value remains with frequency `f`  
   The inner `while` guarantees we keep taking groups of the current size `f` until either no such groups remain or we have already removed half of the array.  
4. **Return** `size`, the minimal number of distinct values needed.

Edge handling:  
- If `arr` is empty or has one element the outer loop never runs, but constraints guarantee `arr.length ≥ 2`.  
- The loop uses `>= 1` and the condition `removed < n/2`; using `<=` would over‑remove and possibly increase `size` unnecessarily.  
- The bucket array is sized `n+1` because a value can appear at most `n` times.

## Dry Run  
Input: `arr = [3,3,5,5,5,2,2,7]` (n = 8, need to remove ≥ 4 elements)

| iter | f | freq[f] (before) | removed (before) | size (before) | action                               |
|------|---|------------------|-------------------|---------------|--------------------------------------|
| 1    | 8 | 0                | 0                 | 0             | skip (freq[8]==0)                   |
| 2    | 7 | 0                | 0                 | 0             | skip                                 |
| 3    | 6 | 0                | 0                 | 0             | skip                                 |
| 4    | 5 | 1                | 0                 | 0             | `removed+=5` →5, `size++` →1, `freq[5]--`→0 |
| 5    | 5 | 0                | 5                 | 1             | exit outer loop (`removed ≥ 4`)      |

Final state: `size = 1`, because removing the value `5` (which appears 5 times) already discards more than half of the array. The algorithm therefore returns `1`.

## Complexity  
- **Time:** **O(n)** – the first two passes over `arr` and `count` are linear, and the greedy loop iterates at most `n` times because `f` decreases from `n` to `1` and each inner iteration consumes one bucket entry.  
- **Space:** **O(n)** – `freq` needs `n+1` slots; `count` uses a fixed 100001 slots independent of `n`. Both are auxiliary structures; the output (`size`) is O(1).

## Solution (Java)

```java
class Solution {
    public int minSetSize(int[] arr) {
        int n = arr.length;
        // count how many times a value appeas
        int count[] = new int[100001];
        for(int num : arr) count[num]++;
        
        // counts how many values appear at a give count example 3 and 2 appears 5 times so freq[5] = 2;
        int freq[] = new int[n + 1];
        for(int c : count) if(c > 0) freq[c]++;

        int removed = 0, size = 0;
        for(int f = n; f >= 1 && removed < n/2; f--){
            while(freq[f] > 0 && removed < n/2){
                removed += f;
                size++;
                freq[f]--;
            }
        }
        return size;

    }
}
```

---

**Runtime** 8 ms (beats 99.0%) · **Memory** 75 MB (beats 94.2%)

<sub>Synced by AILeetHub on 2026-10-23.</sub>
