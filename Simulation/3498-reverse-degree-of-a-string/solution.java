class Solution {
    public int reverseDegree(String s) {
        int result = 0;
        for (int i = 1; i <= s.length(); i++){
            int value = ('z' - s.charAt(i - 1) + 1) * i;
            result += value;
        }
        return result;
    }
}