// package frc.robot.limelights;

// import com.studica.frc.AHRS;
// import com.studica.frc.AHRS.NavXComType;

// import edu.wpi.first.math.VecBuilder;
// import edu.wpi.first.math.estimator.PoseEstimator;
// import edu.wpi.first.math.geometry.Pose2d;
// import edu.wpi.first.math.geometry.Rotation2d;
// import edu.wpi.first.math.geometry.Translation2d;
// import edu.wpi.first.wpilibj.DriverStation;
// import edu.wpi.first.wpilibj.smartdashboard.Field2d;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import edu.wpi.first.wpilibj2.command.SubsystemBase;
// import frc.robot.Constants;
// import frc.robot.LimelightHelpers;
// import frc.robot.LimelightHelpers.PoseEstimate;



// public class JoshVision extends SubsystemBase{
//     // Pose2d position = getData();
//     private static LimelightHelpers.PoseEstimate pose;
//     private static LimelightHelpers.PoseEstimate megaPose;
//     private static Pose2d currentLocation;
//     private final AHRS navx;


//     private static JoshVision instance;
//     public static JoshVision getInstance(){
//         if(instance==null) instance = new JoshVision();
//         return instance;
//     }

//     private JoshVision(){
//         pose = LimelightHelpers.getBotPoseEstimate_wpiBlue(Constants.VisionConstants.FRONT_LEFT_APRIL_TAG_LL);
//         megaPose = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(Constants.VisionConstants.FRONT_LEFT_APRIL_TAG_LL);
//         navx = new AHRS(NavXComType.kMXP_SPI);
//     }
//     private static PoseEstimator location;
    
//     private Field2d field;
    
    

//     public double currentYaw(){
//         double yaw = -navx.getRotation2d().getDegrees();
//         if(DriverStation.getAlliance().orElse(DriverStation.Alliance.Red) == DriverStation.Alliance.Red){
//             yaw+=180;
//         }
//         return yaw;
//     }
//     //this is to test not megatag 2

//     //this is my old function
//     public PoseEstimate getData(){
        
//         PoseEstimate translation = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(Constants.VisionConstants.FRONT_LEFT_APRIL_TAG_LL);
//         //Pose2d estimation = new Pose2d(translation.pose.getTranslation(), Rotation2d.fromDegrees(currentYaw()));
//         return translation;
//     }
//     public Pose2d getPose2d(Translation2d translation, Rotation2d rotation){
//         return new Pose2d(translation, rotation);

//     }

//     //@SuppressWarnings("unchecked")
//     @Override
//     public void periodic(){
//         LimelightHelpers.SetRobotOrientation(Constants.VisionConstants.FRONT_LEFT_APRIL_TAG_LL, currentYaw()-23, 0, 22, 0, 0, 0);
//         location.setVisionMeasurementStdDevs(VecBuilder.fill(.7,.7,9999999));
//         currentLocation = getPose2d(getData().pose.getTranslation(), Rotation2d.fromDegrees(currentYaw()));
//         location.addVisionMeasurement(currentLocation, getData().timestampSeconds);
//         field.setRobotPose(location.getEstimatedPosition());
//         SmartDashboard.putData("robot", field);
//     }


// }





























// // public class JoshVision{
// //     Pose2d currentPose = getData();
// //     Field2d joshVisionMeasurment = new Field2d();
// //     AHRS navx = new AHRS(NavXComType.kMXP_SPI);


    



// // }





