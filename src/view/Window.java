/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import model.Container;
import model.Planer;
import model.Priorty;
import model.Task;
import model.IElement;

/**
 *
 * @author Ondrej
 */
public class Window{

  public static WeekPanel wp[] = new WeekPanel[7];

  final int RES_X = 800;
  final int RES_Y = 600;

  Planer planer;
  JTabbedPane jtp;
  JLabel jlCreate;

  public Window(){
    this.planer = model.Planer.getPlaner();
    JFrame jfrm = new JFrame("Plánovač úkolů");
    this.jtp = new JTabbedPane();
    this.jlCreate = new JLabel("Ondřej Schrek 2016");
    jfrm.setLayout(null);
    jfrm.setSize(RES_X, RES_Y);
    jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    jfrm.setResizable(false);

    Container tmp = this.planer.getDayInWeek("Pondělí");
    wp[0] = new WeekPanel(tmp);
    jtp.addTab(tmp.getName(), wp[0]);

    tmp = this.planer.getDayInWeek("Úterý");
    wp[1] = new WeekPanel(tmp);
    jtp.addTab(tmp.getName(), wp[1]);

    tmp = this.planer.getDayInWeek("Středa");
    wp[2] = new WeekPanel(tmp);
    jtp.addTab(tmp.getName(), wp[2]);

    tmp = this.planer.getDayInWeek("Čtvrtek");
    wp[3] = new WeekPanel(tmp);
    jtp.addTab(tmp.getName(), wp[3]);

    tmp = this.planer.getDayInWeek("Pátek");
    wp[4] = new WeekPanel(tmp);
    jtp.addTab(tmp.getName(), wp[4]);

    tmp = this.planer.getDayInWeek("Sobota");
    wp[5] = new WeekPanel(tmp);
    jtp.addTab(tmp.getName(), wp[5]);

    tmp = this.planer.getDayInWeek("Neděle");
    wp[6] = new WeekPanel(tmp);
    jtp.addTab(tmp.getName(), wp[6]);

    Dimension size = jtp.getPreferredSize();
    Insets ins = jfrm.getInsets();
    int margLft = 30 + ins.left;
    int margTop = 20 + ins.top;
    jtp.setBounds(margLft, margTop, RES_X - margLft * 2, RES_Y - margTop * 4);

    jfrm.add(jtp);

    this.jlCreate.setBounds(355, 550, this.jlCreate.getPreferredSize().width, this.jlCreate.getPreferredSize().height);
    jfrm.add(this.jlCreate);

    jfrm.setVisible(true);
  }

  public static void main(String[] args){
    SwingUtilities.invokeLater(()->{
      new Window();
    });
  }
}

class WeekPanel extends JPanel{

  int selId = -1;
  int selRow = -1;
  ArrayList<String> backup = new ArrayList<>();

  //OK
  JButton jbAdd;
  JButton jbMove;
  JButton jbFilter;
  JButton jbSort;

  //OK
  JButton jbDelete;
  JButton jbChangeState;
  JButton jbModify;

  //OK
  JComboBox<String> jcbPriority;
  String priority[] = {"A", "B", "C"};
  JTextField jtfDescription;
  JToggleButton jtbFinish;
  JLabel jlPriority;
  JLabel jlDescription;

  //OK
  JTable jtable;
  JScrollPane jspTable;
  DefaultTableModel dtm;

  //OK
  Container container;

  public WeekPanel(Container container){
    this.container = container;
    this.setLayout(null);

    this.setTable();
    this.setDetails();
    this.setMove();
    this.setAdd();
    this.setFilter();
    this.setSort();

  }

  private Object[][] fillTable(){
    int count = 0;
    IElement tmp = this.container.getTop();
    while(tmp != null){
      count++;
      tmp = tmp.getNext();
    }

    Object data[][] = new Object[count][4];

    count = 0;
    tmp = this.container.getTop();
    while(tmp != null){
      Task tsk = (Task)tmp;
      data[count][0] = tsk.getId();
      data[count][1] = tsk.getPriority().toString();
      data[count][2] = tsk.getDescription();
      data[count][3] = tsk.isFinish() ? "OK" : "NEDOKONČEN";
      count++;
      tmp = tmp.getNext();
    }
    return data;
  }

  private void setTable(){
    String colHeads[] = {"id", "Priorita", "Popis", "Stav"};
    dtm = new DefaultTableModel(this.fillTable(), colHeads);
    this.jtable = new JTable(dtm);
    //this.jtable.setEnabled(false);
    //this.jtable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

    this.jtable.getColumnModel().getColumn(0).setPreferredWidth(5);
    this.jtable.getColumnModel().getColumn(1).setPreferredWidth(5);
    this.jtable.getColumnModel().getColumn(2).setPreferredWidth(100);
    this.jtable.setRowSelectionAllowed(true);
    this.jtable.setFocusable(false);
    this.jtable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    this.jtable.setPreferredSize(new Dimension(500, 300));

    this.jspTable = new JScrollPane(this.jtable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    this.jspTable.setPreferredSize(new Dimension(500, 100));
    this.jspTable.setBounds(10, 10, this.jspTable.getPreferredSize().width, 200);

    this.jtable.getSelectionModel().addListSelectionListener(new MyListSelectionListener());
    //this.dtm.addTableModelListener(new MyTableChangeListener());

    this.add(this.jspTable);
  }

  private void setDetails(){
    this.jcbPriority = new JComboBox<>(this.priority);
    this.jcbPriority.setBounds(118, 297, this.jcbPriority.getPreferredSize().width, this.jcbPriority.getPreferredSize().height);
    this.add(this.jcbPriority);

    this.jtfDescription = new JTextField(15);
    this.jtfDescription.setBounds(118, 350, this.jtfDescription.getPreferredSize().width, this.jtfDescription.getPreferredSize().height);
    this.add(this.jtfDescription);

    this.jtbFinish = new JToggleButton();
    this.jtbFinish.setBounds(118, 388, 167, 20);
    this.jtbFinish.addActionListener(new MyTogleButtonListener());
    this.add(this.jtbFinish);

    this.jlPriority = new JLabel("Priorita ukolu");
    this.jlPriority.setBounds(20, 300, this.jlPriority.getPreferredSize().width, this.jlPriority.getPreferredSize().height);
    this.add(this.jlPriority);

    this.jlDescription = new JLabel("Popis ukolu");
    this.jlDescription.setBounds(20, 350, this.jlDescription.getPreferredSize().width, this.jlDescription.getPreferredSize().height);
    this.add(this.jlDescription);

    this.jbModify = new JButton("Upravit ukol");
    this.jbModify.setBounds(30, 450, this.jbModify.getPreferredSize().width, this.jbModify.getPreferredSize().height);
    this.jbModify.addActionListener(new MyUpdateButtonListener());
    this.add(this.jbModify);

    this.jbDelete = new JButton("Smazat ukol");
    this.jbDelete.setBounds(180, 450, this.jbDelete.getPreferredSize().width, this.jbDelete.getPreferredSize().height);
    this.jbDelete.addActionListener(new MyDeleteButtonListener());
    this.add(this.jbDelete);

  }

  private void setMove(){
    this.jbMove = new JButton("Presunout ukol");
    this.jbMove.setBounds(320, 300, this.jbMove.getPreferredSize().width, this.jbMove.getPreferredSize().height + 50);
    this.jbMove.addActionListener(new MyMoveButtonListener());
    this.add(this.jbMove);

  }

  private void setAdd(){
    this.jbAdd = new JButton("Pridat ukol");
    this.jbAdd.setBounds(320, 400, this.jbMove.getPreferredSize().width, this.jbAdd.getPreferredSize().height + 50);
    this.jbAdd.addActionListener(new MyAddButtonListener());
    this.add(this.jbAdd);
  }

  private void setFilter(){
    this.jbFilter = new JButton("Filtrovat");
    this.jbFilter.setBounds(600, 400, this.jbMove.getPreferredSize().width, this.jbFilter.getPreferredSize().height + 50);
    this.jbFilter.addActionListener(new MyFilterButtonListener());
    this.add(this.jbFilter);
  }

  private void setSort(){
    this.jbSort = new JButton("Řadit");
    this.jbSort.setBounds(600, 300, this.jbMove.getPreferredSize().width, this.jbSort.getPreferredSize().height + 50);
    this.jbSort.addActionListener(new MySortButtonListener());
    this.add(this.jbSort);
  }

  private void resetDetails(){
    jcbPriority.setSelectedItem("A");
    jtfDescription.setText("");
    jtbFinish.setText("");
    jtbFinish.setSelected(true);

    jtbFinish.setSelected(false);
  }

  class MyListSelectionListener implements ListSelectionListener{

    @Override
    public void valueChanged(ListSelectionEvent e){
      int row = jtable.getSelectedRow();
      jcbPriority.setSelectedItem(jtable.getValueAt(row, 1));
      selId = (int)jtable.getValueAt(row, 0);
      selRow = jtable.getSelectedRow();
      jtfDescription.setText((String)jtable.getValueAt(row, 2));
      if("OK".equals(jtable.getValueAt(row, 3))){
        jtbFinish.setText("OK");
        jtbFinish.setSelected(true);
      }else{
        jtbFinish.setText("NEDOKONCENO");
        jtbFinish.setSelected(false);
      }
    }
  }

  class MyTogleButtonListener implements ActionListener{

    @Override
    public void actionPerformed(ActionEvent e){
      AbstractButton abstractButton = (AbstractButton)e.getSource();
      boolean selected = abstractButton.getModel().isSelected();

      if(selected){
        jtbFinish.setText("OK");
      }else{
        jtbFinish.setText("NEDOKONCENO");
      }
    }
  }

  class MyDeleteButtonListener implements ActionListener{

    @Override
    public void actionPerformed(ActionEvent e){
      if(selId != -1){
        if(jtable.getRowCount() > 0){
          System.out.println(selId);
          container.deleteTask(selId);
          System.out.println(selRow);
          try{
            ((DefaultTableModel)jtable.getModel()).removeRow(selRow);
          }catch(Exception ex){

          }finally{
            ((DefaultTableModel)jtable.getModel()).fireTableDataChanged();
          }
        }
      }
    }
  }

  class MyUpdateButtonListener implements ActionListener{

    @Override
    public void actionPerformed(ActionEvent e){
      if((selId != -1) && (jtable.getSelectedRow() != -1)){
        container.updateTask(selId, Priorty.valueOf((String)jcbPriority.getSelectedItem()), jtfDescription.getText(), jtbFinish.isSelected());
        Object data[] = {selId, Priorty.valueOf((String)jcbPriority.getSelectedItem()), jtfDescription.getText(), jtbFinish.isSelected() ? "OK" : "NEDOKONČEN"};
        dtm.addRow(data);
        dtm.removeRow(jtable.getSelectedRow());
      }
    }
  }

  class MyMoveButtonListener implements ActionListener{

    @Override
    public void actionPerformed(ActionEvent e){
      String[] choices = {"Pondělí", "Úterý", "Středa", "Čtvrtek", "Pátek", "Sobota", "Neděle"};
      String input = (String)JOptionPane.showInputDialog(null, "Vyberte kam přesunout úkol..",
              "Přesunutí úkolu", JOptionPane.QUESTION_MESSAGE, null, // Use
              // default
              // icon
              choices, // Array of choices
              choices[0]); // Initial choice
      Planer pl = Planer.getPlaner();
      Container dest = pl.getDayInWeek(input);
      dest.addElement(container.returnById(selId));
      int pos;
      for(pos = 0; pos < 7; pos++){
        if(Window.wp[pos].container.getName().equals(input)){
          break;
        }
      }

      Object data[] = {Window.wp[pos].container.getActualCnt(), Priorty.valueOf((String)jcbPriority.getSelectedItem()), jtfDescription.getText(), jtbFinish.isSelected() ? "OK" : "NEDOKONČEN"};
      Window.wp[pos].dtm.addRow(data);
      dtm.removeRow(jtable.getSelectedRow());
    }
  }

  class MyAddButtonListener implements ActionListener{

    @Override
    public void actionPerformed(ActionEvent e){
      Task tsk = new Task(Priorty.valueOf((String)jcbPriority.getSelectedItem()), jtfDescription.getText(), jtbFinish.isSelected());
      container.addElement(tsk);
      Object data[] = {tsk.getId(), Priorty.valueOf((String)jcbPriority.getSelectedItem()), jtfDescription.getText(), jtbFinish.isSelected() ? "OK" : "NEDOKONČEN"};
      dtm.addRow(data);
    }
  }

  class MyFilterButtonListener implements ActionListener{

    @Override
    public void actionPerformed(ActionEvent e){
      System.out.println("*****");
      String[] choices = {"Priorita", "Stav"};
      String input = (String)JOptionPane.showInputDialog(null, "Vyberte podle ceho filtrovat úkol..",
              "Filtrovani", JOptionPane.QUESTION_MESSAGE, null, // Use
              // default
              // icon
              choices, // Array of choices
              choices[0]); // Initial choice

      Container cnt = null;
      switch(input){
        case "Priorita": {
          String tmp[] = {"A", "B", "C"};
          input = (String)JOptionPane.showInputDialog(null, "Vyberte prioritu úkolu..",
                  "Filtrovani", JOptionPane.QUESTION_MESSAGE, null, // Use
                  // default
                  // icon
                  tmp, // Array of choices
                  tmp[0]); // Initial choice

          cnt = container.filter(input, 1);
          break;
        }
        case "Stav": {
          String tmp[] = {"NEDOKONČENO", "OK"};
          input = (String)JOptionPane.showInputDialog(null, "Vyberte stav úkolu..",
                  "Filtrovani", JOptionPane.QUESTION_MESSAGE, null, // Use
                  // default
                  // icon
                  tmp, // Array of choices
                  tmp[0]); // Initial choice
          if(input.equals("OK")){
            cnt = container.filter(Boolean.toString(true), 2);
          }else{
            cnt = container.filter(Boolean.toString(false), 2);
          }
          break;
        }
      }
      if(cnt != null){
        String out = "";
        IElement tel = cnt.getTop();
        while(tel != null){
          out += tel.toString() + "\n";
          tel = tel.getNext();
        }

        JTextArea textArea = new JTextArea(out);
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        scrollPane.setPreferredSize(new Dimension(500, 500));
        JOptionPane.showMessageDialog(null, scrollPane, "Vysledek filtrovani",
                JOptionPane.QUESTION_MESSAGE);

      }

    }
  }

  class MySortButtonListener implements ActionListener{

    @Override
    public void actionPerformed(ActionEvent e){
      JRadioButton up = new JRadioButton("Vzestupne");
      up.setSelected(true);
      JRadioButton down = new JRadioButton("Sestupne");
      ButtonGroup bg = new ButtonGroup();
      bg.add(up);
      bg.add(down);
      String items[] = {"id", "priorita", "popis", "stav"};
      JComboBox<String> col = new JComboBox<>(items);
      String message = "Vyberte parametry razeni";
      Object[] params = {message, col, up, down};
      JOptionPane.showMessageDialog(null, params, "Řazení úkolů", JOptionPane.QUESTION_MESSAGE);
      String sel = (String)(col.getSelectedItem());

      IElement[] ael = container.toArray();

      if(ael != null){

        if("id".equals(sel)){
          Arrays.sort(ael, (IElement a, IElement b)->{
            Task ta = (Task)a;
            Task tb = (Task)b;
            int sel1;
            if(ta.getId() == tb.getId()){
              return 0;
            }
            if(ta.getId() > tb.getId()){
              sel1 = (up.isSelected()) ? 1 : -1;
              return sel1;
            }else{
              sel1 = (up.isSelected()) ? -1 : 1;
              return sel1;
            }
          });
        }

        if("priorita".equals(sel)){
          Arrays.sort(ael, (IElement a, IElement b)->{
            Task ta = (Task)a;
            Task tb = (Task)b;
            int sel1;
            sel1 = (up.isSelected()) ? ta.getPriority().compareTo(tb.getPriority()) : tb.getPriority().compareTo(ta.getPriority());
            return sel1;
          });
        }

        if("popis".equals(sel)){
          Arrays.sort(ael, (IElement a, IElement b)->{
            Task ta = (Task)a;
            Task tb = (Task)b;
            int sel1;
            sel1 = (up.isSelected()) ? ta.getDescription().compareTo(tb.getDescription()) : tb.getDescription().compareTo(ta.getDescription());
            return sel1;
          });
        }

        if("stav".equals(sel)){
          Arrays.sort(ael, (IElement a, IElement b)->{
            Task ta = (Task)a;
            Task tb = (Task)b;
            int sel1;
            String sa = ta.isFinish() ? "OK" : "NESPLNENO";
            String sb = tb.isFinish() ? "OK" : "NESPLNENO";
            sel1 = (up.isSelected()) ? sa.compareTo(sb) : sb.compareTo(sa);
            return sel1;
          });
        }

        String out = "";

        for(int i = 0; i < ael.length; i++){
          out += ael[i] + "\n";

        }

        JTextArea textArea = new JTextArea(out);
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        scrollPane.setPreferredSize(new Dimension(500, 500));
        JOptionPane.showMessageDialog(null, scrollPane, "Vysledek filtrovani",
                JOptionPane.QUESTION_MESSAGE);

      }
    }
  }

}
