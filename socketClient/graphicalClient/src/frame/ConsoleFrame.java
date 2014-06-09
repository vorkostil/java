package frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

@SuppressWarnings("serial")
public class ConsoleFrame extends JFrame 
{
	public static final Font errorFont = new Font( "Default", Font.BOLD, 12);
	public static final Font normalFont = new Font( "Default", Font.PLAIN, 12);
	public static final Font serverFont = new Font( "Default", Font.ITALIC, 12);
	
	public static final Color errorColor = Color.RED;
	public static final Color normalColor = Color.BLACK;
	public static final Color serverColor = Color.BLUE;
	
	JTextPane chatArea = new JTextPane();
	
	public ConsoleFrame()
	{
		// characteristics of the frame
		this.setTitle("Console");
		this.setSize(640, 320);
		this.setLocationRelativeTo(null);
		
		chatArea.setEditable( false );
		chatArea.setBackground( new Color( 232, 232, 196 ) );
		
		this.setLayout( new BorderLayout() );
		this.getContentPane().add( new JScrollPane(  chatArea ), BorderLayout.CENTER );
	}
	
	public void displayAlert( String message) 
	{
		appendToChatArea(message, errorFont, errorColor);
	}
	
	public void displayInfo( String message )
	{
		appendToChatArea(message, serverFont, serverColor);
	}
	
	public void displayText( String message )
	{
		appendToChatArea(message, normalFont, normalColor);
	}
	
    private void appendToChatArea(String msg, Font f, Color c)
    {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, f.getFamily() );
        aset = sc.addAttribute(aset, StyleConstants.FontSize, f.getSize() );
        aset = sc.addAttribute(aset, StyleConstants.FontConstants.Bold, f.isBold() );
        aset = sc.addAttribute(aset, StyleConstants.FontConstants.Italic, f.isItalic() );
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        try 
        {
			chatArea.getDocument().insertString( chatArea.getDocument().getLength(), msg + "\n", aset );
			chatArea.setCaretPosition( chatArea.getDocument().getLength() );
		} 
        catch (BadLocationException e) 
        {
			e.printStackTrace();
		}
    }
}
