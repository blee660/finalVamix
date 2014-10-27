package GUI;

import helperFiles.HelpFile;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * This is our main frame where the user can choose what action to take.
 * The user can pick either Download, Extract, or Play option.
 * 
 *  @author Jenny Lee
 * 
 * */

public class VamixWindow extends JFrame {
	
	ImageIcon download = new ImageIcon("./src/download.png");
	ImageIcon play = new ImageIcon("./src/play.png");
	ImageIcon extract = new ImageIcon("./src/cut.png");
	ImageIcon quit = new ImageIcon("./src/quit.png");
	ImageIcon help = new ImageIcon("./src/help.png");
	
	Image downImg = download.getImage().getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH);
	Image extImg = extract.getImage().getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH);
	Image playImg = play.getImage().getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH);
	Image quitImg = quit.getImage().getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
	Image helpImg = help.getImage().getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
	
	private JButton _download = new JButton("Download");
	private JButton _extract = new JButton("Extract");
	private JButton _play = new JButton("Play&Edit");
	private JButton _help = new JButton("Help");
	private JButton _quit = new JButton("Quit");
	private Component parent = this;
	
	private JPanel _listPane = new JPanel();
	private JPanel _hQPanel = new JPanel();
	private String _dirPath;
	private FlowLayout _fLayout = new FlowLayout();
	GridLayout layout = new GridLayout(0,4);
	
	JLabel select = new JLabel("<html><br>Welcome to VAMIX! Select from the following options: <br><html>");
	
	private VamixWindow(){
	
		_download.setIcon(new ImageIcon(downImg));
		_download.setVerticalTextPosition(SwingConstants.BOTTOM);
		_download.setHorizontalTextPosition(SwingConstants.CENTER);

		_extract.setIcon(new ImageIcon(extImg));
		_extract.setVerticalTextPosition(SwingConstants.BOTTOM);
		_extract.setHorizontalTextPosition(SwingConstants.CENTER);
		
		_play.setIcon(new ImageIcon(playImg));
		_play.setVerticalTextPosition(SwingConstants.BOTTOM);
		_play.setHorizontalTextPosition(SwingConstants.CENTER);
		
		_help.setIcon(new ImageIcon(helpImg));
		_help.setVerticalTextPosition(SwingConstants.CENTER);
		_help.setHorizontalTextPosition(SwingConstants.LEFT);
		
		_quit.setIcon(new ImageIcon(quitImg));
		_quit.setVerticalTextPosition(SwingConstants.CENTER);
		_quit.setHorizontalTextPosition(SwingConstants.LEFT);

		_hQPanel.setSize(70, 70);
		_hQPanel.setLayout(new GridLayout(2,1));
		
		JPanel border = new JPanel(new BorderLayout());
        border.add(_help, BorderLayout.CENTER);
        _hQPanel.add(border);
        
    	JPanel border2 = new JPanel(new BorderLayout());
        border2.add(_quit, BorderLayout.CENTER);
        _hQPanel.add(border2);
		
		//if download button is pressed, open a new frame
		_download.addActionListener(new ActionListener() {
			
			JFrame downloadFrame = null;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//if download was pressed and no frame came up, open the frame
				if(downloadFrame == null){
					downloadFrame = new DownloadFrame();
				}

				downloadFrame.setVisible(!downloadFrame.isVisible());

			}
		});
		
		//when the play button is pressed, open a new PlayFrame
		_play.addActionListener(new ActionListener(){
			
			JFrame playFrame = null;
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				if(playFrame == null || playFrame.isVisible() == false){
					
					final JFileChooser fc = new JFileChooser();
					FileNameExtensionFilter filter = new FileNameExtensionFilter("Audio & Video files", "mp4", "avi", "mp3", "mov", "aac", "mkv");
					fc.setFileFilter(filter);
					fc.setDialogTitle("Open file to play");
					fc.showOpenDialog(parent);
					
					
					try{
						_dirPath = fc.getSelectedFile().getAbsolutePath();
					
						if (_dirPath != null) {
							
							playFrame = new PlayFrame(_dirPath);
							playFrame.setVisible(true);
						}else{
							//DO NOTHING
						}
					}catch(NullPointerException e){
						
					}
				}
			}
			
		});
		
		//when the extract option is chosen, open a new ExtractFrame
		_extract.addActionListener(new ActionListener(){

			JFrame extractFrame = null;
			@Override
			public void actionPerformed(ActionEvent e) {
				if(extractFrame == null){
					extractFrame = new ExtractFrame();
				}
				extractFrame.setVisible(!extractFrame.isVisible());
			}
			
		});
		
		_help.addActionListener(new ActionListener(){
			JFrame hf = null;
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(hf == null || hf.isVisible()== false){
					HelpFile hf = new HelpFile();
					hf.setVisible(true);
				}
			}
			
		});
		
		//when Quit button is clicked, terminate the program
		_quit.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
			
		});
		
	}
	
	public static void main(String[] agrs){

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				VamixWindow vamix = new VamixWindow();
				vamix.createAndShowGUI();
			}
		});
	}

	//method that sets up the GUI
	protected void createAndShowGUI() {

		VamixWindow mainframe = new VamixWindow();

		mainframe.setTitle("VAMIX");
		mainframe.setDefaultCloseOperation(EXIT_ON_CLOSE);
		mainframe.setLayout(_fLayout);
		mainframe.setSize(450,150);
		
		_listPane.setLayout(layout);
		_listPane.add(_download);
		_listPane.add(_play);
		_listPane.add(_extract);
		_listPane.add(_hQPanel);
	
		mainframe.add(select, BorderLayout.CENTER);
		mainframe.add(_listPane);

		mainframe.setVisible(true);

	}
	
	
	
	
}
