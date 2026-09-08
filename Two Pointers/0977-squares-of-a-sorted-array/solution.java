// O(n logn)
// class Solution {
//     public int[] sortedSquares(int[] nums) {
//         for(int i = 0; i < nums.length; i++){
//             nums[i] *= nums[i];
//         }
//         Arrays.sort(nums);
//         return nums;
//     }
// }

// O(n)
class Solution {
    public int[] sortedSquares(int[] nums) {
        int ans[] = new int[nums.length];

        int left = 0;
        int right = nums.length - 1;
        for(int i = nums.length - 1; i >= 0; i--){
            int leftSquare = nums[left] * nums[left];
            int rightSquare = nums[right] * nums[right];

            if(leftSquare > rightSquare){
                ans[i] = leftSquare;
                left++;
            }
            else{
                ans[i] = rightSquare;
                right--;
            }
        }
        return ans;
    }
}