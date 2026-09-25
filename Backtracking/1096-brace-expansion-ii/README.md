# 1096. Brace Expansion II

![Hard](https://img.shields.io/badge/Difficulty-Hard-ff375f?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/brace-expansion-ii/)

`Hash Table` · `String` · `Backtracking` · `Stack` · `Breadth-First Search` · `Sorting`

## Intuition  
The expression can be viewed as a hierarchy of three operations: union (comma), concatenation (adjacent factors), and atomic literals (letters or nested braces). If we walk the string left‑to‑right while maintaining a cursor `i`, each time we finish parsing a sub‑expression we already know the exact set of words it denotes, so we never need a second pass or an auxiliary data structure such as a hash map of positions. The key observation is that **the grammar is LL(1)** – the next character uniquely determines whether we are about to read a union separator, a closing brace, or another factor – which lets a simple recursive‑descent parser compute the result on the fly. This eliminates the need for explicit stacks or backtracking beyond the natural recursion.

## Approach  
1. **Initialize global state** – store the input string in `s` and set index `i = 0`.  
2. **parseExpression** (`expression = term (',' term)*`)  
   - Call `parseTerm` to obtain the first term’s set.  
   - While the current character is a comma, advance `i` (`i++`) and union (`addAll`) the next term’s set.  
   - Exit when `i` reaches the end or a closing brace `}`; the invariant is that `result` always holds the union of all terms parsed so far.  
3. **parseTerm** (`term = factor factor …`) – handles concatenation.  
   - Start with `result = {""}` (empty string) so the first factor can be concatenated directly.  
   - Loop while `i` is inside the string and the next char is neither `}` nor `,`.  
   - For each iteration, obtain `factor = parseFactor()` and replace `result` with `multiply(result, factor)`.  
   - The loop invariant: `result` equals the Cartesian product of all factors processed up to the current iteration.  
4. **parseFactor** – distinguishes a single letter from a nested brace expression.  
   - If `s.charAt(i) == '{'`, skip the opening brace (`i++`), recursively call `parseExpression` to get the inner set, then skip the matching `}` (`i++`).  
   - Otherwise, create a singleton set containing the current letter, advance `i` by one.  
   - This step guarantees correct handling of empty or single‑character inputs and respects the grammar’s precedence (braces bind tighter than concatenation).  
5. **multiply** – computes the Cartesian product of two sets by concatenating every string `x` from set `a` with every string `y` from set `b`, storing results in a new `HashSet` to automatically discard duplicates.  

The recursion naturally respects nesting depth, and the use of `HashSet` guarantees each word appears at most once without extra bookkeeping.

## Dry Run  

Expression: `{a,b}{c,{d,e}}`

| Iter | i (char) | Action                              | result (term)                     | note                              |
|------|----------|-------------------------------------|-----------------------------------|-----------------------------------|
| 1    | 0 (`{`)  | parseFactor → recurse expression    | `{a,b}` → `{"a","b"}`              | entered first brace block         |
| 2    | 5 (`}`)  | exit inner parseExpression          | return `{"a","b"}`                | union of two letters              |
| 3    | 6 (`{`)  | parseFactor → recurse expression    | `{c,{d,e}}` → `{"c","d","e"}`      | second brace block                |
| 4    | 13 (`}`) | exit inner parseExpression          | return `{"c","d","e"}`            | union of c and inner set {d,e}    |
| 5    | end      | multiply first and second sets      | `multiply({"a","b"}, {"c","d","e"})` → `{"ac","ad","ae","bc","bd","be"}` | final concatenation result        |

The parser finishes with the set `{"ac","ad","ae","bc","bd","be"}`, which after sorting yields the required output.

## Complexity  
- **Time:** O(N · M) where N is the length of the expression (≤ 60) and M is the total number of distinct words generated; each character is examined once, and each multiplication iterates over the current Cartesian product sizes.  
- **Space:** O(M) for the sets that store intermediate and final results (the recursion stack uses O(depth) ≤ O(N), which is dominated by the output set size).

## Solution (Java)

```java
class Solution {
    private String s;
    private int i;

    public List<String> braceExpansionII(String expression) {
        s = expression;
        i = 0;

        Set<String> result = parseExpression();

        List<String> ans = new ArrayList<>(result);
        Collections.sort(ans);

        return ans;
    }

    // expression = term (',' term)*
    private Set<String> parseExpression() {
        Set<String> result = parseTerm();

        while (i < s.length() && s.charAt(i) == ',') {
            i++; // skip comma
            result.addAll(parseTerm());
        }

        return result;
    }

    // term = factor factor factor...
    // Concatenation
    private Set<String> parseTerm() {
        Set<String> result = new HashSet<>();
        result.add("");

        while (i < s.length()
                && s.charAt(i) != '}'
                && s.charAt(i) != ',') {

            Set<String> factor = parseFactor();

            result = multiply(result, factor);
        }

        return result;
    }

    // factor = letter OR '{' expression '}'
    private Set<String> parseFactor() {
        if (s.charAt(i) == '{') {
            i++; // skip '{'

            Set<String> result = parseExpression();

            i++; // skip '}'

            return result;
        }

        // single character
        Set<String> result = new HashSet<>();
        result.add(String.valueOf(s.charAt(i)));

        i++;

        return result;
    }

    // Cartesian product = concatenation
    private Set<String> multiply(Set<String> a, Set<String> b) {
        Set<String> result = new HashSet<>();

        for (String x : a) {
            for (String y : b) {
                result.add(x + y);
            }
        }

        return result;
    }
}
```

---

**Runtime** 9 ms (beats 82.4%) · **Memory** 47.2 MB (beats 64.0%)

<sub>Synced by AILeetHub on 2026-09-25.</sub>
