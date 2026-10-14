class Solution {
    public String maxSumOfSquares(int num, int sum) {
        if(sum > num * 9) return "";
        int countNine = sum / 9; // finding the num of nines that can be present in the orignal num
        int remaining = sum % 9; // remaining digit that can be included to sum up the num like if sum is 10 then we can have '9' and '1' which will be the max  
        StringBuilder answer = new StringBuilder();
        // append all the nines that can be done 
        for(int nines = 1; nines <= countNine; nines++){
            answer.append("9");
        }
        // append the remaining sum after removing '9'
        if(remaining > 0) answer.append(remaining);
        // filling zeros to make the length equal to the num of digits
        while(answer.length() < num){
            answer.append("0");
        }
        return answer.toString();
    }
}