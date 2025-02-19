package frc.robot.Bobaboard;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.RobotContainer;
import frc.robot.Autonomous.DefaultCommands.StandStillCommand;
//import frc.robot.subsystems.TestSubsystem;
import frc.robot.subsystems.DriveSubsystem;


public class BotControls {

	RobotContainer rContainer = RobotContainer.getInstance();
    ControlHub controlHub = ControlHub.getInstance();
    //TestSubsystem m_TestSubsystem = TestSubsystem.getInstance();
    boolean interruptedPPLib = false;
    boolean interruptedElevatorForward = false;
    boolean interruptedElevatorBackward = false;

    final static SendableChooser<Boolean> ControllerMode = new SendableChooser<>();
    public boolean OneControllerQuery = true;


    public final void PutControllerOption(){
        ControllerMode.addOption("One Controller", true);
        ControllerMode.addOption("Two Controller(s)", false);
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

    public void RunRobot(){
    if (OneControllerQuery == true){
            if (controlHub.driverController.A_Button.wasActivated()) {
                rContainer.m_robotDrive.zeroHeading();
            }
            
            if (controlHub.driverController.L_Bumper.isBeingPressed() || controlHub.driverController.R_Bumper.isBeingPressed()) {
                if(controlHub.driverController.L_Bumper.isBeingPressed() && !controlHub.driverController.R_Bumper.isBeingPressed()){
                    rContainer.m_Elevator.setLazyPercentageOpenLoop(-0.1);
                } 
                if (controlHub.driverController.L_Bumper.isBeingPressed() && !controlHub.driverController.L_Bumper.isBeingPressed()){
                    rContainer.m_Elevator.setLazyPercentageOpenLoop(0.1);
            } else{
                rContainer.m_Elevator.setLazyPercentageOpenLoop(0.0); 
            }  

        }else{
            // if (controlHub.operatorController.L_Bumper.wasActivated()) {
            //     rContainer.FallOffChain().schedule();
            // }else if (controlHub.operatorController.R_Bumper.wasActivated()){
            //     rContainer.ClimbChain().schedule();
            // }else if (controlHub.operatorController.X_Button.wasActivated()){
            //     rContainer.StowArm().schedule();
            // }else if (controlHub.operatorController.Y_Button.wasActivated()){
            //     rContainer.ScoreNote().schedule();
            // }else if (controlHub.driverController.L_Bumper.wasActivated()){
            //     rContainer.IntakeNotePrep().schedule();
            // }else if (controlHub.driverController.R_Bumper.wasActivated()){
            //     rContainer.IntakeNoteStow().schedule();
            // }
        }
    }
    
  }
}
