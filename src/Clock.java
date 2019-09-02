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

    private static Color bg = Color.rgb(5, 10, 25); // Background color
    private static Color hrColor = Color.web("#264653"); // Hour arc color
    private static Color minColor = Color.web("#2A9D8F"); // Minute arc color
    private static Color secColor = Color.web("#E9C46A"); // Second arc color

    private int width = 500, height = 500;
    private GraphicsContext context;

    @Override
    public void start(Stage stage) throws Exception {

        Canvas canvas = new Canvas(width, height);
        context = canvas.getGraphicsContext2D();
        canvas.setRotate(-90); // Rotate 90 degrees counter clock wise

        // Change state of animation every other milliseconds
        AnimationTimer timer = new AnimationTimer() {

            boolean first = true; // First time updating (setup)
            SegDisplay[] clock = new SegDisplay[6]; // The clock with 6 7-segment displays

            @Override
            public void handle(long l) {

                // Setup and initialize the 7-seg displays
                if (first) {
                    for (int i = 0; i < 6; i++) {
                        clock[i] = new SegDisplay();
                    }
                    first = false;
                }

                context.clearRect(0, 0, 500, 500); // Clear screen
                Calendar time = Calendar.getInstance(); // Get current time
                updateCircles(time); // Update arcs.
                updateBgTime(time, clock); // Update the clock numbers
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

    /**
     * Method updates the time shown on clock. Clock positioning is adjusted to rotation made in the initial setup.
     * So x and y axis are also rotated 90 degrees to the left.
     * @param time Calendar instance
     * @param clock SegDisplay array of size 6
     */
    public void updateBgTime(Calendar time, SegDisplay[] clock) {

        int hour = time.get(Calendar.HOUR) == 0 ? 12 : time.get(Calendar.HOUR); // If 12, show 12. Else normally show.
        int min = time.get(Calendar.MINUTE); // Current minute
        int sec = time.get(Calendar.SECOND); // Current second

        int firstHr = hour/10, sndHr = hour%10, // First and second digit of hour
                firstMin = min/10, sndMin = min%10, // First and second digit of minute
                firstSec = sec/10, sndSec = sec%10; // First and second digit of second

        // Update
        clock[0].setSegments(firstHr);
        clock[1].setSegments(sndHr);
        clock[2].setSegments(firstMin);
        clock[3].setSegments(sndMin);
        clock[4].setSegments(firstSec);
        clock[5].setSegments(sndSec);

        int dots = 0; // Two-dots after hour and minute
        for (int i = 0; i < clock.length; i++) {

            // Display two dots
            if (i == 2) { // First dot
                dots++;
                displayTwoDots(200, 115 + i * 40);
            } else if (i == 4) { // Second dot
                dots++;
                displayTwoDots(200, 115 + i * 40 + 15); // 15 pixels as adjustments.
            }

            // 40 pixels between numbers and consider spacing from two-dots with 15 pixels
            displaySegment(clock[i].getSegments(), 200, 115 + i*40 + dots*15);
        }

        int am_pm = time.get(Calendar.AM_PM); // AM or PM

        context.setLineWidth(1); // Line width of 1 pixel
        context.setFont(Font.font(null, 20)); // Set letter font size to 20. Default (null) font family.
        context.fillText(am_pm == 0 ? "AM" : "PM", 221, 400); // Write AM or PM depending on am_pm
    }

    /**
     * Method displays two dots. Clock positioning is adjusted to rotation made in the initial setup.
     *      * So x and y axis are also rotated 90 degrees to the left.
     * @param x x position on canvas from 90 degrees counter clock wise
     * @param y y position on canvas from 90 degrees counter clock wise
     */
    public void displayTwoDots(int x, int y) {
        context.setFill(Color.ANTIQUEWHITE);
        context.fillOval(x + 20, y, 5, 5);
        context.fillOval(x + 40, y, 5, 5);
    }

    /**
     * Method displays the 7 segments. Refer to Wikipedia for segment reference.
     * @param seg the 7 segments of a 7-segment
     * @param x the x position on the canvas from 90 degrees counter clock wise
     * @param y the y position on the canvas from 90 degrees counter clock wise
     */
    public void displaySegment(boolean[] seg, int x, int y) {

        context.setStroke(Color.ANTIQUEWHITE);
        context.setLineWidth(5); // 5 pixels

        // 5 on short side and 20 on long side, 10 on gaps

        // a - top
        if (seg[0]) {
            context.strokeLine(x + 65, y + 25, x + 65, y + 5);
        }

        // b - top right
        if (seg[1]) {
            context.strokeLine(x + 40, y + 30, x + 60, y + 30);
        }

        // c - bottom right
        if (seg[2]) {
            context.strokeLine(x + 10, y + 30, x + 30, y + 30);
        }

        // d - bottom
        if (seg[3]) {
            context.strokeLine(x + 5, y + 5, x + 5, y + 25);
        }

        // e - bottom left
        if (seg[4]) {
            context.strokeLine(x + 30, y, x + 10, y);
        }

        // f - top left
        if (seg[5]) {
            context.strokeLine(x + 60, y, x + 40, y);
        }

        // g - middle
        if (seg[6]) {
            context.strokeLine(x + 35, y + 5, x + 35, y + 25);
        }
    }

    /**
     * Method will update the arcs. Each arc represents the current position of each time unit.
     *
     *
     *  *          graphicscontext.strokeArc(v, v1, v2, v3, v4, v5, v6);
     *  *             x - the X coordinate of the arc.
     *  *             y - the Y coordinate of the arc.
     *  *             w - the width of the arc.
     *  *             h - the height of the arc.
     *  *             startAngle - the starting angle of the arc in degrees.
     *  *             arcExtent - arcExtent the angular extent of the arc in degrees.
     *
     *
     * @param time current time, instance of Calendar
     */
    public void updateCircles(Calendar time) {

        // Radius
        int hrRadius = 400, minRadius = 370, secRadius = 340;

        // Current time
        int hour = time.get(Calendar.HOUR);
        int min = time.get(Calendar.MINUTE);
        int sec = time.get(Calendar.SECOND);

        // Position of time units
        double hrDegree = Math.ceil(- hour/12.0 * 360.0);
        double minDegree = Math.ceil(- min/60.0 * 360.0);
        double secDegree = Math.ceil(- sec/60.0 * 360.0);

        // Setup background
        context.setFill(bg);
        context.fillRect(0,0, 500, 500);

        context.setLineWidth(12);

        /*
            graphicscontext.strokeArc(v, v1, v2, v3, v4, v5, v6);
 *             x - the X coordinate of the arc.
 *             y - the Y coordinate of the arc.
 *             w - the width of the arc.
 *             h - the height of the arc.
 *             startAngle - the starting angle of the arc in degrees.
 *             arcExtent - arcExtent the angular extent of the arc in degrees.
         */

        // Update hour arc
        context.setStroke(hrColor);
        context.strokeArc((width - hrRadius)/2, (height - hrRadius)/2,
                hrRadius, hrRadius,
                0, hrDegree, ArcType.OPEN);

        // Update minute arc
        context.setStroke(minColor);
        context.strokeArc((width - minRadius)/2, (height - minRadius)/2,
                minRadius, minRadius,
                0, minDegree, ArcType.OPEN);

        // Update second arc
        context.setStroke(secColor);
        context.strokeArc((width - secRadius)/2, (height - secRadius)/2,
                secRadius, secRadius,
                0, secDegree, ArcType.OPEN);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
