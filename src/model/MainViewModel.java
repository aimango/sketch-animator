package model;

import java.awt.geom.GeneralPath;
// Note!  Nothing from the view package is imported here.
import java.util.ArrayList;
import java.util.Timer;


// need a start/end time for each path. or length
public class MainViewModel extends Object {
	/* A list of the model's views. */
	private ArrayList<IView> views = new ArrayList<IView>();
	private ArrayList<GeneralPath> paths = new ArrayList<GeneralPath>();
	private int state = 0; // {0,1,2,3,4} = {draw, erase, selection}. Default is draw
	private boolean selected = false;
	private int selectedIndex;
	private boolean cleared = false;
	private int duration = 100; 
	private boolean playing = false;
	private int FPS = 40;
	private Timer t;
	
	// Override the default construtor, making it private.
	public MainViewModel() {
	}
	
	public void setSelected(boolean selected){
		this.selected = selected;
	}
	public boolean getSelected(){
		return this.selected;
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
		this.updateAllViews(); // not sure if this is necessary
	}
	
	public int getState(){
		return this.state;
	}
	/** Add a new view of this triangle. */
	public void addView(IView view) {
		this.views.add(view);
		view.updateView();
	}

	/** Remove a view from this triangle. */
	public void removeView(IView view) {
		this.views.remove(view);
	}

	/** Update all the views that are viewing this triangle. */
	private void updateAllViews() {
		for (IView view : this.views) {
			view.updateView();
		}
	}
	
	public void addPath(GeneralPath path) {
		this.paths.add(path);
	}
	
	public void removePath(int i){
		this.paths.remove(i);
	}
	
	public ArrayList<GeneralPath> getPaths(){
		return paths;
	}
	
	public boolean getClear(){
		return this.cleared;
	}
	
	public void setClear(boolean toclear){
		this.cleared = toclear;
		this.updateAllViews();
	}
}