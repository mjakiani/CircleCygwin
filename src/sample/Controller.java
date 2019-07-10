package sample;

import com.intellij.diagnostic.hprof.action.SystemTempFilenameSupplier;
import com.jcraft.jsch.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Window;
import org.apache.http.io.SessionInputBuffer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

//import static org.apache.commons.lang3.StringUtils.isEmpty;

public class Controller {
//    @FXML
//    private Button run;

    @FXML
    TextField Nxp, Nyp, Nzp, writeRestartFile, recordFile, domainSize, radiusLengthD, Py, Pz, totalTimeSteps, re, dt, maxIterationVelocity, maxIterationPressure, maxResidualVelocity, maxResidualPressure;
    @FXML
    TextField relaxationFactorVelocity, relaxationFactorPressure, inlet1, inlet1u, inlet1v, inlet1w, inlet2, inlet2u, inlet2v, inlet2w, inlet3, inlet3u, inlet3v, inlet3w, cs, alpha;
    @FXML
    TextField lp11, lp12, lp13, lp14, lp21, lp22, lp23, lp24, lp31, lp32, lp33, lp34, startMotion, ampX, ampY, ampZ, freqX, freqY, freqZ;
    @FXML
    TextField cKy, cDy, cFy, totalSnapshots, startSnapshots, dumpSnapshotInterval, suctionChoice2, suctionChoice3;
    @FXML
    TextField suctionVelocity, suctionFreq, suctionPhase, sJ1, eJ1, sJ2, eJ2, tecplotstart, tecplotinterval, tecplotstop, tecplotliftdrag, tecplotliftdragsection;
    @FXML
    TextArea outputTA;
    @FXML
    ProgressBar progress;
    @FXML
    ChoiceBox startOption, turbulenceModel, motionChoice, snapshotChoice, suctionChoice;
    @FXML
    ScrollPane mainScrollPane;
    @FXML
    Button runB;

    Properties configFile;
    Task task;


    @FXML
    public void initialize() {
        startOption.setItems(FXCollections.observableArrayList("New Case", "Restart"));
        startOption.getSelectionModel().selectFirst();

        turbulenceModel.setItems(FXCollections.observableArrayList("No Model", "Spalart Almaras"));
        turbulenceModel.getSelectionModel().selectFirst();

        motionChoice.setItems(FXCollections.observableArrayList("No", "Yes"));
        motionChoice.getSelectionModel().selectFirst();

        snapshotChoice.setItems(FXCollections.observableArrayList("No", "Yes"));
        snapshotChoice.getSelectionModel().selectFirst();

        suctionChoice.setItems(FXCollections.observableArrayList("No", "Yes"));
        suctionChoice.getSelectionModel().selectFirst();

        progress.setMaxWidth(Double.MAX_VALUE);
    }


    @FXML
    private void runAll() {
//        showAlert(Alert.AlertType.CONFIRMATION, "", "", String.valueOf(Long.parseLong(totalTimeSteps.getText())));


        if ((task != null) && task.isRunning()) {
            task.cancel();
            runB.setText("Run");
            progress.progressProperty().unbind();
            progress.setProgress(0);

        } else {
            WritetoIOFile();
            mainScrollPane.setVvalue(1.0);
            testF();
        }
//        cfdSC();
//outputTA.setText("df");

//        showAlert(Alert.AlertType.CONFIRMATION, "", "Registration Successful!", Nyp.getText());
    }


    private void readConfigFile() {
        configFile = new Properties();
        String filename = "config.txt";
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            configFile.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void testF() {

        task = new Task<Void>() {
            @Override
            public Void call() {
                try {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            runB.setText("Stop");
                        }
                    });


                    JSch jsch = new JSch();

                    readConfigFile();

                    String user = configFile.getProperty("user", "dpl");
                    String pass = configFile.getProperty("pass", "dpl");
                    String host = configFile.getProperty("host", "localhost");
                    int port = Integer.parseInt(configFile.getProperty("port", "22"));
                    String inputFileDestination = configFile.getProperty("inputFileDestination", "/home/dpl/cfd/inputoutputfiles/input.dat");
                    String tecPlotFile=configFile.getProperty("tecPlotFile","/home/dpl/cfd/inputoutputfiles/TecPlot");

                    outputTA.setText("Transferring files to Super Computer's Compute Node...\n");

                    Session session = jsch.getSession(user, host, port);
                    session.setPassword(pass);

//                    Session session = jsch.getSession("dpl", "localhost", 22);
//                    session.setPassword("dpl");
//
//                    Session session = jsch.getSession("junaid.ali", "10.9.41.100", 22);
//                    session.setPassword("ceme@123");

                    java.util.Properties config = new java.util.Properties();
                    config.put("StrictHostKeyChecking", "no");
                    session.setConfig(config);
                    session.connect();

                    Channel channel2 = session.openChannel("sftp");
                    channel2.connect();
                    ChannelSftp channelSftp = (ChannelSftp) channel2;
                    File f = new File("input.dat");
                    channelSftp.put(new FileInputStream(f),
//                            "/home/dpl/cfd/inputoutputfiles/input.dat"
//                            "/state/partition1/home4/eme/junaid.ali/cfd/inputoutputfiles/input.dat"
                            inputFileDestination
                    );
                    outputTA.setText("Transfer Complete\n\nStarting Simulation\n\n");
//                    channel2.disconnect();


                    ChannelShell channel = (ChannelShell) session.openChannel("shell");
//        PipedInputStream pis = new PipedInputStream();
//        PipedOutputStream pos = new PipedOutputStream();
//        channel.setInputStream(new PipedInputStream(pos));
//        channel.setOutputStream(new PipedOutputStream(pis));


                    channel.connect();
//        pos.write("ls".getBytes(StandardCharsets.UTF_8));
//        pos.write("ssh compute-0-18".getBytes(StandardCharsets.UTF_8));
//
//        pos.flush();
//        System.out.println(pis.read());
//        pis.close();
//        pos.close();

                    OutputStream ops = channel.getOutputStream();
                    PrintStream ps = new PrintStream(ops, true);

                    InputStream in = channel.getInputStream();
                    byte[] bt = new byte[1024];

//                    ps.println("ls");
//                    ps.println("ssh compute-0-18");
//                    ps.println("ls");


//                    ps.println("cd circle2d");
                    ps.println("cd cfd");

                    ps.println("./job");

                    long iterations = 0;
                    long totalIterations = Long.parseLong(totalTimeSteps.getText());
                    String line = "";

                    while (true) {

                        while (in.available() > 0) {
                            int i = in.read(bt, 0, 1024);
                            if (i < 0)
                                break;
                            line = new String(bt, 0, i);
                            //displays the output of the command executed.
                            System.out.println(line);
                            outputTA.appendText(line);

//                                showAlert(Alert.AlertType.CONFIRMATION,"","O","P");

                            if (line.contains("Time Step")) {
                                for (int j = 0; j < countMatches(line, "Time Step"); j++) {
                                    iterations++;
                                }
//                                updateProgress(startzero, 250);
                                updateProgress(iterations, totalIterations);

                                if ((iterations < (totalIterations - 10)) && ((iterations % 7) == 0)) {
                                    outputTA.setText(line);
                                }


                            }

                        }


                        if (line.contains("Simulation Complete")) {
                            channelSftp.get(tecPlotFile,"TecPlot.dat");
                            channel2.disconnect();
                            break;
                        }

//                        if (iterations == totalIterations) {
////                            showInfo("Iteration", "IT");
//
////                            Channel channelExec = session.openChannel("exec");
////
////
////                            ChannelExec exec = (ChannelExec) channelExec;
////                            exec.setCommand("cd cfd;cd inputoutputfiles;cat Plot3D* > TecPlot");
////                            channelExec.connect();
//
////                            ps.println("cd inputoutputfiles");
////                            ps.println("cat Plot3D* > TecPlot");
////                            channelExec.disconnect();
//
//
//
//
//
//                            System.out.println("Iterational Exit");
//                            break;
//                        }


                        if (channel.isClosed()) {
//                            showInfo("Channel", "Closed");
                            System.out.println("Channel Exit");
                            break;
                        }
                        Thread.sleep(1000);

                    }





//                    outputTA.appendText("\n\n\n Simulation Complete");
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            runB.setText("Run");
                        }
                    });

                    channel.disconnect();
                    session.disconnect();
                } catch (Exception e) {

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (task.isCancelled()) {
                                outputTA.appendText("\n\nSimulation was stopped!");

                            }
                            else {
                                outputTA.appendText("\n\n\n\n\nAn error occurred!!!\n\nTry again\n\n");
                            }
                            runB.setText("Run");
                        }
                    });
                    updateProgress(0,0);
                    e.printStackTrace();
                }
                return null;
            }
        };
        progress.progressProperty().bind(task.progressProperty());
        new Thread(task).start();


    }

    //String[] commands = {"ls","cd mpicheck","mpirun ranker.x"};
//    String commands = "cd circle2d;cd inputoutputfiles;mpirun -np 4 ../bin/main3D.exe";
    String commands = "cd cfd;./job";
//    commands=new String(new byte[4],0,1);

    private void cfdSC() {
        Task task = new Task<Void>() {
            @Override
            public Void call() {

                try {
                    JSch jsch = new JSch();

//                    Session session = jsch.getSession("junaid.ali", "10.9.41.100", 22);
//                    session.setPassword("ceme@123");

//                    int forwardedPort = 2222;
//                    session.setPortForwardingL(forwardedPort,"compute-0-18",22);

//                    Session computeSession=jsch.getSession("junaid.ali","localhost",forwardedPort);
//                    java.util.Properties config1 = new java.util.Properties();
//                    config1.put("StrictHostKeyChecking", "no");
//                    computeSession.setConfig(config1);
//                    computeSession.connect();

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
                    long iterations = 0;
                    long totalIterations = Long.parseLong(totalTimeSteps.getText());


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
                                for (int j = 0; j < countMatches(line, "Time Step"); j++) {
                                    iterations++;
                                }
//                                updateProgress(startzero, 250);
                                updateProgress(iterations, totalIterations);

                                if ((iterations < (totalIterations - 10)) && ((iterations % 5) == 0)) {
                                    outputTA.setText(line);
                                }


                            }

//                            if (line.contains("*******************************************************************")) {
//outputTAS.concat(line.);


//                                outputTA.setText(outputTAS);
//                            }

                        }
                        if (channel.isClosed()) {
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


    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
//        alert.initOwner(owner);
        alert.show();
    }

    public static int countMatches(String str, String sub) {
        if ((str != null && str.isEmpty()) || (sub != null && sub.isEmpty())) {
            return 0;
        }
//        if (isEmpty(str) || isEmpty(sub)) {
//            return 0;
//        }
        int count = 0;
        int idx = 0;
        while ((idx = str.indexOf(sub, idx)) != -1) {
            count++;
            idx += sub.length();
        }
        return count;
    }


    public void WritetoIOFile() {
        try {
            FileWriter fileWriter = new FileWriter("input.dat");
            BufferedWriter io = new BufferedWriter(fileWriter);

            io.write("Nxp Nyp Nzp (r theta z)\n");
            io.write(Nxp.getText() + "," + Nyp.getText() + "," + Nzp.getText() + "\n");
            io.write("Start Option (0:New Case,1:Restart), Write Restart file, Record file\n");
            io.write(startOption.getSelectionModel().getSelectedIndex() + "," + writeRestartFile.getText() + "," + recordFile.getText() + "\n");
            io.write("Domain size (Radius Length)D\n");
            io.write(domainSize.getText() + "," + radiusLengthD.getText() + "\n");
            io.write("Domain Decomposition (Py Pz)\n");
            io.write(Py.getText() + "," + Pz.getText() + "\n");
            io.write("Total Time Steps\n");
            io.write(totalTimeSteps.getText() + "\n");
            io.write("Re dt\n");
            io.write(re.getText() + "," + dt.getText() + "\n");
            io.write("Max Iteration (vel pressure)\n");
            io.write(maxIterationVelocity.getText() + "," + maxIterationPressure.getText() + "\n");
            io.write("Max Residual (vel pressure)\n");
            io.write(maxResidualVelocity.getText() + "," + maxResidualPressure.getText() + "\n");
            io.write("Relaxation factor (vel pressure)\n");
            io.write(relaxationFactorVelocity.getText() + "," + relaxationFactorPressure.getText() + "\n");
            io.write("Inlet Vel with AOA and Gust (u v w)\n");
            io.write(inlet1.getText() + "," + inlet1u.getText() + "," + inlet1v.getText() + "," + inlet1w.getText() + "\n");
            io.write(inlet2.getText() + "," + inlet2u.getText() + "," + inlet2v.getText() + "," + inlet2w.getText() + "\n");
            io.write(inlet3.getText() + "," + inlet3u.getText() + "," + inlet3v.getText() + "," + inlet3u.getText() + "\n");
            io.write("Turbulence Model(0=No model, 1=Spalart Almaras)\n");
            io.write(turbulenceModel.getSelectionModel().getSelectedIndex() + "\n");
            io.write("Cs, alpha\n");
            io.write(cs.getText() + "," + alpha.getText() + "\n");
            io.write("Write Output files(TECPLOT LiftDrag LiftDragSection)\n");
            io.write(tecplotstart.getText() + "," + tecplotinterval.getText() + "," + tecplotstop.getText() + "," + tecplotliftdrag.getText() + "," + tecplotliftdragsection.getText() + "\n");
            io.write("Location of Probes-Should be in Domain of processor specified\n");
            io.write(lp11.getText() + "," + lp12.getText() + "," + lp13.getText() + "," + lp14.getText() + "\n");
            io.write(lp21.getText() + "," + lp22.getText() + "," + lp23.getText() + "," + lp24.getText() + "\n");
            io.write(lp31.getText() + "," + lp32.getText() + "," + lp33.getText() + "," + lp34.getText() + "\n");
            io.write("MOTION Choice(NO=0 YES=1), Start Motion \n");
            io.write(motionChoice.getSelectionModel().getSelectedIndex() + "," + startMotion.getText() + "\n");
            io.write("Amplitude(X,Y,Z)&Freq(X,Y,Z)\n");
            io.write(ampX.getText() + "," + ampY.getText() + "," + ampZ.getText() + "," + freqX.getText() + "," + freqY.getText() + "," + freqZ.getText() + "\n");
            io.write("DYN EQN Coeff (cKy,cDy,cFy)\n");
            io.write(cKy.getText() + "," + cDy.getText() + "," + cFy.getText() + "\n");
            io.write("SnapshotChoice(N0=0 YES=1),TotalSnapshots,StartSnapshots,DumpSnapshot Interval\n");
            io.write(snapshotChoice.getSelectionModel().getSelectedIndex() + "," + totalSnapshots.getText() + "," + startSnapshots.getText() + "," + dumpSnapshotInterval.getText() + "\n");
            io.write("SUCTION Choice\n");
            io.write(suctionChoice.getSelectionModel().getSelectedIndex() + "," + suctionChoice2.getText() + "," + suctionChoice3.getText() + "\n");
            io.write("Suction/Blowing velocity (-ve for suction and +ve for blowing)Vel,Freq,Phase\n");
            io.write(suctionVelocity.getText() + "," + suctionFreq.getText() + "," + suctionPhase.getText() + "\n");
            io.write("Synthetic Jet Index (sJ1 eJ1 sJ2 eJ2)\n");
            io.write(sJ1.getText() + "," + eJ1.getText() + "," + sJ2.getText() + "," + eJ2.getText() + "\n");

            io.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
