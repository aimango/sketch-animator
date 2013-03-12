
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


import view.SliderView;
import model.MainViewModel;

// should make a view just for the toolbar.. would still add as North of the BorderLayout
//Slider should be a view too, that extends IView.
// Use ChangeListener since ActionListener only detects when you release the slider, not as you drag it.

public class Window {
	public static void main(String[] args) {

		MainViewModel model = new MainViewModel();
		final view.MainView view = new view.MainView(model);

		JFrame frame = new JFrame("Assignment 3 - MVC");
		Container content = frame.getContentPane();
		content.setLayout(new BorderLayout());
		frame.getContentPane().add(view, BorderLayout.CENTER);

		JButton clearButton = new JButton("Clear");
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view.clear();
				view.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		});
		
		JButton drawToggle = new JButton("Draw");
		drawToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//stuff
				view.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		});
		JButton eraseToggle = new JButton("Erase");
		eraseToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//change the pointer to something else
				view.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		});
		JButton selectToggle = new JButton("Select");
		selectToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//change the pointer to something else
				view.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
			}
		});
		
		JPanel panel = new JPanel();
		panel.add(clearButton);
		panel.add(drawToggle);
		panel.add(eraseToggle);
		panel.add(selectToggle);
		content.add(panel, BorderLayout.NORTH);
		

		JSlider slider3 = new JSlider();
		slider3.setBorder(BorderFactory.createTitledBorder("Yippee"));
		slider3.setMajorTickSpacing(20);
		slider3.setMinorTickSpacing(5);
		slider3.setPaintTicks(true);
		slider3.setPaintLabels(true);
		content.add(slider3, BorderLayout.SOUTH);
	    
		frame.pack();
		frame.setSize(800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

	}
}
