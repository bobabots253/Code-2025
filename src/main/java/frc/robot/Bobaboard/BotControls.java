package frc.robot.Bobaboard;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Constants.EndEffectorConstants;
import frc.robot.RobotContainer;
import frc.robot.Autonomous.DefaultCommands.StandStillCommand;
//import frc.robot.subsystems.TestSubsystem;
import frc.robot.subsystems.DriveSubsystem;


public class BotControls {

	RobotContainer rContainer = RobotContainer.getInstance();
    ControlHub controlHub = ControlHub.getInstance();

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
            if (controlHub.driverController.L_Bumper.wasActivated()) {
                rContainer.m_robotDrive.zeroHeading();
            }
            
            if(controlHub.driverController.A_Button.wasReleased() || controlHub.driverController.B_Button.wasReleased()
            || controlHub.driverController.A_Button.isNotBeingPressed() || controlHub.driverController.B_Button.isNotBeingPressed()){
                rContainer.m_Elevator.setSafePercentageOpenLoop(0.05);
            }

            if (controlHub.driverController.A_Button.isBeingPressed() || controlHub.driverController.B_Button.isBeingPressed()) {
                if(controlHub.driverController.A_Button.isBeingPressed() && !controlHub.driverController.B_Button.isBeingPressed()){
                    rContainer.m_Elevator.setSafePercentageOpenLoop(-0.075);
                } 
                else if (controlHub.driverController.B_Button.isBeingPressed() && !controlHub.driverController.A_Button.isBeingPressed()){
                    rContainer.m_Elevator.setSafePercentageOpenLoop(0.15);
                }
            }

            if(controlHub.driverController.Y_Button.wasReleased() || controlHub.driverController.X_Button.wasReleased()
                || controlHub.driverController.Y_Button.isNotBeingPressed() || controlHub.driverController.X_Button.isNotBeingPressed()){
                    rContainer.m_Effector.setIntakeLazyPercentageOpenLoop(0);
                }
            if (controlHub.driverController.X_Button.isBeingPressed() && !controlHub.driverController.Y_Button.isBeingPressed()){
                    rContainer.m_Effector.setIntakeLazyPercentageOpenLoop(0.9);
                } else if (controlHub.driverController.Y_Button.isBeingPressed() && !controlHub.driverController.X_Button.isBeingPressed()){
                    rContainer.m_Effector.setIntakeLazyPercentageOpenLoop(0.7);
            }

        }else{
// 2 Controller Here
        // Driver Controls

        //Resets the virtual heading based on the current heading (fixes drift)
        if (controlHub.driverController.Y_Button.wasActivated()) {
                rContainer.m_robotDrive.zeroHeading();
            }

        // Operator Side

        //Polls for Controller Input, if a button is being pressed deliver selected intake/extake speed
        if(controlHub.operatorController.L_Bumper.wasReleased() || controlHub.operatorController.R_Bumper.wasReleased()
                && controlHub.operatorController.L_Bumper.isNotBeingPressed() && controlHub.operatorController.R_Bumper.isNotBeingPressed()){
                rContainer.m_Effector.setIntakeLazyPercentageOpenLoop(0);
        } else{
            if (controlHub.operatorController.L_Bumper.isBeingPressed() && !controlHub.operatorController.R_Bumper.isBeingPressed()) {
                rContainer.m_Effector.setIntakeLazyPercentageOpenLoop(0.7);
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
                    rContainer.tierOneElevatorCommand().schedule();
            }else if (controlHub.operatorController.X_Button.wasActivated() && !controlHub.operatorController.A_Button.isBeingPressed()
                        && !controlHub.operatorController.B_Button.isBeingPressed() && !controlHub.operatorController.Y_Button.isBeingPressed()){
                    rContainer.tierTwoElevatorCommand().schedule();
            }else if (controlHub.operatorController.Y_Button.wasActivated() && !controlHub.operatorController.B_Button.isBeingPressed()
                        && !controlHub.operatorController.X_Button.isBeingPressed() && !controlHub.operatorController.A_Button.isBeingPressed()){
                    rContainer.tierThreeElevatorCommand().schedule();
                }
            }

            if(!controlHub.operatorController.L_Trigger.isBeingPressed() && !controlHub.operatorController.R_Trigger.isBeingPressed()){
                rContainer.m_Climb.setLazyOpenLoop(0);
            }else if (controlHub.operatorController.L_Trigger.isBeingPressed()){
                rContainer.m_Climb.setLazyOpenLoop(1);
            }else if (controlHub.operatorController.R_Trigger.isBeingPressed()){
                rContainer.m_Climb.setLazyOpenLoop(-1);
            }


            //Polls for Controller Input, if a button is being pressed deliver selected algae pivot angle
            // if (!controlHub.operatorController.POV0.isBeingPressed() && !controlHub.operatorController.POV90.isBeingPressed()){
            //     rContainer.stowAlgaeCommand().schedule();
            // }else{
            //     if(controlHub.operatorController.POV0.isBeingPressed()){
            //     rContainer.stowAlgaeCommand().schedule();
            // }else if(controlHub.operatorController.POV90.isBeingPressed()){
            //     rContainer.algaeExtendCommand().schedule();
            //     rContainer.m_Effector.setLazyPivotPositionSetpoint(EndEffectorConstants.extendedPIvotPosition);
            // }
        
        

            // if (!controlHub.operatorController.POV0.isBeingPressed()){
            //     rContainer.m_Effector.
            // }else{

            // if (controlHub.operatorController.POV0.wasActivated()){
            //         rContainer.stowElevatorCommand().schedule();
            // }else if (controlHub.operatorController.POV90.wasActivated() && !controlHub.operatorController.B_Button.isBeingPressed()
            //             && !controlHub.operatorController.X_Button.isBeingPressed() && !controlHub.operatorController.Y_Button.isBeingPressed()) {
            //         rContainer.tierOneElevatorCommand().schedule();
            // }

        }
  }
    } 



