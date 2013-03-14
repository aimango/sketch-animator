package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Timer;
// Note!  Nothing from the view package is imported here.


// need a start/end time for each path. or length
public class MainViewModel extends Object {
	/* A list of the model's views. */
	private ArrayList<IView> views = new ArrayList<IView>();

	private ArrayList<Segment> paths = new ArrayList<Segment>();
	private boolean stillPainting = true;

	
	// {0,1,2,3,4} = {draw, erase, selection}. Default is draw
	private int state = 0; 
	private int selectedIndex = -1;
	
	private int duration = 100; 
	private boolean playing = false;
	private int FPS = 40;
	private Timer t;
	private int currframe = 0;
	private int totalframes = 0;
	Segment currPath = new Segment(currframe);
	Segment selectingPath = new Segment(currframe);
	
	// Override the default constructor, making it private.
	public MainViewModel() {
	}
	
	public int getFrame(){
		return this.currframe;
	}
	

	public void increaseFrames(){
		currframe++;
		
		if (currframe > totalframes)
			currframe--;
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
	
	public void increaseTotalFrames(){
		totalframes++;
		increaseFrames();
		
	}
	public void addPath() {
		currPath = new Segment(currframe);
		paths.add(currPath);
		System.out.println("Added another path for a total of "+ paths.size()+ " paths");
	}
	
	public Segment getSelectingPath(){
		return this.selectingPath;
	}
	public ArrayList<Segment> getPaths(){
		return paths;
	}
	
	public void removePath(int i){
		this.paths.remove(i);
		this.updateAllViews();
	}
	public void removeLasso(){
		this.selectingPath = new Segment(currframe);
		this.updateAllViews();
	}
	public void clearPaths(){
		this.paths.clear();
		this.selectingPath = new Segment(currframe);
		this.updateAllViews();
	}

	public void addPoint(Point point){
		if (state == 0){
			currPath.addPoint(point);
			if (this.stillPainting){
				if (this.paths.size() == 0)
					this.paths.add(currPath);
				this.paths.set(paths.size()-1, currPath);
			} else {
				System.out.println("New PATH!");
				this.paths.set(paths.size()-1, currPath);
				this.addPath();
			}
		} else if (state == 2){
			selectingPath.addPoint(point);
			if (!this.stillPainting){
				System.out.println("Done drawing the selector"); 
				//this.paths.set(paths.size()-1, currPath);
				//this.addSelectPath();
			}
		}
		this.updateAllViews();
	}
	
	public void setStillPainting(boolean isPainting){
		this.stillPainting = isPainting;
	}
	public boolean stillPainting(){
		return this.stillPainting;
	}
	
	public void setPlaying(boolean playing){
		this.playing = true;
	}
	public boolean getPlaying(){
		return this.playing;
	}
	
	public void setSelectedIndex(int selected){
		this.selectedIndex = selected;
	}
	
	public int getSelectedIndex(){
		return this.selectedIndex;
	}
	
	public void setState(int state){
		this.state = state;
	}
	
	public int getState(){
		return this.state;
	}
	
	/** Add a new view. */
	public void addView(IView view) {
		this.views.add(view);
		view.updateView();
	}

	/** Remove a view. */
	public void removeView(IView view) {
		this.views.remove(view);
	}

	/** Update all the views. */
	private void updateAllViews() {
		for (IView view : this.views) {
			view.updateView();
		}
	}
}	