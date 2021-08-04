import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/*
 * Thomas Pinkava's Mayan Date GUI
 * For use with class MayanDate
 * Written July of 2016
 */

public class MayanDateCalculator implements ActionListener {
  
  
  //The maximum number of lines the console will contain. Any additional lines added will result in the deletion of the least recent line.
  private final int C_MAX = 500;
  
  //The Mayan Date
  private MayanDate da;
  
  //Declare each element present in the GUI
  private JFrame frame;
  private JTextArea console;
  private JTextField l1,l2,l3,l4,l5,gd,gm,gy,mdcin, recontzold, reconhaabd, reconn;
  private JComboBox gregBC, recontzolm, reconhaabm;
  private JScrollPane consolepane;
  private JButton fromlc, fromgd, frommdc, minus, plus, recon, todaybutton;
  private JPanel buttonsnorth, buttonssouth, superbuttons, inputs, longCountPanel, gregDatePanel, lcSuper, gregSuper, mdcSuper, reconPanel;
  private JLabel longCountLabel,gregDateLabel, mdcLabel, padding;
  
  
  public static void main(String[] args){
    //On a static call, instantiate myself (?) and run as a window
    MayanDateCalculator layout = new MayanDateCalculator();
    layout.start();
  }
  
  public void start(){
    //Set the frame
    frame = new JFrame("Mayan Date Suite");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setResizable(false);
    
    //Acquire the Content Pane 'content'
    Container content = frame.getContentPane();
    
    //Set the Layout
    content.setLayout(new BorderLayout(5,3));
    
    //Set up the Inputs panel
    inputs = new JPanel();
    inputs.setLayout(new GridLayout(0,1));
    
    //Set up the Long Count Input
    lcSuper = new JPanel();
    lcSuper.setLayout(new BoxLayout(lcSuper,BoxLayout.Y_AXIS));
    longCountLabel = new JLabel("Long Count:");
    lcSuper.add(longCountLabel);
    longCountPanel = new JPanel();
    longCountPanel.setLayout(new BoxLayout(longCountPanel,BoxLayout.X_AXIS));
    l1 = new JTextField(2);
    longCountPanel.add(l1);
    l2 = new JTextField(2);
    longCountPanel.add(l2);
    l3 = new JTextField(2);
    longCountPanel.add(l3);
    l4 = new JTextField(2);
    longCountPanel.add(l4);
    l5 = new JTextField(2);
    longCountPanel.add(l5);
    lcSuper.add(longCountPanel);
    inputs.add(lcSuper);
    
    //Set up the Gregorian Date Input
    gregSuper = new JPanel();
    gregSuper.setLayout(new BoxLayout(gregSuper,BoxLayout.Y_AXIS));
    gregDateLabel = new JLabel("Gregorian Date (d/m/y):");
    gregSuper.add(gregDateLabel);
    gregDatePanel = new JPanel();
    gregDatePanel.setLayout(new BoxLayout(gregDatePanel,BoxLayout.X_AXIS));
    gd = new JTextField(2);
    gregDatePanel.add(gd);
    gm = new JTextField(2);
    gregDatePanel.add(gm);
    gy = new JTextField(4);
    gregDatePanel.add(gy);
    String[] gregops = {"CE","BCE"};
    gregBC = new JComboBox(gregops);
    gregBC.setSelectedIndex(0);
    gregDatePanel.add(gregBC);
    gregSuper.add(gregDatePanel);
    inputs.add(gregSuper);
    
    //Set up the MDC Input
    mdcSuper = new JPanel();
    mdcSuper.setLayout(new BoxLayout(mdcSuper,BoxLayout.Y_AXIS));
    mdcLabel = new JLabel("Mayan Day #:");
    mdcSuper.add(mdcLabel);
    mdcin = new JTextField(2);
    mdcSuper.add(mdcin);
    inputs.add(mdcSuper);
    
    //Pad and add the Inputs panel
    padding = new JLabel("============================================");
    inputs.add(padding);
    
    //Set up the Buttons
    buttonsnorth = new JPanel();
    buttonsnorth.setLayout(new BoxLayout(buttonsnorth,BoxLayout.X_AXIS));
    buttonssouth = new JPanel();
    buttonssouth.setLayout(new BoxLayout(buttonssouth,BoxLayout.X_AXIS));
    fromlc = new JButton("From Long Count");
    fromlc.addActionListener(this);
    fromgd = new JButton("From Gregorian Date");
    fromgd.addActionListener(this);
    frommdc = new JButton("From Mayan Day #");
    frommdc.addActionListener(this);
    minus = new JButton("-1");
    minus.addActionListener(this);
    plus = new JButton("+1");
    plus.addActionListener(this);
    todaybutton = new JButton("Today");
    todaybutton.addActionListener(this);
    buttonsnorth.add(fromlc);
    buttonsnorth.add(fromgd);
    buttonsnorth.add(todaybutton);
    buttonssouth.add(frommdc);
    buttonssouth.add(minus);
    buttonssouth.add(plus);
    
    //Set up the Console
    console = new JTextArea(17,30);
    console.setEditable(false);
    console.setLineWrap(false);
    consolepane = new JScrollPane(console);
    consolepane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    consolepane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    
    //Set up the Reconstructor Pane
    reconPanel = new JPanel();
    reconPanel.setLayout(new BoxLayout(reconPanel,BoxLayout.X_AXIS));
    recon = new JButton("Recover");
    recon.addActionListener(this);
    recontzold = new JTextField(2);
    reconhaabd = new JTextField(2);
    reconn = new JTextField(3);
    reconn.setText("1");
    String[] tzops = {"Ajaw","Imix","Ik'","Ak'bal","K'an","Chikchan","Kimi","Manik","Lamat","Muluk","Ok","Chuwen","Eb'","Ben","Ix","Men","Kib'","Kab'an","Etz'nab'","Kawak"};
    recontzolm = new JComboBox(tzops);
    String[] hops = {"Pop","Wo","Sip","Sots'","Sek","Xul","Yaxk'in","Mol","Ch'en","Yax","Sak","Keh","Mak","K'ank'in","Muwan","Pax","K'ayab","Kumk'u","Wayeb"};
    reconhaabm = new JComboBox(hops);
    reconPanel.add(recontzold);
    reconPanel.add(recontzolm);
    reconPanel.add(reconhaabd);
    reconPanel.add(reconhaabm);
    reconPanel.add(reconn);
    reconPanel.add(recon);
    
    
    //Add the Console, Buttons, and Inputs
    superbuttons = new JPanel();
    superbuttons.setLayout(new BoxLayout(superbuttons,BoxLayout.Y_AXIS));
    superbuttons.add(inputs);
    superbuttons.add(buttonsnorth);
    superbuttons.add(buttonssouth);
    superbuttons.add(consolepane);
    superbuttons.add(reconPanel);
    content.add(superbuttons,BorderLayout.EAST);
    
    //Instantiate the Mayan Date Object
    da = new MayanDate();
    fillFields();
    infoDump();
      
    //Show the frame
    frame.pack();
    frame.setVisible(true);
  }
  
  
  
  
  
  public void actionPerformed(ActionEvent e){
    if(e.getSource()==fromlc){
      //Convert from Long Count
      int[] neolong = new int[5];
      neolong[0]=Integer.parseInt(l1.getText());
      neolong[1]=Integer.parseInt(l2.getText());
      neolong[2]=Integer.parseInt(l3.getText());
      neolong[3]=Integer.parseInt(l4.getText());
      neolong[4]=Integer.parseInt(l5.getText());
      da.setLongCount(neolong);
      fillFields();
      infoDump();
    }else if(e.getSource()==fromgd){
      //Convert from Gregorian
      boolean b = false;
      if(gregBC.getSelectedItem().equals("BCE"))b=true;
      da.setGregDate(Integer.parseInt(gd.getText()),Integer.parseInt(gm.getText()),Integer.parseInt(gy.getText()),b);
      fillFields();
      infoDump();
    }else if(e.getSource()==frommdc){
      //Convert from MDC
      da.setMDC(Integer.parseInt(mdcin.getText()));
      fillFields();
      infoDump();
    }else if(e.getSource()==minus){
      //1down
      da.oneDown();
      fillFields();
      infoDump();
    }else if(e.getSource()==plus){
      //1up
      da.oneUp();
      fillFields();
      infoDump();
    }else if(e.getSource()==recon){
      //Recover
      recoverCR();
    }else if(e.getSource()==todaybutton){
      //Today
      da.today();
      fillFields();
      infoDump();
    }
  }
  
  private void fillFields(){
    //Backfills the input fields
    l1.setText(da.getLongCount()[0]+"");
    l2.setText(da.getLongCount()[1]+"");
    l3.setText(da.getLongCount()[2]+"");
    l4.setText(da.getLongCount()[3]+"");
    l5.setText(da.getLongCount()[4]+"");
    int temp1 = da.getGregDate()[0];
    int temp2 = da.getGregDate()[1];
    int temp3 = da.getGregDate()[2];
    if (temp3<=0){
      gregBC.setSelectedIndex(1);
      temp3 = Math.abs(temp3-1);
    }else{
      gregBC.setSelectedIndex(0);
    }
    gd.setText(temp1+"");
    gm.setText(temp2+"");
    gy.setText(temp3+"");
    mdcin.setText(da.getMDC()+"");
    recontzold.setText(da.getTzolkin()[1]+"");
    reconhaabd.setText(da.getHaab()[1]+"");
    recontzolm.setSelectedIndex(da.getTzolkin()[2]);
    reconhaabm.setSelectedIndex(da.getHaab()[2]);
  }
  
  private void infoDump(){
    //Dumps the given day's information to the Console.
    write("\n=======================\n");
    write(da.gregToString()+"\n\n");
    write("Long Count: "+da.lcToString()+"\n");
    write("Calendar Round: "+da.crToString()+"\n\n");
    write(da.suppToString()+"\n");
    int eightpos = da.getEight()[1];
    write("819-day cycle: "+da.lcCond(da.longCount(eightpos))+"   "+da.crCond(da.tzolkinOf(eightpos),da.haabOf(eightpos))+"\n");
    write(da.eightToString()+"\n\n");
    write("Mayan Day: "+da.getMDC()+"    Calendar Round: "+da.getCRD()+"\n");
    write("Tzolk'in Day: "+da.getTzolkin()[0]+"   Haab' Day: "+da.getHaab()[0]+"\n");
    write("Round began: "+da.roundBorders(da.getMDC())[0]+"   Round will end: "+da.roundBorders(da.getMDC())[1]);
    write("\n\n"+da.omenDay(da.getTzolkin()[2]));
    write("\nYear Omens: "+da.omenYear(da.getSupp()[1],da.getSupp()[2]));
  }
  
  private void recoverCR(){
    //Writes dates recovered from Calendar Round
    if(Integer.parseInt(recontzold.getText())>0&&Integer.parseInt(recontzold.getText())<14&&Integer.parseInt(reconn.getText())<(C_MAX-1)){
      if(Integer.parseInt(reconhaabd.getText())>=0&&Integer.parseInt(reconhaabd.getText())<20){
        int[] possible = da.estimate(da.roundPos(Integer.parseInt(recontzold.getText()),recontzolm.getSelectedIndex(),Integer.parseInt(reconhaabd.getText()),reconhaabm.getSelectedIndex()),Integer.parseInt(reconn.getText()));
        write("\n=======================\n"+reconn.getText()+" possible instances of "+da.crCond(da.tzolkinOf(possible[0]),da.haabOf(possible[0])));
        for(int i=0;i<possible.length;i++){
          int pif = possible[i]%9;
          if(pif==0)pif=9;
          write("\n"+(i+1)+"   "+da.lcCond(da.longCount(possible[i]))+"          "+da.gregCond(da.mdcGREG(possible[i]))+"      G"+pif);
        }
      }
    }
  }
  
  private void write(String a){
    //Protected system for console appending
    if(console.getText().split("\n").length>C_MAX-1){
      //If the console is overflowing
      console.setText(console.getText().substring(console.getText().indexOf("\n")+1));
    }
    console.setText(console.getText()+a);
  }
  
  
  
}