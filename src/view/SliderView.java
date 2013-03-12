package view;

import javax.swing.BorderFactory;
import javax.swing.JSlider;

import model.IView;
import model.MainViewModel;

public class SliderView extends JSlider implements IView {

	private static final long serialVersionUID = 1L;
	
	private MainViewModel model;

	
	public SliderView(MainViewModel aModel) {
		super();
		this.model = aModel;
		//this.layoutView();
		//this.registerControllers();

		// Add a this view as a listener to the model
		this.model.addView(this);
		
		this.setBorder(BorderFactory.createTitledBorder("Yippee"));
		this.setMajorTickSpacing(20);
		this.setMinorTickSpacing(5);
		this.setPaintTicks(true);
		this.setPaintLabels(true);  
	}


	@Override
	public void updateView() {
		// TODO Auto-generated method stub
	}
}
