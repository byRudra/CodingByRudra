class Solution {
    public String multiply(String num1, String num2) {
        if(num1.equals("0") || num2.equals("0")) return "0";
        int n = num1.length();
        int m = num2.length();
        int result[] = new int[n + m];
        for(int i = n - 1; i >= 0; i--){
            for(int j = m - 1; j >= 0; j--){
                int first = num1.charAt(i) - '0'; 
                int second = num2.charAt(j) - '0'; 

                int sum = first * second + result[i + j + 1];
                result[i + j + 1] = sum % 10;
                result[i + j] += sum / 10;

            }
        }
        int i = 0;
        while(i < n + m && result[i] == 0)
            i++;
        
        StringBuilder answer = new StringBuilder();
        while(i < n + m){
            answer.append(result[i++]);
        }
        return answer.toString();
    }
}