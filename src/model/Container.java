/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;

public class Container implements IElement{

  private String name;
  private Typ type;
  private int id;
  private IElement top;
  private final Counter cntElements;
  private IElement next;

  public int getActualCnt(){
    return cntElements.getIdCounter();
  }

  public String getName(){
    return name;
  }

  @Override
  public IElement getNext(){
    return next;
  }

  @Override
  public void setNext(IElement next){
    this.next = next;
  }

  public Container(Counter cntElements){
    this.top = null;
    this.cntElements = cntElements;
  }

  public void setName(String name){
    this.name = name;
  }

  public void setType(Typ type){
    this.type = type;
  }

  public void setId(int id){
    this.id = id;
  }

  public void setTop(IElement top){
    this.top = top;
  }

  public void addElement(IElement e){
    //System.out.println(this.cntElements.addElement());
    e.setId(this.cntElements.addElement());
    e.setNext(null);
    if(this.top == null){
      this.top = e;
    }else{
      IElement tmp = this.top;
      while(tmp.getNext() != null){
        tmp = tmp.getNext();
      }
      tmp.setNext(e);
    }
  }

  public IElement returnById(int id){
    Task tsk = (Task)this.top;
    if(tsk != null){
      if(tsk.getId() == id){
        return tsk;
      }else{
        Task tmp = (Task)this.top.getNext();
        while(tmp != null){
          if(tmp.getId() == id){
            return tmp;
          }
          tmp = (Task)tmp.getNext();
        }
        return null;
      }
    }
    return null;
  }

  public boolean deleteElement(IElement e){
    if(this.top != null){
      if(this.top == e){
        this.top = null;
      }else{
        IElement before = this.top;
        IElement tmp = this.top.getNext();
        while(tmp != null){
          if(tmp == e){
            before.setNext(tmp.getNext());
            return true;
          }
          before = tmp;
          tmp = tmp.getNext();
        }
        return false;
      }
    }
    return false;
  }

  public boolean deleteTask(int id){
    Task tsk = (Task)this.top;
    if(tsk != null){
      if(tsk.getId() == id){
        this.top = null;
      }else{
        Task before = (Task)this.top;
        Task tmp = (Task)this.top.getNext();
        while(tmp != null){
          if(tmp.getId() == id){
            before.setNext(tmp.getNext());
            return true;
          }
          before = tmp;
          tmp = (Task)tmp.getNext();
        }
        return false;
      }
    }
    return false;
  }

  public boolean updateTask(int id, Priorty priority, String description, boolean finish){
    Task tsk = (Task)this.top;
    if(tsk != null){
      if(tsk.getId() == id){
        tsk.setPriority(priority);
        tsk.setDescription(description);
        tsk.setFinish(finish);
      }else{
        Task tmp = (Task)this.top.getNext();
        while(tmp != null){
          if(tmp.getId() == id){
            tmp.setPriority(priority);
            tmp.setDescription(description);
            tmp.setFinish(finish);
            return true;
          }
          tmp = (Task)tmp.getNext();
        }
        return false;
      }
    }
    return false;
  }

  public Container filter(String name, int id){
    Container cnt = new Container(new Counter(Typ.TASK));
    if(this.top != null){
      Task tmp = (Task)this.top;
//      System.out.println("----filtr---");
      while(tmp != null){
//        System.out.println(tmp+"  ->   "+tmp.getNext());
        if(tmp.compare(name, id)){
          cnt.addElement(tmp.copy());
        }
        tmp = (Task)tmp.getNext();
//        System.out.println("next"+tmp);
      }
//      System.out.println("----konec---");
      return cnt;
    }
    return null;
  }

  public IElement[] toArray(){
    ArrayList<IElement> list = new ArrayList<>();
    if(this.top != null){
      IElement tmp = this.top;
      while(tmp != null){
        list.add(tmp);
        tmp = tmp.getNext();
      }
      IElement arr[] = new IElement[list.size()];
      list.toArray(arr);
      return arr;
    }
    return null;
  }

  @Override
  public IElement getTop(){
    return this.top;
  }

  @Override
  public boolean compare(String value, int select){
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public String toString(){
    return "Container{" + "name=" + name + ", type=" + type + ", id=" + id + ", cntElements=" + cntElements + '}';
  }

}
