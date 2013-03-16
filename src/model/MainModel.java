package model;

import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;

//TODO: Not sure why after dragging then playing it will have its selected obj still....
public class MainModel extends Object {
	public enum State {
		draw, erase, selection, dragged, playing
	};

	/* A list of the model's views. */
	private ArrayList<IView> views;

	private ArrayList<Segment> segments;
	private ArrayList<Integer> selectedIndices;

	private State state = State.draw;
	private boolean stillDragging = true;
	private int currframe = 0;
	private int totalframes = 0;
	Segment currSegment;
	Segment selectingSegment;

	// Override the default constructor, making it private.
	public MainModel() {
		views = new ArrayList<IView>();
		segments = new ArrayList<Segment>();
		currSegment = new Segment(currframe, currframe);
		selectingSegment = new Segment(currframe, currframe);
		selectedIndices = new ArrayList<Integer>();
	}

	public void pushFrame() {
		for (Segment s : segments) {
			if (!s.isErased(currframe)) {
				s.createFrame(currframe + 1);
			}
		}
		currframe++;
		if (currframe > totalframes) {
			totalframes++;
		}
		this.updateAllViews();
	}

	// increment totalframes, insert a copy of current frame for each segment.
	public void insertFrame() {
		increaseFrames();
		for (Segment s : segments) {
			s.createFrame(currframe);
		}
		System.out.println("Inserted frame");
	}

	public void addPointToSegment(Point point) {
		if (state == State.draw) {
			currSegment.addPoint(point);
			if (this.stillDragging && this.segments.size() == 0) {
				this.segments.add(currSegment);
			}
			this.segments.set(segments.size() - 1, currSegment);
		} else if (state == State.selection
				&& this.getSelectedIndices().size() == 0) {
			selectingSegment.addPoint(point);
		}
		this.updateAllViews();
	}

	public void addSegment() {
		currSegment = new Segment(currframe, currframe);
		segments.add(currSegment);
		// System.out.println("Added another segment for a total of " +
		// segments.size() + " segments at time "+currframe);
	}


	public void addTranslate(int x, int y) {
		int currFrame = this.getFrame();
		// System.out.println("Dragging the objs");
		for (Segment s : segments) {
			if (!s.isErased(currFrame)) {
				s.addSegmentTranslate(0, 0, currFrame);
			}
		}
		for (int index : selectedIndices) {
			segments.get(index).addSegmentTranslate(x, y, currFrame);
		}
	}


	public void selectStuff(GeneralPath lassoPath) {
		for (int i = 0; i < segments.size(); i++) {
			ArrayList<Point> points = segments.get(i).getTranslates(
					this.getFrame());
			int size = points.size();
			for (int j = 0; j < size; j++) {
				Point currPoint = points.get(j);
				if (!lassoPath.contains(currPoint)) {
					break;
				}
				// all pts inside lasso, so we can select this segment
				else if (j == size - 1) {
					System.out.println("Selected!");
					this.addSelectedIndex(i);
				}
			}
		}
		this.removeLasso();
	}

	public void eraseStuff(int oldX, int oldY) {
		int largestEndTime = 0, currEndTime = 0;
		for (Segment s : segments) {
			ArrayList<Point> points = s.getTranslates(this.getFrame());

			for (int j = 0; j < points.size(); j++) {
				Point currPoint = points.get(j);
				int x = currPoint.x;
				int y = currPoint.y;

				if (oldX > x - 10 && oldX < x + 10 && oldY > y - 10
						&& oldY < y + 10) {
					// System.out.println("Erasing the " + i + "th obj");
					s.setEndTime(currframe - 1);
					break;
				}
			}
			currEndTime = s.getEndTime();
			if (currEndTime > largestEndTime) {
				largestEndTime = currEndTime;
			}
		}
		if (largestEndTime < this.totalframes) {
			this.totalframes = currEndTime;
		}
		this.updateAllViews();
	}


	public void deselect() {
		this.selectedIndices.clear();
		this.removeLasso();
		state = State.selection; // allow user to select again.
		this.updateAllViews();
	}

	
	public void setState(State passedState) {
		// remove the selected items & lasso trace
		if (state == State.draw || state == State.erase
				|| state == State.playing) {
			this.selectedIndices.clear();
			this.removeLasso();
		}

		this.state = passedState;
		this.updateAllViews();
	}

	public void removeLasso() {
		this.selectingSegment = new Segment(currframe, currframe);
		this.updateAllViews();
	}

	public ArrayList<Segment> getSegments() {
		return segments;
	}
	
	public Segment getSelectingSegment() {
		return this.selectingSegment;
	}
	
	public void setStillDragging(boolean stillDragging) {
		this.stillDragging = stillDragging;
	}

	public boolean getStillDragging() {
		return this.stillDragging;
	}

	public void addSelectedIndex(int selected) {
		selectedIndices.add(selected);
	}

	public ArrayList<Integer> getSelectedIndices() {
		return this.selectedIndices;
	}

	
	public State getState() {
		return this.state;
	}

	public int getFrame() {
		return this.currframe;
	}

	public int getTotalFrames() {
		return this.totalframes;
	}

	public void setFrame(int frame) {
		if (frame <= totalframes) {
			this.currframe = frame;
			this.updateAllViews();
		}
	}

	public void gotoZero() {
		currframe = 0;
		this.updateAllViews();
	}

	public void increaseFrames() {
		currframe++;
		if (currframe > totalframes) {
			totalframes++;
		}
		this.updateAllViews();
	}

	public void decreaseFrames() {
		if (currframe > 0) {
			currframe--;
			this.updateAllViews();
		}
	}
	
	public void restart() {
		this.segments.clear();
		this.selectingSegment = new Segment(currframe, currframe);
		this.selectedIndices.clear();
		this.totalframes = 0;
		this.currframe = 0;
		this.updateAllViews();
	}

	public void addView(IView view) {
		this.views.add(view);
		view.updateView();
	}

	public void removeView(IView view) {
		this.views.remove(view);
	}

	private void updateAllViews() {
		for (IView view : this.views) {
			view.updateView();
		}
	}
}