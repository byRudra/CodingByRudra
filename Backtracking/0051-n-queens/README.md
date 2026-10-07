# 51. N-Queens

![Hard](https://img.shields.io/badge/Difficulty-Hard-ff375f?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/n-queens/)

`Array` · `Backtracking` · `Algorithm X`

## Intuition  
When we place queens row by row, any conflict can only come from previously placed queens in earlier rows. Thus after placing `k` queens, the board’s first `k` rows are fully determined and safe, and the remaining rows are untouched. The naive way would try every possible placement of `n` queens on an `n×n` board, which is `nⁿ` possibilities. By advancing one row at a time and discarding a column as soon as it attacks an existing queen, we prune the search dramatically. This is the classic **backtracking** pattern.

## Approach  
1. **Initialize board** – create a `char[n][n]` filled with `'.'`.  
2. **Start recursion** – call `backTrack(0, board, result, n)`.  
3. **Base case** – if `row == n`, all rows are filled safely; convert each `char[]` to a `String`, collect them into `candidate`, and add to `result`.  
4. **Iterate columns** – for the current `row`, loop `col` from `0` to `n‑1`.  
5. **Safety test** – invoke `isSafe(row, col, board, n)`. Inside:  
   - Scan column `col` for any `'Q'` in rows `0 … row‑1`.  
   - Scan the upper‑left diagonal (`i--, j--`) until out of bounds.  
   - Scan the upper‑right diagonal (`i--, j++`) until out of bounds.  
   If any queen is found, return `false`.  
6. **Place queen** – set `board[row][col] = 'Q'`.  
7. **Recurse deeper** – call `backTrack(row + 1, board, result, n)`.  
8. **Backtrack** – after the recursive call returns, reset `board[row][col] = '.'` to explore alternative columns.  
9. **Loop termination** – the `for` loop ends when `col == n`; at that point the current row has no safe column, so the call returns to the previous level.

Key edge handling:  
- If `n == 0` the outer call never reaches the base case, but constraints guarantee `n ≥ 1`.  
- For a single‑row board (`n == 1`) the first column passes `isSafe`, producing the solitary solution.  
- The safety checks use `<=` bounds for the diagonal loops (`i >= 0 && j >= 0` / `j < n`) because the current cell itself is not yet occupied.

## Dry Run  
**Input:** `n = 4`  

| step | row | col tried | board (rows shown)                     | note                              |
|------|-----|-----------|----------------------------------------|-----------------------------------|
| 1    | 0   | 1         | `.Q..`                                 | queen placed at (0,1)            |
| 2    | 1   | 3         | `.Q..`<br>`...Q`                       | safe; place queen at (1,3)       |
| 3    | 2   | 0         | `.Q..`<br>`...Q`<br>`Q...`             | safe; place queen at (2,0)       |
| 4    | 3   | 2         | `.Q..`<br>`...Q`<br>`Q...`<br>`..Q.`   | safe; all rows filled → add solution |
| 5    | 3   | backtrack | board reset to `...` in row 3         | backtrack, try next column (none) |
| 6    | 2   | backtrack | row 2 reset to `....`                 | backtrack, try next column (2)   |
| 7    | 2   | 2 (fails) | conflict on upper‑right diagonal       | `isSafe` returns false           |
| 8    | 2   | 3 (fails) | conflict on column                     | `isSafe` returns false           |
| 9    | 1   | backtrack | row 1 reset to `....`                  | continue exploring other branches |

The algorithm eventually records the second valid board `["..Q.", "Q...", "...Q", ".Q.."]`. The final state is that `result` contains exactly the two known 4‑queen solutions.

## Complexity  
- **Time:** O(N!), because each row tries up to N columns and the safety checks prune many branches, but in the worst case the recursion explores all permutations of queen placements.  
- **Space:** O(N²) for the mutable `board` plus O(N) recursion stack depth; the output list is not counted toward auxiliary space.

## Solution (Java)

```java
class Solution {
    public List<List<String>> solveNQueens(int n) {
        List<List<String>> result = new ArrayList<>();
        
        // constructing the board
        char[][] board = new char[n][n];
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                board[i][j] = '.';
            }
        }

        backTrack(0, board, result, n);
        return result;
    }

    // BackTrack Function
    private void backTrack(int row, char[][] board, List<List<String>> result, int n){
        // Base Condition
        if(row == n){
            List<String> candidate = new ArrayList<>();
            for(char[] r : board){
                candidate.add(new String(r));
            }
            result.add(candidate);
            return;
        }

        // now we traverse for each column in this row 
        for(int col = 0; col < n; col++){
            // check if we can place a queen in this col
            if(isSafe(row, col, board, n)){
                board[row][col] = 'Q';
                backTrack(row + 1, board, result, n);
                board[row][col] = '.';
            }
        }

    }


    // Check if Safe 
    private boolean isSafe(int row, int col, char[][] board, int n){
        
        // check column
        for(int i = 0; i < row; i++){
            if(board[i][col] == 'Q') return false;
        }
        // check upper diagonals
        // only check upper because the bellow ones arent yet constructed so col - 1 goes to the left diagonal index
        
        // upper left
        for(int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--){
            if(board[i][j] == 'Q') return false; 
        }

        // upper right
        // col + 1 will go to the right column but row - 1 will take it above as the diagonal
        for(int i = row - 1, j = col + 1; i >= 0 && j < n; i--, j++){
            if(board[i][j] == 'Q') return false; 
        }


        return true;
    }
}
```

---

**Runtime** 2 ms (beats 81.7%) · **Memory** 46.3 MB (beats 91.0%)

<sub>Synced by AILeetHub on 2026-10-07.</sub>
