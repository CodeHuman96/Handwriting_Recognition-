
import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;


public class RecogChar extends JFrame
{
 	static final int DOWNSAMPLE_WIDTH = 6;
  	static final int DOWNSAMPLE_HEIGHT = 8;
  	Vector sampleList = new Vector();
  	DrawLetters entry;
  	Sample sample;
  	Kalgo net;
  	private UIManager.LookAndFeelInfo looks[];

  	public static final String HANDWRITING[] = {
  	  "1:001100011100110100100100000100000100000100111111",
  	  "2:111100100100100100000100000100001100011000111111",
  	  "3:011110000100000100111000011100000100000101111110",
  	  "4:001000011000010000110100100100100101111111000100",
  	  "5:111111100000100000111111100001000001000001111111",
  	  "6:011111110000100000111110110011100001110001011111",
  	  "7:111111000001000011000010000110000100001100001000",
  	  "8:111111100001110011011110011110110011100001111111",
  	  "9:111111100001100001111111000001000001100001111111",
      "A:001100001100011110011110010010010011100001100001",
      "B:111110100011100011101110111111100001100011111110",
      "C:001111011000110000100000100000100000100000111111",
      "D:111110110011010001010001010001010001110011111110",
      "D:111111100001100001100001100001100001100010111100",
      "E:111111100000100000111111100000100000100000111111",
      "F:111111100000100000100000111110100000100000100000",
      "G:011110110000100000111111100101100101100101111101",
      "H:100001100001100001111111100001100001100001100001",
      "I:111111000100000100000100000100000100000100111111",
      "J:111111000100000100000100000100000100101100111000",
      "K:100011100110111100111100100010100011100001100001",
      "L:100000100000100000100000100000100000100000111111",
      "M:110011111111101101100101100001100001100001100001",
      "N:100001110001111001101101100101100111100011100001",
      "O:011110110011100001100001100011100011110010011110",
      "P:111111100001100001100111111100100000100000100000",
      "Q:011110110011100001100001100001100001111111111111",
      "R:111111100001100011100110111111100001100001100001",
      "S:011111110000100000110000011111000001000001111111",
      "T:111111000100000100000100000100000100000100000100",
      "U:100001100001100001100001100001100001100001111111",
      "V:100001110001010011010010011010001110001100000100",
      "W:100001100001110001110001010101011111011011011011",
      "X:100001110011010110001100001100010110110011100001",
      "Y:100001110010011110001000001000001000001000001000",
      "Y:100001110010011110001100001100001000010000100000",
      "Z:111111000011000110001100001000011000110000111111"



      };

  	public RecogChar(){
  		super("HandWritten Script Recognition Mini-Project");
  	}
	String messag;
  	public void init()
  	{
  		looks = UIManager.getInstalledLookAndFeels();

  		changeTheLookAndFeel( 2 );
  		preload();
    	updateList();

    	setLayout(new GridLayout(2, 1));

    	JPanel topPanel = new JPanel();
    	JPanel bottomPanel = new JPanel();

    	JPanel topButtonPanel = new JPanel();
    	JPanel bottomButtonPanel = new JPanel();

    	topButtonPanel.setLayout(new GridLayout(3, 1));

    	topButtonPanel.add(recognize = new JButton("Recognize"), BorderLayout.SOUTH);
    	topButtonPanel.add(clear = new JButton("Clear"));

    	JPanel addPanel = new JPanel();
    	addPanel.setLayout(new GridLayout(1, 2));
    	addPanel.add(add = new JButton("Add:"));
    	addPanel.add(letterToAdd);
    	topButtonPanel.add(addPanel);
    	letterToAdd.setText("");
        bottomButtonPanel.setLayout(new GridLayout(3,1));
    	bottomButtonPanel.add(del = new JButton("Delete"),BorderLayout.NORTH);
    	bottomButtonPanel.add(delAll = new JButton("Delete All"),BorderLayout.SOUTH);
    	bottomButtonPanel.add(train = new JButton("Train"),BorderLayout.SOUTH);

    	entry = new DrawLetters();
    	topPanel.setLayout(new BorderLayout());
    	topPanel.add(message = new JLabel("Draw a letter, click Recognize"),BorderLayout.NORTH);
    	topPanel.add(entry, BorderLayout.CENTER);
    	topPanel.add(topButtonPanel, BorderLayout.EAST);
		message.setForeground(Color.BLUE);

		bottomPanel.setLayout(new BorderLayout());
    	bottomPanel.add(message1=new JLabel("***************************************Database**********************************************************************************"), BorderLayout.NORTH);
		message1.setForeground(Color.BLUE);

    	JPanel bottomContent = new JPanel();
    	bottomContent.setLayout(new GridLayout(1, 2));
    	bottomPanel.add(bottomContent, BorderLayout.CENTER);
    	bottomPanel.add(bottomButtonPanel, BorderLayout.WEST);

    	JPanel lettersPanel = new JPanel();
    	lettersPanel.setLayout(new BorderLayout());
    	lettersPanel.add(letters, BorderLayout.CENTER);

    	JPanel downSamplePanel = new JPanel();
    	downSamplePanel.setLayout(new BorderLayout());
    	sample = new Sample(DOWNSAMPLE_WIDTH, DOWNSAMPLE_HEIGHT);
    	entry.setSample(sample);
    	downSamplePanel.add(sample, BorderLayout.CENTER);


    	JPanel panel1=new JPanel();
    	panel1.setLayout(new GridLayout(1,2));
    	panel1.add(bottomButtonPanel);
    	panel1.add(lettersPanel);

    	bottomContent.add(panel1);
    	bottomContent.add(downSamplePanel);

    	add(topPanel);
    	add(bottomPanel);

        Font dialogFont = new Font("TimesNewRoman", Font.BOLD, 12);
    	clear.setFont(dialogFont);
    	add.setFont(dialogFont);
    	del.setFont(dialogFont);
    	delAll.setFont(dialogFont);
    	recognize.setFont(dialogFont);
    	train.setFont(dialogFont);

    	SymAction lSymAction = new SymAction();
    	clear.addActionListener(lSymAction);
    	add.addActionListener(lSymAction);
    	del.addActionListener(lSymAction);
    	delAll.addActionListener(lSymAction);
    	SymListSelection lSymListSelection = new SymListSelection();
    	letters.addItemListener(lSymListSelection);

    	train.addActionListener(lSymAction);
    	recognize.addActionListener(lSymAction);

    	entry.requestFocus();
    	resize(1270,680);
  	}
  	JButton add = new JButton();
  	JButton clear = new JButton();
  	JButton recognize = new JButton();

  	JScrollPane scrollPane1 = new JScrollPane();
  	java.awt.List letters = new java.awt.List();
  	JButton del = new JButton();
  	JButton delAll = new JButton();
  	JButton train = new JButton();

  	JLabel message = new JLabel();
	JLabel message1 = new JLabel();

  	JTextField letterToAdd = new JTextField("", 1);

  	class SymAction implements java.awt.event.ActionListener
  	{
		public void actionPerformed(java.awt.event.ActionEvent event)
  	  	{
    		Object object = event.getSource();
      		if (object == clear)
      		  	clear_actionPerformed(event);
      		else if (object == add)
      			add_actionPerformed(event);
      		else if (object == del)
        		del_actionPerformed(event);
      		else if (object == delAll)
        		deleteAll_actionPerformed(event);
      		else if (object == train)
        		train_actionPerformed(event);
      		else if (object == recognize)
        		recognize_actionPerformed(event);
    	}
  	}

  	void clear_actionPerformed(java.awt.event.ActionEvent event)
  	{
  	  	entry.clear();
  	  	sample.getData().clear();
    	sample.repaint();
  	}

  	void deleteAll_actionPerformed(java.awt.event.ActionEvent event)
  	{
  	  	sampleList.removeAllElements();
  	  	net = null;
    	updateList();
    	entry.clear();
    	sample.getData().clear();
    	sample.repaint();
  	}

  	void add_actionPerformed(java.awt.event.ActionEvent event)
  	{
  		int i;
    	String letter = letterToAdd.getText().trim();
    	if (letter.length() > 1 || letter.length()==0)
    	{
			messag="You should enter a single character";
			javax.swing.JOptionPane.showMessageDialog(null,messag,"Warning",javax.swing.JOptionPane.WARNING_MESSAGE );
      		//message.setText("Enter within 1 letter");
      		return;
    	}

    	if (letter.length() < 1)
    	{
			messag="Enter a single character";
			javax.swing.JOptionPane.showMessageDialog(null,messag,"Warning",javax.swing.JOptionPane.WARNING_MESSAGE);
      		return;
    	}

    	entry.downSample();
    	SampleData sampleData = (SampleData) sample.getData().clone();
    	sampleData.setLetter(letter.charAt(0));

    	for (i = 0; i < sampleList.size(); i++)
    	{
      		SampleData str = (SampleData) sampleList.elementAt(i);
      		if (str.equals(sampleData))
      		{
				messag="Letter already defined, delete it first!";
				javax.swing.JOptionPane.showMessageDialog(null,messag,"Warning",javax.swing.JOptionPane.WARNING_MESSAGE);
	        	return;
      		}

      		if (str.compareTo(sampleData) > 0)
      		{
        		sampleList.insertElementAt(sampleData, i);
        		updateList();
        		return;
      		}
    	}
    	sampleList.insertElementAt(sampleData, sampleList.size());
    	updateList();
    	letters.select(i);
    	entry.clear();
    	sample.repaint();

  	}

  	void del_actionPerformed(java.awt.event.ActionEvent event)
  	{
    	int i = letters.getSelectedIndex();

    	if (i == -1)
    	{
			messag="Please select a letter to delete.";
			javax.swing.JOptionPane.showMessageDialog(null,messag,"Warning",javax.swing.JOptionPane.WARNING_MESSAGE);

      		return;
    	}

    	sampleList.removeElementAt(i);
    	updateList();
  	}

  	class SymListSelection implements ItemListener
  	{
    	public void itemStateChanged(ItemEvent event)
    	{
      		Object object = event.getSource();
      		if (object == letters)
        		letters_valueChanged(event);

    	}
  	}

  	void letters_valueChanged(ItemEvent event)
  	{
    	if (letters.getSelectedIndex() == -1)
      		return;
    	SampleData selected = (SampleData) sampleList.elementAt(letters.getSelectedIndex());
    	sample.setData((SampleData) selected.clone());
    	sample.repaint();
    	entry.clear();
  	}

  	void train_actionPerformed(java.awt.event.ActionEvent event)
  	{
		try
    	{
      		int in = RecogChar.DOWNSAMPLE_HEIGHT * RecogChar.DOWNSAMPLE_WIDTH;
      		int out = sampleList.size();

      		TrainingSet set = new TrainingSet(in, out);
      		set.setTrainingSetCount(sampleList.size());

      		for (int t = 0; t < sampleList.size(); t++)
      		{
        		int idx = 0;
        		SampleData ds = (SampleData) sampleList.elementAt(t);
        		for (int y = 0; y < ds.getHeight(); y++)
        		{
          			for (int x = 0; x < ds.getWidth(); x++)
          			{
            			set.setInput(t, idx++, ds.getData(x, y) ? .5 : -.5);
          			}
        		}
      		}

      		net = new Kalgo(in, out, this);
      		net.setTrainingSet(set);
      		net.learn();
      		this.clear_actionPerformed(null);
      		messag="Trained...\n Ready to recognize.";
      		javax.swing.JOptionPane.showMessageDialog(null,messag,"output",javax.swing.JOptionPane.INFORMATION_MESSAGE);
      		//message.setText("Trained. Ready to recognize.");
    	}
    	catch (Exception e)
    	{
     		//messag="Exception:";
			//javax.swing.JOptionPane.showMessageDialog(null,messag,"output",javax.swing.JOptionPane.INFORMATION_MESSAGE);
      		message.setText("Exception:" + e.getMessage());
    	}

  	}

  	void recognize_actionPerformed(java.awt.event.ActionEvent event)
  	{
    	if (net == null)
    	{
			messag="I need to be trained first!";
			javax.swing.JOptionPane.showMessageDialog(null,messag,"output",javax.swing.JOptionPane.WARNING_MESSAGE);
      		return;
    	}
    	entry.downSample();

    	double input[] = new double[8 * 6];
    	int idx = 0;
    	SampleData ds = sample.getData();
    	for (int y = 0; y < ds.getHeight(); y++)
    	{
      		for (int x = 0; x < ds.getWidth(); x++)
      		{
        		input[idx++] = ds.getData(x, y) ? .5 : -.5;
      		}
    	}

    	double normfac[] = new double[1];

    	int best = net.winner(input, normfac);
    	char map[] = mapNeurons();
    	String messag;
    	if(map[best]>=97 && map[best]<=122)
		{
			messag="English Small letter : "+map[best];
			javax.swing.JOptionPane.showMessageDialog(null,messag,"output",javax.swing.JOptionPane.INFORMATION_MESSAGE);
		}
		else if(map[best]>=33 && map[best]<=47)
		{
			messag="Operands : "+map[best];
			javax.swing.JOptionPane.showMessageDialog(null,messag,"output",javax.swing.JOptionPane.INFORMATION_MESSAGE);
		}
		else if(map[best]>=65 && map[best]<=90)
		{
			messag="English Capital letter : "+map[best];
			javax.swing.JOptionPane.showMessageDialog(null,messag,"output",javax.swing.JOptionPane.INFORMATION_MESSAGE);
		}
		else if(map[best]>=48 && map[best]<=57)
		{
			messag="English Numerical : "+map[best];
			javax.swing.JOptionPane.showMessageDialog(null,messag,"output",javax.swing.JOptionPane.INFORMATION_MESSAGE);
		}
		else
		{
			messag="Special Character : "+map[best];
			javax.swing.JOptionPane.showMessageDialog(null,messag,"output",javax.swing.JOptionPane.INFORMATION_MESSAGE);
		}


		clear_actionPerformed(null);

  	}

  	char[] mapNeurons()
  	{
	    char map[] = new char[sampleList.size()];
    	double normfac[] = new double[1];

    	for (int i = 0; i < map.length; i++)
      		map[i] = '?';
    	for (int i = 0; i < sampleList.size(); i++)
    	{
      		double input[] = new double[8 * 6];
      		int idx = 0;
      		SampleData ds = (SampleData) sampleList.elementAt(i);
      		for (int y = 0; y < ds.getHeight(); y++)
      		{
        		for (int x = 0; x < ds.getWidth(); x++)
        		{
          			input[idx++] = ds.getData(x, y) ? .5 : -.5;
        		}
      		}

      		int best = net.winner(input, normfac);
      		map[best] = ds.getLetter();
    	}
    	return map;
  	}

  	public void updateList()
  	{//System.out.println("in updatelist");
    	letters.removeAll();
    	for (int i = 0; i < sampleList.size(); i++)
    	{
      		SampleData sample = (SampleData) sampleList.elementAt(i);
      		//System.out.println("sample="+sample);
      		letters.add("" + sample.letter);
    	}

  	}

  	public void preload()
  	{
  		//System.out.println("in preload");
    	int index = 0;
    	//System.out.println("RecogChar.HANDWRITING.length="+RecogChar.HANDWRITING.length);
    	for (int i = 0; i < RecogChar.HANDWRITING.length; i++)
    	{
      		String line = HANDWRITING[i].trim();
      		//System.out.println("line="+line);
      		SampleData ds = new SampleData(line.charAt(0),RecogChar.DOWNSAMPLE_WIDTH, RecogChar.DOWNSAMPLE_HEIGHT);
      		sampleList.insertElementAt(ds, index++);
      		int idx = 2;
      		for (int y = 0; y < ds.getHeight(); y++)
      		{
        		for (int x = 0; x < ds.getWidth(); x++)
        		{
        	  		ds.setData(x, y, line.charAt(idx++) == '1');
        		}
      		}
    	}
    	train_actionPerformed(null);
  	}

  	private void changeTheLookAndFeel( int value )
  	      {
  			//System.out.println("in changelook and feel");
  	         try
  	         {

  	            UIManager.setLookAndFeel( looks[ value ].getClassName() );
  	        //  System.out.println("looks[2]="+looks[ value ].getClassName());

  	            SwingUtilities.updateComponentTreeUI( this );
  	         }
  	         catch ( Exception exception )
  	         {
  	            exception.printStackTrace();
  	         }
  	      }
  	public static void main(String args[]){
  		RecogChar recogChar = new RecogChar();
  		recogChar.setVisible(true);
  		recogChar.init();
  		recogChar.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  		recogChar.setSize(820, 580);
  	}
}
