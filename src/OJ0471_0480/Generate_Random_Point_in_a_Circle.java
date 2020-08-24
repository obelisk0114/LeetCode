package OJ0471_0480;

import java.util.Random;

public class Generate_Random_Point_in_a_Circle {
	/*
	 * https://leetcode.com/problems/generate-random-point-in-a-circle/discuss/154037/Polar-Coordinates-10-lines
	 * 
	 * The reason is simple. Since we want to get a random point in the region 
	 * x^2 + y^2 <= r^2, '<=' is equal to a random number of which the range is [0,1].
	 * Here is the new equation: x^2 + y^2 = random * r^2.
	 * Then we can transform it to the polar coordinates:
	 * x = Math.sqrt(random) * r * cos(theta)
	 * y = Math.sqrt(random) * r * sin(theta)
	 * 
	 * We are using one random Length to multiply on a random normalized vector
	 * (x^2 + y^2 = 1) to get the random point. However, if we directly multiply them, 
	 * we will get a graph that points placed averagely according to Length/Radius.
	 * 
	 * Therefore, we need to decrease the chance that the point can placed there 
	 * according to how close it is to the center (how short is the that position's 
	 * radius).
	 * 
	 * Rf : 
	 * https://leetcode.com/problems/generate-random-point-in-a-circle/discuss/155650/Explanation-with-Graphs-why-using-Math.sqrt()/438929
	 * https://leetcode.com/problems/generate-random-point-in-a-circle/discuss/155650/Explanation-with-Graphs-why-using-Math.sqrt()
	 * leetcode.com/problems/generate-random-point-in-a-circle/discuss/155650/Explanation-with-Graphs-why-using-Math.sqrt()/167341
	 */
	class Solution {
		double radius, x_center, y_center;

		public Solution(double radius, double x_center, double y_center) {
			this.radius = radius;
			this.x_center = x_center;
			this.y_center = y_center;
		}

		public double[] randPoint() {
			double len = Math.sqrt(Math.random()) * radius;
			double deg = Math.random() * 2 * Math.PI;
			double x = x_center + len * Math.cos(deg);
			double y = y_center + len * Math.sin(deg);
			return new double[] { x, y };
		}
	}
	
	/*
	 * Rf : https://leetcode.com/problems/generate-random-point-in-a-circle/discuss/154092/Very-simple-Python-solution
	 */
	class Solution_self {
	    private double x_center;
	    private double y_center;
	    private double radius;

	    public Solution_self(double radius, double x_center, double y_center) {
	        this.x_center = x_center;
	        this.y_center = y_center;
	        this.radius = radius;
	    }
	    
	    public double[] randPoint() {
	        double[] coordinate = new double[2];
	        double bottomX = x_center - radius;
	        double bottomY = y_center - radius;
	        double r2 = radius * radius;
	        
	        do {
	            coordinate[0] = Math.random() * radius * 2 + bottomX;
	            coordinate[1] = Math.random() * radius * 2 + bottomY;
	        } while (Math.pow(coordinate[0] - x_center, 2) + Math.pow(coordinate[1] - y_center, 2) > r2);
	        
	        return coordinate;
	    }
	}
	
	/*
	 * https://leetcode.com/problems/generate-random-point-in-a-circle/discuss/154027/Straight-forward-Java-AC-solution
	 * 
	 * Fit the circle in a square.
	 * Sample a point in the square and check if it's also in the circle. 
	 * If not, sample again.
	 * 
	 * https://docs.oracle.com/javase/7/docs/api/java/util/Random.html#nextDouble()
	 */
	class Solution_Random {
		double r, x, y;

		public Solution_Random(double radius, double x_center, double y_center) {
			r = radius;
			x = x_center;
			y = y_center;
		}

		public double[] randPoint() {
			Random rand = new Random();
			double nx = x - r + rand.nextDouble() * 2 * r;
			double ny = y - r + rand.nextDouble() * 2 * r;
			double r2 = r * r;
			while (dis(nx, ny) >= r2) {
				nx = x - r + rand.nextDouble() * 2 * r;
				ny = y - r + rand.nextDouble() * 2 * r;
			}
			return new double[] { nx, ny };
		}

		// it returns the square of the distance between the point and the center;
		double dis(double nx, double ny) {
			return (nx - x) * (nx - x) + (ny - y) * (ny - y);
		}
	}

	/**
	 * Your Solution object will be instantiated and called as such:
	 * Solution obj = new Solution(radius, x_center, y_center);
	 * double[] param_1 = obj.randPoint();
	 */

}
