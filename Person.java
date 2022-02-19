import java.lang.Math;
/**
    * <b>The <code>Person</code> class represents a person who need to go from a floor to another at a given moment</b>
    * <p>
    * An instance from <code>Person</code> has 5 variables:
    * <ul>
    * <li>An identificator</li>
    * <li>A step which symbolizes the moment when the person calls a lift</li>
    * <li>The floor where the person is when they call for the lift</li>
    * <li>The floor where they want to go</li>
    * <li>The amount of time they waited for the lift</li>
    * </ul>
    *
    * @author Elise et Emma
    * @version 1.0
*/

public class Person{
  /**
    * The identificator of the person.
  */
  private int id;
  /**
    * The moment when the <code>Person</code> has requested a lift.
  */
  private int step;
  /**
    * The floor where a <code>Person</code> is when they requested a lift.
  */
  private int startFloor;
  /**
    * The floor where a <code>Person</code> want to go.
  */
  private int endFloor;
  /**
    * The amount of time a <code>Person</code> has waited for the lift.
  */
  private int waitingTime;

  //CONSTRUCTEURS

  /**
    * Constructs an instance of the class <code>Person</code>.
    * @param i
    * The identificator.
    * @param s
    * The moment when the <code>Person</code> has requested a lift.
    * @param sF
    * The floor where the <code>Person</code> is
    * @param eF
    * The floor where the <code>Person</code> wants to go
  */
  public Person(int i,int s,int sF,int eF){
    this.id = i;
    this.step = s;
    this.startFloor = sF;
    this.endFloor = eF;
    this.waitingTime = 0;
  }
  /**
    * Constructs an empty instance of the class <code>Person</code>.
    * It sets all the parameters to -1 by default, except the waiting time which is set to 0.
  */
  public Person(){
    this(-1,-1,-1,-1);
  }
  /**
    * Constructs an instance of the class <code>Person</code>.
    * It sets all the parameters to -1, except the waiting time which is set to 0 and the identificator which is set to the parameter.
    * @param id
    * The identifiactor.
  */
  public Person(int id){
    this(id,-1,-1,-1);
  }

  //GETTERS & SETTERS

  /**
    *Returns the identifiactor.
    *@return int
  */
  public int getId(){
    return this.id;
  }
  /**
    *Returns the step.
    *@return int
  */
  public int getStep(){
    return this.step;
  }
  /**
    *Returns the start floor.
    *@return int
  */
  public int getStartFloor(){
    return this.startFloor;
  }
  /**
    *Returns the end floor.
    *@return int
  */
  public int getEndFloor(){
    return this.endFloor;
  }
  /**
    *Returns the waiting time.
    *@return int
  */
  public int getWaitingTime(){
    return this.waitingTime;
  }

  /**
    * Update the identificator.
    * @param i
    * The new identificator.
  */
  public void setId(int i){
    this.id = i;
  }
  /**
    * Update the step.
    * @param s
    * The new step.
  */
  public void setStep(int s){
    this.step = s;
  }
  /**
    * Update the start floor.
    * @param sF
    * The new start floor.
  */
  public void setStartFloor(int sF){
    this.startFloor = sF;
  }
  /**
    * Update the end floor.
    * @param eF
    * The new end floor.
  */
  public void setEndFloor(int eF){
    this.endFloor = eF;
  }
  /**
    * Update the waiting time.
    * @param wT
    * The new waiting time.
  */
  public void setWaitingTime(int wT){
    this.waitingTime = wT;
  }

  //METHODES

  /**
    * Returns a string representation of the person.
    * @return String
  */
  @Override
  public String toString(){
    return "La personne " + id + " est arrivee au moment " + step + ". Elle va de l'etage " + startFloor + " a l'etage " + endFloor +"\n" ;
  }
  /**
    * Returns an integer that gives the direction of the person, either 1, 0 or -1.
    * If the returned integer is 1, the person is going up.
    * If the returned integer is -1, the person is going down.
    * Else the person is not moving in any direction.
    * @return int
  */
  public int direction(){
    int d = this.endFloor - this.startFloor;
    if(d>0) return 1;
    else if(d>0) return -1;
    return 0;
  }

}
