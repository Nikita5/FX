package sample;

import javafx.scene.Scene;
import javafx.scene.control.ListView;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Никита on 16.04.2017.
 */
public class DbHelper {

    public static Connection conectedBd()throws ClassNotFoundException{
        Connection con=null;
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/Parse";
            String login = "postgres";
            String password = "123456";
            con = DriverManager.getConnection(url, login, password);
            //JOptionPane.showMessageDialog(null,"Connection db Parse");
            return con;
        }catch (Exception e){
            e.getMessage();
            JOptionPane.showMessageDialog(null,e);
            return null;
        }
    }




    public void dataBase(){

        try {


        }catch (Exception e){
            e.getMessage();
        }
    }
}
