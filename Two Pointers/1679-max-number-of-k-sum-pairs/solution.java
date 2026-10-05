class Solution {
    public int maxOperations(int[] nums, int k) {
        HashMap<Integer, Integer> map = new HashMap<>();
        int operations = 0;
        for(int num : nums){
            int compliment = k - num;
            if(map.getOrDefault(compliment, 0) > 0){
                map.put(compliment, map.get(compliment) - 1);
                operations++;
            }
            else{
                map.put(num, map.getOrDefault(num, 0)  + 1);
            }
        } 
        return operations;
    }
}