package sample;

import javafx.application.Application;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;


/**
 * Created by Никита on 14.04.2017.
 */
public class Parser extends Application {

    DbHelper db;
    Connection connection=null;
    PreparedStatement preparedStatement=null;
    ResultSet resultSet=null;
    ArrayList<String> listVidosic;
    static Parser parser=null;
    static String url=null;
    static String title=null;
    static String data =null;
    Button enter;
    Button unload;
    public static HashMap<String,String> mapVidosic=new HashMap<String,String>();
    private Scene scene;
    static List<String> hrefVidosic=new ArrayList<String>();
    ListView<String> listView;


    public void handle(ActionEvent event){

    }

    @Override
    public void start(Stage primaryStage) {

        Pane gridPane=new Pane();
        enter=new Button("Enter");
        unload=new Button("Unload");
        enter.setPrefSize(70.0,40.0);

        unload.setAlignment(Pos.BOTTOM_RIGHT);



        listView=new ListView<>();
        listView.setPrefSize(600.0,200.0);
        listView.setItems(FXCollections.observableList(parser.updateList()));
        gridPane.getChildren().addAll(listView,enter,unload);

        scene=new Scene(gridPane,640,480);
        primaryStage.setScene(scene);
        primaryStage.show();

        enter.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {

                String title=listView.getSelectionModel().getSelectedItem();
                String key;
                for (Map.Entry<String, String> entry : mapVidosic.entrySet()) {
                    key=entry.getKey();
                    if(title.hashCode()==key.hashCode()){
                        hrefVidosic.add(entry.getValue());
                        break;
                    }

                }
            }
        });

        unload.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {


                    String text = "https://www.youtube.com"+hrefVidosic;
                    try(FileOutputStream fos=new FileOutputStream("C://test.txt"))
                    {
                        // перевод строки в байты
                        byte[] buffer = text.getBytes();

                        fos.write(buffer, 0, buffer.length);
                    }
                    catch(IOException ex){

                        System.out.println(ex.getMessage());
                    }



            }
        });
    }




    public static void main(String[] args) {
        try {
            parser=new Parser();
            parser.parserMain();
            Application.launch(args);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }





    public Connection goDB()throws ClassNotFoundException{
        db=new DbHelper();
        return db.conectedBd();
    }

    public void parserMain(){

        try {
            Document doc= Jsoup.connect("https://www.youtube.com/watch?v=-l8Ji17d65Y&list=WL&index=4").get();
            Elements h2Elements=doc.getElementsByAttributeValue("class","content-wrapper");

            try {
                parser.connection= parser.goDB();
                int y;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            parser.deleteTableParser();
            h2Elements.forEach(h2Element ->{
                Element aElement=h2Element.child(0);
                url=aElement.attr("href");
                title=aElement.child(0).text();
                data =aElement.child(3).text().replaceAll("[А-Яа-я]", "");

                parser.enterTableParser();
                mapVidosic.put(title,url);
                //articleList.add(new Article(url,title,data));
            });




        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public ArrayList<String> updateList(){

        listVidosic=new ArrayList<String>();
        try {
            String sql="SELECT * FROM public.\"YouTube\"";

            preparedStatement=connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while ( resultSet.next() ){
                listVidosic.add(resultSet.getString("Vidosic"));
               ;
            }


        } catch (Exception e) {

            e.printStackTrace();
        }

        return listVidosic;
    }


    public void enterTableParser(){
            try {
                String sql="insert into public.\"YouTube\" ( " +
                        "\"href\", " +
                        "\"Vidosic\", " +
                        "\"View\") values(?,?,?)";
                preparedStatement=connection.prepareStatement(sql);

                preparedStatement.setString(1,url);
                preparedStatement.setString(2,title);
                preparedStatement.setString(3,data);

                preparedStatement.execute();

            } catch (Exception e) {

                e.printStackTrace();
            }
    }
    public void deleteTableParser(){
        try {
            String sql="TRUNCATE TABLE public.\"YouTube\"  ";
            preparedStatement=connection.prepareStatement(sql);



            preparedStatement.execute();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


}
