package cuie.project.template_simplecontrol;

import java.util.ArrayList;
import java.util.Iterator;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;
import javafx.util.converter.NumberStringConverter;

/**
 * Ampel CustomControl
 *
 * @author Dieter Holz
 * @author Tabea Eggler
 * @author Benjamin Huber
 */

public class PowerStation extends Region {
	private static final double ARTBOARD_WIDTH = 100;
	private static final double ARTBOARD_HEIGHT = 100;

	private static final double ASPECT_RATIO = ARTBOARD_WIDTH / ARTBOARD_HEIGHT;

	private static final double MINIMUM_WIDTH = 200;
	private static final double MINIMUM_HEIGHT = MINIMUM_WIDTH / ASPECT_RATIO;

	private static final double MAXIMUM_WIDTH = 800;

	// colors
	private static final Color COLOR_GREY = Color.rgb(221, 219, 219);
	private static final Color COLOR_LIGHTGREY = Color.rgb(243, 243, 243);
	private static final Color COLOR_BLUE = Color.rgb(73, 91, 115);
	private static final Color COLOR_RED = Color.rgb(197, 160, 157);
	private static final Color COLOR_YELLOW = Color.rgb(192, 174, 154);
	private static final Color COLOR_BACK = Color.rgb(51, 51, 51);
	private static final Color COlOR_DISABLED = Color.rgb(85, 85, 85);
	
	private final IntegerProperty leistung1 = new SimpleIntegerProperty();
	private final IntegerProperty leistung2 = new SimpleIntegerProperty();
	private final IntegerProperty leistung3 = new SimpleIntegerProperty();
	private final IntegerProperty leistung4 = new SimpleIntegerProperty();
	private final IntegerProperty anzahlLadepunkte = new SimpleIntegerProperty();
	
	private int totalCalculation = 0;
	private static int TOTAL_HEIGHT_BOXES = 40;
	
	private double sizeBox1 = 7;
	private double sizeBox2 = 7;
	private double sizeBox3 = 7;
	private double sizeBox4 = 7;
	
	private ArrayList<Rectangle> boxes;
	private ArrayList<Integer> leistungen;


	// shapes
	private Rectangle frame;
	private Rectangle socket;
	private Rectangle boxTotal;
	private Rectangle box1;
	private Rectangle box2;
	private Rectangle box3;
	private Rectangle box4;
	private Circle steckertyp1;
	private Circle steckertyp2;
	private Circle steckertyp3; 
	private SVGPath kabel;
	private SVGPath steckergehause;
	private Text total;
	private TextField txtLeistung1;
	private TextField txtLeistung2;
	private TextField txtLeistung3;
	private TextField txtLeistung4;
	private Group svgs;
	private Line elektrode1;
	private Line elektrode2;
	//TODO: Elektroden noch einfügen;

	// properties
	private BooleanProperty stop = new SimpleBooleanProperty();
	private BooleanProperty go = new SimpleBooleanProperty();

	// needed for resizing
	private Pane drawingPane;

	public PowerStation() {
		initializeSelf();
		initializeParts();
		initializeDrawingPane();
		layoutParts();
		setupEventHandlers();
		setupValueChangeListeners();
		setupBinding();
	}

	private void initializeSelf() {
		String fonts = getClass().getResource("/fonts/fonts.css").toExternalForm();
		getStylesheets().add(fonts);

		String stylesheet = getClass().getResource("style.css").toExternalForm();
		getStylesheets().add(stylesheet);

		getStyleClass().add("powerstation");

	}

	private void initializeParts() {
		double centerX =  31.5;
		double centerY = 50-(69/2);
		
		double sizeTextfield = 14;
		double sizeFrame = 69;
		double positionBox1 = centerY+25;
		double positionBox2 = centerY+38;
		double positionBox3 = centerY+49;
		double positionBox4 = 0;


		frame = new Rectangle(centerX, 50-(69/2), 37, sizeFrame);
		frame.getStyleClass().addAll("frame");
		frame.setArcHeight(3);
		frame.setArcWidth(3);
		
		socket = new Rectangle(centerX-((47-37)/2), centerY+65, 47, 4);
		socket.getStyleClass().addAll("socket");
		
		boxTotal = new Rectangle(centerX-((31-37)/2), centerY+3, 31, 19);
		boxTotal.getStyleClass().addAll("boxTotal");
		boxTotal.setArcHeight(2);
		boxTotal.setArcWidth(2);
		
		box1 = new Rectangle(centerX+3, 0, 31, 0);
		box1.getStyleClass().addAll("box1");
		
		box2 = new Rectangle(centerX+3, 0, 31, 0);
		box2.getStyleClass().addAll("box2");
		
		box3 = new Rectangle(centerX+3, 0, 31, 0);
		box3.getStyleClass().addAll("box3");
		
		box4 = new Rectangle(centerX+3, 0, 31, 0);
		box4.getStyleClass().addAll("box2");
		
		boxes = new ArrayList<>();
		leistungen = new ArrayList<>();
		
		steckertyp1 = new Circle(50-(15/2), centerY+31, 2.9);
		steckertyp1.getStyleClass().addAll("steckertyp");
		steckertyp1.setStrokeWidth(0.2); 
		steckertyp1.setStroke(Color.rgb(221, 219, 219));
		steckertyp1.setFill(Color.TRANSPARENT);
		
		steckertyp2 = new Circle(50-(15/2), centerY+43, 2.9);
		steckertyp2.getStyleClass().addAll("steckertyp");
		steckertyp2.setStrokeWidth(0.2); 
		steckertyp2.setStroke(Color.rgb(221, 219, 219));
		steckertyp2.setFill(Color.TRANSPARENT);
		
		steckertyp3 = new Circle(50-(15/2), centerY+57, 2.9);
		steckertyp3.getStyleClass().addAll("steckertyp");
		steckertyp3.setStrokeWidth(0.2); 
		steckertyp3.setStroke(Color.rgb(221, 219, 219));
		steckertyp3.setFill(Color.TRANSPARENT);
		
		total = new Text();
		calculateTotal();
		total.setX(40);
		total.setY(centerY+3+7.8);
		total.setFont(Font.font("Open Sans", FontWeight.NORMAL, 5.5));
		total.setTextAlignment(TextAlignment.CENTER);
		total.setWrappingWidth(20);
		
//		total = new Text("Total: " + "103" + " kW");
//		total.setX(40);
//		total.setY(centerY+3+7.8);
//		total.setFont(Font.font("Open Sans", FontWeight.NORMAL, 5.5));
//		total.setTextAlignment(TextAlignment.CENTER);
//		total.setWrappingWidth(20);
		
		//total.setId("total-text"); <-- ??????
		
		txtLeistung1 = new TextField();
		txtLeistung1.setLayoutX(45);
		txtLeistung1.setLayoutY(positionBox1 + (sizeBox1/2) - (sizeTextfield/2));
		txtLeistung1.getStyleClass().addAll("text-leistung");
		
		txtLeistung2 = new TextField("25 kW");
		txtLeistung2.setLayoutX(45);
		txtLeistung2.setLayoutY(positionBox2 + (sizeBox2/2) - (sizeTextfield/2));
		txtLeistung2.getStyleClass().addAll("text-leistung");
		
		txtLeistung3 = new TextField("48 kW");
		txtLeistung3.setLayoutX(45);
		txtLeistung3.setLayoutY(positionBox3 + (sizeBox3/2) - (sizeTextfield/2));
		txtLeistung3.getStyleClass().addAll("text-leistung");
		
		txtLeistung4 = new TextField("12 KW");
		txtLeistung4.setLayoutX(45);
		txtLeistung4.setLayoutY(positionBox4 + (sizeBox4/2) - (sizeTextfield/2));
		txtLeistung4.getStyleClass().addAll("text-leistung");
		
		kabel = new SVGPath();
		kabel.setContent("M57.1479 47.1146V32.05H59.95V47C59.95 48.5027 59.9493 49.8675 59.5158 50.9924C59.0846 52.1115 58.2227 52.9976 56.4852 53.5352C56.2323 53.6134 55.8438 53.667 55.3787 53.6992C54.9145 53.7314 54.3775 53.7421 53.8292 53.7359C52.7322 53.7235 51.5936 53.6435 50.9079 53.5336C49.3508 53.2838 48.4388 52.4008 47.9137 51.221C47.3871 50.0378 47.25 48.556 47.25 47.1154V32C47.25 31.8793 47.2525 31.747 47.2552 31.6063C47.2671 30.9745 47.2823 30.1728 47.0881 29.4846C46.9688 29.0622 46.7697 28.6775 46.4389 28.3986C46.1075 28.1191 45.6491 27.95 45.0196 27.95H42.05V25.05H45.0196C46.7537 25.05 48.0594 25.4301 48.9321 26.2751C49.8045 27.1199 50.2538 28.4387 50.2538 30.3382L50.2538 47.1154L50.2539 47.1168L50.3038 47.1154L50.2539 47.1169L50.2539 47.117L50.2539 47.1173L50.2539 47.1184L50.254 47.123L50.2546 47.1405C50.2552 47.1559 50.2561 47.1786 50.2574 47.2076C50.26 47.2658 50.2644 47.3497 50.2713 47.4529C50.2851 47.6593 50.3092 47.9433 50.3502 48.2534C50.3912 48.5633 50.4493 48.9002 50.5311 49.2122C50.6128 49.5235 50.7191 49.8134 50.8581 50.0273C51.1372 50.4566 51.6261 50.7113 52.1639 50.8591C52.7025 51.0071 53.2977 51.05 53.8 51.05C54.804 51.05 55.7768 50.9096 56.5378 50.0328C56.7127 49.8312 56.8366 49.5461 56.925 49.2364C57.0136 48.9259 57.0677 48.5864 57.1006 48.2726C57.1336 47.9586 57.1454 47.6691 57.1489 47.4581C57.1507 47.3526 57.1505 47.2667 57.1497 47.207C57.1494 47.1772 57.1489 47.154 57.1485 47.1381L57.148 47.12L57.1479 47.1154L57.1479 47.1146Z");
        kabel.setFill(Color.rgb(221, 219, 219));
        kabel.setStroke(Color.TRANSPARENT);
		
        steckergehause = new SVGPath();
        steckergehause.setContent("M57 32C52.5556 32 52.5 25.6364 52.5 22H64.5C64.5 25.6364 64.5 32 60.5 32H57Z");
        steckergehause.setFill(Color.rgb(221, 219, 219));
        steckergehause.setStroke(Color.TRANSPARENT);
        
        elektrode1 = new Line(centerX + 55.75, centerY + 23.25, centerX + 55.75, centerY + 17.75);
        elektrode1.setStrokeWidth(1);
        elektrode1.setStroke(COLOR_GREY);

        elektrode2 = new Line(centerX + 51, centerY + 23.25, centerX + 51, centerY + 17.75);
        elektrode2.setStrokeWidth(1);
        elektrode2.setStroke(COLOR_GREY);
        
        svgs = new Group(kabel, steckergehause);
        svgs.setLayoutX(centerX-5);
        svgs.setLayoutY(centerY);
        
        
		
	}

	private void initializeDrawingPane() {
		drawingPane = new Pane();
		drawingPane.getStyleClass().add("drawing-pane");
		drawingPane.setMaxSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
		drawingPane.setMinSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
		drawingPane.setPrefSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
	}

	private void layoutParts() {
		drawingPane.getChildren().addAll(frame, socket, boxTotal, box1, box2, box3, box4,
				svgs, elektrode1, elektrode2, steckertyp1, steckertyp2, steckertyp3, total, txtLeistung1, txtLeistung2, txtLeistung3, txtLeistung4);

		getChildren().add(drawingPane);
	}

	private void setupEventHandlers() {


	}

	private void setupValueChangeListeners() {
		anzahlLadepunkte.addListener((observable, oldValue, newValue) -> {
			updateArrays();
//	        	boxes.add(new Rectangle(centerX, 0, 31, 0));
	       
       });
		
		leistung1.addListener((observable, oldValue, newValue) -> {
			updateArrays(); 
			calculateTotal();
             changeBoxSize();
        });
		
		leistung2.addListener((observable, oldValue, newValue) -> {
			updateArrays();
			calculateTotal();
			changeBoxSize();
       });
		
		leistung3.addListener((observable, oldValue, newValue) -> {
			updateArrays();
			calculateTotal();
			changeBoxSize();
		});
		
		leistung4.addListener((observable, oldValue, newValue) -> {
			updateArrays();
			calculateTotal();
			changeBoxSize();
		});
	}


	private void setupBinding() {
		txtLeistung1.textProperty().bindBidirectional(leistung1Property(), new NumberStringConverter());
		txtLeistung2.textProperty().bindBidirectional(leistung2Property(), new NumberStringConverter());
		txtLeistung3.textProperty().bindBidirectional(leistung3Property(), new NumberStringConverter());
		txtLeistung4.textProperty().bindBidirectional(leistung4Property(), new NumberStringConverter());
		
	}
	
	public void calculateTotal() {
		totalCalculation = leistung1.get() + leistung2.get() + leistung3.get() + leistung4.get();
		total.setText("Total: "+ '\n' + Integer.toString(totalCalculation) + " kW");
    }
	
	public void changeBoxSize() {
		if(totalCalculation > 0) {
			double position = 41.1;
			for(int i = 0; i < getAnzahlLadepunkte(); i++) {
				double sizeBox = ((double)leistungen.get(i) / totalCalculation) * TOTAL_HEIGHT_BOXES;
				if(i != 0) {
					sizeBox -= 1;
				}
				boxes.get(i).setHeight(sizeBox);
				boxes.get(i).setY(position);
				position += boxes.get(i).getHeight() + 1;  
			}
		} else {

		}


		
	}
	
	private void updateArrays() {
		boxes.clear();
		leistungen.clear();
		System.out.println(boxes);
		System.out.println(leistungen);
		System.out.println("__________________________________________________________");
		System.out.println("Ladepunkte: " + getAnzahlLadepunkte());
		switch (getAnzahlLadepunkte()) {
    	case 1: 
    		boxes.add(box1);
    		leistungen.add(getLeistung1());
    		break;
    	case 2: 
    		boxes.add(box1);
    		boxes.add(box2);
    		leistungen.add(getLeistung1());
    		leistungen.add(getLeistung2());
    		break;
    	case 3: 
    		boxes.add(box1);
    		boxes.add(box2);
    		boxes.add(box3);
    		leistungen.add(getLeistung1());
    		leistungen.add(getLeistung2());
    		leistungen.add(getLeistung3());
    		break;
    	case 4: 
    		boxes.add(box1);
    		boxes.add(box2);
    		boxes.add(box3);
    		boxes.add(box4);
    		leistungen.add(getLeistung1());
    		leistungen.add(getLeistung2());
    		leistungen.add(getLeistung3());
    		leistungen.add(getLeistung4());
    		break;
    	}
		System.out.println(boxes);
		System.out.println(leistungen);
		System.out.println("__________________________________________________________");
	}

	// resize by scaling
	@Override
	protected void layoutChildren() {
		super.layoutChildren();
		resize();
	}

	private void resize() {
		Insets padding = getPadding();
		double availableWidth = getWidth() - padding.getLeft() - padding.getRight();
		double availableHeight = getHeight() - padding.getTop() - padding.getBottom();

		double width = Math.max(Math.min(Math.min(availableWidth, availableHeight * ASPECT_RATIO), MAXIMUM_WIDTH),
				MINIMUM_WIDTH);

		double scalingFactor = width / ARTBOARD_WIDTH;

		if (availableWidth > 0 && availableHeight > 0) {
			drawingPane.relocate((getWidth() - ARTBOARD_WIDTH) * 0.5, (getHeight() - ARTBOARD_HEIGHT) * 0.5);
			drawingPane.setScaleX(scalingFactor);
			drawingPane.setScaleY(scalingFactor);
		}
	}

	// some handy functions

	private double percentageToValue(double percentage, double minValue, double maxValue) {
		return ((maxValue - minValue) * percentage) + minValue;
	}

	private double valueToPercentage(double value, double minValue, double maxValue) {
		return (value - minValue) / (maxValue - minValue);
	}

	private double valueToAngle(double value, double minValue, double maxValue) {
		return percentageToAngle(valueToPercentage(value, minValue, maxValue));
	}

	private double mousePositionToValue(double mouseX, double mouseY, double cx, double cy, double minValue,
			double maxValue) {
		double percentage = angleToPercentage(angle(cx, cy, mouseX, mouseY));

		return percentageToValue(percentage, minValue, maxValue);
	}

	private double angleToPercentage(double angle) {
		return angle / 360.0;
	}

	private double percentageToAngle(double percentage) {
		return 360.0 * percentage;
	}

	private double angle(double cx, double cy, double x, double y) {
		double deltaX = x - cx;
		double deltaY = y - cy;
		double radius = Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
		double nx = deltaX / radius;
		double ny = deltaY / radius;
		double theta = Math.toRadians(90) + Math.atan2(ny, nx);

		return Double.compare(theta, 0.0) >= 0 ? Math.toDegrees(theta) : Math.toDegrees((theta)) + 360.0;
	}

	private Point2D pointOnCircle(double cX, double cY, double radius, double angle) {
		return new Point2D(cX - (radius * Math.sin(Math.toRadians(angle - 180))),
				cY + (radius * Math.cos(Math.toRadians(angle - 180))));
	}

	private Text createCenteredText(String styleClass) {
		return createCenteredText(ARTBOARD_WIDTH * 0.5, ARTBOARD_HEIGHT * 0.5, styleClass);
	}

	private Text createCenteredText(double cx, double cy, String styleClass) {
		Text text = new Text();
		text.getStyleClass().add(styleClass);
		text.setTextOrigin(VPos.CENTER);
		text.setTextAlignment(TextAlignment.CENTER);
		double width = cx > ARTBOARD_WIDTH * 0.5 ? ((ARTBOARD_WIDTH - cx) * 2.0) : cx * 2.0;
		text.setWrappingWidth(width);
		text.setBoundsType(TextBoundsType.VISUAL);
		text.setY(cy);
		text.setX(cx - (width / 2.0));

		return text;
	}

	private Group createTicks(double cx, double cy, int numberOfTicks, double overallAngle, double tickLength,
			double indent, double startingAngle, String styleClass) {
		Group group = new Group();

		double degreesBetweenTicks = overallAngle == 360 ? overallAngle / numberOfTicks
				: overallAngle / (numberOfTicks - 1);
		double outerRadius = Math.min(cx, cy) - indent;
		double innerRadius = Math.min(cx, cy) - indent - tickLength;

		for (int i = 0; i < numberOfTicks; i++) {
			double angle = 180 + startingAngle + i * degreesBetweenTicks;

			Point2D startPoint = pointOnCircle(cx, cy, outerRadius, angle);
			Point2D endPoint = pointOnCircle(cx, cy, innerRadius, angle);

			Line tick = new Line(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
			tick.getStyleClass().add(styleClass);
			group.getChildren().add(tick);
		}

		return group;
	}

	// compute sizes

	@Override
	protected double computeMinWidth(double height) {
		Insets padding = getPadding();
		double horizontalPadding = padding.getLeft() + padding.getRight();

		return MINIMUM_WIDTH + horizontalPadding;
	}

	@Override
	protected double computeMinHeight(double width) {
		Insets padding = getPadding();
		double verticalPadding = padding.getTop() + padding.getBottom();

		return MINIMUM_HEIGHT + verticalPadding;
	}

	@Override
	protected double computePrefWidth(double height) {
		Insets padding = getPadding();
		double horizontalPadding = padding.getLeft() + padding.getRight();

		return ARTBOARD_WIDTH + horizontalPadding;
	}

	@Override
	protected double computePrefHeight(double width) {
		Insets padding = getPadding();
		double verticalPadding = padding.getTop() + padding.getBottom();

		return ARTBOARD_HEIGHT + verticalPadding;
	}

	// alle getter und setter (generiert via "Code -> Generate... -> Getter and
	// Setter)

	public int getLeistung1() {
		return leistung1.get();
	}
	public void setLeistung1(int leistung1) {
		this.leistung1.set(leistung1);
	}
	public IntegerProperty leistung1Property () {
		return leistung1;
	}
	public int getLeistung2() {
		return leistung2.get();
	}
	public void setLeistung2(int leistung2) {
		this.leistung2.set(leistung2);
	}
	public IntegerProperty leistung2Property () {
		return leistung2;
	}
	public int getLeistung3() {
		return leistung3.get();
	}
	public void setLeistung3(int leistung3) {
		this.leistung3.set(leistung3);
	}
	public IntegerProperty leistung3Property () {
		return leistung3;
	}
	public int getLeistung4() {
		return leistung4.get();
	}
	public void setLeistung4(int leistung4) {
		this.leistung4.set(leistung4);
	}
	public IntegerProperty leistung4Property () {
		return leistung4;
	}
	public int getAnzahlLadepunkte() {
        return anzahlLadepunkte.get();
    }
    public void setAnzahlLadepunkte(int anzahl) {
        this.anzahlLadepunkte.set(anzahl);
    }
    public IntegerProperty anzahlLadepunkteProperty () {
        return anzahlLadepunkte;
    }
	
}
