package com.medrec.persistence.doctor;

import com.medrec.persistence.DBUtils;
import com.medrec.persistence.ICrudRepository;
import com.medrec.persistence.ResponseMessage;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DoctorRepository implements ICrudRepository<Doctor> {
    private static DoctorRepository instance;

    private final Logger logger = Logger.getLogger(DoctorRepository.class.getName());

    private DoctorRepository() {}

    public static DoctorRepository getInstance() {
        if (instance == null) {
            instance = new DoctorRepository();
        }

        return instance;
    }

    @Override
    public ResponseMessage save(Doctor doctor) {
        try (Session session = DBUtils.getCurrentSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(doctor);
            tx.commit();

            this.logger.info(String.format("Doctor %s saved successfully", doctor.toString()));
            return new ResponseMessage(
                    true,
                    String.format("%s %s created successfully!", doctor.getFirstName(), doctor.getLastName())
            );
        } catch (Exception e) {
            this.logger.severe(String.format("Doctor %s save failed", doctor.toString()));
            return new ResponseMessage(false, e.getMessage());
        }
    }

    @Override
    public Doctor findById(int id) {
        Doctor doctor = null;
        try (Session session = DBUtils.getCurrentSession()) {
            Transaction tx = session.beginTransaction();
            doctor = session.get(Doctor.class, id);
            tx.commit();

            this.logger.info(String.format("Doctor %s found", doctor.toString()));
        } catch (Exception e) {
            this.logger.severe(String.format("Doctor %s findById failed", id));
        }
        return doctor;
    }

    @Override
    public List<Doctor> findAll() {
        List<Doctor> doctors = new ArrayList<>();
        try (Session session = DBUtils.getCurrentSession()) {
            Transaction tx = session.beginTransaction();
            doctors = session.createQuery("from Doctor", Doctor.class).getResultList();
            tx.commit();
        } catch (Exception e) {
            this.logger.severe(String.format("Doctor findAll failed: %s", e.getMessage()));
        }

        return doctors;
    }
    @Override
    public ResponseMessage update(Doctor doctor) {
        try (Session session = DBUtils.getCurrentSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(doctor);
            tx.commit();

            this.logger.info(String.format("Doctor %s updated successfully", doctor.toString()));
            return new ResponseMessage(
                    true,
                    String.format("%s %s updated successfully!", doctor.getFirstName(), doctor.getLastName())
            );
        } catch (Exception e) {
            this.logger.severe(String.format("Doctor %s update failed", doctor.toString()));
            return new ResponseMessage(false, e.getMessage());
        }
    }

    @Override
    public ResponseMessage delete(int id) {
        try (Session session = DBUtils.getCurrentSession()) {
            Transaction tx = session.beginTransaction();
            Doctor doctor = session.get(Doctor.class, id);
            session.remove(doctor);
            tx.commit();

            this.logger.info(String.format("Doctor %s deleted successfully", doctor.toString()));
            return new ResponseMessage(
                    true,
                    String.format("%s %s deleted successfully!", doctor.getFirstName(), doctor.getLastName())
            );
        } catch (Exception e) {
            this.logger.severe(String.format("Doctor %s delete failed", id));
            return new ResponseMessage(false, e.getMessage());
        }
    }
}
