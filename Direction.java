/**
 * <b>Direction is an enum type.</b>
 * <p>
 * There is 3 different types of direction :
 * <ul>
 * <li>Up</li>
 * <li>Still</li>
 * <li>Down</li>
 * </ul>
 * @author Emma et Elise
 * @version 1.0
 */

public enum Direction{
  up(1), down(-1), still(0);
  /**
    * The value of the direction.
  */
  private int value;

  /**
    * Direction constructor.
    * @param v
    * The value of the direction
  */
  private Direction(int v) {
      this.value = v;
  }
  /**
    * Returns the value of the direction
    * @return int
  */
  public int getValue(){
      return value;
  }
}
