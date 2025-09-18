// package frc.robot.limelights;

// import static edu.wpi.first.units.Units.Seconds;

// import com.pathplanner.lib.controllers.PPHolonomicDriveController;

// import edu.wpi.first.math.MathUtil;
// import edu.wpi.first.math.geometry.Pose2d;
// import edu.wpi.first.math.kinematics.ChassisSpeeds;
// import edu.wpi.first.units.measure.Time;
// import edu.wpi.first.wpilibj2.command.Command;
// import edu.wpi.first.wpilibj2.command.PIDCommand;
// import edu.wpi.first.wpilibj2.command.button.Trigger;
// import frc.robot.subsystems.DriveSubsystem;

// public class JoshCommand extends Command{
//     public DriveSubsystem mSwerve;
//     public final Pose2d goalPose;
//     //private PPHolonomicDriveController mDriveController = Drive.AutoConstants.kDriveController;

//     private final Trigger endTrigger;
//     private final Trigger endTriggerDebounced;
//     private JoshCommand(DriveSubsystem mSwerve, Pose2d goalPose) {
//         this.mSwerve = mSwerve;
//         this.goalPose = goalPose;

//         endTrigger = new Trigger(() -> {
//             Pose2d diff = mSwerve.getPose().relativeTo(goalPose);

//             var rotation = MathUtil.isNear(
//                 0.0, 
//                 diff.getRotation().getRotations(), 
//                 kRotationTolerance.getRotations(), 
//                 0.0, 
//                 1.0
//             );

//             var position = diff.getTranslation().getNorm() < kPositionTolerance.in(Meters);

//             var speed = mSwerve.getSpeed() < kSpeedTolerance.in(MetersPerSecond);

//             System.out.println("end trigger conditions R: "+ rotation + "\tP: " + position + "\tS: " + speed);
            
//             return rotation && position && speed;
//         });

//         endTriggerDebounced = endTrigger.debounce(kEndTriggerDebounce.in(Seconds));
//     }
//     public class Command generate(DriveSubsystem swerve, Pose2d goal, Time timeout){
//         return new JoshCommand(swerve, goal).withTimeout(timeout).finallyDo(() -> {
//             swerve.drive(new ChassisSpeeds(0,0,0), false, false);
            
//         });
//     }
// }