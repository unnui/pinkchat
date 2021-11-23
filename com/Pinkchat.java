package com;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.Image.*;
import java.io.*;
import java.net.*;
import java.time.*;

public class Pinkchat extends JFrame implements ActionListener {

	//Inicializaciones
	JMenuBar Barra;
	JMenu Acciones, Ayuda;
	JMenuItem Salir, Boop, Hostear, Unirse, Version;
	static JTextArea ChatLog;
	JTextField ChatMessage;
	WinCanvas canv;
	JPanel chat;
	String message;
	Point punto;
	HelpWin win_help;
	JoinWin win_join;
	ServerWin win_server;
	static Socket socket;
	static ServerSocket server_socket;
	static InputStream input;
	static BufferedReader reader;
	static OutputStream output;
	static PrintWriter writer;
	
		
	static Pinkchat pinkchat;

	//Constructor de ventana
	public Pinkchat(){
		try{
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		}
		catch(Exception e) {
			e.getMessage();
		}
		setLocation(300,300);
		
		Image icon = Toolkit.getDefaultToolkit().getImage("assets/icon.png");
		setIconImage(icon);
		
		Barra = new JMenuBar();
		Acciones = new JMenu("Acciones");
		Ayuda = new JMenu("Ayuda");
		Salir = new JMenuItem("Salir");
		Boop = new JMenuItem("Boop");
		Hostear = new JMenuItem("Hostear");
		Unirse = new JMenuItem("Unirse");
		Version = new JMenuItem("Version");
		ChatLog = new JTextArea("Te doy la bienvenida a Pinkchat!");
		ChatMessage = new JTextField();
		chat = new JPanel();
		canv = new WinCanvas();
		
		//ChatLog.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		ChatLog.setLineWrap(true); ChatLog.setWrapStyleWord(true); ChatLog.setEditable(false);
		ChatMessage.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		ChatMessage.setScrollOffset(200); ChatMessage.setEditable(false);
		ChatLog.setBounds(20,20,500-20,320-20);
		ChatMessage.setBounds(20,340+20,500-20,150-90);
		ChatMessage.addActionListener(this);
		
		Version.addActionListener(this);
		Salir.addActionListener(this);
		Hostear.addActionListener(this);
		Unirse.addActionListener(this);
		
		Acciones.add(Hostear);
		Acciones.add(Unirse);
		Acciones.add(Boop);
		Acciones.add(Salir);
		Ayuda.add(Version);
		Barra.add(Acciones);
		Barra.add(Ayuda);
		
		
		ChatLog.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		ChatMessage.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		
		chat.setLayout(null); 	
		chat.setBounds(0,0,500,650);
		canv.setBounds(500,0,150,500);

		Barra.setBackground(new Color(1f,0.89f,0.85f));
		canv.setBackground(new Color(0.976f,0.82f,0.80f));
		chat.setBackground(new Color(0.976f,0.82f,0.80f));
		ChatLog.setBackground(new Color(0.976f,0.834f,0.81f));
		ChatMessage.setBackground(new Color(1f,0.89f,0.85f));
		chat.add(ChatLog);
		chat.add(ChatMessage);
		setJMenuBar(Barra);
		add(chat);
		add(canv);
		
		setTitle("Pinkchat");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		setSize(650,500);
		setResizable(false);
		setVisible(true);
		
	}

	public static void main(String[] args) {
		System.out.println("Bienvenido a Pinkchat!\nVersion 1.0-SNAPSHOT /---/ @Autor: Nui");
		pinkchat = new Pinkchat();
		
		while (!false){
			
			while (ChatLog.getLineCount() > 15){
				try {
				int end = ChatLog.getLineEndOffset(0);
				ChatLog.replaceRange("", 0, end);  } catch (Exception error) {}
			}
			
			try {
			Thread.sleep(20);}
			catch (Exception e) { e.getMessage(); }
			
			try {
				if (reader != null) {
				
				String str = reader.readLine();
				if (str == null) { socket.close(); reader = null; ChatLog.append("\n[Desconectado]"); break; }
				ChatLog.append("\n" + LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute() + " [Them] " + str); }}
			catch (Exception e) {}
		}
}


public void actionPerformed(ActionEvent e) {
	if (e.getSource()==Salir){
	dispose();
	try { socket.close(); } catch (Exception error) {}
	try { server_socket.close(); } catch (Exception error) {}
	System.exit(0);
	}
	
	else if (e.getSource()==ChatMessage){
	try{
		message = ChatMessage.getText();
		
		writer.println(message);
		
		ChatLog.append("\n" + LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute() + " [You] " + message);
		
		while (ChatLog.getLineCount() > 15){
				try {
				int end = ChatLog.getLineEndOffset(0);
				ChatLog.replaceRange("", 0, end);  } catch (Exception error) {}}
				
				
		ChatMessage.setText(""); }
	catch(Exception error) { error.getMessage();}
	}
	
	else if (e.getSource()==Version){
		win_help = new HelpWin();
	}
	
	else if (e.getSource()==Hostear){
		win_server = new ServerWin();
	}
	
	else if (e.getSource()==Unirse){
		win_join = new JoinWin();
	}
	
	
	try {
	if (e.getSource()==win_server.Button1) {
		try {
			Listen(Integer.parseInt(win_server.Input1.getText()));
			win_server.dispose(); }
		catch (Exception error) {
			error.getMessage(); }
	}}
	catch (Exception error) {}
	
	try {
	if (e.getSource()==win_join.Button2) {
		try {
			Join(win_join.Input1.getText(), Integer.parseInt(win_join.Input2.getText()));
			win_join.dispose(); }
		catch (Exception error) {
			error.getMessage(); }
	}}
	catch (Exception error) {}
	
}

public class WinCanvas extends Canvas{
	public void paint(Graphics g)
	{
		Image i=Toolkit.getDefaultToolkit().getImage("assets/plush_sit1_mini.png");
		g.drawImage(i,-10,250,canv);
	}
}

public class HelpWin extends JFrame {
	public HelpWin(){
	setTitle("Te amo");
	JPanel bg = new JPanel();
	bg.setBackground(new Color(0.976f,0.82f,0.80f));
	JLabel d = new JLabel("Pinkchat Snapshot 1.0 - gf version");
	add(d);
	punto = pinkchat.getLocation();
	setLocation(punto.x+200,punto.y+200);
	setSize(255,70);
	setVisible(true);
	setResizable(false);
	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	add(bg);
	}
}

public class JoinWin extends JFrame {
	JButton Button2;
	JTextField Input1, Input2;

	public JoinWin() {
	setTitle("Unirse");
	JPanel bg = new JPanel();
	bg.setBackground(new Color(0.976f,0.82f,0.80f));
	bg.setLayout(null);
	
	JLabel Texto1 = new JLabel("IP:");
	Texto1.setBounds(50,20,100,20);
	JLabel Texto2 = new JLabel("Puerto:");
	Texto2.setBounds(50,90,100,20);
	bg.add(Texto1);
	bg.add(Texto2);
	
	Input1 = new JTextField();
	Input1.setBounds(50,40,200,30);
	bg.add(Input1);
	
	Input2 = new JTextField();
	Input2.setBounds(50,110,200,30);
	bg.add(Input2);
	
	Button2 = new JButton("Conectarse");
	Button2.setBounds(120,155,130,20);
	bg.add(Button2);
	Button2.addActionListener(pinkchat);
	
	punto = pinkchat.getLocation();
	setLocation(punto.x+200,punto.y+100);
	setSize(300,230);
	setVisible(true);
	setResizable(false);
	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	add(bg);
	}
}

public class ServerWin extends JFrame {
	JButton Button1;
	JTextField Input1;

	public ServerWin() {
	setTitle("Hostear");
	JPanel bg = new JPanel();
	bg.setBackground(new Color(0.976f,0.82f,0.80f));
	bg.setLayout(null);
	
	
	JLabel Texto1 = new JLabel("Puerto:");
	Texto1.setBounds(50,20,100,20);
	
	Input1 = new JTextField();
	Input1.setBounds(50,40,200,30);
	bg.add(Input1);
	bg.add(Texto1);
	
	Button1 = new JButton("Abrir");
	Button1.setBounds(150,85,100,20);
	bg.add(Button1);
	Button1.addActionListener(pinkchat);
	
	punto = pinkchat.getLocation();
	setLocation(punto.x+200,punto.y+100);
	setSize(300,150);
	setVisible(true);
	setResizable(false);
	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	add(bg);
	}
}

public void Listen (int port) {
	try {
		server_socket = new ServerSocket (port);
		System.out.println("Escuchando en puerto " + port);
		ChatLog.append("\nEscuchando en puerto " + port + "...");
		while (!false){
			try {
			socket = server_socket.accept();
			ChatLog.append("\n[Conexión establecida!]");
			ChatMessage.setEditable(true);
			
			input = socket.getInputStream();
			reader = new BufferedReader(new InputStreamReader(input));
			
			OutputStream output = socket.getOutputStream();
         		writer = new PrintWriter(output, true);
			
			break; }
			catch(Exception error) { Thread.sleep(20); }
		}
		
	}
	catch (Exception e) {
		e.getMessage();
	}
}


public void Join (String Ip, int port) {
	try {
		socket = new Socket (Ip, port);
		System.out.println("Conectándose a " + Ip + ":" + port);
		ChatLog.append("\nConectándose a " + Ip + ":" + port + "...");
		while (!false) {
			if (socket.isConnected() == true) { 
			ChatLog.append("\n[Conexión establecida!]");
			ChatMessage.setEditable(true);
			
			input = socket.getInputStream();
			reader = new BufferedReader(new InputStreamReader(input));
			
			output = socket.getOutputStream();
            		writer = new PrintWriter(output, true);
			
			break; }
			Thread.sleep(20);
		}
}
	catch (Exception  e) {
		e.getMessage();
	}
}
}
