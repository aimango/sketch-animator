package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.GeneralPath;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JSlider;

import model.IView;
import model.MainViewModel;

public class MainView extends JComponent implements IView {

	private static final long serialVersionUID = 1L;
	Image image;
	Graphics2D graphics2D;
	int currentX, currentY, oldX, oldY;


	private MainViewModel model;
//	private JTextField baseTF = new JTextField(10);
//	private JTextField heightTF = new JTextField(10);
//	private JTextField hypoTF = new JTextField(10);


	
	/**
	 * What to do when the model changes.
	 */
	public void updateView() {
//		baseTF.setText("" + model.getBase());
//		heightTF.setText("" + model.getHeight());
//		hypoTF.setText("" + model.getHypotenuse());
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
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				oldX = e.getX();
				oldY = e.getY();
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				currentX = e.getX();
				currentY = e.getY();
				if (graphics2D != null){
					graphics2D.setStroke(new BasicStroke(5));
				    GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 1);

			        path.moveTo(oldX,oldY);
		            path.lineTo(currentX,currentY);
			        //path.curveTo(150, 150, 300, 300, 50, 250); wut
			        path.closePath();
			        model.addPath(path); // store the path for future usage
			        
			        graphics2D.draw(path);
			      //graphics2D.drawLine(oldX, oldY, currentX, currentY);
				}
				
				repaint();
				oldX = currentX;
				oldY = currentY;
			}
		});
	}
	
	public MainView(MainViewModel aModel) {
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
