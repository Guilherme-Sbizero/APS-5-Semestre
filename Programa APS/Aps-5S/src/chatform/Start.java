package chatform;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class Start extends JFrame {

    /*
     * Variáveis da classe Start
    */
    private static final long serialVersionUID = 5602644222765201727L;
    private final JPanel contentPanel = new JPanel();

    /* 
     *  Executa a aplicação
     */
    public static void main(String[] args) {
        try {
            Start dialog = new Start();
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     *  Definido as propriedades dos componentes utilizado e ação dos botões
     */
    public Start() {
    	getContentPane().setBackground(Color.BLACK);
    	setForeground(Color.BLACK);
    	setBackground(Color.WHITE);
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(null);
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBounds(0, 0, 434, 261);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel);
        contentPanel.setLayout(null);
        setLocationRelativeTo(null);
        setTitle("Aplicação de Conversa");
        {
            JButton btnServidor = new JButton("Servidor");
           
            btnServidor.setBackground(new Color(7, 242, 237));
            btnServidor.setFont(new Font("Arial Black", Font.PLAIN, 11));
            btnServidor.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    SetupServer server = new SetupServer();
                    server.setVisible(true);
                    Start.this.dispose();
                }
            });
            btnServidor.setBounds(10, 126, 182, 61);
            contentPanel.add(btnServidor);
        }
        
        {
            JButton btnCliente = new JButton("Cliente");
           
            btnCliente.setBackground(new Color(7, 242, 237));
            btnCliente.setFont(new Font("Arial Black", Font.PLAIN, 11));
            btnCliente.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	SetupServer s1 = new SetupServer();
                    Login login = new Login();
                    login.setVisible(true);
               
                                      
                    
                }
            });
            btnCliente.setBounds(242, 126, 182, 61);
            contentPanel.add(btnCliente);
        }
        {
            JLabel lblInicializar = new JLabel("Como deseja inicializar o programa?");
            lblInicializar.setForeground(Color.BLACK);
            lblInicializar.setBounds(67, 40, 305, 34);
            contentPanel.add(lblInicializar);
            lblInicializar.setFont(new Font("Arial Black", Font.PLAIN, 15));
        }
    }

}
