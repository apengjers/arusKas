package mpkm;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final SessionFactory SESSION_FACTORY = buatSessionFactory();

    private static SessionFactory buatSessionFactory() {
        return new Configuration().configure().addAnnotatedClass(Transaksi.class).buildSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

    public static void tutup() {
        SESSION_FACTORY.close();
    }
}
