package antiSpamFilter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;

import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class GUI {
	private Controller controller = new Controller();
	private JFrame janelaPrincipal;
	private JPanel painel;
	static final double fator = 0.5;
	private static Font f = new Font("Century Gothic", Font.PLAIN, 18);
	private static Font f2 = new Font("Century Gothic", Font.PLAIN, 16);
	private static Font f3 = new Font("Century Gothic", Font.BOLD, 20);
	private JTextField info;
	private JTextField fp;
	private JTextField fn;
	private JTable table;
	private JScrollPane scroll;
	final JFileChooser fc = new JFileChooser();
	private String[][] dados;
	private DefaultTableModel model;
	private String[] colunas;
	private int botao = 0;

	public static void main(String[] args) {
		GUI janela = new GUI();
	}

	// FRAME

	public GUI() {
		// configura��o da janela
		janelaPrincipal = new JFrame("AntiSpammers");
		janelaPrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		janelaPrincipal.setBounds(0, 0, (int) (screenSize.height * fator * 1.5625), (int) (screenSize.height * fator));
		System.out.println(screenSize.width * fator + "X" + screenSize.height * fator);
		painel = new JPanel();
		painel.setBorder(new EmptyBorder(5, 5, 5, 5));
		janelaPrincipal.setContentPane(painel);
		painel.setLayout(new MigLayout("", "[grow][grow][grow][][grow]", "[100px][100px][][][250px][100px][][]"));
		janelaPrincipal.setResizable(false);
		janelaPrincipal.setLocationRelativeTo(null);

		// MENU

		JMenuBar menuBar = new JMenuBar();
		JMenu ficheiros = new JMenu("Ficheiros");
		JMenu guardar = new JMenu("Guardar configura��o");
		JMenu historico = new JMenu("Hist�rico");

		ficheiros.setFont(f2);
		guardar.setFont(f2);
		historico.setFont(f2);

		menuBar.add(ficheiros);
		menuBar.add(guardar);
		menuBar.add(historico);

		JMenuItem rules_cf = new JMenuItem("rules.cf");
		JMenuItem ham_txt = new JMenuItem("ham.txt");
		JMenuItem spam_txt = new JMenuItem("spam.txt");

		rules_cf.setFont(f2);
		ham_txt.setFont(f2);
		spam_txt.setFont(f2);

		ficheiros.add(rules_cf);
		ficheiros.add(ham_txt);
		ficheiros.add(spam_txt);

		janelaPrincipal.setJMenuBar(menuBar);

		// Listener Menu

		rules_cf.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileDialog dialog = new FileDialog(janelaPrincipal, "Selecione o caminho para o ficheiro rules.cf");
				dialog.setMode(FileDialog.LOAD);
				dialog.setFile("*.cf");
				dialog.setVisible(true);
				String file = dialog.getDirectory() + dialog.getFile();
				if (file != null) {
					addinfo("Caminho para o ficheiro rules.cf definido");
					controller.setRulesPath(file);
					controller.readRules();
					setTable();
				}
			}
		});

		ham_txt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileDialog dialog = new FileDialog(janelaPrincipal, "Selecione o caminho para o ficheiro ham.txt");
				dialog.setMode(FileDialog.LOAD);
				dialog.setFile("*.txt");
				dialog.setVisible(true);
				String file = dialog.getDirectory() + dialog.getFile();
				if (file != null) {
					addinfo("Caminho para o ficheiro ham.txt definido");
					controller.setHamPath(file);
					controller.readHam();
				}

			}
		});

		spam_txt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileDialog dialog = new FileDialog(janelaPrincipal, "Selecione o caminho para o ficheiro spam.txt");
				dialog.setMode(FileDialog.LOAD);
				dialog.setFile("*.txt");
				dialog.setVisible(true);
				String file = dialog.getDirectory() + dialog.getFile();
				if (file != null) {
					addinfo("Caminho para o ficheiro spam.txt definido");
					controller.setSpamPath(file);
					controller.readSpam();
				}
			}
		});

		// Info e titulo da tabela

		info = new JTextField();
		info.setMinimumSize(new Dimension(400, 40));
		info.setFont(f.deriveFont(Font.BOLD));
		info.setForeground(Color.WHITE);
		painel.add(info, "cell 0 0 2 1,alignx center");
		info.setColumns(10);
		info.setEditable(false);
		info.setBackground(Color.BLACK);
		info.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel regras = new JLabel("Regras e pesos respetivos");
		painel.add(regras, "cell 0 1 2 1,alignx center");
		regras.setFont(f);

		// FP e FN

		JLabel falsospositivos = new JLabel("Falsos Positivos");
		painel.add(falsospositivos, "cell 2 0 1 2,alignx center");
		falsospositivos.setFont(f);

		JLabel falsosnegativos = new JLabel("Falsos Negativos");
		painel.add(falsosnegativos, "cell 4 0 1 2,alignx center");
		falsosnegativos.setFont(f);

		fp = new JTextField();
		painel.add(fp, "cell 2 2,alignx center,aligny center");
		fp.setColumns(5);
		fp.setMinimumSize(new Dimension(75, 75));
		fp.setFont(f3);
		fp.setEditable(false);
		fp.setBackground(Color.WHITE);
		fp.setHorizontalAlignment(SwingConstants.CENTER);
		

		fn = new JTextField();
		painel.add(fn, "cell 4 2,alignx center");
		fn.setColumns(5);
		fn.setMinimumSize(new Dimension(75, 75));
		fn.setFont(f3);
		fn.setEditable(false);
		fn.setBackground(Color.WHITE);
		fn.setHorizontalAlignment(SwingConstants.CENTER);

		// Tabela Regras e pesos associados

		colunas = new String[] { "Regras", "Peso" };
		table = new JTable();
		setTable();
		scroll = new JScrollPane(table);
		painel.add(scroll, "cell 0 2 2 5,grow");

		// Botao Algoritmo

		JButton algoritmo = new JButton("Algoritmo");
		algoritmo.setMinimumSize(new Dimension(140, 60));
		;
		algoritmo.setFont(f);
		painel.add(algoritmo, "cell 4 4,alignx center");

		// Bot�o Aleatorio

		JButton random = new JButton("Aleat�rio");
		random.setMinimumSize(new Dimension(140, 60));
		;
		random.setFont(f);
		random.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.pesosAleatorios();
				setTable();
				addinfo("Pesos aleat�rios gerados");
				botao = 1;
			}
		});

		painel.add(random, "cell 2 4,alignx center");

		// Botao Iniciar

		JButton iniciar = new JButton("Iniciar");
		iniciar.setMinimumSize(new Dimension(140, 60));
		;
		iniciar.setFont(f);
		iniciar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (controller.ficheirosDef()) {
					if(botao==1) {
						controller.pesosAleatorios();
						setTable();
					}
					fp.setText(String.valueOf(controller.calcularFP()));
					fn.setText(String.valueOf(controller.calcularFN()));
					classificar("Leisure");
				} else {
					addinfo("Verifique os caminhos dos ficheiros");
				}
			}

		});
		painel.add(iniciar, "cell 2 5 3 1,alignx center");

		janelaPrincipal.setVisible(true);

		addinfo("Bem Vindo");
	}

	// Janela INFO

	private void setTable() {
		table.setModel(new DefaultTableModel(controller.preencherTabela(), colunas));
		table.setCellSelectionEnabled(true);
		table.setBorder(new LineBorder(new Color(0, 0, 0)));
		table.getTableHeader().setFont(f2);
		table.getColumnModel().getColumn(0).setMinWidth(300);
		table.getTableHeader().setBackground(Color.WHITE);
		table.setRowHeight(25);
		table.setFont(f2);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer.setHorizontalAlignment(SwingConstants.CENTER);
		table.getColumnModel().getColumn(1).setCellRenderer(renderer);
		table.repaint();
		table.revalidate();
	}

	public void addinfo(String s) {
		new Thread(new Runnable() {

			public void run() {
				try {
					Thread.sleep(250);
					info.setText(s);
					if (s.equals("Bem Vindo")) {
						Thread.sleep(1500);
						info.setText("Defina os caminhos para os ficheiros");
					} else {
						Thread.sleep(4000);
						info.setText("");
					}

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	private void classificar(String tipo) {
		int total = controller.calcularFP() + controller.calcularFN();
		if (tipo.equals("Leisure")) {
			if (controller.calcularFP() < total * 0.20) {
				fp.setForeground(Color.GREEN);
				fn.setForeground(Color.GREEN);
			} else if (controller.calcularFP() < total * 0.4 && controller.calcularFP() > total * 0.2) {
				fp.setForeground(Color.BLUE);
				fn.setForeground(Color.BLUE);
			} else {
				fp.setForeground(Color.RED);
				fn.setForeground(Color.RED);
			}
		} else if (tipo.equals("Professional")) {
			if (controller.calcularFP() > total * 0.80) {
				fp.setForeground(Color.GREEN);
				fn.setForeground(Color.GREEN);
			} else if (controller.calcularFP() > total * 0.6 && controller.calcularFP() < total * 0.8) {
				fp.setForeground(Color.BLUE);
				fn.setForeground(Color.BLUE);
			} else {
				fp.setForeground(Color.RED);
				fn.setForeground(Color.RED);
			}
		} else if (tipo.equals("Leisure and Professional")) {
			if (controller.calcularFP() > total * 0.45 && controller.calcularFP() < total * 0.55) {
				fp.setForeground(Color.GREEN);
				fn.setForeground(Color.GREEN);
			} else if (controller.calcularFP() < total * 0.45 || controller.calcularFP() > total * 0.55) {
				fp.setForeground(Color.BLUE);
			}
		}

	}
}
