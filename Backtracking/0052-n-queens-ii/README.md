# 52. N-Queens II

![Hard](https://img.shields.io/badge/Difficulty-Hard-ff375f?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/n-queens-ii/)

`Backtracking` · `Algorithm X`

## Intuition  
When queens are placed one per row, any conflict can only involve a previously placed queen because rows below have not been touched yet. Therefore after `k` rows have been filled, the board’s first `k` rows already satisfy the “no‑attack” rule, and the next queen must avoid the columns and the two diagonals that are already occupied. This eliminates the need for a second pass, a hash map, or a full‑board scan at every step; a simple linear check of the three directions suffices. The overall pattern is classic **backtracking with pruning**.

## Approach  
1. **Initialise board** – create an `n×n` char matrix filled with `'.'`.  
2. **Start recursion** – call `backTrack(0, board, n)`.  
3. **Base case** – if `row == n`, a complete placement has been found; increment the global `count` and return.  
4. **Iterate columns** – for the current `row`, loop `col` from `0` to `n‑1`.  
5. **Safety test** – invoke `isSafe(row, col, board, n)`.  
   - *Column check*: scan rows `0 … row‑1` in the same column.  
   - *Upper‑left diagonal*: walk `i--, j--` while both indices stay ≥ 0.  
   - *Upper‑right diagonal*: walk `i--, j++` while `i ≥ 0` and `j < n`.  
   If any `'Q'` is encountered, the position is illegal.  
6. **Place queen** – set `board[row][col] = 'Q'`.  
7. **Recurse deeper** – call `backTrack(row + 1, board, n)`.  
8. **Backtrack** – after the recursive call returns, reset the cell with `board[row][col] = '.'` so the next column can be tried.  
9. **Loop termination** – the `for` loop ends when `col == n`. At that point all possibilities for the current row have been exhausted and the function returns to the previous level.

Key edge handling:  
- If `n == 0` the outer call never reaches the base case, but the constraints guarantee `n ≥ 1`.  
- For a single‑row board the loop places a queen in the only column, hits the base case, and counts one solution.  
- The safety loops use `<=`‑style bounds (`i >= 0 && j >= 0`, `j < n`) to avoid off‑by‑one errors when the queen sits on the board edge.

## Dry Run  
**Input:** `n = 4`  

| step | row | col tried | board change (Q placed) | note |
|------|-----|-----------|--------------------------|------|
| 1 | 0 | 0 | (0,0) = Q | first queen placed |
| 2 | 1 | 0 | ❌ unsafe (same column) | skip |
| 3 | 1 | 1 | ❌ unsafe (diag left) | skip |
| 4 | 1 | 2 | (1,2) = Q | second queen placed |
| 5 | 2 | 0 | ❌ unsafe (diag left) | skip |
| 6 | 2 | 1 | ❌ unsafe (diag right) | skip |
| 7 | 2 | 2 | ❌ unsafe (column) | skip |
| 8 | 2 | 3 | (2,3) = Q | third queen placed |
| 9 | 3 | 0 | (3,0) = Q → row = 4 → count++ | first solution found |
| … | … | … | backtrack removals restore board | continue exploring other columns |
| final | – | – | count = 2 | two distinct placements satisfy all rows |

The table stops after the first solution; the algorithm proceeds similarly to discover the second one, ending with `count = 2`.

## Complexity  
- **Time:** `O(N!)` in the worst case, because each row tries up to `N` columns and the recursion depth is `N`; the safety checks are `O(N)` but are dominated by the factorial branching.  
- **Space:** `O(N)` for the recursion stack and the `board` matrix (the output count is a single integer and does not affect the asymptotic bound).

## Solution (Java)

```java
class Solution {
    // Refer to N Queen 1 for better understanding as this is the same code but just replaced the List with a count variable
    private int count = 0;
    public int totalNQueens(int n) {
        
        // constructing the board
        char[][] board = new char[n][n];
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                board[i][j] = '.';
            }
        }

        backTrack(0, board, n);
        return count;
    }

    // BackTrack Function
    private void backTrack(int row, char[][] board, int n){
        // Base Condition
        if(row == n){
            count++;
            return;
        }

        // now we traverse for each column in this row 
        for(int col = 0; col < n; col++){
            // check if we can place a queen in this col
            if(isSafe(row, col, board, n)){
                board[row][col] = 'Q';
                backTrack(row + 1, board, n);
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

**Runtime** 2 ms (beats 47.8%) · **Memory** 42 MB (beats 57.4%)

<sub>Synced by AILeetHub on 2026-10-07.</sub>
