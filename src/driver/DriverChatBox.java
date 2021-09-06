package driver;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
public class DriverChatBox extends JFrame{
    Label historyLabel,enterNewMessageLabel;
    static JTextArea historyTextArea,newMessageTextArea;
    static JButton sendButton;
    static BufferedReader bufferedReader;
    static BufferedWriter bufferedWriter;
    static Socket driverSocket;
    static int customerPortNumber;
    static String customerName;
    DriverChatBox(String name,int PortNumber){
        setBounds(0,0,1900,1000);
        setVisible(true);
        setLayout(null);
        setBackground(new Color(216, 115, 255));
        setFont(new Font("Times New Roman",Font.BOLD,25));

        //Label
        historyLabel=new Label("History : ");
        enterNewMessageLabel=new Label("Enter new Message : ");
        historyLabel.setBounds(0,0,200,50);
        enterNewMessageLabel.setBounds(0,620,200,50);
        add(enterNewMessageLabel);
        add(historyLabel);

        //Button
        sendButton=new JButton("Send");
        sendButton.setBounds(1220,620,200,50);
        add(sendButton);
        sendButton.setBorder(TabServer.bdr);

        //Text Area
        historyTextArea=new JTextArea();
        newMessageTextArea=new JTextArea();
        historyTextArea.setBounds(210,10,1700,600);
        newMessageTextArea.setBounds(210,620,1000,200);
        add(historyTextArea);
        add(newMessageTextArea);
        historyTextArea.setBorder(TabServer.bdr);
        newMessageTextArea.setBorder(TabServer.bdr);

        customerPortNumber=PortNumber;
        customerName=name;

        try {
            driverSocket = new Socket("127.0.0.1", customerPortNumber);
            bufferedReader = new BufferedReader(new InputStreamReader(driverSocket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(driverSocket.getOutputStream()));
        } catch (Exception e) {
            System.out.println("DriverChatBox --->establishConnection" + e);
            JOptionPane.showMessageDialog(null, "Customer is Offline please retry again");
        }
        Thread readerThread = new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        String message = bufferedReader.readLine();
                        historyTextArea.setText(historyTextArea.getText() + "\n" + customerName + " : " + message);
                    }

                } catch (Exception e) {
                    System.out.println("DriverChatBox" + e);
                }
            }
        };
        readerThread.start();
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(newMessageTextArea.getText().trim().equals("")){
                    return;
                }
                historyTextArea.setText(historyTextArea.getText()+"\nYOU : "+newMessageTextArea.getText());
                try{
                    bufferedWriter.write(newMessageTextArea.getText());
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
                catch(Exception ex){
                    System.out.println("DriverChatBox class"+ex);
                }
            }
        });
    }
}
