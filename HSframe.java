package GUI;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.metal.MetalSliderUI;

public class HSframe extends JFrame implements ChangeListener, ActionListener, MouseListener{

	private float cS;
	private int cH;
	private JSlider _hue;
	private JSlider _sat;
	
	private Dimension prefSizeH;
	private Dimension prefSizeS;
	
	private Dimension prefSH;
	private Dimension prefSS;

	private PlayFrame _pf;
	
	private JPanel _panel = new JPanel();
	private JPanel _panelH = new JPanel();
	private JPanel _panelS = new JPanel();
	private JPanel _panelButtons = new JPanel();
	private JLabel hue = new JLabel("Hue");
	private JLabel sat = new JLabel("Saturation");
	
	private JButton _ok = new JButton("OK");
	private JButton _cancel = new JButton("Cancel");
	private JButton _default = new JButton("Restore Default");

	HSframe(PlayFrame pf, int currentH, float currentS){
		setSize(340, 180);
		_pf = pf;
		cH = currentH;
		cS = currentS;
		
		_hue = new JSlider(0, 300, (int)(cH/1.2));
		_sat = new JSlider(0, 300, (int)(cS*100));

		_hue.setMajorTickSpacing(75);  
		_hue.setPaintTicks(true);  
		java.util.Hashtable<Integer,JLabel> labelTableH = new java.util.Hashtable<Integer,JLabel>();
		labelTableH.put(new Integer(300), new JLabel("360"));
		labelTableH.put(new Integer(225), new JLabel("270"));
		labelTableH.put(new Integer(150), new JLabel("180"));
		labelTableH.put(new Integer(75), new JLabel("90"));
		labelTableH.put(new Integer(0), new JLabel("0"));
		_hue.setLabelTable( labelTableH );
		_hue.setPaintLabels(true);
		
		
		_sat.setMajorTickSpacing(75);  
		_sat.setPaintTicks(true);  
		java.util.Hashtable<Integer,JLabel> labelTableS = new java.util.Hashtable<Integer,JLabel>();
		labelTableS.put(new Integer(300), new JLabel("3.0"));
		labelTableS.put(new Integer(225), new JLabel("2.25"));
		labelTableS.put(new Integer(150), new JLabel("1.5"));
		labelTableS.put(new Integer(75), new JLabel("0.75"));
		labelTableS.put(new Integer(0), new JLabel("0.0"));
		_sat.setLabelTable( labelTableS );
		_sat.setPaintLabels(true);
		
		prefSizeH = _hue.getPreferredSize();
		prefSizeH.width = 270;
		_hue.setPreferredSize(prefSizeH);
		
		prefSizeS = _sat.getPreferredSize();
		prefSizeS.width = 270;
		_sat.setPreferredSize(prefSizeS);
		
		_panelH.setLayout(new FlowLayout(FlowLayout.RIGHT));
		_panelS.setLayout(new FlowLayout(FlowLayout.RIGHT));
		_panelButtons.setLayout(new FlowLayout(FlowLayout.RIGHT));

		_panelH.add(hue);
		_panelH.add(_hue);
		
		_panelS.add(sat);
		_panelS.add(_sat);
		
		_panelButtons.add(_ok);
		_panelButtons.add(_cancel);
		_panelButtons.add(_default);

		_panel.add(_panelH);
		_panel.add(_panelS);
		_panel.add(_panelButtons);

		add(_panel);
		setTitle("Set Hue/Saturation");

		_hue.addChangeListener(this);
		_sat.addChangeListener(this);
		_hue.addMouseListener(this);
		_sat.addMouseListener(this);

		_default.addActionListener(this);
		_ok.addActionListener(this);
		_cancel.addActionListener(this);


	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource() == _hue){
			JSlider source = (JSlider)e.getSource();
			int h = (int) (source.getValue()*1.2);
			_pf.setHue(h);
		}else if(e.getSource() == _sat){
			JSlider source = (JSlider)e.getSource();
			float s = (float)(source.getValue()/100.0);
			_pf.setSat(s);
		}
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource() == _hue){
			_hue.setUI(new MetalSliderUI() {
				protected void scrollDueToClickInTrack(int direction) {

					int value = _hue.getValue(); 

					if (_hue.getOrientation() == JSlider.HORIZONTAL) {
						value = this.valueForXPosition(_hue.getMousePosition().x);

					}
					_hue.setValue(value);
					_pf.setHue((int)(value*1.2));
				}
			});
		}else if(e.getSource() == _sat){
			_sat.setUI(new MetalSliderUI() {
				protected void scrollDueToClickInTrack(int direction) {

					int value = _sat.getValue(); 

					if (_sat.getOrientation() == JSlider.HORIZONTAL) {
						value = this.valueForXPosition(_sat.getMousePosition().x);

					}
					_sat.setValue(value);
					_pf.setSat((float)value/100.0f);
				}
			});
		}
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == _ok){
			this.dispose();
			this.setVisible(false);
		}else if(e.getSource() == _cancel){
			_pf.setHue(cH);
			_pf.setSat(cS);
			this.dispose();
			this.setVisible(false);
		}else if(e.getSource() == _default){
			_pf.setBrightness(1.0f);
			_pf.setContrast(1.0f);
			_hue.setValue(0);
			_sat.setValue(100);
		}
	}

}
