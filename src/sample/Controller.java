package sample;

import com.jcraft.jsch.*;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Window;

import java.io.*;

public class Controller {
//    @FXML
//    private Button run;

    @FXML
    TextField Nxp, Nyp, Nzp, writeRestartFile, recordFile, domainSize, radiusLengthD, Py, Pz, totalTimeSteps, re, dt, maxIterationVelocity, maxIterationPressure, maxResidualVelocity, maxResidualPressure;
    @FXML
    TextField relaxationFactorVelocity, relaxationFactorPressure, inlet1, inlet1u, inlet1v, inlet1w, inlet21, inlet2u, inlet2v, inlet2w, inlet3, inlet3u, inlet3v, inlet3w, cs, alpha;
    @FXML
    TextField lp11, lp12, lp13, lp14, lp21, lp22, lp23, lp24, lp31, lp32, lp33, lp34, startMotion, ampX, ampY, ampZ, freqX, freqY, freqZ;
    @FXML
    TextField cKy, cDy, cFy, totalSnapshots, startSnapshots, dumpSnapshotInterval, suctionChoice1, suctionChoice2, suctionChoice3;
    @FXML
    TextField suctionVelocity, suctionFreq, suctionPhase, eJ1, eJ2, eJ3, eJ4;
    @FXML
    TextArea outputTA;
    @FXML
    ProgressBar progress;
    @FXML
    ChoiceBox startOption, turbulenceModel, motionChoice, snapshotChoice;


    @FXML
    public  void initialize() {
        startOption.setItems(FXCollections.observableArrayList("New Case","Restart"));
        startOption.getSelectionModel().selectFirst();

        turbulenceModel.setItems(FXCollections.observableArrayList("No Model","Spalart Almaras"));
        turbulenceModel.getSelectionModel().selectFirst();

        motionChoice.setItems(FXCollections.observableArrayList("No","Yes"));
        motionChoice.getSelectionModel().selectFirst();

        snapshotChoice.setItems(FXCollections.observableArrayList("No","Yes"));
        snapshotChoice.getSelectionModel().selectFirst();
    }


    @FXML
    private void runAll() {

        WritetoIOFile();
//        cfdSC();
//outputTA.setText("df");

//        showAlert(Alert.AlertType.CONFIRMATION, "", "Registration Successful!", Nyp.getText());
    }

    //String[] commands = {"ls","cd mpicheck","mpirun ranker.x"};
    String commands = "cd circle2d;cd inputoutputfiles;mpirun -np 4 ../bin/main3D.exe";
//    commands=new String(new byte[4],0,1);

    private void cfdSC() {
        Task task = new Task<Void>() {
            @Override
            public Void call() {

                try {
                    JSch jsch = new JSch();

                    //                    Session session = jsch.getSession("junaid.ali", "111.68.97.5", 2299);
//                    session.setPassword("ceme@123");


                    Session session = jsch.getSession("dpl", "localhost", 22);
                    session.setPassword("dpl");

                    java.util.Properties config = new java.util.Properties();
                    config.put("StrictHostKeyChecking", "no");
                    session.setConfig(config);
                    session.connect();


                    Channel channel = session.openChannel("exec");

                    ((ChannelExec) channel).setCommand(commands);

                    channel.setInputStream(null);
                    ((ChannelExec) channel).setErrStream(System.err);
                    InputStream in = channel.getInputStream();


                    channel.connect();

                    int startzero = 0;
//
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//                    String output;
//
//                    while ((output = reader.readLine()) != null) {
//                        System.out.println(output);
//                        if (output.contains("Time Step")) {
//                            startzero++;
//                            updateProgress(startzero, 250);
//                        }
////                            outputTA.setText(output);
//                    }
//                    reader.close();


//                    String line = "";
//                    byte[] buffer = new byte[1024];
//                    while (true) {
//                        while (in.available() > 0) {
//                            int i = in.read(buffer, 0, 1024);
//                            if (i < 0) {
//                                break;
//                            }
//                            line = new String(buffer, 0, i);
//                            System.out.println(line);
//                            if (line.contains("Time Step")) {
//                                startzero++;
//                                updateProgress(startzero, 250);
//                            }
//                        }
//                    }


                    byte[] tmp = new byte[1024];
                    String line = "";
                    String outputTAS = "";

                    while (true) {
                        while (in.available() > 0) {
                            int i = in.read(tmp, 0, 1024);
                            if (i < 0)
                                break;
//                                System.out.print(new String(tmp, 0, i));
//                                outputTA.setText(new String(tmp, 0, i));
                            line = new String(tmp, 0, i);
                            System.out.println(line);
                            outputTA.appendText(line);

//                                showAlert(Alert.AlertType.CONFIRMATION,"","O","P");

                            if (line.contains("Time Step")) {
                                startzero++;
                                updateProgress(startzero, 250);


                            }

//                            if (line.contains("*******************************************************************")) {
//outputTAS.concat(line.);


//                                outputTA.setText(outputTAS);
//                            }

                        }
                        if (channel.isClosed()
                        ) {
                            System.out.println("exit:" + channel.getExitStatus());
                            break;
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                    }


                    channel.disconnect();


                    session.disconnect();

//            ChannelSftp channelSftp = (ChannelSftp) channel;
//                    channelSftp.cd;
//            File f = new File("outp.txt");

//            channelSftp.put(new FileInputStream(f), f.getName());

//                    f.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }

               /* final int max=10000;
                for (int i=1;i<=max;i++) {
                    updateProgress(i,max);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }*/
                return null;
            }
        };

        progress.progressProperty().bind(task.progressProperty());
        new Thread(task).start();

        /*
         */
    }


    private void showAlert(Alert.AlertType alertType, String owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
//        alert.initOwner(owner);
        alert.show();
    }


    public void WritetoIOFile() {
        try {
            FileWriter fileWriter = new FileWriter("input.dat");
            BufferedWriter io = new BufferedWriter(fileWriter);

            io.write("Nxp Nyp Nzp (r theta z)\n");
            io.write(Nxp.getText() + "," + Nyp.getText() + "," + Nzp.getText() + "\n");
            io.write("Start Option (0:New Case,1:Restart), Write Restart file, Record file\n");
            io.write(startOption.getSelectionModel().getSelectedIndex()+"\n");
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
