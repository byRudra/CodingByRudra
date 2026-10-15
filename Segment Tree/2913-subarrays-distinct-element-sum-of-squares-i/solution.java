// class Solution {
//     public int sumCounts(List<Integer> nums) {
//         List<List<Integer>> subArrays = findSubarrays(nums);
//         List<Integer> uniqueList = uniqueValues(subArrays);
//         int result = squareOfNums(uniqueList);
//         return result;

//     }
//     public int squareOfNums(List<Integer> uniqueList){
//         int result = 0;
//         for(int num : uniqueList){
//             result += num * num;
//         }
//         return result;
//     }
//     public List uniqueValues(List<List<Integer>> subArrays){
//         List<Integer> result = new ArrayList<>();
//         for(List<Integer> current : subArrays){
//             Set<Integer> set = new HashSet<>();
//             for(int num : current){
//                 set.add(num);
//             }
//             result.add(set.size());
//         }

//         return result;
//     }
//     public List findSubarrays(List<Integer> nums){
//         List<List<Integer>> result = new ArrayList<>();
//         int left = 0;
//         while(left < nums.size()){
//             List<Integer> candidate = new ArrayList<>();
//             for(int right = left; right < nums.size(); right++){
//                 candidate.add(nums.get(right));
//                 result.add(new ArrayList<>(candidate));
//             }
//             left++;
//         }
//         return result;
//     }
// }
class Solution {
    public int sumCounts(List<Integer> nums) {
        int result = 0;
        int n = nums.size();

        for (int i = 0; i < n; i++) {
            int count = 0;
            boolean[] visited = new boolean[101];
            for (int j = i; j < n; j++) {
                int num = nums.get(j);
                if (!visited[num]) {
                    visited[num] = true;
                    count++;
                }
                result += count * count;
            }

        }

        return result;
    }
}