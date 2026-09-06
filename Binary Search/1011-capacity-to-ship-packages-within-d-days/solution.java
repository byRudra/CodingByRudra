class Solution {
    public int shipWithinDays(int[] weights, int days) {
        int minCapacity = 0, maxCapacity = 0;
        for(int weight : weights){
            minCapacity = Math.max(weight, minCapacity);
            maxCapacity += weight;
        }

        while(minCapacity < maxCapacity){
            int mid = minCapacity + (maxCapacity - minCapacity) / 2;

            int sum = 0, day = 1;
            for(int weight : weights){
                if(sum + weight > mid){
                    day++;
                    sum = 0;
                }
                sum += weight;
            }

            if(day > days){
                minCapacity = mid + 1;
            }
            else{
                maxCapacity = mid;
            }
        }
        return minCapacity;
    }
}