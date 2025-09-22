// package frc.robot.limelights;

// import edu.wpi.first.math.kinematics.ChassisSpeeds;
// import edu.wpi.first.math.util.Units;
// import frc.robot.LimelightHelpers;
// import frc.robot.RobotContainer;
// import frc.robot.subsystems.DriveSubsystem;
// import frc.robot.Constants;
// import frc.robot.Constants.VisionConstants;

// public class VisionData {
//     public final String name;
//     public final LimelightHelpers.PoseEstimate MegaTag;
//     public final LimelightHelpers.PoseEstimate MegaTag2;
//     public Boolean canTrustRotation;
//     public Boolean canTrustPosition;

//     public boolean optimized;

//     public VisionData(String name, LimelightHelpers.PoseEstimate MegaTag, LimelightHelpers.PoseEstimate MegaTag2) {
//         this.name = name;
//         this.MegaTag = MegaTag;
//         this.MegaTag2 = MegaTag2;
//         this.optimized = false;

//         // These are calculated when the measurement is created
//         this.canTrustRotation = canTrustRotation();
//         this.canTrustPosition = canTrustPosition();
//     }

//     //Makes sure: Distance <= 3 meters ; Angular <= 180 deg/s ; Translational <= 2 m/s
//     public boolean canTrustRotation() {
//         if (this.canTrustRotation == null) {
//             this.canTrustRotation = 
//             this.MegaTag != null
//             && this.MegaTag2 != null;  
//         // return
//         // // this.MegaTag2.avgTagDist <= 3 // 3 Meters
//         // // && this.MegaTag != null
//         // // && this.MegaTag.tagCount >= 2
//         // this.MegaTag != null
//         // && this.MegaTag2 != null;   
//         }
//         return this.canTrustRotation;
//     }

//     /**
//      * Checks if the MegaTag2 Pose2d is within distance tolerance relative to the bot's position.
//      * @return Whether position data can be trusted.
//      */
//     public boolean canTrustPosition() {
//         if (this.canTrustPosition == null) {
//             this.canTrustPosition =
//             this.MegaTag2 != null
//             && this.MegaTag2.tagCount > 0
//             && this.MegaTag2.avgTagDist < VisionConstants.TRUSTWORTHY_DISTANCE;
//         }
//         return this.canTrustPosition;
//         // this.MegaTag2.tagCount > 0
//         // && this.MegaTag2.avgTagDist < VisionConstants.TRUSTWORTHY_DISTANCE;
//     }
// }
