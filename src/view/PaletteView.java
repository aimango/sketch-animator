package view;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import model.IView;
import model.MainModel;

//TODO; use this for color picker. need to use gridbag or some vertical layout
public class PaletteView extends JPanel implements IView {

	private static final long serialVersionUID = 1L;
	private MainModel model;

	public PaletteView(MainModel aModel) {
		super();

		this.setLayout(new GridLayout(0, 1));

		ImageIcon orange = new ImageIcon(getClass().getResource("/orange.png"));
		ImageIcon blue = new ImageIcon(getClass().getResource("/blue.png"));
		ImageIcon lime = new ImageIcon(getClass().getResource("/green.png"));
		ImageIcon purple = new ImageIcon(getClass().getResource("/purple.png"));
		ImageIcon blk = new ImageIcon(getClass().getResource("/black.png"));
		JButton oj = new JButton(orange);
		oj.setFocusable(false);
		oj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.setPaletteColor(new Color(255, 102, 51));
			}
		});
		JButton blu = new JButton(blue);
		blu.setFocusable(false);
		blu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.setPaletteColor(new Color(51, 204, 255));
			}
		});
		JButton green = new JButton(lime);
		green.setFocusable(false);
		green.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.setPaletteColor(new Color(51, 255, 102));
			}
		});
		JButton violet = new JButton(purple);
		violet.setFocusable(false);
		violet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.setPaletteColor(new Color(204, 51, 255));
			}
		});
		JButton black = new JButton(blk);
		black.setFocusable(false);
		black.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.setPaletteColor(Color.BLACK);
			}
		});

		this.add(oj);
		this.add(blu);
		this.add(green);
		this.add(violet);
		this.add(black);

		model = aModel;
		// Add a this view as a listener to the model
		this.model.addView(this);
	}

	@Override
	public void updateView() {
	}
}
