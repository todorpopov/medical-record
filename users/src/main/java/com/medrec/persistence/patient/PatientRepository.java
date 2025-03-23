package com.medrec.persistence.patient;

import com.medrec.persistence.DBUtils;
import com.medrec.persistence.ICrudRepository;
import com.medrec.persistence.ResponseMessage;
import com.medrec.persistence.doctor.Doctor;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PatientRepository implements ICrudRepository<Patient> {
    private static PatientRepository instance;

    private final Logger logger = Logger.getLogger(PatientRepository.class.getName());

    private PatientRepository() {}

    public static PatientRepository getInstance() {
        if (instance == null) {
            instance = new PatientRepository();
        }

        return instance;
    }

    @Override
    public ResponseMessage save(Patient patient) {
        try (Session session = DBUtils.getCurrentSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(patient);
            tx.commit();

            this.logger.info(String.format("Patient %s saved successfully", patient.toString()));
            return new ResponseMessage(
                    true,
                    String.format("%s %s created successfully!", patient.getFirstName(), patient.getLastName())
            );
        } catch (Exception e) {
            this.logger.severe(String.format("Patient %s save failed", patient.toString()));
            return new ResponseMessage(false, e.getMessage());
        }
    }

    @Override
    public Patient findById(int id) {
        Patient patient = null;
        try (Session session = DBUtils.getCurrentSession()) {
            Transaction tx = session.beginTransaction();
            patient = session.get(Patient.class, id);
            tx.commit();

            this.logger.info(String.format("Patient %s found", patient.toString()));
        } catch (Exception e) {
            this.logger.severe(String.format("Patient %s findById failed", id));
        }
        return patient;
    }

    public Patient findByEmail(String email) {
        Patient patient = null;
        try (Session session = DBUtils.getCurrentSession()) {
            Transaction tx = session.beginTransaction();
            String hql = "from Doctor where email = :email";
            Query query = session.createQuery("from Patient where email = :email")
                .setParameter("email", email);
            patient = (Patient) query.uniqueResult();
            tx.commit();

            this.logger.info(String.format("Doctor %s found", patient.toString()));
        } catch (Exception e) {
            this.logger.severe(String.format("Doctor %s findByEmail failed", email));
        }
        return patient;
    }

    @Override
    public List<Patient> findAll() {
        List<Patient> patients = new ArrayList<>();
        try (Session session = DBUtils.getCurrentSession()) {
            Transaction tx = session.beginTransaction();
            patients = session.createQuery("from Patient", Patient.class).getResultList();
            tx.commit();
        } catch (Exception e) {
            this.logger.severe(String.format("Patients findAll failed: %s", e.getMessage()));
        }

        return patients;
    }
    @Override
    public ResponseMessage update(Patient patient) {
        try (Session session = DBUtils.getCurrentSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(patient);
            tx.commit();

            this.logger.info(String.format("Patient %s updated successfully", patient.toString()));
            return new ResponseMessage(
                    true,
                    String.format("%s %s updated successfully!", patient.getFirstName(), patient.getLastName())
            );
        } catch (Exception e) {
            this.logger.severe(String.format("Patient %s update failed", patient.toString()));
            return new ResponseMessage(false, e.getMessage());
        }
    }

    @Override
    public ResponseMessage delete(int id) {
        try (Session session = DBUtils.getCurrentSession()) {
            Transaction tx = session.beginTransaction();
            Patient patient = session.get(Patient.class, id);
            session.remove(patient);
            tx.commit();

            this.logger.info(String.format("Patient %s deleted successfully", patient.toString()));
            return new ResponseMessage(
                    true,
                    String.format("%s %s deleted successfully!", patient.getFirstName(), patient.getLastName())
            );
        } catch (Exception e) {
            this.logger.severe(String.format("Patient %s delete failed", id));
            return new ResponseMessage(false, e.getMessage());
        }
    }
}
