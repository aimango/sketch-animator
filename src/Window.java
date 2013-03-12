
import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;

import model.MainViewModel;
import view.SliderView;
import view.ToolbarView;

// should make a view just for the toolbar.. would still add as North of the BorderLayout
//Slider should be a view too, that extends IView.
// Use ChangeListener since ActionListener only detects when you release the slider, not as you drag it.

public class Window {
	public static void main(String[] args) {

		MainViewModel model = new MainViewModel();
		final view.CanvasView view = new view.CanvasView(model);

		JFrame frame = new JFrame("Assignment 3 - MVC");
		Container content = frame.getContentPane();
		content.setLayout(new BorderLayout());
		frame.getContentPane().add(view, BorderLayout.CENTER);

		ToolbarView toolbar = new view.ToolbarView(model);
		content.add(toolbar, BorderLayout.NORTH);

		SliderView slider = new view.SliderView(model);
		content.add(slider, BorderLayout.SOUTH);
	    
		frame.pack();
		frame.setSize(800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

	}
}
