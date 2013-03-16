package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import model.IView;
import model.MainModel;

public class PlayerView extends JPanel implements IView {

	private static final long serialVersionUID = 1L;
	private MainModel model;
	private JButton playBtn, fwdBtn, rewindBtn, fastFwd, fastRewind;
	private ImageIcon play;

	public PlayerView(MainModel aModel) {
		super();
		this.model = aModel;

		ImageIcon fwd = new ImageIcon(getClass().getResource("/fwd.png"));
		fwdBtn = new JButton(fwd);
		fwdBtn.setFocusable(false);
		fwdBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.increaseFrames();
			}
		});
		
		ImageIcon rewind = new ImageIcon(getClass().getResource("/rewind.png"));
		rewindBtn = new JButton(rewind);
		rewindBtn.setFocusable(false);
		rewindBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.decreaseFrames();
			}
		});
		

		ImageIcon fastfw = new ImageIcon(getClass().getResource("/end.png"));
		fastFwd = new JButton(fastfw);
		fastFwd.setFocusable(false);
		fastFwd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.setFrame(model.getTotalFrames());
			}
		});
		
		ImageIcon fastrw = new ImageIcon(getClass().getResource("/begin.png"));
		fastRewind = new JButton(fastrw);
		fastRewind.setFocusable(false);
		fastRewind.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.gotoZero();
			}
		});
		
		play = new ImageIcon(getClass().getResource("/play.png"));
		playBtn = new JButton(play);
		playBtn.setFocusable(false);

		final ImageIcon pause = new ImageIcon(getClass().getResource(
				"/pause.png"));
		
		playBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (playBtn.getIcon() == play) {

					// play from 0 in this case
					if (model.getFrame() == model.getTotalFrames())
						model.gotoZero();
					model.setState(MainModel.State.playing);
					playBtn.setIcon(pause);
				} else if (playBtn.getIcon() == pause) {
					model.setState(MainModel.State.draw);
					playBtn.setIcon(play);
				}
			}
		});
		this.add(fastRewind);
		this.add(rewindBtn);
		this.add(playBtn);
		this.add(fwdBtn);
		this.add(fastFwd);
		
		// Add a this view as a listener to the model
		this.model.addView(this);
	}

	@Override
	public void updateView() {
		int totalFrames = model.getTotalFrames();
		// allow play if there is animation to play
		if (totalFrames > 0) {
			rewindBtn.setEnabled(true);
			fwdBtn.setEnabled(true);
			playBtn.setEnabled(true);
			fastRewind.setEnabled(true);
			fastFwd.setEnabled(true);
		}
		else if (totalFrames == 0) {
			rewindBtn.setEnabled(false);
			fwdBtn.setEnabled(false);
			playBtn.setEnabled(false);
			fastRewind.setEnabled(false);
			fastFwd.setEnabled(false);
		}

		MainModel.State state = model.getState();
		// disable everything during playback
		if (state != MainModel.State.playing) { 
			playBtn.setIcon(play);
		}

		// disable play if currently animating
		if (state == MainModel.State.dragged) {
			rewindBtn.setEnabled(false);
			fwdBtn.setEnabled(false);
			playBtn.setEnabled(false);
			fastRewind.setEnabled(false);
			fastFwd.setEnabled(false);
		}

	}
}
