package com.medrec.persistence.doctor;

import com.medrec.persistence.DBUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DoctorRepository {
    private static DoctorRepository instance;

    private final Logger logger = Logger.getLogger(DoctorRepository.class.getName());

    private DoctorRepository() {}

    public static DoctorRepository getInstance() {
        if (instance == null) {
            instance = new DoctorRepository();
        }

        return instance;
    }

    public ResponseMessage save(Doctor doctor) {
        Transaction tx = null;

        try (Session session = DBUtils.getCurrentSession()) {
            tx = session.beginTransaction();
            session.persist(doctor);
            tx.commit();

            this.logger.info(String.format("Doctor %s saved successfully", doctor.toString()));
            return new ResponseMessage(true, null);
        } catch (Exception e) {
            this.logger.severe(String.format("Doctor %s save failed", doctor.toString()));
            return new ResponseMessage(false, e.getMessage());
        }
    }

    public List<Doctor> findAll() {
        Transaction tx = null;

        List<Doctor> doctors = new ArrayList<>();
        try (Session session = DBUtils.getCurrentSession()) {
            tx = session.beginTransaction();
            doctors = session.createQuery("from Doctor", Doctor.class).getResultList();
            tx.commit();
        } catch (Exception e) {
            this.logger.severe(String.format("Doctor findAll failed: %s", e.getMessage()));
        }

        return doctors;
    }
}
