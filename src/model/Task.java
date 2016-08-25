package model;

import java.util.Date;
import java.util.logging.Logger;

/**
 *
 * @author Ondrej
 */
public class Task implements IElement{

  private int id;
  private Priorty priority;
  private String description;
  private boolean finish;
  private IElement next;

  public Task(){
    this.finish = false;
  }

  public Task(Priorty priority, String description, boolean finish){
    this.priority = priority;
    this.description = description;
    this.finish = finish;
  }

  public int getId(){
    return id;
  }

  @Override
  public IElement getNext(){
    return next;
  }

  @Override
  public void setNext(IElement next){
    this.next = next;
  }

  public Priorty getPriority(){
    return priority;
  }

  public String getDescription(){
    return description;
  }

  public boolean isFinish(){
    return finish;
  }

  public void setPriority(Priorty priority){
    this.priority = priority;
  }

  public void setDescription(String description){
    this.description = description;
  }

  public void setFinish(boolean finish){
    this.finish = finish;
  }
  

  @Override
  public void setId(int id){
    this.id = id;
  }

  @Override
  public boolean compare(String value, int select){
    switch(select){
      case 1: {
        Priorty tmp = Priorty.valueOf(value);
        if(tmp == this.priority){
          return true;
        }else{
          return false;
        }
      }
      case 2: {
        boolean tmp = Boolean.parseBoolean(value);
        if(tmp == this.finish){
          return true;
        }else{
          return false;
        }
      }
      default:
        throw new UnsupportedOperationException();
    }
  }


  public IElement copy() {
    return new Task(this.priority, this.description, this.finish);

  }



  @Override
  public IElement getTop(){
    return null;
  }

  @Override
  public String toString(){
    return "Task{" + "id=" + id + ", priority=" + priority + ", description=" + description + ", finish=" + finish + '}';
  }

}
