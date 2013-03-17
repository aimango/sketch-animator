package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import model.IView;
import model.MainModel;

public class ToolbarView extends JPanel implements IView {

	private static final long serialVersionUID = 1L;
	private MainModel model;
	private JButton drawToggle, eraseToggle, selectToggle;
	private JButton clearButton, insertFrame;
	ImageIcon select, deselect;

	public ToolbarView(MainModel aModel) {
		super();

		ImageIcon trash = new ImageIcon(getClass().getResource("/trash.png"));
		clearButton = new JButton(trash);
		clearButton.setFocusable(false);

		ImageIcon draw = new ImageIcon(getClass().getResource("/draw2.png"));
		drawToggle = new JButton(draw);
		drawToggle.setFocusable(false);

		ImageIcon erase = new ImageIcon(getClass().getResource("/eraser.png"));
		eraseToggle = new JButton(erase);
		eraseToggle.setFocusable(false);

		select = new ImageIcon(getClass().getResource("/dotssquare.png"));
		deselect = new ImageIcon(getClass().getResource(
				"/dotssquare_deselect.png"));
		selectToggle = new JButton(select);
		selectToggle.setFocusable(false);

		ImageIcon copy = new ImageIcon(getClass().getResource("/copy.png"));
		insertFrame = new JButton(copy);
		insertFrame.setFocusable(false);

		this.add(drawToggle);
		this.add(eraseToggle);
		this.add(selectToggle);
		this.add(insertFrame);
		this.add(clearButton);
		this.registerListeners();
		model = aModel;
		model.addView(this);
	}

	private void registerListeners() {
		drawToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.setState(MainModel.State.draw);
			}
		});

		eraseToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.setState(MainModel.State.erase);
			}
		});

		selectToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (model.getState() == MainModel.State.selection) {
					model.deselect();
				} else {
					model.setState(MainModel.State.selection);
				}
			}
		});

		insertFrame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.insertFrame();
			}
		});

		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.restart();
			}
		});
	}

	@Override
	public void updateView() {

		MainModel.State state = model.getState();
		// disable everything during playback
		if (state == MainModel.State.playing) {
			eraseToggle.setEnabled(false);
			drawToggle.setEnabled(false);
			insertFrame.setEnabled(false);
			clearButton.setEnabled(false);
			selectToggle.setEnabled(false);
		} else {
			eraseToggle.setEnabled(true);
			drawToggle.setEnabled(true);
			insertFrame.setEnabled(true);
			clearButton.setEnabled(true);
			selectToggle.setEnabled(true);
		}
		if (state == MainModel.State.draw) {
			drawToggle.setEnabled(false);
		} else if (state == MainModel.State.erase) {
			eraseToggle.setEnabled(false);
		}
		if (state == MainModel.State.selection
				&& model.getSelectedIndices().size() > 0) {
			selectToggle.setIcon(deselect);
		} else {
			selectToggle.setIcon(select);
		}
	}
}
