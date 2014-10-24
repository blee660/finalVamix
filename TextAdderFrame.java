package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import model.JTextFieldLimit;

import worker.TextWorker;

public class TextAdderFrame extends JFrame implements ActionListener {

	private JLabel _reqTextS = new JLabel("Enter Text to add into start of Video: ");
	private JLabel _reqTimeStart = new JLabel("How long do you wish the text to appear for?");
	private JLabel _reqTextEnd = new JLabel("Enter Text to add into end of Video: ");
	private JLabel _reqTimeEnd = new JLabel("How long do you wish the text to appear for?");
	private JLabel _reqNewName = new JLabel("Save edited video as: (without .mp4 extension)");

	private JPanel _tS = new JPanel();
	private JPanel _tF = new JPanel();
	private JPanel _progPan = new JPanel();

	private JTextArea _enterTextS = new JTextArea(10, 40);
	private JTextField _enterTimeStart = new JTextField("in seconds:", 10);
	private JTextArea _enterTextEnd = new JTextArea(10,40);
	private JTextField _enterTimeEnd = new JTextField("in seconds:", 10);
	private JTextField _enterNewName = new JTextField(20);

	private JButton _cancel = new JButton("Cancel");
	private JButton _add = new JButton("Add Text");
	private FlowLayout _layout = new FlowLayout();

	private String _textS;
	private String _timeS;
	private String _textE;
	private String _timeE;
	private String _newName;
	private String _vidName;
	private long _time;
	private String _font;
	private String _fontColor;
	private String _fontSize;

	private JComboBox<String> cb;
	private JComboBox<String> cb2;
	private JComboBox<String> cb3;
	
	private TextWorker textWorker;
	
	private JProgressBar _prog;

	private int _error;

	private String _previewText = "Preview Text";
	private JLabel previewLabel = new JLabel(_previewText);
	
	private GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	private Font customFont;
	private Font customFont2;
	private Font customFont3;
	private Font customFont4;
	private float f = 20f;

	//constructor of the class
	TextAdderFrame(String vidName, long time){
		
		try{
			customFont = Font.createFont(Font.TRUETYPE_FONT, new File("/usr/share/fonts/truetype/ubuntu-font-family/" + "Ubuntu-L.ttf"));
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Ubuntu-L.ttf")));
		}catch (IOException|FontFormatException e) {
			//Handle exception
		}try{
			customFont2 = Font.createFont(Font.TRUETYPE_FONT, new File("/usr/share/fonts/truetype/ubuntu-font-family/" + "Ubuntu-LI.ttf"));
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Ubuntu-LI.ttf")));
		}catch (IOException|FontFormatException e) {
			//Handle exception
		}try{
			customFont3 = Font.createFont(Font.TRUETYPE_FONT, new File("/usr/share/fonts/truetype/ubuntu-font-family/" + "Ubuntu-B.ttf")); 
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Ubuntu-B.ttf")));
		}catch (IOException|FontFormatException e) {
			//Handle exception
		}try{
			customFont4 = Font.createFont(Font.TRUETYPE_FONT, new File("/usr/share/fonts/truetype/ubuntu-font-family/" + "Ubuntu-BI.ttf"));
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Ubuntu-BI.ttf")));
		}catch (IOException|FontFormatException e) {
			//Handle exception
		}
		
		

		previewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		Dimension d = previewLabel.getPreferredSize();  
		previewLabel.setPreferredSize(new Dimension(d.width+160,d.height+50)); 
		previewLabel.setOpaque(true);
		previewLabel.setBackground(Color.WHITE);

		//set up of GUI
		_time = time;
		_vidName = vidName;

		_enterTextS.setLineWrap(true);
		_enterTextS.setWrapStyleWord(true);
		_enterTextEnd.setLineWrap(true);
		_enterTextEnd.setWrapStyleWord(true);

		_enterTextS.setDocument(new JTextFieldLimit(250));
		_enterTextEnd.setDocument(new JTextFieldLimit(250));

		String[] fontsString = new String[] {"Font", "Normal", "Italics", "Bold", "Bold + Italics"};
		JComboBox<String> fonts = new JComboBox<>(fontsString);
		String[] fontSize = new String[] {"Size", "14", "16", "18", "20", "22", "24", "26", "28", "30", "32", "34"};
		JComboBox<String> sizes = new JComboBox<>(fontSize);
		String[] fontColor = new String[] {"Color", "Black", "White", "Yellow", "Blue", "Red"};
		JComboBox<String> fontColors = new JComboBox<>(fontColor);

		JScrollPane spS = new JScrollPane(_enterTextS);
		JScrollPane spE = new JScrollPane(_enterTextEnd);

		setTitle("Add Text to Video");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(550,650);
		setLayout(_layout);

		add(_reqTextS);
		add(spS);

		_tS.add(_reqTimeStart);
		_tS.add(_enterTimeStart);

		add(_tS);

		add(_reqTextEnd);
		add(spE);

		_tF.add(_reqTimeEnd);
		_tF.add(_enterTimeEnd);

		add(_tF);

		add(_reqNewName);
		add(_enterNewName);

		add(new JLabel("font style:"));
		add(fonts);
		add(new JLabel("font size:"));
		add(sizes);
		add(new JLabel("font Color:"));
		add(fontColors);

		add(previewLabel);
		
		_prog = new JProgressBar(0, (int)_time);
		_progPan.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		_progPan.add(_add);
		_progPan.add(_cancel);
		_progPan.add(_prog);
		
		add(_progPan);
		//if cancel button is pressed, dispose the frame
		_cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(textWorker != null){
					//textWorker.
					//textWorker.cancel(true);
				}
				dispose();
			}

		});

		//if fonts combo box is clicked
		fonts.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				cb = (JComboBox<String>)e.getSource();
				updatePreview(cb);
				System.out.println(_time);
			}
		});

		//when size combo box is clicked
		sizes.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				cb2 = (JComboBox<String>)e.getSource();
				updatePreview(cb2);

			}
		});

		//when colors combo box is clicked
		fontColors.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				cb3 = (JComboBox<String>)e.getSource();
				updatePreview(cb3);

			}
		});

		//if the Add button is clicked
		_add.addActionListener(this);


		_enterTimeStart.addMouseListener(new MouseAdapter(){

			@Override
			public void mouseClicked(MouseEvent e) {
				_enterTimeStart.setText("");
			}
		});


		_enterTimeEnd.addMouseListener(new MouseAdapter(){

			@Override
			public void mouseClicked(MouseEvent e) {
				_enterTimeEnd.setText("");
			}
		});

	}

	protected void updatePreview(JComboBox<String> cbItem){
		String string = (String)cbItem.getSelectedItem();
		if(string.contains("1") || string.contains("2") || string.contains("3")){
			f  = Float.parseFloat(string);
			previewLabel.setFont(previewLabel.getFont().deriveFont(f));
			
		}else if(string.contains("Bold") || string.contains("Normal") || string.contains("Italics")){
			
				if(string.equals("Normal")){
					previewLabel.setFont(customFont);
					previewLabel.setFont(previewLabel.getFont().deriveFont(f));
				}else if(string.equals("Italics")){
					previewLabel.setFont(customFont2);
					previewLabel.setFont(previewLabel.getFont().deriveFont(f));
				}else if(string.equals("Bold")){
					previewLabel.setFont(customFont3);
					previewLabel.setFont(previewLabel.getFont().deriveFont(f));
				}else if(string.equals("Bold + Italics")){
					previewLabel.setFont(customFont4);
					previewLabel.setFont(previewLabel.getFont().deriveFont(f));
				}
				

		}else{
			if(string.equals("Black")){
				previewLabel.setFont(previewLabel.getFont().deriveFont(f));
				previewLabel.setBackground(Color.white);
				previewLabel.setForeground(Color.BLACK);
			}else if(string.equals("White")){
				previewLabel.setFont(previewLabel.getFont().deriveFont(f));
				previewLabel.setBackground(Color.black);
				previewLabel.setForeground(Color.WHITE);
			}else if(string.equals("Yellow")){
				previewLabel.setFont(previewLabel.getFont().deriveFont(f));
				previewLabel.setBackground(Color.black);
				previewLabel.setForeground(Color.YELLOW);
			}else if(string.equals("Blue")){
				previewLabel.setFont(previewLabel.getFont().deriveFont(f));
				previewLabel.setBackground(Color.white);
				previewLabel.setForeground(Color.BLUE);
			}else if(string.equals("Red")){
				previewLabel.setFont(previewLabel.getFont().deriveFont(f));
				previewLabel.setBackground(Color.white);
				previewLabel.setForeground(Color.RED);
			}

		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		_textS = _enterTextS.getText();
		_timeS = _enterTimeStart.getText();
		_textE = _enterTextEnd.getText();
		_timeE = _enterTimeEnd.getText();
		_newName = _enterNewName.getText();
	
		
		if(_timeS.contains("[a-zA-Z]+") || _timeE.contains("[a-zA-Z]+")){
			JOptionPane.showMessageDialog(null, "Incorrect time format");
			_error = 0;
		}else if (_timeS.equals("in seconds:") || _timeE.equals("in seconds:")){
			JOptionPane.showMessageDialog(null, "Please enter time fields!");
		}
		//if nothing is selected from the font combo box, display an error message
		else if(cb == null){
			JOptionPane.showMessageDialog(null, "Please select a font type");
			_error = 0;
			
		//if nothing is selected from the size combo box, display an error message
		}else if(cb2 == null){
			JOptionPane.showMessageDialog(null, "Please select a font size");
			_error = 0;
		
		//if nothing is selected from the color combo box, display an error message
		}else if(cb3 == null){
			JOptionPane.showMessageDialog(null, "Please select a font color");
			_error = 0;
		
		}
		//if field entry is not done properly, display an error message
		else if (_newName.contains(".mp4")) {
			JOptionPane.showMessageDialog(null, "Please do not include \".mp4\" file extension");
		} else if (_timeS == null || _timeE == null || _timeS.equals("") || _timeE.equals("")) {
			JOptionPane.showMessageDialog(null, "Please enter time fields!");
		} else if (_newName == null) {
			JOptionPane.showMessageDialog(null, "Please enter the name to save the file as");
		}else{
			String fontName = (String)cb.getSelectedItem();
			_font = fontName;
			
			String fontSize = (String)cb2.getSelectedItem();
			_fontSize = fontSize;
			
			String color = (String)cb3.getSelectedItem();
			_fontColor = color;
			
			int time = (int) (_time - Integer.parseInt(_timeE));
			_timeE = Integer.toString(time);
			
					
			_error = 1;
		}

		

		//if no particular error is found, start the SwingWorker
		if(_error != 0){
			textWorker = new TextWorker(_vidName, _newName, _timeS, _timeE, _textS, _textE, _font, _fontSize, _fontColor, _prog);	
			
			textWorker.execute();
				
			_add.setEnabled(false);
		}

	}
}
