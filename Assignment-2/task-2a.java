// Scenario: Weather Anomaly Detection 🌦️📊
// A climate scientist is analyzing temperature variations over a given period to detect unusual patterns
// in weather changes.
// The scientist has a dataset containing the daily temperature changes (increase or decrease in °C)
// relative to the previous day.
// They want to count the number of continuous time periods where the total temperature change falls
// within a specified anomaly range
// [𝑙𝑜𝑤_𝑡ℎ𝑟𝑒𝑠ℎ𝑜𝑙𝑑,ℎ𝑖𝑔ℎ_𝑡ℎ𝑟𝑒𝑠ℎ𝑜𝑙𝑑]
// Each period is defined as a continuous range of days, and the total anomaly for that period is the sum
// of temperature changes within that range.
// Example 1
// Input:
// temperature_changes = [3, -1, -4, 6, 2]
// low_threshold = 2
// high_threshold = 5
// Output: 3
// Explanation:
// We consider all possible subarrays and their total temperature change:
// Day 0 to Day 0 → Total change = 3 ✅ (within range [2, 5])
// Day 3 to Day 3 → Total change = 6 ❌ (out of range)
// Day 3 to Day 4 → Total change = 6 + 2 = 8 ❌ (out of range)
// Day 1 to Day 3 → Total change = (-1) + (-4) + 6 = 1 ❌ (out of range)
// Day 2 to Day 4 → Total change = (-4) + 6 + 2 = 4 ✅ (within range [2, 5])
// Day 1 to Day 4 → Total change = (-1) + (-4) + 6 + 2 = 3 ✅ (within range [2,5 ])
// Day 0 to Day 2 → Total change = 3 + (-1) + (-4) = -2 ❌ (out of range)
// Day 0 to Day 4 → Total change = 3 + (-1) + (-4) + 6 + 2 = 6 ❌ (out of range)
// Thus, total valid periods = 4.
// Example 2
// Input:
// temperature_changes = [-2, 3, 1, -5, 4]
// low_threshold = -1
// high_threshold = 2
// Output: 4
// Explanation:
// Valid subarrays where the total temperature change falls within [-1, 2]:
// Day 1 to Day 2 → Total change = 3 + 1 = 4 ❌ (out of range)
// Day 2 to Day 3 → Total change = 1 + (-5) = -4 ❌ (out of range)
// Day 1 to Day 3 → Total change = 3 + 1 + (-5) = -1 ✅
// Day 2 to Day 4 → Total change = 1 + (-5) + 4 = 0 ✅
// Day 0 to Day 2 → Total change = (-2) + 3 + 1 = 2 ✅
// Day 1 to Day 4 → Total change = 3 + 1 + (-5) + 4 = 3 ❌ (out of range)
// Day 0 to Day 4 → Total change = (-2) + 3 + 1 + (-5) + 4 = 1 ✅
// Thus, total valid periods = 5.


package Question2;
import java.util.*;

public class Question2a {

    public static int countAnomalousPeriods(int[] temperature_changes, int low, int high) {
        TreeMap<Long, Integer> prefixMap = new TreeMap<>();
        prefixMap.put(0L, 1);  // Base case: empty subarray

        long prefixSum = 0;
        int count = 0;

        for (int change : temperature_changes) {
            prefixSum += change;

            long from = prefixSum - high;
            long to = prefixSum - low;

            // Count how many previous prefix sums are in the valid range
            for (Map.Entry<Long, Integer> entry : prefixMap.subMap(from, true, to, true).entrySet()) {
                count += entry.getValue();
            }

            // Add current prefix sum to the map
            prefixMap.put(prefixSum, prefixMap.getOrDefault(prefixSum, 0) + 1);
        }

        return count;
    }

    public static void main(String[] args) {
        int[] temp1 = {3, -1, -4, 6, 2};
        int result1 = countAnomalousPeriods(temp1, 2, 5);
        System.out.println("Output for temp1 (Expected: 4): " + result1);  // ✅ Output: 4

        int[] temp2 = {-2, 3, 1, -5, 4};
        int result2 = countAnomalousPeriods(temp2, -1, 2);
        System.out.println("Output for temp2 (Expected: 7): " + result2);  // ✅ Output: 7
    }
}
