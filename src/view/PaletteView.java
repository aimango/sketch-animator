package view;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.IView;
import model.MainModel;

public class PaletteView extends JPanel implements IView {

	private static final long serialVersionUID = 1L;
	private MainModel model;

	JColorChooser tcc;

	public PaletteView(MainModel aModel) {
		super();
		this.setLayout(new GridLayout(7, -1));
		this.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		tcc = new JColorChooser();
		tcc.setBorder(BorderFactory.createTitledBorder("Choose Draw Color"));

		final JButton choose = new JButton("Picker");
		choose.setBorderPainted(false);
		choose.setOpaque(true);
		choose.setFocusable(false);
		choose.setBackground(Color.GRAY);
		choose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JDialog d = new JDialog();
				d.add(tcc);
				d.pack();
				d.setVisible(true);
			}
		});
		tcc.getSelectionModel().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				Color newColor = tcc.getColor();
				model.setPaletteColor(newColor);
				choose.setBackground(newColor);
				choose.setBorderPainted(false);
				choose.setOpaque(true);
			}
		});
		JButton black = new JButton();
		black.setBackground(Color.BLACK);
		black.setBorderPainted(false);
		black.setOpaque(true);
		black.setFocusable(false);
		black.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.setPaletteColor(Color.BLACK);
			}
		});

		JButton blu = new JButton();
		blu.setBackground(new Color(51, 204, 255));
		blu.setBorderPainted(false);
		blu.setOpaque(true);
		blu.setFocusable(false);
		blu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.setPaletteColor(new Color(51, 204, 255));
			}
		});
		JButton green = new JButton();
		green.setBackground(new Color(51, 255, 102));
		green.setBorderPainted(false);
		green.setOpaque(true);
		green.setFocusable(false);
		green.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.setPaletteColor(new Color(51, 255, 102));
			}
		});
		JButton violet = new JButton();
		violet.setBackground(new Color(204, 51, 255));
		violet.setBorderPainted(false);
		violet.setOpaque(true);
		violet.setFocusable(false);
		violet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.setPaletteColor(new Color(204, 51, 255));
			}
		});
		JButton oj = new JButton();
		oj.setBackground(new Color(255, 102, 51));
		oj.setBorderPainted(false);
		oj.setOpaque(true);
		oj.setFocusable(false);
		oj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.setPaletteColor(new Color(255, 102, 51));
			}
		});

		JButton pink = new JButton();
		pink.setBackground(new Color(249, 119, 214));
		pink.setBorderPainted(false);
		pink.setOpaque(true);
		pink.setFocusable(false);
		pink.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.setPaletteColor(new Color(249, 119, 214));
			}
		});

		this.add(black);
		this.add(blu);
		this.add(green);
		this.add(violet);
		this.add(oj);
		this.add(pink);
		this.add(choose);

		model = aModel;
		this.model.addView(this);
	}

	@Override
	public void updateView() {
	}
}
