package model;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class AnimatorModel extends Object {
	public enum State {
		draw, erase, selection, dragged, playing, export
	};

	/* A list of the model's views. */
	private ArrayList<IView> views = new ArrayList<IView>();

	private State state = State.draw;
	private boolean stillDragging = true;
	private int currframe = 0;
	private int totalframes = 0;

	private Color paletteColor = Color.BLACK;
	private int strokeSize = 5;

	private ArrayList<Segment> segments = new ArrayList<Segment>();
	private ArrayList<Integer> selectedIndices = new ArrayList<Integer>();
	private Segment currSegment = new Segment(currframe, currframe,
			paletteColor, strokeSize);
	private Segment selectingSegment = new Segment(currframe, currframe,
			paletteColor, strokeSize);

	// Override the default constructor, making it private.
	public AnimatorModel() {
	}

	public void setPaletteColor(Color c) {
		paletteColor = c;
	}

	public Color getPaletteColor() {
		return paletteColor;
	}

	public void setStrokeSize(int sz) {
		strokeSize = sz;
		this.updateAllViews();
	}

	public int getStrokeSize() {
		return strokeSize;
	}

	public void setState(State passedState) {
		// remove the selected items & lasso trace
		if (passedState == State.draw || passedState == State.erase
				|| passedState == State.playing) {
			selectedIndices.clear();
			this.removeLasso();
		}

		state = passedState;
		this.updateAllViews();
	}

	public State getState() {
		return state;
	}

	public void pushFrame() {
		for (Segment s : segments) {
			if (!s.isErased(currframe) && s.getEndTime() <= currframe) {
				s.createFrame(currframe + 1);
			}
		}
		currframe++;
		if (currframe > totalframes) {
			totalframes++;
		}
		this.updateAllViews();
	}

	// increment totalframes, insert a copy of current frame for each segment.
	public void insertFrame() {
		increaseFrames(true);
		for (Segment s : segments) {
			if (currframe > totalframes) {
				s.createFrame(currframe);
			} else {
				s.copyFrame(currframe);
			}
		}
		System.out.println("Inserted frame.");

		this.updateAllViews();
	}

	public void addPointToSegment(Point point) {
		if (state == State.draw) {
			currSegment.addPoint(point);
			if (stillDragging && segments.size() == 0) {
				segments.add(currSegment);
			}
			segments.set(segments.size() - 1, currSegment);
		} else if (state == State.selection
				&& this.getSelectedIndices().size() == 0) {
			selectingSegment.addPoint(point);
		}
		this.updateAllViews();
	}

	public void createSegment() {
		currSegment = new Segment(currframe, currframe, this.getPaletteColor(),
				strokeSize);
		segments.add(currSegment);
	}

	public ArrayList<Segment> getSegments() {
		return segments;
	}

	public Segment getSelectingSegment() {
		return selectingSegment;
	}

	public void addTranslate(int x, int y) {
		int currFrame = this.getFrame();
		for (int index : selectedIndices) {
			segments.get(index).setSegmentTranslate(x, y, currFrame);
		}
	}

	public void eraseAction(int oldX, int oldY) {
		int largestEndTime = 0, currEndTime = 0;
		for (Segment s : segments) {
			ArrayList<Point> points = s.getTranslates(this.getFrame());

			for (int j = 0; j < points.size(); j++) {
				Point currPoint = points.get(j);
				int x = currPoint.x;
				int y = currPoint.y;

				if (oldX > x - 10 && oldX < x + 10 && oldY > y - 10
						&& oldY < y + 10) {
					// System.out.println("Erasing the " + i + "th obj");
					s.setEndTime(currframe - 1);
					break;
				}
			}
			currEndTime = s.getEndTime();
			if (currEndTime > largestEndTime) {
				largestEndTime = currEndTime;
			}
		}
		if (largestEndTime < totalframes) {
			totalframes = largestEndTime;
			currframe = largestEndTime;
			System.out.println("Number of frames cut down to  " + totalframes);
		}
		this.updateAllViews();
	}

	public void selectAction(GeneralPath lassoPath) {
		for (int i = 0; i < segments.size(); i++) {
			ArrayList<Point> points = segments.get(i).getTranslates(
					this.getFrame());
			int size = points.size();
			for (int j = 0; j < size; j++) {
				Point currPoint = points.get(j);
				if (!lassoPath.contains(currPoint)) {
					break;
				}
				// all pts inside lasso, so we can select this segment
				else if (j == size - 1) {
					System.out.println("Selected!");
					this.addSelectedIndex(i);
				}
			}
		}
		this.removeLasso();
	}

	public void deselect() {
		selectedIndices.clear();
		this.removeLasso();
		state = State.selection; // allow user to select again.
		this.updateAllViews();
	}

	public void removeLasso() {
		selectingSegment = new Segment(currframe, currframe, paletteColor,
				strokeSize);
		this.updateAllViews();
	}

	public void setStillDragging(boolean stillDragging) {
		this.stillDragging = stillDragging;
	}

	public boolean getStillDragging() {
		return this.stillDragging;
	}

	public void addSelectedIndex(int selected) {
		selectedIndices.add(selected);
	}

	public ArrayList<Integer> getSelectedIndices() {
		return this.selectedIndices;
	}

	public int getFrame() {
		return this.currframe;
	}

	public int getTotalFrames() {
		return this.totalframes;
	}

	public void setFrame(int frame) {
		if (frame <= totalframes) {
			this.currframe = frame;
			this.updateAllViews();
		}
	}

	public void gotoZero() {
		currframe = 0;
		this.updateAllViews();
	}

	public void increaseFrames(boolean copyframe) {
		currframe++;
		if (currframe > totalframes || copyframe) {
			totalframes++;
		}
		this.updateAllViews();
	}

	public void decreaseFrames() {
		if (currframe > 0) {
			currframe--;
			this.updateAllViews();
		}
	}


	public void exportImage(){
		  try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	 
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("animation");
			doc.appendChild(rootElement);
	 
			Element segments = doc.createElement("segments");
			rootElement.appendChild(segments);
			
			for (Segment s : this.getSegments()){
				Element segment = doc.createElement("segment");
				segments.appendChild(segment);
				
				Element color = doc.createElement("color");
				color.appendChild(doc.createTextNode(String.valueOf(s.getColor().getRGB())));
				segment.appendChild(color);
		 
				Element stroke = doc.createElement("stroke");
				stroke.appendChild(doc.createTextNode(String.valueOf(s.getStroke())));
				segment.appendChild(stroke);
		 
				Element start = doc.createElement("start");
				start.appendChild(doc.createTextNode(String.valueOf(s.getStartTime())));
				segment.appendChild(start);
				
				Element end = doc.createElement("end");
				end.appendChild(doc.createTextNode(String.valueOf(s.getEndTime())));
				segment.appendChild(end);
				
				Element points = doc.createElement("points");
				segment.appendChild(points);
		 
				for (int m = 0; m < s.size(); m++){
					Point p = s.getPoint(m);
					Element point = doc.createElement("point");
					points.appendChild(point);
					
					Element x = doc.createElement("xvalue");
					Element y = doc.createElement("yvalue");
					x.appendChild(doc.createTextNode(String.valueOf(p.getX())));
					y.appendChild(doc.createTextNode(String.valueOf(p.getY())));
					point.appendChild(x);
					point.appendChild(y);
				}
				
				Element transforms = doc.createElement("transforms");
				segment.appendChild(transforms);
				
				for (int i = 0; i < s.getEndTime() - s.getStartTime(); i++){ // check bounds
					
					Element transform = doc.createElement("transform");
					transforms.appendChild(transform);
					
					ArrayList<Point> pointlist = s.getTranslates(i);
					for (Point p : pointlist){
						Element point = doc.createElement("point");
						transform.appendChild(point);
						
						Element x = doc.createElement("xvalue");
						Element y = doc.createElement("yvalue");
						x.appendChild(doc.createTextNode(String.valueOf(p.getX())));
						y.appendChild(doc.createTextNode(String.valueOf(p.getY())));
						point.appendChild(x);
						point.appendChild(y);
					}
				}
			}
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("../xml-files/file.xml"));
	 
			// Output to console for testing
			//StreamResult result = new StreamResult(System.out);
	 
			transformer.transform(source, result);
	 
			System.out.println("File saved!");
	 
		  } catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		  } catch (TransformerException tfe) {
			tfe.printStackTrace();
		  }
	}
	
	public void restart() {
		segments.clear();
		selectingSegment = new Segment(currframe, currframe, paletteColor,
				strokeSize);
		selectedIndices.clear();
		totalframes = 0;
		currframe = 0;
		this.updateAllViews();
	}

	public void addView(IView view) {
		views.add(view);
		view.updateView();
	}

	public void removeView(IView view) {
		views.remove(view);
	}

	private void updateAllViews() {
		for (IView view : this.views) {
			view.updateView();
		}
	}
}