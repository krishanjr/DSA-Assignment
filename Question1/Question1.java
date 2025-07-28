// Maximizing Tech Startup Revenue before Acquisition
// A tech startup, AlgoStart, is planning to get acquired by a larger company. To negotiate a higher
// acquisition price, AlgoStart wants to increase its revenue by launching a few high-return projects.
// However, due to limited resources, the startup can only work on at most k distinct projects before the
// acquisition.
// You are given n potential projects, where the i-th project has a projected revenue gain of revenues[i]
// and requires a minimum investment capital of investments[i] to launch.
// Initially, AlgoStart has c capital. When a project is completed, its revenue gain is added to the
// startup’s total capital, which can then be reinvested into other projects.
// Your task is to determine the maximum possible capital AlgoStart can accumulate after completing at
// most k projects.
// Example 1:
// k = 2, c = 0, revenues = [2, 5, 8], investments = [0, 2, 3]
// Output: 7
// Explanation:
//  With initial capital 0, the startup can only launch Project 0 (since it requires 0 investment).
//  After completing Project 0, the capital becomes 0 + 2 = 2.
//  Now, with 2 capital, the startup can choose either Project 1 (investment 2, revenue 5) or
// Project 2 (investment 3, revenue 8).
//  To maximize revenue, it should select Project 2. However, Project 2 requires 3 capital, which is
// not available. So it selects Project 1.
//  After completing Project 1, the capital becomes 2 + 5 = 7.
//  The final maximized capital is 7.
// Example 2:
// Input:
// k = 3, c = 1, revenues = [3, 6, 10], investments = [1, 3, 5]
// Output: 19
// Explanation:
//  Initially, with 1 capital, Project 0 can be launched (investment 1, revenue 3).
//  Capital after Project 0 = 1 + 3 = 4.
//  With 4 capital, the startup can now launch Project 1 (investment 3, revenue 6).
//  Capital after Project 1 = 4 + 6 = 10.
//  Now, with 10 capital, Project 2 (investment 5, revenue 10) can be launched.
//  Final capital = 10 + 10 = 19.



package Question1;
import java.util.*;

public class Question1 {

    public static int maximizeCapital(int k, int c, int[] revenues, int[] investments) {
        int n = revenues.length;

        // List of all projects (investment, revenue)
        List<int[]> projects = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            projects.add(new int[]{investments[i], revenues[i]});
        }

        // Sort projects by required investment (ascending)
        projects.sort(Comparator.comparingInt(a -> a[0]));

        // Max-heap to select the project with max revenue we can afford
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());

        int i = 0;
        for (int j = 0; j < k; j++) {
            // Add all affordable projects to the max-heap
            while (i < n && projects.get(i)[0] <= c) {
                maxHeap.offer(projects.get(i)[1]); // add revenue to max-heap
                i++;
            }

            // No more projects we can afford
            if (maxHeap.isEmpty()) {
                break;
            }

            // Take the most profitable project
            c += maxHeap.poll();
        }

        return c;
    }

    // Example usage
    public static void main(String[] args) {
        int[] revenues1 = {2, 5, 8};
        int[] investments1 = {0, 2, 3};
        System.out.println(maximizeCapital(2, 0, revenues1, investments1)); // Output: 7

        int[] revenues2 = {3, 6, 10};
        int[] investments2 = {1, 3, 5};
        System.out.println(maximizeCapital(3, 1, revenues2, investments2)); // Output: 19
    }
}