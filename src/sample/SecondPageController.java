package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.TDistribution;

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
    @FXML
    public Label label;

    @FXML
    public Label kontrastiLabel;

    @FXML
    public TextField firstInput;
    @FXML
    public TextField secondInput;
    @FXML
    public Button calculateContrast;
    @FXML
    public  Label contrastResult;

    @FXML
    public Pane p;

    private int numeratorDegOfFreedom;
    private int denominatorDegOfFreedom;
    private double[] alphas = new double[m];
    private double ssa;
    private double sse;
    private double sst;

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

        for(int i = 0; i<m; i++)
        {
            alphas[i] = colMean[i] - totalMean;
        }

        System.out.println("y..="+totalMean);

        // Racunanje SSA i SSE
        //double ssa =0;
        for (double v : colMean) {
            ssa += Math.pow(v - totalMean, 2);
        }
        ssa*=n;

        //double sse =0;
        for(int j =0; j<m; j++)
        {
            for(int i =0; i<n; i++)
            {
                sse+=Math.pow((Double.parseDouble(matrix[i][j].getText())-colMean[j]),2);
            }
        }


         sst = sse+ssa;

        // Racunanje F
        double FCalculated = (ssa/numeratorDegOfFreedom)/(sse/(denominatorDegOfFreedom));

        FDistribution fDistribution= new FDistribution(numeratorDegOfFreedom,denominatorDegOfFreedom);
        double FTab = fDistribution.inverseCumulativeProbability(0.95);


        result.setText("SSA = " + String.format("%.4f",ssa) + " \t " +
                       "SSE = " + String.format("%.4f",sse) + " \t\t " +
                       "SST = " + String.format("%.4f",sst) + "  \n" +
                       "k-1 = " + numeratorDegOfFreedom + " \t\t " +
                        "k(n-1) =" +denominatorDegOfFreedom + " \t\t " +
                        "kn-1 = " + (numeratorDegOfFreedom+denominatorDegOfFreedom) + "  \n" +
                        "Fizracunato = " + String.format("%.2f",FCalculated) + " \t\t\t\t " +
                        "Ftabelarno = " + String.format("%.2f",FTab) + " \t ");

        label.setText("Sa nivoom povjerenja od 95% zakljucuje se da "+ (FCalculated > FTab ? "postoji " : "ne postoji") + "\nstatisticki znacajna razlika izmedju sistema.");

        kontrastiLabel.setText("\t\t Kontrasti \n Unesite zeljeni par alternativa: ");
        kontrastiLabel.setVisible(true);
        firstInput.setVisible(true);
        secondInput.setVisible(true);
        calculateContrast.setVisible(true);
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
        firstInput.setText("");
        secondInput.setText("");
        contrastResult.setText("");
    }

    public void contrast()
    {
        int i = Integer.parseInt(firstInput.getText());
        int j = Integer.parseInt(secondInput.getText());

        //kontrast
        double c = alphas[i] - alphas[j];
        System.out.println("c= " +c);
        double seSqrd = sse/denominatorDegOfFreedom;

        // sumarna devijacija
        double sc = Math.sqrt(seSqrd)*Math.sqrt(2.0/(n*m));
        System.out.println("se = " + Math.sqrt(seSqrd));
        System.out.println("sc ="+sc);
        double c1;
        double c2;

        TDistribution tDistribution = new TDistribution(denominatorDegOfFreedom,0.95);
        double t = tDistribution.inverseCumulativeProbability(0.95);
        double m = tDistribution.cumulativeProbability(denominatorDegOfFreedom);

        System.out.println("t = "+t);
        System.out.println(m);

        c1 = c - t*sc;
        c2 = c + t*sc;

        contrastResult.setText(" c = " + String.format("%.4f",c) + "\t s = " + String.format("%.4f",sc) +" \t t = "+ String.format("%.3f",t)
                                + "\n 95% : (c1,c2) = (" + String.format("%.4f",c1) + ","
                                                    + String.format("%.4f",c2) + ")"
                                + "\n Buduci da dobijeni interval povjerenja \n"
                                + ((c1<=0 && c2>=0) ? " ukljucuje" : " ne ukljucuje ") + " nulu, izmedju sistema "+i+" i " +j
                                + ((c1<=0 && c2>=0) ? " \n ne postoji " : " \npostoji ") + " statisticki znacajna razlika." );




    }
}
