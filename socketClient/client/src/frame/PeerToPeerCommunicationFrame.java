package frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.PrintWriter;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EtchedBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import main.GraphicalClient;

import common.MessageType;

@SuppressWarnings("serial")
public class PeerToPeerCommunicationFrame extends JFrame 
{
	String targetName = null;
	String login = null;
	PrintWriter targetWriter = null;
	GraphicalClient father = null;
	
	JTextField textEdition = new JTextField();
	JTextPane chatArea = new JTextPane();
	
	public PeerToPeerCommunicationFrame( GraphicalClient gClient, String source, String name, PrintWriter writer ) 
	{
		targetWriter = writer;
		targetName = name;
		login = source;
		father = gClient;
		
		// characteristics of the frame
		this.setTitle( "Chat with " + name );
		this.setSize( 400, 320 );
		this.setLocationRelativeTo( null );

		this.addWindowListener( new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent arg0) 
			{
				if  ( targetWriter != null )
				{
					father.closeSpecificCommunication( targetName, false );
					targetWriter.println( MessageType.MessageSystem + " " + MessageType.MessageCommunicationSpecificClose + " " + targetName + " " + login );
					targetWriter.flush();
				}
			}
			
			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		createElement();
	}
	
	private void createElement()
	{
		// create the reference panel
//		JPanel pan = new ClientPanel();
		
		// associate the panel to the frame
//		this.setContentPane(pan);

		// associate a BorderLayout (simplest one)
		this.setLayout( new BorderLayout() );
		
		textEdition.setMinimumSize( new Dimension( 400, 32 ) );
		textEdition.addKeyListener( new KeyListener() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				if ( arg0.getKeyCode() == KeyEvent.VK_ENTER )
				{
					String message = textEdition.getText();
					textEdition.setText( "" );
					
					if  ( targetWriter != null )
					{
						targetWriter.println( MessageType.MessageSystem + " " + MessageType.MessageCommunicationSpecific + " " + targetName + " " + login + " " + message );
						appendToChatArea( login + "> " +  message + "\n", GraphicalClient.normalFont, GraphicalClient.normalColor );
						
						targetWriter.flush();
					}
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}
		});
		textEdition.setBorder( new EtchedBorder( EtchedBorder.RAISED ) );
		
		chatArea.setEditable( false );
		chatArea.setBorder( new EtchedBorder( EtchedBorder.RAISED ) );
		chatArea.setBackground( new Color( 232, 232, 196 ) );
		
		this.getContentPane().add( textEdition, BorderLayout.SOUTH );
		this.getContentPane().add( new JScrollPane(  chatArea ), BorderLayout.CENTER );
		
		// display itself
		this.setVisible(true);
	}
	
    public void appendToChatArea(String msg, Font f, Color c)
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
			chatArea.getDocument().insertString( chatArea.getDocument().getLength(), msg, aset );
			chatArea.setCaretPosition( chatArea.getDocument().getLength() );
		} 
        catch (BadLocationException e) 
        {
			e.printStackTrace();
		}
    }
}
