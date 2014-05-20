package client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Date;
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
	
	public TurtleGraphicDisplayer(Turtle turtle) {
		turtle_ = turtle;
		turtle.addObserver(this);
	}
	
	@Override
	public void update(Observable observable, Object argument) {
		if (observable == turtle_) {
			lines_ = createLinesFromTurtlePoint(turtle_.getDrawnPoints());
			System.out.println("TurtleGraphicDisplayer> " + new Date(turtle_.getTimestamp()) + "-->");
			System.out.println(this);
		}
	}

	public void draw(Graphics g) {
		for (Line line : lines_) {
			line.draw(g);
		}
	}
	
	public String toString() {
		String result = new String();
		for (Line line : lines_) {
			result += "\t(" + line.begin_.getX() + "," + line.begin_.getY() + "-" + line.end_.getX() + "," + line.end_.getY() + ")@" + line.color_ + "\n"; 
		}
		result += " \t--> " + currentPosition_;
		return result;
	}
	
	private List<Line> createLinesFromTurtlePoint(List<TurtlePoint> points) {
		List<Line> result = new ArrayList<Line>();
		
		TurtlePoint lastUsedPoint = points.get(0);
		for (TurtlePoint point : points) {
			if (point.write_ != lastUsedPoint.write_ || point.color_ != lastUsedPoint.color_ || point.direction_ != lastUsedPoint.direction_) {
				if (lastUsedPoint.write_) {
					result.add(new Line(lastUsedPoint.x_,lastUsedPoint.y_,point.x_,point.y_,lastUsedPoint.color_));
				}
				lastUsedPoint = point;
			}
		}
		TurtlePoint lastPoint = points.get(points.size()-1);
		if (lastPoint != lastUsedPoint) {
			result.add(new Line(lastUsedPoint.x_,lastUsedPoint.y_,lastPoint.x_,lastPoint.y_,lastUsedPoint.color_));
		}
		
		currentPosition_ = lastPoint;
		return result;
	}
	
	
}
