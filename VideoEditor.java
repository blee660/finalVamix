import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

/**
 * This VideoEditor class provides functionalities of Overlaying and Replacing audio
 * of a video file.
 * 
 * This script also contains a worker class to support the VideoEditor class
 * by utilising the ProcessBuilder class
 * 
 * @author Jenny Lee
 *
 */

public class VideoEditor extends JFrame implements ActionListener{

	private JLabel _enterAudName = new JLabel("Choose Audio file: ");
	private JLabel _saveAs = new JLabel("Save new video as (without .mp4 extension): ");

	private JButton _chooseAud = new JButton("Open");

	private JTextField _Aname = new JTextField(40);	
	private JTextField _newName = new JTextField(40);

	private JButton _start = new JButton("OK");
	private JButton _cancel = new JButton("Cancel");

	private FlowLayout _layout = new FlowLayout();

	private String _action;
	private String _vidName;
	private String _audName;
	
	private String _overlay;

	//constructor for the class
	VideoEditor(String action, String oLay, String vidName){
		//set-up of the GUI
		setTitle("Edit");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(500, 200);
		setLayout(_layout);

		add(_enterAudName);
		add(_chooseAud);
		add(_Aname);

		add(_saveAs);
		add(_newName);

		add(_start);
		add(_cancel);

		_action = action;
		_overlay = oLay;
		_vidName = vidName;

		_chooseAud.addActionListener(this);


		//if cancel button is clicked, dispose the window
		_cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				setVisible(false);
			}
		});

		//if the start button is clicked,
		_start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				String audFile = _Aname.getText();
				String newName = _newName.getText();
				String vidFile = _vidName;	
				File newFile = new File(newName);
				
				//check if all the fields are entered in the right format
				if (newName == null) {
					JOptionPane.showMessageDialog(null, "Please enter the name of the file");
				} else if (!newName.contains(".")) {
					JOptionPane.showMessageDialog(null, "Please include the extension to the file name");
				} else if (newName.contains(".mp4")) {
					JOptionPane.showMessageDialog(null, "Please remove file extension");
				} else if (!audFile.contains(".mp3")) {
					JOptionPane.showMessageDialog(null, "Please choose an mp3 file");
				}
				//if a file with the same name already exists in the directory
				else if (newFile.exists()) {
					int o = JOptionPane.showConfirmDialog(null, "File already exists. Do you wish to override file?","Duplicate File", JOptionPane.YES_NO_OPTION);
					if( o == JOptionPane.YES_OPTION){
						//delete original file and commence overlay/replace
						newFile.delete();
						AudWorker aw = new AudWorker(vidFile, audFile, newName, _action, _overlay);
						aw.execute();
					} else {
						//if user does not want to override, cancel overlay/replace process
						JOptionPane.showMessageDialog(null, "Editting cancelled");
					}
				}
				//if all the fields are entered in the right format and there is no file with the same name, start editing
				else {
					AudWorker aw = new AudWorker(vidFile, audFile, newName, _action, _overlay);
					aw.execute();
				}
			}
		});
		
		

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		//if open button is clicked, open a new JFileChooser
		if(e.getSource() == _chooseAud){
			
			JFileChooser chooseAudio = new JFileChooser();
			File dir = new File(System.getProperty("user.dir"));
			chooseAudio.setCurrentDirectory(dir);
			
			int result2 = chooseAudio.showDialog(null, null);
			
			if (chooseAudio.getSelectedFile() == null) {
				try {
					_audName = null;
					dispose();
				} catch (NullPointerException arg0) {
					// do nothing
				}
			} else if(result2 == JFileChooser.APPROVE_OPTION) {
				if(chooseAudio.getSelectedFile() != null){
					
					_audName = chooseAudio.getSelectedFile().getName();
					_Aname.setText(_audName);
					
				}
			}
			
		}
		
	
	}
}

//a worker that supports the VideoEditor class
class AudWorker extends SwingWorker<String, Integer>{

	private ProcessBuilder _builder;
	private Process _process;

	private int _complete;
	private int _exit;

	AudWorker(String vid, String aud, String newName, String act, String overlay){
		
		//if the action is replace, perform replace action using bash commands through ProcessBuilder class
		if(act.equals("Replace")){
			_builder = new ProcessBuilder("/bin/bash", "-c", "avconv -i " + vid + " -i " + aud + " -c:v copy -map 0:0 -map 1:0 -t " + overlay 
					+ " " + "\"" + newName  +  "\"" + ".mp4" + " && echo \"Successful\" || echo \"Error\"");
		}
		//if the action chosen is to overlay, do the same as above
		else if(act.equals("Overlay")){
			_builder = new ProcessBuilder("/bin/bash", "-c", "avconv -i " + vid + " -i " + aud + 
			" -filter_complex [0:a][1:a]amix[out] -map \"[out]\" -map 0:v -c:v copy"
			+ " -t " + overlay + " -strict experimental " + "\"" + newName  + "\"" + ".mp4");
		}

		_builder.redirectErrorStream(true);
	}

	@Override
	protected String doInBackground() throws Exception {
		// Start overlay/replace process
		_process = _builder.start();

		// output information and progress to console
		InputStream stdout = _process.getInputStream();
		BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));

		String line = null;

		while ((line = stdoutBuffered.readLine()) != null ) {
			//assign appropriate value to the _complete variable depending on the result from the process
			if(line.equals("Successful")){
				_complete = 0;
			}else if (line.equals("Error")){
				_complete = 1;
			}
		}
		_exit = _process.exitValue();
		return line;
	}

	@Override
	protected void done() {
		//display a message if the process is completed successfully
		if(_complete == 0){
			JOptionPane.showMessageDialog(null, "Completed");
		}
		//display an error message otherwise
		else if(_complete == 1 || _exit != 0){
			JOptionPane.showMessageDialog(null, "Error Encountered");
		}
	}

}