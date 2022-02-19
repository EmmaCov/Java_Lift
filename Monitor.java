import java.util.List;
import java.util.ArrayList;
import java.util.stream.*;
import java.util.stream.Collectors.*;
import java.util.Comparator;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.NumberFormatException;
import java.lang.Math;

/**
    * <b> The <code>Monitor</code> class represents a centralised system whose decides which lift will handle a request. </b>
    * <p>
    * An instance from <code>Monitor</code> has 7 variables:
    * <ul>
    * <li>A list of all the lifts associated to the monitor.</li>
    * <li>The number of the lift in the list.</li>
    * <li>The maximal floor.</li>
    * <li>The capacity of the lifts.</li>
    * <li>The speed of the lifts.</li>
    * <li>The average waiting time of the people who have a request.</li>
    * <li>The requests</li>
    * </ul>
    * @see Lift
    * @see Person
    * @see Requests
    * @author Emma et Elise
    * @version 1.0
*/
public class Monitor{ //Dispositif de contrôle
  private List<Lift> lifts;
  private int nbLifts;
  private int floorMax;
  private int capacity;
  private int speed;
  private int avgWaitingTime; // = somme waitingTime / nbPassergers
  private Requests requests;

  //CONSTRUCTOR

  /**
    * Constucts an instance of the <code>Monitor</code> class.
    * It reads a file put in parameter and uses the information contained in it to implement its variables.
    * @param ParameterFile The name of the file where the information concernings the parameters are.
    * @param RequestsFile The name of the file where the information concernings the requests are.

  */
  public Monitor(String ParameterFile, String RequestsFile){
    BufferedReader file = null;
    try{
      file = new BufferedReader(new FileReader(ParameterFile));
      String currentLine;
      int i;
      for(i = 1; i <= 4; i++){
        if((currentLine = file.readLine()) != null){
          switch(i){
            case 1: this.nbLifts = Integer.parseInt(currentLine); break;
            case 2: this.floorMax = Integer.parseInt(currentLine); break;
            case 3: this.capacity = Integer.parseInt(currentLine); break;
            case 4: this.speed = Integer.parseInt(currentLine); break;
          }
        }
      }
      this.lifts = new ArrayList<Lift>();
      for(i = 0; i < this.floorMax; i++){
        this.lifts.add(new Lift(this.capacity, this.speed));
      }
      this.avgWaitingTime = 0;
      this.requests = new Requests(RequestsFile);
    }
    catch(FileNotFoundException e){
      System.out.println("The file is not found or does not exist.\nTry to put the file containing all the requests in the same directory as the project.");
      e.printStackTrace();
    }
    catch(IOException e){
      System.out.println("");
      e.printStackTrace();
    }
    catch(NumberFormatException e){
      System.out.println("The file does not respect the mandatory format.");
      e.printStackTrace();
    }
    finally{
      try{
        if(file != null)
          file.close();
      }
      catch(IOException e){
        System.out.println("IOException");
        e.printStackTrace();
      }
    }
  }

  //GETTERS & SETTERS

  /**
    * Returns the list of the lifts used by the monitor.
    * @return List&lt;Lift&gt;
    * @see Lift
  */
  public List<Lift> getLifts(){
    return this.lifts;
  }
  /**
    * Returns the number of lift in the list.
    * @return int
    * @see Lift
  */
  public int getNbLifts(){
    return this.nbLifts;
  }
  /**
    * Returns the maximal floor.
    * @return int
  */
  public int getFloorMax(){
    return this.floorMax;
  }
  /**
    * Returns the capacity of the lifts.
    * @return int
    * @see Lift
  */
  public int getCapacity(){
    return this.capacity;
  }
  /**
    * Returns the speed of the lifts.
    * @return int
    * @see Lift
  */
  public int getSpeed(){
    return this.speed;
  }
  /**
    * Returns the average waiting time of the people who had requests.
    * @return int
    * @see Person
    * @see Requests
  */
  public int getAvgWaitingTime(){
    return this.avgWaitingTime;
  }
  /**
    * Returns the all the requests.
    * @return Requests
    * @see Requests
  */
  public Requests getRequests(){
    return this.requests;
  }

  /**
    * Set the list of lifts of the monitor.
    * @param l
    * The new list of lifts.
    * @see Lift
  */
  public void setLifts(List<Lift> l){
    this.lifts = l;
  }
  /**
    * Set the amount of lifts used in the monitor.
    * @param nbL
    * The new amount of lifts.
    * @see Lift
  */
  public void setNbLifts(int nbL){
    this.nbLifts = nbL;
  }
  /**
    * Set the maximal floor of the monitor.
    * @param fM
    * The new maximal floor.
  */
  public void setFloorMax(int fM){
    this.floorMax = fM;
  }
  /**
    * Set the capacity of the lifts.
    * @param c The new capacity.
    * @see Lift
  */
  public void setCapacity(int c){
    this.capacity = c;
  }
  /**
    * Set the speed of the lifts.
    * @param s The new speed.
    * @see Lift
  */
  public void setSpeed(int s){
    this.speed = s;
  }
  /**
    * Set the average waiting time of the people who had requests.
    * @param aWT The new average waiting time.
    * @see Person
    * @see Requests
  */
  public void setAvgWaitingTime(int aWT){
    this.avgWaitingTime = aWT;
  }
  /**
    * Set the requests.
    * @param r
    * The new requests.
    * @see Requests
  */
  public void setRequests(Requests r){
    this.requests = r;
  }

  //METHODS

  /**
    * Returns a string representation of the monitor.
    * @return String
  */
  @Override
  public String toString(){
    String s = "";
    s += lifts.toString() + requests.toString() + "\nThe average waiting time: " + String.valueOf(avgWaitingTime);
    return s;
  }
  /**
    * Returns the lift corresponding to the identificator equals to the parameter.
    * @param id
    * The identificator of the sought lift.
    * @return <code>Lift</code>
    * @see Lift
  */
  public Lift getLift(int id){
    return lifts.get(id-1);
  }
  /**
    * Returns an integer corresponding to the identificator of the chosen lift that can possibly process the person's request.
    * The lift is chosen at a certain time.
    * It is not the final value as another lift can address the person's request first.
    * With the filter, all the lifts that are in the right direction to the Person's start floor are keeped.
    * With the map, the distance between the person and the lifts kept are computed.
    * @param p
    * The <code>Person</code> that has to be affected to a lift.
    * @return int
  */
  public int getIdChosenLift(Person p){
    int id = this.lifts.stream()
                        .filter(l -> ((l.getCurrentFloor() - p.getStartFloor() > 0 && ((l.getDirection().getValue() == -1) || (l.getDirection().getValue() == 0))
                                      || (l.getCurrentFloor() - p.getStartFloor() < 0 && ((l.getDirection().getValue() == 1) || (l.getDirection().getValue() == 0))))))
                        .map(l -> Math.abs(p.getStartFloor() - l.getCurrentFloor()))
                        .min(Comparator.comparing(Integer::valueOf))
                        .get();
    return id;
  }

  /**
    * Computes the average amount of time a person waits for a lift.
    * @return int
  */
  public float computeAvgWaitingTime(){
    float sum = 0;
    for(Person p : requests.getAllRequests()){
      sum += (float)p.getWaitingTime();
    }
    return sum/requests.getNbRequests();
  }
}
