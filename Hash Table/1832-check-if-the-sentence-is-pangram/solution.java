class Solution {
    public boolean checkIfPangram(String sentence) {
        boolean[] isPangram = new boolean[26];

        int size = sentence.length();
        
        if (size < 26) return false;
        
        for(char word : sentence.toCharArray()){
            isPangram[word - 'a'] = true;
        }
        
        for(boolean check : isPangram){
            if(!check) return false;
        }
        
        return true;

    }
}