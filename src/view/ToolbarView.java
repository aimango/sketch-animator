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
		
		// Add a this view as a listener to the model
		this.model.addView(this);
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		JButton clearButton = new JButton("Clear");
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.clear(); 
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); 
			}
		});
		
		JButton drawToggle = new JButton("Draw");
		drawToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.setState(0);
				setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		});
		JButton eraseToggle = new JButton("Erase");
		eraseToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.setState(1);
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		});
		JButton selectToggle = new JButton("Select");
		selectToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.setState(2);
				setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
			}
		});
		JButton deselectToggle = new JButton("Deselect");
		deselectToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.setState(3);
				setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
			}
		});
		JButton zeroToggle = new JButton("Time 0");
		zeroToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.gotoZero();
				setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		});
		
		JButton playToggle = new JButton("<<");
		playToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.decreaseFrames();
				setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		});
		JButton pauseToggle = new JButton(">>");
		pauseToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.increaseFrames();
				setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		});
		this.add(clearButton);
		this.add(drawToggle);
		this.add(eraseToggle);
		this.add(selectToggle);
		this.add(deselectToggle);
		this.add(zeroToggle);
		this.add(playToggle);
		this.add(pauseToggle);
	}

	@Override
	public void updateView() {
		// TODO Auto-generated method stub
		
	}
}
