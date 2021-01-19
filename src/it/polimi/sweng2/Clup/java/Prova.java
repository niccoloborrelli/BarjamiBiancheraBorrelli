package it.polimi.sweng2.Clup.java;

import javax.persistence.*;
import java.sql.Time;
import java.util.List;


public class Prova {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myclup");
        EntityManager em = emf.createEntityManager();
        //List<User> u = em.createNamedQuery("User.checkCredentials", User.class).setParameter(1, "Ciccio").setParameter(2, "Graziani").getResultList();
        op1(em);
    }

    public static void op1(EntityManager em){
        em.getTransaction().begin();
        Store store1 = em.find(Store.class, 8);
        for(Department d: store1.getDepartments())
            System.out.println(d.getId().getDepType());
        em.getTransaction().commit();
    }

    public static void op3(EntityManager em){
        em.getTransaction().begin();
        Store store1 = em.find(Store.class, 8);
        DepId depId = new DepId();
        depId.setDepType("Pesce");
        depId.setStoreId(store1.getId());
        Department dep = new Department();
        dep.setDepCapability(20);
        dep.setDepType(depId);
        store1.addDepartment(dep);
        //em.persist(dep);

        em.getTransaction().commit();
    }


    public static void op2(EntityManager em){
        em.getTransaction().begin();

        Store store1 = new Store();
        store1.setAddress("Via Adriano");
        store1.setChain("Esselunga");
        store1.setName("Qualcosa da capire");
        store1.setAverageVisitDuration(Time.valueOf("01:05:00")); //plus 1 hour because of UTC
        store1.setMaxCapability(60);
        store1.setStartingTime(Time.valueOf("09:00:00"));
        store1.setClosureTime(Time.valueOf("21:00:00"));


        em.persist(store1);

        em.getTransaction().commit();
    }
}
