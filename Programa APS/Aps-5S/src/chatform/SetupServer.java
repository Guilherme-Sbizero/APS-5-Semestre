package chatform;

import connection.Server;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.CardLayout;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.Color;

public class SetupServer extends JFrame {
	/**
	 * Server variáveis
	 */
	private static ServerSocket server;

	/**
	 *  Form variáveis
	 */
	private static final long serialVersionUID = 4998717362394143017L;
	private JPanel contentPane;
	private JTextField inputPort;
	private JButton btnOk;
	private JButton btnBack;
	private JPanel panelConfig;
	private JPanel panelStatus;
	private JLabel lblIp;
	private JLabel lblPort;
	private JLabel lblValueIP;
	private JLabel lblValuePort;
	private JButton btnStopConnection;
	private InetAddress inetAddress;
	private String hostaddress;
	private JButton btnBack_1;
	private String localhost = "";
	

	
	/**
	 * Executa a aplicação
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SetupServer frame = new SetupServer();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	


	
	/**
	 * Aqui é definido as propriedades dos componentes utilizados nessa tela
	 * além de definir a ação dos botões
	 */
	public SetupServer() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 366, 158);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));
		this.setLocationRelativeTo(null);
		this.setTitle("Aplicação de Conversa (Servidor)");
		
		panelConfig = new JPanel();
		contentPane.add(panelConfig, "panelConfig");
		panelConfig.setLayout(null);
		
		JLabel lblNumeroDaPorta = new JLabel("Número da porta:");
		lblNumeroDaPorta.setBounds(10, 35, 113, 18);
		panelConfig.add(lblNumeroDaPorta);
		lblNumeroDaPorta.setFont(new Font("Arial Black", Font.PLAIN, 11));
		
		inputPort = new JTextField();
		inputPort.setBounds(135, 35, 86, 20);
		panelConfig.add(inputPort);
		inputPort.setText("");
		inputPort.setColumns(10);
		
		btnOk = new JButton("Ok");
		btnOk.setBackground(new Color(72, 209, 204));
		btnOk.setFont(new Font("Arial Black", Font.PLAIN, 11));
		btnOk.setBounds(90, 86, 73, 23);
		panelConfig.add(btnOk);
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				      		
				startServer();
			}
		});
		
		btnBack = new JButton("Voltar");
		btnBack.setFont(new Font("Arial Black", Font.PLAIN, 11));
		btnBack.setBackground(new Color(72, 209, 204));
		btnBack.setBounds(177, 86, 73, 23);
		panelConfig.add(btnBack);
		
		panelStatus = new JPanel();
		contentPane.add(panelStatus, "panelStatus");
		panelStatus.setLayout(null);
		
		lblIp = new JLabel("IP:");
		lblIp.setFont(new Font("Arial Black", Font.PLAIN, 15));
		lblIp.setBounds(38, 11, 46, 14);
		panelStatus.add(lblIp);
		
		lblPort = new JLabel("Porta:");
		lblPort.setFont(new Font("Arial Black", Font.PLAIN, 15));
		lblPort.setBounds(38, 36, 57, 14);
		panelStatus.add(lblPort);
		
		lblValueIP = new JLabel(); 
		lblValueIP.setForeground(new Color(0, 100, 0));
		lblValueIP.setFont(new Font("Arial Black", Font.PLAIN, 13));
		lblValueIP.setBounds(67, 11, 171, 14);
		panelStatus.add(lblValueIP);
		
		lblValuePort = new JLabel(); 
		lblValuePort.setForeground(new Color(0, 100, 0));
		lblValuePort.setFont(new Font("Arial Black", Font.PLAIN, 13));
		lblValuePort.setBounds(97, 36, 121, 14);
		panelStatus.add(lblValuePort);
		
		btnStopConnection = new JButton("Encerrar Conexão");
		btnStopConnection.setBackground(new Color(72, 209, 204));
		btnStopConnection.setFont(new Font("Arial Black", Font.PLAIN, 11));
		btnStopConnection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				System.exit(0);
			}
		});
		btnStopConnection.setBounds(38, 79, 144, 30);
		panelStatus.add(btnStopConnection);
		
		btnBack_1 = new JButton("Voltar");
		btnBack_1.setFont(new Font("Arial Black", Font.PLAIN, 11));
		btnBack_1.setBackground(new Color(72, 209, 204));
		btnBack_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SetupServer.this.dispose();
				
				Start start = new Start();
				start.setVisible(true);
			}
		});
		btnBack_1.setBounds(212, 79, 75, 30);
		panelStatus.add(btnBack_1);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SetupServer.this.dispose();
				Start start = new Start();
				start.setVisible(true);
			}
		});
	}

	/*
	 * Aqui é iniciado o servidor, o inetadress pega as informações do localhost
	 * e inicia o servidor, deixando disponivel para novas conexões
	 */
	private void startServer() {
		try {
			server = new ServerSocket(Integer.parseInt(inputPort.getText()));
			new Thread(new Runnable() {
				@Override
				public void run() {		
					CardLayout c = (CardLayout)(contentPane.getLayout());
					c.show(contentPane, "panelStatus");
					try {
						inetAddress = InetAddress.getLocalHost();
						hostaddress = inetAddress.getHostAddress();
						localhost = hostaddress;
						lblValueIP.setText(hostaddress);
					} catch (UnknownHostException e1) {
						e1.printStackTrace();
					}					
					lblValuePort.setText(inputPort.getText());
					while (true) {
		                System.out.println("Waiting connection...");
		                try {
		                Socket connection = server.accept();
		                Thread serverThread = new Server(connection);
		                serverThread.start();
		                } catch (Exception e) {
		                	e.printStackTrace();
		                }		                
		            }
				}				
			}).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
		}


}

