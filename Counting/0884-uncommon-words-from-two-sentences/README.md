# 884. Uncommon Words from Two Sentences

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/uncommon-words-from-two-sentences/)

`Hash Table` · `String` · `Counting`

## Intuition  
The only thing that distinguishes an uncommon word from a common one is its total frequency across the two sentences: it must appear exactly once overall. If we could count how many times each word occurs in the concatenated string, the answer is simply the set of words whose counter equals 1. A naïve solution would compare every word of `s1` with every word of `s2`, leading to O((n+m)²) time or would need two separate hash maps and a second pass to intersect them. By merging the sentences first and maintaining a single frequency map, we eliminate the extra pass and any need for explicit set operations. This is the classic **hash‑table counting** pattern.

## Approach  
1. **Create a frequency map** – `Map<String, Integer> map = new HashMap<>();`.  
2. **Iterate over every word** in the concatenated string `(s1 + " " + s2).split(" ")`.  
   - *Loop exit*: when the split array is exhausted.  
   - *Invariant*: after processing the first *k* words, `map` stores the exact occurrence count of each of those *k* words.  
   - For each `word`, update its count with `map.put(word, map.getOrDefault(word, 0) + 1);`.  
   - The code deliberately uses `getOrDefault` to avoid a separate `containsKey` check, guaranteeing O(1) update even for first‑time entries.  
3. **Collect uncommon words** – initialise `List<String> res = new ArrayList<>();`.  
4. **Iterate over the key set** of `map`.  
   - *Loop exit*: when all distinct keys have been examined.  
   - *Invariant*: at the start of each iteration, `res` contains exactly those keys seen so far whose count is 1.  
   - If `map.get(word) == 1`, add `word` to `res`. The equality check is `== 1` (not `<= 1`) because a count of zero never appears in the map, and we only want words that occur once, not zero times.  
5. **Return the result** as an array: `return res.toArray(new String[0]);`.  
   - The `new String[0]` idiom lets the runtime allocate an array of the correct size, keeping the method O(1) extra space beyond the list.

Edge‑case handling is implicit: an empty or single‑word sentence still yields a split array with at least one element because the concatenation always inserts a space; the map correctly records a count of 1 for that lone word, which is then returned.

## Dry Run  

**Input**  
```
s1 = "this apple is sweet"
s2 = "this apple is sour"
```

| Iteration | word   | map after update (key→count)                     | Note                              |
|-----------|--------|--------------------------------------------------|-----------------------------------|
| 1         | this   | {this→1}                                         | first occurrence                  |
| 2         | apple  | {this→1, apple→1}                                | first occurrence                  |
| 3         | is     | {this→1, apple→1, is→1}                          | first occurrence                  |
| 4         | sweet  | {this→1, apple→1, is→1, sweet→1}                | first occurrence                  |
| 5         | this   | {this→2, apple→1, is→1, sweet→1}                | second occurrence, count becomes 2 |
| 6         | apple  | {this→2, apple→2, is→1, sweet→1}                | second occurrence                  |
| 7         | is     | {this→2, apple→2, is→2, sweet→1}                | second occurrence                  |
| 8         | sour   | {this→2, apple→2, is→2, sweet→1, sour→1}        | first occurrence                  |

After the first loop, `map` holds the exact frequencies. The second loop scans the keys and adds `sweet` and `sour` because their counts equal 1. The final array `["sweet","sour"]` is therefore the correct set of uncommon words.

## Complexity  
- **Time:** O(n + m) – the first loop processes each word of both sentences once, and the second loop visits each distinct word exactly once.  
- **Space:** O(u) – a hash map storing one entry per unique word (`u` ≤ n + m). The output array is not counted toward extra space.

## Solution (Java)

```java
class Solution {
    public String[] uncommonFromSentences(String s1, String s2) {
        Map<String, Integer> map = new HashMap<>();

        for (String word : (s1 + " " + s2).split(" ")) {
            map.put(word, map.getOrDefault(word, 0) + 1);
        }

        List<String> res = new ArrayList<>();

        for (String word : map.keySet()) {
            if (map.get(word) == 1) {
                res.add(word);
            }
        }

        return res.toArray(new String[0]);
    }
}
```

---

**Runtime** 4 ms (beats 36.9%) · **Memory** 43.1 MB (beats 88.1%)

<sub>Synced by AILeetHub on 2026-10-22.</sub>
