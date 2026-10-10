// TLE
// class Solution {
//     public long minSumSquareDiff(int[] nums1, int[] nums2, int k1, int k2) {
//         // making a priority queue as we have to minimize the res 
//         PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
//         long operations = (long) k1 + k2;
//         int total = 0;
//         for(int i = 0; i < nums1.length; i++){
//             int diff = Math.abs(nums1[i] - nums2[i]);
//             total += diff;
//             pq.offer(diff);
//         }
//         if(operations >= total) return 0;
//         // Minimizing
//         while(operations != 0 && !pq.isEmpty()){
//             int diff = pq.poll();
//             if (diff == 0) break;
//             pq.offer(diff - 1);
//             operations--;
//         }

//         long answer = 0;
//         while(!pq.isEmpty()){
//             long diff = pq.poll();
//             answer += diff * diff;
//         }
//         return answer;
//     }
// }

// Better Approach

class Solution {
    public long minSumSquareDiff(int[] nums1, int[] nums2, int k1, int k2) {
        int n = nums1.length;
        int []diff = new int[n];
        long totalDiff = 0;
        int maxDiff = 0;
        for(int i = 0; i < n; i++){
            diff[i] = Math.abs(nums1[i] - nums2[i]);
            totalDiff += diff[i];
            maxDiff = Math.max(maxDiff, diff[i]);
        }

        long operations = (long) k1 + k2;
        if(operations >= totalDiff) return 0;

        long[] count = new long[maxDiff + 1];
        for(int d : diff) count[d]++;

        for(int i = maxDiff; i > 0; i--){
            if(count[i] == 0) continue;
            long reduce = Math.min(operations, count[i]);
            count[i] -= reduce;
            count[i - 1] += reduce;
            operations -= reduce;

            if(operations == 0) break;
        }

        long res = 0;
        for(int i = 1; i <= maxDiff; i++){
            if(count[i] > 0) res += count[i] * (long) i * i;
        }
        return res;
    }
}