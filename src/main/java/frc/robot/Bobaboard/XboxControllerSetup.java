package frc.robot.Bobaboard;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import frc.utils.Util;

public class XboxControllerSetup extends XboxController {
    private static final double PRESS_THRESHOLD = 0.1;
	private double DEAD_BAND = 0.2;
	public ButtonCheck
            A_Button,
			B_Button,
			X_Button,
			Y_Button,
			startButton,
			backButton,
			L_Bumper,
			R_Bumper,
			L_Trigger,
			R_Trigger,
			POV0,
			POV90,
			POV180,
			POV270;

	public static final int A_BUTTON = 1;
	public static final int B_BUTTON = 2;
	public static final int X_BUTTON = 3;
	public static final int Y_BUTTON = 4;
	public static final int LEFT_BUMPER = 5;
	public static final int RIGHT_BUMPER = 6;
	public static final int BACK_BUTTON = 7;
	public static final int START_BUTTON = 8;
	public static final int LEFT_CENTER_CLICK = 9;
	public static final int RIGHT_CENTER_CLICK = 10;
	public static final int LEFT_TRIGGER = -2;
	public static final int RIGHT_TRIGGER = -3;
	public static final int POV_0 = -4;
	public static final int POV_90 = -5;
	public static final int POV_180 = -6;
	public static final int POV_270 = -7;

	public static final double POVButtonTolerance = 25; //Degrees //Skill Issue
	public static final double POVButtonNorth = 0;
	public static final double POVButtonEast = 90;
	public static final double POVButtonSouth = 180;
	public static final double POVButtonWest = 270;

public void setDeadband(double deadband) {
		DEAD_BAND = deadband;
	}

	public XboxControllerSetup(int usb) {
		super(usb);
		A_Button = new ButtonCheck(A_BUTTON);
		B_Button = new ButtonCheck(B_BUTTON);
		X_Button = new ButtonCheck(X_BUTTON);
		Y_Button = new ButtonCheck(Y_BUTTON);
		startButton = new ButtonCheck(START_BUTTON);
		backButton = new ButtonCheck(BACK_BUTTON);
		L_Bumper = new ButtonCheck(LEFT_BUMPER);
		R_Bumper = new ButtonCheck(RIGHT_BUMPER);
		L_Trigger = new ButtonCheck(LEFT_TRIGGER);
		R_Trigger = new ButtonCheck(RIGHT_TRIGGER);
		POV0 = new ButtonCheck(POV_0);
		POV90 = new ButtonCheck(POV_90);
		POV180 = new ButtonCheck(POV_180);
		POV270 = new ButtonCheck(POV_270);
	}

	@Override
	public double getLeftX() {
		return Util.deadBand(getRawAxis(0), DEAD_BAND);
	}

	@Override
	public double getRightX() {
		return Util.deadBand(getRawAxis(4), DEAD_BAND);
	}

	@Override
	public double getLeftY() {
		return Util.deadBand(getRawAxis(1), DEAD_BAND);
	}

	@Override
	public double getRightY() {
		return Util.deadBand(getRawAxis(5), DEAD_BAND);
	}
    //Note: Drive Default Command = get## * -1

	@Override
	public double getLeftTriggerAxis() {
		return Util.deadBand(getRawAxis(2), PRESS_THRESHOLD);
	}

	@Override
	public double getRightTriggerAxis() {
		return Util.deadBand(getRawAxis(3), PRESS_THRESHOLD);
	}

	public Rotation2d getPOVDirection() {
		System.out.println(getPOV());
		return Rotation2d.fromDegrees(getPOV());
	}

	public boolean getFaceButtonA(){
		return getRawButtonPressed(A_BUTTON);
	}

	public boolean getFaceButtonB(){
		return getRawButtonPressed(B_BUTTON);
	}

	public boolean getFaceButtonX(){
		return getRawButtonPressed(X_BUTTON);
	}

	public boolean getFaceButtonY(){
		return getRawButtonPressed(Y_BUTTON);
	}

	public boolean getRightBumper(){
		return getRawButtonPressed(RIGHT_BUMPER);
	}

	public boolean getLeftBumper(){
		return getRawButtonPressed(LEFT_BUMPER);
	}

	public class ButtonCheck {
		boolean buttonCheck = false;
		boolean buttonActive = false;
		boolean activationReported = false;
		boolean longPressed = false;
		boolean longPressActivated = false;
		boolean hasBeenPressed = false;
		boolean longReleased = false;
		private double buttonStartTime = 0;
		private double longPressDuration = 0.3;

		public void setLongPressDuration(double seconds) {
			longPressDuration = seconds;
		}

		private int buttonNumber;

		public ButtonCheck(int id) {
			buttonNumber = id;
		}

		public void update() {
			if (buttonNumber > 0) {
				buttonCheck = getRawButton(buttonNumber);
			} else {
				switch (buttonNumber) {
					case LEFT_TRIGGER:
						buttonCheck = getLeftTriggerAxis() > 0;
						break;
					case RIGHT_TRIGGER:
						buttonCheck = getRightTriggerAxis() > 0;
						break;
					case POV_0:
						buttonCheck = (MathUtil.isNear(POVButtonNorth,
							 ((double)getPOV()),
							 POVButtonTolerance));
						break;
					case POV_90:
						buttonCheck = (MathUtil.isNear(POVButtonEast,
						((double)getPOV()),
						POVButtonTolerance));
						break;
					case POV_180:
						buttonCheck = (MathUtil.isNear(POVButtonSouth,
						((double)getPOV()),
						POVButtonTolerance));
						break;
					case POV_270:
						buttonCheck = (MathUtil.isNear(POVButtonWest,
						((double)getPOV()),
						POVButtonTolerance));
						break;
					case A_BUTTON:
						buttonCheck = (getFaceButtonA());
						break;
					case X_BUTTON:
						buttonCheck = (getFaceButtonX());
						break;
					case Y_BUTTON:
						buttonCheck = (getFaceButtonY());
						break;
					case B_BUTTON:
						buttonCheck = (getFaceButtonB());
						break;
					case LEFT_BUMPER:
						buttonCheck = (getLeftBumper());
						break;
					case RIGHT_BUMPER:
						buttonCheck = (getLeftBumper());
						break;
					default:
						buttonCheck = false;
						break;
				}
			}
			if (buttonCheck) {
				if (buttonActive) {
					if (((Timer.getFPGATimestamp() - buttonStartTime) > longPressDuration) && !longPressActivated) {
						longPressActivated = true;
						longPressed = true;
						longReleased = false;
					}
				} else {
					buttonActive = true;
					activationReported = false;
					buttonStartTime = Timer.getFPGATimestamp();
				}
			} else {
				if (buttonActive) {
					buttonActive = false;
					activationReported = true;
					if (longPressActivated) {
						hasBeenPressed = false;
						longPressActivated = false;
						longPressed = false;
						longReleased = true;
					} else {
						hasBeenPressed = true;
					}
				}
			}
		}

		/**
		 * Returns true once the button is pressed, regardless of
		 * the activation duration. Only returns true one time per
		 * button press, and is reset upon release.
		 */
		public boolean wasActivated() {
			if (buttonActive && !activationReported) {
				activationReported = true;
				return true;
			}
			return false;
		}

		/**
		 * Returns true once the button is released after being
		 * held for 0.5 seconds or less. Only returns true one time
		 * per button press.
		 */
		public boolean shortReleased() {
			if (hasBeenPressed) {
				hasBeenPressed = false;
				return true;
			}
			return false;
		}

		/**
		 * Returns true once if the button is pressed for more than 0.5 seconds.
		 * Only true while the button is still depressed; it becomes false once the
		 * button is released.
		 */
		public boolean longPressed() {
			if (longPressed) {
				longPressed = false;
				return true;
			}
			return false;
		}

		/**
		 * Returns true one time once the button is released after being held for
		 * more than 0.5 seconds.
		 */
		public boolean longReleased() {
			if (longReleased) {
				longReleased = false;
				return true;
			}
			return false;
		}

		/**
		 * Returns true once the button is released, regardless of activation duration.
		 */
		public boolean wasReleased() {
			return shortReleased() || longReleased();
		}

		/** Returns true if the button is currently being pressed. */
		public boolean isBeingPressed() {
			return buttonActive;
		}

		public boolean isNotBeingPressed(){
			return !buttonActive;
		}
	}

	public void update() {
		A_Button.update();
		B_Button.update();
		X_Button.update();
		Y_Button.update();
		L_Bumper.update();
		R_Bumper.update();
		L_Trigger.update();
		R_Trigger.update();
		POV0.update();
		POV90.update();
		POV180.update();
		POV270.update();
	}
}
