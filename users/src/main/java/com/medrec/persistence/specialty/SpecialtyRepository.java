package com.medrec.persistence.specialty;

import com.medrec.exception_handling.exceptions.*;
import com.medrec.persistence.DBUtils;
import com.medrec.persistence.ICrudRepository;
import com.medrec.persistence.ResponseMessage;
import jakarta.persistence.EntityExistsException;
import org.hibernate.*;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;
import java.util.logging.Logger;

public class SpecialtyRepository implements ICrudRepository<Specialty> {
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
    public ResponseMessage save(Specialty specialty) throws RuntimeException {
        try (Session session = DBUtils.getCurrentSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(specialty);
            tx.commit();

            this.logger.info(String.format("Specialty %s saved successfully", specialty.toString()));
            return new ResponseMessage(
                    true,
                    String.format("%s created successfully!", specialty.getSpecialtyName())
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

    public int saveGetId(Specialty specialty) throws RuntimeException {
        try (Session session = DBUtils.getCurrentSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(specialty);
            tx.commit();

            this.logger.info(String.format("Specialty %s saved successfully", specialty.toString()));
            return specialty.getId();
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
        try (Session session = DBUtils.getCurrentSession()) {
            Transaction tx = session.beginTransaction();
            Specialty specialty = session.get(Specialty.class, id);
            tx.commit();

            this.logger.info(String.format("Specialty %s found", specialty.toString()));
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
        try (Session session = DBUtils.getCurrentSession()) {
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
    public ResponseMessage update(Specialty specialty) throws RuntimeException {
        try (Session session = DBUtils.getCurrentSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(specialty);
            tx.commit();

            this.logger.info(String.format("Specialty %s updated successfully", specialty.toString()));
            return new ResponseMessage(
                    true,
                    String.format("%s updated successfully!", specialty.getSpecialtyName())
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
            Specialty specialty = session.get(Specialty.class, id);
            session.remove(specialty);
            tx.commit();

            this.logger.info(String.format("Specialty %s deleted successfully", specialty.toString()));
            return new ResponseMessage(
                    true,
                    String.format("%s deleted successfully!", specialty.getSpecialtyName())
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
}
