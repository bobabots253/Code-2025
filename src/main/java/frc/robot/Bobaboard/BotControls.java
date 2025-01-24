package frc.robot.Bobaboard;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.RobotContainer;
//import frc.robot.subsystems.TestSubsystem;
import frc.robot.subsystems.DriveSubsystem;


public class BotControls {

	RobotContainer rContainer = RobotContainer.getInstance();
    ControlHub controlHub = ControlHub.getInstance();
    //TestSubsystem m_TestSubsystem = TestSubsystem.getInstance();
    boolean interruptedPPLib = false;

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
        SmartDashboard.putBoolean("m_ControllerSelected", OneControllerQuery);
        SmartDashboard.putBoolean("Driver A Button", controlHub.driverController.A_Button.isBeingPressed());
        SmartDashboard.putBoolean("Driver B Button", controlHub.driverController.X_Button.isBeingPressed());
        SmartDashboard.putBoolean("Driver X Button", controlHub.driverController.X_Button.isBeingPressed());
        SmartDashboard.putBoolean("Driver Y Button", controlHub.driverController.Y_Button.isBeingPressed());
        SmartDashboard.putBoolean("Operator X Button", controlHub.operatorController.X_Button.isBeingPressed());
        SmartDashboard.putBoolean("Operator Y Button", controlHub.operatorController.Y_Button.isBeingPressed());
        SmartDashboard.putBoolean("Driver LBumper", controlHub.driverController.L_Bumper.isBeingPressed());
        SmartDashboard.putBoolean("Driver RBumper", controlHub.driverController.R_Bumper.isBeingPressed());
    }

    public void RunRobot(){
    if (OneControllerQuery == true){
            if (controlHub.driverController.A_Button.wasActivated()) {
                System.out.println("A Button Detected");
                //rContainer.Score().schedule();
            }

            if (controlHub.driverController.X_Button.wasActivated()){
                interruptedPPLib = !interruptedPPLib;
                RobotContainer.PathFindAmp(interruptedPPLib).schedule();
            }

            
            
            if (controlHub.driverController.L_Bumper.isBeingPressed()){
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
