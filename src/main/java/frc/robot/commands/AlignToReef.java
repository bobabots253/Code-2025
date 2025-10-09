package frc.robot.commands;

import java.util.ArrayList;
import java.util.List;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathConstraints;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.units.TimeUnit;
import edu.wpi.first.units.measure.Time;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.FieldSetup;
import frc.robot.subsystems.DriveSubsystem;

public class AlignToReef extends Command{

    private DriveSubsystem driveSubsystem; // = DriveSubsystem.getInstance();
    public static boolean isPIDLoopRunning = false;
    private Pose2d targetPose;
    public Command autoAlignCommand;
    public double kAlignmentAdjustmentTimeout = 5; //Do not use edu.wpi.first.units.measure.Time;
    private Boolean isRight;
    List<Pose2d> rightReefTags = new ArrayList<Pose2d>();
    List<Pose2d> leftReefTags = new ArrayList<Pose2d>();
    public static PathConstraints defaultPathfindingConstraints = new PathConstraints(
        2.0,3.5, Units.degreesToRadians(540), Units.degreesToRadians(720));

    public AlignToReef(Boolean isRight) {
        this.driveSubsystem = driveSubsystem;
        addRequirements(DriveSubsystem.getInstance());
    }

    @Override
    public void initialize(){
        //Pose2d currentPose = driveSubsystem.mono_getPoseVision_L();
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
    public void execute(){
        Pose2d currentPos = DriveSubsystem.getInstance().getPose();
        if (isRight) {
            targetPose = currentPos.nearest(rightReefTags);
        } else{
            targetPose = currentPos.nearest(leftReefTags);
        }

        autoAlignCommand = AutoBuilder.pathfindToPose(
            targetPose, defaultPathfindingConstraints, 2.0).andThen(
                holonomicCorrectionCommand.generateCommand(driveSubsystem, targetPose, kAlignmentAdjustmentTimeout)
            );
    }
    @Override
    public void end(boolean interrupted){

    }

    @Override
    public boolean isFinished(){
        return autoAlignCommand.isFinished();
    }
}
