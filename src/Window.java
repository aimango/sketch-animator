import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;

import model.MainModel;
import view.SliderView;
import view.ToolbarView;

public class Window {
	public static void main(String[] args) {

		MainModel model = new MainModel();
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
		frame.setLocation(100, 100);
		frame.setVisible(true);

	}
}
