package GUI;

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

/**
 * This BrightContrastFrame is a Brightness and Contrast control frame
 * It controls the level of brightness and contrast for the currently playing media file.
 * 
 * Author: Jenny Bom Yi Lee
 * 
 * */
public class BrightContrastFrame extends JFrame implements ChangeListener, ActionListener, MouseListener{

	//Set up the components for this frame
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
	
	// ========== Set properties of frame and components, and insert components into frame ================================== //
	
	//Constructor for this frame
	public BrightContrastFrame(PlayFrame pf, float currentB, float currentC){
		
		//Set size of frame
		setSize(330, 170);

		_pf = pf;
		cB = currentB;
		cC = currentC;
		
		//Set the size of the control sliders with maximum value as 200
		_brightness = new JSlider(0, 200, (int)(cB*100));
		_contrast = new JSlider(0, 200, (int)(cC*100));
		
		//Add in tick marks to the brightness control slider for easier viewing
		_brightness.setMajorTickSpacing(25);  
		_brightness.setPaintTicks(true);  
		java.util.Hashtable<Integer,JLabel> labelTable = new java.util.Hashtable<Integer,JLabel>();
		labelTable.put(new Integer(200), new JLabel("2.0"));
		labelTable.put(new Integer(150), new JLabel("1.50"));
		labelTable.put(new Integer(100), new JLabel("1.0"));
		labelTable.put(new Integer(50), new JLabel("0.50"));
		labelTable.put(new Integer(0), new JLabel("0.0"));
		
		//Brightness level can range from 0.0 to 2.0, default is 1.0
		_brightness.setLabelTable( labelTable );
		_brightness.setPaintLabels(true);

		//Add tick marks to the contrast control slider for easier viewing
		_contrast.setMajorTickSpacing(25);  
		_contrast.setPaintTicks(true);  
		java.util.Hashtable<Integer,JLabel> labelTableC = new java.util.Hashtable<Integer,JLabel>();
		labelTableC.put(new Integer(200), new JLabel("2.0"));
		labelTableC.put(new Integer(150), new JLabel("1.50"));
		labelTableC.put(new Integer(100), new JLabel("1.0"));
		labelTableC.put(new Integer(50), new JLabel("0.50"));
		labelTableC.put(new Integer(0), new JLabel("0.0"));
		
		//Contrast level can range from 0.0 to 2.0, default is 1.0
		_contrast.setLabelTable( labelTable );
		_contrast.setPaintLabels(true);

		//Set layout of main panel as box layout
		_panel.setLayout(new BoxLayout(_panel, BoxLayout.Y_AXIS));
		
		//Set size of brightness and contrast sliders as 270
		prefSize = _brightness.getPreferredSize();
		prefSize.width = 270;
		prefSizeC = _contrast.getPreferredSize();
		prefSizeC.width = 270;

		_brightness.setPreferredSize(prefSize);
		_contrast.setPreferredSize(prefSizeC);

		//Add in image icons for users to know which slider controls brightness/contrast
		ImageIcon b = new ImageIcon(this.getClass().getResource("resources/brightness.png"));
		Image img = b.getImage();
		Image newimg = img.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
		ImageIcon newIcon = new ImageIcon(newimg);
		JLabel br = new JLabel(newIcon);
		//Set size of brightness image icon to fit into frame
		br.setSize(20, 20);

		ImageIcon c = new ImageIcon(this.getClass().getResource("resources/contrast.png"));
		Image imgC = c.getImage();
		Image newimgC = imgC.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
		ImageIcon newIconC = new ImageIcon(newimgC);
		JLabel con = new JLabel(newIconC);
		//Set size of contrast image icon to fit into frame
		con.setSize(20, 20);

		//Set layout of sub panels
		_panelB.setLayout(new FlowLayout());
		_panelC.setLayout(new FlowLayout());
		_panelButtons.setLayout(new FlowLayout(FlowLayout.RIGHT));

		//add icons and sliders and buttons into each sub panel
		_panelB.add(br);
		_panelB.add(_brightness);

		_panelC.add(con);
		_panelC.add(_contrast);

		_panelButtons.add(_ok);
		_panelButtons.add(_cancel);
		_panelButtons.add(_default);
		
		//Add sub panels into main panel
		_panel.add(_panelB);
		_panel.add(_panelC);
		_panel.add(_panelButtons);

		//Add main panel and set title appropriately
		add(_panel);
		setTitle("Set Brightness/Contrast");

		//Add change listeners and mouse listeners to the brightness and contrast sliders
		_brightness.addChangeListener(this);
		_contrast.addChangeListener(this);
		_brightness.addMouseListener(this);
		_contrast.addMouseListener(this);

		//Add action listeners to the buttons
		_default.addActionListener(this);
		_ok.addActionListener(this);
		_cancel.addActionListener(this);
		
	}
	// ====== Frame set up complete ==================================================================================== //

	
	
	// ====== Overridden methods for ChangeListener, ActionListener, MouseListener ====================================== //
	
	
	//This method sets the brightness and contrast level of the media in the PlayFrame
	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource() == _brightness){
			
			//When the brightness control slider has been changed, get the brightness value
			JSlider source = (JSlider)e.getSource();
			float br = (float)(source.getValue()/100.0);
			
			//Set the brightness of the media player to the value
			_pf.setBrightness(br);
		}else if(e.getSource() == _contrast){
			
			//When the Contrast control slider has been changed, get the contrast value
			JSlider source = (JSlider)e.getSource();
			float con = (float)(source.getValue()/100.0);
			
			//Set the contrast of the media player to the value
			_pf.setContrast(con);
		}
	}


	//This method sets the brightness/contrast level of the media player according to which button is pressed
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == _ok){
			//if OK is clicked, set the level to the set values and dispose brightness/contrast frame
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


	// method to set brightness or contrast as the set value
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource() == _brightness){
			//if brightness slider has been clicked
			_brightness.setUI(new MetalSliderUI() {
				protected void scrollDueToClickInTrack(int direction) {

					// get value of slider
					int _value = _brightness.getValue(); 

					if (_brightness.getOrientation() == JSlider.HORIZONTAL) {
						_value = this.valueForXPosition(_brightness.getMousePosition().x);

					}
					// set value of slider and brightness
					_brightness.setValue(_value);
					_pf.setBrightness((float)_value/100.0f);
				}
			});
			
		}else if(e.getSource() == _contrast){
			//if contrast slider has been clicked
			JSlider source = (JSlider)e.getSource();
			float con = (float)(source.getValue()/100.0);
			_pf.setContrast(con);
		}
	}


	@Override
	public void mouseEntered(MouseEvent arg0) {
		// Do Nothing
	}


	@Override
	public void mouseExited(MouseEvent arg0) {
		// Do Nothing
	}


	@Override
	public void mousePressed(MouseEvent arg0) {
		// Do Nothing
	}


	@Override
	public void mouseReleased(MouseEvent arg0) {
		// Do Nothing
	}






}
