package frc.robot.limelights;

import com.studica.frc.AHRS;
import com.studica.frc.AHRS.NavXComType;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.LimelightHelpers;
import frc.robot.LimelightHelpers.PoseEstimate;

public class JoshVision {
    // Pose2d position = getData();
}

// public class JoshVision{
//     Pose2d currentPose = getData();
//     Field2d joshVisionMeasurment = new Field2d();
//     AHRS navx = new AHRS(NavXComType.kMXP_SPI);
//     public double currentYaw(){
//         double yaw = -navx.getRotation2d().getDegrees();
//         if(DriverStation.getAlliance().orElse(DriverStation.Alliance.Red) == DriverStation.Alliance.Red){
//             yaw+=180;
//         }
//         return yaw;
//     }
//     public Pose2d getData(){
//         LimelightHelpers.SetRobotOrientation(Constants.VisionConstants.FRONT_LEFT_APRIL_TAG_LL, currentYaw(), 0, 0, 0, 0, 0);
//         PoseEstimate translation = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(Constants.VisionConstants.FRONT_LEFT_APRIL_TAG_LL);
//         Pose2d estimation = new Pose2d(translation.pose.getTranslation(), Rotation2d.fromDegrees(currentYaw()));
//         return estimation;
//     }
    


//     public void periodic(){
//         currentPose = getData();
//         joshVisionMeasurment.setRobotPose(currentPose);
//         SmartDashboard.putData(joshVisionMeasurment);
//     }
// }





