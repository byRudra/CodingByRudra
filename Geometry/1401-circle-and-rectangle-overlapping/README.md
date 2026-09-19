# 1401. Circle and Rectangle Overlapping

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/circle-and-rectangle-overlapping/)

`Math` · `Geometry`

## Intuition  
The key observation is that the point of the rectangle that can be nearest to the circle’s centre is obtained by clamping the centre’s coordinates to the rectangle’s bounds. In other words, if we “push” the centre horizontally into the interval \[x1, x2\] and vertically into \[y1, y2\], the resulting point (closestX, closestY) is the rectangle point with minimal Euclidean distance to the centre. The naive way would be to enumerate every integer point inside the rectangle or to test many candidate points, which would be O(area) and unnecessary. By reducing the problem to a single distance computation we avoid any extra pass, any hash‑map, and any geometry library. This is a classic **clamping** (or “closest point on AABB to a point”) pattern.

## Approach  
1. **Clamp the x‑coordinate**  
   `closestX = Math.max(x1, Math.min(xCenter, x2));`  
   *Exit condition*: none (single expression).  
   *Invariant*: after evaluation, `closestX` lies in \[x1, x2\] and is the nearest x‑value of the rectangle to `xCenter`.  
2. **Clamp the y‑coordinate**  
   `closestY = Math.max(y1, Math.min(yCenter, y2));`  
   Same invariant holds for the y‑axis.  
3. **Compute the offset vector**  
   `dx = xCenter - closestX;`  
   `dy = yCenter - closestY;`  
   These differences are the horizontal and vertical distances from the centre to the closest rectangle point.  
4. **Check squared distance against squared radius**  
   `return dx * dx + dy * dy <= radius * radius;`  
   The inequality is inclusive because a point on the boundary counts as overlap.  
   *Edge‑case handling*:  
   - If the centre lies inside the rectangle, both clamps return the centre itself, yielding `dx = dy = 0`, so the condition is always true.  
   - When the centre is exactly on a rectangle edge, the clamp picks that edge coordinate, again giving the correct zero or minimal distance.  
   - The code uses `<=` rather than `<` to treat touching as overlapping, matching the problem definition.  
   - All arithmetic stays within 32‑bit signed range because the constraints guarantee \|dx\|, \|dy\| ≤ 2·10⁴, and squaring fits in an `int`.

## Dry Run  
**Input**: `radius = 1, xCenter = 0, yCenter = 0, x1 = 1, y1 = -1, x2 = 3, y2 = 1`

| Step | closestX | closestY | dx | dy | Note |
|------|----------|----------|----|----|------|
| 1 – clamp X | `Math.min(0,3)=0` → `Math.max(1,0)=1` | – | – | – | X is left of rectangle, pushed to left edge (1) |
| 2 – clamp Y | – | `Math.min(0,1)=0` → `Math.max(-1,0)=0` | – | – | Y already inside vertical span, stays 0 |
| 3 – offsets | – | – | `0‑1 = -1` | `0‑0 = 0` | Horizontal distance 1, vertical distance 0 |
| 4 – check | – | – | – | – | `(-1)² + 0² = 1 ≤ 1²` → **true** |

The algorithm ends with `true` because the rectangle’s left edge touches the circle at (1, 0).

## Complexity  
- **Time:** O(1) – only a constant number of arithmetic and `Math` calls; no loops or recursion.  
- **Space:** O(1) – only a handful of primitive variables are allocated, independent of input size. (The output boolean is not counted.)

## Solution (Java)

```java
class Solution {
    public boolean checkOverlap(int radius, int xCenter, int yCenter, int x1, int y1, int x2, int y2) {
        int closestX = Math.max(x1, Math.min(xCenter, x2));
        int closestY = Math.max(y1, Math.min(yCenter, y2));

        int dx = xCenter - closestX;
        int dy = yCenter - closestY;

        return dx * dx + dy * dy <= radius * radius;
    }
}
```

---

**Runtime** 0 ms (beats 100.0%) · **Memory** 42.1 MB (beats 77.3%)

<sub>Synced by AILeetHub on 2026-09-19.</sub>
