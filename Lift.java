import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.lang.Math;
import java.util.ListIterator;
import java.util.Comparator;
import java.util.stream.*;
import java.util.stream.Collectors.*;

/**
    * <b>The <code>Lift</code> class represents a lift that goes from one floor to another.</b>
    * <p>
    * An instance from the class <code>Lift</code> has 12 variables:
    * <ul>
    * <li>An unique identificator attributed permanently.</li>
    * <li>A list of <code>Person</code> who are in the lift.</li>
    * <li>An activity time which symbolizes the amount of time a lift has been activated.</li>
    * <li>A floor which is the floor where the elevator is stationned at the moment.</li>
    * <li>A maximal amount of <code>Person</code> carriable by the lift. This amount cannot be changed.</li>
    * <li>A direction : either the lift is going up or down or stay still.</li>
    * <li>The speed of the lift, the amountof time they take to go from one floor to the up or down floor.</li>
    * <li>A list of two lists which each contains the next floors the lift is going to visit according to the <code>Person</code> already in the lift.</li>
    * <li>A boolean variable that tell us if the lift has its doors open or not.</li>
    * <li>The amount of time the lift has its doors open.</li>
    * <li>The moment when its doors open.</li>
    * <li>The moment when its doors close.</li>
    * </ul>
    *
    * @see Person
    *
    * @author Elise et Emma
    * @version 1.0
*/
public class Lift implements computeChangeTime{
  /**
    * The clock used to incremente properly the id. It starts from 1.
  */
  private static int cpt = 1;
  /**
    * The identificator of the lift. This id cannot be changed.
  */
  private final int id;
  /**
    * The maximal amount of <code>Person</code> carriable by the lift. This amount cannot be changed.
  */
  private final int capacity;
  /**
    * The list of <code>Person</code> in the lift.
    * <p> It is possible to add and remove a <code>Person</code>. </p>
    * @see Person
  */
  private List<Person> passengers;
  /**
    * The amount of time the lift has been used so far.
  */
  private int actTime;
  /**
    * The current floor of the lift.
  */
  private int currentFloor;
  /**
    * The direction of the lift.
  */
  private Direction direction;
  /**
    * The speed of the lift.
  */
  private final int speed;
  /**
    * The list of floors the lift will stop at.
  */
  private LinkedList<LinkedList<Integer>> nextFloor; //ON PEUT LIMITER LA TAILLE A 2 TOUT DE SUITE ? UN UP ET UN DOWN
  /**
    * If the lift has his doors open or not.
  */
  private boolean doorsOpen;
  /**
    * The amount of time during which the doors are open.
  */
  private int periodDoorsOpen;
  /**
    * The moment when the lift opens its doors.
  */
  private int debDoorsOpen;
  /**
    * The moment when the lift closes its doors.
  */
  private int endDoorsOpen;

  //CONSTRUCTEURS

  //A VOIR POUR LE CURRENT FLOOR PAR DEFAUT --> RANDOM ENTRE 0 ET FLOORMAX ? DANS CE CAS LA COMMENT OBTENIR FLOORMAX AUSSI ? CAR ON Y A ACCES DANS MONITOR SEULEMENT.

  /**
    * Constructs an empty instance of the class <code>Lift</code>.
    * an empty list of <code>Person</code>, a time sets to <code>0</code> and a floor sets to <code>0</code> by default.
    * @see Person
  */
  public Lift(){
    this(new ArrayList<Person>(),0,0,16,Direction.still,1, new LinkedList<LinkedList<Integer>>(), false, 0, 0, 0);
  }
  /**
    * Constructs an instance of the class <code>Lift</code>.
    * @param p The list of <code>Person</code> in the lift.
    * @param aT The time of the lift.
    * @param cF The floor where the lift is stationned.
    * @param c The capacity of <code>Person</code> of the lift.
    * @param d The direction of the lift.
    * @param s The speed of the lift.
    * @param nF The list of lists for the floors to visit next.
    * @param tf The fact that the lift has its doors open.
    * @param t The amount of time the lift has its doors open.
    * @param dt The moment when the lift opens its doors.
    * @param et The moment when the lift closes its doors.
  */
  public Lift(ArrayList<Person> p, int aT, int cF, int c, Direction d, int s, LinkedList<LinkedList<Integer>> nF, boolean tf, int t, int dt, int et){
    this.passengers = p;
    this.actTime = aT;
    this.currentFloor = cF;
    this.capacity = c;
    this.id = cpt++;
    this.direction = d;
    this.speed = s;
    this.nextFloor = nF;
    int size = this.nextFloor.size();
    for(int i = size; i < 2; i++)
      this.nextFloor.add(new LinkedList<Integer>());
    this.doorsOpen = tf;
    this.periodDoorsOpen = t;
    this.debDoorsOpen = dt;
    this.endDoorsOpen = et;
  }
  /**
    * Constructs an instance of the class <code>Lift</code>.
    * @param c
    * The capacity of <code>Person</code> of the lift.
    * @param s
    * The speed of the lift
  */
  public Lift(int c, int s){
    this(new ArrayList<Person>(), 0, 0, c, Direction.still, s, new LinkedList<LinkedList<Integer>>(), false, 0, 0, 0);
  }

  //GETTERS & SETTERS

  /**
    * Returns the list of passengers.
    * @return ArrayList&lt;Person&gt;
    * @see Person
  */
  public List<Person> getPassengers(){
    return this.passengers;
  }
  /**
    * Returns the activty time.
    * @return int
  */
  public int getActTime(){
    return this.actTime;
  }
  /**
    * Returns the current floor of the lift.
    * @return int
  */
  public int getCurrentFloor(){
    return  this.currentFloor;
  }
  /**
    * Returns the identificator of the lift.
    * @return int
  */
  public int getId(){
    return this.id;
  }
  /**
    * Returns the capacity of <code>Person</code> carriable by the lift.
    * @return int
  */
  public int getCapacity(){
    return this.capacity;
  }
  /**
    * Returns the direction of the lift.
    * @return Direction
  */
  public Direction getDirection(){
    return this.direction;
  }
  /**
    * Returns the speed of the lift.
    * @return int
  */
  public int getSpeed(){
    return this.speed;
  }
  /**
    * Returns the list of the next floor to visit.
    * @return LinkedList&lt;LinkedList&lt;Integer&gt;&gt;
  */
  public LinkedList<LinkedList<Integer>> getNextFloor(){
    return this.nextFloor;
  }
  /**
    * Returns if the lift has its doors open.
    * @return boolean
  */
  public boolean getDoorsOpen(){
    return this.doorsOpen;
  }
  /**
    * Returns the amount of time during which the doors are open.
    * @return int
  */
  public int getPeriodDoorsOpen(){
    return this.periodDoorsOpen;
  }
  /**
    * Returns the time when the lift starts to open its doors.
    * @return int
  */
  public int getDebDoorsOpen(){
    return this.debDoorsOpen;
  }
  /**
    * Returns the time when the lift closes its doors.
    * @return int
  */
  public int getEndDoorsOpen(){
    return this.endDoorsOpen;
  }


  /**
    * Sets the list of <code>Person</code> of the lift.
    * @param p
    * The new list of <code>Person</code>.
  */
  public void setPassengers(List<Person> p){
    this.passengers = p;
  }
  /**
    * Sets the activity time of the lift.
    * @param t
    * The new activty time.
  */
  public void setActTime(int t){
    this.actTime = t;
  }
  /**
    * Sets the current floor of the lift.
    * @param cF
    * The new current floor.
  */
  public void setCurrentFloor(int cF){
    this.currentFloor = cF;
  }
  /**
    * Sets the direction of the lift.
    * @param d
    * The new direction.
  */
  public void setDirection(Direction d){
    this.direction = d;
  }
  /**
    * Sets the list of the next floor to visit.
    * @param nF
    * The new list of next floors.
  */
  public void setNextFloor(LinkedList<LinkedList<Integer>> nF){
    this.nextFloor = nF;
  }
  /**
    * Sets the state of the doors of the lift.
    * @param tf
    * The new value (true or false).
  */
  public void setDoorsOpen(boolean tf){
    this.doorsOpen = tf;
  }
  /**
    * Sets the amount of time during which the doors are open.
    * @param t
    * The new period.
  */
  public void setPeriodDoorsOpen(int t){
    this.periodDoorsOpen = t;
  }
  /**
    * Sets the time when the lift starts to open its doors.
    * @param t
    * The new time.
  */
  public void setDebDoorsOpen(int t){
    this.debDoorsOpen = t;
  }
  /**
    * Sets the time when the lift closes its doors.
    * @param t
    * The new time.
  */
  public void setEndDoorsOpen(int t){
    this.endDoorsOpen = t;
  }

  //METHODES

  /**
    * Returns a string representation of the lift.
    * @return String
  */
  @Override
  public String toString(){
    return "L'ascenceur " + this.id + " est a l'etage " + this.currentFloor + " au moment " + this.actTime + "\nIl y a : " + this.passengers +"\n";
  }

  /**
    * Updates the direction of the lift.
    * @param p
    * The next person the lift has to collect.
  */
  public void updateDirection(Person p){
    //if there are passengers in the lift
    if(!this.getPassengers().isEmpty()){
      int d = this.getPassengers().get(0).direction();
      if(d == 1) this.setDirection(Direction.up);
      else if(d == -1) this.setDirection(Direction.down);
      else this.setDirection(Direction.still);
    }
    //If there are no more passengers on the lift but it is requested by a person.
    else if(p != null){   // CA FONCTIONNE CA OU J'AI PAS LE DROIT ???
      int d = p.getStartFloor() - this.getCurrentFloor();
      if(d > 0) this.setDirection(Direction.up);
      else if(d < 0) this.setDirection(Direction.down);
      else this.setDirection(Direction.still);
    }
    // The lift is empty and is no longer requested
    else{
      System.out.println("RIEN A CHANGER");
    }

    /*
    int d = 0;
    if(!this.getPassengers().isEmpty()) d = this.getPassengers().get(0).direction();
    else if(p != null) d = p.getStartFloor() - this.getCurrentFloor();
    else System.out.println("RIEN A CHANGER");

    if(d > 0) this.setDirection(Direction.up);
    else if(d < 0) this.setDirection(Direction.down);
    else this.setDirection(Direction.still);
    */
  }
  /**
    * Adds the end floor of a person to the list of the next floors to visit.
    * @param p
    * The person to add.
  */
  public void addNextFloor(Person p){
    System.out.println("On est dans addNextFloor");
    LinkedList<LinkedList<Integer>> tmp;
    LinkedList<Integer> up, down;
    int e = p.getEndFloor();
    //The list is empty --> we have to create two new lists inside the first one and then we can add our person.
    if(this.getNextFloor() == null){
      tmp = new LinkedList<LinkedList<Integer>>();
      up = new LinkedList<Integer>();
      down = new LinkedList<Integer>();
      tmp.add(up);
      tmp.add(down);
      if(p.direction() == 1){
        tmp.getFirst().add(e);
      }
      else if(p.direction() == -1){
        tmp.getLast().add(e);
      }
    }
    //The list isn't empty so just add our person where they belong.
    else{
      tmp = new LinkedList<LinkedList<Integer>>(this.getNextFloor());
      int i = 0;
      if(p.direction() == 1){
        System.out.println("On va rentrer sort la 1ere sous liste");
        //getFirst et get lance NoSuchElementException si la liste est vide.
        //Attention parfois e peut etre plus grand que le dernier element et donc avec get(i), on essaye d'acceder a un element qui n'existe pas. POur contourner, on utlise un iterator.
        tmp.getFirst().add(e);
        List<Integer> l = tmp.getFirst().stream()
                                        .sorted(Comparator.comparing(Integer::valueOf))
                                        .collect(Collectors.toList());
        up = new LinkedList<Integer>(l);
        tmp.set(0,up);
        System.out.println(tmp.getFirst());
      }
      else if(p.direction() == -1){
        System.out.println("On va rentrer sort la 2eme sous liste");
        tmp.getLast().add(e);
        List<Integer> l = tmp.getLast().stream()
                                        .sorted(Comparator.reverseOrder())
                                        .collect(Collectors.toList());
        down = new LinkedList<Integer>(l);
        tmp.set(0,down);
        System.out.println(tmp.getLast());
      }
    }
    //In every case we have to set the new list of lists.
    this.setNextFloor(tmp);
  }
  /**
    * Removes the end floor of a person to the list of the next floors to visit.
    * @param p
    * The person to remove.
  */
  public void removeNextFloor(Person p){
    LinkedList<LinkedList<Integer>> tmp;
    LinkedList<Integer> up, down;
    int e = p.getEndFloor();
    //The list is empty --> we cannot remove anything since there is nothing
    if(this.getNextFloor() == null){
      System.out.println("ON NE PEUT RIEN RETIRER CAR IL N'Y A RIEN.");
    }
    //The list isn't empty so just remove our person.
    else{
      tmp = new LinkedList<LinkedList<Integer>>(this.getNextFloor());
      if(p.direction() == 1){
        //The sub-list is empty --> we cannot remove anything since there is nothing
        if(tmp.getFirst() == null){
          System.out.println("ON NE PEUT RIEN RETIRER CAR IL N'Y A RIEN.");
        }
        //The sub-list isn't empty so just remove our person.
        else{
          int i = 0;
          while(e != tmp.getFirst().get(i)){
            i++;
          }
          tmp.getFirst().remove(i);
        }
      }
      else if(p.direction() == -1){
        //The sub-list is empty --> we cannot remove anything since there is nothing
        if(tmp.getFirst() == null){
            System.out.println("ON NE PEUT RIEN RETIRER CAR IL N'Y A RIEN.");
        }
        //The sub-list isn't empty so just remove our person.
        else{
          int i = 0;
          while(e != tmp.getLast().get(i)){
            i++;
          }
          tmp.getLast().remove(i);
        }
      }
      this.setNextFloor(tmp);
    }
  }

  /**
    * return the amount of time a lift is going to need at one floor.
    * @param stay
    * the amount of <code>Person</code> who stay in the lift.
    * @param out
    * the amount of <code>Person</code> who are going out of the lift.
    * @param in
    * the amount of <code>Person</code> who are going in the lift.
    * @return int
  */
  public int computeTime(int stay, int out, int in){
    int val = Math.max(out,in);
    return val;
  }
  /**
    * Deals with all the events that can happen when the doors of a lift are open.
    * It resets the activity time of the lift and his direction.
    * If it returns a list of <code>Person</code>, it means that the lift was crowded and they will have to wait for another one.
    * @param out The list of <code>Person</code> who are going out of the lift.
    * @param in The list of <code>Person</code> who are going in the lift.
    * @param startTime The moment when the lift starts to open its doors.
    * @return ArrayList&lt;Person&gt;
    * @see Person
  */
  public ArrayList<Person> peopleOutIn(ArrayList<Person> out, ArrayList<Person> in, int startTime){
    ArrayList<Person> tmp;
    if(this.getPassengers() == null){
      tmp = new ArrayList<Person>();
    }
    else{
      tmp = new ArrayList<Person>(this.getPassengers());
    }
    // Les personnes descendent de l'ascenceur
    if(!out.isEmpty()){
      for(Person p : out){
        if(tmp.contains(p)){
          tmp.remove(p);
        }
      }
    }
    // Les personnes qui restent dans l'ascenceur (celles d'origines moins celles qui sortent)
    ArrayList<Person> stay = new ArrayList<Person>(tmp);
    // Les personnes montent dans l'ascenceur
    if(!in.isEmpty()){
      for(Person p : in){
        if(tmp.size()<capacity){
          tmp.add(p);
        }
      }
    }
    for(Person p : tmp){
      if(in.contains(p)){
        in.remove(p);
      }
    }
    this.setPassengers(tmp);  // On actualise la liste des passagers dans l'ascenceur
    int t = computeTime(stay.size(),out.size(),in.size());
    this.setActTime(this.getActTime()+t);
    this.setDoorsOpen(true);
    this.setPeriodDoorsOpen(t);
    this.setDebDoorsOpen(startTime);
    this.setEndDoorsOpen(startTime + t);

    return in;
  }
  /**
    * The lift doesn't do anything (load,unload or move). The time just passes by...
    * It increases the activity time of the lift by 1 time unity.
  */
  public void stay(){
    this.setActTime(this.getActTime()+1);
  }
  /**
    * The lift moves to a floor.
    * It sets the <code>currentFloor</code> of the lift to the floor passed in parameter.
    * It updates the activity time of the lift (the amount of time is proportionnal to the number of floor it has to travel).
    * @param nextFloor
    * The floor towards which the lift is heading.
  */
  public void move(int nextFloor){
    int tmp = Math.abs(this.getCurrentFloor() - nextFloor);
    this.setActTime(this.getActTime() + tmp*speed);
    this.setCurrentFloor(nextFloor);
  }

  public static void main(String[] args){
    //A garder pour Test JUnit
    Lift l0 = new Lift();
    Lift l1 = new Lift();
    System.out.println(l0);
    System.out.println(l1);

    //Test PersonIn
    System.out.println("On charge qqun dans l'ascenceur 1");
    Person p = new Person(0,3,4,6);
    System.out.println(p);
    ArrayList<Person> in = new ArrayList<Person>();
    ArrayList<Person> out = new ArrayList<Person>();
    in.add(p);
    l1.peopleOutIn(out,in,3);
    System.out.println(l1);
  }

}
