package fr.univtln.cniobechoudayer.model;

import fr.univtln.cniobechoudayer.server.exceptions.PersistanceException;

import java.sql.Connection;

/**
 * Created by Cyril on 24/10/2017.
 */
public interface Entity {
    public void persist(Connection connection) throws PersistanceException;

    public void merge(Connection connection) throws PersistanceException;

    public void update(Connection connection) throws PersistanceException;

    public void remove(Connection connection) throws PersistanceException;
}
