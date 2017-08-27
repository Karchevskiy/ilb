package lib.errorHandling;

/**
 * Created by Алекс on 04.03.2017.
 */
public class ValueAlreadyExistsAndNotEqualsException extends ValueAlreadyExistsException {
    public ValueAlreadyExistsAndNotEqualsException(String s){
        super(s);
    }
    public ValueAlreadyExistsAndNotEqualsException(){
        super();
    }
}
