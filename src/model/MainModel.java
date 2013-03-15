package model;

import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;

//TODO: Not sure why after dragging it will have its selected obj still....
public class MainModel extends Object {
	public enum State {draw, erase, selection, dragged, playing}; 
	
	/* A list of the model's views. */
	private ArrayList<IView> views;
	
	private ArrayList<Segment> segments;
	private ArrayList<Integer> selectedIndices;

	private State state = State.draw;
	private boolean stillDragging = true;
	private int currframe = 0;
	private int totalframes = 0;
	Segment currPath;
	Segment selectingPath;

	// Override the default constructor, making it private.
	public MainModel() {
		views = new ArrayList<IView>();
		segments = new ArrayList<Segment>();
		currPath = new Segment(currframe, currframe);
		selectingPath = new Segment(currframe, currframe);
		selectedIndices = new ArrayList<Integer>();
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

	// increment totalframes, insert a copy of current frame.
	public void insertFrame() {
		increaseFrames();
		for (Segment s : segments) {
			if (!s.isErased(currframe)) {
				s.copyTransform(currframe);
			}
		}
		System.out.println("Inserted frame");
	}

	public void addPoint(Point point) {
		if (state == State.draw) {
			currPath.addPoint(point);
			if (this.stillDragging && this.segments.size() == 0) {
				this.segments.add(currPath);
			}
			this.segments.set(segments.size() - 1, currPath);
		} else if (state == State.selection && this.getSelectedIndices().size() == 0) {
			// System.out.println("Add point");
			selectingPath.addPoint(point);
		}
		this.updateAllViews();
	}

	public void addSegment() {
		currPath = new Segment(currframe, currframe);
		segments.add(currPath);
		// System.out.println("Added another segment for a total of " +
		// segments.size() + " paths at time "+currframe);
	}

	public ArrayList<Segment> getSegments() {
		return segments;
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

	public Segment getSelectingPath() {
		return this.selectingPath;
	}

	public void removeLasso() {
		this.selectingPath = new Segment(currframe, currframe);
		this.updateAllViews();
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

	public void selectStuff(GeneralPath selectedPath) {
		for (int i = 0; i < segments.size(); i++) {
			ArrayList<Point> points = segments.get(i).getTranslates(
					this.getFrame());
			int size = points.size();
			for (int j = 0; j < size; j++) {
				Point currPoint = points.get(j);
				if (!selectedPath.contains(currPoint)) {
					break;
				}
				// all pts inside lasso, so we can select this segment
				else if (j == size - 1) {
					System.out.println("Selected!");
					this.addSelectedIndex(i);
				}
			}
		}
		// didnt select anything. so remove lasso
		if (this.getSelectedIndices().size() == 0) {
			this.removeLasso();
		}
	}

	public void eraseStuff(int oldX, int oldY) {
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
		}
		this.updateAllViews();
	}

	public void setState(State passedState) {
		// remove the selected items & lasso trace
		if (state == State.draw || state == State.erase || state == State.playing) {
			this.selectedIndices.clear();
			this.removeLasso();
		}

		this.state = passedState;
		this.updateAllViews();
	}

	public State getState() {
		return this.state;
	}

	public void deselect() {
		this.selectedIndices.clear();
		this.removeLasso();
		state = State.selection; // allow user to select again.
		this.updateAllViews();
	}

	public void restart() {
		this.segments.clear();
		this.selectingPath = new Segment(currframe, currframe);
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