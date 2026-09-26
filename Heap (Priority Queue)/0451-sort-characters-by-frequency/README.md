# 451. Sort Characters By Frequency

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/sort-characters-by-frequency/)

`Hash Table` · `String` · `Sorting` · `Heap (Priority Queue)` · `Bucket Sort` · `Counting`

## Intuition  
The key observation is that the final order depends only on how many times each distinct character appears, not on their original positions. If we know each character’s frequency, we can place all copies of the most frequent character first, then the next most frequent, and so on. A naïve solution would sort the whole string by repeatedly counting frequencies, which would be O(n²). By collapsing the problem to “sort the set of distinct characters by their pre‑computed counts”, we eliminate the extra passes and any need for a hash‑map lookup during the final construction. This leads directly to a two‑step solution that uses a hash table for counting and a list sort for ordering – a classic **hash‑table + custom sort** pattern.

## Approach  
1. **Count frequencies** – Iterate over `s.toCharArray()`. For each `c`, execute `counts.put(c, counts.getOrDefault(c, 0) + 1)`. After the loop `counts` maps every distinct character to its exact occurrence count.  
2. **Collect distinct characters** – Create `List<Character> chars = new ArrayList<>(counts.keySet())`. The list now contains each unique character exactly once.  
3. **Sort by descending frequency** – Call `chars.sort((a, b) -> counts.get(b) - counts.get(a))`. The comparator guarantees that after sorting, for any adjacent pair `a, b` we have `counts.get(a) ≥ counts.get(b)`. The loop terminates when the list length is reached; no off‑by‑one issues arise because `List.sort` handles the full range internally.  
4. **Build the answer** – Initialise `StringBuilder sb = new StringBuilder()`. For each `c` in the sorted `chars`, retrieve `int freq = counts.get(c)`. Then run `for (int i = 0; i < freq; i++) sb.append(c)`. The inner loop appends exactly `freq` copies, preserving the required grouping.  
5. **Return result** – `return sb.toString();`. The builder already contains the characters in the correct order, so no further processing is needed.

Edge cases: an empty or single‑character string never reaches the sorting step because `counts` will contain 0 or 1 key, and the loops naturally handle those sizes. The comparator uses subtraction (`counts.get(b) - counts.get(a)`) which is safe because frequencies are bounded by `s.length ≤ 5·10⁵`, well within `int` range.

## Dry Run  
**Input:** `s = "tree"`  

| iter | `c` (from `chars`) | `freq` | `sb` after iteration | note |
|------|-------------------|-------|----------------------|------|
| 1    | `e`               | 2     | `ee`                 | `e` is most frequent (2) |
| 2    | `r`               | 1     | `eer`                | next highest frequency |
| 3    | `t`               | 1     | `eert`               | last character appended |

After processing all entries, `sb` holds `"eert"`, which satisfies the required decreasing‑frequency order.

## Complexity  
- **Time:** **O(n log k)** – counting is O(n) for `n` characters; sorting `k` distinct characters costs O(k log k), and the final nested loop writes each character exactly `freq` times, totaling another O(n). Since `k ≤ n`, the dominant term is the sort.  
- **Space:** **O(k)** – the hash map `counts` stores one entry per distinct character, and the list `chars` holds the same `k` keys. The `StringBuilder` output is not counted against auxiliary space.

## Solution (Java)

```java
class Solution {
    public String frequencySort(String s) {
        Map<Character, Integer> counts = new HashMap<>();
        for (char c : s.toCharArray()) {
            counts.put(c, counts.getOrDefault(c, 0) + 1);
        }

        List<Character> chars = new ArrayList<>(counts.keySet());
        chars.sort((a, b) -> counts.get(b) - counts.get(a));

        StringBuilder sb = new StringBuilder();
        for (char c : chars) {
            int freq = counts.get(c);
            for (int i = 0; i < freq; i++) {
                sb.append(c);
            }
        }

        return sb.toString();
    }
}
```

---

**Runtime** 13 ms (beats 74.6%) · **Memory** 46.7 MB (beats 49.9%)

<sub>Synced by AILeetHub on 2026-09-26.</sub>
