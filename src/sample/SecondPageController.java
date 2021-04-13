package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.commons.math3.distribution.FDistribution;

import java.net.URL;
import java.text.Format;
import java.util.Arrays;
import java.util.ResourceBundle;


public class SecondPageController implements Initializable {

    public static int n;
    public static int m;
    public Label alternatives;
    public Label measurements;

    @FXML
    public Pane p;

//    public GridPane pane = new GridPane();

    public static TextField[][] matrix = new TextField[n][m];

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        alternatives.setText("Broj alternativa: "+m);
        measurements.setText("Broj mjerenja: " + n);

        matrix = new TextField[n][m];
        GridPane pane = new GridPane();
//        pane.setMaxWidth(200);
//        pane.setMaxHeight(200);
        pane.setHgap(10);
        pane.setVgap(10);
//        pane.setGridLinesVisible(true);
        for(int i = 0; i < n; i++)
        {
            matrix[i] = new TextField[m];

            for(int j = 0; j<m ; j++)
            {
                matrix[i][j] = new TextField();
                matrix[i][j].setMaxWidth(80);
                matrix[i][j].relocate(j,i);
                pane.add(matrix[i][j],j,i);

              //  pane.getChildren().add( matrix[i][j]);

            }
        }
        p.getChildren().add(pane);
    }

    public void calculate() {

        double [] colMean = new double[m];
        double totalMean;
        double totalSum = 0;

        // Racunanje y.. i  y.j
        for (int j =0; j<m; j++)
        {
            double sum = 0;
            for(int i=0; i<n; i++)
            {
                sum+=Double.parseDouble(matrix[i][j].getText());
                totalSum+=Double.parseDouble(matrix[i][j].getText());
            }
            System.out.println("y."+j+"="+sum/n);
            colMean[j]=sum/n;
        }

        totalMean=totalSum/(m*n);
        System.out.println("y..="+totalMean);

        // Racunanje SSA i SSE
        double ssa =0;
        for (double v : colMean) {
            ssa += Math.pow(v - totalMean, 2);
        }
        ssa*=n;
        System.out.println("SSA="+ ssa);

        double sse =0;
        for(int j =0; j<m; j++)
        {
            for(int i =0; i<n; i++)
            {
                sse+=Math.pow((Double.parseDouble(matrix[i][j].getText())-colMean[j]),2);
            }
        }
        System.out.println("SSE="+sse);

        // Racunanje F
        double FCalculated = (ssa/(m-1))/(sse/(m*(n-1)));
        System.out.println("Fcalculated= "+ FCalculated);

        FDistribution fDistribution= new FDistribution(m-1,m*(n-1),0.95);

        double FTab = fDistribution.inverseCumulativeProbability(0.95);
        System.out.println("Ftabelarno=" + FTab);
        System.out.println("Sistemi se "+ (FCalculated>FTab ? "razlikuju." : "ne razlikuju.") );
    }
}
