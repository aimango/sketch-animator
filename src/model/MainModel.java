package model;

import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;

//TODO: use enums for states.
//Not sure why after dragging it will have its selected obj still....
public class MainModel extends Object {
	/* A list of the model's views. */
	private ArrayList<IView> views;

	// {0,1,2,3,4} = {draw, erase, selection, deselection, dragged}. Default is draw
	private int state = 0; 
	private ArrayList<Segment> segments;
	private boolean stillDragging = true;
	private ArrayList<Integer> selectedIndices; 
	
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
	
	public int getFrame(){
		return this.currframe;
	}
	public int getTotalFrames(){
		return this.totalframes;
	}
	public void setFrame(int frame){
		if (frame <= totalframes){
			this.currframe = frame;
			this.updateAllViews();
		}
	}
	public void gotoZero(){
		currframe = 0;
		this.updateAllViews();
	}
	public void increaseFrames(){
		currframe++;
		if (currframe > totalframes){
			totalframes++;
		}

		this.updateAllViews();
		System.out.println("Frame has been increased to "+ currframe);
	}
	public void decreaseFrames(){
		if (currframe >0){
			currframe--;
			this.updateAllViews();
			//System.out.println("Frame has been decreased to "+ currframe);
		}
	}
	
	public void pushFrame(){
		for (int i = 0; i < segments.size(); i++){
			if (!segments.get(i).isErased(currframe)){
				System.out.println("For segment "+i);
				segments.get(i).createFrame(currframe+1);
			}
		}
		currframe++;
		if (currframe > totalframes){
			totalframes++;
		}
		this.updateAllViews();
		System.out.println("Frame has been increased to "+ currframe);
	}
	
	public void addPoint(Point point){
		if (state == 0){
			currPath.addPoint(point);
			if (this.stillDragging && this.segments.size() == 0){
				this.segments.add(currPath);
			} 
			this.segments.set(segments.size()-1, currPath);
		} 
		else if (state == 2 && this.getSelectedIndices().size() == 0){
			//System.out.println("Add point");
			selectingPath.addPoint(point);
		}
		this.updateAllViews();
	}
	
	public void addSegment(){
		currPath = new Segment(currframe, currframe);
		segments.add(currPath);
		//System.out.println("Added another path for a total of " + segments.size() + " paths at time "+currframe);
	}
	
	public ArrayList<Segment> getSegments(){
		return segments;
	}

	public void addTranslate(int x, int y){
		int currFrame = this.getFrame();
		System.out.println("Dragging the objs");
		for (int i = 0; i < segments.size(); i++){
			Segment s = segments.get(i);
			if (!s.isErased(currFrame)){
			s.addSegmentTranslate(0, 0, currFrame);
			}
		}
		for (int index : selectedIndices){
			this.getSegments().get(index).addSegmentTranslate(x, y, currFrame);	
		}
	}
	
	public void erasePath(int i){
		this.segments.get(i).setEndTime(currframe-1);
		this.updateAllViews();
	}

	public Segment getSelectingPath(){
		return this.selectingPath;
	}
	public void removeLasso(){
		this.selectingPath = new Segment(currframe, currframe);
		this.updateAllViews();
	}
	
	public void setStillDragging(boolean stillDragging){
		this.stillDragging = stillDragging;
	}
	public boolean getStillDragging(){
		return this.stillDragging;
	}
	
	public void addSelectedIndex(int selected){
		selectedIndices.add(selected);
	}
	public ArrayList<Integer> getSelectedIndices(){
		return this.selectedIndices;
	}
	
	public void selectStuff(GeneralPath selectedPath){
		for (int i = 0; i < segments.size(); i++) {
			ArrayList<Point> points = segments.get(i).getTranslates(this.getFrame());
			int size = points.size();
			for (int j = 0; j < size; j++) {
				Point currPoint = points.get(j);
				if (!selectedPath.contains(currPoint)){
					break;
				} 
				// all pts inside lasso, so we can select this segment
				else if (j == size-1){
					System.out.println("Selected!"); 
					this.addSelectedIndex(i);
				}
			}
		}
		// didnt select anything. so remove lasso
		if (this.getSelectedIndices().size() == 0){ 
			this.removeLasso();
		}
	}
	
	public void eraseStuff(int oldX, int oldY){
		for (int i = 0; i < segments.size(); i++) {
			ArrayList<Point> points = segments.get(i).getTranslates(this.getFrame());

			for (int j = 0; j < points.size(); j++) {
				Point currPoint = points.get(j);
				int x = currPoint.x;
				int y = currPoint.y;
				
				if (oldX > x - 10 && oldX < x + 10 && oldY > y - 10 && oldY < y + 10) {
					//System.out.println("Erasing the " + i + "th obj");
					this.erasePath(i);
					break;
				}
			}
		}
	}
	
	public void setState(int state){
		// remove the selected items & lasso trace
		if (state == 0 || state == 1 || state == 3 || state == 5){ 
			System.out.println("Set state to "+state);
			this.selectedIndices.clear();
			this.removeLasso();
			if (state == 3) // allow user to select again.
				state = 2;			
		}

		this.state = state;
		this.updateAllViews();
	}
	public int getState(){
		return this.state;
	}
	
	public void restart(){
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