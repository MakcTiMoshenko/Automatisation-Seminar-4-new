package org.max.seminar;

import org.hibernate.query.Query;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

public class EmployeeTest extends AbstractTest {

    @Test
    void getEmployees_whenValid_shouldReturn() throws SQLException {
        //given
        String sql = "SELECT * FROM employee";
        Statement stmt = getConnection().createStatement();
        int countTableSize = 0;
        //when
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            System.out.println(rs.getInt(1));
            System.out.println(rs.getString(2));
            System.out.println(rs.getString(3));
            System.out.println(rs.getString(4));
            System.out.println(rs.getString(5));
            System.out.println("\n");
            countTableSize++;
        }
        final Query query = getSession().createSQLQuery(sql).addEntity(EmployeeEntity.class);
        //then
        Assertions.assertEquals(3, countTableSize);
        Assertions.assertEquals(3, query.list().size());
    }

    @ParameterizedTest
    @CsvSource({"1, Менеджер", "2, Главный Менеджер", "3, Главный Технолог", "4, Главный Инженер"})
    void getEmployeeById_whenValid_shouldReturn(int id, String portion) throws SQLException {
        //given
        //when
        final Query query = getSession().createQuery("from " + "EmployeeEntity" + " where employeeId=" + id);
        EmployeeEntity employeeEntity = (EmployeeEntity) query.uniqueResult();
        //then
        System.out.println(employeeEntity.getEmployeeId());
        System.out.println(employeeEntity.getFirstName());
        System.out.println(employeeEntity.getLastName());
        System.out.println(employeeEntity.getPhoneNumber());
        System.out.println(employeeEntity.getPortion());
        Assertions.assertEquals(portion, employeeEntity.getPortion());
    }

    @Test
    void getEngineers() {
        String sql = "SELECT * FROM employee WHERE portion = 'Инженер'";
        final Query query = getSession().createSQLQuery(sql).addEntity(EmployeeEntity.class);
        List<EmployeeEntity> employeeEntityList = query.list();
        Assertions.assertAll(
                () -> Assertions.assertEquals(2, employeeEntityList.size()),
                () -> Assertions.assertEquals("Инженер", employeeEntityList.get(0).getPortion()),
                () -> Assertions.assertEquals("Инженер", employeeEntityList.get(1).getPortion())
        );
    }

}
