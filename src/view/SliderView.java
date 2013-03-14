package view;

import javax.swing.BorderFactory;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.IView;
import model.MainModel;

//Slider - Use ChangeListener since ActionListener only detects when you release the slider, not as you drag it.
//Need to 

public class SliderView extends JSlider implements IView {

	private static final long serialVersionUID = 1L;
	private MainModel model;
	
	
	public SliderView(MainModel aModel) {
		super();
		this.model = aModel;
		this.registerControllers();
		this.model.addView(this);
		this.setValue(0);
		this.setBorder(BorderFactory.createTitledBorder("Yippee"));
		this.setMaximum(1);
		this.setPaintTicks(true);
		this.setPaintLabels(true);  
	}

	private void registerControllers() {
	    this.addChangeListener(new ChangeListener() {
	        public void stateChanged(ChangeEvent evt) {
	          JSlider slider = (JSlider) evt.getSource();
	          if (slider.getValueIsAdjusting()) {
		          model.setFrame(slider.getValue());
	          }
	        }
	    });
	}
	
	@Override
	public void updateView() {
		this.setMaximum(model.getTotalFrames());
		this.setValue(model.getFrame());
		this.setMinorTickSpacing(1);
	}
}
