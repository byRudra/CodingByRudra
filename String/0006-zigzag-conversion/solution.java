class Solution {
    public String convert(String s, int numRows) {
        if(numRows == 1 || numRows >= s.length())
            return s;
        boolean goingDown = false;
        int rowNumber = 0;

        StringBuilder[] rows = new StringBuilder[numRows];
        for(int i = 0; i < numRows; i++){
            rows[i] = new StringBuilder();
        }

        for(char c : s.toCharArray()){
            rows[rowNumber].append(c);

            if(rowNumber == 0 || rowNumber == numRows - 1)
                goingDown = !goingDown;
            
            rowNumber += goingDown ? 1 : -1;
        }

        StringBuilder result = new StringBuilder();
        for(StringBuilder row : rows){
            result.append(row);
        }
        return result.toString();
    }
}