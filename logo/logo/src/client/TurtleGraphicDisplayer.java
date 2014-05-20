package client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import server.Turtle;
import server.Turtle.TurtlePoint;

public class TurtleGraphicDisplayer implements Observer {

	private class Line {
		Point2D begin_;
		Point2D end_;
		Color 	color_;
		
		public Line(double x1, double y1, double x2, double y2, Color color) {
			begin_ 	= new Point2D.Double(x1,y1);
			end_ 	= new Point2D.Double(x2,y2);
			color_	= color;
		}
		
		public void draw(Graphics g) {
			g.setColor(color_);
			g.drawLine((int)begin_.getX(), (int)begin_.getY(), (int)end_.getX(), (int)end_.getY());
		}
	}
	private Turtle turtle_;
	private List<Line> lines_;
	private TurtlePoint currentPosition_;
	private Point2D[] initialTurtlePosition_ = {new Point2D.Double(0, 0),new Point2D.Double(-5, -5),new Point2D.Double(10, 0),new Point2D.Double(-5, 5)};
	private Point2D[] currentTurtlePosition_ = new Point2D.Double[4];
	
	public TurtleGraphicDisplayer(Turtle turtle) {
		turtle_ = turtle;
		currentPosition_ = turtle_.getCurrentPosition();
		turtle.addObserver(this);
	}
	
	@Override
	public void update(Observable observable, Object argument) {
		if (observable == turtle_) {
			lines_ = createLinesFromTurtlePoint(turtle_.getDrawnPoints());
			currentPosition_ = turtle_.getCurrentPosition();
		}
	}

	public void draw(Graphics g) {
		if (lines_ != null) {
			for (Line line : lines_) {
				line.draw(g);
			}
		}
		drawTurtle_(g);
	}
	
	private void drawTurtle_(Graphics g) {
		AffineTransform translateTransform = AffineTransform.getTranslateInstance(currentPosition_.x_, currentPosition_.y_);
		AffineTransform rotateTransform = AffineTransform.getRotateInstance(Math.toRadians(currentPosition_.direction_));
		rotateTransform.transform(initialTurtlePosition_,0,currentTurtlePosition_,0,4);
		translateTransform.transform(currentTurtlePosition_,0,currentTurtlePosition_,0,4);
		
		Polygon polygon = new Polygon();
		for (Point2D point : currentTurtlePosition_) {
			polygon.addPoint((int)point.getX(),(int)point.getY());
		}
		
		if (currentPosition_.write_) {
			Color oldColor = g.getColor();
			g.setColor(currentPosition_.color_);
			g.fillPolygon((Polygon)polygon);
			g.setColor(oldColor);
		}
		else {
			g.drawPolygon((Polygon)polygon);
		}
	}

	public String toString() {
		String result = new String();
		for (Line line : lines_) {
			result += "\t(" + line.begin_.getX() + "," + line.begin_.getY() + "-" + line.end_.getX() + "," + line.end_.getY() + ")\n"; 
		}
		return result;
	}
	
	private List<Line> createLinesFromTurtlePoint(List<TurtlePoint> points) {
		List<Line> result = new ArrayList<Line>();
		
		if (!points.isEmpty()) {
			TurtlePoint lastUsedPoint = points.get(0);
			TurtlePoint previousPoint = null;
			for (TurtlePoint point : points) {
				if (point.write_ != lastUsedPoint.write_ || point.color_ != lastUsedPoint.color_ || point.direction_ != lastUsedPoint.direction_) {
					if (lastUsedPoint.write_) {
						if (previousPoint != null) {
							result.add(new Line(lastUsedPoint.x_,lastUsedPoint.y_,previousPoint.x_,previousPoint.y_,lastUsedPoint.color_));
						}
						else {
							result.add(new Line(lastUsedPoint.x_,lastUsedPoint.y_,lastUsedPoint.x_,lastUsedPoint.y_,lastUsedPoint.color_));
						}
					}
					lastUsedPoint = point;
				}
				previousPoint = point;
			}
			TurtlePoint lastPoint = points.get(points.size()-1);
			if (lastPoint != lastUsedPoint && lastUsedPoint.write_) {
				result.add(new Line(lastUsedPoint.x_,lastUsedPoint.y_,lastPoint.x_,lastPoint.y_,lastUsedPoint.color_));
			}
		}
		
		return result;
	}
}
