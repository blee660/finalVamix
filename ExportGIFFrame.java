package GUI;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import worker.GIFWorker;

/**
 * 
 * This ExportGIFFrame class enables exporting of GIF files from a video
 * It makes use of the GIFWorker class 
 * 
 * @param file - current video file
 * @param time - time of video file
 * 
 * @author blee660
 * 
 * 
 * */
public class ExportGIFFrame extends JFrame{

	//Set up GUI components
	private FlowLayout _layout = new FlowLayout();
	private String _file;
	
	private JLabel _startTime = new JLabel("Time to start GIF export from (HH:MM:SS): ");
	private JLabel _duration = new JLabel("Choose length of GIF");
	private JLabel _saveAs = new JLabel("Save new GIF flie as (without .gif extension): ");
	private JTextField _newName = new JTextField(12);
	
	private JButton _start = new JButton("Export GIF");
	private JButton _cancel = new JButton("Cancel");
	
	private JRadioButton _option = new JRadioButton("5s", true);
	private JRadioButton _option2 = new JRadioButton("10s");

	private SpinnerModel sm2 = new SpinnerNumberModel(0, 0, 59, 1);
	private SpinnerModel sm3 = new SpinnerNumberModel(0, 0, 59, 1);
	private SpinnerModel sm4 = new SpinnerNumberModel(0, 0, 59, 1);
	private JSpinner startH = new JSpinner(sm2);
	private JSpinner startM = new JSpinner(sm3);
	private JSpinner startS = new JSpinner(sm4);
	
	private JPanel _panel = new JPanel();
	private JPanel _panel2 = new JPanel();
	private JPanel _panel3 = new JPanel();
	private JPanel _buttonPanel = new JPanel();
	
	private long _time;
	
	private String timeStart;
	private String _durationTime;
	private String _gifName;
	
	private String sh;
	private String sm;
	private String ss;
	
	private int _time2;
	
	private GIFWorker gw;
	
	//Constructor for GIFFrame
	public ExportGIFFrame(String file, long time){
		
		_time = time;
		
		//set title, size and add GUI components
		setTitle("Export GIF");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(500, 200);
		setLayout(_layout);
		_file = file;
		
		_panel.add(_startTime);
		_panel.add(startH);
		_panel.add(new JLabel("h"));
		_panel.add(startM);
		_panel.add(new JLabel("m"));
		_panel.add(startS);
		_panel.add(new JLabel("s"));
		
		_panel2.add(_duration);
		_panel2.add(_option);
		_panel2.add(_option2);

		add(_panel2);
		ButtonGroup group = new ButtonGroup();
		group.add(_option);
		group.add(_option2);
		
		_panel3.add(_saveAs);
		_panel3.add(_newName);
		
		_buttonPanel.add(_start);
		_buttonPanel.add(_cancel);
		
		add(_panel);
		add(_panel2);
		
		add(_panel3);
		
		
		add(_buttonPanel);
		
		// add Action listener to the start button
		_start.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				sh = startH.getValue().toString();
				sm = startM.getValue().toString();
				ss = startS.getValue().toString();
				
				//check if the time specified is 1 digit, and append a "0" to the front if so
				if(sh.length() == 1){
					sh = "0" + sh;
				}
				if(sm.length() == 1){
					sm = "0" + sm;
				}
				if(ss.length() == 1){
					ss = "0" + ss;
				}
				//get time format in hh:mm:ss
				timeStart = sh + ":" + sm + ":" + ss;
				
				//check whether time for GIF is speciifed as 5 or 10 seconds
				if(_option.isSelected()){
					_durationTime = "5";
				}else if(_option2.isSelected()){
					_durationTime = "10";
				}
				//get name of ne GIF file
				_gifName = _newName.getText();
				
				_time2 = Integer.parseInt(startH.getValue().toString())*3600 + Integer.parseInt(startM.getValue().toString())*60 
						+ Integer.parseInt(startS.getValue().toString());
	
				//check whether the start time exceeds the length of the video
				if(_time2 >= (int)_time){
					JOptionPane.showMessageDialog(null, "Specified start time exceeds length of video");
				}
				//Check whether the start time + duration exceeds the length of the video
				else if(_time2 + Integer.parseInt(_durationTime) > (int)_time){
					JOptionPane.showMessageDialog(null, "Specified gif time exceeds media time");
				}
				//Check whether the name text field is empty
				else if(_newName == null || _newName.getText().equals("")){
					JOptionPane.showMessageDialog(null, "Please enter the new name for gif file");
				}
				//GIF file should not have extension
				else if(_newName.getText().contains(".gif")){
					JOptionPane.showMessageDialog(null, "Please remove .gif extension from new name");
				}
				//if all conditions are met
				else{
					// create new GIFWorker and execute
					gw = new GIFWorker(_file, timeStart, _durationTime, _gifName);
					gw.execute();
				
				}
				
			}
			
		});
		
		// add action listener to cancel button
		_cancel.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				// if worker is in process, cancel it
				if(gw != null){
					gw.cancel(true);
					gw.stopProcess();
				}
				//dispose frame
				dispose();
				setVisible(false);
			}
		});
		
	}
	
}
