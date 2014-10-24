import java.io.File;

import javax.swing.JFileChooser;


public class FileChooser {
	
	public String chooseFile(){
		String dirPath = "";
		JFileChooser chooseFile = new JFileChooser();
		//set directory to be current directory
		File dir = new File(System.getProperty("user.dir"));
		chooseFile.setCurrentDirectory(dir);
		int result = chooseFile.showDialog(null, null);

		if (chooseFile.getSelectedFile() == null) {
			try {
				dirPath = null;
			
			} catch (NullPointerException e) {
				// do nothing
			}
		} else if(result == JFileChooser.APPROVE_OPTION) {
			if(chooseFile.getSelectedFile() != null){

				dirPath = chooseFile.getSelectedFile().getName();
				
			}
		}
		return dirPath;

	}
}
