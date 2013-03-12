package view;

import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSlider;

import model.IView;
import model.MainViewModel;

public class SliderView extends JComponent implements IView {

	private static final long serialVersionUID = 1L;
	
	static final int FPS_MIN = 0;
	static final int FPS_MAX = 30;
	static final int FPS_INIT = 15;    //initial frames per second

	
	public SliderView(MainViewModel aModel) {
		super();
		//this.model = aModel;
		//this.layoutView();
		//this.registerControllers();

		// Add a this view as a listener to the model
		//this.model.addView(this);
		
		
        //Create the label.
        JLabel sliderLabel = new JLabel("Frames Per Second", JLabel.CENTER);
        sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        //Create the slider.
        JSlider framesPerSecond = new JSlider(JSlider.HORIZONTAL,
                                              FPS_MIN, FPS_MAX, FPS_INIT);
         
// 
//        framesPerSecond.addChangeListener(this);
// 
        //Turn on labels at major tick marks.
 
        framesPerSecond.setMajorTickSpacing(10);
        framesPerSecond.setMinorTickSpacing(1);
        framesPerSecond.setPaintTicks(true);
        framesPerSecond.setPaintLabels(true);
        framesPerSecond.setBorder(
                BorderFactory.createEmptyBorder(0,0,10,0));
        Font font = new Font("Serif", Font.ITALIC, 15);
        framesPerSecond.setFont(font);
 
        //Put everything together.
        add(sliderLabel);
        add(framesPerSecond);
        
        
	}


	@Override
	public void updateView() {
		// TODO Auto-generated method stub
		
	}
}
