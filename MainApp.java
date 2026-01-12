package com.LM10;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class MainApp {
    public static void main(String[] args) {
        // Load Hibernate configuration
        Configuration cfg = new Configuration();
        cfg.configure("config.xml");
        
        // Build session factory
        SessionFactory factory = cfg.buildSessionFactory();
        
        // Open session
        Session ses = factory.openSession();
        Transaction tr = ses.beginTransaction();
        
        // Save the user object
//        Account acc = new Account("A002", 7000.00);
        
        // Delete account with ID "2"
//        Account account = ses.find(Account.class, "2");
//        if (account != null) {
//            account = ses.merge(account); // Attach to session
//            ses.remove(account); // Delete it
//            System.out.println("Removed");
//        }
//
//        // Commit transaction
//        tr.commit();
//        User user = ses.find(User.class, 2);
//        if(user!=null) {
//        	user.setUser("Neymar");
//        	ses.flush(); 
//        	System.out.println("Changed");
//        }
        Address addr = new Address("123 Street", "Mumbai", "MH", "400001");
        User user = new User("Pedri","Barca@123",addr);
        ses.persist(user);
        tr.commit();
        // Close session
        ses.close();
        factory.close();
    }
}
