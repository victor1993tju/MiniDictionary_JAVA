package project;
import java.io.*;
//import java.util.Arrays;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


public class GraphicSpellChecker implements ActionListener  {

   	private JFrame appFrame;
   	
   	private JTextField jInput;
   	private JButton jCheck, jClear, jLoad, jQuit;
   	private JTextArea jGuesses;
   	private JLabel jMsg;
   	
   	private JMenuBar menuBar;
   	private JMenu menuFile;
	private JMenuItem exitMenuItem;
	private JMenu menuList;
	private JMenuItem defaultMenuItem;
	private JMenu menuAdd;
	
	private ActionListener menuAddListener;
	
	private final JFileChooser fc = new JFileChooser();
	private SpellChecker sc;
   	
   	public GraphicSpellChecker() throws FileNotFoundException{
   		menuAddListener = new MenuAddListener();
   		appFrame = new JFrame("DiCo");
   		createAndPlaceComponents();
   		appFrame.setResizable(true);
   		appFrame.pack();
   		appFrame.setVisible(true);
   		appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   		sc = new SpellChecker();
   		loadDefault();
   	}
   	
   	public void createAndPlaceComponents() {
   		menuBar = new JMenuBar();
		menuFile = new JMenu("File");
		exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addActionListener(this);
		menuFile.add(exitMenuItem);
		menuBar.add(menuFile);
		menuList = new JMenu("Dictionaries");
		defaultMenuItem = new JMenuItem("Set as default");
		defaultMenuItem.addActionListener(this);
		menuList.add(defaultMenuItem);
		menuList.addSeparator();
		menuBar.add(menuList);
		menuAdd = new JMenu("Add");
		menuBar.add(menuAdd);
		
		jInput = new JTextField(15);
		jGuesses = new JTextArea(5,15);
		JScrollPane scrollPane = new JScrollPane(jGuesses);
		JPanel left = new JPanel(new BorderLayout());
		left.add(jInput,BorderLayout.NORTH);
		left.add(scrollPane,BorderLayout.SOUTH);
		
		jCheck = new JButton("Check");
		//jCheck.setPreferredSize(new Dimension(25,10));
		jCheck.addActionListener(this);
		jClear = new JButton("Clear");
		jClear.addActionListener(this);
		jLoad = new JButton("Load");
		jLoad.addActionListener(this);
		jQuit = new JButton("Exit");
		jQuit.addActionListener(this);
		
		JPanel right = new JPanel();
		right.setLayout(new GridLayout(4,1));
		right.add(jCheck);
		right.add(jLoad);
		right.add(jClear);
		right.add(jQuit);
		
		JPanel top = new JPanel(new BorderLayout());
		top.add(left,BorderLayout.WEST);
		top.add(right,BorderLayout.EAST);
		
		jMsg = new JLabel("Enter a word to check");
		JPanel main = new JPanel();
		main.setLayout(new BoxLayout(main,BoxLayout.PAGE_AXIS));
		main.add(top);
		main.add(jMsg);
		
		appFrame.setJMenuBar(menuBar);
		appFrame.setContentPane(main);
   	}
   	
   	public void actionPerformed (ActionEvent e) {
   		if ( e.getSource() == jCheck ) {
   			doCheck();
   		} else if ( e.getSource() == jClear) {
   			doClear();
   		} else if ( e.getSource() == jLoad ) {
   			doLoad();
   		} else if ( e.getSource() == jQuit ) {
   			doQuit();
   		} else if ( e.getSource() == exitMenuItem ) {
   			doQuit();
   		} else if ( e.getSource() == defaultMenuItem ) {
   			doDefault();
   		} else {
   			try {
				doUnload(e);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
   		}
   	}
   	
   	private void doCheck() {
   		print("doCheck()");
   		String word = jInput.getText();
   		jGuesses.setText("");
		if ( sc.contains(word) )
			jMsg.setText(word + " is a correct word");
		else {
			jMsg.setText(word + " is not a correct word");
			String output = "Maybe you meant:";
			String[] guesses = sc.guess(word);
			for ( int i = 0; i < guesses.length; i++ )
				output += "\n" + guesses[i];
			jGuesses.setText(output);
		}  		
   	}
 
   	private void doClear() {
   		print("doClear()");
   		jInput.setText("");
   		jGuesses.setText("");
   		jMsg.setText(" ");
   	}
   	
   	private void doLoad() {
   		print("doLoad()");
  		int returnVal = fc.showOpenDialog(appFrame);
   		String Dname = null;
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            Dname = fc.getSelectedFile().getName();
           // String name=Dname.substring(0,Dname.indexOf('.'));//to cut ".txt"
            try {
            	sc.load(Dname);
            }
            catch ( Exception e ) {
            	jMsg.setText("Problem loading " + Dname);
            	return;
            }
    		JMenuItem menuItem = new JMenuItem(Dname);
    		menuItem.addActionListener(this);
    		menuList.add(menuItem);
			menuItem = new JMenuItem(Dname);
			menuItem.addActionListener(menuAddListener);
			menuAdd.add(menuItem);
        }
   	}
   	
   	private void doQuit() {
   		print("doQuit()");
   		System.exit(0);
   	}
   	
   	private void doDefault() {
   		print("doDefault()");
    		try {
			sc.rewrite();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   	}
   	
   	private void doUnload(ActionEvent e) throws FileNotFoundException {
   		print("doUnload()");
   		if ( JOptionPane.showConfirmDialog(appFrame,"Remove " + e.getActionCommand() + "?","Unloading a dictionary",JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION ) {
   			JMenuItem menuItem = (JMenuItem) e.getSource();   			
   			menuList.remove(menuItem);
   			sc.unload(e.getActionCommand());
   			sc.rewrite();/////////////////////////////////////

		}
   	}
  	
   	private void loadDefault()throws FileNotFoundException{//private before
		print("loadDefaut()");	
		String[]dlist=sc.defaultname;	
		//System.out.print(Arrays.toString(dlist));	
		System.out.print(dlist.toString());	
		for(int i=0;i<dlist.length;i++){
		String name =dlist[i];	
		JMenuItem menuItem = new JMenuItem(name);
		menuItem.addActionListener(this);
		menuList.add(menuItem);
		menuAdd.add(menuItem);
		
		}
	}
 	
	private class MenuAddListener implements ActionListener {
		
		public void actionPerformed (ActionEvent e) {
			sc.addWord(jInput.getText(),e.getActionCommand());
		}
	}
   	
	public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	try {
					new GraphicSpellChecker();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
	}
	
	private void print(String call) {
		System.out.println("Calling " + call + " from GraphicSpellChecker");
	}	
}
