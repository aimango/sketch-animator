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
	private JButton playToggle;
	public ToolbarView(MainModel aModel) {
		super();
		this.model = aModel;
		
		// Add a this view as a listener to the model
		this.model.addView(this);
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
		
		JButton clearButton = new JButton("Clear");
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.restart(); 
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
		
		JButton back = new JButton("<<");
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.decreaseFrames();
			}
		});
		JButton fwd = new JButton(">>");
		fwd.addActionListener(new ActionListener() {
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
		this.add(back);
		this.add(fwd);
		this.add(playToggle);
	}

	@Override
	public void updateView() {
		//TODO: can do some state disabling here
		
		if (model.getFrame() == 0 && model.getTotalFrames() > 0){
			playToggle.setText("Play");
		}
	}
}
