class Solution {

    int n, k;
    int[][] cnt;
    int[] prod;

    public int[] resultArray(int[] nums, int k, int[][] queries) {
        this.n = nums.length;
        this.k = k;

        cnt = new int[4 * n][k];
        prod = new int[4 * n];

        build(1, 0, n - 1, nums);

        int[] ans = new int[queries.length];

        for (int i = 0; i < queries.length; i++) {

            int index = queries[i][0];
            int value = queries[i][1];
            int start = queries[i][2];
            int x = queries[i][3];

            // Persistent update
            update(1, 0, n - 1, index, value);

            // Query nums[start ... n-1]
            Node res = query(1, 0, n - 1, start, n - 1);

            ans[i] = res.cnt[x];
        }

        return ans;
    }

    // Stores information about a segment
    class Node {
        int product;
        int[] cnt;

        Node(int product, int[] cnt) {
            this.product = product;
            this.cnt = cnt;
        }
    }

    void build(int node, int l, int r, int[] nums) {

        if (l == r) {
            prod[node] = nums[l] % k;
            cnt[node][prod[node]] = 1;
            return;
        }

        int mid = l + (r - l) / 2;

        build(node * 2, l, mid, nums);
        build(node * 2 + 1, mid + 1, r, nums);

        pull(node);
    }

    void pull(int node) {

        int left = node * 2;
        int right = node * 2 + 1;

        prod[node] = (prod[left] * prod[right]) % k;

        // Prefixes completely inside left
        for (int r = 0; r < k; r++) {
            cnt[node][r] = cnt[left][r];
        }

        // Prefixes that extend into right
        for (int r = 0; r < k; r++) {

            int newRem = (prod[left] * r) % k;

            cnt[node][newRem] += cnt[right][r];
        }
    }

    void update(int node, int l, int r, int index, int value) {

        if (l == r) {

            prod[node] = value % k;

            for (int i = 0; i < k; i++) {
                cnt[node][i] = 0;
            }

            cnt[node][prod[node]] = 1;

            return;
        }

        int mid = l + (r - l) / 2;

        if (index <= mid) {
            update(node * 2, l, mid, index, value);
        } else {
            update(node * 2 + 1, mid + 1, r, index, value);
        }

        pull(node);
    }

    Node query(int node, int l, int r, int ql, int qr) {

        if (ql <= l && r <= qr) {

            int[] copy = new int[k];

            for (int i = 0; i < k; i++) {
                copy[i] = cnt[node][i];
            }

            return new Node(prod[node], copy);
        }

        int mid = l + (r - l) / 2;

        if (qr <= mid) {
            return query(node * 2, l, mid, ql, qr);
        }

        if (ql > mid) {
            return query(node * 2 + 1, mid + 1, r, ql, qr);
        }

        Node left = query(node * 2, l, mid, ql, qr);
        Node right = query(node * 2 + 1, mid + 1, r, ql, qr);

        return merge(left, right);
    }

    Node merge(Node left, Node right) {

        int[] resCnt = new int[k];

        // Prefixes entirely in left
        for (int r = 0; r < k; r++) {
            resCnt[r] += left.cnt[r];
        }

        // Prefixes extending from left into right
        for (int r = 0; r < k; r++) {

            int rem = (left.product * r) % k;

            resCnt[rem] += right.cnt[r];
        }

        int resProduct = (left.product * right.product) % k;

        return new Node(resProduct, resCnt);
    }
}