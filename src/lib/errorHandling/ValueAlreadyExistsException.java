package lib.errorHandling;

/**
 * Created by Алекс on 04.03.2017.
 */
public class ValueAlreadyExistsException extends Exception {
    public ValueAlreadyExistsException(String s){
        super(s);
    }
    public ValueAlreadyExistsException(){
        super();
    }
}
