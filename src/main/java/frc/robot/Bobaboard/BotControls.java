package frc.robot.Bobaboard;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Constants.OIConstants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveSubsystem;


public class BotControls {

	RobotContainer rContainer = RobotContainer.getInstance();
    ControlHub controlHub = ControlHub.getInstance();
    boolean interruptedPPLib;
    

    final static SendableChooser<Boolean> ControllerMode = new SendableChooser<>();
    public boolean interruptAutoAlign = false;
    public boolean OneControllerQuery = true;
    public Command leftCommand;
    public Command rightCommand;
    public Command algaeCommand;

    public final void PutControllerOption(){
        ControllerMode.addOption("One Controller", true);
        ControllerMode.addOption("Two Controller(s)", false);
        ControllerMode.setDefaultOption("Default", false);
        SmartDashboard.putData("Controller Selection", ControllerMode);
    }

    public final void selectControllerOption(){
        if (ControllerMode.getSelected() == null){
            OneControllerQuery = true;
        }else{
        Boolean m_ControllerSelected = ControllerMode.getSelected();
        OneControllerQuery = m_ControllerSelected;
        }
    }

    public final void o_reportBotControlData(){
        SmartDashboard.putBoolean("BotCTRL/isDebugMode:", OneControllerQuery);
        SmartDashboard.putBoolean("DRV/A_Button", controlHub.driverController.A_Button.isBeingPressed());
        SmartDashboard.putBoolean("DRV/B_Button", controlHub.driverController.B_Button.isBeingPressed());
        SmartDashboard.putBoolean("DRV/X_Button", controlHub.driverController.X_Button.isBeingPressed());
        SmartDashboard.putBoolean("DRV/Y_Button", controlHub.driverController.Y_Button.isBeingPressed());
        SmartDashboard.putBoolean("OPR/A_Button", controlHub.operatorController.A_Button.isBeingPressed());
        SmartDashboard.putBoolean("OPR/B_Button", controlHub.operatorController.B_Button.isBeingPressed());
        SmartDashboard.putBoolean("OPR/X_Button", controlHub.operatorController.X_Button.isBeingPressed());
        SmartDashboard.putBoolean("OPR/Y_Button", controlHub.operatorController.Y_Button.isBeingPressed());
        SmartDashboard.putBoolean("DRV/LBumper", controlHub.driverController.L_Bumper.isBeingPressed());
        SmartDashboard.putBoolean("DRV/RBumper", controlHub.driverController.R_Bumper.isBeingPressed());
        SmartDashboard.putBoolean("OPR/LBumper", controlHub.operatorController.L_Bumper.isBeingPressed());
        SmartDashboard.putBoolean("OPR/RBumper", controlHub.operatorController.R_Bumper.isBeingPressed());
        SmartDashboard.putBoolean("DRV/LTrigger", controlHub.driverController.L_Trigger.isBeingPressed());
        SmartDashboard.putBoolean("DRV/RTrigger", controlHub.driverController.R_Trigger.isBeingPressed());
        SmartDashboard.putBoolean("OPR/LTrigger", controlHub.operatorController.L_Trigger.isBeingPressed());
        SmartDashboard.putBoolean("OPR/RTrigger", controlHub.operatorController.R_Trigger.isBeingPressed());
    }

    public void toggleCoralAutoAlign(boolean branchSide) {
    boolean autoAlignActive = SmartDashboard.getBoolean("AutoAlign Status", true);
    if (!autoAlignActive) {
        // Turn ON
        rContainer.autoAlignCommand(branchSide).schedule();
        SmartDashboard.putBoolean("AutoAlign Status", true);
        System.out.println("autoAlign Activated");
    } else {
        // Turn OFF

        CommandScheduler.getInstance().cancel(rContainer.autoAlignCommand(branchSide));
        SmartDashboard.putBoolean("AutoAlign Status", false);
        System.out.println("autoAlign Deactivated");
    }
}

    public void RunRobot(){
    if (OneControllerQuery == true){
            if (controlHub.driverController.Y_Button.wasActivated()) {
                DriveSubsystem.getInstance().zeroHeading();
            }

            if (controlHub.driverController.X_Button.wasActivated()) {
            }

            if (controlHub.driverController.B_Button.wasActivated()) {
            }
            if (controlHub.driverController.A_Button.wasActivated()) {
            }
    } else {
// 2 Controller Here
        // Driver Controls
            if(Math.abs(controlHub.driverController.getLeftY())>=OIConstants.kDriveDeadband || 
            Math.abs(controlHub.driverController.getLeftX())>=OIConstants.kDriveDeadband ||
            Math.abs(controlHub.driverController.getRightX())>=OIConstants.kDriveDeadband ){
                if (leftCommand != null && leftCommand.isScheduled()){
                    leftCommand.cancel();
                } if (rightCommand != null){
                    rightCommand.cancel();
                } if (algaeCommand != null){
                    algaeCommand.cancel();
                }
            }
    
        //Resets the virtual heading based on the current heading (fixes drift)
        if (controlHub.driverController.Y_Button.wasActivated()) {
                DriveSubsystem.getInstance().zeroHeading();
        }
        if (controlHub.driverController.X_Button.wasActivated()) {

            leftCommand = rContainer.autoAlignCommand(false);
            leftCommand.schedule();
        }


        if (controlHub.driverController.B_Button.wasActivated()) {
                    rightCommand = rContainer.autoAlignCommand(true);
            rightCommand.schedule();
        }

        if (controlHub.driverController.A_Button.wasActivated()) {
            algaeCommand = rContainer.autoAlignAlgaeCommad();
            algaeCommand.schedule();
        }

        // Operator Side
        //Polls for Controller Input, if a button is being pressed deliver selected intake/extake speed
        if(controlHub.operatorController.L_Bumper.wasReleased() || controlHub.operatorController.R_Bumper.wasReleased()
                && controlHub.operatorController.L_Bumper.isNotBeingPressed() && controlHub.operatorController.R_Bumper.isNotBeingPressed()){
                rContainer.m_Effector.setIntakeLazyPercentageOpenLoop(0);
        } else{
            if (controlHub.operatorController.L_Bumper.wasActivated() && !controlHub.operatorController.R_Bumper.isBeingPressed()) {
                rContainer.doubleRollerCommand().schedule();
                }else if (controlHub.operatorController.R_Bumper.isBeingPressed() && !controlHub.operatorController.L_Bumper.isBeingPressed()) {
                rContainer.m_Effector.setIntakeLazyPercentageOpenLoop(1.0);
                }
        }
        
            //Polls for Controller Input, if a button is being pressed deliver selected Elevator height
            if (!controlHub.operatorController.A_Button.isBeingPressed() && !controlHub.operatorController.X_Button.isBeingPressed() 
            && !controlHub.operatorController.Y_Button.isBeingPressed() && !controlHub.operatorController.B_Button.isBeingPressed()){
                rContainer.m_Elevator.setSafePercentageOpenLoop(0.0);
            }else{

            if (controlHub.operatorController.B_Button.wasActivated()){
                    rContainer.stowElevatorCommand().schedule();
            }else if (controlHub.operatorController.A_Button.wasActivated() && !controlHub.operatorController.B_Button.isBeingPressed()
                        && !controlHub.operatorController.X_Button.isBeingPressed() && !controlHub.operatorController.Y_Button.isBeingPressed()) {
                    rContainer.tierOneElevatorCommand().schedule(); //tierOneHandoffCommand
            }else if (controlHub.operatorController.X_Button.wasActivated() && !controlHub.operatorController.A_Button.isBeingPressed()
                        && !controlHub.operatorController.B_Button.isBeingPressed() && !controlHub.operatorController.Y_Button.isBeingPressed()){
                    rContainer.tierTwoElevatorCommand().schedule();
            }else if (controlHub.operatorController.Y_Button.wasActivated() && !controlHub.operatorController.B_Button.isBeingPressed()
                        && !controlHub.operatorController.X_Button.isBeingPressed() && !controlHub.operatorController.A_Button.isBeingPressed()){
                    rContainer.tierThreeElevatorCommand().schedule();
                }
            }

            if(controlHub.operatorController.L_Trigger.isNotBeingPressed() && controlHub.operatorController.R_Trigger.isNotBeingPressed()){
                rContainer.m_Effector.setPivotLazyPercentageOpenLoop(0);
            }else if(controlHub.operatorController.L_Trigger.wasActivated()){
                rContainer.m_Effector.setPivotLazyPercentageOpenLoop(-0.7);
            }else if (controlHub.operatorController.R_Trigger.wasActivated()){
                rContainer.m_Effector.setPivotLazyPercentageOpenLoop(0.7);
            }

        }
    }
}
     



