class Solution {
    public String reverseParentheses(String s) {
        StringBuilder curr = new StringBuilder();
        Stack<StringBuilder> reverse = new Stack<>();
        for(char ch : s.toCharArray()){
            if(ch == '('){
                reverse.push(curr);
                curr = new StringBuilder();
            }
            else if (ch == ')'){
                curr.reverse();
                curr.insert(0, reverse.pop());
            }
            else{
                curr.append(ch);
            }
        }
        return curr.toString();
    }
}
// find (  
// then continue till ) or ( appears'
// if ( appears start again till ) appears and call a function reverse 