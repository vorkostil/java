package common;

import helper.DataRepository.DataInformation;

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
	private static final String SERVER_NAME_KEY = "server_name";
	private static final String SERVER_HOST_KEY = "server_host";
	private static final String SERVER_LOGIN_KEY = "server_login";
	private static final String SERVER_PASSWD_KEY = "server_passwd";

	private ConnectionInfo info = new ConnectionInfo();
	
	private JLabel serverLabel;
	private JLabel portLabel;
	private JLabel loginLabel;
	private JLabel passwdLabel;
	
	private JTextField serverText;
	private JTextField portText;
	private JTextField loginText;
	private JTextField passwdText;

	private String registerServerName = "127.0.0.1";
	private String registerServerHost = "8001";
	private String registerLogin = "";
	private String registerPasswd = "";
	
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

	public void setDataInformation( DataInformation connectionInformation )
	{
		if ( connectionInformation.contains( SERVER_NAME_KEY ) == true )
		{
			registerServerName = connectionInformation.getStringValue( SERVER_NAME_KEY );
		    serverText.setText( registerServerName );
		}
		if ( connectionInformation.contains( SERVER_HOST_KEY ) == true )
		{
			registerServerHost = connectionInformation.getStringValue( SERVER_HOST_KEY );
		    portText.setText( registerServerHost );
		}
		if ( connectionInformation.contains( SERVER_LOGIN_KEY ) == true )
		{
			registerLogin = connectionInformation.getStringValue( SERVER_LOGIN_KEY );
		    loginText.setText( registerLogin );
		}
		if ( connectionInformation.contains( SERVER_PASSWD_KEY ) == true )
		{
			registerPasswd = connectionInformation.getStringValue( SERVER_PASSWD_KEY );
		    passwdText.setText( registerPasswd );
		}
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
	    serverText.setText( registerServerName );
	    serverLabel = new JLabel("server:");
	    
	    portText = new JTextField();
	    portText.setPreferredSize(new Dimension(100, 25));
	    portText.setText( registerServerHost );
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
	    loginText.setText( registerLogin );
	    loginText.setPreferredSize(new Dimension(100, 25));
	    loginLabel = new JLabel("login:");
	    loginText.requestFocusInWindow();
	    
	    passwdText = new JTextField();
	    passwdText.setText( registerPasswd );
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
