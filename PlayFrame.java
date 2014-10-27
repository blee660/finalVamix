package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import javax.swing.filechooser.FileNameExtensionFilter;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import worker.ExtractWorker;
import worker.RewindWorker;
import model.SubtitleAdder;
import listener.MediaPlayerListener;
import model.UpdateSeekBar;
import model.VideoEditor;
import model.UpdateLabel;

/**
 * This class, PlayFrame.java, is a frame class that contains multiple JButtons
 * such as fast forward, rewind, play, pause, and etc (general commands for media players)
 * It also contains a pane containing the media player component where the video is being played.
 * 
 * In the menu tab, we also provided overlay and replace options for the audio. 
 * 
 * @author Jenny Lee
 *
 */

public class PlayFrame extends JFrame implements ActionListener, ChangeListener{

	static final int VOL_MIN = 0;
	static final int VOL_MAX = 120;
	static final int VOL_INIT = 80;

	private String _newName = "";
	private String _newNameVid = "";
	private int _override = 3;
	private int _overrideVid = 3;

	private JButton _ff = new JButton("▶▶"); 
	private JButton _skipF = new JButton("skip ▶▶");
	private JButton _rewind = new JButton("◀◀"); 
	private JButton _skipB = new JButton("◀◀ skip");
	private JButton _pause = new JButton("||"); 
	private JButton _close = new JButton("close");
	private JButton _mute = new JButton("mute");
	private JButton _sshot = new JButton("Snapshot");

	private JSlider _volume = new JSlider(JSlider.HORIZONTAL,VOL_MIN, VOL_MAX, VOL_INIT);
	private JLabel _vidTime = new JLabel();
	private JSlider _seekBar = new JSlider(JSlider.HORIZONTAL);
	private JSlider source;

	private JMenuBar _menuBar = new JMenuBar(); 
	private JMenu subtitles = new JMenu("SUBTITLES");
	private JMenu editMenu = new JMenu("EDIT");
	private JMenu open = new JMenu("OPEN");
	private JMenu pref = new JMenu("PREFERENCES");
	private JMenuItem bright;
	private JMenuItem sat;
	private JRadioButtonMenuItem m;
	
	private JMenuItem overlay = new JMenuItem("Overlay Audio on video");
	private JMenuItem replace = new JMenuItem("Replace Audio on video");
	private JMenuItem extract = new JMenuItem("Extract Audio from video");
	private JMenuItem addText = new JMenuItem("Add Text to Video");
	private JMenuItem extractVid = new JMenuItem("Extract Video (without audio)");
	private JRadioButtonMenuItem disable = new JRadioButtonMenuItem("Disable subtitle");
	private JMenu enable = new JMenu("Select subtitles");
	private JMenuItem selectSub = new JMenuItem("Add Subtitle to Video");
	private JMenuItem exportGIF = new JMenuItem("Export GIF");

	private JMenuItem openVid = new JMenuItem("Open File to play");

	private JPanel _panel = new JPanel();
	private JPanel _panelP = new JPanel();
	private JPanel _panelVol = new JPanel();
	private JPanel _seek = new JPanel();

	private static String _file;

	private String _overLay;

	private JFrame bcframe = null;
	private JFrame hsframe = null;

	private PlayFrame parent = this;
	private RewindWorker rw;

	private EmbeddedMediaPlayerComponent _mediaPlayer; 

	private long _time = 4000;
	private float _speed = 1.5f;
	private int _currentVol;

	private int _value;

	private long _mediaTime;

	private Color lighterBlue= new Color(130, 170, 198); 
	private Color menu1 = new Color(216, 227, 235);
	private Color menu2 = new Color(232, 239, 243);

	protected Object dirPath;

	public static void main (final String args[]) {

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new PlayFrame(_file); 
			} 
		}); 
	}


	public PlayFrame (String file) { 
		//construction of GUI design
		_file = file;
		_mediaPlayer = new EmbeddedMediaPlayerComponent(); 
		setTitle("Vamix Player");

		_seekBar.setMinimum(0);
		_seekBar.setMaximum(1000);
		_seekBar.setValue(0);
		
		_menuBar.setOpaque(true);
		_menuBar.setBackground(lighterBlue);

	
		enable.setOpaque(true);
		enable.setBackground(menu1);
		
		
		selectSub.setOpaque(true);
		selectSub.setBackground(menu2);

		editMenu.add(overlay);
		overlay.setBackground(menu1);
		editMenu.add(replace);
		replace.setBackground(menu2);
		editMenu.add(extract);
		extract.setBackground(menu1);
		editMenu.add(addText);
		addText.setBackground(menu2);
		editMenu.add(extractVid);
		extractVid.setBackground(menu1);
		editMenu.add(exportGIF);
		exportGIF.setBackground(menu2);

		Component[] comps = editMenu.getComponents();
		for(Component c : comps) {
			((JComponent) c).setOpaque(true); 
		}
		Component[] compSub = subtitles.getComponents();
		for(Component c : compSub) {
			((JComponent) c).setOpaque(true); 
		}
		Component[] compPref = pref.getComponents();
		for(Component c : compPref) {
			((JComponent) c).setOpaque(true); 
		}

		bright = new JMenuItem("Set Brightness/Contrast");
		sat = new JMenuItem("set Hue/Saturation");

		open.add(openVid);
		openVid.setBackground(menu1);
		pref.add(bright);
		bright.setBackground(menu1);
		pref.add(sat);
		sat.setBackground(menu2);

		_seek.add(_vidTime);
		_seek.add(_seekBar);
		ImageIcon vol = new ImageIcon("./src/volume.png");
		Image img = vol.getImage();
		Image newimg = img.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
		ImageIcon newIcon = new ImageIcon(newimg);
		JLabel volume = new JLabel(newIcon);
		volume.setSize(20, 20);

		_panel.add(_skipB);
		_panel.add(_rewind);
		_panel.add(_pause);
		_panel.add(_ff);
		_panel.add(_skipF);
		_panel.add(_mute);

		_panelVol.add(volume);
		_panelVol.add(_volume);

		_panel.add(_panelVol);
		_panel.add(_sshot);
		_panel.add(_close);

		_panel.setOpaque(true);
		_panel.setBackground(lighterBlue);

		Dimension prefSize = _seekBar.getPreferredSize();
		prefSize.width = 800;
		_seekBar.setPreferredSize(prefSize);
		_panelP.setLayout(new BoxLayout(_panelP, BoxLayout.Y_AXIS));

		_panelP.add(_seek);
		_panelP.add(_panel);
		
		
		setSize(1050, 600); 

		//only the play frame closes when the x button is closed.
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);

		//check if the file to be played is a playable media type
		if(_file.endsWith(".mp4") || _file.endsWith(".avi") || _file.endsWith(".mov") || _file.endsWith(".mkv")){
			setContentPane(_mediaPlayer); 
			_mediaPlayer.setBackground(Color.white);
			setMenuBar(true);
			getContentPane().add(_menuBar, BorderLayout.NORTH);
			getContentPane().add(_panelP, BorderLayout.SOUTH);

			_mediaPlayer.getMediaPlayer().playMedia(_file);
			_mediaPlayer.getMediaPlayer().start();

			setupSubtitleMenu(enable, disable, selectSub);
			long duration = _mediaPlayer.getMediaPlayer().getLength()/1000;
			_overLay = Integer.toString((int)duration);

		}else if(_file.endsWith(".mp3")||  _file.endsWith(".aac")){
			setContentPane(_mediaPlayer); 
			setMenuBar(false);
			getContentPane().add(_menuBar, BorderLayout.NORTH);

			getContentPane().add(_panelP, BorderLayout.SOUTH);
			_mediaPlayer.getMediaPlayer().playMedia(_file);
			_mediaPlayer.getMediaPlayer().start();

			long duration = _mediaPlayer.getMediaPlayer().getLength()/1000;
			_overLay = Integer.toString((int)duration);
		}

		_mediaTime = _mediaPlayer.getMediaPlayer().getLength()/1000;

		//add listener to the media player
		_mediaPlayer.getMediaPlayer().addMediaPlayerEventListener(new MediaPlayerListener(this));
		_mediaPlayer.getMediaPlayer().addMediaPlayerEventListener(new UpdateLabel(_vidTime));
		_seekBar.addMouseListener(new UpdateSeekBar(this, _seekBar, _mediaTime));

		

		//when the pause button is pressed
		_pause.addActionListener(this);

		//when the rewind button is pressed
		_rewind.addActionListener(this);

		//when skip forward button is pressed, skip a certain length of the video
		_skipF.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				_mediaPlayer.getMediaPlayer().skip(_time);
			}

		});

		//when skip backward button is pressed, skip back a certain length of the video
		_skipB.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				_mediaPlayer.getMediaPlayer().skip(-_time);	
			}

		});

		//when fast forward button is pressed, increase the rate of the change of the frames
		_ff.addActionListener(this);

		

		//if mute button is pressed
		_mute.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {

				//set the volume to zero
				if(_mediaPlayer.getMediaPlayer().getVolume() != 0){
					_currentVol = _mediaPlayer.getMediaPlayer().getVolume();
					_mediaPlayer.getMediaPlayer().setVolume(0);
					_volume.setValue(0);
				}
				//if the volume is already set to zero, turn the volume on again
				else if(_mediaPlayer.getMediaPlayer().getVolume() == 0){
					_mediaPlayer.getMediaPlayer().setVolume(_currentVol);
					_volume.setValue(_currentVol);
				}
			}

		});

		//add actionlistener to volume slider
		_volume.addChangeListener(this);

		

		_sshot.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				//String name = JOptionPane.showInputDialog("Save Screen shot of current window as (with .jpg extension): ");
				_mediaPlayer.getMediaPlayer().setSnapshotDirectory(".");
				boolean shot = _mediaPlayer.getMediaPlayer().saveSnapshot();
				if(shot == true){
					JOptionPane.showMessageDialog(null, "Snapshot saved to Current directory");
				}else{
					JOptionPane.showMessageDialog(null, "Snapshot failed");
				}

			}

		});
		
		//if the Add Text button is clicked, open a new TextAdderFrame window
		addText.addActionListener(new ActionListener() { 

			JFrame addTextFrame = null;
			@Override
			public void actionPerformed(ActionEvent e) {
				if(addTextFrame == null || addTextFrame.isVisible() == false){
					int time = (int)(_mediaPlayer.getMediaPlayer().getLength()/1000);

					addTextFrame = new TextAdderFrame(_file, time);
				}
				addTextFrame.setVisible(!addTextFrame.isVisible());
			}
		});

		//when the close button is selected, close the frame.
		_close.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_mediaPlayer.getMediaPlayer().pause();
				setVisible(false);
				dispose();
			}
		});

		sat.addActionListener(this);

		bright.addActionListener(this);
		
		disable.addActionListener(this);

		selectSub.addActionListener(new ActionListener(){
			JFrame sAdder = null;
			@Override
			public void actionPerformed(ActionEvent e) {
				if(sAdder == null || sAdder.isVisible() == false){
					sAdder = new SubtitleAdder(_file);
					sAdder.setVisible(true);
				}

			}

		});

		//when extract button is pressed,
		extract.addActionListener(new ActionListener(){


			@Override
			public void actionPerformed(ActionEvent e) {

				//ask the user to enter the new file's name
				_newName = JOptionPane.showInputDialog("Enter new name for audio file (without .mp3 extension): ");
				File fName = new File(_newName + ".mp3");

				//the file name cannot be null
				if (_newName != null){ 
					//if the file already exists in the directory, check if the user's intention is to override
					if (fName.exists()) {
						int o = JOptionPane.showConfirmDialog(null, "<html>File already exists. Do you wish to override? <br><html>","Duplicate File", JOptionPane.YES_NO_OPTION);

						//if yes, delete the previous file
						if ( o == JOptionPane.YES_OPTION){
							_override = 0;
							fName.delete();
						}
						else{
							// if intention is not to override, cancel extraction
							JOptionPane.showMessageDialog(null, "Extract Cancelled.");
							_override = 3;
						}
					}

					else if (_newName.contains(".mp3") || _newName.contains(".")) {
						JOptionPane.showMessageDialog(null, "The file name must not include the extension");
					}

					//check if the file name contains the extension.
					if(! _newName.contains(".mp3") && !_newName.contains(".")) {
						if(_override == 0 || !(fName.exists())){	
							ExtractWorker eworker = new ExtractWorker(_file, null, null, _newName);
							eworker.execute();
						}
					}
				}else{
					JOptionPane.showMessageDialog(null, "Please enter a name");
				}
			}

		});


		extractVid.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {

				//ask the user to enter the new file's name
				_newNameVid = JOptionPane.showInputDialog("Enter new name for audio file (without .mp4 extension): ");
				File fName =new File(_newNameVid + ".mp4");

				//the file name cannot be null
				if (_newNameVid != null){ 
					//if the file already exists in the directory, check if the user's intention is to override
					if (fName.exists()) {
						int o = JOptionPane.showConfirmDialog(null, "<html>File already exists. Do you wish to override? <br><html>");

						//if yes, delete the previous file
						if ( o == JOptionPane.YES_OPTION){
							_overrideVid = 0;
							fName.delete();
						}else{
							// if intention is not to override, cancel extraction
							JOptionPane.showMessageDialog(null, "Extraction Cancelled.");
							_overrideVid = 3;
						}
					}
					//check if the file name contains the extension.
					if(! _newNameVid.contains(".mp3") && !_newNameVid.contains(".")) {
						if(_overrideVid == 0 || !(fName.exists())){				
							ExtractWorker eworker = new ExtractWorker(_file, null, null, _newNameVid);
							eworker.execute();
						}
					}
					//if the file name does include extensions, display an error message
					else if (_newNameVid.contains(".mp3") || _newNameVid.contains(".")) {
						JOptionPane.showMessageDialog(null, "The file name must not include the extension");
					}
				}
			}			
		});
		
		//create new frame to export GIFs
		exportGIF.addActionListener(new ActionListener(){

			JFrame gf = null;
			@Override
			public void actionPerformed(ActionEvent e) {
				if(gf == null || gf.isVisible() == false){
					gf = new ExportGIFFrame(_file, _mediaTime);
					gf.setVisible(true);
				}
			}
			
		});
		
		//if the overlay option in the menu bar is selected, open a VideoEditor window
		overlay.addActionListener(new ActionListener(){
			JFrame vd = null;
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(vd == null || vd.isVisible() == false){
					vd = new VideoEditor("Overlay", _overLay, _file, _mediaPlayer.getMediaPlayer().getLength());
					vd.setVisible(true);
				}

			}


		});

		//if the replace option in the menu bar is selected, open a VideoEditor window
		replace.addActionListener(new ActionListener(){

			JFrame vd = null;
			public void actionPerformed(ActionEvent arg0) {
				if(vd == null || vd.isVisible() == false){
					vd = new VideoEditor("Replace", _overLay, _file, _mediaPlayer.getMediaPlayer().getLength());
					vd.setVisible(true);
				}
			}
		});
		
		// Opening a new media file to play
		openVid.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				//Open a new file chooser window
				JFileChooser fc = new JFileChooser();
				
				//filter extensions so only valid media files are shown
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Audio & Video files", "mp4", "avi", "mp3", "mov", "aac", "mkv");
				fc.setFileFilter(filter);
				fc.showOpenDialog(parent);
				fc.setDialogTitle("Please select a file");

				try{
					//get the path of the selected file
					dirPath = fc.getSelectedFile().getAbsolutePath();

					if (dirPath != null) {
						
						// play selected media
						_mediaPlayer.getMediaPlayer().playMedia(dirPath.toString());
						_mediaPlayer.getMediaPlayer().start();
						
						//refresh the menubar
						subtitles.removeAll();
						enable.removeAll();
						_menuBar.removeAll();
						//set menubar according to whether file is a video file or an audio file
						if(dirPath.toString().endsWith(".mp4") ||dirPath.toString().endsWith(".mov")|| dirPath.toString().endsWith(".avi")||dirPath.toString().endsWith(".mkv")){
							setMenuBar(true);
							pref.setVisible(true);
							_sshot.setEnabled(true);
						}else if(dirPath.toString().endsWith(".mp3") || dirPath.toString().endsWith("aac")){
							setMenuBar(false);
							pref.setVisible(false);
							_sshot.setEnabled(false);
						}
						setupSubtitleMenu(enable, disable, selectSub);
					}
				}catch(Exception arg){
					// Do Nothing
				}
			}

		});

	}

	//general rules for clicking any other buttons
	@Override
	public void actionPerformed(ActionEvent e) {

		//if rewind button is clicked, change the text to play, then execute the rewind worker
		if(e.getSource() == _rewind){
			if(!(this.getTitle().equals("Vamix Player - Rewinding"))){
				_pause.setText("▷");
				rw = new RewindWorker(_mediaPlayer);
				rw.execute();
			}
			setTitle("Vamix Player - Rewinding");
			//
		}else if(e.getSource() == _ff){
			_pause.setText("▷");

			if(_mediaPlayer.getMediaPlayer().getRate() == 1){
				_mediaPlayer.getMediaPlayer().setRate(_speed);
			}else if(_mediaPlayer.getMediaPlayer().getRate() == _speed){
				_mediaPlayer.getMediaPlayer().setRate(_speed + 1.5f);
			}else{
				_mediaPlayer.getMediaPlayer().setRate(_speed + 1.5f);
			}
		}
		//if pause button is pressed, change the button to a play button
		else if(e.getSource() == _pause) {
			if(_pause.getText().equals("||")){
				_mediaPlayer.getMediaPlayer().pause();
				_pause.setText("▷");
				//if the button pressed was a play button, cancel rewind
			} else {
				if(rw!=null){
					this.setTitle("Vamix Player");
					rw.cancel(true);
				}

				//change the rate of the media back to 1 (normal rate)
				if(_mediaPlayer.getMediaPlayer().getRate() != 1){
					_mediaPlayer.getMediaPlayer().setRate(1);
					_mediaPlayer.getMediaPlayer().play();
					//if the media was not playing, start the media
				}else if(!_mediaPlayer.getMediaPlayer().isPlaying()){
					_mediaPlayer.getMediaPlayer().start();
					//if the media was stopped, resume the play
				}else{
					_mediaPlayer.getMediaPlayer().play();
				}
				_pause.setText("||");
			}
		}else if(e.getSource() == bright){

			float cB = _mediaPlayer.getMediaPlayer().getBrightness();
			float cC = _mediaPlayer.getMediaPlayer().getContrast();

			_mediaPlayer.getMediaPlayer().setAdjustVideo(true);

			if(bcframe == null || bcframe.isVisible() == false){
				bcframe = new BrightContrastFrame(this, cB, cC);
			}

			bcframe.setVisible(!bcframe.isVisible());


		}else if(e.getSource() == sat){
			int cH = _mediaPlayer.getMediaPlayer().getHue();
			float cS = _mediaPlayer.getMediaPlayer().getSaturation();

			_mediaPlayer.getMediaPlayer().setAdjustVideo(true);

			if(hsframe == null || hsframe.isVisible() == false){
				hsframe = new HueSaturationFrame(this, cH, cS);
			}

			hsframe.setVisible(!hsframe.isVisible());

		}else if(e.getSource().toString().contains("Subtitle") || e.getSource().toString().contains("Disable")){

			if(e.getSource().toString().contains("Subtitle")){
				int spuVal = Integer.parseInt(e.getActionCommand().split(" ")[1]);
				_mediaPlayer.getMediaPlayer().setSpu(spuVal + 1);
			}else{
				_mediaPlayer.getMediaPlayer().setSpu(-1);
			}

			if(m != null){
				m.setSelected(false);
			}

			m = (JRadioButtonMenuItem) e.getSource();

		}

	}

	//when the volume bar state is changed, adjust to the new set volume
	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider)e.getSource();

		int vol = (int)source.getValue();
		_mediaPlayer.getMediaPlayer().setVolume(vol);
	}

	/**
	 * 
	 * This method sets the value of the seek bar according to the current media time
	 * 
	 * @param time - time of media player
	 * @author blee660
	 * 
	 * */
	public void updateSeekBar(int time) {
		_seekBar.setValue(time);
	}

	/**
	 * 
	 * This method sets the time of the media player according to a given value
	 * 
	 * @param t - time to set media player
	 * @author blee660
	 * 
	 * */
	public void changeMediaPlayerTime(long t) {
		_mediaPlayer.getMediaPlayer().setTime(t);

	}

	/**
	 * 
	 * This method updates the time of the media according to the seek bar value
	 * 
	 * @param MouseEvent e - event source provides seek bar value
	 * @author blee660
	 * 
	 * */
	public void updateMediaTime(MouseEvent e){
		source = (JSlider)e.getSource();
		_value = source.getValue(); 
		_seekBar.setValue(_value);
		_mediaPlayer.getMediaPlayer().setTime(_value*(_mediaPlayer.getMediaPlayer().getLength())/1000);
	}


	/**
	 * 
	 * This setBrightness method sets the brightness to a specified value
	 * 
	 * @param  br - brightness level
	 * @author blee660
	 * 
	 * */
	public void setBrightness(float br) {
		_mediaPlayer.getMediaPlayer().setBrightness(br);
	}


	/**
	 * 
	 * This setContrast method sets the contrast to a specified value
	 * 
	 * @param con = contrast level
	 * @author blee660
	 * 
	 * */
	public void setContrast(float con) {
		_mediaPlayer.getMediaPlayer().setContrast(con);
	}

	/**
	 * 
	 * This setHue method sets the hue to a specified value
	 * 
	 * @param h - hue level
	 * @author blee660
	 * 
	 * */
	public void setHue(int h) {
		_mediaPlayer.getMediaPlayer().setHue(h);
	}

	/**
	 * 
	 * This setSat method sets the media saturation to a specified value
	 * 
	 * @param s - saturation level
	 * @author blee660
	 * 
	 * */
	public void setSat(float s) {
		_mediaPlayer.getMediaPlayer().setSaturation(s);
	}




	public void playFile(String file){
		_mediaPlayer.getMediaPlayer().playMedia(file);
	}

	public void setupSubtitleMenu(JMenu enable, JMenuItem disable, JMenuItem selectsub){
		if(_mediaPlayer.getMediaPlayer().getSpuCount() != 0){

			enable.add(disable);
			disable.setBackground(menu1);

			enable.setEnabled(true);
			disable.setEnabled(true);
			m = (JRadioButtonMenuItem) disable;
			disable.setSelected(true);
			_mediaPlayer.getMediaPlayer().setSpu(-1);

			int i;
			for(i = 1; i<_mediaPlayer.getMediaPlayer().getSpuCount(); i++){
				JRadioButtonMenuItem j = new JRadioButtonMenuItem( "Subtitle " + (i));
				enable.add(j);

				if(i%2 == 1){
					j.setBackground(menu2);
				}else{
					j.setBackground(menu1);
				}

				j.setActionCommand("Subtitle "+(i)); 
				j.addActionListener(this);
			}

		}else if(_mediaPlayer.getMediaPlayer().getSpuCount() == 0){
			enable.setEnabled(false);
			disable.setEnabled(false);
		}
		subtitles.add(enable);
		subtitles.add(selectsub);
	}
	
	public void setMenuBar(boolean video){
		
		_menuBar.add(open);
		_menuBar.add(editMenu);
		_menuBar.add(subtitles);
		_menuBar.add(pref);
		
		if(video == false){
			editMenu.setEnabled(false);
			subtitles.setEnabled(false);
			pref.setEnabled(false);
			
			editMenu.setVisible(false);
			subtitles.setVisible(false);
			pref.setVisible(false);
		}else if(video == true){
			editMenu.setEnabled(true);
			subtitles.setEnabled(true);
			pref.setEnabled(true);
			
			editMenu.setVisible(true);
			subtitles.setVisible(true);
			pref.setVisible(true);
		}
		
	}

}
