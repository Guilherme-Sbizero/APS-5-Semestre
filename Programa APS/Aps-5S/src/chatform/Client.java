package chatform;

import java.awt.BorderLayout;
import java.awt.event.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.DefaultCaret;

import java.io.*;

import java.net.ConnectException;
import java.net.Socket;
import java.awt.Font;
import java.awt.Color;

public class Client extends JFrame implements Runnable {

    /**
     * Varia veis do Socket
     */
    private Socket socket;
    private BufferedWriter bufferWriter;

    /**
     * Variaveis da clsse Client
     */
    private String user;
    private String serverIP;
    private int serverPort;
    Login login = new Login();

    /**
     * Variaveis do Form
     */
    private static final long serialVersionUID = 5391582161763137020L;
    private JTextField inputText;
    private JTextArea output;
    

    /**
     * Execucao da aplicacao
     */
    public void run() {
        try {
            this.setTitle(this.user);
            this.establishConnection();
            this.listenConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Cria o frame, aqui e verificado a conexao do socket,
     * como tambem, se necessario encerra o programa ou retorna para tela anterior
     */
    public Client(String[] args) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
                                   @Override
                                   public void windowClosing(WindowEvent e) {
                                       if(socket == null)
                                           dispose();
                                       else if(!socket.isClosed()) {
                                           try {
                                               disconnect();
                                           } catch (Exception ex) {
                                               ex.printStackTrace();
                                           }
                                       }

                                       Start start = new Start();
                                       start.setVisible(true);
                                       dispose();
                                   }
                               }
        );

        /* Aqui e definido as propriedades da interface e seus componentes
         * 
         */
        setBounds(100, 100, 600, 500);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));
        setLocationRelativeTo(null);
        setTitle("Aplicacao de Conversa (Cliente)");

        output = new JTextArea();
        output.setBackground(Color.WHITE);
        output.setFont(new Font("Arial Black", Font.PLAIN, 12));
        output.setEditable(false);
        output.setLineWrap(true);

        JScrollPane messages = new JScrollPane(output);
        messages.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        DefaultCaret caretOutput = (DefaultCaret) output.getCaret();
        caretOutput.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        contentPane.add(messages, BorderLayout.CENTER);

        JPanel interactive = new JPanel();
        contentPane.add(interactive, BorderLayout.SOUTH);
        interactive.setLayout(new BorderLayout(0, 0));

        inputText = new JTextField();
        inputText.setFont(new Font("Arial Black", Font.PLAIN, 12));
        interactive.add(inputText, BorderLayout.CENTER);
        inputText.setColumns(10);

        JPanel buttons = new JPanel();
        interactive.add(buttons, BorderLayout.EAST);
        buttons.setLayout(new BorderLayout(0, 0));

        JButton btnSend = new JButton("Enviar");
        btnSend.setBackground(new Color(7, 242, 237));
        btnSend.setFont(new Font("Arial Black", Font.PLAIN, 11));

        btnSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String text = inputText.getText();
                if(!text.equals("")) {
                    sendMessage(concatMsg(text));
                    inputText.setText("");
                }
            }
        });
        buttons.add(btnSend, BorderLayout.NORTH);

        inputText.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String text = inputText.getText();
                    if(!text.equals("")) {
                        sendMessage(concatMsg(text));
                        inputText.setText("");
                    }
                }
            }
        });

        JButton btnExit = new JButton("Desconectar");
        btnExit.setBackground(new Color(7, 242, 237));
        btnExit.setFont(new Font("Arial Black", Font.PLAIN, 11));
        btnExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    disconnect();Client.this.dispose();  				
    				login.setVisible(true);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        buttons.add(btnExit, BorderLayout.SOUTH);
        
        JButton btnBack = new JButton("Voltar");
        btnBack.setBackground(new Color(7, 242, 237));
        btnBack.setFont(new Font("Arial Black", Font.PLAIN, 11));
        btnBack.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Client.this.dispose();
        		login.setVisible(true);
        	}
        });
        buttons.add(btnBack, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
        setClientInfo(args[0], args[1], args[2]);
    }

    private void setClientInfo(String user, String serverIP, String serverPort) {
        this.user = user;
        this.serverIP = serverIP;
        this.serverPort = Integer.parseInt(serverPort);
    }

    private void sendMessage(String msg) {
        try {
            bufferWriter.write(msg);
            bufferWriter.flush();
        } catch (Exception e) {
            writeOutput("Desconectado");
        }
    }

    private String concatMsg(String msg) {
        return "Text&" + "[" + user + "]" + " ~> " + msg + "\r\n";
    }

    private void writeOutput(String phrase) {
        output.append(phrase + "\r\n");
    }

    
    /*Aqui verifica o IP e a Porta informados no login e estabelece a conexao com o servidor,
     * caso contrario, apresenta a mensagem de erro
     * 
     */
    private void establishConnection() {
        try {
            socket = new Socket(this.serverIP, this.serverPort);
            bufferWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferWriter.write(user + "\r\n");
            bufferWriter.flush();
        } catch (ConnectException e) {
            JOptionPane.showMessageDialog(null, "Nao foi possovel criar a conexao, servidor indisponivel na porta e IP indicados.");
            Client.this.dispose();
			login.setVisible(true);
            
        } catch (Exception e) {
            e.printStackTrace();
            disconnect();
        }
    }

    /*
     * Aqui e verificado a mensagem, se ocorrer algum problema na mensagem, apresenta erro e desconeta o usuario
     */
    private void listenConnection() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String msg = "";
            String command;
            String textMsg;

            do {
                if (bufferedReader.ready()) {
                    msg = bufferedReader.readLine();
                    command = msg.split("&", 2)[0];
                    textMsg = msg.split("&", 2)[1];

                    if(command.equals("Text")) {
                        writeOutput(textMsg);
                    } else {
                        writeOutput("Algo esta errado na mensagem recebida do servidor");
                    }
                }
            } while (!("Disconnect " + user).equalsIgnoreCase(msg));

        } catch (Exception e) {
            System.out.println("Impossovel escutar servidor. O mesmo possivelmente esta indisponivel.");
        }
    }

    private void disconnect() {
        writeOutput("Desconectado");
        sendMessage("Disconnect " + this.user);
        try {
            bufferWriter.close();
            socket.close();
        } catch (Exception e) {
            System.out.println("Nao e possivel fechar conexao.");
        }
    }
}
