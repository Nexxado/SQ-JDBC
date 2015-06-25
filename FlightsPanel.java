package ex5;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class FlightsPanel extends JPanel {
	
	private FlightsDatabase fd;
	private JPanel _toolbar;
	private JButton cmdCreateTable, cmdDropTable, cmdInsert, cmdAddTrigger, cmdCheapestFlight, cmdClear;
	private JLabel lblFno, lblSource, lblDest, lblCost, lblInsert, lblCheapestFlight;
	private JTextField txtFno, txtSource, txtDest, txtCost;
	private JTextArea console;
	private JScrollPane scroll; 
	
	/*******************************************************/
	/******************** Constructor **********************/ 
	/*******************************************************/
	public FlightsPanel(String login, String password, int option) {
		
		/*** Initialize JPanel Components ***/
		
		//Buttons
		cmdCreateTable = new JButton("Create Table");
		cmdDropTable = new JButton("Drop Table");
		cmdInsert = new JButton("Insert");
		cmdAddTrigger = new JButton("Add Trigger");
		cmdCheapestFlight = new JButton("Cheapest Flight");
		cmdClear = new JButton("Clear Console");
		
		//Labels
		lblFno = new JLabel("Fno: (Max 3 Digits)");
		lblSource = new JLabel("Source: (Max 30 Chars)");
		lblDest = new JLabel("Destination: (Max 30 Chars)");
		lblCost = new JLabel("Cost: (Max 4 Digits)");
		lblInsert = new JLabel("Insert Values");
		lblCheapestFlight = new JLabel("Cheapest Flight");
		
		//Text Fields
		txtFno = new JTextField(3);
		txtSource = new JTextField(30);
		txtDest = new JTextField(30);
		txtCost = new JTextField(4);
		
		
		//Set Button State
		cmdCreateTable.setEnabled(true);
		cmdDropTable.setEnabled(false);
		cmdInsert.setEnabled(false);
		cmdAddTrigger.setEnabled(false);
		cmdCheapestFlight.setEnabled(false);
		cmdClear.setEnabled(true);
		
		//Assign Button Listener
		ButtonListener buttonLis = new ButtonListener();
		cmdCreateTable.addActionListener(buttonLis);
		cmdDropTable.addActionListener(buttonLis);
		cmdInsert.addActionListener(buttonLis);
		cmdAddTrigger.addActionListener(buttonLis);
		cmdCheapestFlight.addActionListener(buttonLis);
		cmdClear.addActionListener(buttonLis);
		
		/*** Define Layout ***/
		//General Layout
		setLayout(new BorderLayout());
		setBackground(Color.white);
		_toolbar = new JPanel();
		_toolbar.setLayout(new GridLayout(2, 3, 10, 10));
		
		_toolbar.add(cmdInsert);
		_toolbar.add(cmdAddTrigger);
		_toolbar.add(cmdCheapestFlight);
		_toolbar.add(cmdCreateTable);
		_toolbar.add(cmdDropTable);
		_toolbar.add(cmdClear);
		
		
		//Text Area for Console output
		console = new JTextArea(5, 20);
		console.setEditable(false);
		console.setBackground(Color.WHITE);
		console.setLineWrap(true);
		console.setBounds(0, 0, getWidth(), getHeight()/2);
		
		//Set update policy to always scroll to bottom of console
		DefaultCaret caret = (DefaultCaret)console.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		//Add scroll bar
		scroll = new JScrollPane (console, 
				   JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		
		//Add Components to JPanel
		add(_toolbar, BorderLayout.SOUTH);
		add(scroll, BorderLayout.CENTER);
		
		//Redirect Output according to option
		if(option == JOptionPane.YES_OPTION) {
			PrintStream printStream = new PrintStream(new CustomOutputStream(console)); 
			System.setOut(printStream);
			System.setErr(printStream);
		}
		
		//Initialize Database
		fd = new FlightsDatabase(login, password);
		
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		setBackground(Color.WHITE);
		repaint();
	}
	
	
	/*******************************************************/
	/****************** Button Listener ********************/ 
	/*******************************************************/
	private class ButtonListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == cmdClear) {
				console.setText("");
				
			}
			else if(e.getSource() == cmdCreateTable) {
				fd.create();
				
				cmdCreateTable.setEnabled(false);
				cmdDropTable.setEnabled(true);
				cmdInsert.setEnabled(true);
				cmdAddTrigger.setEnabled(true);
				cmdCheapestFlight.setEnabled(true);
				
			}
			else if (e.getSource() == cmdDropTable) {
				fd.clean();
				
				cmdCreateTable.setEnabled(true);
				cmdDropTable.setEnabled(false);
				cmdInsert.setEnabled(false);
				cmdAddTrigger.setEnabled(false);
				cmdCheapestFlight.setEnabled(false);
				
			}
			else if (e.getSource() == cmdAddTrigger) {
				fd.addTrigger();
				
			}
			else if (e.getSource() == cmdInsert) {
				
				String source = "", dest = "";
				int fno = 0, cost = 0;
				
				while(true) {
					
					//Open Dialog to get Insert Values
					Object[] arr = {lblFno, txtFno, lblSource, txtSource, lblDest, txtDest, lblCost, txtCost};
					int option = JOptionPane.showConfirmDialog(null, arr, lblInsert.getText(), JOptionPane.OK_CANCEL_OPTION);
					
					if(option == JOptionPane.OK_OPTION) {
						//check Values legality
						if(txtFno.getText().trim().equals("") || txtCost.getText().trim().equals("") ||
								txtSource.getText().trim().equals("") || txtDest.getText().trim().equals("")) {
							
							JOptionPane.showMessageDialog(null, "some text fields are Empty! Please Input Data in all of them!",
									"ERROR", JOptionPane.ERROR_MESSAGE);
							continue;
						}
					}
					else
						return;
				
					
					source = txtSource.getText().trim();
					dest = txtDest.getText().trim();
					
					try {
						fno = Integer.parseInt(txtFno.getText().trim());
						cost = Integer.parseInt(txtCost.getText().trim());
							
					} catch (NumberFormatException err) {
						JOptionPane.showMessageDialog(null, "Text was inserted into a Number field! please insert a number!",
								"ERROR", JOptionPane.ERROR_MESSAGE);
						continue;
					}
					
					break;
				}	
				
				//Reset Text Fields
				txtFno.setText("");
				txtSource.setText("");
				txtDest.setText("");
				txtCost.setText("");
					
				fd.insert(fno, source, dest, cost);
			
				
			}
			else if (e.getSource() == cmdCheapestFlight) {
				
				while(true) {
					
					Object[] arr = {lblSource, txtSource, lblDest, txtDest};
					int option = JOptionPane.showConfirmDialog(null, arr, lblCheapestFlight.getText(), JOptionPane.OK_CANCEL_OPTION);
					
					
					if(option == JOptionPane.OK_OPTION) {
						//check Values legality
						if(txtSource.getText().trim().equals("") || txtDest.getText().trim().equals("")) {
							JOptionPane.showMessageDialog(null, "some text fields are Empty! Please Input Data in all of them!",
									"ERROR", JOptionPane.ERROR_MESSAGE);
							continue;
						}
					}
					else
						return;
					
					break;
				}	
				
					String source = txtSource.getText().trim();
					String dest = txtDest.getText().trim();
				
					//Reset Text Fields
					txtSource.setText("");
					txtDest.setText("");
					
					fd.CheapestFlight(source, dest);
			}
		}
			
			
	}
	
	/*******************************************************/
	/***************** CustomOutputStream ******************/ 
	/*******************************************************/
	public class CustomOutputStream extends OutputStream {
		private JTextArea output;
		
		public CustomOutputStream(JTextArea textArea) {
			output = textArea;
		}

		@Override
		public void write(int b) throws IOException {
			// redirects data to the text area
	        output.append(String.valueOf((char)b));
	        // scrolls the text area to the end of data
	        output.setCaretPosition(output.getDocument().getLength());
			
		}
		
	}
	
	
	

}
