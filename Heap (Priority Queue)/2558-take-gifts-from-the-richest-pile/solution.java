class Solution {
    public long pickGifts(int[] gifts, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
        for (int gift : gifts)
            pq.offer(gift);

        while (k-- > 0)
            pq.offer((int) Math.sqrt(pq.poll()));

        long sum = 0;

        for (int gift : pq)
            sum += gift;
        return sum;
    }
}