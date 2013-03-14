package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Timer;

public class MainViewModel extends Object {
	/* A list of the model's views. */
	private ArrayList<IView> views = new ArrayList<IView>();

	private ArrayList<Segment> paths = new ArrayList<Segment>();
	private boolean stillPainting = true;
	
	// {0,1,2,3,4} = {draw, erase, selection}. Default is draw
	private int state = 0; 
	private ArrayList<Integer> selectedIndices = new ArrayList<Integer>(); 
	//private int selectedIndex = -1;
	
	
	private int currframe = 0;
	private int totalframes = 0;
	Segment currPath = new Segment(currframe, totalframes);
	Segment selectingPath = new Segment(currframe, totalframes);
	
	
	private int duration = 100; 
	private boolean playing = false;
	private int FPS = 40;
	private Timer t;
	
	
	// Override the default constructor, making it private.
	public MainViewModel() {
	}
	
	public int getFrame(){
		return this.currframe;
	}
	

	public void increaseFrames(){
		currframe++;
		
		if (currframe > totalframes){
			//currframe--;
			totalframes++;
		}

		this.updateAllViews();
		System.out.println("Frame has been increased to "+ currframe);
	}
	
	public void pushFrame(){
		for (Segment s : paths){
			if (!s.isErased(currframe)){
				s.createFrame(currframe+1);
			}
		}
		currframe++;
		
		if (currframe > totalframes){
			//currframe--;
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
	
	public void increaseTotalFrames(){
		totalframes++;
		increaseFrames();
	}
	public void addPath() {
		currPath = new Segment(currframe, totalframes);
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
		//this.paths.get(i).setEndTime(currframe-1);
		this.updateAllViews();
	}
	public void removeLasso(){
		this.selectingPath = new Segment(currframe, totalframes);
		this.updateAllViews();
	}
	
	public void gotoZero(){
		currframe=0;
		this.updateAllViews();
	}
	public void clear(){

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
			if (this.stillPainting){
				if (this.paths.size() == 0)
					this.paths.add(currPath);
				
			} 
			this.paths.set(paths.size()-1, currPath);
		} else if (state == 2 && this.getSelectedIndices().size() == 0){
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
	
	public void addSelectedIndex(int selected){
		selectedIndices.add(selected);
	}
	
	public ArrayList<Integer> getSelectedIndices(){
		return this.selectedIndices;
	}
	
	public void setState(int state){
		if (state == 0 || state == 1){ // if it's toggled to draw or erase, remove the selected items & lasso trace
			this.selectedIndices.clear();
			this.removeLasso();
			this.updateAllViews();
		}
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