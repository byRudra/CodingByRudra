class Solution {
    public int minSwaps(String s) {
        int no1 = 0;
        int no0 = 0;
        for(char ch : s.toCharArray()){
            if(ch == '0') no0++;
            else no1++;
        }
        if (Math.abs(no1 - no0) > 1) return -1;

        int startZero = 0;
        int startOne = 0;
        int index = 0;
        for(char ch : s.toCharArray()){
            char startwithZero = index % 2 == 0 ? '0' : '1';
            char startwithOne = index % 2 == 0 ? '1' : '0';
            if(startwithZero != ch) startZero++;
            if(startwithOne != ch) startOne++;
            index++;
        }
        if(no1 > no0)
            return startOne / 2;
        if(no1 < no0)
            return startZero / 2;
        return Math.min(startZero, startOne) / 2;
    }
}