package com.ibm.uwv.disktool;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Vector;

import javax.naming.spi.DirectoryManager;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;

public class Main extends JPanel implements ActionListener {
    private static String SELECT_COMMAND		= "select";
    private static String SELECT_FILE_COMMAND	= "select_file";
    private static String ADD_COMMAND 			= "add";
    private static String REMOVE_COMMAND 		= "remove";
    private static String STRUCT_COMMAND 		= "clear";
    JTextField nodeField						= null;
    JTextField directoryField					= null;
    JTextField fileField						= null;
    DirectoryChooser fcd						= null;
    StructuurFileChooser sc						= null;
    private String	tmpString					= null;
    
    private DynamicTree treePanel;

    public Main() {
        super(new BorderLayout());
        
        //Create the components.
        treePanel = new DynamicTree();
        //populateTree(treePanel);

        JButton selectButton = new JButton("Selecteer");
        selectButton.setActionCommand(SELECT_COMMAND);
        selectButton.addActionListener(this);

        JButton selectFileButton = new JButton("Selecteer");
        selectFileButton.setActionCommand(SELECT_FILE_COMMAND);
        selectFileButton.addActionListener(this);
        
        JButton addButton = new JButton("Toevoegen");
        addButton.setActionCommand(ADD_COMMAND);
        addButton.addActionListener(this);
        
        JButton removeButton = new JButton("Verwijderen");
        removeButton.setActionCommand(REMOVE_COMMAND);
        removeButton.addActionListener(this);
        
        JButton structButton = new JButton("Maak structuur");
        structButton.setActionCommand(STRUCT_COMMAND);
        structButton.addActionListener(this);


        //Lay everything out.
        treePanel.setPreferredSize(new Dimension(300, 150));
        
        nodeField 		= new JTextField(20);
        directoryField 	= new JTextField(20);
        fileField 		= new JTextField(20);
        fileField.setEditable(false);
        directoryField.setEditable(false);
        
        fcd = new DirectoryChooser();
        sc	= new StructuurFileChooser();
        
        add(treePanel, BorderLayout.CENTER);

        JPanel panel 		= new JPanel(new GridLayout(0,1));
        JPanel tmpPanel		= new JPanel();  
        JPanel tmpPanel2	= new JPanel();  
        JPanel tmpPanel3	= new JPanel();  
        JPanel tmpPanel4	= new JPanel();  
        JPanel panelNoord 	= new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.5;
        c.gridx = 0;
        c.gridy = 0;
        panelNoord.add(fcd,c);
        c.gridx = 1;
        c.gridy = 0;
        panelNoord.add(selectButton,c);
        c.gridx = 2;
        c.gridy = 0;
        panelNoord.add(tmpPanel,c);
        c.gridx = 3;
        c.gridy = 0;
        panelNoord.add(directoryField,c);
        c.gridx = 4;
        c.gridy = 0;
        panelNoord.add(tmpPanel2,c);

        c.gridx = 0;
        c.gridy = 1;
        panelNoord.add(sc,c);
        c.gridx = 1;
        c.gridy = 1;
        panelNoord.add(selectFileButton,c);
        c.gridx = 2;
        c.gridy = 1;
        panelNoord.add(tmpPanel3,c);
        c.gridx = 3;
        c.gridy = 1;
        panelNoord.add(fileField,c);
        c.gridx = 4;
        c.gridy = 1;
        panelNoord.add(tmpPanel4,c);
       
        add(panelNoord, BorderLayout.NORTH);
        
        
        panel.add(removeButton);
        add(panel, BorderLayout.LINE_END);
        JPanel panelSouth = new JPanel();
        panelSouth.add(nodeField);
        panelSouth.add(addButton);
        panelSouth.add(structButton);
        add(panelSouth, BorderLayout.SOUTH);
        
    }

    public void populateTree(DynamicTree treePanel, String fileParam) {
		try{
	        FileInputStream fstream = new FileInputStream(fileParam);
	        DataInputStream in = new DataInputStream(fstream);
	        String tmpString	= null;
			DefaultMutableTreeNode p1 = null;
			DefaultMutableTreeNode p2 = null;
			while (in.available() !=0){
				tmpString = in.readLine();
				tmpString = tmpString.replace('\\','/');
				String tmpStrings[] = tmpString.split("/");
				
				for (int i=0; i< tmpStrings.length; i++){
					String tmpStringNieuw = tmpStrings[i];

					if ((tmpStringNieuw.equals(tmpString)) && (i==0) )
				        p1 = treePanel.addObject(null, tmpStringNieuw);						
					
					if ((!tmpStringNieuw.equals(tmpString)) && (i==1) )
						p2 = treePanel.addObject(p1,tmpStringNieuw);

					if ((!tmpStringNieuw.equals(tmpString)) && (i==2) )
						treePanel.addObject(p2,tmpStringNieuw);						
					
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
    }
    
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        
        if (ADD_COMMAND.equals(command)) {
            treePanel.addObject(nodeField.getText());
        } else if (REMOVE_COMMAND.equals(command)) {
            treePanel.removeCurrentNode();
        } else if (SELECT_COMMAND.equals(command)) {
        	directoryField.setText(fcd.getSelectedDirectory());
        	treePanel.tree.setBackground(Color.YELLOW);
        } else if (SELECT_FILE_COMMAND.equals(command)) {
            populateTree(treePanel,sc.getStructuurFile());
        }else if (STRUCT_COMMAND.equals(command)) {
        	if(fcd.getSelectedDirectory()!=null)
        		if(new File(fcd.getSelectedDirectory()).isDirectory())
        				generateDirectoryStructure();
        		else
            		nodeField.setText("U heeft geen juiste directory gekozen");
        	else
        		nodeField.setText("U heeft geen juiste directory gekozen");
        		
        }
        
    }

    private void generateDirectoryStructure(){
    	TreeModel tm =  treePanel.tree.getModel();
        if (tm != null) {
            Object root = tm.getRoot();
            walk1(tm,root);    
            this.setTmpString(fcd.getSelectedDirectory());
            walk2(tm,root);    
        }
        else{
           System.out.println("Tree is empty.");
        }
    }
    
    protected void walk1(TreeModel model, Object o){
        int  	cc;
        File 	aFile 			= null; 
        cc 						= model.getChildCount(o);
        
        for( int i=0; i < cc; i++) {
          Object child = model.getChild(o, i );
          System.out.println(child.toString());

          this.setTmpString(fcd.getSelectedDirectory()+"\\"+child.toString());
		  aFile = new File(this.getTmpString());
		  aFile.mkdir();            
        }       
    }

    protected void walk2(TreeModel model, Object o){
        int  	cc;
        File 	aFile 			= null; 
        cc 						= model.getChildCount(o);

        for( int i=0; i < cc; i++) {
	        Object child = model.getChild(o, i );
	        if ((model.isLeaf(child))	&& 
	        	(model.getChildCount(child)==0)){
	        		System.out.println(i+" "+this.getTmpString()+" "+child.toString());
	        		if(	(!new File(this.getTmpString()+"\\"+child.toString()).exists())	&&
	        			(!new File(fcd.getSelectedDirectory()+"\\"+child.toString()).exists())){
	        				aFile = new File(this.getTmpString()+"\\"+child.toString());
	        				aFile.mkdir();
	        		}
	        } else {
	    	  // Met subdirectories
	        	this.setTmpString(fcd.getSelectedDirectory()+"\\"+child.toString());
	        	if(!new File(this.getTmpString()).exists()){
	    		  aFile = new File(this.getTmpString());
	    		  aFile.mkdir();            
	        	}
	  		    walk2(model,child );
	        }
	    }
    }
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
        JFrame frame = new JFrame("Leeuwerink DiskTool");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        Main newContentPane = new Main();
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

	public String getTmpString() {
		return tmpString;
	}

	public void setTmpString(String tmpString) {
		this.tmpString = tmpString;
	}
}

