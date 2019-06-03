package tabeaeggler.simplecontrol;

import java.util.ArrayList;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.converter.NumberStringConverter;

/**
 * PowerStation CustomControl
 *
 * @author Tabea Eggler
 * @author Benjamin Huber
 * @author Dieter Holz (Template)

 */

public class PowerStation extends Region {
    //artboard settings
	private static final double ARTBOARD_WIDTH = 100;
	private static final double ARTBOARD_HEIGHT = 100;
	private static final double ASPECT_RATIO = ARTBOARD_WIDTH / ARTBOARD_HEIGHT;
	private static final double MINIMUM_WIDTH = 200;
	private static final double MAXIMUM_WIDTH = 800;

	//colors (others are in css defined)
	private static final Color COLOR_GREY = Color.rgb(221, 219, 219);

	//properties
	private final IntegerProperty leistung1 = new SimpleIntegerProperty();
	private final IntegerProperty leistung2 = new SimpleIntegerProperty();
	private final IntegerProperty leistung3 = new SimpleIntegerProperty();
	private final IntegerProperty leistung4 = new SimpleIntegerProperty();
	private final IntegerProperty anzahlLadepunkte = new SimpleIntegerProperty();

	//calculation variables
    private static int TOTAL_HEIGHT_BOXES = 40;
	private int totalCalculation = 0;
	private double sizeBox1;
	private double sizeBox2;
	private double sizeBox3;
	private double sizeBox4;
    double positionBox1;
    double positionBox2;
    double positionBox3;
    double positionBox4;

    //center position
    double centerX;
    double centerY;

    //sizes
    double sizeTextfield;
    double sizeFrame;
    double sizeTotal;

	//arrays
	private ArrayList<Rectangle> boxes;
	private ArrayList<Integer> leistungen;
	private ArrayList<TextField> textFields;
	private ArrayList<Label> labels;

	// shapes
	private Rectangle frame;
	private Rectangle socket;
	private Rectangle boxTotal;
	private Rectangle box1; 
	private Rectangle box2;
	private Rectangle box3;
	private Rectangle box4;
	private SVGPath kabel;
	private SVGPath steckergehause;
	private Text txtTotal;
	private TextField txtLeistung1;
	private TextField txtLeistung2;
	private TextField txtLeistung3;
	private TextField txtLeistung4;
	private Label lblKw1;
	private Label lblKw2;
	private Label lblKw3;
	private Label lblKw4;
	private Group svgGroup;
	private Line elektrode1;
	private Line elektrode2;

	//needed for resizing
	private Pane drawingPane;

	public PowerStation() {
		initializeSelf();
		initializeParts();
		initializeDrawingPane();
		layoutParts();
		setupValueChangeListeners();
		setupBinding();
		initializer();
	}

	private void initializeSelf() {
		String fonts = getClass().getResource("/fonts/fonts.css").toExternalForm();
		getStylesheets().add(fonts);

		String stylesheet = getClass().getResource("style.css").toExternalForm();
		getStylesheets().add(stylesheet);

		getStyleClass().add("powerstation");
	}

	private void initializeParts() {
		centerX =  31.5;
		centerY = 50-(69/2);
		sizeTextfield = 14;
		sizeFrame = 69;
		sizeTotal = 81.1;

		//text
        txtLeistung1 = new TextField();
        txtLeistung1.getStyleClass().addAll("text-leistung");
        txtLeistung2 = new TextField();
        txtLeistung2.getStyleClass().addAll("text-leistung");
        txtLeistung3 = new TextField();
        txtLeistung3.getStyleClass().addAll("text-leistung");
        txtLeistung4 = new TextField();
        txtLeistung4.getStyleClass().addAll("text-leistung");
        
        lblKw1 = new Label("kW");
        lblKw1.getStyleClass().addAll("text-leistung");
        lblKw2 = new Label("kW");
        lblKw2.getStyleClass().addAll("text-leistung");
        lblKw3 = new Label("kW");
        lblKw3.getStyleClass().addAll("text-leistung");
        lblKw4 = new Label("kW");
        lblKw4.getStyleClass().addAll("text-leistung");

        txtTotal = new Text();
        txtTotal.setX(40);
        txtTotal.setY(centerY+3+7.8);
        txtTotal.setFont(Font.font("Open Sans", FontWeight.NORMAL, 5.5));
        txtTotal.setTextAlignment(TextAlignment.CENTER);
        txtTotal.setWrappingWidth(20);

        //design elements
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
		box4.getStyleClass().addAll("box4");
		
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
        
        svgGroup = new Group(kabel, steckergehause);
        svgGroup.setLayoutX(centerX-5);
        svgGroup.setLayoutY(centerY);

        //arrays needed for loops (indexing)
        boxes = new ArrayList<>();
        leistungen = new ArrayList<>();
        textFields = new ArrayList<>();
        labels = new ArrayList<>();
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
                svgGroup, elektrode1, elektrode2, txtTotal, txtLeistung1, txtLeistung2, txtLeistung3, txtLeistung4, lblKw1, lblKw2, lblKw3, lblKw4);
		getChildren().add(drawingPane);
	}

	private void setupValueChangeListeners() {
		anzahlLadepunkte.addListener((observable, oldValue, newValue) -> {
			if(newValue.intValue() < 1) {
				box1.setHeight(0);
				box2.setHeight(0);
				box3.setHeight(0);
				box4.setHeight(0);
				txtLeistung1.setVisible(false);
				txtLeistung2.setVisible(false);
				txtLeistung3.setVisible(false);
				txtLeistung4.setVisible(false);
				lblKw1.setVisible(false);
				lblKw2.setVisible(false);
				lblKw3.setVisible(false);
				lblKw4.setVisible(false);
			}
			if(oldValue.intValue() < newValue.intValue()) {
				if(newValue.intValue() == 1) {
					box2.setHeight(0);
					box3.setHeight(0);
					box4.setHeight(0);
					txtLeistung2.setVisible(false);
					txtLeistung3.setVisible(false);
					txtLeistung4.setVisible(false);
					lblKw2.setVisible(false);
					lblKw3.setVisible(false);
					lblKw4.setVisible(false);
				}
				if(newValue.intValue() == 2) {
					box3.setHeight(0);
					box4.setHeight(0);
					txtLeistung3.setVisible(false);
					txtLeistung4.setVisible(false);
					lblKw3.setVisible(false);
					lblKw4.setVisible(false);
				}
				if(newValue.intValue() == 3) {
					box4.setHeight(0);
					txtLeistung4.setVisible(false);
					lblKw4.setVisible(false);
				}
			}
			updateArrays();
			changeBoxSize();
			updateTextPosition();
	       
       });
		
		leistung1.addListener((observable, oldValue, newValue) -> {
			updateArrays(); 
			calculateTotal();
            changeBoxSize();
            updateTextPosition();
        });
		
		leistung2.addListener((observable, oldValue, newValue) -> {
			updateArrays();
			calculateTotal();
			changeBoxSize();
			updateTextPosition();
       });
		
		leistung3.addListener((observable, oldValue, newValue) -> {
			updateArrays();
			calculateTotal();
			changeBoxSize();
			updateTextPosition();
		});
		
		leistung4.addListener((observable, oldValue, newValue) -> {
			updateArrays();
			calculateTotal();
			changeBoxSize();
			updateTextPosition();
		});
	}

	private void setupBinding() {
		txtLeistung1.setTextFormatter(new TextFormatter<>(change ->
        (change.getControlNewText().matches("([1-9][0-9]*)?")) ? change : null)); //only numbers allowed
		txtLeistung1.textProperty().bindBidirectional(leistung1Property(), new NumberStringConverter());
		
		txtLeistung2.setTextFormatter(new TextFormatter<>(change ->
        (change.getControlNewText().matches("([1-9][0-9]*)?")) ? change : null));
		txtLeistung2.textProperty().bindBidirectional(leistung2Property(), new NumberStringConverter());
		
		txtLeistung3.setTextFormatter(new TextFormatter<>(change ->
        (change.getControlNewText().matches("([1-9][0-9]*)?")) ? change : null));
		txtLeistung3.textProperty().bindBidirectional(leistung3Property(), new NumberStringConverter());
		
		txtLeistung4.setTextFormatter(new TextFormatter<>(change ->
        (change.getControlNewText().matches("([1-9][0-9]*)?")) ? change : null));
		txtLeistung4.textProperty().bindBidirectional(leistung4Property(), new NumberStringConverter());
	}

	//*Helper Methods*

	public void calculateTotal() {
		totalCalculation = getLeistung1() + getLeistung2() + getLeistung3() + getLeistung4();
		txtTotal.setText("Total:"+ '\n' + Integer.toString(totalCalculation) + " kW");
    }

	private void initializer() {
		if(getLeistung1() <= 0) {
			txtLeistung1.setVisible(false);
			lblKw1.setVisible(false);
			box1.setHeight(0);
		}
		if(getLeistung2() <= 0) {
			txtLeistung2.setVisible(false);
			lblKw2.setVisible(false);
			box2.setHeight(0);
		}
		if(getLeistung3() <= 0) {
			txtLeistung3.setVisible(false);
			lblKw3.setVisible(false);
			box3.setHeight(0);
		}
		if(getLeistung4() <= 0) {
			txtLeistung4.setVisible(false);
			lblKw4.setVisible(false);
			box4.setHeight(0);
		}
		updateArrays();
		calculateTotal();
		changeBoxSize();
		updateTextPosition();
	}

	public void changeBoxSize() {
		if(totalCalculation > 0) {
			double position = 41.1;
			int countTooSmallValues = 0;
			ArrayList<Integer> tooSmallValuesPositions = new ArrayList<Integer>();

			for(int i = 0; i < getAnzahlLadepunkte(); i++) {
				double currentSize = ((double)leistungen.get(i) / totalCalculation) * TOTAL_HEIGHT_BOXES;
				if(i != 0) {
					currentSize -= 1;
				}
				if(currentSize < 7 && (leistungen.get(i) > 0)) {
					countTooSmallValues++;
					tooSmallValuesPositions.add(i);
				}
				boxes.get(i).setHeight(currentSize);
				boxes.get(i).setY(position);
				position += boxes.get(i).getHeight() + 1;  //calculate new position
			}

			//if tooSmallValues exist
			if(countTooSmallValues > 0) {
				double sumNewHeights = 0;
				double totalHeightTooSmallValues = 0;
				position = 41.1; //set position to default value

				//calculate total Height of all values which are too small (smaller than 7)
				for (Integer index : tooSmallValuesPositions) {
					totalHeightTooSmallValues += boxes.get(index).getHeight();
				}

				//calculate total height and total difference (to be distributed on other boxes)
				double totalHeight = TOTAL_HEIGHT_BOXES - (countTooSmallValues * 7);
				double totalDifference = (0.175 * countTooSmallValues) - (totalHeightTooSmallValues / TOTAL_HEIGHT_BOXES); //%-Satz

				//go through all boxes and resize them (at the ratio of the total difference and its size)
				for(int j = 0; j < getAnzahlLadepunkte(); j++) {
					double currentSize = boxes.get(j).getHeight();
					double newHeight = 0;
					if(currentSize > 7) {
						double ownHeight = boxes.get(j).getHeight();
						double percentageOwnHeight = ownHeight / totalHeight;
						double heightDifference = totalHeight * totalDifference;
						newHeight = ownHeight - (percentageOwnHeight * heightDifference);
						sumNewHeights += newHeight;
					} else {
						newHeight = 7;
					}
					if(j == getAnzahlLadepunkte()-1) {
						newHeight = sizeTotal - position;
					}
					//set new Height and Position of current box
					boxes.get(j).setHeight(newHeight);
					boxes.get(j).setY(position);
					position += boxes.get(j).getHeight() + 1;
				}
			}
		}
	}

	private void updateTextPosition() {
		for(int i = 0; i < textFields.size(); i++) {
			double position = boxes.get(i).getY() + (boxes.get(i).getHeight() / 2) - (textFields.get(i).getHeight() / 2);
			textFields.get(i).setLayoutY(position);
			labels.get(i).setLayoutY(position);
			int value = leistungen.get(i);
			if(value <= 0) {
				textFields.get(i).setVisible(false);
				labels.get(i).setVisible(false);
				boxes.get(i).setHeight(0);
			}
			if(0 < value && value <= 9) {
				textFields.get(i).setLayoutX(41);
				labels.get(i).setLayoutX(textFields.get(i).getLayoutX() + 7.5);
			}
			if(10 <= value && value <= 99) {
				textFields.get(i).setLayoutX(39.5);
				labels.get(i).setLayoutX(textFields.get(i).getLayoutX() + 7.5 + 2.6);
			}
			if(100 <= value && value <= 999) {
				textFields.get(i).setLayoutX(38);
				labels.get(i).setLayoutX(textFields.get(i).getLayoutX() + 7.5  + 5.2);
			}
		}
	}

	private void updateArrays() {
		//clear arrays to ensure that amount of ladepunkte is correct
		boxes.clear();
		leistungen.clear();
		textFields.clear();
		labels.clear();

		//fill arrays (dependent on amount of ladepunkte)
		switch (getAnzahlLadepunkte()) {
    	case 1: 
    		boxes.add(box1);
    		leistungen.add(getLeistung1());
    		textFields.add(txtLeistung1);
    		labels.add(lblKw1);
    		txtLeistung1.setVisible(true);
    		lblKw1.setVisible(true);
    		break;
    	case 2: 
    		boxes.add(box1);
    		boxes.add(box2);
    		leistungen.add(getLeistung1());
    		leistungen.add(getLeistung2());
    		textFields.add(txtLeistung1);
    		textFields.add(txtLeistung2);
    		labels.add(lblKw1);
    		labels.add(lblKw2);
    		txtLeistung1.setVisible(true);
    		txtLeistung2.setVisible(true);
    		lblKw1.setVisible(true);
    		lblKw2.setVisible(true);
    		break;
    	case 3: 
    		boxes.add(box1);
    		boxes.add(box2);
    		boxes.add(box3);
    		leistungen.add(getLeistung1());
    		leistungen.add(getLeistung2());
    		leistungen.add(getLeistung3());
    		textFields.add(txtLeistung1);
    		textFields.add(txtLeistung2);
    		textFields.add(txtLeistung3);
    		labels.add(lblKw1);
    		labels.add(lblKw2);
    		labels.add(lblKw3);
    		txtLeistung1.setVisible(true);
    		txtLeistung2.setVisible(true);
    		txtLeistung3.setVisible(true);
    		lblKw1.setVisible(true);
    		lblKw2.setVisible(true);
    		lblKw3.setVisible(true);
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
    		textFields.add(txtLeistung1);
    		textFields.add(txtLeistung2);
    		textFields.add(txtLeistung3);
    		textFields.add(txtLeistung4);
    		labels.add(lblKw1);
    		labels.add(lblKw2);
    		labels.add(lblKw3);
    		labels.add(lblKw4);
    		txtLeistung1.setVisible(true);
    		txtLeistung2.setVisible(true);
    		txtLeistung3.setVisible(true);
    		txtLeistung4.setVisible(true);
    		lblKw1.setVisible(true);
    		lblKw2.setVisible(true);
    		lblKw3.setVisible(true);
    		lblKw4.setVisible(true);
    		break;
    	}
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

	//all getter und setter
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