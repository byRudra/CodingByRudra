class Solution {
    public int[] numSmallerByFrequency(String[] queries, String[] words) {
        int ans[] = new int[queries.length];
        int freq[] = new int[words.length];

        for(int i = 0; i < words.length; i++){
            freq[i] = f(words[i]);
        }

        for(int i = 0; i < queries.length; i++){
            int q = f(queries[i]);
            int count = 0;

            for(int j = 0; j < words.length; j++){
                if(freq[j] > q){
                    count++;
                }
            }
            ans[i] = count;
        }
        return ans;
    }

    private int f(String word){
        char min = 'z';
        int count = 0;
        for(char c  : word.toCharArray()){
            if(c < min){
                min = c;
                count = 1;
            }else if(c == min){
                count++;
            }
        }
        return count;
    }
}