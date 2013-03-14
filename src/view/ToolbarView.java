package view;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import model.IView;
import model.MainModel;

public class ToolbarView extends JPanel implements IView {

	private static final long serialVersionUID = 1L;

	private MainModel model;

	public ToolbarView(MainModel aModel) {
		super();
		this.model = aModel;
		
		// Add a this view as a listener to the model
		this.model.addView(this);
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		JButton clearButton = new JButton("Clear");
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.restart(); 
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); 
			}
		});
		
		JButton drawToggle = new JButton("Draw");
		drawToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.setState(0);
			}
		});
		JButton eraseToggle = new JButton("Erase");
		eraseToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.setState(1);
			}
		});
		JButton selectToggle = new JButton("Select");
		selectToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.setState(2);
			}
		});
		JButton deselectToggle = new JButton("Deselect");
		deselectToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.setState(3);
			}
		});
		JButton zeroToggle = new JButton("Time 0");
		zeroToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.gotoZero();
			}
		});
		
		JButton playToggle = new JButton("<<");
		playToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.decreaseFrames();
			}
		});
		JButton pauseToggle = new JButton(">>");
		pauseToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.increaseFrames();
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
		//can do some state disabling here
	}
}
