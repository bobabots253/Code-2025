// package frc.robot.commands;

// import com.pathplanner.lib.controllers.PPHolonomicDriveController;

// import edu.wpi.first.math.controller.HolonomicDriveController;
// import edu.wpi.first.math.controller.PIDController;
// import edu.wpi.first.math.controller.ProfiledPIDController;
// import edu.wpi.first.math.kinematics.ChassisSpeeds;
// import edu.wpi.first.math.kinematics.SwerveModuleState;
// import edu.wpi.first.wpilibj2.command.Command;
// import frc.robot.Constants;
// import frc.robot.Constants.AutoConstants;

// public class joshDrive extends Command {
//     //     private final DriveSubsystem driveRequire = RobotContainer.getInstance().m_robotDrive;

//         public static PPHolonomicDriveController holonomicDriveController;
//         public static PIDController xController;
//         public static PIDController yController;
//         public static ProfiledPIDController rotController;
        

//     @Override
//     public void execute() {
//         ChassisSpeeds chassisSpeeds = holonomicDriveController.calculate(driveRequire.getCurrentPose(), target.get(),
//                 AutoConstants.kMaxSpeedMetersPerSecond, target.get().getRotation());
//         SwerveModuleState[] swerveModuleStates = Constants.DriveConstants.kDriveKinematics
//                 .toSwerveModuleStates(chassisSpeeds);
//         driveRequire.setModuleStates(swerveModuleStates);
//     }
// //     public DriveToPose(Supplier<Pose2d> target, Pose2d tolerance) {
// //         this.target = target;
// //         this.tolerance = tolerance;

// //         xController = new PIDController(.01, 0, 0);
// //         yController = new PIDController(.01, 0, 0);

// //         rotController = new ProfiledPIDController(1, 0, 0, new TrapezoidProfile.Constraints(4, 4));

// //         holonomicDriveController = new HolonomicDriveController(xController, yController, rotController);
// //         holonomicDriveController.setTolerance(this.tolerance);

// //         addRequirements(driveRequire);
// //     }    
// }
