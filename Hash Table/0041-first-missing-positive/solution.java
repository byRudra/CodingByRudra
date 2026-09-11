class Solution {
    public int firstMissingPositive(int[] nums) {
        int firstMissingpositive = -1;
        int index = 1;
        HashSet<Integer> set = new HashSet<>();
        for(int num : nums){
            set.add(num);
        }
        for(int i = 0; i <= set.size(); i++){
            if(!set.contains(index))
                return index;
            index++;
        }
        return 0;
    }
}