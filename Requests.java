import java.util.PriorityQueue;
import java.util.Map; //HashMap, TreeMap, LinkedHashMap ??
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.NumberFormatException;
import java.util.Comparator;
import java.lang.Math;
import java.util.Random;

/**
    * <b>The <code>Requests</code> class represents the list of requests.</b>
    * <p>
    * An instance from the class <code>Requests</code> has 2 variables:
    * <ul>
    * <li>A priority queue of <code>Person</code> which represents the requests order by the steps.</li>
    * <li>A map which represents requests on hold which have been affected to a <code>Lift</code>. A key represents the identificator of a <code>Person</code> and its value the identificator of a <code>Lift</code>. </li>
    * <li>A list of all the people who have requests.</li>
    * <li>The total amount of requests.</li>
    * </ul>
    *
    *
    * @author Elise et Emma
    * @version 1.0
*/
public class Requests{
  private ArrayList<Person> allRequests; //GETTER & SETTER
  private PriorityQueue<Person> pq;
  private final int nbRequests;
  private Map<Person, Integer> onHold;

  //CONSTRUCTORS

  /**
    * Constructs an instance of the <code>Requests</code> class.
    * This is the constructor for a random generation of the <code>Person</code>.
    * @param nbMaxPerson
    * The maximal number of <code>Person</code> to create.
    * @param lambda
    * The parameter of the poisson's law used to randomly generate the step of the <code>Person</code>.
    * @param nbFloor
    * The second parameter of the uniform law used to randomly generate the startFloor and endFloor of the <code>Person</code>, the first one is 0 by default.
    * @see Person
  */
  public Requests(int nbMaxPerson, int lambda, int nbFloor){
    //Initialisation des variables
    allRequests = new ArrayList<Person>();
    onHold = new HashMap<Person, Integer>();
    pq = new PriorityQueue<Person>();
    //Creation des requetes aleatoires
    for(int i = 0; i<nbMaxPerson; i++){
      Person p = new Person(i);
      p.setStep(poisson(lambda));
      p.setStartFloor(uniform(0,nbFloor));
      p.setEndFloor(uniform(0,nbFloor));
      pq.add(p);
      allRequests.add(p);
    }
    nbRequests = pq.size();
  }
  /**
    * Constructs an instance of the <code>Requests</code> class.
    * This is the constructor for a file containing all the requests.
    * @param FileName
    * The file containing all the requests that will be changed into several <code>Person</code>, each representing a request.
  */
  public Requests(String FileName){
    onHold = new HashMap<Person, Integer>();
    Comparator<Person> personStepComparator = (p1, p2) -> { return p1.getStep() - p2.getStep(); };
    pq = new PriorityQueue<Person>(personStepComparator);
    allRequests = new ArrayList<Person>();
    BufferedReader file = null;
    try{
      file = new BufferedReader(new FileReader(FileName));
      String currentLine;
      int firstOccurrence, secondOccurrence, thirdOccurrence;
      while((currentLine = file.readLine()) != null){
        //System.out.println(currentLine);
        firstOccurrence = currentLine.indexOf(",");
        secondOccurrence = currentLine.indexOf(",", firstOccurrence + 1);
        thirdOccurrence = currentLine.lastIndexOf(",");
        int id = Integer.parseInt(currentLine.substring(0, firstOccurrence));
        int step = Integer.parseInt(currentLine.substring(firstOccurrence + 2, secondOccurrence));
        int startFloor = Integer.parseInt(currentLine.substring(secondOccurrence + 2, thirdOccurrence));
        int endFloor = Integer.parseInt(currentLine.substring(thirdOccurrence + 2, currentLine.length()));

        pq.add(new Person(id, step, startFloor, endFloor));
        allRequests.add(new Person(id, step, startFloor, endFloor));
      }
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
      nbRequests = pq.size();
    }
  }

  //GETTERS & SETTERS

  /**
    * Returns all the requests of the simulation.
    * @return ArrayList&lt;Person&gt;
    * @see Person
  */
  public ArrayList<Person> getAllRequests(){
    return allRequests;
  }
  /**
    * Returns the priority queue of requests represented by <code>Person</code>.
    * @return PriorityQueue&lt;Person&gt;
    * @see Person
  */
  public PriorityQueue<Person> getPQ(){
    return pq;
  }
  /**
    * Returns the map of requests on hold where each request has been associated with lift.
    * @return Map&lt;Integer,Integer&gt;
  */
  public Map<Person, Integer> getOnHold(){
    return onHold;
  }
  /**
    * Returns te total number of requests.
    * @return int
  */
  public int getNbRequests(){
    return nbRequests;
  }

  /**
    * Set the list of requests.
    * @param ar The new list.
  */
  public void setAllRequests(ArrayList<Person> ar){
    this.allRequests = ar;
  }
  /**
    * Set the priority queue of requests represented by <code>Person</code>.
    * @param pq
    * The new priority queue of <code>Person</code>.
  */
  public void setPQ(PriorityQueue<Person> pq){
    this.pq = pq;
  }
  /**
    * Set the map of requests on hold where each request has been associated with lift.
    * @param r
    * The new map.
  */
  public void setOnHold(Map<Person, Integer> r){
    this.onHold = r;
  }

  //METHODS

  /**
    * Returns a string representation of the request.
    * @return String
  */
  @Override
  public String toString(){
    String s = "The set of requests ordered by priority:\n";
    PriorityQueue<Person> tmp = new PriorityQueue<Person>(pq);
    while(!tmp.isEmpty()){
      s += tmp.remove() + "\n";
    }
    s += "The set of requests on hold that have been attributed to a lift:\n";
    for(Map.Entry<Person, Integer> r : onHold.entrySet()){
      Person p = r.getKey();
      Integer id = r.getValue();
      s += "(" + p + " || " + id + ")\n";
    }
    return s;
  }
  /**
    * Returns the value of a poisson's law for a certain parameter lambda.
    * @param lambda
    * The value put in parameter.
    * @return int
  */
  public static int poisson(double lambda){
    double L = Math.exp(-lambda);
    double p = 1.0;
    int k = 0;
    do {
      k++;
      p *= Math.random();
    } while (p > L);
    return k-1;
  }
  /**
    * Returns a random integer number between two borns passed in parameters.
    * @param min
    * The lower born.
    * @param max
    * The higher born.
    * @return int
  */
  public static int uniform(int min, int max){
    Random r = new Random();
    return min + r.nextInt(max - min);
  }

  /*
  public static void main(String [] args){
    Requests r = new Requests("exemple.txt");
    System.out.println(r.pq); //Affiche la queue dans l'ordre dans lequel les personnes ont ete ajoutees
    while(!r.pq.isEmpty()){
      System.out.println(r.pq.remove());
    }
  }
  */
}
