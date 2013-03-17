package view;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.IView;
import model.MainModel;

public class SliderView extends JSlider implements IView {

	private static final long serialVersionUID = 1L;
	private MainModel model;

	public SliderView(MainModel aModel) {
		super();
		model = aModel;
		model.addView(this);
		this.registerControllers();
		
		this.setValue(0);
		this.setMaximum(1);
		this.setMajorTickSpacing(20);
		this.setPaintTicks(true);
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
	}
}
