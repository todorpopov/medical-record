package com.medrec.persistence.specialty;

import com.medrec.dtos.CreateSpecialtyDTO;
import com.medrec.exception_handling.exceptions.*;
import com.medrec.grpc.users.Users;
import com.medrec.persistence.DBUtils;
import com.medrec.persistence.ICrudRepository;
import jakarta.persistence.EntityExistsException;
import org.hibernate.*;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class SpecialtyRepository implements ICrudRepository<Specialty, CreateSpecialtyDTO, Users.UpdateSpecialtyRequest> {
    private static SpecialtyRepository instance;

    private final Logger logger = Logger.getLogger(SpecialtyRepository.class.getName());

    private SpecialtyRepository() {}

    public static SpecialtyRepository getInstance() {
        if (instance == null) {
            instance = new SpecialtyRepository();
        }

        return instance;
    }

    @Override
    public Specialty save(CreateSpecialtyDTO dto) throws RuntimeException {
        try {
            Session session = DBUtils.getCurrentSession();
            Transaction tx = session.beginTransaction();
            Specialty specialty = dto.getDomainModel();
            session.persist(specialty);
            tx.commit();

            this.logger.info("Specialty saved successfully");
            return specialty;
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
    public Specialty findById(int id) throws RuntimeException {
        try {
            Session session = DBUtils.getCurrentSession();
            Transaction tx = session.beginTransaction();
            Specialty specialty = session.get(Specialty.class, id);
            tx.commit();

            this.logger.info(String.format("Specialty with id %s found", id));
            return specialty;
        } catch (ExceptionInInitializerError e) {
            this.logger.severe("Exception found in database connection initialization: " + e.getMessage());
            throw new DatabaseConnectionException("Exception found in database connection initialization!");
        } catch (ObjectNotFoundException e) {
            this.logger.severe("Specialty with id + " + id + " not found: " + e.getMessage());
            throw new NotFoundException("Not found exception");
        } catch (HibernateException e) {
            this.logger.severe("Database exception found: " + e.getMessage());
            throw new DatabaseException("Database exception found");
        }
    }

    @Override
    public List<Specialty> findAll() throws RuntimeException {
        try {
            Session session = DBUtils.getCurrentSession();
            Transaction tx = session.beginTransaction();
            List<Specialty> specialties = session.createQuery("from Specialty", Specialty.class).getResultList();
            tx.commit();
            return specialties;
        } catch (ExceptionInInitializerError e) {
            this.logger.severe("Exception found in database connection initialization: " + e.getMessage());
            throw new DatabaseConnectionException("Exception found in database connection initialization!");
        } catch (HibernateException e) {
            this.logger.severe("Database exception found: " + e.getMessage());
            throw new DatabaseException("Database exception found");
        }
    }

    @Override
    public Specialty update(Users.UpdateSpecialtyRequest request) throws RuntimeException {
        int id = request.getId();
        Optional<Specialty> specialtyOptional = Optional.ofNullable(findById(id));

        try {
            if (specialtyOptional.isEmpty()) {
                throw new NotFoundException("specialty_not_found");
            }

            Specialty specialty = specialtyOptional.get();

            if (request.hasSpecialtyName()) {
                specialty.setSpecialtyName(request.getSpecialtyName());
            }

            if (request.hasSpecialtyDescription()) {
                specialty.setSpecialtyDescription(request.getSpecialtyDescription());
            }

            Session session = DBUtils.getCurrentSession();
            Transaction tx = session.beginTransaction();
            Specialty updatedSpecialty = session.merge(specialty);
            tx.commit();

            this.logger.info("Specialty updated successfully");
            return updatedSpecialty;
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
            Specialty specialty = session.get(Specialty.class, id);
            session.remove(specialty);
            tx.commit();

            this.logger.info("Specialty deleted successfully");
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
}
