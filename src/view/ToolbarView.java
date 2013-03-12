package view;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import model.IView;
import model.MainViewModel;

public class ToolbarView extends JPanel implements IView {

	private static final long serialVersionUID = 1L;

	private MainViewModel model;

	public ToolbarView(MainViewModel aModel) {
		super();
		this.model = aModel;
		//this.layoutView();
		//this.registerControllers();

		// Add a this view as a listener to the model
		this.model.addView(this);
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		JButton clearButton = new JButton("Clear");
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.setClear(true); 
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); 
			}
		});
		
		JButton drawToggle = new JButton("Draw");
		drawToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//stuff
				model.setState(0);
				setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		});
		JButton eraseToggle = new JButton("Erase");
		eraseToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//change the pointer to something else
				model.setState(1);
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		});
		JButton selectToggle = new JButton("Select");
		selectToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//change the pointer to something else
				model.setState(2);
				setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
			}
		});
		
		this.add(clearButton);
		this.add(drawToggle);
		this.add(eraseToggle);
		this.add(selectToggle);
	}


	@Override
	public void updateView() {
		// TODO Auto-generated method stub
		
	}
}
