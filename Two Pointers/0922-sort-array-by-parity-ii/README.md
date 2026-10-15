# 922. Sort Array By Parity II

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/sort-array-by-parity-ii/)

`Array` · `Two Pointers` · `Sorting`

## Intuition  
The only requirement is that every even value lands on an even index and every odd value lands on an odd index. Because the input guarantees exactly half the numbers are even and half are odd, we can treat the two groups independently: the first free even slot and the first free odd slot are always the correct places for the next even or odd element we encounter. A naïve solution would scan the array twice or sort it, which costs extra passes or O(n log n) time. By maintaining two pointers that jump by two, we place each number in its final position in a single linear pass. This is the classic **two‑pointer** placement pattern.

## Approach  
1. **Initialize pointers** – `even = 0` (next free even index) and `odd = 1` (next free odd index). Also allocate `answer` of the same length.  
2. **Iterate over `nums`** with a for‑each loop. The loop terminates when the last element of `nums` has been processed.  
   - **Invariant**: before each iteration, `even` points to the smallest even index that has not been filled yet, and `odd` points to the smallest odd index that has not been filled yet. All earlier positions in `answer` already contain correctly‑parity numbers.  
3. **Place the current `num`**  
   - If `num % 2 == 0`, assign `answer[even] = num` and advance `even += 2`.  
   - Otherwise, assign `answer[odd] = num` and advance `odd += 2`.  
   The `+= 2` step guarantees we stay on the same parity and never overwrite a filled slot.  
4. **Finish** – after the loop, every index of `answer` is filled with a value of matching parity, so return `answer`.  

**Edge considerations**  
- The constraints guarantee `nums.length` is even and contains equal evens/odds, so the pointers never run out of bounds.  
- An empty or single‑element array cannot appear, but the code would still behave correctly because the loop would simply not execute or would fill the only slot.  
- The choice of `<=` vs `<` is irrelevant here because the pointers are only updated after a successful placement; the loop condition is driven by the foreach over `nums`, not by the pointers.

## Dry Run  

Input: `nums = [4, 2, 5, 7]`

| Iter | `num` | `even` before | `odd` before | `answer` after placement                | Note                              |
|------|-------|---------------|--------------|------------------------------------------|-----------------------------------|
| 1    | 4     | 0             | 1            | `[4, _, _, _]`                           | 4 is even → placed at index 0, `even+=2` → 2 |
| 2    | 2     | 2             | 1            | `[4, _, 2, _]`                           | 2 is even → placed at index 2, `even+=2` → 4 |
| 3    | 5     | 4 (out of range) | 1            | `[4, 5, 2, _]`                           | 5 is odd → placed at index 1, `odd+=2` → 3 |
| 4    | 7     | 4             | 3            | `[4, 5, 2, 7]`                           | 7 is odd → placed at index 3, `odd+=2` → 5 |

After the fourth iteration `answer = [4,5,2,7]`, which satisfies the parity‑index condition, so it is returned.

## Complexity  
- **Time:** `O(n)` – the single `for` loop visits each of the `n` elements exactly once, and pointer updates are constant‑time.  
- **Space:** `O(n)` – a new array `answer` of length `n` is allocated; no additional data structures are used beyond a few integer variables. (The output array itself is not counted against auxiliary space.)

## Solution (Java)

```java
class Solution {
    public int[] sortArrayByParityII(int[] nums) {
        int even = 0;
        int odd = 1;
        int answer[] = new int[nums.length];
        for (int num : nums) {
            if (num % 2 == 0) {
                answer[even] = num;
                even += 2;
            } else {
                answer[odd] = num;
                odd += 2;
            }
        }

        return answer;

    }
}
```

---

**Runtime** 2 ms (beats 99.8%) · **Memory** 48.4 MB (beats 76.6%)

<sub>Synced by AILeetHub on 2026-10-15.</sub>
