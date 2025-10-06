package frc.robot.commands;

import java.util.List;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.GoalEndState;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.path.Waypoint;
import com.pathplanner.lib.trajectory.PathPlannerTrajectoryState;

import edu.wpi.first.math.controller.HolonomicDriveController;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.trajectory.TrapezoidProfile.State;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Constants;
import frc.robot.Constants.AutoConstants;
import frc.robot.subsystems.DriveSubsystem;

public class autoAlign extends Command{
    private DriveSubsystem driveSubsystem;
    private Pose2d targetPose;
    public Command autoAlignCommand;
    public Field2d targetfield = new Field2d();
    public static PathConstraints defaultPathfindingConstraints = new PathConstraints(
        2.0,3.5, Units.degreesToRadians(540), Units.degreesToRadians(720));

    private final HolonomicDriveController holonomicDriveController;
    private final PIDController xController;
    private final PIDController yController;
    private final ProfiledPIDController rotController;
    


    public autoAlign(DriveSubsystem driveSubsystem, Pose2d targetPose){
        targetfield.setRobotPose(targetPose);
        SmartDashboard.putData("TargetField", targetfield);
        this.driveSubsystem = driveSubsystem;
        this.targetPose = targetPose;
        xController = new PIDController(.1, 0, 0);
        yController = new PIDController(.1, 0, 0);

        rotController = new ProfiledPIDController(1, 0, 0, new TrapezoidProfile.Constraints(3.5, 3.5));
        holonomicDriveController = new HolonomicDriveController(xController, yController, rotController);
        holonomicDriveController.setTolerance(new Pose2d(new Translation2d(0.1, 0.1),
                Rotation2d.fromDegrees(0)));
        addRequirements(DriveSubsystem.getInstance());
    }
    public Command positionPIDCommand(DriveSubsystem driveSubsystem, Pose2d goalPose){
        List<Waypoint> waypoints = PathPlannerPath.waypointsFromPoses(new Pose2d(driveSubsystem.getPose().getTranslation(), driveSubsystem.getTrueInitialRotation2dBasedOnAlliance()), goalPose);
        PathPlannerPath path = new PathPlannerPath(waypoints, defaultPathfindingConstraints, 
        null, 
        new GoalEndState(0, 
            Rotation2d.fromDegrees(goalPose.getRotation().getDegrees())
            ));
        path.preventFlipping = true;

        Trajectory.State targetState = new Trajectory.State();
        ChassisSpeeds chassis = holonomicDriveController.calculate(driveSubsystem.getPose(), targetPose, AutoConstants.kMaxSpeedMetersPerSecond, targetPose.getRotation());
        driveSubsystem.driveRobotRelative(holonomicDriveController.calculate(driveSubsystem.getPose(), targetState, targetPose.getRotation()));
        SwerveModuleState[] swerveModuleStates = Constants.DriveConstants.kDriveKinematics.toSwerveModuleStates(chassis);
        return (AutoBuilder.followPath(path)
            .andThen(new RunCommand(
                () -> driveSubsystem.setModuleStates(swerveModuleStates), driveSubsystem)));



    }

    @Override
    public void initialize(){
        //Pose2d currentPose = driveSubsystem.mono_getPoseVision_L();
        autoAlignCommand = AutoBuilder.pathfindToPose(targetPose, defaultPathfindingConstraints, 1.0);
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
        // driveSubsystem.drive(0, 0, 0, false, false);
    }
    @Override
    public boolean isFinished(){
        return autoAlignCommand == null || autoAlignCommand.isFinished();
    }
}