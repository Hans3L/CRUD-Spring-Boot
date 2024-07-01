package pe.creativity.Restfull.helper;

public class UserAlreadyExistsException  extends RuntimeException{
    public UserAlreadyExistsException(String message){
        super(message);
    }
}
