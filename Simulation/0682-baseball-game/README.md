# 682. Baseball Game

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/baseball-game/)

`Array` · `Stack` · `Simulation`

## Intuition  
The only information ever needed to process a new operation is the most recent one or two valid scores. If we keep those scores in a LIFO structure, the top of the structure is always the “previous” score and the element just beneath it is the “second‑previous” score. This observation lets us avoid scanning the whole record or maintaining an auxiliary index for each operation. By using a stack we can apply `C`, `D` and `+` in constant time, which is the classic **stack** pattern for “last‑in‑first‑out” dependencies.

## Approach  
1. **Initialize** an empty `Deque<Integer> stack`.  
2. **Iterate** over each `op` in `operations` (the loop terminates when the array is exhausted). The invariant: *stack contains exactly the scores that are still valid, with the most recent score on the top.*  
3. **Switch on `op`**  
   - **Case `"+"`**  
     - `int top = stack.pop();` removes the most recent score temporarily.  
     - `int second = stack.peek();` reads the second‑most score without removing it.  
     - `stack.push(top);` restores the original top.  
     - `stack.push(top + second);` pushes the sum, which becomes the new top.  
     The exit condition of this branch is reaching the `break`; the invariant is restored because the stack now holds the original two scores followed by their sum.  
   - **Case `"D"`**  
     - `stack.push(stack.peek() * 2);` duplicates the current top, doubles it, and pushes the result. The invariant holds because the new top is the doubled score while the older scores stay untouched.  
   - **Case `"C"`**  
     - `stack.pop();` discards the most recent score. The invariant is preserved because the stack now represents the record after the invalidation.  
   - **Default (numeric string)**  
     - `stack.push(Integer.parseInt(op));` converts the string to an integer and pushes it as a new valid score.  
4. **After the loop**, compute the total: `int sum = 0; for (int score : stack) sum += score;`. The loop iterates over all remaining elements; the invariant guarantees that `stack` now holds exactly the final record.  
5. **Return** `sum`.

Edge cases are handled naturally: an empty or single‑element input never triggers `"+"` because the problem guarantees enough prior scores; `"C"` and `"D"` are safe because the stack is non‑empty when those branches execute. The code chooses `pop()` then `push()` for `"+"` to avoid an extra temporary variable for the second element, ensuring correct ordering.

## Dry Run  

**Input:** `["5","2","C","D","+"]`

| Step | op | top (after pop) | second (peek) | stack after step | note |
|------|----|----------------|--------------|------------------|------|
| 1 | "5" | – | – | [5] | numeric → push 5 |
| 2 | "2" | – | – | [2,5] | numeric → push 2 |
| 3 | "C" | – | – | [5] | pop removes 2 |
| 4 | "D" | – | – | [10,5] | peek 5, push 5*2 |
| 5 | "+" | 10 | 5 | [15,10,5] | pop 10, peek 5, push 10 back, push 10+5 |

Final stack `[15,10,5]` represents the record `[5,10,15]`; their sum `30` is returned.

## Complexity  
- **Time:** **O(n)** – each operation is processed in constant time; the final summation traverses the stack once, so the total work is proportional to the number of input strings `n`.  
- **Space:** **O(n)** – the stack stores at most one entry per operation, i.e., the current valid record; no additional structures proportional to `n` are created. (The output integer itself is excluded from the space count.)

## Solution (Java)

```java
class Solution {
    public int calPoints(String[] operations) {
        Deque<Integer> stack = new ArrayDeque<>();
        for (String op : operations) {
            switch (op) {
                case "+":
                    int top = stack.pop();
                    int second = stack.peek();
                    stack.push(top);
                    stack.push(top + second);
                    break;
                case "D":
                    stack.push(stack.peek() * 2);
                    break;
                case "C":
                    stack.pop();
                    break;
                default:
                    stack.push(Integer.parseInt(op));
                    break;
            }
        }
        
        int sum = 0;
        for (int score : stack) {
            sum += score;
        }
        return sum;
    }
}
```

---

**Runtime** 3 ms (beats 78.9%) · **Memory** 43.4 MB (beats 82.1%)

<sub>Synced by AILeetHub on 2026-09-26.</sub>
