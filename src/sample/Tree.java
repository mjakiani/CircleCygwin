package sample;

import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class Tree {
    @FXML
    TreeView inputTree;

    @FXML
    public void initialize() {
        TreeItem root = new TreeItem("Inputs");
        TreeItem pre = new TreeItem("Pre-Processing");

        pre.getChildren().add(new TreeItem("Domain"));
        pre.getChildren().add(new TreeItem("Start"));
        root.getChildren().add(pre);

        inputTree.setRoot(root);

    }
}
