class Solution {
    public boolean isPossibleDivide(int[] nums, int k) {
        if(nums.length % k != 0) return false;
        
        TreeMap<Integer, Integer> map = new TreeMap<>();
        // Counting each element
        for(int num : nums) map.put(num, map.getOrDefault(num, 0) + 1);

        while(!map.isEmpty()){
            int start = map.firstKey();
            for(int i = start; i < start + k; i++){
                if(!map.containsKey(i)) return false;
                int current = map.get(i);
                if(current == 1) map.remove(i);
                else map.put(i, current - 1);
            }
        }
        
        return true;
    }
}