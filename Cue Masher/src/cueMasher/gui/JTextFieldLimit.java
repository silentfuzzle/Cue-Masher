// License MIT
// 2016, Emily Palmieri <silentfuzzle@gmail.com>

package cuemasher.gui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

// When applied to a JTextField, this object limits the number of characters the user can enter into a text box.
public class JTextFieldLimit extends PlainDocument {
	private static final long serialVersionUID = 1L;
	private int limit;

	// Constructor
	// limit - The maximum number of characters that can be entered into the associated text box
	JTextFieldLimit(int limit) {
		super();
		this.limit = limit;
	}

	@Override
	public void insertString( int offset, String  str, AttributeSet attr ) throws BadLocationException {
		if (str == null) return;

		if ((getLength() + str.length()) <= limit) {
			super.insertString(offset, str, attr);
		}
	}
}
