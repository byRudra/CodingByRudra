# 946. Validate Stack Sequences

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/validate-stack-sequences/)

`Array` · `Stack` · `Simulation`

## Intuition  
The key observation is that a stack can only pop the element that was most recently pushed, so while we scan the `pushed` sequence we can immediately discard any top‑of‑stack values that already match the next required element in `popped`. A naïve simulation would first push everything, then repeatedly search for the next pop target, costing O(n²) or requiring extra bookkeeping such as a hash map. By interleaving pushes with opportunistic pops, we eliminate the second pass entirely. This is the classic **two‑pointer / stack simulation** pattern.

## Approach  
1. **Initialise** an empty `Deque<Integer>` called `stack` and a pointer `j = 0` that tracks how many elements of `popped` have been successfully matched.  
2. **Iterate** over each `num` in `pushed` (enhanced for‑loop).  
   - **Push** `num` onto `stack` (`stack.push(num)`).  
   - **Enter** a `while` loop whose guard is threefold: the stack is not empty, `j` is still within bounds, and the current top equals the next required pop (`popped[j] == stack.peek()`).  
3. **Inside** the `while` loop, **pop** the matching element (`stack.pop()`) and **advance** `j` (`j++`). This may repeat multiple times because a newly pushed element can enable a cascade of pops.  
4. The `while` loop **terminates** when either the stack becomes empty, we have exhausted `popped`, or the top no longer matches the next pop target. At that moment the invariant holds: all elements before index `j` in `popped` have been produced in correct order, and the stack contains exactly those pushed elements that cannot yet be popped.  
5. After the `for` loop finishes, **return** `stack.isEmpty()`. If the stack is empty, every element of `popped` was matched; otherwise some pushed values could not be removed, meaning the sequence is impossible.  
6. Edge cases are handled implicitly: an empty input never occurs because of constraints, and a single‑element array works because the `while` condition will pop it immediately if it matches.

## Dry Run  
**Input**  
`pushed = [1,2,3,4,5]`  
`popped = [4,5,3,2,1]`

| Iteration | num (pushed) | stack (top→bottom) | j | Action taken | Note |
|-----------|--------------|--------------------|---|--------------|------|
| 1 | 1 | [1] | 0 | push 1 | top ≠ popped[0]=4 |
| 2 | 2 | [2,1] | 0 | push 2 | top ≠ 4 |
| 3 | 3 | [3,2,1] | 0 | push 3 | top ≠ 4 |
| 4 | 4 | [4,3,2,1] | 0 | push 4 → while pops 4, j=1 | new top 3 ≠ popped[1]=5 |
| 5 | 5 | [5,3,2,1] | 1 | push 5 → while pops 5, j=2 → while pops 3, j=3 | new top 2 = popped[3] |
| 6 | (no more num) | [2,1] | 3 | while pops 2, j=4 → while pops 1, j=5 | stack empty |

After processing all pushes, `stack.isEmpty()` is true, so the algorithm returns `true`.

## Complexity  
- **Time:** O(n) – each element is pushed once and popped at most once; the inner `while` runs a total of `n` iterations across the whole algorithm.  
- **Space:** O(n) – the `Deque` may hold up to all `n` elements in the worst case; auxiliary variables use O(1) extra space.

## Solution (Java)

```java
class Solution {
    public boolean validateStackSequences(int[] pushed, int[] popped) {
     Deque<Integer> stack = new ArrayDeque<>();
     int j = 0; // no of popped performed
     for(int num : pushed){
        stack.push(num);
        while(!stack.isEmpty() && j < popped.length && popped[j] == stack.peek()){
            stack.pop();
            j++;
        }
     }
     return stack.isEmpty();
    }
}
```

---

**Runtime** 2 ms (beats 87.9%) · **Memory** 45.7 MB (beats 91.8%)

<sub>Synced by AILeetHub on 2026-11-04.</sub>
