/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Ondrej
 */
class Counter{

  private Typ type;
  private int idCounter;

  public Counter(Typ type){
    this.type = type;
    this.idCounter = 0;
  }

  int addElement(){
    this.idCounter++;
    return this.idCounter;
  }

  public int getIdCounter(){
    return idCounter;
  }



  @Override
  public String toString(){
    return "Counter{" + "type=" + type + ", idCounter=" + idCounter + '}';
  }

}
