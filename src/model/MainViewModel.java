package model;

import java.awt.geom.GeneralPath;
// Note!  Nothing from the view package is imported here.
import java.util.ArrayList;


public class MainViewModel extends Object {
	/* A list of the model's views. */
	private ArrayList<IView> views = new ArrayList<IView>();
	private ArrayList<GeneralPath> paths = new ArrayList<GeneralPath>();
	private int state = 0; // {0,1,2} = {draw, erase, selection}. Default is draw
	private boolean cleared = false;
	

	// Override the default construtor, making it private.
	public MainViewModel() {
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
	
	public boolean getClear(){
		return this.cleared;
	}
	
	public void setClear(boolean toclear){
		this.cleared = toclear;
		this.updateAllViews();
	}
}