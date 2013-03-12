
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import model.MainViewModel;

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
			}
		});
		
		JPanel panel = new JPanel();
		panel.add(clearButton);
		content.add(panel, BorderLayout.NORTH);
		

		frame.pack();
		frame.setSize(800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

	}
}
