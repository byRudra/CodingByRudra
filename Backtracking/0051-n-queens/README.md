# 51. N-Queens

![Hard](https://img.shields.io/badge/Difficulty-Hard-ff375f?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/n-queens/)

`Array` · `Backtracking` · `Algorithm X`

## Intuition  
When we place queens row by row, any queen that has already been placed can only threaten cells in the rows above the current one. Therefore, after we have fixed the first *k* rows, the safety of a candidate column in row *k* depends solely on those *k* rows; we never need to look ahead. This observation eliminates the need for a second pass, a global hash map, or exhaustive enumeration of all board configurations. By pruning illegal placements immediately, the search space shrinks dramatically, and a simple depth‑first traversal suffices. The overall pattern is classic **backtracking with pruning**.

## Approach  
1. **Initial board construction** – Create an `n×n` char matrix `board` filled with `'.'`.  
2. **Recursive entry** – Call `backTrack(0, board, result, n)`. The parameter `row` denotes the next row to fill.  
3. **Base case** – If `row == n`, every row contains a queen; convert each `char[]` in `board` to a `String`, collect them into `candidate`, and add `candidate` to `result`.  
4. **Column iteration** – For the current `row`, iterate `col` from `0` to `n‑1`.  
   - **Invariant**: before each iteration, all rows `< row` already satisfy the N‑Queens constraints.  
5. **Safety check** – Invoke `isSafe(row, col, board, n)`.  
   - **Column check** scans rows `0 … row‑1` in column `col`.  
   - **Upper‑left diagonal** walks `(i‑1, j‑1)` while `i ≥ 0 && j ≥ 0`.  
   - **Upper‑right diagonal** walks `(i‑1, j+1)` while `i ≥ 0 && j < n`.  
   - If any queen `'Q'` is found, the placement is illegal and the loop proceeds to the next `col`.  
6. **Place queen** – Set `board[row][col] = 'Q'`.  
7. **Recurse** – Call `backTrack(row + 1, board, result, n)` to fill the next row.  
8. **Backtrack** – After the recursive call returns, reset `board[row][col] = '.'` to undo the decision and try the next column.  
9. **Edge handling** – The algorithm naturally handles `n = 1` (the first placement succeeds) and returns an empty list for `n = 2` or `3` because the loop exhausts all columns without reaching the base case.

## Dry Run  
**Input:** `n = 4`  

| Step | row | col tried | board change (row,col) | Note |
|------|-----|-----------|------------------------|------|
| 1 | 0 | 0 | `board[0][0] = 'Q'` | First queen placed at (0,0) |
| 2 | 1 | 0 | `isSafe` fails (same column) | skip |
| 3 | 1 | 1 | `isSafe` fails (diag left) | skip |
| 4 | 1 | 2 | `board[1][2] = 'Q'` | second queen placed |
| 5 | 2 | 0 | `isSafe` fails (diag left) | skip |
| 6 | 2 | 1 | `isSafe` fails (diag right) | skip |
| 7 | 2 | 2 | `isSafe` fails (column) | skip |
| 8 | 2 | 3 | `board[2][3] = 'Q'` | third queen placed |
| 9 | 3 | 0‑2 | all `isSafe` fail | backtrack: remove (2,3) |
|10 | 2 | 3 (retry) | backtrack removes (1,2) | try next column in row 1 |
|…| … | … | … | continues until a full board is found (`[".Q..","...Q","Q...","..Q."]`).  

When `row` finally reaches `4`, the board represents a valid solution and is added to `result`. The same process discovers the second solution.

## Complexity  
- **Time:** O(N!), because each row tries up to N columns and the depth‑first search explores only the permutations that survive the `isSafe` pruning; the worst‑case number of recursive calls is proportional to the factorial of N.  
- **Space:** O(N²) for the `board` matrix plus O(N) recursion stack depth; the output list itself is not counted in the auxiliary space.

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

**Runtime** 2 ms (beats 81.8%) · **Memory** 46.3 MB (beats 90.9%)

<sub>Synced by AILeetHub on 2026-10-07.</sub>
