package view;

import javax.swing.BoundedRangeModel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


// http://www.java2s.com/Tutorial/Java/0240__Swing/TrackingchangestoaJSliderwithaChangeListener.htm
// need to store current slider value in model.

class BoundedChangeListener implements ChangeListener {
	public void stateChanged(ChangeEvent changeEvent) {
		Object source = changeEvent.getSource();
		if (source instanceof BoundedRangeModel) {
			BoundedRangeModel aModel = (BoundedRangeModel) source;
			if (!aModel.getValueIsAdjusting()) {
				System.out.println("Changed: " + aModel.getValue());
			}
		} else if (source instanceof JSlider) {
			JSlider theJSlider = (JSlider) source;
			//if (!theJSlider.getValueIsAdjusting()) {
			System.out.println("Slider changed: " + theJSlider.getValue());
  //}
   // } else {
//  System.out.println("Something changed: " + source);
		}
	}
}