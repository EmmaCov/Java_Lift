//EST IL POSSIBLE D AVOIR UN UNIQUE FICHIER AVEC TOUS LES IMPORTS ET D IMPORTER SEULEMENT CE FICHIER (COMME EN C) ??
import java.util.List;
import java.util.ArrayList;
import java.util.stream.*;
import java.util.stream.Collectors.*;
import java.util.Map; //HashMap, TreeMap, LinkedHashMap ??
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Scanner;

/**
    * <b>The <code>Simulation</code> class represents a simulation.</b>
    * <p>
    * An instance from the class <code>Simulation</code> has 2 variables:
    * <ul>
    * <li>A monitor that represents a centralised system whose decides which lift will handle a request.</li>
    * <li>A maximum amount of time a simulation lasts. </li>
    * </ul>
    *
    *
    * @author Elise et Emma
    * @version 1.0
*/

public class Simulation{
  private Monitor m;
  private int timeMax;

  //CONSTRUCTOR

  /**
    * Constructs an instance of the <code>Simulation</code> class.
    * @param ParameterFile The file containing the informations for the parameters.
    * @param RequestsFile The file containing the informations for the requests.
    * @param tMax The maximum amount of time a simulation lasts.
  */
  public Simulation(String ParameterFile, String RequestsFile, int tMax){
    this.m = new Monitor(ParameterFile, RequestsFile);
    this.timeMax = tMax;
  }

  //GETTERS & SETTERS

  /**
    * Returns the monitor of the simulation.
    * @return Monitor
    * @see Monitor
  */
  public Monitor getM(){
    return this.m;
  }
  /**
    * Returns the maximal amount of time a simulation have to complete itself.
    * @return int
  */
  public int getTimeMax(){
    return this.timeMax;
  }

  /**
    * Set the monitor of the simulation.
    * @param monitor
    * The new monitor.
  */
  public void setM(Monitor monitor){
    this.m = monitor;
  }
  /**
    * Set the maximal time.
    * @param tM
    * The new maximal time.
  */
  public void setTimeMax(int tM){
    this.timeMax = tM;
  }


  //METHODS
  public float firstPolitic(){
    //GERER LE TEMPS
    int t = 0;
    int currentFloor, idChosenLift = 0;
    Person currentRequest;
    ArrayList<Person> tmpIn, tmpOut;
    System.out.println("tMax = " + this.timeMax);
    while(t < timeMax && (!(m.getRequests().getPQ().isEmpty()) || !(m.getRequests().getOnHold().isEmpty()))){ //ENTRE DANS LE WHILE ALORS QUE T > TIMEMAX
      //There is a request at time t but there's no lift at the start floor of the request.
      do{
        currentRequest = m.getRequests().getPQ().peek();
        boolean existsLiftThatCanProcessRequest = false;
        for(Lift l : m.getLifts()){ //Check if there's no lift at the moment to process the request.
          if(l.getCurrentFloor() == currentRequest.getStartFloor() && l.getPassengers().size() < l.getCapacity() && !l.getDoorsOpen()){
            existsLiftThatCanProcessRequest = true;
            idChosenLift = l.getId();
            break;
          }
        }
        if(!existsLiftThatCanProcessRequest){
          idChosenLift = m.getIdChosenLift(currentRequest);
          m.getRequests().getOnHold().put(currentRequest, idChosenLift); //Add the request in the list of requests on hold
          m.getRequests().setOnHold(m.getRequests().getOnHold());
          m.getRequests().getPQ().remove();
        }
        m.getLift(idChosenLift).updateDirection(currentRequest);
        m.getLift(idChosenLift).addNextFloor(currentRequest);
      } while(m.getRequests().getPQ().peek().getStep() == t);

      for(Lift l : m.getLifts()){
        currentFloor = l.getCurrentFloor();
        if(l.getDoorsOpen()){
          //DES PERSONNES SONT EN TRAIN DE DESCENDRE ET DE MONTER
          //SI QQN VIENT D'ARRIVER A L ETAGE, DOIT ATTENDRE UN AUTRE ascenceur
          //The person arrived after the lift has open its door. She/he has to wait for another lift.
          do{
            currentRequest = m.getRequests().getPQ().peek();
            idChosenLift = m.getIdChosenLift(currentRequest);
            m.getRequests().getOnHold().put(currentRequest, idChosenLift); //Add the request in the list of requests on hold
            m.getRequests().setOnHold(m.getRequests().getOnHold());
            m.getRequests().getPQ().remove();
            m.getLift(idChosenLift).updateDirection(currentRequest);
            m.getLift(idChosenLift).addNextFloor(currentRequest);
          } while(m.getRequests().getPQ().peek().getStep() == t);
          if(l.getEndDoorsOpen() == t) l.setDoorsOpen(false); //The lift closes its doors.
        }
        else{
          //There is a lift stopping at time t.
          //element lance NoSuchElementException si la liste est vide.
          int nFloor = -1;
          if(!l.getNextFloor().get(0).isEmpty()) nFloor = l.getNextFloor().get(0).element();
          else if(!l.getNextFloor().get(1).isEmpty()) nFloor = l.getNextFloor().get(1).element();
          if(currentFloor == nFloor){
            //Some people want to get out of the lift l at time t.
            tmpOut = new ArrayList<Person>();
            for(Person p : l.getPassengers()){
              if(p.getEndFloor() == l.getCurrentFloor()){
                tmpOut.add(p);
                l.removeNextFloor(p);
              }
            }
            //Some people want to get in the lift l at time t.
            tmpIn = new ArrayList<Person>();
            //There is a request at time t at the current floor of the lift l.
            do{
              currentRequest = m.getRequests().getPQ().peek();
              if(currentRequest.getStep() == t && currentRequest.getStartFloor() == currentFloor && t == l.getDebDoorsOpen()){
                //The person arrived at the same time as the lift l.
                tmpIn.add(currentRequest);
                m.getRequests().getAllRequests().get(m.getRequests().getAllRequests().indexOf(currentRequest)).setWaitingTime(0);
                m.getRequests().getPQ().remove();
                l.addNextFloor(currentRequest);
                l.updateDirection(currentRequest);
              }
            } while(m.getRequests().getPQ().peek().getStep() == t);
            //People who were waiting for a lift at the current floor of the lift l are also getting in the lift.
            List<Person> listOnHoldRequestsToProccess = m.getRequests().getOnHold().entrySet().stream()
                                                                                              .filter(e -> e.getValue() == l.getId())
                                                                                              .map(Map.Entry::getKey)
                                                                                              .collect(Collectors.toList());
            for(Person r : listOnHoldRequestsToProccess){
              tmpIn.add(r);
              m.getRequests().getAllRequests().get(m.getRequests().getAllRequests().indexOf(r)).setWaitingTime(t);
              m.getRequests().getOnHold().remove(r);
            }
            l.peopleOutIn(tmpOut, tmpIn, t);
          }
          else l.move(currentFloor + l.getDirection().getValue());
        }
      }
      t++;
      System.out.println("t = " + t);
    }
    System.out.println("Fin du while");
    return m.computeAvgWaitingTime();
  }
  /*public void secondPolitic(){

  }*/

  public static void main(String [] args){
    System.out.println("Start of the simulation!");
    if(args.length == 2){
      System.out.println("Jusqu'a quel pas de temps souhaitez vous faire tourner la simulation ?");
      Scanner sc = new Scanner(System.in);
      int tMax = sc.nextInt();
      Simulation s = new Simulation(args[0], args[1], tMax);
    }
    /*
    else{
      System.out.println("Vous n'avez pas passer de fichier de requetes en parametre.");
      System.out.println("Nous allons generer des requetes aleatoires pour vous.");
      System.out.println("Combien en voulez-vous ?");
      Scanner sc = new Scanner(System.in);
      int nbRequests = sc.nextInt();
      System.out.println("Combien d'etages souhaitez vous dans cette simulation ?");
      sc = new Scanner(System.in);
      int floorMax = sc.nextInt();
      System.out.println("Quel parametre voulez-vous utiliser pour le calcul de la loi de poisson ?");
      sc = new Scanner(System.in);
      int lambda = sc.nextInt();
      Simulation s = new Simulation(args[0], nbRequests, floorMax, lambda, 50);
      System.out.println("Afin d'etre le plus precis possible nous allons faire tourner la simulation autant de fois que vous le souhaitez :");
      sc = new Scanner(System.in);
      int k = sc.nextInt();
    }
    */
    //System.out.println(s.getM().toString());
    System.out.println(s.firstPolitic());
    // RECUPERATION DES INFOS DU FICHIER PARAMETRES.TXT PASSER OBLIGATOIREMENT EN PARAMETRE
  }
}
