// package frc.robot.commands;

// import java.nio.file.Path;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.function.Supplier;

// import com.pathplanner.lib.auto.AutoBuilder;
// import com.pathplanner.lib.path.GoalEndState;
// import com.pathplanner.lib.path.PathConstraints;
// import com.pathplanner.lib.path.PathPlannerPath;
// import com.pathplanner.lib.path.Waypoint;
// import com.pathplanner.lib.trajectory.PathPlannerTrajectory;
// import com.pathplanner.lib.util.PPLibTelemetry;

// import edu.wpi.first.math.controller.HolonomicDriveController;
// import edu.wpi.first.math.controller.PIDController;
// import edu.wpi.first.math.controller.ProfiledPIDController;
// import edu.wpi.first.math.geometry.Pose2d;
// import edu.wpi.first.math.geometry.Rotation2d;
// import edu.wpi.first.math.geometry.Translation2d;
// import edu.wpi.first.math.geometry.Twist2d;
// import edu.wpi.first.math.kinematics.ChassisSpeeds;
// import edu.wpi.first.math.trajectory.TrapezoidProfile;
// import edu.wpi.first.math.util.Units;
// import edu.wpi.first.util.sendable.Sendable;
// import edu.wpi.first.wpilibj.DriverStation;
// import edu.wpi.first.wpilibj.DriverStation.Alliance;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import edu.wpi.first.wpilibj2.command.Command;
// import edu.wpi.first.wpilibj2.command.RunCommand;
// import frc.robot.FieldSetup;
// import frc.robot.RobotContainer;
// import frc.robot.Constants;
// import frc.robot.Constants.DriveConstants;
// import frc.robot.Constants.PPLibConstants;
// import frc.robot.subsystems.DriveSubsystem;

// public class PathfindClosest extends Command {
//     private Pose2d target;
//     private boolean runCommand = false;
//     public RunCommand newCommand;
//     private Boolean isRight;
//     Pose2d closestRealPose2d;
//     Command pathfindingCommand;
//     PathPlannerPath path;
//     Command runnable;
//     PathConstraints constraints;
//     Command waypointfinder;
//     List<Pose2d> rightReefTags = new ArrayList<Pose2d>();
//     List<Pose2d> leftReefTags = new ArrayList<Pose2d>();

//     //Note: Possibel Fix for Invalid Static Reference to DriveSubsys which has been causing the runtime crash
//     // Vision Pose Estimation works but gets interefered by "estimated velocities"
//     // Sometimes the position gets flipped which is unideal (find fix later)
//     public PathfindClosest(boolean runCommand,Boolean isRight) {
//         this.isRight = isRight;
//         this.runCommand = runCommand;
//         this.closestRealPose2d = RobotContainer.m_robotDrive.getRefinedPoseVision();
//         addRequirements(DriveSubsystem.getInstance());

//         rightReefTags.add(FieldSetup.allianceReefBSupplier.get());
//         rightReefTags.add(FieldSetup.allianceReefDSupplier.get());
//         rightReefTags.add(FieldSetup.allianceReefESupplier.get());
//         rightReefTags.add(FieldSetup.allianceReefHSupplier.get());
//         rightReefTags.add(FieldSetup.allianceReefJSupplier.get());
//         rightReefTags.add(FieldSetup.allianceReefKSupplier.get());

//         leftReefTags.add(FieldSetup.allianceReefASupplier.get());
//         leftReefTags.add(FieldSetup.allianceReefCSupplier.get());
//         leftReefTags.add(FieldSetup.allianceReefFSupplier.get());
//         leftReefTags.add(FieldSetup.allianceReefGSupplier.get());
//         leftReefTags.add(FieldSetup.allianceReefISupplier.get());
//         leftReefTags.add(FieldSetup.allianceReefLSupplier.get());
//         SmartDashboard.putData((Sendable) target);
//     }

//     public Command generateApproachReachCommand(){
//         target = getClosetApproachByOrientation();
//         // target = FieldSetup.allianceReefHSupplier.get();

//         List<Waypoint> waypoints =
//         PathPlannerPath.waypointsFromPoses(
//             target,
//             target);
        
//             //target.exp(new Twist2d(0.5, 0, 0))
        
//         //Create the path using the waypoints created above
//         path = new PathPlannerPath(
//                 waypoints,
//                 PPLibConstants.defaultPathfindingConstraints,
//                 null, // The ideal starting state, this is only relevant for pre-planned paths, so can be null for on-the-fly paths.
//                 new GoalEndState(0.0, Rotation2d.fromDegrees(target.getRotation().getDegrees())) // Goal end state. You can set a holonomic rotation here. If using a differential drivetrain, the rotation will have no effect.
//         );      

//         path.preventFlipping = true;
//         return generateOTFPathCommand(path);
//     }


//     public Pose2d getClosetApproachByOrientation(){
//         if(isRight){
//             target = closestRealPose2d.nearest(rightReefTags);
//         }else if (!isRight){
//             target = closestRealPose2d.nearest(leftReefTags);
//         }
//         return target;
//     }

//     public static Command generateOTFPoseCommand(Pose2d pose) {
//         return AutoBuilder.pathfindToPose(pose, Constants.PPLibConstants.handoffReefAlignmentConstraints);
//     }

//     public static Command generateOTFPathCommand(PathPlannerPath path) {
//         return AutoBuilder.pathfindThenFollowPath(path, Constants.PPLibConstants.finalAlignmentConstraints);
//     }


//     @Override
//     public void execute() {
//         getClosetApproachByOrientation();
//         this.runnable = generateApproachReachCommand();
//         if (runCommand == false){
//             runnable.end(true);
//             end(true);
//             System.out.println("PathFinding_Ended_Early");
//         } else if (runCommand == true) { 
//             runnable.schedule();
//         }
//     }

//     @Override
//     public void end(boolean interrupted) {
//         runCommand = false;
//     }

//     @Override
//     public boolean isFinished() {
//         return runnable.isFinished();
//     }
// }

