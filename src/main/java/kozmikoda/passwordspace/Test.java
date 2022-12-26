package kozmikoda.passwordspace;

public class Test {

    public static void main(String[] args) {
        try {
            PSQLConnection db = new PSQLConnection();

            MainUserAccount m = new MainUserAccount(db, "admin");

            m.addNewService("netflix", "sertaki", "anan3");
            m.addNewService("youtube", "sertaki", "anan3");
            m.addNewService("facebook", "sertaki", "anan3");


        }catch (Exception e) {
            e.printStackTrace();
        }




    }


    public static void printUser(MainUserAccount m) {
        System.out.printf("%s %s %s %s \n", m.getUserName(), m.getRealName(), m.getEMail(), m.getPhoneNumber()) ;
    }

}
