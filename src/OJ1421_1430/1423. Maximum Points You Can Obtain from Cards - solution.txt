class Solution {
    public int maxScore(int[] cardPoints, int k) {
        int startingIndex = 0;
        int presentSubarrayScore = 0;
        int n = cardPoints.length;
        int requiredSubarrayLength = n - k;
        int minSubarrayScore;
        int totalScore = 0;

        // Total score obtained on selecting all the cards.
        for (int i : cardPoints) {
            totalScore += i;
        }

        minSubarrayScore = totalScore;

        if (k == n) {
             return totalScore;
        }

        for (int i = 0; i < n; i++) {
            presentSubarrayScore += cardPoints[i];
            int presentSubarrayLength = i - startingIndex + 1;
            // If a valid subarray (having size cardsPoints.length - k) is possible.
            if (presentSubarrayLength == requiredSubarrayLength) {
                minSubarrayScore = Math.min(minSubarrayScore, presentSubarrayScore);
                presentSubarrayScore -= cardPoints[startingIndex++];
            }
        }
        return totalScore - minSubarrayScore;
    }
}
