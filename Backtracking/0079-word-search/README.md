# 79. Word Search

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/word-search/)

`Array` · `String` · `Backtracking` · `Depth-First Search` · `Matrix`

## Intuition  
The key observation is that building the word is a path‑finding problem: each character must be taken from a cell that is orthogonally adjacent to the previous one, and a cell cannot be reused. If we walk the board depth‑first, marking a cell as “used” while we are on it, we can explore every possible continuation without ever revisiting the same cell in the current branch. A naïve solution would try all permutations of cells, costing exponential time and requiring extra structures to remember visited cells. By mutating the board in‑place (temporarily replacing a character with a sentinel) we eliminate the extra visited‑set and keep the search linear in the number of recursive calls.

## Approach  
1. Convert `word` to a `char[] words` for O(1) index access.  
2. Iterate over every board position `(i, j)`.  
   * **Loop invariant:** before each iteration, no state from previous start positions is retained because the board is restored after each DFS.  
   * If `board[i][j]` matches `words[0]`, launch a DFS from that cell.  
3. `exists(board, word, idx, i, j)` performs the recursive search.  
   * **Exit conditions:**  
     - Out‑of‑bounds (`i < 0 || i >= board.length || j < 0 || j >= board[0].length`).  
     - Current cell character does not equal `word[idx]`.  
   * **Success condition:** `idx == word.length‑1` → the last character matched, return `true`.  
   * **Marking:** store the original character in `temp`, then set `board[i][j] = '#'` to forbid reuse in this branch.  
   * Recurse in the four orthogonal directions with `idx+1`. The invariant during recursion is that all cells on the current path are marked, and all others retain their original letters.  
   * After exploring, restore the cell with `board[i][j] = temp` so other branches see the correct board.  
4. The DFS returns `true` if any direction yields a complete match; otherwise it returns `false` and the outer loops continue searching other start cells.  

## Dry Run  
**Input**  
```
board = [
  ['A','B','C','E'],
  ['S','F','C','S'],
  ['A','D','E','E']
]
word = "ABCCED"
```

| step | idx | i | j | board[i][j] (after marking) | note |
|------|-----|---|---|-----------------------------|------|
| 1    | 0   | 0 | 0 | '#'                         | start at 'A', mark |
| 2    | 1   | 0 | 1 | '#'                         | move right to 'B', mark |
| 3    | 2   | 0 | 2 | '#'                         | move right to first 'C', mark |
| 4    | 3   | 1 | 2 | '#'                         | move down to second 'C', mark |
| 5    | 4   | 2 | 2 | '#'                         | move down to 'E', mark |
| 6    | 5   | 2 | 1 | '#'                         | move left to 'D', idx == last → return true |
| 7    | –   | – | – | board restored stepwise    | unwind recursion, all marks cleared |
| 8    | –   | – | – | final result = true        | first successful path found |

The DFS reaches `idx == 5` (last character) at cell `(2,1)`, confirming that “ABCCED” exists.

## Complexity  
- **Time:** O(m · n · 4^L) in the worst case, where `L = word.length`. Each cell can be a start point (m·n) and the recursive search explores up to four directions per character, but pruning stops branches as soon as a mismatch occurs.  
- **Space:** O(L) auxiliary stack space for recursion, because the board is modified in‑place and no extra data structures proportional to the board size are allocated. (The output is a single boolean, not counted.)

## Solution (Java)

```java
class Solution {
    public boolean exist(char[][] board, String word) {
        char[] words = word.toCharArray();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (words[0] == board[i][j] && exists(board, words, 0, i, j))
                    return true;
            }
        }
        return false;
    }

    private boolean exists(char[][] board, char[] word, int idx, int i, int j) {
        if (i >= board.length || j >= board[0].length || j < 0 || i < 0 || word[idx] != board[i][j])
            return false;
        if (idx == word.length - 1)
            return true;
        char temp = board[i][j];
        board[i][j] = '#';

        boolean result = exists(board, word, idx + 1, i + 1, j) ||
                exists(board, word, idx + 1, i - 1, j) ||
                exists(board, word, idx + 1, i, j + 1) ||
                exists(board, word, idx + 1, i, j - 1);
        board[i][j] = temp;

        return result;
    }
}
```

---

**Runtime** 106 ms (beats 94.2%) · **Memory** 43.2 MB (beats 28.6%)

<sub>Synced by AILeetHub on 2026-10-04.</sub>
