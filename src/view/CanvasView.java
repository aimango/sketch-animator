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

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;

import model.IView;
import model.MainModel;
import model.Segment;

//TODO: Rotation
//TODO: Color picker.
public class CanvasView extends JComponent implements IView {

	private static final long serialVersionUID = 1L;

	private int currentX, currentY, oldX, oldY;
	private MainModel model;
	private GeneralPath selectedPath;
	private Timer t;
	private int fps = 40;

	public CanvasView(MainModel aModel) {
		super();
		this.model = aModel;
		this.registerControllers();
		//this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.model.addView(this);

		setDoubleBuffered(false);

		ActionListener tick = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (model.getState() == MainModel.State.dragged){
					model.pushFrame();
				}
				else if (model.getState() == MainModel.State.playing) {
					model.increaseFrames();
					if (model.getFrame() >= model.getTotalFrames()) {
						model.setState(MainModel.State.draw);
					}
				}
			}
		};
		t = new Timer(1000/fps, tick);
		t.start();
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		clearScreen(g2);

		MainModel.State state = model.getState();
		ArrayList<Integer> selected = model.getSelectedIndices();

		// ZEE SEGMENTS
		ArrayList<Segment> segments = model.getSegments();
		if (segments.size() > 0) {
			for (int i = 0; i < segments.size(); i++) {
				int size = segments.get(i).size();

				int currFrame = model.getFrame();
				GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD,
						size);

				// make sure # points is greater than 0 and that the segment
				// is spse to be visible in the current frame
				ArrayList<Point> transformedPoints = segments.get(i)
						.getTranslates(currFrame);
				if (transformedPoints.size() > 0) {
					//System.out.println("num points is "+transformedPoints.size());
					Point first = transformedPoints.get(0);
					path.moveTo(first.getX(), first.getY());

					for (int j = 1; j < segments.get(i).size(); j++) {
						Point to = transformedPoints.get(j);
						path.lineTo(to.getX(), to.getY());
					}
					if (transformedPoints.size() == 1){
						path.lineTo(first.getX(), first.getY());
					}
				}

				if (selected.contains(i) && state != MainModel.State.playing) {
					// highlight!
					g2.setStroke(new BasicStroke(9));
					g2.setColor(new Color(223, 128, 255));
					g2.draw(path);

					// draw actual segment
					g2.setStroke(new BasicStroke(5));
					g2.setColor(Color.BLACK);
					g2.draw(path);
				} else {
					g2.setStroke(new BasicStroke(5));
					g2.setColor(Color.BLACK);
					g2.draw(path);
				}

			}
		}

		// LASSO - stage where we are still selecting something
		if (state == MainModel.State.selection && selected.size() == 0) {
			Segment selectedPathPts = model.getSelectingSegment();
			int size = selectedPathPts.size();
			if (size > 0) {
				selectedPath = new GeneralPath(GeneralPath.WIND_EVEN_ODD, size);
				selectedPath.moveTo(selectedPathPts.get(0).x,
						selectedPathPts.get(0).y);
				for (int i = 1; i < size; i++) {
					selectedPath.lineTo(selectedPathPts.get(i).x,
							selectedPathPts.get(i).y);
				}
				if (!model.getStillDragging()) {
					selectedPath.closePath();
				}
				final float dash1[] = { 5.0f };
				final BasicStroke dashed = new BasicStroke(5.0f,
						BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 50.0f,
						dash1, 0.0f);
				g2.setColor(Color.BLUE);
				g2.setStroke(dashed);
				g2.draw(selectedPath);
			}
		}
	}

	private void registerControllers() {

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				model.setStillDragging(true);
				oldX = e.getX();
				oldY = e.getY();
				MainModel.State state = model.getState();
				if (state == MainModel.State.draw) {
					model.addSegment();
					model.addPointToSegment(new Point(oldX, oldY));
				} else if (state == MainModel.State.erase) {
					model.eraseStuff(oldX, oldY);
				}
				//only allow dragging if we press down on 1 of the selected segments
				else if (state == MainModel.State.selection
						&& model.getSelectedIndices().size() > 0) {
					for (Segment s : model.getSegments()){
						if (s.contains(oldX, oldY, model.getFrame())){
							model.setState(MainModel.State.dragged);
							break;
						}
					}
				}
			}

			public void mouseReleased(MouseEvent e) {
				model.setStillDragging(false);
				// go back to select mode if we were in dragged mode
				if (model.getState() == MainModel.State.dragged
						&& model.getSelectedIndices().size() > 0) {
					model.setState(MainModel.State.selection);
				}
				// in select mode and have no segments selected yet
				else if (model.getState() == MainModel.State.selection
						&& model.getSelectedIndices().size() == 0) {
					model.selectStuff(selectedPath);
				}

			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				MainModel.State state = model.getState();
				currentX = e.getX();
				currentY = e.getY();

				int numSelected = model.getSelectedIndices().size();

				// if dragging, add some translations!
				if (state == MainModel.State.dragged && numSelected != 0) {
					model.addTranslate(currentX - oldX, currentY - oldY);
				}

				// if in selection, add points for either segment or lasso
				// drawing. (let model handle logic)
				if (state == MainModel.State.draw
						|| state == MainModel.State.selection) {
					model.addPointToSegment(new Point(currentX, currentY));
				}

				else if (model.getState() == MainModel.State.erase) {
					model.eraseStuff(currentX, currentY);
				}
				oldX = currentX;
				oldY = currentY;
				repaint();
			}
		});
	}

	public void clearScreen(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, getSize().width, getSize().height);
		g.setColor(Color.black);
	}

	public void updateView() {
		if (model.getState() == MainModel.State.draw) {
			setCursor(new Cursor(Cursor.HAND_CURSOR));
		} else if (model.getState() == MainModel.State.erase) {
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		} else if (model.getState() == MainModel.State.selection
				|| model.getState() == MainModel.State.dragged) {
			setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		}
		repaint();
	}

}
