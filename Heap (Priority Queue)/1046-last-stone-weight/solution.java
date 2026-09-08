class Solution {
    public int lastStoneWeight(int[] stones) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());

        for(int stone : stones){
            pq.offer(stone);
        }

        while(pq.size() > 1){
            int maxheavy = pq.poll();
            int notmaxheavy = pq.poll();

            if(maxheavy != notmaxheavy)
                pq.offer(maxheavy - notmaxheavy);
        }
        return pq.isEmpty() ? 0 : pq.poll();
    }
}