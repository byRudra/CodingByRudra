# 43. Multiply Strings

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/multiply-strings/)

`Math` · `String` · `Simulation`

## Intuition  
When we multiply two decimal numbers by hand, each digit of the second operand is multiplied by every digit of the first, and the partial products are shifted according to their positions before being summed. The key observation is that the contribution of `num1[i]` and `num2[j]` always lands at index `i + j + 1` (least‑significant side) of the final digit array, while any overflow (carry) goes to `i + j`. By storing the intermediate sums directly in an `int[]` of length `n+m`, we avoid a second pass, a hash map, or converting the strings to big integers. This is the classic **two‑pointer digit multiplication** pattern.

## Approach  
1. **Zero shortcut** – If either string equals `"0"` return `"0"` immediately.  
2. **Allocate result buffer** – `int[] result = new int[n + m];` where `n = num1.length()` and `m = num2.length()`. All entries start at `0`.  
3. **Outer loop** – `for (int i = n - 1; i >= 0; i--)` iterates from the least‑significant digit of `num1` toward the most. Invariant: all positions to the right of `i` have already received every contribution that involves a digit right of `i`.  
4. **Inner loop** – `for (int j = m - 1; j >= 0; j--)` does the same for `num2`. Invariant: after each iteration, `result[i + j + 1]` holds the correct digit for the current partial product, and any overflow has been added to `result[i + j]`.  
   * Extract digits: `int first = num1.charAt(i) - '0';` and `int second = num2.charAt(j) - '0';`.  
   * Compute raw sum: `int sum = first * second + result[i + j + 1];`.  
   * Store the unit digit back: `result[i + j + 1] = sum % 10;`.  
   * Propagate the carry: `result[i + j] += sum / 10;`.  
5. **Skip leading zeros** – `int i = 0; while (i < n + m && result[i] == 0) i++;`. The loop stops at the first non‑zero digit or at the end, guaranteeing we never output a leading zero.  
6. **Build the answer** – Append `result[i]` through `result[n+m-1]` to a `StringBuilder` and return its string representation.

Edge handling: the code treats empty or single‑character inputs uniformly because the loops still run (or are bypassed) and the leading‑zero skip correctly yields `"0"` only when the early shortcut fires. The `<=` vs `<` choice in the inner index `i + j + 1` is dictated by the fact that the least‑significant position of the product is at index `n+m-1`; the extra slot at `i + j` is reserved for possible carry.

## Dry Run  

Multiply `num1 = "12"` and `num2 = "34"`.

| step | i | j | first | second | sum (incl. prev) | result array after step | note |
|------|---|---|-------|--------|------------------|--------------------------|------|
| 1 | 1 | 1 | 2 | 4 | 2*4+0 = 8 | [0,0,0,8] | unit placed at idx 3 |
| 2 | 1 | 0 | 2 | 3 | 2*3+0 = 6 | [0,0,6,8] | unit placed at idx 2 |
| 3 | 0 | 1 | 1 | 4 | 1*4+6 = 10 → digit 0, carry 1 | [0,1,0,8] | carry added to idx 1 |
| 4 | 0 | 0 | 1 | 3 | 1*3+1 = 4 | [0,4,0,8] | final digit at idx 1 |

After the loops `result = [0,4,0,8]`. Skipping the leading zero yields the string `"408"`, which is the correct product of 12 × 34.

## Complexity  
- **Time:** `O(n * m)` – the nested loops run `n` times for `i` and `m` times for `j`, performing constant work per iteration.  
- **Space:** `O(n + m)` – the `result` array holds at most `n+m` digits; the `StringBuilder` output does not count toward extra space.

## Solution (Java)

```java
class Solution {
    public String multiply(String num1, String num2) {
        if(num1.equals("0") || num2.equals("0")) return "0";
        int n = num1.length();
        int m = num2.length();
        int result[] = new int[n + m];
        for(int i = n - 1; i >= 0; i--){
            for(int j = m - 1; j >= 0; j--){
                int first = num1.charAt(i) - '0'; 
                int second = num2.charAt(j) - '0'; 

                int sum = first * second + result[i + j + 1];
                result[i + j + 1] = sum % 10;
                result[i + j] += sum / 10;

            }
        }
        int i = 0;
        while(i < n + m && result[i] == 0)
            i++;
        
        StringBuilder answer = new StringBuilder();
        while(i < n + m){
            answer.append(result[i++]);
        }
        return answer.toString();
    }
}
```

---

**Runtime** 2 ms (beats 99.8%) · **Memory** 43.3 MB (beats 89.6%)

<sub>Synced by AILeetHub on 2026-10-07.</sub>
