class Solution {
    public int numRescueBoats(int[] people, int limit) {
        Arrays.sort(people);
        int i = 0, j = people.length - 1, boats = 0;
        while (i <= j) {
            if (people[i] + people[j] <= limit) i++; // lightest hops on too
            j--;                                      // heaviest always leaves
            boats++;
        }
        return boats;
    }
}