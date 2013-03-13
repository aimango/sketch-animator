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

//Erase: need to detect if we click on a path in the path list. not sure how intersects works for path..
//Selection: use a dotted BasicStroke.. just check if this new path is around the old paths. Full object.s
// Not really working X__X
public class CanvasView extends JComponent implements IView {

	private static final long serialVersionUID = 1L;
	Image image;
	Graphics2D graphics2D;
	int currentX, currentY, oldX, oldY;
	// GeneralPath currPath = null;
	ArrayList<Point> currPath = null;
	GeneralPath lassoPath = null;
	private MainViewModel model;

	public void paintComponent(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;
		//clear();
		ArrayList<ArrayList<Point>> paths = model.getPaths();
		if (paths.size() > 0) {
			//System.out.println("REPAINTSSS");
			System.out.println("Now "+paths.size()+" paths");
			// System.out.println("number of paths is "+paths.size());
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
						g2.setStroke(new BasicStroke(5));
						g2.setColor(Color.BLACK);
						g2.draw(path);
					}
				}

			} 
//			
//			if (image == null) {
//				image = createImage(getSize().width, getSize().height);
//				graphics2D = (Graphics2D) image.getGraphics();
//				graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//						RenderingHints.VALUE_ANTIALIAS_ON);
//				clear();
//			}
//			g2.drawImage(image, 0, 0, null);
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

		if (model.getClear() == true) {
			clear();
			model.setClear(false);
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
		// // Add a controller to interpret user actions in the base text field


		// need to listen for what state we are in. draw/erase/selection

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				model.setStillPainting(true);
				oldX = e.getX();
				oldY = e.getY();
				if (model.getState() == 0) {
					currPath = new ArrayList<Point>();// GeneralPath(GeneralPath.WIND_NON_ZERO,
														// 1);

					model.addPoint(new Point(oldX, oldY));
				}
				if (model.getState() == 1) { // erase
					System.out.println("ERASE~");
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
								System.out.println("HIT");
								model.removePath(i);

								break;
							}
						}
						// if (paths.get(j).intersects(oldX, oldY, 1, 1)){ //
						// doesnt really work....
						// //model.removePath(j);
						// repaint(); // need to remove somehow visually as
						// well...
						// System.out.print("HIT ");
						// Rectangle r = paths.get(j).getBounds();
						// System.out.println(oldX + " " + oldY +
						// "Rect bounds are "+r.x+" " + r.y+ " "+ r.height +
						// " " + r.width);
						// } else{
						// Rectangle r = paths.get(j).getBounds();
						// System.out.println(oldX + " " + oldY +
						// "Rect bounds are "+r.x+" " + r.y+ " "+ r.height +
						// " " + r.width);
						// }
					}
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (model.getState() == 0) {
					// currPath.closePath();
					// model.addPath(currPath); // store the path for future
					// usage
					// currPath = null;
					model.setStillPainting(false);
					model.addPoint(new Point(currentX, currentY));
					repaint();
				} else if (model.getState() == 2 && !model.getSelected()) {
					/*
					 * currPath.closePath();
					 * 
					 * ArrayList<GeneralPath> paths = model.getPaths();
					 * System.out.println("# paths"+paths.size()); for (int i =
					 * 0; i < paths.size(); i++){
					 * 
					 * double[][] points; points = getPoints(paths.get(i)); int
					 * npoints = points[0].length; double[] xpoints, ypoints;
					 * for (int j = 0; j < npoints; j++){ for (int k = 0; k <
					 * npoints; k++){ System.out.print(points[j][k]); //wtffff }
					 * } Polygon p = new Polygon(); Rectangle r =
					 * paths.get(i).getBounds(); if (!currPath.contains(r)){
					 * System.out.println("NO!"); break; } else { //if (i ==
					 * paths.size() - 1){ System.out.println("YES!");
					 * model.setSelected(true); model.setSelectedIndex(i); //so
					 * now we should be able to drag this object } } currPath =
					 * null;
					 */
				}
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				int state = model.getState();
				currentX = e.getX();
				currentY = e.getY();
				if (state != 1 && model.getSelected()) {
					System.out.println("Dragging the obj");
					// if
					// (model.getPaths().get(model.getSelectedIndex()).contains(oldX,
					// oldY)){
					// AffineTransform at = new AffineTransform();
					// at.translate(100,100);
					// model.getPaths().get(model.getSelectedIndex()).transform(at);
					// // doesnt workk.
					// }
				} else if (state == 0) {

					if (state == 2) {
						final float dash1[] = { 5.0f };
						final BasicStroke dashed = new BasicStroke(5.0f,
								BasicStroke.CAP_BUTT,
								BasicStroke.JOIN_MITER, 50.0f, dash1, 0.0f);
						graphics2D.setColor(Color.BLUE);
						graphics2D.setStroke(dashed);

					}

					// if (currentX > oldX+3 && currentY > oldY+3){
					model.addPoint(new Point(currentX, currentY));
					repaint();
					// }

					// graphics2D.draw(currPath);
					// graphics2D.drawLine(oldX, oldY, currentX, currentY);
				

					repaint();
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
	public void clear() {
		model.clearPaths();
		graphics2D.setPaint(Color.white);
		graphics2D.fillRect(0, 0, getSize().width, getSize().height);
		graphics2D.setPaint(Color.black);
		repaint();
	}

	static double[][] getPoints(GeneralPath path) {
		List<double[]> pointList = new ArrayList<double[]>();
		double[] coords = new double[6];
		int numSubPaths = 0;
		for (PathIterator pi = path.getPathIterator(null); !pi.isDone(); pi
				.next()) {
			switch (pi.currentSegment(coords)) {
			case PathIterator.SEG_MOVETO:
				pointList.add(Arrays.copyOf(coords, 2));
				++numSubPaths;
				break;
			case PathIterator.SEG_LINETO:
				pointList.add(Arrays.copyOf(coords, 2));
				break;
			case PathIterator.SEG_CLOSE:
				if (numSubPaths > 1) {
					throw new IllegalArgumentException(
							"Path contains multiple subpaths");
				}
				return pointList.toArray(new double[pointList.size()][]);
			default:
				throw new IllegalArgumentException("Path contains curves");
			}
		}
		throw new IllegalArgumentException("Unclosed path");
	}
}
