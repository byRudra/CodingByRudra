# 380. Insert Delete GetRandom O(1)

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/insert-delete-getrandom-o1/)

`Array` · `Hash Table` · `Math` · `Design` · `Randomized`

## Intuition  
The key to constant‑time operations is to keep the elements in a dense array **and** know each element’s position. If we store every value in an `ArrayList` and maintain a `HashMap` from value → its index, we can locate any element instantly. The naïve deletion from an array costs O(n) because all trailing items must shift; the insight that removes this cost is to overwrite the target slot with the **last** element and then pop the tail. This single swap preserves the array’s compactness while the map is updated to reflect the moved element’s new index. The pattern is the classic “array + hash‑map for O(1) set with random access”.

## Approach  
1. **Construction** – Initialise an empty `list`, an empty `map`, and a `Random` generator.  
2. **insert(val)**  
   - Check `if (map.containsKey(val))` → return `false` (value already present).  
   - Append `val` to `list` (`list.add(val)`).  
   - Record its index: `map.put(val, list.size() - 1)`.  
   - Return `true`.  
   *Invariant*: after insertion, every key in `map` points to the exact position of that key in `list`.  
3. **remove(val)**  
   - If `!map.containsKey(val)` → return `false` (nothing to delete).  
   - Retrieve the index of `val`: `int index = map.get(val)`.  
   - Grab the last element: `int last = list.get(list.size() - 1)`.  
   - Overwrite the slot `index` with `last` (`list.set(index, last)`).  
   - Update the moved element’s mapping: `map.put(last, index)`.  
   - Trim the tail (`list.remove(list.size() - 1)`).  
   - Erase `val` from the map (`map.remove(val)`).  
   - Return `true`.  
   *Invariant*: the array stays contiguous; every remaining key’s index stays correct.  
4. **getRandom()**  
   - Generate a uniform index `int idx = random.nextInt(list.size())`.  
   - Return `list.get(idx)`.  
   *Invariant*: because `list` contains exactly the current set elements, each index is equally likely, guaranteeing uniform randomness.

## Dry Run  
Input sequence: `insert(1) → insert(2) → remove(1) → getRandom()`

| Step | list (order) | map (val→idx) | Note |
|------|--------------|--------------|------|
| insert(1) | [1] | {1→0} | added 1 at tail |
| insert(2) | [1,2] | {1→0, 2→1} | added 2 |
| remove(1) | [2] | {2→0} | swapped last (2) into index 0, popped tail, removed 1 |
| getRandom() | [2] | {2→0} | only element, index 0 returned |

After the removal, the list contains a single element `2`, and the map correctly maps `2` to index 0, so `getRandom()` inevitably returns `2`.

## Complexity  
- **Time:** O(1) average for each method – `insert` and `remove` perform a constant number of hashmap lookups and list operations; `remove` runs in O(1) because it swaps with the last element instead of shifting, and `getRandom` samples an index in constant time.  
- **Space:** O(n) where *n* is the number of stored values – the `list` and `map` each hold one entry per element, and no extra auxiliary structures are allocated beyond these. (The output of `getRandom` does not affect the asymptotic space.)

## Solution (Java)

```java
class RandomizedSet {

    ArrayList<Integer> list;
    HashMap<Integer, Integer> map;
    Random random;

    public RandomizedSet() {
        list = new ArrayList<>();
        map = new HashMap<>();
        random = new Random();
    }
    
    public boolean insert(int val) {

        // Already exists
        if (map.containsKey(val)) {
            return false;
        }

        // Add value
        list.add(val);

        // Store its index
        map.put(val, list.size() - 1);

        return true;
    }
    
    public boolean remove(int val) {

        // Doesn't exist
        if (!map.containsKey(val)) {
            return false;
        }

        int index = map.get(val);

        // Get last element
        int last = list.get(list.size() - 1);

        // Move last element to the position of val
        list.set(index, last);

        // Update last element's index
        map.put(last, index);

        // Remove last position
        list.remove(list.size() - 1);

        // Remove val from map
        map.remove(val);

        return true;
    }
    
    public int getRandom() {
        int index = random.nextInt(list.size());
        return list.get(index);
    }
}

/**
 * Your RandomizedSet object will be instantiated and called as such:
 * RandomizedSet obj = new RandomizedSet();
 * boolean param_1 = obj.insert(val);
 * boolean param_2 = obj.remove(val);
 * int param_3 = obj.getRandom();
 */
```

---

**Runtime** 26 ms (beats 74.6%) · **Memory** 100.5 MB (beats 52.0%)

<sub>Synced by AILeetHub on 2026-10-01.</sub>
