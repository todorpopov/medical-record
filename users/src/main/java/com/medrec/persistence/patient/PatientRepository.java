package com.medrec.persistence.patient;

import com.medrec.dtos.CreatePatientDoctorIdDTO;
import com.medrec.exception_handling.exceptions.*;
import com.medrec.persistence.DBUtils;
import com.medrec.persistence.ICrudRepository;
import com.medrec.persistence.ResponseMessage;
import com.medrec.persistence.doctor.Doctor;
import com.medrec.persistence.doctor.DoctorRepository;
import jakarta.persistence.EntityExistsException;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;

import java.util.List;
import java.util.logging.Logger;

public class PatientRepository implements ICrudRepository<Patient> {
    private static PatientRepository instance;

    private final DoctorRepository doctorRepository = DoctorRepository.getInstance();

    private final Logger logger = Logger.getLogger(PatientRepository.class.getName());

    private PatientRepository() {}

    public static PatientRepository getInstance() {
        if (instance == null) {
            instance = new PatientRepository();
        }

        return instance;
    }

    @Override
    public ResponseMessage save(Patient patient) throws RuntimeException {
        try (Session session = DBUtils.getCurrentSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(patient);
            tx.commit();

            this.logger.info(String.format("Patient %s saved successfully", patient.toString()));
            return new ResponseMessage(
                    true,
                    String.format("%s %s created successfully!", patient.getFirstName(), patient.getLastName())
            );
        } catch (ExceptionInInitializerError e) {
            this.logger.severe("Exception found in database connection initialization: " + e.getMessage());
            throw new DatabaseConnectionException("Exception found in database connection initialization!");
        } catch (ConstraintViolationException e){
            this.logger.severe("Constraint violation: " + e.getMessage());
            throw new ConstrainException("Constraint exception");
        } catch (EntityExistsException e) {
            this.logger.severe("Entity already exists: " + e.getMessage());
            throw new AlreadyExistsException("Entity already exists");
        } catch (HibernateException e) {
            this.logger.severe("Database exception found: " + e.getMessage());
            throw new DatabaseException("Database exception found");
        }
    }

    public ResponseMessage saveWithDoctorId(CreatePatientDoctorIdDTO dto) throws RuntimeException {
        Doctor doctor = doctorRepository.findById(dto.getGpId());

        if (doctor == null) {
            this.logger.warning(String.format("Doctor %s not found", dto.getGpId()));
            throw new NotFoundException(String.format("Doctor with id %s not found", dto.getGpId()));
        } else if (!doctor.isGp()) {
            this.logger.warning(String.format("Doctor %s is not gp", dto.getGpId()));
            throw new InvalidPropertyException(String.format("Doctor with id %s is not Gp", dto.getGpId()));
        }

        try (Session session = DBUtils.getCurrentSession()) {
            Transaction tx = session.beginTransaction();
            Patient patient = dto.createPatientWithDoctor(doctor);
            session.persist(patient);
            tx.commit();

            this.logger.info(String.format("Patient %s saved successfully", patient.toString()));
            return new ResponseMessage(
                true,
                String.format("%s %s created successfully!", patient.getFirstName(), patient.getLastName())
            );
        } catch (ExceptionInInitializerError e) {
            this.logger.severe("Exception found in database connection initialization: " + e.getMessage());
            throw new DatabaseConnectionException("Exception found in database connection initialization!");
        } catch (ConstraintViolationException e){
            this.logger.severe("Constraint violation: " + e.getMessage());
            throw new ConstrainException("Constraint exception");
        } catch (EntityExistsException e) {
            this.logger.severe("Entity already exists: " + e.getMessage());
            throw new AlreadyExistsException("Entity already exists");
        } catch (HibernateException e) {
            this.logger.severe("Database exception found: " + e.getMessage());
            throw new DatabaseException("Database exception found");
        }
    }

    @Override
    public Patient findById(int id) throws RuntimeException {
        try (Session session = DBUtils.getCurrentSession()) {
            Transaction tx = session.beginTransaction();
            Patient patient = session.get(Patient.class, id);
            tx.commit();

            this.logger.info(String.format("Patient %s found", patient.toString()));
            return patient;
        } catch (ExceptionInInitializerError e) {
            this.logger.severe("Exception found in database connection initialization: " + e.getMessage());
            throw new DatabaseConnectionException("Exception found in database connection initialization!");
        } catch (ObjectNotFoundException e) {
            this.logger.severe("Patient with id + " + id + " not found: " + e.getMessage());
            throw new NotFoundException("Not found exception");
        } catch (HibernateException e) {
            this.logger.severe("Database exception found: " + e.getMessage());
            throw new DatabaseException("Database exception found");
        }
    }

    public Patient findByEmail(String email) throws RuntimeException {
        try (Session session = DBUtils.getCurrentSession()) {
            Transaction tx = session.beginTransaction();
            String hql = "from Doctor where email = :email";
            Query query = session.createQuery("from Patient where email = :email").setParameter("email", email);
            Patient patient = (Patient) query.uniqueResult();
            tx.commit();

            this.logger.info(String.format("Doctor %s found", patient.toString()));
            return patient;
        } catch (ExceptionInInitializerError e) {
            this.logger.severe("Exception found in database connection initialization: " + e.getMessage());
            throw new DatabaseConnectionException("Exception found in database connection initialization!");
        } catch (ObjectNotFoundException e) {
            this.logger.severe("Patient with email + " + email + " not found: " + e.getMessage());
            throw new NotFoundException("Not found exception");
        } catch (HibernateException e) {
            this.logger.severe("Database exception found: " + e.getMessage());
            throw new DatabaseException("Database exception found");
        }
    }

    @Override
    public List<Patient> findAll() throws RuntimeException {
        try (Session session = DBUtils.getCurrentSession()) {
            Transaction tx = session.beginTransaction();
            List<Patient> patients = session.createQuery("from Patient", Patient.class).getResultList();
            tx.commit();
            return patients;
        } catch (ExceptionInInitializerError e) {
            this.logger.severe("Exception found in database connection initialization: " + e.getMessage());
            throw new DatabaseConnectionException("Exception found in database connection initialization!");
        } catch (HibernateException e) {
            this.logger.severe("Database exception found: " + e.getMessage());
            throw new DatabaseException("Database exception found");
        }
    }

    @Override
    public ResponseMessage update(Patient patient) throws RuntimeException {
        try (Session session = DBUtils.getCurrentSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(patient);
            tx.commit();

            this.logger.info(String.format("Patient %s updated successfully", patient.toString()));
            return new ResponseMessage(
                    true,
                    String.format("%s %s updated successfully!", patient.getFirstName(), patient.getLastName())
            );
        } catch (ExceptionInInitializerError e) {
            this.logger.severe("Exception found in database connection initialization: " + e.getMessage());
            throw new DatabaseConnectionException("Exception found in database connection initialization!");
        } catch (HibernateException e) {
            this.logger.severe("Database exception found: " + e.getMessage());
            throw new DatabaseException("Database exception found");
        }
    }

    @Override
    public ResponseMessage delete(int id) throws RuntimeException {
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
        } catch (ExceptionInInitializerError e) {
            this.logger.severe("Exception found in database connection initialization: " + e.getMessage());
            throw new DatabaseConnectionException("Exception found in database connection initialization!");
        } catch (ObjectNotFoundException e) {
            this.logger.severe(String.format("Patient with id + %s not found", id));
            throw new NotFoundException("Not found exception");
        } catch (ConstraintViolationException e) {
            this.logger.severe("Constraint exception: " + e.getMessage());
            throw new ConstrainException("Constraint exception");
        } catch (HibernateException e) {
            this.logger.severe("Database exception found: " + e.getMessage());
            throw new DatabaseException("Database exception found");
        }
    }
}
