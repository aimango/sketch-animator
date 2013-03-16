package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import model.IView;
import model.MainModel;

//TODO: have text under icons
public class ToolbarView extends JPanel implements IView {

	private static final long serialVersionUID = 1L;
	private MainModel model;
	private JButton drawToggle, eraseToggle, selectToggle, deselectToggle;
	private JButton clearButton, insertFrame;

	public ToolbarView(MainModel aModel) {
		super();
		this.model = aModel;
		ImageIcon trash = new ImageIcon(getClass().getResource("/trash.png"));
		clearButton = new JButton(trash);
		clearButton.setFocusable(false);
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.restart();
			}
		});
		ImageIcon draw = new ImageIcon(getClass().getResource("/draw2.png"));
		drawToggle = new JButton(draw);
		drawToggle.setFocusable(false);
		drawToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.setState(MainModel.State.draw);
			}
		});
		ImageIcon erase = new ImageIcon(getClass().getResource("/eraser.png"));
		eraseToggle = new JButton(erase);
		eraseToggle.setFocusable(false);
		eraseToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.setState(MainModel.State.erase);
			}
		});
		ImageIcon select = new ImageIcon(getClass().getResource(
				"/dotssquare.png"));
		selectToggle = new JButton(select);
		selectToggle.setFocusable(false);
		selectToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.setState(MainModel.State.selection);
			}
		});
		ImageIcon deselect = new ImageIcon(getClass().getResource(
				"/deselect.png"));
		deselectToggle = new JButton(deselect);
		deselectToggle.setFocusable(false);
		deselectToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.deselect();
			}
		});
		ImageIcon copy = new ImageIcon(getClass().getResource("/copy.png"));
		insertFrame = new JButton(copy);
		insertFrame.setFocusable(false);
		insertFrame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.insertFrame();
			}
		});

		this.add(clearButton);
		this.add(drawToggle);
		this.add(eraseToggle);
		this.add(selectToggle);
		this.add(deselectToggle);
		this.add(insertFrame);
		//this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		// Add a this view as a listener to the model
		this.model.addView(this);
	}

	@Override
	public void updateView() {
		int totalFrames = model.getTotalFrames();

		MainModel.State state = model.getState();
		// disable everything during playback
		if (state == MainModel.State.playing) { 
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
		if (state == MainModel.State.draw) {
			drawToggle.setEnabled(false);
		} else if (state == MainModel.State.erase) {
			eraseToggle.setEnabled(false);
		} else if (state == MainModel.State.selection) {
			selectToggle.setEnabled(false);
			deselectToggle.setEnabled(true);
		}
	}
}
