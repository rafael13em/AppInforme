/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appinformes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author rafae
 */
public class AppInformes extends Application {

    public static Connection conexion = null;

    @Override
    public void start(Stage primaryStage) {
        //establecemos la conexión con la BD
        conectaBD();
        //Creamos la escena
        TextField txt1 = new TextField();
        Button btn = new Button("Enviar");
        HBox param = new HBox();
        param.getChildren().addAll(txt1, btn);
        param.setVisible(false);
        
        Menu menu = new Menu("INFORMES");
        MenuItem item1 = new MenuItem("Listado Facturas");
        MenuItem item2 = new MenuItem("Ventas Totales");
        MenuItem item3 = new MenuItem("Factura por Cliente");
        MenuItem item4 = new MenuItem("Listado Facturas con Subinformes");
        menu.getItems().add(item1);
        menu.getItems().add(item2);
        menu.getItems().add(item3);
        menu.getItems().add(item4);
        MenuBar menubar = new MenuBar(menu);
        
        VBox root = new VBox();
        root.getChildren().addAll(menubar, param);

        item1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                generaInforme1();
                System.out.println("Generando informe");

            }
        });

        item2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                generaInforme2();
                System.out.println("Generando informe");

            }
        });
        
        item3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                param.setVisible(true);
            }
        });
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                generaInforme3(txt1);
                param.setVisible(false);
            }
        });
        
        item4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                generaInforme4();
            }
        });
        
        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("Obtener informe");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        try {

            DriverManager.getConnection("jdbc:hsqldb:hsql://localhost;shutdown=true");
        } catch (Exception ex) {
            System.out.println("No se pudo cerrar la conexion a la BD");
        }
    }

    public void conectaBD() {
        //Establecemos conexión con la BD
        String baseDatos = "jdbc:hsqldb:hsql://localhost:9001/xdb1";
        String usuario = "sa";
        String clave = "";
        try {
            Class.forName("org.hsqldb.jdbcDriver").newInstance();
            conexion = DriverManager.getConnection(baseDatos, usuario, clave);
        } catch (ClassNotFoundException cnfe) {
            System.err.println("Fallo al cargar JDBC");
            System.exit(1);
        } catch (SQLException sqle) {
            System.err.println("No se pudo conectar a BD");
            System.exit(1);
        } catch (java.lang.InstantiationException sqlex) {
            System.err.println("Imposible Conectar");
            System.exit(1);
        } catch (Exception ex) {
            System.err.println("Imposible Conectar");
            System.exit(1);
        }
    }

    public void generaInforme1() {

        try {
            JasperReport jr
                    = (JasperReport) JRLoader.loadObject(AppInformes.class.getResource("ListadoFacturas.jasper"));
            
            //Map de parámetros
            //Map parametros = new HashMap();
            //int nproducto = Integer.valueOf(tintro.getText());
            //parametros.put("ParamProducto", nproducto);
            JasperPrint jp = (JasperPrint) JasperFillManager.fillReport(jr, null, conexion);
            JasperViewer.viewReport(jp);
        } catch (JRException ex) {
            System.out.println("Error al recuperar el jasper");
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    
    public void generaInforme2() {

        try {
            JasperReport jr
                    = (JasperReport) JRLoader.loadObject(AppInformes.class.getResource("VentasTotales.jasper"));
            
            //Map de parámetros
            //Map parametros = new HashMap();
            //int nproducto = Integer.valueOf(tintro.getText());
            //parametros.put("ParamProducto", nproducto);
            JasperPrint jp = (JasperPrint) JasperFillManager.fillReport(jr, null, conexion);
            JasperViewer.viewReport(jp);
        } catch (JRException ex) {
            System.out.println("Error al recuperar el jasper");
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    public void generaInforme3(TextField tintro) {

        try {
            JasperReport jr
                    = (JasperReport) JRLoader.loadObject(AppInformes.class.getResource("FacturaPorCliente.jasper"));
            
            //Map de parámetros
            Map parametros = new HashMap();
            int cliente_id = Integer.valueOf(tintro.getText());
            parametros.put("cliente_id", cliente_id);
            JasperPrint jp = (JasperPrint) JasperFillManager.fillReport(jr, parametros, conexion);
            JasperViewer.viewReport(jp);
        } catch (JRException ex) {
            System.out.println("Error al recuperar el jasper");
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    public void generaInforme4() {

        try {
            JasperReport jr
                    = (JasperReport) JRLoader.loadObject(AppInformes.class.getResource("ListadoFacturas_Subinformes.jasper"));
            //Map de parámetros
            
            JasperPrint jp = (JasperPrint) JasperFillManager.fillReport(jr, null, conexion);
            JasperViewer.viewReport(jp);
        } catch (JRException ex) {
            System.out.println("Error al recuperar el jasper");
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
