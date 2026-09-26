# 1807. Evaluate the Bracket Pairs of a String

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/evaluate-the-bracket-pairs-of-a-string/)

`Array` · `Hash Table` · `String`

## Intuition  
The key observation is that every bracket pair can be processed independently in a single left‑to‑right scan because brackets never nest. While walking the string, the moment we encounter an opening ‘(’ we can locate its matching ‘)’ by advancing a second pointer until the closing bracket appears; the substring between them is the exact key to look up. This eliminates the need for a second pass, a hash‑set of positions, or recursion that would otherwise be required to match pairs. The solution therefore follows the classic **two‑pointer scanning** pattern.

## Approach  
1. **Build a lookup table** – iterate over `knowledge` and insert each `[key, value]` into `map`.  
2. **Initialize** `StringBuilder ans` and index `i = 0`.  
3. **Outer loop** `while (i < s.length())` – invariant: all characters before `i` have already been appended to `ans`.  
   - **Case A – opening bracket** `if (s.charAt(i) == '(')`:  
     a. Set `j = i + 1`.  
     b. **Inner loop** `while (s.charAt(j) != ')')` – invariant: `j` points to the first character after the opening bracket that is not yet examined; when the loop exits `j` is the position of the matching ‘)’.  
     c. Extract the key with `s.substring(i+1, j)`.  
     d. If `map.containsKey(key)` append `map.get(key)` to `ans`; otherwise append `'?'`.  
     e. Advance `i` to `j + 1` so the next iteration starts after the closing bracket.  
   - **Case B – regular character** `else`: append `s.charAt(i)` to `ans` and increment `i`.  
4. After the loop finishes, return `ans.toString()`.

**Edge handling** – The code correctly processes an empty `knowledge` (the map stays empty) and strings that start or end with a bracket because `i` is moved past the closing bracket in step 3e. The inner loop uses `!= ')'` rather than `<` because the problem guarantees a matching closing bracket, so a simple equality check suffices and avoids off‑by‑one errors.

## Dry Run  

Input  
```
s = "(name)is(age)yearsold"
knowledge = [["name","bob"],["age","two"]]
```

| i (outer) | j (inner) | key extracted | ans after step | note |
|-----------|-----------|---------------|----------------|------|
| 0         | 5         | "name"        | "bob"          | '(' found, j stops at ')', replace with map value |
| 6         | –         | –             | "bobi"         | regular 'i' appended |
| 7         | –         | –             | "bobis"        | regular 's' appended |
| 8         | 12        | "age"         | "bobistwo"     | second '(' processed, replace with map value |
| 13        | –         | –             | "bobistwoy"    | regular 'y' |
| 14        | –         | –             | "bobistwoye"   | regular 'e' |
| 15        | –         | –             | "bobistwoyea"  | regular 'a' |
| 16        | –         | –             | "bobistwoyear" | regular 'r' |
| 17        | –         | –             | "bobistwoyears"| regular 's' |
| 18        | –         | –             | "bobistwoyears o"? actually continue until end → final "bobistwoyearsold" | remaining letters appended |

After the loop `ans` equals `"bobistwoyearsold"`, which is the required evaluated string.

## Complexity  
- **Time:** O(n + k) where *n* is `s.length()` and *k* is `knowledge.size()`. The outer scan visits each character once, and the inner scan collectively moves `j` across each bracket pair exactly once.  
- **Space:** O(k) for the hash map storing the knowledge pairs; the `StringBuilder` output is not counted against auxiliary space.

## Solution (Java)

```java
class Solution {
    public String evaluate(String s, List<List<String>> knowledge) {
        HashMap<String, String> map = new HashMap<>();

        for(List<String> curr : knowledge){
            map.put(curr.get(0), curr.get(1));
        }
        StringBuilder ans = new StringBuilder();
        int i = 0;
        while(i < s.length()){
            if(s.charAt(i) == '('){
                // finding the closing bracket )
                int j = i+1;
                while(s.charAt(j) != ')'){
                    j++;
                }
                String key = s.substring(i+1, j);
                if(map.containsKey(key))
                    ans.append(map.get(key));
                else 
                    ans.append('?');

                i = j + 1; 
            } else {
                ans.append(s.charAt(i));
                i++;
            }
        }

        return ans.toString();
    }
}
```

---

**Runtime** 32 ms (beats 91.0%) · **Memory** 91 MB (beats 65.2%)

<sub>Synced by AILeetHub on 2026-09-26.</sub>
