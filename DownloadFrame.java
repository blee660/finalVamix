package GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import worker.DownloadWorker;

/**
 * The DownloadFrame class is responsible for receiving necessary inputs for downloading.
 * 
 * @author Jenny Lee
 *
 */

public class DownloadFrame extends JFrame implements ActionListener{

	//Declaration of all the necessary fields and objects
	private JTextField _text = new JTextField(30);
	private JButton _download = new JButton("Download");
	private JButton _cancel = new JButton("Back");
	private JButton _stop = new JButton("Stop Download");
	private String _url;
	private FlowLayout _layout = new FlowLayout();
	private JProgressBar _progressBar = new JProgressBar();
	
	private JPanel _listOp = new JPanel();
	private JLabel _openSource = new JLabel("<html>Is this an open-source URL?<html>");
	private JPanel _listPane = new JPanel();
	private JRadioButton _no = new JRadioButton("no", true);
	private JRadioButton _yes = new JRadioButton("yes");
	
	private int _override = 0;
	
	// constructor is defined here.
	DownloadFrame (){
		
		//setting up the GUI
		setTitle("DOWNLOAD");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(400,200);
		setLayout(_layout);
		add(new JLabel("Enter URL to download"));
		add(_text, BorderLayout.CENTER);
		add(_progressBar);

		_listPane.add(_download);
		_listPane.add(_cancel);
		_listPane.add(_stop);
		_stop.setEnabled(false);

		_listOp.add(_openSource);
		_listOp.add(_no);
		_listOp.add(_yes);

		add(_listOp);
		ButtonGroup group = new ButtonGroup();
		group.add(_no);
		group.add(_yes);

		add(_listPane, BorderLayout.CENTER);
		add(_cancel, BorderLayout.WEST);
		//pressing download button will start downloading.
		_download.addActionListener(this);
		
		//pushing the cancel button will close the download frame
		_cancel.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//obtain url from the text field
		_url = _text.getText();
		
		//URL should contain "http://"
		if (_url.contains("http://")) {
			String fileName = _url.substring( _url.lastIndexOf('/')+1, _url.length() );
			File f = new File (fileName);
			
			//if the URL field is empty, ask for it by sending a message
			if (_url.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Please enter the URL");
			}
			
			//make sure download only happens if it is from open source
			if(_yes.isSelected()){	
				
				//if a file with the same base name already exists, ask if intention is to override
				if (f.exists()){
					int o = JOptionPane.showConfirmDialog(null, "<html>File already exists. Do you wish to override? <br> no will resume previous download<br> cancel will cancel Download <html>");
					
					if ( o == JOptionPane.YES_OPTION){
						_override = 0;
						f.delete();
					}	
					else if( o == JOptionPane.NO_OPTION){
						_override = 1;	
					}
					else{
						// if intention is not to override, cancel download
						JOptionPane.showMessageDialog(null, "Download Cancelled.");
					}
				}
				//commence download if all conditions are met
				if(_override == 1 || _override == 0 || !(f.exists())){		
					_stop.setEnabled(true);
					DownloadWorker dworker = new DownloadWorker(_url, _override, _progressBar);
					_stop.addActionListener(dworker);
					dworker.execute();
				}
			}
			//if URL is not open source, display relevant message box
			else{
				JOptionPane.showMessageDialog(DownloadFrame.this, "Only open-source URLs can be downloaded");
			}
		} else {
			JOptionPane.showMessageDialog(null, "Error: This URL can not be downloaded");
		}
		
	}
}