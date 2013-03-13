package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JComponent;

import model.IView;
import model.MainViewModel;

//Selection: use a dotted BasicStroke.. just check if this new path is around the old paths. Full object.s
public class CanvasView extends JComponent implements IView {

	private static final long serialVersionUID = 1L;
	Graphics2D graphics2D;
	int currentX, currentY, oldX, oldY;
	ArrayList<Point> currPath = null;
	private MainViewModel model;
	private GeneralPath selectedPath;
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		int state = model.getState();
		
		if (state == 2){
			ArrayList<Point> selectedPathPts = model.getSelectingPath();
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
		ArrayList<ArrayList<Point>> paths = model.getPaths();
		if (paths.size() > 0) {
			for (int i = 0; i < paths.size(); i++) { // separate objects
				
				int size = paths.get(i).size();
				System.out.println("Has "+ size + " points");
				GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD, size);
				if (size > 0) {
					Point first = paths.get(i).get(0);
					path.moveTo(first.getX(), first.getY());
					// System.out.println(first.getX() + " " + first.getY());
					for (int j = 1; j < paths.get(i).size(); j++) {
						Point to = paths.get(i).get(j);
						path.lineTo(to.getX(), to.getY());
						// System.out.println(to.getX() + " " + to.getY());
					}
				}
				g2.setStroke(new BasicStroke(5));
				g2.setColor(Color.BLACK);
				g2.draw(path);
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

		// need to listen for what state we are in. draw/erase/selection
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				model.setStillPainting(true);
				oldX = e.getX();
				oldY = e.getY();
				if (model.getState() == 0) {
					currPath = new ArrayList<Point>();
					model.addPoint(new Point(oldX, oldY));
				} else if (model.getState() == 1) { // erase
					ArrayList<ArrayList<Point>> paths = model.getPaths();
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
				if (model.getState() == 0) {
					
					model.addPoint(new Point(currentX, currentY));

				} else if (model.getState() == 2 && !model.getSelected()) {
					model.addPoint(new Point(currentX, currentY));
					
					ArrayList<ArrayList<Point>> paths = model.getPaths();
					for (int i = 0; i < paths.size(); i++) {
						int size = paths.get(i).size();
						for (int j = 0; j < size; j++) {

							Point currPoint = paths.get(i).get(j);
							if (!selectedPath.contains(currPoint)){
								break;
							} else if ( j == size-1){ // all pts inside, so select
								System.out.println("Yay!"); // THIS WORKS!11one1
								// now just need to 'outline' the object and allow drag/drop..
							}
						}
						
					}
				}
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				int state = model.getState();
				currentX = e.getX();
				currentY = e.getY();
				if (state == 2 && model.getSelected()) {
					System.out.println("Dragging the obj");
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
			}
		});
	}

	public CanvasView(MainViewModel aModel) {
		super();
		this.model = aModel;
		this.layoutView();
		this.registerControllers();

		// Add a this view as a listener to the model
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
