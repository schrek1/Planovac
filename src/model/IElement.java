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
public interface IElement{
  public void setId(int id);
  public IElement getNext();
  public void setNext(IElement e);
  public boolean compare(String value, int select);
  public IElement getTop();
  @Override
  public String toString();
}
