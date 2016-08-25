package model;

import java.util.LinkedList;

/**
 *
 * @author Ondrej
 */
public class Planer{

  private IElement top;
  private static final Planer PLANER = new Planer();

  private Planer(){
    Container week = new Container(new Counter(Typ.DAY));
    top = week;
    week.setName("Týden");
    week.setType(Typ.WEEK);
    Container tmp = new Container(new Counter(Typ.TASK));

    tmp.setName("Pondělí");
    tmp.setType(Typ.DAY);
    tmp.addElement(new Task(Priorty.A, "Napsat ukoly", false));
    tmp.addElement(new Task(Priorty.B, "Nasekat drevo", false));
    week.addElement(tmp);

    tmp = new Container(new Counter(Typ.TASK));
    tmp.setName("Úterý");
    tmp.setType(Typ.DAY);
    tmp.addElement(new Task(Priorty.C, "Umyt nadobi", false));
    tmp.addElement(new Task(Priorty.B, "Zavolat domu", true));
    tmp.addElement(new Task(Priorty.B, "Serial Simpsonovi", false));
    week.addElement(tmp);

    tmp = new Container(new Counter(Typ.TASK));
    tmp.setName("Středa");
    tmp.setType(Typ.DAY);
    week.addElement(tmp);

    tmp = new Container(new Counter(Typ.TASK));
    tmp.setName("Čtvrtek");
    tmp.setType(Typ.DAY);
    week.addElement(tmp);

    tmp = new Container(new Counter(Typ.TASK));
    tmp.setName("Pátek");
    tmp.setType(Typ.DAY);
    week.addElement(tmp);

    tmp = new Container(new Counter(Typ.TASK));
    tmp.setName("Sobota");
    tmp.setType(Typ.DAY);
    week.addElement(tmp);

    tmp = new Container(new Counter(Typ.TASK));
    tmp.setName("Neděle");
    tmp.setType(Typ.DAY);
    week.addElement(tmp);
  }

  public static Planer getPlaner(){
    return Planer.PLANER;
  }

  public String toString(int deep){
    IElement next = this.top;
    String str = "";
    for(int i = 0; i <= deep - 1; i++){
      if((next != null) && (i == deep - 1)){
        while(next != null){
          str += next.toString();
          next = next.getNext();
        }
      }
      next = top.getTop();
    }
    return str;
  }

  LinkedList<IElement> goDeep(IElement e){
    LinkedList<IElement> stack = new LinkedList<>();
    stack.add(e);
    IElement top = e.getTop();
    while(top != null){
      if(top.getTop() == null){
        return stack;
      }
      stack.add(top);
      top = top.getTop();
    }
    return stack;
  }

  public String toTree(){
    String out = "";
    int deep = 0;
    IElement tmp;
    LinkedList<IElement> stack = new LinkedList<>();
    LinkedList<IElement> tstck = new LinkedList<>();

    stack = this.goDeep(this.top);
    tstck = (LinkedList<IElement>)stack.clone();
    while(!tstck.isEmpty()){
      for(int i = 0; i < deep; i++){
        out += "\t";
      }
      out += tstck.pop() + "\n";
      deep++;
    }

    deep--;

    while(!stack.isEmpty()){
      tmp = stack.pollLast();
      //System.out.println(tmp);
      while(tmp.getNext() != null){
        IElement top = tmp.getTop();
        while(top != null){
          deep++;
          for(int i = 0; i < deep; i++){
            out += "\t";
          }
          out += top + "\n";
          top = top.getNext();
          deep--;
        }
        tmp = tmp.getNext();
        for(int i = 0; i < deep; i++){
          out += "\t";
        }
        out += tmp + "\n";
      }
    }
    return out;
  }


  public Container getDayInWeek(String name){
    Container tmp = (Container)this.top.getTop();
    while(tmp!=null){
      if(tmp.getName().equals(name)){
        return tmp;
      }
      tmp = (Container)tmp.getNext();
    }
    return null;
  }



  public static void main(String[] args){
    Planer planer = Planer.getPlaner();
    System.out.println(planer.toTree());
//    System.out.println(planer.getDayInWeek("Středa"));

  }

}
