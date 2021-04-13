package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.apache.commons.math3.distribution.FDistribution;

import java.net.URL;
import java.util.ResourceBundle;

public class SecondPageController implements Initializable {

    public static int n;
    public static int m;


    @FXML
    public Label alternatives;
    @FXML
    public Label measurements;
    @FXML
    public Label result;
    public Label label;
    @FXML
    public Pane p;

    private int numeratorDegOfFreedom;
    private int denominatorDegOfFreedom;

//    public GridPane pane = new GridPane();

    public static TextField[][] matrix = new TextField[n][m];

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        alternatives.setText("Broj alternativa: "+m);
        measurements.setText("Broj mjerenja: " + n);

        matrix = new TextField[n][m];
        GridPane pane = new GridPane();

        pane.setHgap(10);
        pane.setVgap(10);

        for(int i = 0; i < n; i++)
        {
            matrix[i] = new TextField[m];

            for(int j = 0; j<m ; j++)
            {
                matrix[i][j] = new TextField();
                matrix[i][j].setMaxWidth(80);
                matrix[i][j].relocate(j,i);
                matrix[i][j].setStyle("-fx-background-color: #303841;  -fx-border-color:  #3a4750; -fx-border-width: 1.5; -fx-text-fill: #ffffff;");
                pane.add(matrix[i][j],j,i);

            }
        }
        p.getChildren().add(pane);
    }

    public void calculate() {

        double [] colMean = new double[m];
        double totalMean;
        double totalSum = 0;
        numeratorDegOfFreedom = m-1;
        denominatorDegOfFreedom = m*(n-1);

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

        double sse =0;
        for(int j =0; j<m; j++)
        {
            for(int i =0; i<n; i++)
            {
                sse+=Math.pow((Double.parseDouble(matrix[i][j].getText())-colMean[j]),2);
            }
        }


        double sst = sse+ssa;

        // Racunanje F
        double FCalculated = (ssa/numeratorDegOfFreedom)/(sse/(denominatorDegOfFreedom));

        FDistribution fDistribution= new FDistribution(numeratorDegOfFreedom,denominatorDegOfFreedom);
        double FTab = fDistribution.inverseCumulativeProbability(0.95);

        System.out.println("Ftabelarno=" + FTab);

        result.setText("SSA = " + String.format("%.4f",ssa) + " \t " +
                       "SSE = " + String.format("%.4f",sse) + " \t\t " +
                       "SST = " + String.format("%.4f",sst) + " \t \n" +
                       "k-1 = " + numeratorDegOfFreedom + " \t\t " +
                        "k(n-1) =" +denominatorDegOfFreedom + " \t\t " +
                        "kn-1 = " + (numeratorDegOfFreedom+denominatorDegOfFreedom) + " \t \n" +
                        "Fizracunato = " + String.format("%.2f",FCalculated) + " \t\t\t\t " +
                        "Ftabelarno = " + String.format("%.2f",FTab) + " \t ");
        label.setText("Sistemi se "+ (FCalculated>FTab ? "razlikuju." : "ne razlikuju."));
    }

    public void clear()
    {
        for(int i =0;i<n;i++)
        {
            for(int j =0; j<m; j++)
            {
                matrix[i][j].setText("");
            }
        }
        result.setText("");
        label.setText("");
    }
}
