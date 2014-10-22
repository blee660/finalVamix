import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.metal.MetalSliderUI;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;

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

public class PlayFrame extends JFrame implements ChangeListener, ActionListener, MouseInputListener{

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
	private JMenuItem bright;
	private JPanel _panel = new JPanel();
	private JPanel _panelP = new JPanel();
	private JPanel _panelOP = new JPanel();
	private JPanel _seek = new JPanel();
	private static String _file;
	private String _overLay;
	private RewindWorker rw;
	private JFrame bcframe = null;



	private EmbeddedMediaPlayerComponent _mediaPlayer; 

	private long _time = 4000;
	private float _speed = 1.5f;
	private int _currentVol;

	private int _value;
	private String _dir;
	private boolean _audVid;


	public static void main (final String args[]) {

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new PlayFrame(_file); 
			} 
		}); 
	}


	PlayFrame (String file) { 
		//construction of GUI design
		_file = file;
		setTitle("Vamix Player");

		_seekBar.setMinimum(0);
		_seekBar.setMaximum(1000);
		_seekBar.setValue(0);

		JMenuBar menuBar = new JMenuBar();
		JMenu editMenu = new JMenu("EDIT VIDEO");
		JMenu open = new JMenu("OPEN");
		JMenu pref = new JMenu("PREFERENCES");
		menuBar.add(editMenu);
		menuBar.add(open);
		menuBar.add(pref);


		JMenuItem overlay = new JMenuItem("Overlay Audio on video");
		JMenuItem replace = new JMenuItem("Replace Audio on video");
		JMenuItem extract = new JMenuItem("Extract Audio from video");
		JMenuItem addText = new JMenuItem("Add Text to Video");
		JMenuItem extractVid = new JMenuItem("Extract Video (without audio)");
		JMenuItem openVid = new JMenuItem("Open Video to play");
		bright = new JMenuItem("Set Brightness/Contrast");

		editMenu.add(overlay);
		editMenu.add(replace);
		editMenu.add(extract);
		editMenu.add(addText);
		editMenu.add(extractVid);
		open.add(openVid);
		pref.add(bright);

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

		_panel.add(volume);
		_panel.add(_volume);

		_panelOP.setLayout(new FlowLayout(FlowLayout.RIGHT));
		_panelOP.add(_sshot);
		_panelOP.add(_close);

		_panel.add(_panelOP, BorderLayout.WEST);

		Dimension prefSize = _seekBar.getPreferredSize();
		prefSize.width = 800;
		_seekBar.setPreferredSize(prefSize);
		_panelP.setLayout(new BoxLayout(_panelP, BoxLayout.Y_AXIS));

		_panelP.add(_seek);
		_panelP.add(_panel);

		_mediaPlayer = new EmbeddedMediaPlayerComponent(); 

		setLocation(100, 100);
		setSize(1050, 600); 
		//only the play frame closes when the x button is closed.
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);


		String newName = null;
		//check if the file to be played is a playable media type
		if(_file.endsWith(".mp4") || _file.endsWith(".avi") || _file.endsWith(".mov")){
			PWorker pworker = new PWorker(_file, newName);
			pworker.execute();
			setContentPane(_mediaPlayer); 

			getContentPane().add(menuBar, BorderLayout.NORTH);
			getContentPane().add(_panelP, BorderLayout.SOUTH);

			_mediaPlayer.getMediaPlayer().playMedia(_file);
			_mediaPlayer.getMediaPlayer().start();

			long duration = _mediaPlayer.getMediaPlayer().getLength()/1000;
			_overLay = Integer.toString((int)duration);

		}else if(_file.endsWith(".mp3")||  _file.endsWith(".aac")){
			PWorker pworker = new PWorker(_file, newName);
			pworker.execute();
			setContentPane(_mediaPlayer); 

			_sshot.setVisible(false);

			getContentPane().add(_panelP, BorderLayout.SOUTH);
			_mediaPlayer.getMediaPlayer().playMedia(_file);
			_mediaPlayer.getMediaPlayer().start();

			long duration = _mediaPlayer.getMediaPlayer().getLength()/1000;
			_overLay = Integer.toString((int)duration);
		}

		//add listener to the media player
		_mediaPlayer.getMediaPlayer().addMediaPlayerEventListener(new MediaPlayerListener(this));
		_mediaPlayer.getMediaPlayer().addMediaPlayerEventListener(new UpdateLabel());
		_seekBar.addMouseListener(this);


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
		_ff.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {

				_pause.setText("▷");

				if(_mediaPlayer.getMediaPlayer().getRate() == 1){
					_mediaPlayer.getMediaPlayer().setRate(_speed);
				}else if(_mediaPlayer.getMediaPlayer().getRate() == _speed){
					_mediaPlayer.getMediaPlayer().setRate(_speed + 1.5f);
				}else{
					_mediaPlayer.getMediaPlayer().setRate(_speed + 1.5f);
				}

			}

		});

		openVid.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				_dir = fc.getSelectedFile().getAbsolutePath();
				if (_dir != null) {
					_mediaPlayer.getMediaPlayer().playMedia(_dir);
					_mediaPlayer.getMediaPlayer().start();
				}
			}

		});


		bright.addActionListener(this);

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
							_audVid = true;
							EWorker eworker = new EWorker(_file, null, null, _newName, _audVid);
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
					if(! _newNameVid.contains(".mp4") && !_newNameVid.contains(".")) {
						if(_overrideVid == 0 || !(fName.exists())){				
							EWorker eworker = new EWorker(_file, null, null, _newNameVid, _audVid);
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

		_volume.addChangeListener(this);

		//if the overlay option in the menu bar is selected, open a VideoEditor window
		overlay.addActionListener(new ActionListener(){
			JFrame vd = null;
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(vd == null || vd.isVisible() == false){
					vd = new VideoEditor("Overlay", _overLay, _file);
					vd.setVisible(true);
				}

			}


		});

		//if the replace option in the menu bar is selected, open a VideoEditor window
		replace.addActionListener(new ActionListener(){

			JFrame vd = null;
			public void actionPerformed(ActionEvent arg0) {
				if(vd == null || vd.isVisible() == false){
					vd = new VideoEditor("Replace", _overLay, _file);
					vd.setVisible(true);
				}
			}
		});

		_sshot.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				//String name = JOptionPane.showInputDialog("Save Screen shot of current window as (with .jpg extension): ");
				_mediaPlayer.getMediaPlayer().setSnapshotDirectory(".");
				boolean shot = _mediaPlayer.getMediaPlayer().saveSnapshot();
				if(shot == true){
					JOptionPane.showMessageDialog(null, "Snapshot saved to current directory");
				}else{
					JOptionPane.showMessageDialog(null, "Snapshot failed");
				}

			}

		});


	}

	//this class is responsible for updating the time next to the seek bar as the media progresses
	private class UpdateLabel extends MediaPlayerEventAdapter{

		public void positionChanged(MediaPlayer _mediaPlayer, float pos) {
			long currentMillis = (long)(_mediaPlayer.getLength()*pos);
			long totalMillis = _mediaPlayer.getLength();

			//change the time in the desired format
			String currentTime = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(currentMillis),
					TimeUnit.MILLISECONDS.toMinutes(currentMillis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(currentMillis)), 
					TimeUnit.MILLISECONDS.toSeconds(currentMillis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(currentMillis)));
			String totalTime = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(totalMillis),
					TimeUnit.MILLISECONDS.toMinutes(totalMillis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(totalMillis)), 
					TimeUnit.MILLISECONDS.toSeconds(totalMillis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(totalMillis)));

			_vidTime.setText(currentTime + "/" + totalTime);
		}
	}

	//when the volume bar is changed, adjust to the new set volume
	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider)e.getSource();

		int vol = (int)source.getValue();
		_mediaPlayer.getMediaPlayer().setVolume(vol);
		//}
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
				bcframe = new BCframe(this, cB, cC);
			}

			bcframe.setVisible(!bcframe.isVisible());


		}

	}

	public void updateSeekBar(int time) {
		_seekBar.setValue(time);

	}

	public void changeMediaPlayerTime(long t) {
		_mediaPlayer.getMediaPlayer().setTime(t);

	}

	//if the user clicks a point on the seek bar, go to that frame
	@Override
	public void mouseClicked(MouseEvent e) {

		_seekBar.setUI(new MetalSliderUI() {
			protected void scrollDueToClickInTrack(int direction) {

				_value = _seekBar.getValue(); 

				if (_seekBar.getOrientation() == JSlider.HORIZONTAL) {
					_value = this.valueForXPosition(_seekBar.getMousePosition().x);

				}
				_seekBar.setValue(_value);
				_mediaPlayer.getMediaPlayer().setTime(_value*(_mediaPlayer.getMediaPlayer().getLength())/1000);
			}
		});

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		updateMediaTime(e);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		updateMediaTime(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	void updateMediaTime(MouseEvent e){
		source = (JSlider)e.getSource();
		_value = source.getValue(); 
		_seekBar.setValue(_value);
		_mediaPlayer.getMediaPlayer().setTime(_value*(_mediaPlayer.getMediaPlayer().getLength())/1000);
	}


	public void setBrightness(float br) {
		_mediaPlayer.getMediaPlayer().setBrightness(br);
	}


	public void setContrast(float con) {
		_mediaPlayer.getMediaPlayer().setContrast(con);
	}



}
