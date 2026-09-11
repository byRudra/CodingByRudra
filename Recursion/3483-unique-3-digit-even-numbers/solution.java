class Solution {
    public int totalNumbers(int[] digits) {
        int freq[] = new int[10];
        for (int digit : digits) {
            freq[digit]++;
        }

        int count = 0;

        // Find last Must be EVEN

        for (int last = 0; last <= 8; last += 2) {
            if (freq[last] == 0)
                continue;
            freq[last]--;

            // Find First Must not be 0
            for (int first = 1; first <= 9; first++) {
                if (freq[first] == 0)
                    continue;

                freq[first]--;

                // Find the Middle
                for (int middle = 0; middle <= 9; middle++) {
                    if (freq[middle] != 0)
                        count++;
                }

                freq[first]++;
            }

            freq[last]++;

        }
        return count;
    }
}