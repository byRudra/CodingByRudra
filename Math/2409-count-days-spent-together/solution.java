class Solution {
    public int countDaysTogether(String arriveAlice, String leaveAlice, String arriveBob, String leaveBob) {
        int start = Math.max(toDays(arriveAlice), toDays(arriveBob));
        int end = Math.min(toDays(leaveAlice), toDays(leaveBob));

        return Math.max(0, end - start + 1);
    }
    private int toDays(String date){
        int[] daysInMonth = {31, 28, 31, 30, 31, 30,
                             31, 31, 30, 31, 30, 31 };
        int month = (date.charAt(0) - '0') * 10 + (date.charAt(1) - '0');
        int day = (date.charAt(3) - '0') * 10 + (date.charAt(4) - '0');

        // calculating days from the start of the year
        int total = day;
        for(int i = 0; i < month - 1; i++){
            total += daysInMonth[i];
        }
        return total;
    }
}