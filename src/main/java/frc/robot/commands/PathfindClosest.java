package frc.robot.commands;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;
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
import frc.robot.subsystems.DriveSubsystem;

public class PathfindClosest extends Command {
    private Pose2d target;
    private Pose2d tolerance;
    private boolean runCommand = false;
    private final DriveSubsystem driveRequire = RobotContainer.getInstance().m_robotDrive;

    // private final HolonomicDriveController holonomicDriveController;
    // private final PIDController xController;
    // private final PIDController yController;
    // private final ProfiledPIDController rotController;
    private PathPlannerPath currentPath;
    private PathPlannerTrajectory currentTrajectory;
    private double timeOffset = 0;
    public RunCommand newCommand;

    //Note: Possibel Fix for Invalid Static Reference to DriveSubsys which has been causing the runtime crash
    // Vision Pose Estimation works but gets interefered by "estimated velocities"
    // Sometimes the position gets flipped which is unideal (find fix later)
    public PathfindClosest(Pose2d target, Pose2d tolerance, boolean runCommand) {
        this.target = target;
        this.tolerance = tolerance;
        this.runCommand = runCommand;
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
            // xController = new PIDController(.1, 0, 0);
        // yController = new PIDController(.1, 0, 0);

        // rotController = new ProfiledPIDController(1, 0, 0, new TrapezoidProfile.Constraints(3.5, 3.5));
        // holonomicDriveController = new HolonomicDriveController(xController, yController, rotController);
        // holonomicDriveController.setTolerance(this.tolerance);
        addRequirements(driveRequire);
    }


    @Override
    public void execute() {
        currentTrajectory = null;
        timeOffset = 0;
        PathConstraints constraints = new PathConstraints(3.0, 4.0,
        Units.degreesToRadians(540),
        Units.degreesToRadians(720));
        Pose2d currentPose = driveRequire.getRefinedPoseVision();
        Pose2d closestRealPose2d = new Pose2d();
        reefTags.closest
        Command pathfindingCommand = AutoBuilder.pathfindToPose(
        target,
        constraints,
        0.0
        );
        
        // Command pathFinish;
        // PathPlannerPath pathBFinishCommand;
        // PathPlannerPath pathRFinishCommand;

        // pathBFinishCommand = PathPlannerPath.fromPathFile("B_AmpFinish");
        // pathRFinishCommand = PathPlannerPath.fromPathFile("R_AmpFinish");
        // System.out.println("Failed to Fetch Amp Files");
        
        // var alliance = DriverStation.getAlliance();
        // if (alliance.isPresent()) {
        //   if (alliance.get() == DriverStation.Alliance.Blue){
        //     pathFinish = AutoBuilder.followPath(pathBFinishCommand);
        //   }else {
        //     pathFinish = AutoBuilder.followPath(pathRFinishCommand);
        //   }

            if (runCommand == false){
                pathfindingCommand.end(true);
                System.out.println("PathFinding_Ended_Early");
            } else if (runCommand == true) { 
                pathfindingCommand.schedule();
                // pathFinish.schedule();
            }
        }

    

    @Override
    public void end(boolean interrupted) {
        driveRequire.Xmode();
    }
}

