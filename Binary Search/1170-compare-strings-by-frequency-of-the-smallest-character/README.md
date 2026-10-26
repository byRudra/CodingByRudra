# 1170. Compare Strings by Frequency of the Smallest Character

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/compare-strings-by-frequency-of-the-smallest-character/)

`Array` · `Hash Table` · `String` · `Binary Search` · `Sorting`

## Intuition  
The only thing that matters for comparison is how many times the smallest character appears in each string, i.e., the value returned by `f`. If we know `f` for every word once, we never need to recompute it while scanning the queries. The naïve way would call `f` inside the double loop, costing O(|queries|·|words|·L) where L is the average string length. By extracting all word frequencies into an auxiliary array `freq` we eliminate the repeated work, turning the inner work into a simple integer comparison. This is a classic **pre‑computation** (or “cache”) pattern.

## Approach  
1. **Allocate result and cache.**  
   - `int ans[] = new int[queries.length];` stores the final counts.  
   - `int freq[] = new int[words.length];` will hold `f(words[i])` for every word.  

2. **Populate `freq`.**  
   - Loop `i` from 0 to `words.length‑1`.  
   - In each iteration compute `freq[i] = f(words[i]);`.  
   - *Invariant:* after processing index i, `freq[0..i]` contain correct frequencies.  
   - Exit when `i == words.length`.  

3. **Answer each query.**  
   - Loop `i` from 0 to `queries.length‑1`.  
   - Compute `int q = f(queries[i]);` – the frequency of the current query.  
   - Initialise `int count = 0;`.  

4. **Count superior words.**  
   - Loop `j` from 0 to `words.length‑1`.  
   - If `freq[j] > q` then `count++`.  
   - *Invariant:* after processing index j, `count` equals the number of words among `words[0..j]` whose frequency exceeds `q`.  
   - Exit when `j == words.length`.  

5. **Store the answer.**  
   - Set `ans[i] = count;`.  
   - Proceed to the next query.  

6. **Return the filled array.**  

*Edge handling:* The code assumes non‑empty input as guaranteed by the constraints; no special case for length 0 is needed. The comparison uses `>` (strictly greater) because the problem asks for `f(query) < f(word)`. The helper `f` tracks the current minimum character (`min`) and its count, resetting the count whenever a new smaller character appears.

## Dry Run  
**Input**  
```
queries = ["cbd"]
words   = ["zaaaz","a"]
```

| step | i (word idx) | freq[i] = f(words[i]) | q = f(queries[0]) | j (inner idx) | count before | count after | note |
|------|--------------|----------------------|-------------------|---------------|--------------|-------------|------|
| 1    | 0            | min='a', count=3 → 3 | –                 | –             | –            | –           | compute f("zaaaz") |
| 2    | 1            | min='a', count=1 → 1 | –                 | –             | –            | –           | compute f("a") |
| 3    | –            | –                    | q = 1 (min='b')   | 0             | 0            | 1           | freq[0]=3 > 1 |
| 4    | –            | –                    | –                 | 1             | 1            | 1           | freq[1]=1 not > 1 |
| 5    | –            | –                    | –                 | –             | –            | –           | loop ends, ans[0]=1 |

Final state: `ans = [1]` because exactly one word (“zaaaz”) has a larger smallest‑character frequency than the query “cbd”.

## Complexity  
- **Time:** O(|words|·L₁ + |queries|·L₂ + |queries|·|words|) → O(m·n) after the pre‑computation, because `f` runs in linear time over each string once and the double loop performs a constant‑time comparison for every query‑word pair.  
- **Space:** O(|words|) for the `freq` array (output `ans` is not counted). No additional data structures are allocated.

## Solution (Java)

```java
class Solution {
    public int[] numSmallerByFrequency(String[] queries, String[] words) {
        int ans[] = new int[queries.length];
        int freq[] = new int[words.length];

        for(int i = 0; i < words.length; i++){
            freq[i] = f(words[i]);
        }

        for(int i = 0; i < queries.length; i++){
            int q = f(queries[i]);
            int count = 0;

            for(int j = 0; j < words.length; j++){
                if(freq[j] > q){
                    count++;
                }
            }
            ans[i] = count;
        }
        return ans;
    }

    private int f(String word){
        char min = 'z';
        int count = 0;
        for(char c  : word.toCharArray()){
            if(c < min){
                min = c;
                count = 1;
            }else if(c == min){
                count++;
            }
        }
        return count;
    }
}
```

---

**Runtime** 12 ms (beats 33.9%) · **Memory** 45.8 MB (beats 98.3%)

<sub>Synced by AILeetHub on 2026-10-26.</sub>
