package common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import network.client.ConnectionInfo;

@SuppressWarnings("serial")
public class ConnectionDialog extends JDialog {
	private ConnectionInfo info = new ConnectionInfo();
	
	private JLabel serverLabel;
	private JLabel portLabel;
	private JLabel loginLabel;
	private JLabel passwdLabel;
	
	private JTextField serverText;
	private JTextField portText;
	private JTextField loginText;
	private JTextField passwdText;
	
	public ConnectionDialog( JFrame parent,
							 String title,
							 boolean modal )
	{
		super( parent,
			   title,
			   modal );
		
		this.setSize( 420, 210);
		this.setLocationRelativeTo( null );
		this.setResizable( false );
		this.setDefaultCloseOperation( JDialog.DO_NOTHING_ON_CLOSE );
		this.init();
	}

	public ConnectionInfo showConnectionDialog()
	{
	    this.setVisible(true);      
	    return this.info;      
	}
	
	private void init() 
	{
		JPanel serverPanel = new JPanel();
		serverPanel.setBackground( Color.white );
		serverPanel.setPreferredSize(new Dimension(400, 60));
	    serverPanel.setBorder(BorderFactory.createTitledBorder("Server info"));
		
	    serverText = new JTextField();
	    serverText.setPreferredSize(new Dimension(100, 25));
	    serverText.setText( "192.168.1.76" );
	    serverLabel = new JLabel("server:");
	    
	    portText = new JTextField();
	    portText.setPreferredSize(new Dimension(100, 25));
	    portText.setText( "8001" );
	    portLabel = new JLabel("port:");
	    
	    serverPanel.add(serverLabel);
	    serverPanel.add(serverText);
	    serverPanel.add(portLabel);
	    serverPanel.add(portText);
		
		JPanel loginPanel = new JPanel();
		loginPanel.setBackground( Color.white );
		loginPanel.setPreferredSize(new Dimension(400, 60));
		loginPanel.setBorder(BorderFactory.createTitledBorder("Login info"));
		
	    loginText = new JTextField();
	    loginText.setPreferredSize(new Dimension(100, 25));
	    loginLabel = new JLabel("login:");
	    loginText.requestFocusInWindow();
	    
	    passwdText = new JTextField();
	    passwdText.setPreferredSize(new Dimension(100, 25));
	    passwdLabel = new JLabel("passwd:");
	    
	    loginPanel.add(loginLabel);
	    loginPanel.add(loginText);
	    loginPanel.add(passwdLabel);
	    loginPanel.add(passwdText);
		

	    JPanel content = new JPanel();
	    content.setBackground(Color.white);
	    content.add(serverPanel);
	    content.add(loginPanel);
	    
	    JPanel control = new JPanel();
	    JButton okBouton = new JButton("OK");
	    
	    okBouton.addActionListener(new ActionListener(){
	      public void actionPerformed(ActionEvent arg0) {        
	        info = new ConnectionInfo( serverText.getText(),
	        						   portText.getText(),
	        						   loginText.getText(),
	        						   passwdText.getText() );
	        setVisible(false);
	      }
	    });

	    JButton cancelBouton = new JButton("Cancel");
	    cancelBouton.addActionListener(new ActionListener(){
	      public void actionPerformed(ActionEvent arg0) {
	        setVisible(false);
	      }      
	    });

	    control.add(okBouton);
	    control.add(cancelBouton);

	    this.getContentPane().add(content, BorderLayout.CENTER);
	    this.getContentPane().add(control, BorderLayout.SOUTH);
	}
}
