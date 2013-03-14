package model;

import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.Timer;

public class MainModel extends Object {
	/* A list of the model's views. */
	private ArrayList<IView> views;

	// {0,1,2,3,4} = {draw, erase, selection, deselection, dragged}. Default is draw
	private int state = 0; 
	private ArrayList<Segment> paths;
	private boolean stillDragging = true;
	private ArrayList<Integer> selectedIndices; 
	
	private int currframe = 0;
	private int totalframes = 0;
	Segment currPath;
	Segment selectingPath;
	
	private boolean playing = false;
	private Timer t;
	
	
	// Override the default constructor, making it private.
	public MainModel() {
		views = new ArrayList<IView>();
		paths = new ArrayList<Segment>();
		currPath = new Segment(currframe, totalframes);
		selectingPath = new Segment(currframe, totalframes);
		selectedIndices = new ArrayList<Integer>();
	}
	
	public int getFrame(){
		return this.currframe;
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
			System.out.println("Frame has been decreased to "+ currframe);
		}
	}
	
	public void pushFrame(){
		for (Segment s : paths){
			if (!s.isErased(currframe)){
				s.createFrame(currframe+1);
			}
		}
		currframe++;
		if (currframe > totalframes){
			totalframes++;
		}
		this.updateAllViews();
		System.out.println("Frame has been increased to "+ currframe);
	}
	
	public void addTranslate(int x, int y){
		int currFrame = this.getFrame();
		System.out.println("Dragging the objs");
		for (int i = 0; i < this.getPaths().size(); i++){
			Segment s = this.getPaths().get(i);
			s.addTranslate(0, 0, currFrame);
		}
		for (int index : selectedIndices){
			this.getPaths().get(index).addTranslate(x, y, currFrame);	
		}
	}
	
	public void addPath(){
		currPath = new Segment(currframe, totalframes);
		paths.add(currPath);
		System.out.println("Added another path for a total of " + paths.size() + " paths");
	}
	public ArrayList<Segment> getPaths(){
		return paths;
	}
	public Segment getSelectingPath(){
		return this.selectingPath;
	}
	
	public void eraseStuff(int oldX, int oldY){
		ArrayList<Segment> paths = this.getPaths();
		for (int i = 0; i < paths.size(); i++) {
			ArrayList<Point> points = paths.get(i).getTranslates(this.getFrame());
			int size = points.size();
			for (int j = 0; j < size; j++) {

				Point currPoint = points.get(j);
				int x = currPoint.x;
				int y = currPoint.y;
				
				//System.out.println("x,y " + x + " " + y + " pressed at " + oldX + " " + oldY);
				if (oldX > x - 10 && oldX < x + 10 && oldY > y - 10 && oldY < y + 10) {
					System.out.println("Erasing this obj");
					this.erasePath(i);
					break;
				}
			}
		}
	}
	
	public void selectStuff(GeneralPath selectedPath){
		ArrayList<Segment> paths = this.getPaths();
		for (int i = 0; i < paths.size(); i++) {
			ArrayList<Point> points = paths.get(i).getTranslates(this.getFrame());
			int size = points.size();
			for (int j = 0; j < size; j++) {
				Point currPoint = points.get(j);
				if (!selectedPath.contains(currPoint)){
					break;
				} else if (j == size-1){ // all pts inside, so we can select this one
					System.out.println("Selected!"); 
					this.addSelectedIndex(i);
				}
			}
		}
		if (this.getSelectedIndices().size() == 0){ // didnt select anything. so remove lasso
			this.removeLasso();
		}
	}
	
	public void erasePath(int i){
		this.paths.get(i).setEndTime(currframe-1);
		this.updateAllViews();
	}
	
	public void removeLasso(){
		this.selectingPath = new Segment(currframe, totalframes);
		this.updateAllViews();
	}
	
	public void gotoZero(){
		currframe = 0;
		this.updateAllViews();
	}

	public void restart(){
		this.paths.clear();
		this.selectingPath = new Segment(currframe, totalframes);
		this.selectedIndices.clear();
		this.totalframes = 0;
		this.currframe = 0;
		this.updateAllViews();
	}

	public void addPoint(Point point){
		if (state == 0){
			currPath.addPoint(point);
			if (this.stillDragging && this.paths.size() == 0){
				this.paths.add(currPath);
			} 
			this.paths.set(paths.size()-1, currPath);
		} 
		else if (state == 2 && this.getSelectedIndices().size() == 0){
			selectingPath.addPoint(point);
		}
		this.updateAllViews();
	}
	
	public void setStillDragging(boolean stillDragging){
		this.stillDragging = stillDragging;
	}
	public boolean getStillDragging(){
		return this.stillDragging;
	}
	
	public void setPlaying(boolean playing){
		this.playing = true;
	}
	public boolean getPlaying(){
		return this.playing;
	}
	
	public void addSelectedIndex(int selected){
		selectedIndices.add(selected);
	}
	public ArrayList<Integer> getSelectedIndices(){
		return this.selectedIndices;
	}
	
	public void setState(int state){
		// if it's toggled to draw or erase, remove the selected items & lasso trace
		if (state == 0 || state == 1 || state == 3){ 
			this.selectedIndices.clear();
			this.removeLasso();
			if (state == 3) // allow user to select again.
				state = 2;
			this.updateAllViews();
		} 
		this.state = state;
	}
	public int getState(){
		return this.state;
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