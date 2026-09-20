class Solution {
    public int findMinArrowShots(int[][] intervals) {
        int count = 0;
        Arrays.sort(intervals, (a,b) -> Integer.compare(a[1], b[1]));
        int prevEnd = intervals[0][1];
        for(int i = 1; i < intervals.length; i++){
            if(prevEnd >= intervals[i][0]){
                count ++;
                prevEnd = Math.min(prevEnd, intervals[i][1]);
            }
            else{
                prevEnd =  intervals[i][1];
            }
        }
        return intervals.length - count;
    }
}