package frame;

import graphic.listener.ClosingMessageListener;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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
import network.client.ConnectionClient;

import common.MessageType;

@SuppressWarnings("serial")
public class PeerToPeerCommunicationFrame extends JFrame 
{
	String targetName;
	GraphicalClient father;
	ConnectionClient connectionClient;
	
	JTextField textEdition = new JTextField();
	JTextPane chatArea = new JTextPane();
	
    public PeerToPeerCommunicationFrame( GraphicalClient gClient,
										 ConnectionClient cClient, 
										 String name ) 
    {
		this.connectionClient = cClient;
		targetName = name;
		father = gClient;
		
		// characteristics of the frame
		this.setTitle( "Chat with " + name );
		this.setSize( 400, 320 );
		this.setLocationRelativeTo( null );

		this.addWindowListener( new ClosingMessageListener( connectionClient, 
															MessageType.MessageSystem + " " + MessageType.MessageCommunicationSpecificClose + " " + targetName ) ); 
		
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
					
					connectionClient.sendMessageIfConnected( MessageType.MessageSystem + " " + MessageType.MessageCommunicationSpecific + " " + targetName + " " + connectionClient.getLogin() + " " + message );
					appendToChatArea( connectionClient.getLogin() + "> " +  message, GraphicalClient.normalFont, GraphicalClient.normalColor );
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
		
		// send the connected message
		connectionClient.sendMessageIfConnected( MessageType.MessageSystem + " " + MessageType.MessageCommunicationSpecificOpen + " " + name + " " + connectionClient.getLogin() );
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
			chatArea.getDocument().insertString( chatArea.getDocument().getLength(), msg + "\n", aset );
			chatArea.setCaretPosition( chatArea.getDocument().getLength() );
		} 
        catch (BadLocationException e) 
        {
			e.printStackTrace();
		}
    }
}
