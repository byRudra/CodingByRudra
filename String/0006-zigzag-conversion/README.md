# 6. Zigzag Conversion

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/zigzag-conversion/)

`String`

## Intuition  
When we write the characters of `s` in a zigzag, each step either moves one row down or one row up. After every full down‑and‑up traversal the row index repeats, so the pattern’s period is `2 * numRows - 2`. A naïve solution would allocate a full 2‑D grid or perform two passes to compute the exact positions, both costing extra space or time. By tracking the current row and flipping a direction flag whenever we hit the top or bottom row, we can stream the characters into the correct rows in a single pass.

## Approach  
1. **Handle trivial cases.**  
   - If `numRows == 1` or `numRows >= s.length()`, return `s` unchanged because no zigzag can form.  
2. **Initialise helpers.**  
   - `boolean goingDown = false;` – indicates the current vertical direction.  
   - `int rowNumber = 0;` – the index of the row that will receive the next character.  
   - `StringBuilder[] rows = new StringBuilder[numRows];` – one builder per row, each created in a loop (`rows[i] = new StringBuilder();`).  
3. **Distribute characters.**  
   - Iterate `for (char c : s.toCharArray())`.  
   - Append `c` to the current row: `rows[rowNumber].append(c);`.  
   - If `rowNumber` is at the first (`0`) or last (`numRows‑1`) row, flip the direction: `goingDown = !goingDown;`.  
   - Move to the next row: `rowNumber += goingDown ? 1 : -1;`.  
   - **Invariant:** before each iteration, `rowNumber` is a valid index `[0, numRows‑1]` and `goingDown` correctly reflects whether the next step should go down (true) or up (false).  
4. **Combine rows.**  
   - Create `StringBuilder result = new StringBuilder();`.  
   - Append each row in order: `result.append(row);`.  
   - Return `result.toString();`.  

The code deliberately uses `<=`‑style checks (`rowNumber == 0 || rowNumber == numRows - 1`) to toggle direction exactly at the boundaries, avoiding off‑by‑one errors that would arise if the toggle were placed after the index update.

## Dry Run  

**Input:** `s = "PAYPAL"`, `numRows = 3`

| Iter | c | rowNumber (before) | goingDown (before) | rows[0] | rows[1] | rows[2] | Action |
|------|---|--------------------|--------------------|---------|---------|---------|--------|
| 1 | P | 0 | false | "" → "P" | "" | "" | toggle (top) → goingDown=true, rowNumber+=1 → 1 |
| 2 | A | 1 | true | "P" | "" → "A" | "" | rowNumber+=1 → 2 |
| 3 | Y | 2 | true | "P" | "A" | "" → "Y" | toggle (bottom) → goingDown=false, rowNumber‑=1 → 1 |
| 4 | P | 1 | false | "P" | "A" → "AP" | "Y" | rowNumber‑=1 → 0 |
| 5 | A | 0 | false | "P" → "PA" | "AP" | "Y" | toggle (top) → goingDown=true, rowNumber+=1 → 1 |
| 6 | L | 1 | true | "PA" | "AP" → "APL" | "Y" | rowNumber+=1 → 2 (loop ends) |

After the loop the rows contain `"PA"`, `"APL"`, and `"Y"`. Concatenating yields `"PAAPLY"`, which is the correct zigzag reading for this input.

## Complexity  
- **Time:** `O(n)` – the main `for` loop visits each of the `n` characters exactly once, and the final concatenation traverses the same `n` characters across the `numRows` builders.  
- **Space:** `O(n)` – we store the characters in `numRows` `StringBuilder`s whose total length equals `n`; no additional data structures proportional to `n` are created. (The output string itself is not counted.)

## Solution (Java)

```java
class Solution {
    public String convert(String s, int numRows) {
        if(numRows == 1 || numRows >= s.length())
            return s;
        boolean goingDown = false;
        int rowNumber = 0;

        StringBuilder[] rows = new StringBuilder[numRows];
        for(int i = 0; i < numRows; i++){
            rows[i] = new StringBuilder();
        }

        for(char c : s.toCharArray()){
            rows[rowNumber].append(c);

            if(rowNumber == 0 || rowNumber == numRows - 1)
                goingDown = !goingDown;
            
            rowNumber += goingDown ? 1 : -1;
        }

        StringBuilder result = new StringBuilder();
        for(StringBuilder row : rows){
            result.append(row);
        }
        return result.toString();
    }
}
```

---

**Runtime** 4 ms (beats 87.9%) · **Memory** 46.7 MB (beats 48.0%)

<sub>Synced by AILeetHub on 2026-09-15.</sub>
