class Solution {
    public boolean validateStackSequences(int[] pushed, int[] popped) {
     Deque<Integer> stack = new ArrayDeque<>();
     int j = 0; // no of popped performed
     for(int num : pushed){
        stack.push(num);
        while(!stack.isEmpty() && j < popped.length && popped[j] == stack.peek()){
            stack.pop();
            j++;
        }
     }
     return stack.isEmpty();
    }
}