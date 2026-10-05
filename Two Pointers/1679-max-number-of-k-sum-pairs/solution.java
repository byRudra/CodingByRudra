// class Solution {
//     public int maxOperations(int[] nums, int k) {
//         HashMap<Integer, Integer> map = new HashMap<>();
//         int operations = 0;
//         for(int num : nums){
//             int compliment = k - num;
//             if(map.getOrDefault(compliment, 0) > 0){
//                 map.put(compliment, map.get(compliment) - 1);
//                 operations++;
//             }
//             else{
//                 map.put(num, map.getOrDefault(num, 0)  + 1);
//             }
//         } 
//         return operations;
//     }
// }

//  Better 
class Solution {
    public int maxOperations(int[] nums, int k) {
        Arrays.sort(nums);
        int operations = 0;
        int left = 0, right = nums.length - 1;
        while (left < right) {
            int sum = nums[left] + nums[right];
            if (sum == k) {
                operations++;
                left++;
                right--;
            } else if (sum > k) {
                right--;
            } else {
                left++;
            }
        }
        return operations;
    }
}