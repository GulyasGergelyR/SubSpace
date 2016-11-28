package Main;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.LayoutStyle;
import javax.swing.SpinnerNumberModel;
/**
 *
 * @author Akos
 */
public class MainWindow extends JFrame {
    
	JFormattedTextField ipAddress;
	JSpinner portNumber;
	
    public static void main(String[] args) {
    	if (args.length>0){
    		boolean server = false;
    		boolean serverWindow = false;
    		for(String s: args){
    			if (s.equals("-server")){
    				server = true;
    			}
    			if (s.equals("-serverWindow")){
    				serverWindow = true;
    			}
    		}
    		if (server){
    			try {
    				SMain.InitServer(serverWindow);
    				SMain.StartServer(serverWindow);
    			} catch (Exception e) {
    				if (SMain.getCommunicationHandler() != null)
    					SMain.getCommunicationHandler().CloseUDPNode();
    				e.printStackTrace();
    			} finally {
    				if (SMain.getCommunicationHandler() != null)
    					SMain.getCommunicationHandler().CloseUDPNode();
    			}
    		}else{
        		new MainWindow();
    		}
    	}else{
    		new MainWindow();
    	}
        
    }
    
    public MainWindow()
    {
        this.setSize(865, 525);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.getContentPane().setLayout(new GridLayout(1, 1));
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.addTab("Client", CreateClient());
        tabbedPane.addTab("Server", CreateServer());
        this.getContentPane().add(tabbedPane);

        this.setVisible(true);
    }
    
    private JPanel CreateClient()
    {
        JPanel p = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {

                  super.paintComponent(g);
                  ImageIcon temp = new ImageIcon("res\\object\\background\\bg1.png");

                  g.drawImage(temp.getImage(), 0, 0, null);
        }};
        
        JButton startClientButton = new JButton();
        JLabel ipLabel = new JLabel();
        ipAddress = new JFormattedTextField(new IPAddressFormatter());
        JLabel portLabel = new JLabel();
        portNumber = new JSpinner();
        
        startClientButton.setText("Start");
        startClientButton.addActionListener((ActionEvent evt) -> {
            StartClientButtonPressed(evt);
        });

        ipLabel.setForeground(new java.awt.Color(255, 255, 255));
        ipLabel.setText("Server IP address:");

        ipAddress.setText("127.0.0.1");

        portLabel.setForeground(new java.awt.Color(255, 255, 255));
        portLabel.setText("Server port:");

        portNumber.setModel(new SpinnerNumberModel(10000, 0, 65535, 1));

        GroupLayout layout = new GroupLayout(p);
        p.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(ipLabel)
                    .addComponent(portLabel))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(ipAddress)
                    .addComponent(portNumber, GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                .addComponent(startClientButton, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(ipLabel)
                    .addComponent(ipAddress, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(startClientButton))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(portLabel)
                    .addComponent(portNumber, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        return p;
    }
    
    private void StartClientButtonPressed(java.awt.event.ActionEvent evt) {  
    	this.setState(Frame.ICONIFIED);
    	try {
    		IPAddressFormatter ipAddressFormatter = new IPAddressFormatter();
			SMain.InitClient((byte[])ipAddressFormatter.stringToValue(ipAddress.getText()));
			SMain.StartClient();
		} catch (Exception e) {
			if (SMain.getCommunicationHandler() != null)
				SMain.getCommunicationHandler().CloseUDPNode();
			e.printStackTrace();
		} finally {
			if (SMain.getCommunicationHandler() != null)
				SMain.getCommunicationHandler().CloseUDPNode();
			this.dispose();
		}
    } 
    
    private JPanel CreateServer()
    {
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {

                  super.paintComponent(g);
                  ImageIcon temp = new ImageIcon("res\\object\\background\\bg1.png");

                  g.drawImage(temp.getImage(), 0, 0, null);
        }};
        
        JLabel portLabel = new JLabel();
        JSpinner portNumber = new JSpinner();
        JButton startServerButton = new JButton();

        portLabel.setForeground(new java.awt.Color(255, 255, 255));
        portLabel.setText("Port:");

        portNumber.setModel(new SpinnerNumberModel(10000, 0, 65535, 1));

        startServerButton.setText("Start");
        startServerButton.addActionListener((ActionEvent evt) -> {
            StartServerButtonPressed(evt);
        });

        GroupLayout layout = new GroupLayout(p);
        p.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(portLabel)
                .addGap(18, 18, 18)
                .addComponent(portNumber, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 89, Short.MAX_VALUE)
                .addComponent(startServerButton, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(portLabel)
                    .addComponent(portNumber, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(startServerButton))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        return p;
    }
    
    private void StartServerButtonPressed(java.awt.event.ActionEvent evt) {
    	this.setState(Frame.ICONIFIED);
    	try {
			SMain.InitServer(false);
			SMain.StartServer(false);
		} catch (Exception e) {
			if (SMain.getCommunicationHandler() != null)
				SMain.getCommunicationHandler().CloseUDPNode();
			e.printStackTrace();
		} finally {
			if (SMain.getCommunicationHandler() != null)
				SMain.getCommunicationHandler().CloseUDPNode();
			this.dispose();
		}
    } 
}
