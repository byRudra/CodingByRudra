class Solution {
    public List<Integer> findDuplicates(int[] nums) {
        int num[] = new int[nums.length + 1];
        for(int nu : nums){
            num[nu]++;
        }
        List<Integer> result = new ArrayList<>();
        for(int n = 0; n < nums.length + 1; n++){
            if(num[n] == 2) result.add(n);
        }
        return result;
    }
}