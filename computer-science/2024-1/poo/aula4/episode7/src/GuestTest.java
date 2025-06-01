import java.util.*;
/***
 * @author Rafael Domingues 
 */
public class GuestTest {
    public static void main(String[] args) {
        Guest firstObject = new Guest();
        firstObject.setName("Rafael");
        firstObject.setLastName("Domingues");

        // Displaying firstObject State
        System.out.println("Name...: " + firstObject.getName());
        System.out.println("Last Name...: " + firstObject.getLastName());

        Guest secondObject = new Guest();
        secondObject.setName("Nicolas");
        secondObject.setLastName("Passos");

        // Displaying secondObject State
        System.out.println("Name...: " + secondObject.getName());
        System.out.println("Last Name...: " + secondObject.getLastName());

        Date date = new Date();
        ArrayList list = new ArrayList();

        Guest francisco = new Guest();
        francisco.setName("Francisco");
        francisco.setLastName("Ribeiro");

        Guest maria = new Guest();
        maria.name = "Maria";
        maria.lastName = "da Silva";
    }
}
