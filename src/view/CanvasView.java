package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;

import javax.swing.JComponent;

import model.IView;
import model.MainViewModel;
import model.Segment;

//TODO: erasing/selecting transformed segments - need to apply affinetransform X__X FML **
//TODO: also should allow selection of multiple segments - not really working for some reason
//TODO: replacing old frames when backtracked 
//when i erase, i need to save end time instead of actually removing <- CURRENTLY DOING.. very confusing.

public class CanvasView extends JComponent implements IView {

	private static final long serialVersionUID = 1L;
	
	int currentX, currentY, oldX, oldY;
	ArrayList<Point> currPath = null;
	private MainViewModel model;
	private GeneralPath selectedPath;
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		int state = model.getState();
		ArrayList<Integer> selected = model.getSelectedIndices();
		
		//THE SEGMENTS!!!!!!!!
		ArrayList<Segment> paths = model.getPaths();
		if (paths.size() > 0) {
			for (int i = 0; i < paths.size(); i++) { // separate objects	
				int size = paths.get(i).size();
				//System.out.println("Has "+ size + " points");
				int currFrame = model.getFrame();
				GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD, size);
				int isAlive = currFrame - paths.get(i).getStartTime();
				;
				// make sure # points is greater than 0 and that the segment
				// is spse to be visible in the current frame
				// paths.get(i).getEndTime() < currFrame
				 System.out.println(paths.get(i).getEndTime() + " " + currFrame);
				 
				 
				if (size > 0 && isAlive >= 0 &&  paths.get(i).getEndTime() <= currFrame) { 
					//get the transformed points based on frame number.....
					ArrayList<Point> transformedPoints = paths.get(i).getTransformed(currFrame);
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
				if (!model.stillPainting()){
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
	
	

	/**
	 * What to do when the model changes.
	 */
	public void updateView() {
		if (model.getState() == 0) {
			setCursor(new Cursor(Cursor.HAND_CURSOR));
		} else if (model.getState() == 1) {
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		} else if (model.getState() == 2) {
			setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		}
		repaint();
	}

	private void layoutView() {
		// this.setLayout(new FormLayout());
		// this.add(new JLabel("Base:"));
		// this.add(this.baseTF);
		// this.add(new JLabel("Height:"));
		// this.add(this.heightTF);
		// this.add(new JLabel("Hypotenuse:"));
		// this.add(this.hypoTF);

	}

	private void registerControllers() {

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				model.setStillPainting(true);
				oldX = e.getX();
				oldY = e.getY();
				
				
				if (model.getState() == 0) {
					System.out.println("New PATH!");
					model.addPath();
					currPath = new ArrayList<Point>();
					model.addPoint(new Point(oldX, oldY));
				} 
				
				else if (model.getState() == 1) { // ERASE!!!!
					ArrayList<Segment> paths = model.getPaths();
					for (int i = 0; i < paths.size(); i++) {
						int size = paths.get(i).size();
						for (int j = 0; j < size; j++) {

							Point currPoint = paths.get(i).get(j);
							int x = currPoint.x;
							int y = currPoint.y;
							//System.out.println("x,y " + x + " " + y + " pressed at " + oldX + " " + oldY);
							if (oldX > x - 10 && oldX < x + 10 && oldY > y - 10
									&& oldY < y + 10) {
								System.out.println("Erasing this obj");
								model.removePath(i);

								break;
							}
						}
					}
				} 
//				else if (model.getState() == 2){ // select
//					model.setSelected(true);
//				}
			}

			public void mouseReleased(MouseEvent e) {
				model.setStillPainting(false);
				model.addPoint(new Point(currentX, currentY));
				if (model.getState() == 2 && model.getSelectedIndices().size() == 0) {
					
					ArrayList<Segment> paths = model.getPaths();
					for (int i = 0; i < paths.size(); i++) {
						int size = paths.get(i).size();
						for (int j = 0; j < size; j++) {
							Point currPoint = paths.get(i).get(j);
							if (!selectedPath.contains(currPoint)){
								break;
							} else if (j == size-1){ // all pts inside, so we can select this one
								System.out.println("Selected!"); 
								model.addSelectedIndex(i);
							}
						}
					}
					if (model.getSelectedIndices().size() == 0){ // didnt select anything. so remove lasso
						model.removeLasso();
					}
				}

			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				int state = model.getState();
				int currFrame = model.getFrame();
				currentX = e.getX();
				currentY = e.getY();
				ArrayList<Integer> selectedIndices = model.getSelectedIndices();
				int numSelected = model.getSelectedIndices().size();
				if (state == 2 && numSelected != 0) {
					model.increaseTotalFrames();
					System.out.println("Dragging the objs");
					for (int index : selectedIndices){
						model.getPaths().get(index).addTranslate(
								currentX-oldX, currentY-oldY, currFrame);
						
						for (int i = 0; i < model.getPaths().size(); i++){
							Segment s = model.getPaths().get(i);
							if (i != index)
								s.addTranslate(0, 0, currFrame);
							if (s.getEndTime() == currFrame)
								s.setEndTime(currFrame);
							
						}
					}
					// if
					// (model.getPaths().get(model.getSelectedIndex()).contains(oldX,
					// oldY)){
					// AffineTransform at = new AffineTransform();
					// at.translate(100,100);
					// model.getPaths().get(model.getSelectedIndex()).transform(at);
					// // doesnt workk.
					// }
				} else if (state == 0 || state == 2) {
					// if (currentX > oldX+3 && currentY > oldY+3){
					model.addPoint(new Point(currentX, currentY));
					oldX = currentX;
					oldY = currentY;
				}	
				repaint();
			}
			
		});
	}

	public CanvasView(MainViewModel aModel) {
		super();
		this.model = aModel;
		this.layoutView();
		this.registerControllers();

		// Add this view as a listener to the model
		this.model.addView(this);

		setDoubleBuffered(false);

		// TODO: activate this
		// ActionListener repainter = new ActionListener(){
		// public void actionPerformed(ActionEvent e){
		// repaint();
		// }
		// };
		// t = new Timer(1000/fps, repainter);
		// t.start();
	}

	public void clearScreen(Graphics g){
		g.setColor(Color.white);
		g.fillRect(0, 0, getSize().width, getSize().height);
		g.setColor(Color.black);
	}
}
