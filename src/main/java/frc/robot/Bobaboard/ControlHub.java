package frc.robot.Bobaboard;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;

public class ControlHub {
    private static ControlHub mInstance = null;

	public static ControlHub getInstance() {
		if (mInstance == null) {
			mInstance = new ControlHub();
        }
		return mInstance;
	}

	public XboxControllerSetup driverController;
	public XboxControllerSetup operatorController;
	public XboxControllerSetup tControllerSetup1;
	public XboxControllerSetup tControllerSetup2;

	boolean testBoolean = false;

	private ControlHub() {
		driverController = new XboxControllerSetup(Constants.OIConstants.kDriverControllerPort);
		operatorController = new XboxControllerSetup(Constants.OIConstants.kOperatorControllerPort);
	}

	public void verifyControllerIntegrity(){
		boolean driverIsConnected = driverController.isConnected();
		boolean operatorIsConnected = operatorController.isConnected();
		SmartDashboard.putBoolean("Controller Can Exist", testBoolean);
		SmartDashboard.putBoolean("Driver Controller Connected", driverIsConnected);
		SmartDashboard.putBoolean("Operator Controller Connected", operatorIsConnected);
	}

	public boolean verifyPossibleControllerInit(){
		tControllerSetup1 = new XboxControllerSetup(Constants.OIConstants.kDriverControllerPort);
		tControllerSetup2 = new XboxControllerSetup(Constants.OIConstants.kOperatorControllerPort);
		if (tControllerSetup1.isConnected() == false || tControllerSetup2.isConnected() == false){
			tControllerSetup1 = null;
			tControllerSetup2 = null;
			testBoolean = false;
			return testBoolean;
		}else{
			tControllerSetup1 = null;
			tControllerSetup2 = null;
			testBoolean = true;
			return testBoolean;
		}
	}

	public void update() {
		driverController.update();
		operatorController.update();
	}

}
