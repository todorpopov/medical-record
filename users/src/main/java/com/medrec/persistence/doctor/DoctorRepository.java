package com.medrec.persistence.doctor;

import com.medrec.dtos.CreateDoctorDTO;
import com.medrec.exception_handling.exceptions.*;
import com.medrec.grpc.users.Users;
import com.medrec.persistence.DBUtils;
import com.medrec.persistence.ICrudRepository;
import com.medrec.persistence.specialty.Specialty;
import com.medrec.persistence.specialty.SpecialtyRepository;
import jakarta.persistence.EntityExistsException;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class DoctorRepository implements ICrudRepository<Doctor, CreateDoctorDTO, Users.UpdateDoctorRequest> {
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
    public Doctor save(CreateDoctorDTO dto) throws RuntimeException {
        try {
            Specialty specialty = specialtyRepository.findById(dto.getSpecialtyId());

            Session session = DBUtils.getCurrentSession();
            Transaction tx = session.beginTransaction();
            Doctor doctor = dto.createDoctorWithSpecialty(specialty);
            session.persist(doctor);
            tx.commit();

            this.logger.info("Doctor saved successfully");
            return doctor;
        } catch (ExceptionInInitializerError e) {
            this.logger.severe("Exception found in database connection initialization: " + e.getMessage());
            throw new DatabaseConnectionException("Exception found in database connection initialization!");
        } catch (NotFoundException e) {
            this.logger.severe("Specialty with id + " + dto.getSpecialtyId() + " not found: " + e.getMessage());
            throw e;
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
        try {
            Session session = DBUtils.getCurrentSession();
            Transaction tx = session.beginTransaction();
            Doctor doctor = session.get(Doctor.class, id);
            tx.commit();

            if (doctor == null) {
                throw new NotFoundException("doctor_not_found");
            }

            this.logger.info(String.format("Doctor with id %s found", id));
            return doctor;
        } catch (ExceptionInInitializerError e) {
            this.logger.severe("Exception found in database connection initialization: " + e.getMessage());
            throw new DatabaseConnectionException("Exception found in database connection initialization!");
        } catch (NotFoundException e) {
            this.logger.severe("Doctor with id " + id + " not found: " + e.getMessage());
            throw e;
        } catch (HibernateException e) {
            this.logger.severe("Database exception found: " + e.getMessage());
            throw new DatabaseException("Database exception found");
        }
    }

    public Doctor findByEmail(String email) throws RuntimeException {
        try {
            Session session = DBUtils.getCurrentSession();
            Transaction tx = session.beginTransaction();
            String hql = "from Doctor where email = :email";
            Query query = session.createQuery("from Doctor where email = :email")
                .setParameter("email", email);
            Doctor doctor = (Doctor) query.uniqueResult();
            tx.commit();

            if (doctor == null) {
                throw new NotFoundException("doctor_not_found");
            }

            this.logger.info(String.format("Doctor with email %s found", email));
            return doctor;
        } catch (ExceptionInInitializerError e) {
            this.logger.severe("Exception found in database connection initialization: " + e.getMessage());
            throw new DatabaseConnectionException("Exception found in database connection initialization!");
        } catch (NotFoundException e) {
            this.logger.severe("Doctor with email + " + email + " not found: " + e.getMessage());
            throw e;
        } catch (HibernateException e) {
            this.logger.severe("Database exception found: " + e.getMessage());
            throw new DatabaseException("Database exception found");
        }
    }

    @Override
    public List<Doctor> findAll() throws RuntimeException {
        try {
            Session session = DBUtils.getCurrentSession();
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
    public Doctor update(Users.UpdateDoctorRequest request) throws RuntimeException {
        int id = request.getDoctorId();
        Optional<Doctor> doctorOptional = Optional.ofNullable(findById(id));

        try {
            if (doctorOptional.isEmpty()) {
                throw new NotFoundException("doctor_not_found");
            }

            Doctor doctor = doctorOptional.get();

            if (request.hasFirstName()) {
                doctor.setFirstName(request.getFirstName());
            }

            if (request.hasLastName()) {
                doctor.setLastName(request.getLastName());
            }

            if (request.hasPassword()) {
                doctor.setPassword(request.getPassword());
            }

            if (request.hasIsGp()) {
                doctor.setGp(request.getIsGp());
            }

            if (request.hasSpecialtyId()) {
                Optional<Specialty> optionalSpecialty = Optional.ofNullable(specialtyRepository.findById(request.getSpecialtyId()));
                if (optionalSpecialty.isEmpty()) {
                    throw new NotFoundException("specialty_not_found");
                }
                doctor.setSpecialty(optionalSpecialty.get());
            }

            Session session = DBUtils.getCurrentSession();
            Transaction tx = session.beginTransaction();
            Doctor updatedDoctor = session.merge(doctor);
            tx.commit();

            this.logger.info("Doctor updated successfully");
            return updatedDoctor;
        } catch (ExceptionInInitializerError e) {
            this.logger.severe("Exception found in database connection initialization: " + e.getMessage());
            throw new DatabaseConnectionException("Exception found in database connection initialization!");
        } catch (NotFoundException e) {
            this.logger.severe("Not found: " + e.getMessage());
            throw e;
        } catch (HibernateException e) {
            this.logger.severe("Database exception found: " + e.getMessage());
            throw new DatabaseException("Database exception found");
        }
    }

    @Override
    public void delete(int id) throws RuntimeException {
        try {
            Session session = DBUtils.getCurrentSession();
            Transaction tx = session.beginTransaction();
            Doctor doctor = session.get(Doctor.class, id);

            if (doctor == null) {
                throw new NotFoundException("doctor_not_found");
            }

            session.remove(doctor);
            tx.commit();

            this.logger.info(String.format("Doctor %s deleted successfully", doctor.toString()));
        } catch (ExceptionInInitializerError e) {
            this.logger.severe("Exception found in database connection initialization: " + e.getMessage());
            throw new DatabaseConnectionException("Exception found in database connection initialization!");
        } catch (NotFoundException e) {
            this.logger.severe(String.format("Specialty with id + %s not found", id));
            throw e;
        } catch (ConstraintViolationException e) {
            this.logger.severe("Constraint exception: " + e.getMessage());
            throw new ConstrainException("Constraint exception");
        } catch (HibernateException e) {
            this.logger.severe("Database exception found: " + e.getMessage());
            throw new DatabaseException("Database exception found");
        }
    }

    public List<Doctor> findAllGpDoctors() throws RuntimeException {
        try {
            Session session = DBUtils.getCurrentSession();
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
