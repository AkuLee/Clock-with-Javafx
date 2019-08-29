import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Calendar;

/**
 *         // graphicscontext.strokeArc(v, v1, v2, v3, v4, v5, v6);
 *             x - the X coordinate of the arc.
 *             y - the Y coordinate of the arc.
 *             w - the width of the arc.
 *             h - the height of the arc.
 *             startAngle - the starting angle of the arc in degrees.
 *             arcExtent - arcExtent the angular extent of the arc in degrees.
 */
public class Clock extends Application {

    // #252E3D, #404259, #848196
    // #59C9A5, #5B6C5D, #3B2C35 green-ish
    // #03256C, #2541B2, #1768AC, #06BEE1 blue
    // #13293D, #006494, #247BA0, #1B98E0 blue
    // #264653, #2A9D8F, #E9C46A yellow-ish

    private static Color bg = Color.rgb(5, 10, 25);
    private static Color hrColor = Color.web("#264653");
    private static Color minColor = Color.web("#2A9D8F");
    private static Color secColor = Color.web("#E9C46A");

    private int width = 500, height = 500;
    private GraphicsContext context;

    @Override
    public void start(Stage stage) throws Exception {

        Canvas canvas = new Canvas(width, height);
        context = canvas.getGraphicsContext2D();
        canvas.setRotate(-90);

        AnimationTimer timer = new AnimationTimer() {

            boolean first = true;
            SegDisplay[] clock = new SegDisplay[6];

            @Override
            public void handle(long l) {

                if (first) {
                    for (int i = 0; i < 6; i++) {
                        clock[i] = new SegDisplay();
                    }
                    first = false;
                }

                context.clearRect(0, 0, 500, 500);
                Calendar time = Calendar.getInstance();
                updateCircles(time);
                updateBgTime(time, clock);
            }
        };
        timer.start();

        Pane pane = new Pane(canvas);

        Scene root = new Scene(pane);

        stage.setResizable(false);
        stage.setScene(root);
        stage.setTitle("Clock");
        stage.show();
    }

    public void updateBgTime(Calendar time, SegDisplay[] clock) {
        int hour = time.get(Calendar.HOUR);
        int min = time.get(Calendar.MINUTE);
        int sec = time.get(Calendar.SECOND);

        int firstHr = hour/10, sndHr = hour%10,
                firstMin = min/10, sndMin = min%10,
                firstSec = sec/10, sndSec = sec%10;

        clock[0].setSegments(firstHr);
        clock[1].setSegments(sndHr);
        clock[2].setSegments(firstMin);
        clock[3].setSegments(sndMin);
        clock[4].setSegments(firstSec);
        clock[5].setSegments(sndSec);

        int dots = 0;
        for (int i = 0; i < clock.length; i++) {

            if (i == 2) { // Display two dots
                dots++;
                displayTwoDots(200, 115 + i * 40);
            } else if (i == 4) {
                dots++;
                displayTwoDots(200, 115 + i * 40 + 15);
            }

            displaySegment(clock[i].getSegments(), 200, 115 + i*40 + dots*15);
        }

        int am_pm = time.get(Calendar.AM_PM);

        context.setLineWidth(1);
        context.setFont(Font.font(null, 20));
        context.fillText(am_pm == 0 ? "AM" : "PM", 221, 400);
    }

    public void displayTwoDots(int x, int y) {
        context.setFill(Color.ANTIQUEWHITE);
        context.fillOval(x + 20, y, 5, 5);
        context.fillOval(x + 40, y, 5, 5);
    }

    public void displaySegment(boolean[] seg, int x, int y) {

        // 5 on short and 20 on long, 10 on gaps
        context.setStroke(Color.ANTIQUEWHITE);
        context.setLineWidth(5);

        // a
        if (seg[0]) {
            context.strokeLine(x + 65, y + 25, x + 65, y + 5);
        }

        // b
        if (seg[1]) {
            context.strokeLine(x + 40, y + 30, x + 60, y + 30);
        }

        // c
        if (seg[2]) {
            context.strokeLine(x + 10, y + 30, x + 30, y + 30);
        }

        // d
        if (seg[3]) {
            context.strokeLine(x + 5, y + 5, x + 5, y + 25);
        }

        // e
        if (seg[4]) {
            context.strokeLine(x + 30, y, x + 10, y);
        }

        // f
        if (seg[5]) {
            context.strokeLine(x + 60, y, x + 40, y);
        }

        // g
        if (seg[6]) {
            context.strokeLine(x + 35, y + 5, x + 35, y + 25);
        }
    }

    public void updateCircles(Calendar time) {

        int hrWidth = 400, minWidth = 370, secWidth = 340;

        int hour = time.get(Calendar.HOUR);
        int min = time.get(Calendar.MINUTE);
        int sec = time.get(Calendar.SECOND);

        double hrDegree = Math.ceil(- hour/12.0 * 360.0);
        double minDegree = Math.ceil(- min/60.0 * 360.0);
        double secDegree = Math.ceil(- sec/60.0 * 360.0);

        context.setFill(bg);
        context.fillRect(0,0, 500, 500);

        context.setLineWidth(12);

        // Hour
        context.setStroke(hrColor);
        context.strokeArc((width - hrWidth)/2, (height - hrWidth)/2,
                hrWidth, hrWidth,
                0, hrDegree, ArcType.OPEN);

        // Minute
        context.setStroke(minColor);
        context.strokeArc((width - minWidth)/2, (height - minWidth)/2,
                minWidth, minWidth,
                0, minDegree, ArcType.OPEN);

        // Seconds
        context.setStroke(secColor);
        context.strokeArc((width - secWidth)/2, (height - secWidth)/2,
                secWidth, secWidth,
                0, secDegree, ArcType.OPEN);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
