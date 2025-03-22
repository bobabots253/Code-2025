package frc.robot.commands;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.GoalEndState;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.path.Waypoint;
import com.pathplanner.lib.trajectory.PathPlannerTrajectory;
import com.pathplanner.lib.util.PPLibTelemetry;

import edu.wpi.first.math.controller.HolonomicDriveController;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.FieldSetup;
import frc.robot.RobotContainer;
import frc.robot.Constants.PPLibConstants;
import frc.robot.subsystems.DriveSubsystem;

public class PathfindClosest extends Command {
    private Pose2d target;
    private boolean runCommand = false;
    public RunCommand newCommand;
    private final HolonomicDriveController holonomicDriveController;
    private final PIDController xController;
    private final PIDController yController;
    private final ProfiledPIDController rotController;
    Command pathfindingCommand;
    PathPlannerPath path;
    PathConstraints constraints;

    //Note: Possibel Fix for Invalid Static Reference to DriveSubsys which has been causing the runtime crash
    // Vision Pose Estimation works but gets interefered by "estimated velocities"
    // Sometimes the position gets flipped which is unideal (find fix later)
    public PathfindClosest(boolean runCommand) {
        this.runCommand = runCommand;
        xController = new PIDController(.1, 0, 0);
        yController = new PIDController(.1, 0, 0);

        rotController = new ProfiledPIDController(1, 0, 0, new TrapezoidProfile.Constraints(3.5, 3.5));
        holonomicDriveController = new HolonomicDriveController(xController, yController, rotController);
        holonomicDriveController.setTolerance(FieldSetup.kReefFarEntranceTolerance);
        addRequirements(DriveSubsystem.getInstance());

        List<Pose2d> reefTags = new ArrayList<Pose2d>();
        reefTags.add(FieldSetup.allianceReefASupplier.get());
        reefTags.add(FieldSetup.allianceReefBSupplier.get());
        reefTags.add(FieldSetup.allianceReefCSupplier.get());
        reefTags.add(FieldSetup.allianceReefDSupplier.get());
        reefTags.add(FieldSetup.allianceReefESupplier.get());
        reefTags.add(FieldSetup.allianceReefFSupplier.get());
        reefTags.add(FieldSetup.allianceReefGSupplier.get());
        reefTags.add(FieldSetup.allianceReefHSupplier.get());
        reefTags.add(FieldSetup.allianceReefISupplier.get());
        reefTags.add(FieldSetup.allianceReefJSupplier.get());
        reefTags.add(FieldSetup.allianceReefKSupplier.get());
        reefTags.add(FieldSetup.allianceReefLSupplier.get());

        // PathConstraints constraints = PPLibConstants.handoffReefAlignmentConstraints;
        // Pose2d closestRealPose2d = RobotContainer.m_robotDrive.mono_getPoseVision_L();
        // target = closestRealPose2d.nearest(reefTags);

        // pathfindingCommand = AutoBuilder.pathfindToPose(
        //     target,
        //     constraints,
        //     0.0
        //     );

        List<Waypoint> waypoints = PathPlannerPath.waypointsFromPoses(
        new Pose2d(5.750, 4.185, Rotation2d.fromDegrees(180)),
        new Pose2d(5.675, 4.185, Rotation2d.fromDegrees(180))
        );

        constraints = new PathConstraints(3.0, 3.0, 2 * Math.PI, 4 * Math.PI); // The constraints for this path.
        // PathConstraints constraints = PathConstraints.unlimitedConstraints(12.0); // You can also use unlimited constraints, only limited by motor torque and nominal battery voltage

// Create the path using the waypoints created above
path = new PathPlannerPath(
        waypoints,
        constraints,
        null, // The ideal starting state, this is only relevant for pre-planned paths, so can be null for on-the-fly paths.
        new GoalEndState(0.0, Rotation2d.fromDegrees(0)) // Goal end state. You can set a holonomic rotation here. If using a differential drivetrain, the rotation will have no effect.
);

// Prevent the path from being flipped if the coordinates are already correct
path.preventFlipping = true;
    }


    @Override
    public void execute() {
        if (runCommand == false){
            //pathfindingCommand.end(true);
            end(true);
            System.out.println("PathFinding_Ended_Early");
        } else if (runCommand == true) { 
            //pathfindingCommand.schedule();
            AutoBuilder.pathfindThenFollowPath(path, constraints);
        }
    }

    @Override
    public void end(boolean interrupted) {
        runCommand = false;
    }

    @Override
    public boolean isFinished() {
        return pathfindingCommand.isFinished();
    }
}

