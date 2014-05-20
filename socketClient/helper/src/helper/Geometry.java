package helper;

import java.awt.Point;

public class Geometry {

	static public double distanceEuclide(Point p1, Point p2) {
		int xCarre = (p1.x - p2.x) * (p1.x - p2.x);
		int yCarre = (p1.y - p2.y) * (p1.y - p2.y);
		return Math.sqrt(xCarre + yCarre);
	}

	static public double distanceManhattan(Point p1, Point p2) {
		int x = Math.abs(p1.x - p2.x);
		int y = Math.abs(p1.y - p2.y);
		return x + y;
	}
}
