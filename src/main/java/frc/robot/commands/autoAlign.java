package frc.robot.commands;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathConstraints;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveSubsystem;

public class autoAlign extends Command{
    private DriveSubsystem driveSubsystem;
    private Pose2d targetPose;
    public Command autoAlignCommand;
    public Field2d targetfield = new Field2d();
    RobotContainer rContainer = RobotContainer.getInstance();
    public static PathConstraints defaultPathfindingConstraints = new PathConstraints(3.5,4.0, Units.degreesToRadians(540), Units.degreesToRadians(720));

    public autoAlign(DriveSubsystem driveSubsystem, Pose2d targetPose){
        targetfield.setRobotPose(targetPose);
        SmartDashboard.putData("TargetField", targetfield);
        this.driveSubsystem = driveSubsystem;
        this.targetPose = targetPose;
        addRequirements(DriveSubsystem.getInstance());
    }

    @Override
    public void initialize(){
        //Pose2d currentPose = driveSubsystem.mono_getPoseVision_L();
        autoAlignCommand = AutoBuilder.pathfindToPose(targetPose, defaultPathfindingConstraints, 0.0);
        autoAlignCommand.schedule();

    }
    @Override
    public void execute(){

    }
    @Override
    public void end(boolean interrupted){
        if(autoAlignCommand == null){
            autoAlignCommand.cancel();
        }
        driveSubsystem.drive(0, 0, 0, false, false);
    }
    @Override
    public boolean isFinished(){
        return autoAlignCommand == null || autoAlignCommand.isFinished();
    }
    public Command autoScore(){
        return new SequentialCommandGroup(rContainer.autoRightAlign(), rContainer.tierTwoScoreCommand());
    }
}
