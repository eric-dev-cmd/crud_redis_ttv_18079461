package com.redis.ttv.repository;

import com.redis.ttv.entity.Employee;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmployeeRepository {

    private HashOperations hashOperations;//crud hash
    private RedisTemplate redisTemplate;
    private ListOperations listOperations;
    private SetOperations setOperations;

    private final static String EMPLOYEE_KEY = "EMPLOYEE";

    public EmployeeRepository(RedisTemplate redisTemplate) {
        this.setOperations = redisTemplate.opsForSet();
        this.hashOperations = redisTemplate.opsForHash();
        this.redisTemplate = redisTemplate;
        this.listOperations = redisTemplate.opsForList();
    }

    public void saveEmployee(Employee employee) {
        listOperations.leftPush("EMPLOYEE", employee);
//        hashOperations.put("EMPLOYEE", employee.getId(), employee);
    }

    public List<Employee> findAll() {
        return listOperations.range("EMPLOYEE", 0, listOperations.size("EMPLOYEE"));
//        return hashOperations.values("EMPLOYEE");
    }

    public Employee findById(Integer id) {

//        return (Employee) hashOperations.get("EMPLOYEE", id);
        List<Employee> employees = this.findAll();
        for (Employee employee : employees) {
            if(employee.getId() == id)
                return employee;
        }
        return null;
    }

    public void update(Employee employee) {
        saveEmployee(employee);
    }

    public void delete(Integer id) {
//        hashOperations.delete("EMPLOYEE", id);
        listOperations.remove("EMPLOYEE", 1, findById(id));
    }
}
