package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.Timer;

import model.IView;
import model.MainViewModel;
import model.Segment;

//buggy:
//when i erase, i need to save end time instead of actually removing <- CURRENTLY DOING.. very confusing.


public class CanvasView extends JComponent implements IView {

	private static final long serialVersionUID = 1L;
	
	private int currentX, currentY, oldX, oldY;
	private ArrayList<Point> currPath = null;
	private MainViewModel model;
	private GeneralPath selectedPath;
	private Timer t;
	private int fps = 5;
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		clearScreen(g2);
		
		int state = model.getState();
		ArrayList<Integer> selected = model.getSelectedIndices();
		
		//THE SEGMENTS!!!!!!!!
		ArrayList<Segment> paths = model.getPaths();
		if (paths.size() > 0) {
			for (int i = 0; i < paths.size(); i++) { // separate objects	
				int size = paths.get(i).size();
				
				int currFrame = model.getFrame();
				GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD, size);
				
				// make sure # points is greater than 0 and that the segment
				// is spse to be visible in the current frame
				//System.out.println(paths.get(i).getEndTime() + " " + currFrame);

				ArrayList<Point> transformedPoints = paths.get(i).getTranslates(currFrame);
				if (transformedPoints.size() > 0){
					Point first = transformedPoints.get(0);
					path.moveTo(first.getX(), first.getY());
					
					for (int j = 1; j < paths.get(i).size(); j++) {
						Point to = transformedPoints.get(j);
						path.lineTo(to.getX(), to.getY());
					}
				}
				
				g2.setStroke(new BasicStroke(5));
				if (selected.contains(i)){
					g2.setColor(Color.RED);
				} else {
					g2.setColor(Color.BLACK);
				}
				g2.draw(path);
			} 
		}
		
		//LASSO!!!!!!!!!!!!
		if (state == 2 && selected.size() == 0){ // stage where we are still selecting something
			Segment selectedPathPts = model.getSelectingPath();
			int size = selectedPathPts.size();
			if (size > 0){
				selectedPath = new GeneralPath(GeneralPath.WIND_EVEN_ODD, size);
				selectedPath.moveTo(selectedPathPts.get(0).x, selectedPathPts.get(0).y);
				for (int i = 1; i < size; i++){
					selectedPath.lineTo(selectedPathPts.get(i).x, selectedPathPts.get(i).y);
				}
				if (!model.getStillDragging()){
					selectedPath.closePath();
				}
				final float dash1[] = { 5.0f };
				final BasicStroke dashed = new BasicStroke(5.0f, BasicStroke.CAP_BUTT,
						BasicStroke.JOIN_MITER, 50.0f, dash1, 0.0f);
				g2.setColor(Color.BLUE);
				g2.setStroke(dashed);
				g2.draw(selectedPath);
			}
		}
	}

	public void updateView() {
		if (model.getState() == 0) {
			setCursor(new Cursor(Cursor.HAND_CURSOR));
		} else if (model.getState() == 1) {
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		} else if (model.getState() == 2 || model.getState() == 4) {
			setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		}
		repaint();
	}

	private void registerControllers() {

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				model.setStillDragging(true);
				oldX = e.getX();
				oldY = e.getY();
				
				if (model.getState() == 0) {
					model.addPath();
					currPath = new ArrayList<Point>();
					model.addPoint(new Point(oldX, oldY));
				} 
				
				else if (model.getState() == 1) {
					model.eraseStuff(oldX, oldY);
				} 
				if (model.getState() == 2 && model.getSelectedIndices().size() > 0){
					model.setState(4);
				}
			}

			public void mouseReleased(MouseEvent e) {
				model.setStillDragging(false);
				model.addPoint(new Point(currentX, currentY));
				
				// in select mode and have no paths selected yet 
				if (model.getState() == 4 && model.getSelectedIndices().size() > 0){
					model.setState(2);
				}
				else if (model.getState() == 2 && model.getSelectedIndices().size() == 0) {
					model.selectStuff(selectedPath);
				}

			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				int state = model.getState();
				currentX = e.getX();
				currentY = e.getY();
				
				int numSelected = model.getSelectedIndices().size();
				if (state == 4 && numSelected != 0) {
					//model.pushFrame();
					model.addTranslate(currentX-oldX, currentY-oldY);
				} 
				if (state == 0 || state == 2) {
					// if (currentX > oldX+3 && currentY > oldY+3){
					model.addPoint(new Point(currentX, currentY));
					
				}
				else if (model.getState() == 1) {
					model.eraseStuff(currentX, currentY);
				} 
				oldX = currentX;
				oldY = currentY;
				repaint();
			}
		});
	}

	public CanvasView(MainViewModel aModel) {
		super();
		this.model = aModel;
		this.registerControllers();

		// Add this view as a listener to the model
		this.model.addView(this);

		setDoubleBuffered(false);

		// TODO: activate this
		 ActionListener repainter = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (model.getState() == 4)
					model.pushFrame();
			}
		 };
		 t = new Timer(1000/fps, repainter);
		 t.start();
	}

	public void clearScreen(Graphics g){
		g.setColor(Color.white);
		g.fillRect(0, 0, getSize().width, getSize().height);
		g.setColor(Color.black);
	}
}
