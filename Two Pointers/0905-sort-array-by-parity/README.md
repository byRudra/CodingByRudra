# 905. Sort Array By Parity

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/sort-array-by-parity/)

`Array` · `Two Pointers` · `Sorting`

## Intuition  
If we could place every even number at the front of a new array while simultaneously placing every odd number at the back, the whole array would be partitioned correctly after just one scan. The key observation is that the relative order inside the even and odd groups does not matter, so we can treat the front and back as independent “buckets”. By advancing a `left` pointer from the start for evens and a `right` pointer from the end for odds, each element is written exactly once, eliminating the need for a second pass, a sort, or auxiliary data structures such as hash maps. This is the classic two‑pointer partitioning pattern.

## Approach  
1. **Initialize pointers** – `left = 0` (first free slot from the left) and `right = nums.length‑1` (first free slot from the right). Also allocate `answer` of the same length.  
2. **Iterate over `nums`** – `for (int num : nums)`. The loop terminates when the enhanced‑for iterator has visited every element.  
3. **Even case** – If `num % 2 == 0`, store it at `answer[left]` and then increment `left`. The invariant after this step: all positions `< left` contain only evens, and `left` points to the next empty slot.  
4. **Odd case** – Otherwise, store `num` at `answer[right]` and decrement `right`. The invariant after this step: all positions `> right` contain only odds, and `right` points to the next empty slot from the right.  
5. **Loop exit** – After the last element, `left` will be exactly one position beyond the last even, and `right` will be exactly one position before the first odd; the two regions meet without overlap.  
6. **Return** – The filled `answer` array already satisfies the parity ordering, so return it directly.

*Edge considerations*  
- The constraints guarantee at least one element, so the empty‑array case never occurs.  
- A single‑element array works because the loop writes the element either at `answer[0]` (even) or `answer[0]` (odd) and then finishes.  
- Duplicate values pose no problem; each occurrence follows the same parity rule.  
- The code uses `left++` and `right--` post‑increment/decrement to avoid off‑by‑one errors; the check `num % 2 == 0` is inclusive of zero, which is correctly classified as even.

## Dry Run  

**Input**: `nums = [3, 1, 2, 4]`

| Step | `num` | `left` before | `right` before | `answer` snapshot                     | Change                              |
|------|-------|---------------|----------------|---------------------------------------|-------------------------------------|
| 1    | 3     | 0             | 3              | `[_, _, _, _]`                        | odd → `answer[3]=3`, `right=2`      |
| 2    | 1     | 0             | 2              | `[_, _, _, 3]`                        | odd → `answer[2]=1`, `right=1`      |
| 3    | 2     | 0             | 1              | `[_, _, 1, 3]`                        | even → `answer[0]=2`, `left=1`      |
| 4    | 4     | 1             | 1              | `[2, _, 1, 3]`                        | even → `answer[1]=4`, `left=2`      |

After processing all elements, `answer = [2, 4, 1, 3]`. All evens occupy indices `0..1` and all odds occupy `2..3`, satisfying the required ordering.

## Complexity  
- **Time:** `O(n)` – the enhanced‑for loop visits each of the `n` elements exactly once, and each iteration performs only constant‑time assignments and pointer updates.  
- **Space:** `O(n)` – an auxiliary array `answer` of size `n` is allocated; the algorithm otherwise uses only a few integer variables. (The output array itself is not counted against extra space.)

## Solution (Java)

```java
class Solution {
    public int[] sortArrayByParity(int[] nums) {
        int left = 0;
        int right = nums.length - 1;
        int answer[] = new int[nums.length];
        for(int num : nums){
            if(num % 2 == 0){
                answer[left++] = num;
            }
            else
                answer[right--] = num;
        }
        return answer;

    }
}
```

---

**Runtime** 1 ms (beats 42.4%) · **Memory** 46.2 MB (beats 98.8%)

<sub>Synced by AILeetHub on 2026-10-15.</sub>
