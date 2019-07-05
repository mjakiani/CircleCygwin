package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));


//        Button btn = new Button();
//        btn.setText("Say 'Hello World'");
//        btn.setOnAction(new EventHandler<ActionEvent>() {
//
//            @Override
//            public void handle(ActionEvent event) {
//                System.out.println("Hello World!");
//                WritetoIOFile();

//                byte bWW[] = {12, 34, 67, 78};
//                try {
//                    OutputStream os = new FileOutputStream("outp.txt");
//                    for (int x = 0; x < bWW.length; x++) {
//                        os.write(bWW[x]);
//                    }
//                    os.close();
//                } catch (Exception e) {
//
//                }

             /*   try {
                    JSch jsch = new JSch();
                    Session session = jsch.getSession("dpl", "localhost", 22);
                    session.setPassword("dpl");

                    java.util.Properties config = new java.util.Properties();
                    config.put("StrictHostKeyChecking", "no");
                    session.setConfig(config);
                    session.connect();

                    Channel channel = session.openChannel("sftp");
                    channel.connect();
                    ChannelSftp channelSftp = (ChannelSftp) channel;
//                    channelSftp.cd;
                    File f = new File("outp.txt");

                    channelSftp.put(new FileInputStream(f), f.getName());

//                    f.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }*/


//            }
//        });

//        StackPane root = new StackPane();
//        root.getChildren().add(btn);

        primaryStage.setTitle("Circle 2D CFD");
        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.setMaximized(true);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public void WritetoIOFile() {
        try {
            FileWriter fileWriter=new FileWriter("input.dat");
            BufferedWriter io = new BufferedWriter(fileWriter);

            io.write("Nxp Nyp Nzp (r theta z)\n");
            io.write("\n");
            io.write("Start Option (0:New Case,1:Restart), Write Restart file, Record file\n");
            io.write("\n");
            io.write("Domain size (Radius Length)D\n");
            io.write("\n");
            io.write("Domain Decomposition (Py Pz)\n");
            io.write("\n");
            io.write("Total Time Steps\n");
            io.write("\n");
            io.write("Re dt\n");
            io.write("\n");
            io.write("Max Iteration (vel pressure)\n");
            io.write("\n");
            io.write("Max Residual (vel pressure)\n");
            io.write("\n");
            io.write("Relaxation factor (vel pressure)\n");
            io.write("\n");
            io.write("Inlet Vel with AOA and Gust (u v w)\n");
            io.write("\n");
            io.write("\n");
            io.write("\n");
            io.write("Turbulence Model(0=No model, 1=Spalart Almaras)\n");
            io.write("\n");
            io.write("Cs, alpha\n");
            io.write("\n");
            io.write("Write Output files(TECPLOT LiftDrag LiftDragSection)\n");
            io.write("\n");
            io.write("Location of Probes-Should be in Domain of processor specified\n");
            io.write("\n");
            io.write("\n");
            io.write("\n");
            io.write("MOTION Choice(NO=0 YES=1), Start Motion \n");
            io.write("\n");
            io.write("Amplitude(X,Y,Z)&Freq(X,Y,Z)\n");
            io.write("\n");
            io.write("DYN EQN Coeff (cKy,cDy,cFy)\n");
            io.write("\n");
            io.write("SnapshotChoice(N0=0 YES=1),TotalSnapshots,StartSnapshots,DumpSnapshot Interval\n");
            io.write("\n");
            io.write("SUCTION Choice\n");
            io.write("\n");
            io.write("Suction/Blowing velocity (-ve for suction and +ve for blowing)Vel,Freq,Phase\n");
            io.write("\n");
            io.write("Synthetic Jet Index (sJ1 eJ1 sJ2 eJ2)\n");
            io.write("\n");

            io.close();
        }
        catch (Exception e ){
            e.printStackTrace();
        }
    }
}
