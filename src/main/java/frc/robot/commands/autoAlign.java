package frc.robot.commands;

import java.util.List;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.GoalEndState;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.path.Waypoint;

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
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
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
    public SwerveModuleState[] input;
    public ChassisSpeeds chassis;
    

    public autoAlign(DriveSubsystem driveSubsystem, Pose2d targetPose){
        targetfield.setRobotPose(targetPose);
        SmartDashboard.putData("TargetField", targetfield);
        this.driveSubsystem = driveSubsystem;
        this.targetPose = targetPose;
        xController = new PIDController(3, 0, 0.5); //Max Low Accuracy: 3.5, 0, 0.25
        yController = new PIDController(3, 0, 0.5); //Testing: 3.5, 0, 0.5

        rotController = new ProfiledPIDController(2, 0, 0, new TrapezoidProfile.Constraints(5, 5));
        holonomicDriveController = new HolonomicDriveController(xController, yController, rotController);
        holonomicDriveController.setTolerance(new Pose2d(new Translation2d(0.03, 0.03),
                Rotation2d.fromDegrees(1)));

        addRequirements(DriveSubsystem.getInstance());
    }

    @Override
    public void initialize(){
    }

    public SwerveModuleState[] positionPIDCommand(DriveSubsystem driveSubsystem, Pose2d goalPose){
        ChassisSpeeds chassis = holonomicDriveController.calculate(driveSubsystem.getPose(), targetPose, 0, targetPose.getRotation());
        SwerveModuleState[] swerveModuleStates = Constants.DriveConstants.kDriveKinematics.toSwerveModuleStates(chassis);
        return swerveModuleStates;
    }


    @Override
    public void execute(){
        chassis = holonomicDriveController.calculate(driveSubsystem.getPose(), targetPose, 0.0, targetPose.getRotation());
        input = Constants.DriveConstants.kDriveKinematics.toSwerveModuleStates(chassis);
        driveSubsystem.setModuleStates(input);
        // autoAlignCommand = new RunCommand(() -> driveSubsystem.setModuleStates(input), driveSubsystem);
        System.out.println("running");

    }
    @Override
    public void end(boolean interrupted){
    }

    @Override
    public boolean isFinished(){
        return holonomicDriveController.atReference();
    }

}
