package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.awt.geom.Point2D;

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
	GeneralPath currPath = null;
	GeneralPath lassoPath = null;
	private MainViewModel model;
	
	/**
	 * What to do when the model changes.
	 */
	public void updateView() {
		if (model.getState() == 0){
			setCursor(new Cursor(Cursor.HAND_CURSOR));
		} else if (model.getState() == 1){
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		} else if (model.getState() == 2){
			setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		}
		
		if (model.getClear() == true){
			clear();
		}
	}

	private void layoutView() {
//		this.setLayout(new FormLayout());
//		this.add(new JLabel("Base:"));
//		this.add(this.baseTF);
//		this.add(new JLabel("Height:"));
//		this.add(this.heightTF);
//		this.add(new JLabel("Hypotenuse:"));
//		this.add(this.hypoTF);

	}

	private void registerControllers() {
//		// Add a controller to interpret user actions in the base text field
//		this.baseTF.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent evt) {
//				double base = Double.parseDouble(baseTF.getText());
//				model.setBase(base);
//			}
//		});
//
//		// Add a controller to interpret user actions in the height text field
//		this.heightTF.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent evt) {
//				double height = Double.parseDouble(heightTF.getText());
//				model.setHeight(height);
//			}
//		});
		
		// need to listen for what state we are in. draw/erase/selection
		
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				oldX = e.getX();
				oldY = e.getY();
				if (model.getState() == 1){
					ArrayList<GeneralPath> paths = model.getPaths();
					for (int j = 0; j < paths.size(); j++){
						if (paths.get(j).intersects(oldX, oldY, 1, 1)){ // doesnt really work....
							//model.removePath(j);
							repaint(); // need to remove somehow visually as well...
							System.out.print("HIT ");
							Rectangle r = paths.get(j).getBounds();
							System.out.println(oldX + " " + oldY + "Rect bounds are "+r.x+" " + r.y+ " "+ r.height +
									" " + r.width);
						} else{
							Rectangle r = paths.get(j).getBounds();
							System.out.println(oldX + " " + oldY + "Rect bounds are "+r.x+" " + r.y+ " "+ r.height +
									" " + r.width);
						}
					}
				}
			}
			
			public void mouseReleased(MouseEvent e){
				if (model.getState() == 0){
					currPath.closePath();
			        model.addPath(currPath); // store the path for future usage
			        currPath = null;
			        repaint();
				}
				else if (model.getState() == 2){
					currPath.closePath();
					
					ArrayList<GeneralPath> paths = model.getPaths();
					System.out.println("# paths"+paths.size());
					for (int i = 0; i < paths.size(); i++){
						Rectangle r = paths.get(i).getBounds();
						if (!currPath.contains(r)){
							System.out.println("NO!");
							break;
						} else if (i == paths.size() - 1){
							System.out.println("YES!");
						}
					}
					currPath = null;
					
				}
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				int state = model.getState();
				if (state != 1){
					currentX = e.getX();
					currentY = e.getY();
					if (graphics2D != null){
						graphics2D.setStroke(new BasicStroke(5));
						if (state == 0){
							graphics2D.setColor(Color.BLACK);
						} else if (state == 2) {
							final float dash1[] = {5.0f};
						    final BasicStroke dashed = new BasicStroke(5.0f, BasicStroke.CAP_BUTT,
						    		BasicStroke.JOIN_MITER, 50.0f, dash1, 0.0f);
						    graphics2D.setColor(Color.BLUE);
						    graphics2D.setStroke(dashed);    
						} 
						
						if (currPath == null){
							currPath = new GeneralPath(GeneralPath.WIND_NON_ZERO, 1);
							System.out.println("FIRST");
							currPath.moveTo(oldX, oldY);
						}
						currPath.lineTo(currentX, currentY);
				        //path.curveTo(150, 150, 300, 300, 50, 250); wut

				        graphics2D.draw(currPath);
				      //graphics2D.drawLine(oldX, oldY, currentX, currentY);
					}
					
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
	}

	public void paintComponent(Graphics g) {
		if (image == null) {
			image = createImage(getSize().width, getSize().height);
			graphics2D = (Graphics2D) image.getGraphics();
			graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			clear();
		}
		g.drawImage(image, 0, 0, null);
	}

	public void clear() {
		graphics2D.setPaint(Color.white);
		graphics2D.fillRect(0, 0, getSize().width, getSize().height);
		graphics2D.setPaint(Color.black);
		repaint();
	}
}
