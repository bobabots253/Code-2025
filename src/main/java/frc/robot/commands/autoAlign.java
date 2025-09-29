package frc.robot.commands;

import java.util.ArrayList;
import java.util.List;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathConstraints;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.FieldSetup;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveSubsystem;

public class autoAlign extends Command{
    private DriveSubsystem driveSubsystem;
    private Pose2d targetPose = new Pose2d();
    public Command autoAlignCommand;
    private Pose2d currentPose;
    public Pose2d closestPose;
    public Field2d targetfield = new Field2d();
    // RobotContainer rContainer = RobotContainer.getInstance();
    List<Pose2d> rightReefTags = new ArrayList<Pose2d>();
    List<Pose2d> leftReefTags = new ArrayList<Pose2d>();
    private Boolean isRight;
    public static PathConstraints defaultPathfindingConstraints = new PathConstraints(3.5,4.0, Units.degreesToRadians(540), Units.degreesToRadians(720));

    public autoAlign(DriveSubsystem driveSubsystem, Boolean right){
        targetfield.setRobotPose(targetPose);
        SmartDashboard.putData("TargetField", targetfield);
        this.driveSubsystem = driveSubsystem;
        this.isRight = right;
        this.currentPose = RobotContainer.m_robotDrive.getPose();
        addRequirements(DriveSubsystem.getInstance());

        rightReefTags.add(FieldSetup.allianceReefBSupplier.get());
        rightReefTags.add(FieldSetup.allianceReefDSupplier.get());
        rightReefTags.add(FieldSetup.allianceReefESupplier.get());
        rightReefTags.add(FieldSetup.allianceReefHSupplier.get());
        rightReefTags.add(FieldSetup.allianceReefJSupplier.get());
        rightReefTags.add(FieldSetup.allianceReefKSupplier.get());

        leftReefTags.add(FieldSetup.allianceReefASupplier.get());
        leftReefTags.add(FieldSetup.allianceReefCSupplier.get());
        leftReefTags.add(FieldSetup.allianceReefFSupplier.get());
        leftReefTags.add(FieldSetup.allianceReefGSupplier.get());
        leftReefTags.add(FieldSetup.allianceReefISupplier.get());
        leftReefTags.add(FieldSetup.allianceReefLSupplier.get());
    }

    @Override
    public void initialize(){
        //Pose2d currentPose = driveSubsystem.mono_getPoseVision_L();
        targetPose = getClosetApproachByOrientation();
        autoAlignCommand = AutoBuilder.pathfindToPose(targetPose, defaultPathfindingConstraints, 0.0);
        autoAlignCommand.schedule();

    }

    public Pose2d getClosetApproachByOrientation(){
        if(isRight){
            closestPose = currentPose.nearest(rightReefTags);
        }else if (!isRight){
            closestPose = currentPose.nearest(leftReefTags);
        }
        return closestPose;
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
    // public Command autoScore(){
    //     return new SequentialCommandGroup(rContainer.autoRightAlign(), rContainer.tierTwoScoreCommand());
    // }
}
