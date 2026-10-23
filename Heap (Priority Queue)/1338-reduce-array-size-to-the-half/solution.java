class Solution {
    public int minSetSize(int[] arr) {
        int n = arr.length;
        // count how many times a value appeas
        int count[] = new int[100001];
        for(int num : arr) count[num]++;
        
        // counts how many values appear at a give count example 3 and 2 appears 5 times so freq[5] = 2;
        int freq[] = new int[n + 1];
        for(int c : count) if(c > 0) freq[c]++;

        int removed = 0, size = 0;
        for(int f = n; f >= 1 && removed < n/2; f--){
            while(freq[f] > 0 && removed < n/2){
                removed += f;
                size++;
                freq[f]--;
            }
        }
        return size;

    }
}