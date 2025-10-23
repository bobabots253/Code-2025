package frc.robot.Autonomous;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.util.ReadConstrainedTextBuffer;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.FieldSetup;
import frc.robot.RobotContainer;
import frc.robot.States;
import frc.robot.commands.autoAlign;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.EndEffectorSubsystem;

public class AutoModeBase {
    private static DriveSubsystem mDriveSubsystem;
        private static ElevatorSubsystem mElevatorSubsystem;
        private static EndEffectorSubsystem mEndEffectorSubsystem;
        List<Pose2d> rightReefTags = new ArrayList<Pose2d>();
        List<Pose2d> leftReefTags = new ArrayList<Pose2d>();
            
        public AutoModeBase (RobotContainer rContainer){
            this.mElevatorSubsystem = rContainer.m_Elevator;
            this.mEndEffectorSubsystem = rContainer.m_Effector;
            this.mDriveSubsystem = DriveSubsystem.getInstance();
    
            rightReefTags.add(FieldSetup.allianceReefBSupplier.get());
            rightReefTags.add(FieldSetup.allianceReefDSupplier.get());
            rightReefTags.add(FieldSetup.allianceReefFSupplier.get());
            rightReefTags.add(FieldSetup.allianceReefHSupplier.get());
            rightReefTags.add(FieldSetup.allianceReefJSupplier.get());
            rightReefTags.add(FieldSetup.allianceReefLSupplier.get());
            
            leftReefTags.add(FieldSetup.allianceReefASupplier.get());
            leftReefTags.add(FieldSetup.allianceReefCSupplier.get());
            leftReefTags.add(FieldSetup.allianceReefESupplier.get());
            leftReefTags.add(FieldSetup.allianceReefGSupplier.get());
            leftReefTags.add(FieldSetup.allianceReefISupplier.get());
            leftReefTags.add(FieldSetup.allianceReefKSupplier.get());
            }
       
       public static Command coralRollersAuto(double requestedSpeed){
                   return Commands.sequence(
                       new InstantCommand(() -> {
                           mEndEffectorSubsystem.setIntakeLazyPercentageOpenLoop(requestedSpeed);
                         }, mEndEffectorSubsystem)
           );
       }
       
       public static Command setElevatorStowAuto(){
           return Commands.sequence(
               new RunCommand(() -> {
                   mElevatorSubsystem.setLazyElevatorState(States.ElevatorPos.STOW);
               }, mElevatorSubsystem)
            );
        }
       
        public static Command setElevatorL1Auto(){
            return Commands.sequence(
                new RunCommand(() -> {
                    mElevatorSubsystem.setLazyElevatorState(States.ElevatorPos.L1Score);
                }, mElevatorSubsystem)
            );
        }
       
        public Command setElevatorL2Auto(){
            return Commands.sequence(
                new RunCommand(() -> {
                    mElevatorSubsystem.setLazyElevatorState(States.ElevatorPos.L2Score);
                }, mElevatorSubsystem)
            );
        }
       
        public Command setElevatorL3Auto(){
            return Commands.sequence(
                new RunCommand(() -> {
                    mElevatorSubsystem.setLazyElevatorState(States.ElevatorPos.L3SCORE);
                }, mElevatorSubsystem)
            );
        }
       
        public Command flickL1CoralAuto(){
            return Commands.sequence(
                coralRollersAuto(0.7).withTimeout(0.075),
                new WaitCommand(0.1),
                coralRollersAuto(0.7).withTimeout(2.0)
            );
        }
       
        public static Command rollerDefaultScore(){
            return Commands.sequence(
                coralRollersAuto(1.0)
            );
        }
    
        public static Command rollerDefaultStop(){
            return Commands.sequence(
                coralRollersAuto(0.0)
            );
        }
    
        public static Command spinMove(){
            return mDriveSubsystem.spinMoveCommand(2);
        // Command.sequence(
        //     new RunCommand(() -> {
        //         mDriveSubsystem.spinMoveCommand(2);
        //     }, mDriveSubsystem)
        // );
    }

    public static Command PIDPathfindToPose(Pose2d targetPose){
        return new autoAlign(mDriveSubsystem, targetPose);
    }

    // public Command setElevatorL1Auto(double requestedSpeed, double setTimeout){
    //     return Commands.sequence(
    //         new RunCommand(() -> {
    //             mElevatorSubsystem.setLazyElevatorState(States.ElevatorPos.L2Score);
    //         }, mElevatorSubsystem)
    //     );
    // }
}
