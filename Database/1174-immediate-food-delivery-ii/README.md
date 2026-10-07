# 1174. Immediate Food Delivery II

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/immediate-food-delivery-ii/)

`Database`

## Intuition  
The key observation is that a customer’s first order is uniquely identified by the smallest `order_date` for that `customer_id`. Once we isolate those rows, the condition `order_date = customer_pref_delivery_date` is a Boolean that evaluates to 1 for an immediate order and 0 otherwise. Averaging these 0/1 values gives the fraction of immediate first orders, and scaling by 100 yields the required percentage. A naïve solution might scan the table multiple times—first to collect each customer’s orders, then to compare dates—incurring extra passes or temporary structures. By leveraging a single sub‑query to fetch the minimal dates and using MySQL’s ability to treat Boolean expressions as numeric, we collapse everything into one pass. This follows the **two‑step filter‑then‑aggregate** pattern.

## Approach  
1. **Identify first orders** –  
   ```sql
   SELECT customer_id, MIN(order_date) FROM Delivery GROUP BY customer_id
   ```  
   *Exit condition*: the `GROUP BY` finishes after processing all rows.  
   *Invariant*: for each `customer_id` seen so far, the stored `MIN(order_date)` is the earliest order date encountered.  
2. **Filter the original table** –  
   ```sql
   WHERE (customer_id, order_date) IN ( …subquery… )
   ```  
   The `IN` clause keeps only rows whose `(customer_id, order_date)` pair matches one of the `(customer_id, MIN(order_date))` pairs from step 1.  
   *Edge handling*: If a customer has only one row, that row automatically satisfies the condition; empty tables produce no rows, and the final `AVG` returns `NULL` (which rounds to `0.00`).  
3. **Compute the immediate flag** –  
   The expression `order_date = customer_pref_delivery_date` yields `1` when the dates are equal (immediate) and `0` otherwise (scheduled).  
4. **Aggregate to a percentage** –  
   ```sql
   ROUND(AVG(order_date = customer_pref_delivery_date) * 100, 2) AS immediate_percentage
   ```  
   `AVG` sums the 0/1 flags and divides by the count of filtered rows, i.e., the number of customers. Multiplying by 100 converts the fraction to a percentage, and `ROUND(...,2)` formats it to two decimal places.

## Dry Run  
**Input**  

| delivery_id | customer_id | order_date | customer_pref_delivery_date |
|------------|-------------|------------|-----------------------------|
| 1          | 1           | 2021‑01‑01 | 2021‑01‑02 |
| 2          | 2           | 2021‑01‑03 | 2021‑01‑03 |
| 3          | 1           | 2021‑01‑05 | 2021‑01‑06 |
| 4          | 3           | 2021‑01‑02 | 2021‑01‑02 |

**Execution trace**

| step | subquery result (customer_id, min_date) | row kept? (matches?) | flag (`order_date = pref_date`) | note |
|------|------------------------------------------|----------------------|--------------------------------|------|
| 1    | (1, 2021‑01‑01), (2, 2021‑01‑03), (3, 2021‑01‑02) | – | – | Subquery finishes after scanning all rows. |
| 2    | – | Row 1 matches (1, 2021‑01‑01) | 0 | Scheduled first order for customer 1. |
| 3    | – | Row 2 matches (2, 2021‑01‑03) | 1 | Immediate first order for customer 2. |
| 4    | – | Row 3 does **not** match (order_date is later) | – | Ignored; not a first order. |
| 5    | – | Row 4 matches (3, 2021‑01‑02) | 1 | Immediate first order for customer 3. |

**Aggregation**  
`AVG = (0 + 1 + 1) / 3 = 0.6667`.  
`0.6667 * 100 = 66.67`.  
Rounded to two decimals → **66.67**.

The final state is a single row with `immediate_percentage = 66.67`, correctly reflecting that two out of three customers placed immediate first orders.

## Complexity  
- **Time:** O(N) – the `GROUP BY` scans the table once to compute minima, and the outer query scans it again for the `IN` filter; both passes are linear in the number of rows N.  
- **Space:** O(C) – only the set of `(customer_id, MIN(order_date))` pairs for the C distinct customers is stored; no additional structures proportional to N are allocated. (The output column itself is not counted.)

## Solution (MySQL)

```sql
# Write your MySQL query statement below
SELECT
ROUND(
    AVG(order_date = customer_pref_delivery_date) * 100,
    2
) AS immediate_percentage
FROM Delivery
WHERE (customer_id, order_date) IN (
    SELECT 
        customer_id,
        MIN(order_date)
        FROM Delivery
        GROUP BY customer_id
)
```

---

**Runtime** 808 ms (beats 23.9%) · **Memory** 0B (beats 100.0%)

<sub>Synced by AILeetHub on 2026-10-07.</sub>
