package com.ibm.uwv.disktool;


import java.io.*;
import java.util.Locale;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.*;

	public class StructuurFileChooser extends JPanel
	                             implements ActionListener {
	    static private final String newline = "\n";
	    JButton openButton, saveButton;
	    JTextArea log;
	    JFileChooser fc;
	    private String structuurFile;

	    public StructuurFileChooser() {
	        super(new BorderLayout());

	        log = new JTextArea(5,20);
	        log.setMargin(new Insets(5,5,5,5));
	        log.setEditable(false);
	        JScrollPane logScrollPane = new JScrollPane(log);

	        fc = new JFileChooser();
	        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
	        openButton = new JButton("Bestand",createImageIcon("images/Open16.gif"));
	        openButton.addActionListener(this);

	        JPanel buttonPanel = new JPanel(); //use FlowLayout
	        buttonPanel.add(openButton);
	        add(buttonPanel, BorderLayout.PAGE_START);
	    }

	    public void actionPerformed(ActionEvent e) {
	        if (e.getSource() == openButton) {
	            int returnVal = fc.showOpenDialog(StructuurFileChooser.this);

	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	                File file = fc.getSelectedFile();
	    	    	setStructuurFile(fc.getSelectedFile().toString());
	            } 
	            log.setCaretPosition(log.getDocument().getLength());

	        } 
	    }

	    /** Returns an ImageIcon, or null if the path was invalid. */
	    protected static ImageIcon createImageIcon(String path) {
	        java.net.URL imgURL = StructuurFileChooser.class.getResource(path);
	        if (imgURL != null) {
	            return new ImageIcon(imgURL);
	        } else {
	            System.err.println("Couldn't find file: " + path);
	            return null;
	        }
	    }

	    private static void createAndShowGUI() {
	        //Make sure we have nice window decorations.
	        JFrame.setDefaultLookAndFeelDecorated(true);
	        JDialog.setDefaultLookAndFeelDecorated(true);

	        //Create and set up the window.
	        JFrame frame = new JFrame("FileChooserDemo");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	        //Create and set up the content pane.
	        JComponent newContentPane = new StructuurFileChooser();
	        newContentPane.setOpaque(true); //content panes must be opaque
	        frame.setContentPane(newContentPane);

	        //Display the window.
	        frame.pack();
	        frame.setVisible(true);
	    }

	    public static void main(String[] args) {
	        //Schedule a job for the event-dispatching thread:
	        //creating and showing this application's GUI.
	        javax.swing.SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	                createAndShowGUI();
	            }
	        });
	    }

		public String getStructuurFile() {
			return structuurFile;
		}

		public void setStructuurFile(String structuurFile) {
			this.structuurFile = structuurFile;
		}


}
