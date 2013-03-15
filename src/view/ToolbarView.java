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
	private JButton clearButton, drawToggle, eraseToggle, selectToggle, deselectToggle;
	private JButton insertFrame, playToggle;
	public ToolbarView(MainModel aModel) {
		super();
		this.model = aModel;

		setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		playToggle = new JButton("Play");
		playToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (playToggle.getText() == "Play"){
					
					// play from 0 in this case
					if (model.getFrame() == model.getTotalFrames())
						model.gotoZero();
					model.setState(5);
					playToggle.setText("Pause");
				} 
				else if (playToggle.getText() == "Pause"){
					model.setState(0);
					playToggle.setText("Play");
				}
			}
		});
		
		clearButton = new JButton("Clear");
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.restart(); 
			}
		});
		
		drawToggle = new JButton("Draw");
		drawToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.setState(0);
			}
		});
		eraseToggle = new JButton("Erase");
		eraseToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.setState(1);
			}
		});
		selectToggle = new JButton("Select");
		selectToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.setState(2);
			}
		});
		deselectToggle = new JButton("Deselect");
		deselectToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.setState(3);
			}
		});
		insertFrame = new JButton("Insert");
		insertFrame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//increment totalframes, insert a copy of current frame.
			}
		});


		this.add(clearButton);
		this.add(drawToggle);
		this.add(eraseToggle);
		this.add(selectToggle);
		this.add(deselectToggle);
		this.add(insertFrame);
		this.add(playToggle);
		
		
		// Add a this view as a listener to the model
		this.model.addView(this);
	}

	@Override
	public void updateView() {
		//TODO: can do some state disabling here
		int state = model.getState();
		if (state == 5){ // disable everything during playback
			eraseToggle.setEnabled(false);
			drawToggle.setEnabled(false);
			insertFrame.setEnabled(false);
			clearButton.setEnabled(false);
			selectToggle.setEnabled(false);
			deselectToggle.setEnabled(false);
		} else {
			eraseToggle.setEnabled(true);
			drawToggle.setEnabled(true);
			insertFrame.setEnabled(true);
			clearButton.setEnabled(true);
			selectToggle.setEnabled(true);
			deselectToggle.setEnabled(false);
		}
		if (state == 0){
			drawToggle.setEnabled(false);
		} else if (state == 1){
			eraseToggle.setEnabled(false);
		} else if (state == 2){
			selectToggle.setEnabled(false);
			deselectToggle.setEnabled(true);
		} 
		if (model.getFrame() == 0 && model.getTotalFrames() > 0){
			playToggle.setText("Play");
		}
	}
}
