import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.metal.MetalSliderUI;


public class BCframe extends JFrame implements ChangeListener, ActionListener, MouseListener{

	private float cB;
	private float cC;

	private JSlider _brightness;
	private JSlider _contrast;


	private JButton _ok = new JButton("OK");
	private JButton _cancel = new JButton("Cancel");
	private JButton _default = new JButton("Restore Default");

	private Dimension prefSize;
	private Dimension prefSizeC;

	private PlayFrame _pf;

	private JPanel _panel = new JPanel();
	private JPanel _panelB = new JPanel();
	private JPanel _panelC = new JPanel();
	private JPanel _panelButtons = new JPanel();

	BCframe(PlayFrame pf, float currentB, float currentC){
		setSize(330, 170);

		_pf = pf;
		cB = currentB;
		cC = currentC;
		
		_brightness = new JSlider(0, 200, (int)(cB*100));
		_contrast = new JSlider(0, 200, (int)(cC*100));
		
		_brightness.setMajorTickSpacing(25);  
		_brightness.setPaintTicks(true);  
		java.util.Hashtable<Integer,JLabel> labelTable = new java.util.Hashtable<Integer,JLabel>();
		labelTable.put(new Integer(200), new JLabel("2.0"));
		labelTable.put(new Integer(150), new JLabel("1.50"));
		labelTable.put(new Integer(100), new JLabel("1.0"));
		labelTable.put(new Integer(50), new JLabel("0.50"));
		labelTable.put(new Integer(0), new JLabel("0.0"));
		_brightness.setLabelTable( labelTable );
		_brightness.setPaintLabels(true);

		_contrast.setMajorTickSpacing(25);  
		_contrast.setPaintTicks(true);  
		java.util.Hashtable<Integer,JLabel> labelTableC = new java.util.Hashtable<Integer,JLabel>();
		labelTableC.put(new Integer(200), new JLabel("2.0"));
		labelTableC.put(new Integer(150), new JLabel("1.50"));
		labelTableC.put(new Integer(100), new JLabel("1.0"));
		labelTableC.put(new Integer(50), new JLabel("0.50"));
		labelTableC.put(new Integer(0), new JLabel("0.0"));
		_contrast.setLabelTable( labelTable );
		_contrast.setPaintLabels(true);




		_panel.setLayout(new BoxLayout(_panel, BoxLayout.Y_AXIS));
		prefSize = _brightness.getPreferredSize();
		prefSize.width = 270;
		prefSizeC = _contrast.getPreferredSize();
		prefSizeC.width = 270;
		_brightness.setPreferredSize(prefSize);
		_contrast.setPreferredSize(prefSizeC);

		ImageIcon b = new ImageIcon("./src/brightness.png");
		Image img = b.getImage();
		Image newimg = img.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
		ImageIcon newIcon = new ImageIcon(newimg);
		JLabel br = new JLabel(newIcon);
		br.setSize(20, 20);

		ImageIcon c = new ImageIcon("./src/contrast.png");
		Image imgC = c.getImage();
		Image newimgC = imgC.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
		ImageIcon newIconC = new ImageIcon(newimgC);
		JLabel con = new JLabel(newIconC);
		con.setSize(20, 20);

		_panelB.setLayout(new FlowLayout());
		_panelC.setLayout(new FlowLayout());
		_panelButtons.setLayout(new FlowLayout(FlowLayout.RIGHT));

		_panelB.add(br);
		_panelB.add(_brightness);

		_panelC.add(con);
		_panelC.add(_contrast);

		_panelButtons.add(_ok);
		_panelButtons.add(_cancel);
		_panelButtons.add(_default);

		_panel.add(_panelB);
		_panel.add(_panelC);
		_panel.add(_panelButtons);

		add(_panel);
		setTitle("Set Brightness/Contrast");

		_brightness.addChangeListener(this);
		_contrast.addChangeListener(this);
		_brightness.addMouseListener(this);
		_contrast.addMouseListener(this);

		_default.addActionListener(this);
		_ok.addActionListener(this);
		_cancel.addActionListener(this);

	}


	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource() == _brightness){
			JSlider source = (JSlider)e.getSource();
			float br = (float)(source.getValue()/100.0);
			_pf.setBrightness(br);
		}else if(e.getSource() == _contrast){
			JSlider source = (JSlider)e.getSource();
			float con = (float)(source.getValue()/100.0);
			_pf.setContrast(con);
		}
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == _ok){
			this.dispose();
			this.setVisible(false);
		}else if(e.getSource() == _cancel){
			_pf.setBrightness(cB);
			_pf.setContrast(cC);
			this.dispose();
			this.setVisible(false);
		}else if(e.getSource() == _default){
			_pf.setBrightness(1.0f);
			_pf.setContrast(1.0f);
			_brightness.setValue(100);
			_contrast.setValue(100);
		}
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource() == _brightness){
			_brightness.setUI(new MetalSliderUI() {
				protected void scrollDueToClickInTrack(int direction) {

					int _value = _brightness.getValue(); 

					if (_brightness.getOrientation() == JSlider.HORIZONTAL) {
						_value = this.valueForXPosition(_brightness.getMousePosition().x);

					}
					_brightness.setValue(_value);
					_pf.setBrightness((float)_value/100.0f);
				}
			});
		}else if(e.getSource() == _contrast){
			JSlider source = (JSlider)e.getSource();
			float con = (float)(source.getValue()/100.0);
			_pf.setContrast(con);
		}
	}


	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}


	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}


	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}


	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}






}
