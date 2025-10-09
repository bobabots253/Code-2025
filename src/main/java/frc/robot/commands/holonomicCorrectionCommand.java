package frc.robot.commands;

import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import com.pathplanner.lib.trajectory.PathPlannerTrajectoryState;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Twist2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.networktables.BooleanPublisher;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.units.measure.Time;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.PPHolonomicConstants;
import frc.robot.subsystems.DriveSubsystem;

public class holonomicCorrectionCommand extends Command{
    
    private DriveSubsystem driveSubsystem; // = DriveSubsystem.getInstance();
    public final Pose2d goalPose;
    private PPHolonomicDriveController mDriveController = new PPHolonomicDriveController(
        PPHolonomicConstants.kTranslationPID, 
        PPHolonomicConstants.kRotationPID
    );

    private holonomicCorrectionCommand(DriveSubsystem driveSubsystem, Pose2d goalPose) {
        this.driveSubsystem = driveSubsystem;
        this.goalPose = goalPose;
        addRequirements(DriveSubsystem.getInstance());
    }

    public static Command generateCommand(DriveSubsystem driveSubsystem, Pose2d goalPose, Double timeout){
        return new holonomicCorrectionCommand(driveSubsystem, goalPose).withTimeout(timeout).finallyDo(() -> {
            driveSubsystem.drive(0,0,0,true, true);
        });
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        PathPlannerTrajectoryState goalState = new PathPlannerTrajectoryState();
        goalState.pose = goalPose;
        ChassisSpeeds requestSpeeds = mDriveController.calculateRobotRelativeSpeeds(
            driveSubsystem.getPose(), goalState);

        driveSubsystem.driveRobotRelative(requestSpeeds); //maybe use the normal drive functions
    }

    @Override
    public void end(boolean interrupted) {

    }

    @Override
    public boolean isFinished() {
        //use differential geometry bc of euclidean reasons
        Pose2d currentPose = new Pose2d(1.0, 1.0, new Rotation2d());
        Pose2d targetPose = new Pose2d(1.05, 1.02, Rotation2d.fromDegrees(5));

        //use difference
        Twist2d error = currentPose.log(targetPose);

        double dx = error.dx; //x-error meter
        double dy = error.dy; //y-error meter
        double dtheta = error.dtheta; //rot-error rads

        // You can then check each of these against your tolerances
        return (Math.hypot(dx, dy) < PPHolonomicConstants.kPositionTolerance &&
                Math.abs(dtheta) < PPHolonomicConstants.kRotationTolerance)
                ? true : false;
    }
}
