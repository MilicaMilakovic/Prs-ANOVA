package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Controller {
    @FXML
    public TextField brojMjerenja;
    @FXML
    public TextField brojAlternativa;
    @FXML
    public Button btn;
    public void calculate() throws Exception
    {
        int n=0;
        int m=0;
        try{
            n = Integer.parseInt(brojMjerenja.getText());
            m = Integer.parseInt(brojAlternativa.getText());

            SecondPageController.n = n;
            SecondPageController.m = m;
        }catch (Exception e)
        {
            e.printStackTrace();
        }

//        System.out.println("broj mjerenja:" + n);
//        System.out.println("broj alternativa:" + m);

        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("SecondPage.fxml"));
        primaryStage.setTitle("ANOVA");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();

        Stage stage = (Stage) btn.getScene().getWindow();
        stage.close();
    }

}
