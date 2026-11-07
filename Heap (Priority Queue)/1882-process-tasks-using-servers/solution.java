class Solution {
    public int[] assignTasks(int[] servers, int[] tasks) {
        int n = servers.length, m = tasks.length;

        // free: {weight, index} -> smallest weight first, then smallest index
        PriorityQueue<int[]> free = new PriorityQueue<>(
                (a, b) -> a[0] != b[0] ? Integer.compare(a[0], b[0]) : Integer.compare(a[1], b[1]));
        for (int i = 0; i < n; i++) {
            free.offer(new int[] { servers[i], i });
        }

        // busy: {timeFreeAgain, weight, index}; long because finish times can exceed int range
        PriorityQueue<long[]> busy = new PriorityQueue<>((a, b) -> {
            if (a[0] != b[0])
                return Long.compare(a[0], b[0]);
            if (a[1] != b[1])
                return Long.compare(a[1], b[1]);
            return Long.compare(a[2], b[2]);
        });

        int[] ans = new int[m];
        long t = 0;
        for (int j = 0; j < m; j++) {
            // Task j enters the queue at second j; time never moves backwards.
            t = Math.max(t, j);

            // No free server: jump ahead to when the earliest one finishes.
            if (free.isEmpty() && busy.peek()[0] > t) {
                t = busy.peek()[0];
            }

            // Release every server that has finished by time t.
            while (!busy.isEmpty() && busy.peek()[0] <= t) {
                long[] s = busy.poll();
                free.offer(new int[] { (int) s[1], (int) s[2] });
            }

            int[] s = free.poll();
            ans[j] = s[1];
            busy.offer(new long[] { t + tasks[j], s[0], s[1] });
        }
        return ans;
    }
}