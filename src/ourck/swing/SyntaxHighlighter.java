package ourck.swing;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.undo.CannotUndoException;

import ourck.tokens.manager.TokenHandler;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import java.awt.Color;
import javax.swing.UIManager;
import java.awt.Font;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * SyntaxHighlighter主窗体。
 * @author ourck
 */
@SuppressWarnings("serial")
public class SyntaxHighlighter extends JFrame {

	private JPanel contentPane;
	
	/**
	 * boolean标志，制定了当前文本框处于HTML显示模式还是普通文本模式
	 */
	private boolean isHTMLMode = false;
	
	/**
	 * 指示Ctrl键是否被按压。
	 */
	private boolean ctrlFlag = false;
	
	/**
	 * 指示Shift键是否被按压。
	 */
	private boolean shiftFlag = false;
	
	/**
	 * 撤销使用到的管理器
	 */
	private OnUndoHandler undoMgr = new OnUndoHandler();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SyntaxHighlighter frame = new SyntaxHighlighter();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SyntaxHighlighter() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 626, 708);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBackground(new Color(211, 215, 207));
		scrollPane.setForeground(new Color(211, 215, 207));
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		JEditorPane mainCodeArea = new JEditorPane();
		mainCodeArea.setFont(new Font("文泉驿等宽微米黑", Font.PLAIN, 14));
		mainCodeArea.setForeground(new Color(211, 215, 207));
		mainCodeArea.setBackground(Color.DARK_GRAY);
		scrollPane.setViewportView(mainCodeArea);
		//mainCodeArea.getDocument().addDocumentListener(new DocOnChangeHandler());
		mainCodeArea.getDocument().addUndoableEditListener(undoMgr);
		mainCodeArea.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_CONTROL) ctrlFlag = false;
				if(e.getKeyCode() == KeyEvent.VK_SHIFT) shiftFlag = false;
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_CONTROL) { ctrlFlag = true; System.out.println("ctrl!"); }
				if(e.getKeyCode() == KeyEvent.VK_SHIFT) { shiftFlag = true; System.out.println("shift!"); }
				if(ctrlFlag && e.getKeyCode() == KeyEvent.VK_Z) {
					try { undoMgr.undo(); }
					catch(CannotUndoException ex) {}
				}
				if(ctrlFlag && shiftFlag && e.getKeyCode() == KeyEvent.VK_Z) {
					try {undoMgr.redo();}
					catch(CannotUndoException ex) {ex.printStackTrace();}
				}
			}
		});
		
		JButton btnExecute = new JButton("Execute");
		btnExecute.setForeground(UIManager.getColor("Button.light"));
		btnExecute.setBackground(Color.GRAY);
		btnExecute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// If the code area is HTML mode, reset it.
				if(isHTMLMode) {
					mainCodeArea.setContentType("text/plain");
					mainCodeArea.setText("");
					isHTMLMode = false;
					btnExecute.setText("Execute");
					return;
				}
				
				// Get code area's text.
				String rawCode = mainCodeArea.getText();
				StringBuilder stb = new StringBuilder();
				stb.append(rawCode);
				
				// Execute highlighting.
				TokenHandler handler = TokenHandler.getHandler();
				handler.analyze(stb);
				
				// Show HTML text...
				mainCodeArea.setContentType("text/html");
				isHTMLMode = true;
				mainCodeArea.setText(stb.toString());
				btnExecute.setText("Reset");
				
				// ...and select a path...
				JFileChooser chooser = new JFileChooser("选择HTML文件保存路径...");
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY); // Accept file-only choice.
				chooser.showDialog(new JLabel(), "保存");
				File target = chooser.getSelectedFile();
				
				// ...and save HTML text as file.
				BufferedWriter writer = null;
				try {
					if(target == null) return;
					writer = new BufferedWriter(
							new FileWriter(target.getAbsoluteFile()));
					writer.write(mainCodeArea.getText());
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try { if(writer != null) writer.close(); }
					catch (IOException e) { e.printStackTrace(); }
				}
			}
		});
		contentPane.add(btnExecute, BorderLayout.SOUTH);
	}
} 
