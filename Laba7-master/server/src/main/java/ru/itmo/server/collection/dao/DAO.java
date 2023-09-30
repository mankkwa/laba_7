package ru.itmo.server.collection.dao;

import ru.itmo.common.User;
import ru.itmo.common.model.HumanBeing;

public interface DAO {
    int add(HumanBeing humanBeing, User user);
    boolean update(HumanBeing humanBeing, User user);
    boolean delete(int index, User user);
    HumanBeing get(int id);
}
