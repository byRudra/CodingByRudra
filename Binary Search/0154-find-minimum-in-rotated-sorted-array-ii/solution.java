// class Solution {
//     public int findMin(int[] nums) {
//         int min = Integer.MAX_VALUE;
//         for(int num : nums){
//             min = Math.min(min, num);
//         }
//         return min;
//     }
// }

// Using Binary Search

class Solution {
    public int findMin(int[] nums) {
        int left = 0, right = nums.length - 1;
        while (left < right) {

            int mid = left + (right - left) / 2;

            if (nums[mid] > nums[right]) {
                left = mid + 1;
            } else if (nums[mid] < nums[right]) {
                right = mid;
            } else {
                right--;
            }
        }
        return nums[left];
    }
}