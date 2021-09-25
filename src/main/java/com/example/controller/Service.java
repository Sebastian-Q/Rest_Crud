package com.example.controller;

import com.example.dataBase.ConnectionMysql;
import com.example.model.Employee;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;



@Path("/employee")
public class Service{
    Connection con;
    PreparedStatement pstm;
    Statement statement;
    ResultSet rs;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Employee> getEmployees(){
        List<Employee> employees = new ArrayList<>();
        try{
            con = ConnectionMysql.getConnection();
            String query = "SELECT employeeNumber, lastName, firstName, extension, email, officeCode, reportsTo, jobTitle FROM employees";
            statement = con.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()){
                Employee empleado = new Employee();
                empleado.setEmployeeNumber(rs.getInt("employeeNumber"));
                empleado.setLastName(rs.getString("lastName"));
                empleado.setFirstName(rs.getString("firstName"));
                empleado.setExtension(rs.getString("extension"));
                empleado.setEmail(rs.getString("email"));
                empleado.setOfficeCode(rs.getInt("officeCode"));
                empleado.setReportsTo(rs.getInt("reportsTo"));
                empleado.setJobTitle(rs.getString("jobTitle"));
                employees.add(empleado);
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }finally {
            closeConnection();
        }
        return employees;
    }
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Employee getEmployees(@PathParam("id") int employeeNumber){
        Employee empleado = new Employee();
        try{
            con = ConnectionMysql.getConnection();
            String query = "SELECT employeeNumber, lastName, firstName, extension, email, officeCode, reportsTo, jobTitle FROM employees WHERE employeeNumber=?";
            pstm = con.prepareStatement(query);
            pstm.setInt(1,employeeNumber);
            rs = pstm.executeQuery();
            if (rs.next()){
                empleado.setEmployeeNumber(rs.getInt("employeeNumber"));
                empleado.setLastName(rs.getString("lastName"));
                empleado.setFirstName(rs.getString("firstName"));
                empleado.setExtension(rs.getString("extension"));
                empleado.setEmail(rs.getString("email"));
                empleado.setOfficeCode(rs.getInt("officeCode"));
                empleado.setReportsTo(rs.getInt("reportsTo"));
                empleado.setJobTitle(rs.getString("jobTitle"));
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }finally {
            closeConnection();
        }
        return empleado;
    }

    @POST
    @Path("/{employeeNumber}/{lastName}/{firstName}/{extension}/{email}/{officeCode}/{reportsTo}/{jobTitle}")
    @Produces(MediaType.APPLICATION_JSON)
    public String create(@PathParam("employeeNumber") int employeeNumber, @PathParam("lastName") String lastName, @PathParam("firstName") String firstName,
                          @PathParam("extension") String extension, @PathParam("email") String email, @PathParam("officeCode") int officeCode,
                          @PathParam("reportsTo") int reportsTo, @PathParam("jobTitle") String jobTitle){
        String state = "No entro";
        try{
            con = ConnectionMysql.getConnection();
            String query = "INSERT INTO employees (employeeNumber, lastName, firstName, extension, email, officeCode, reportsTo, jobTitle) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            pstm = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstm.setInt(1,employeeNumber);
            pstm.setString(2,lastName);
            pstm.setString(3, firstName);
            pstm.setString(4, extension);
            pstm.setString(5, email);
            pstm.setInt(6, officeCode);
            pstm.setInt(7, reportsTo);
            pstm.setString(8, jobTitle);
            if (pstm.executeUpdate() == 1){
                state = "REGISTRO EXITOSO";
            }else{
                state = "ERROR :(";
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }finally {
            closeConnection();
        }
        return state;
    }

    @PUT
    @Path("/{employeeNumber}/{lastName}/{firstName}/{extension}/{email}/{officeCode}/{reportsTo}/{jobTitle}")
    @Produces (MediaType.APPLICATION_JSON)
    public String update(@PathParam("employeeNumber") int employeeNumber, @PathParam("lastName") String lastName, @PathParam("firstName") String firstName,
                          @PathParam("extension") String extension, @PathParam("email") String email, @PathParam("officeCode") int officeCode,
                          @PathParam("reportsTo") int reportsTo, @PathParam("jobTitle") String jobTitle){
        String state ="NAH";
        try{
            con = ConnectionMysql.getConnection();
            String query = "UPDATE employees SET lastName = ?, firstName = ?, extension = ?, email = ?, officeCode = ?, reportsTo = ?, jobTitle = ? WHERE employeeNumber = ?";
            pstm = con.prepareStatement(query);
            pstm.setString(1,lastName);
            pstm.setString(2, firstName);
            pstm.setString(3, extension);
            pstm.setString(4, email);
            pstm.setInt(5, officeCode);
            pstm.setInt(6, reportsTo);
            pstm.setString(7, jobTitle);
            pstm.setInt(8, employeeNumber);
            if (pstm.executeUpdate() == 1){
                state = "ACTUALIZACION EXITOSA";
            }else{
                state = "FALLO :V";
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }finally {
            closeConnection();
        }
        return state;
    }

    @DELETE
    @Path("/{employeeNumber}")
    @Produces (MediaType.APPLICATION_JSON)
    public String delete(@PathParam("employeeNumber") int employeeNumber){
        String state = "MENOS";
        try{
            con = ConnectionMysql.getConnection();
            String query = "DELETE FROM employees WHERE employeeNumber = ?";
            pstm = con.prepareStatement(query);
            pstm.setInt(1,employeeNumber);
            if (pstm.executeUpdate() == 1){
                state = "ELIMINACION DE EMPLEADO";
            }else{
                state = "NO SE ELIMINO XD";
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }finally {
            closeConnection();
        }
        return state;
    }



    public void closeConnection(){
        try {
            if (con != null){
                con.close();
            }
            if (pstm != null){
                pstm.close();
            }
            if (rs != null){
                rs.close();
            }
        }catch (SQLException ex){
            ex.printStackTrace();

        }
    }

}
