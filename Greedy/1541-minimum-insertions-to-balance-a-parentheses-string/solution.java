class Solution {
    public int minInsertions(String s) {
        int answer = 0;
        int currentNeed = 0;

        for (char current : s.toCharArray()) {
            if (current == '(') {
                // checking if our previours ( has already gotten its )) pair or not if need % 2 == 1 means 1 ) is remaining 
                if (currentNeed % 2 == 1) {
                    answer++; // intertion of one )
                    currentNeed--;
                }
                currentNeed += 2; // current ( requires two ))
            } else {
                if (currentNeed == 0) { // means the closing bracket ')' needs an opening bracket '('
                    answer++;
                    currentNeed = 1; // we have already gotten one ')' bracket so need = 1 not 2
                } else {
                    currentNeed--;
                }
            }
        }
        return answer + currentNeed;
    }
}