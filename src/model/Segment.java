package model;

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class Segment extends Object {

	private ArrayList<Point> points = new ArrayList<Point>();
	private ArrayList<AffineTransform> atList = new ArrayList<AffineTransform>();
	private int startTime;
	private int endTime;

	public Segment(int start, int end) {
		startTime = start;
		endTime = end;
		atList.add(new AffineTransform());
	}

	public int size() {
		return points.size();
	}

	public Point get(int i) {
		return points.get(i);
	}

	public boolean contains(int x, int y, int frame) {
		ArrayList<Point> transformedPath = getTranslates(frame);
		for (Point p : transformedPath) {
			if (x > p.x - 10 && x < p.x + 10 && y > p.y - 10 && y < p.y + 10) {
				return true;
			}
		}
		return false;
	}

	public void addPoint(Point point) {
		points.add(point);
	}

	public int getStartTime() {
		return this.startTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	public int getEndTime() {
		return this.endTime;
	}

	public boolean isErased(int frame) {
		int isAliveStart = frame - startTime;
		int isAliveEnd = endTime - frame;
		if (isAliveStart >= 0 && isAliveEnd >= 0) {
			return false;
		}
		return true;
	}

	public void createFrame(int frame) {
		if (frame - startTime == 0) { // first one
			atList.set(0, new AffineTransform());
		} else if (frame > endTime) { // last one
			atList.add(new AffineTransform(atList.get(atList.size() - 1)));
			endTime++;
		} else { // middle
			atList.set(frame - startTime,
					new AffineTransform(atList.get(frame - startTime - 1)));
		}
	}

	public void addSegmentTranslate(int x, int y, int frame) {
		AffineTransform a = atList.get(frame - startTime);
		a.translate(x, y);
	}

	// get all the transforms at a particular frame
	public ArrayList<Point> getTranslates(int frame) {
		ArrayList<Point> destination = new ArrayList<Point>();

		// dont draw if doesnt exist at that frame.
		if (frame > endTime || frame < startTime)
			return destination;

		for (Point p : points) {
			Point dest = null;
			dest = new Point();
			atList.get(frame - startTime).transform(p, dest);
			destination.add(dest);
		}
		return destination;
	}

}