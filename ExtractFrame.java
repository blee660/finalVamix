package GUI;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import worker.ExtractWorker;

/**
 * The ExtractFrame class is a frame class where user is asked to enter
 * all the required information for extraction.
 * 
 * @author Jenny Lee
 *
 */

public class ExtractFrame extends JFrame implements ActionListener {

	//declaration of the fields and objects
	private JLabel _enterFname = new JLabel("Choose File(Audio or Video) to Extract: ");
	private JLabel _enterStartTime = new JLabel("Enter the start time to extract from (in format hh:mm:ss): ");
	private JLabel _enterLength = new JLabel ("Enter the length to keep (in format hh:mm:ss): ");
	private JLabel _enterNewName = new JLabel("Enter filename for the mp3 output (without .mp3 extension): ");
	
	private JTextField _Fname = new JTextField(40);	
	private JTextField _startTime = new JTextField(40);
	private JTextField _length = new JTextField(40);
	private JTextField _newName = new JTextField(40);
	
	private JButton _start = new JButton("Extract");
	private JButton _cancel = new JButton("Back");
	private JButton _chooseFile = new JButton("Open");
	private JPanel _listpane = new JPanel();

	private FlowLayout _layout = new FlowLayout();

	private boolean audio;
	
	//constructor for the class
	ExtractFrame(){
		//setting up GUI
		setTitle("EXTRACT");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(500, 300);
		setLayout(_layout);

		add(_enterFname);
		add(_chooseFile);
		add(_Fname);

		_chooseFile.addActionListener(this);

		add(_enterStartTime);
		add(_startTime);

		add(_enterLength);
		add(_length);

		add(_enterNewName);
		add(_newName);

		_listpane.add(_start);
		_listpane.add(_cancel);

		//add action listener to the extract button which will commence extraction
		_start.addActionListener(this);

		add(_listpane);

		// extract frame will be closed if cancel is pressed
		_cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});



	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String textFile = _Fname.getText();
		String time = _startTime.getText();
		String length = _length.getText();
		String newName = _newName.getText();

		File newFile = new File(newName + ".mp3");

		// if the user clicks the "Open" button
		//select it and output the name of the chosen file to the textField 
		if(e.getSource() == _chooseFile){
			JFileChooser chooseFile = new JFileChooser();
			//set directory to be current directory
			File dir = new File(System.getProperty("user.dir"));
			chooseFile.setCurrentDirectory(dir);
			int result = chooseFile.showDialog(this,null);

			if (result == JFileChooser.APPROVE_OPTION){ 
				if(chooseFile.getSelectedFile() != null){

					String dirPath = chooseFile.getSelectedFile().getName();
					_Fname.setText(dirPath);
				}
			}
		}
		//If extract button is pressed check if any text fields have been left blank and if it has, show appropriate message box
		else if (e.getSource() == _start){
			if (((textFile.equals("")) || (time.equals("")) || (length.equals("")) || (newName.equals("")))
					&& !(!textFile.equals("")) && (time.equals("")) && (length.equals("")) && (!newName.equals(""))) {
				JOptionPane.showMessageDialog(this, "All fields must be entered!");
			}
			else if (((time.length()!=8) || (length.length()!= 8)) && !((time.length() == 0) && (length.length() == 0))
				|| (time.contains("[a-zA-Z]+") == true) || (length.contains("	") == true)) {
				//check whether times are entered in the right format
				if ((length.charAt(2)!= ':') || (time.charAt(2) != ':') || (length.charAt(5) != ':') || (time.charAt(5) != ':')) {
					JOptionPane.showMessageDialog(null, "Invalid time entry/entries");
				}
			}
			//check if the user included an extension to the file name, if did reject and display error message
			else if (newName.contains(".")) {
				JOptionPane.showMessageDialog(null, "The file name must not include the extension");
			}
			else{
				//if the new name of the extracted file already exists, ask user if intention is to override
				if(newFile.exists()){
					int o = JOptionPane.showConfirmDialog(this, "File already exists. Do you wish to override file?");

					//user specifies intention of overriding original file
					if( o == JOptionPane.YES_OPTION){
						
						//delete original file and commence extraction
						newFile.delete();

						ExtractWorker eworker = new ExtractWorker(textFile, time, length, newName);

						eworker.execute();
					}else{
						//if user does not want to override, cancel extraction process
						JOptionPane.showMessageDialog(this, "Extraction cancelled");
					}

				}
				else{
					//If all conditions are met and filename does not exist, commence extraction

					ExtractWorker eworker = new ExtractWorker(textFile, time, length, newName);

					eworker.execute();
				}	


			}

		}

	}

}
