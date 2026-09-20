class Solution {
    public int[][] insert(int[][] intervals, int[] newInterval) {
        List<int[]> result = new ArrayList<>();
        int start = 0, n = intervals.length;
        // Skip intervals that are shorter
        while (start < n && intervals[start][1] < newInterval[0]) {
            result.add(intervals[start++]);
        }
        // Now can add the interval but there can be overlapping intervals too soo include them
        while (start < n && intervals[start][0] <= newInterval[1]) {
            newInterval[0] = Math.min(newInterval[0], intervals[start][0]);
            newInterval[1] = Math.max(newInterval[1], intervals[start][1]);
            start++;
        }
        result.add(newInterval);
        // Now for the bigger intervals
        while (start < n) {
            result.add(intervals[start++]);
        }
        return result.toArray(new int[result.size()][]);

    }
}