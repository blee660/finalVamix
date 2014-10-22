import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * This JTextFieldLimit class is a helper class that limits the
 * maximum number of characters that can be entered in the
 * JTextArea in the TextAdderFrame
 * 
 * 
 * @author Jenny Lee
 *
 */

public class JTextFieldLimit extends PlainDocument {

	private int limit;
	  // optional uppercase conversion
	  private boolean toUppercase = false;
	  
	  //constructor for the class
	  JTextFieldLimit(int limit) {
	   super();
	   this.limit = limit;
	   }
	   
	  //another constructor for different input parameter
	  JTextFieldLimit(int limit, boolean upper) {
	   super();
	   this.limit = limit;
	   toUppercase = upper;
	   }
	 
	  public void insertString
	    (int offset, String  str, AttributeSet attr)
	      throws BadLocationException {
	   if (str == null) return;

	   if ((getLength() + str.length()) <= limit) {
	     if (toUppercase) str = str.toUpperCase();
	     super.insertString(offset, str, attr);
	     }
	   }
}