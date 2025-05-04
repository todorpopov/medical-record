package com.medrec.persistence.doctor;

import com.medrec.dtos.CreateDoctorSpecialtyIdDTO;
import com.medrec.exception_handling.exceptions.*;
import com.medrec.persistence.DBUtils;
import com.medrec.persistence.ICrudRepository;
import com.medrec.persistence.ResponseMessage;
import com.medrec.persistence.specialty.Specialty;
import com.medrec.persistence.specialty.SpecialtyRepository;
import jakarta.persistence.EntityExistsException;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DoctorRepository implements ICrudRepository<Doctor> {
    private static DoctorRepository instance;

    private final SpecialtyRepository specialtyRepository = SpecialtyRepository.getInstance();

    private final Logger logger = Logger.getLogger(DoctorRepository.class.getName());

    private DoctorRepository() {}

    public static DoctorRepository getInstance() {
        if (instance == null) {
            instance = new DoctorRepository();
        }

        return instance;
    }

    @Override
    public ResponseMessage save(Doctor doctor) throws RuntimeException {
        try (Session session = DBUtils.getCurrentSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(doctor);
            tx.commit();

            this.logger.info(String.format("Doctor %s saved successfully", doctor.toString()));
            return new ResponseMessage(
                    true,
                    String.format("%s %s created successfully!", doctor.getFirstName(), doctor.getLastName())
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

    public ResponseMessage saveWithSpecialtyId(CreateDoctorSpecialtyIdDTO dto) throws RuntimeException {
        try {
            Specialty specialty = specialtyRepository.findById(dto.getSpecialtyId());

            if (specialty == null) {
                this.logger.warning(String.format("Specialty with id %s not found", dto.getSpecialtyId()));
                throw new NotFoundException("");
            }

            Session session = DBUtils.getCurrentSession();
            Transaction tx = session.beginTransaction();
            Doctor doctor = dto.createDoctorWithSpecialty(specialty);
            session.persist(doctor);
            tx.commit();

            this.logger.info(String.format("Doctor %s saved successfully", doctor.toString()));
            return new ResponseMessage(
                true,
                String.format("%s %s created successfully!", doctor.getFirstName(), doctor.getLastName())
            );
        } catch (ExceptionInInitializerError e) {
            this.logger.severe("Exception found in database connection initialization: " + e.getMessage());
            throw new DatabaseConnectionException("Exception found in database connection initialization!");
        } catch (NotFoundException e) {
            throw new NotFoundException("specialty_not_found");
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
    public Doctor findById(int id) throws RuntimeException {
        try (Session session = DBUtils.getCurrentSession()) {
            Transaction tx = session.beginTransaction();
            Doctor doctor = session.get(Doctor.class, id);
            tx.commit();

            this.logger.info(String.format("Doctor %s found", doctor.toString()));
            return doctor;
        } catch (ExceptionInInitializerError e) {
            this.logger.severe("Exception found in database connection initialization: " + e.getMessage());
            throw new DatabaseConnectionException("Exception found in database connection initialization!");
        } catch (ObjectNotFoundException e) {
            this.logger.severe("Doctor with id + " + id + " not found: " + e.getMessage());
            throw new NotFoundException("Not found exception");
        } catch (HibernateException e) {
            this.logger.severe("Database exception found: " + e.getMessage());
            throw new DatabaseException("Database exception found");
        }
    }

    public Doctor findByEmail(String email) throws RuntimeException {
        try (Session session = DBUtils.getCurrentSession()) {
            Transaction tx = session.beginTransaction();
            String hql = "from Doctor where email = :email";
            Query query = session.createQuery("from Doctor where email = :email")
                .setParameter("email", email);
            Doctor doctor = (Doctor) query.uniqueResult();
            tx.commit();

            this.logger.info(String.format("Doctor with email %s found", email));
            return doctor;
        } catch (ExceptionInInitializerError e) {
            this.logger.severe("Exception found in database connection initialization: " + e.getMessage());
            throw new DatabaseConnectionException("Exception found in database connection initialization!");
        } catch (ObjectNotFoundException e) {
            this.logger.severe("Doctor with email + " + email + " not found: " + e.getMessage());
            throw new NotFoundException("Not found exception");
        } catch (HibernateException e) {
            this.logger.severe("Database exception found: " + e.getMessage());
            throw new DatabaseException("Database exception found");
        }
    }

    @Override
    public List<Doctor> findAll() throws RuntimeException {
        try (Session session = DBUtils.getCurrentSession()) {
            Transaction tx = session.beginTransaction();
            List<Doctor> doctors = session.createQuery("from Doctor", Doctor.class).getResultList();
            tx.commit();
            return doctors;
        } catch (ExceptionInInitializerError e) {
            this.logger.severe("Exception found in database connection initialization: " + e.getMessage());
            throw new DatabaseConnectionException("Exception found in database connection initialization!");
        } catch (HibernateException e) {
            this.logger.severe("Database exception found: " + e.getMessage());
            throw new DatabaseException("Database exception found");
        }
    }

    @Override
    public ResponseMessage update(Doctor doctor) throws RuntimeException {
        try (Session session = DBUtils.getCurrentSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(doctor);
            tx.commit();

            this.logger.info(String.format("Doctor %s updated successfully", doctor.toString()));
            return new ResponseMessage(
                    true,
                    String.format("%s %s updated successfully!", doctor.getFirstName(), doctor.getLastName())
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
            Doctor doctor = session.get(Doctor.class, id);
            session.remove(doctor);
            tx.commit();

            this.logger.info(String.format("Doctor %s deleted successfully", doctor.toString()));
            return new ResponseMessage(
                    true,
                    String.format("%s %s deleted successfully!", doctor.getFirstName(), doctor.getLastName())
            );
        } catch (ExceptionInInitializerError e) {
            this.logger.severe("Exception found in database connection initialization: " + e.getMessage());
            throw new DatabaseConnectionException("Exception found in database connection initialization!");
        } catch (ObjectNotFoundException e) {
            this.logger.severe(String.format("Specialty with id + %s not found", id));
            throw new NotFoundException("Not found exception");
        } catch (ConstraintViolationException e) {
            this.logger.severe("Constraint exception: " + e.getMessage());
            throw new ConstrainException("Constraint exception");
        } catch (HibernateException e) {
            this.logger.severe("Database exception found: " + e.getMessage());
            throw new DatabaseException("Database exception found");
        }
    }

    public List<Doctor> findAllGpDoctors() throws RuntimeException {
        try (Session session = DBUtils.getCurrentSession()) {
            Transaction tx = session.beginTransaction();
            List<Doctor> doctors = session.createQuery("from Doctor where isGp=true", Doctor.class).getResultList();
            tx.commit();
            return doctors;
        } catch (ExceptionInInitializerError e) {
            this.logger.severe("Exception found in database connection initialization: " + e.getMessage());
            throw new DatabaseConnectionException("Exception found in database connection initialization!");
        } catch (HibernateException e) {
            this.logger.severe("Database exception found: " + e.getMessage());
            throw new DatabaseException("Database exception found");
        }
    }
}
