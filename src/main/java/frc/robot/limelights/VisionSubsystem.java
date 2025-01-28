package frc.robot.limelights;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.VisionConstants;
import frc.robot.LimelightHelpers;
import frc.robot.RobotContainer;
import frc.robot.Constants;
import frc.robot.subsystems.DriveSubsystem;

public class VisionSubsystem extends SubsystemBase {

    private static VisionSubsystem instance;

    public static synchronized VisionSubsystem getInstance() {
        if (instance == null) {
            instance = new VisionSubsystem();
        }
        return instance;
    }

    private volatile VisionData[] limelightDatas = new VisionData[2];
    /** Last heartbeat of the front LL (updated every frame) */
    private volatile long lastHeartbeatFrontLL = 0;
    /** Last heartbeat of the back LL (updated every frame) */
    private volatile long lastHeartbeatBackLL = 0;
    DriveSubsystem driveRequire = RobotContainer.getInstance().m_robotDrive;

    //Fix these later
    private final List<Integer> BLUE_REEF = Arrays.asList(12, 13, 14, 15, 16, 17, 18, 29, 20, 21, 22);
    private final List<Integer> RED_REEF = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);
    private final List<Integer> BLUE_TOP_SOURCE = Arrays.asList(13, 17, 18, 19, 20 , 21);
    private final List<Integer> BLUE_BOT_SOURCE = Arrays.asList(12, 13, 16, 17, 18, 21, 22);
    private final List<Integer> RED_TOP_SOURCE = Arrays.asList(1, 2, 3, 6, 7, 8, 9, 10);
    private final List<Integer> RED_BOT_SOURCE = Arrays.asList(1, 2, 6, 7, 8, 11);
    private final List<Integer> BLUE_PROCCESSOR = Arrays.asList(12, 15, 16, 17, 18, 22);
    private final List<Integer> RED_PROCCESSR = Arrays.asList(2, 3, 4, 7, 8, 9, 10);

    public VisionSubsystem() {
    }

    private VisionData[] getFilteredLimelightData(boolean useStored) {
        LimelightHelpers.PoseEstimate frontLLDataMT2 = null;
        LimelightHelpers.PoseEstimate backLLDataMT2 = null;
        long delayLeftLL = -1; //default
        long delayRightLL = 1; //default
    
        if (!useStored) {
            double rotationDegrees = driveRequire.getRotation2DHeading().getDegrees();
            LimelightHelpers.SetRobotOrientation(Constants.VisionConstants.FRONT_LEFT_APRIL_TAG_LL,
                rotationDegrees, 0, 0, 0, 0, 0
            );
            LimelightHelpers.SetRobotOrientation(Constants.VisionConstants.FRONT_RIGHT_APRIL_TAG_LL,
                rotationDegrees, 0, 0, 0, 0, 0
            );
        
            delayLeftLL = LimelightHelpers.getLimelightNTTableEntry(Constants.VisionConstants.FRONT_LEFT_APRIL_TAG_LL, "hb").getInteger(-1);
            delayRightLL = LimelightHelpers.getLimelightNTTableEntry(Constants.VisionConstants.FRONT_RIGHT_APRIL_TAG_LL, "hb").getInteger(-1);

            if (delayLeftLL == -1 || this.lastHeartbeatFrontLL < delayLeftLL) {
                frontLLDataMT2 = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(Constants.VisionConstants.FRONT_LEFT_APRIL_TAG_LL);
                LimelightHelpers.PoseEstimate frontLLDataMT = LimelightHelpers.getBotPoseEstimate_wpiBlue(Constants.VisionConstants.FRONT_LEFT_APRIL_TAG_LL);
                this.limelightDatas[0] = new VisionData(Constants.VisionConstants.FRONT_LEFT_APRIL_TAG_LL, frontLLDataMT, frontLLDataMT2);
                this.lastHeartbeatFrontLL = delayLeftLL == -1 ? this.lastHeartbeatFrontLL : delayLeftLL;
            }
            
            if (delayRightLL == -1 || this.lastHeartbeatBackLL < delayRightLL) {
                backLLDataMT2 = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(Constants.VisionConstants.FRONT_RIGHT_APRIL_TAG_LL);
                LimelightHelpers.PoseEstimate backLLDataMT = LimelightHelpers.getBotPoseEstimate_wpiBlue(Constants.VisionConstants.FRONT_RIGHT_APRIL_TAG_LL);
                this.limelightDatas[1] = new VisionData(Constants.VisionConstants.FRONT_RIGHT_APRIL_TAG_LL, backLLDataMT, backLLDataMT2);
                this.lastHeartbeatBackLL = delayRightLL == -1 ? this.lastHeartbeatBackLL : delayRightLL;
            }

            ChassisSpeeds robotChassisSpeeds = driveRequire.getRobotRelativeSpeeds();
            double velocity = Math.sqrt(Math.pow(robotChassisSpeeds.vxMetersPerSecond, 2) + Math.pow(robotChassisSpeeds.vyMetersPerSecond, 2));
            //Makes sure: Distance <= 3 meters ; Angular <= 180 deg/s ; Translational <= 2 m/s (See PPLib Gui)
            // Else for both LLs the data is outdated or has no data, thus ignore vision updates.
            if (Math.abs(Units.radiansToDegrees(robotChassisSpeeds.omegaRadiansPerSecond)) > 270
                || Math.abs(velocity) > 2 // m/s
                || (this.lastHeartbeatBackLL != delayRightLL && this.lastHeartbeatFrontLL != delayLeftLL)
                || ((frontLLDataMT2 == null || frontLLDataMT2.tagCount == 0) && (backLLDataMT2 == null || backLLDataMT2.tagCount == 0))
                || ((frontLLDataMT2 == null || frontLLDataMT2.avgTagDist > Constants.VisionConstants.TRUSTWORTHY_DISTANCE)
                && (backLLDataMT2 == null || backLLDataMT2.avgTagDist > Constants.VisionConstants.TRUSTWORTHY_DISTANCE))
            ) {
                return new VisionData[0];
            }
        }

        // Allows LLs to compare data even if they have unsynced FPS / heartbeats.
        // Upon startup, these may still be null, so it is important to check for them or robot code could crash.
        double timestampNow = Timer.getFPGATimestamp();
        if (frontLLDataMT2 == null && this.limelightDatas[0] != null) {
            frontLLDataMT2 = this.limelightDatas[0].MegaTag2;
            
            if (frontLLDataMT2 != null && Math.abs(frontLLDataMT2.timestampSeconds - timestampNow) > 1) {
                frontLLDataMT2 = null;
            }
        }
        if (backLLDataMT2 == null && this.limelightDatas[1] != null) {
            backLLDataMT2 = this.limelightDatas[1].MegaTag2;
            
            if (backLLDataMT2 != null && Math.abs(backLLDataMT2.timestampSeconds - timestampNow) > 1) {
                backLLDataMT2 = null;
            }
        }
        if (frontLLDataMT2 == null && backLLDataMT2 == null) {
            return new VisionData[0];
        }

        // Returns the data with the greater tag count.
        // Will only return the data if it has the same heartbeat (synced)
        if ((frontLLDataMT2 != null && (useStored || this.lastHeartbeatFrontLL == delayLeftLL))
            && (backLLDataMT2 == null
                || backLLDataMT2.avgTagDist > Constants.VisionConstants.TRUSTWORTHY_DISTANCE
                || frontLLDataMT2.tagCount > backLLDataMT2.tagCount)) {
                return new VisionData[]{ this.limelightDatas[0] };
        }
        else if ((backLLDataMT2 != null && (useStored || this.lastHeartbeatBackLL == delayRightLL))
            && (frontLLDataMT2 == null
                || frontLLDataMT2.avgTagDist > Constants.VisionConstants.TRUSTWORTHY_DISTANCE
                || backLLDataMT2.tagCount > frontLLDataMT2.tagCount)) {
                return new VisionData[]{ this.limelightDatas[1] };
        }

        // Returns the data that's closer to its respective camera than 90% of the other's distance. (heuristic.
        if ((!useStored && this.lastHeartbeatFrontLL == delayLeftLL)
            && frontLLDataMT2.avgTagDist < backLLDataMT2.avgTagDist * 0.9) {
            return new VisionData[]{ this.limelightDatas[0] };
        }
        else if ((!useStored && this.lastHeartbeatBackLL == delayRightLL)
            && backLLDataMT2.avgTagDist < frontLLDataMT2.avgTagDist * 0.9) {
            return new VisionData[]{ this.limelightDatas[1] };
        }

        // This return statement assumes that both LLs have the same amount of tags 
        return this.limelightDatas;
    }

    //Not 100% neccessary but would be nice (doesn't impact delay)
    private void optimizeLimelights() {
        int index = 0; 
        for (VisionData limelightData : this.limelightDatas) {
            if (limelightData == null || limelightData.optimized) {
                return;
            }
            else {
                this.limelightDatas[index++].optimized = true;
            }
            
            // Avoid unnecessary optimization for a LL with no tags and
            // reset any optimization that might have been done previously.
            if (limelightData.MegaTag2 == null || limelightData.MegaTag2.tagCount == 0) {
                LimelightHelpers.SetFiducialDownscalingOverride(limelightData.name, 1.5f);
                LimelightHelpers.SetFiducialIDFiltersOverride(limelightData.name, Constants.VisionConstants.ALL_TAG_IDS);
                LimelightHelpers.setCropWindow(
                    limelightData.name,
                    -Constants.VisionConstants.DEFAULT_CROP_SIZE, //tune these
                    Constants.VisionConstants.DEFAULT_CROP_SIZE,
                    -Constants.VisionConstants.DEFAULT_CROP_SIZE,
                    Constants.VisionConstants.DEFAULT_CROP_SIZE
                );
                continue;
            }

            // Downscaling closer to tags.
            if (limelightData.MegaTag2.avgTagDist < 1.5) {
                LimelightHelpers.SetFiducialDownscalingOverride(limelightData.name, 3);
            }
            else if (limelightData.MegaTag2.avgTagDist < 2) {
                LimelightHelpers.SetFiducialDownscalingOverride(limelightData.name, 2);
            }
            else {
                LimelightHelpers.SetFiducialDownscalingOverride(limelightData.name, 1.5f);
            }

            // Tag filtering for nearby tags.
            Set<Integer> nearbyTagsSet = new HashSet<Integer>();
            for (LimelightHelpers.RawFiducial fiducial : limelightData.MegaTag2.rawFiducials) {
                switch (fiducial.id) {
                    case 1:
                        nearbyTagsSet.addAll(this.RED_BOT_SOURCE);
                    case 2:
                        nearbyTagsSet.addAll(this.RED_TOP_SOURCE);
                    case 3:
                        nearbyTagsSet.addAll(this.RED_PROCCESSR);
                    case 4:
                    case 5:
                    case 6:
                        nearbyTagsSet.addAll(this.RED_REEF);
                    case 7:
                        nearbyTagsSet.addAll(this.RED_REEF);
                    case 8:
                        nearbyTagsSet.addAll(this.RED_REEF);
                    case 9:
                        nearbyTagsSet.addAll(this.RED_REEF);
                    case 10:
                        nearbyTagsSet.addAll(this.RED_REEF);
                    case 11:
                        nearbyTagsSet.addAll(this.RED_REEF);
                    case 12:
                        nearbyTagsSet.addAll(this.BLUE_BOT_SOURCE);
                    case 13:
                        nearbyTagsSet.addAll(this.BLUE_TOP_SOURCE);
                    case 14:
                    case 15:
                    case 16:
                        nearbyTagsSet.addAll(this.BLUE_PROCCESSOR);
                    case 17:
                        nearbyTagsSet.addAll(this.BLUE_REEF);
                    case 18:
                        nearbyTagsSet.addAll(this.BLUE_REEF);
                    case 19:
                        nearbyTagsSet.addAll(this.BLUE_REEF);
                    case 20:
                        nearbyTagsSet.addAll(this.BLUE_REEF);
                    case 21:
                        nearbyTagsSet.addAll(this.BLUE_REEF);
                    case 22:
                        nearbyTagsSet.addAll(this.BLUE_REEF);  
                    default:
                        nearbyTagsSet.add(fiducial.id);
                }
            }
            int[] nearbyTagsArray = nearbyTagsSet.stream().mapToInt(i -> i).toArray();
            LimelightHelpers.SetFiducialIDFiltersOverride(limelightData.name, nearbyTagsArray);
            
            // Smart cropping around on-screen AprilTags and potential nearby ones.
            if (limelightData.MegaTag2.rawFiducials.length == 0) {
                LimelightHelpers.setCropWindow(
                    limelightData.name,
                    -VisionConstants.DEFAULT_CROP_SIZE,
                    VisionConstants.DEFAULT_CROP_SIZE,
                    -VisionConstants.DEFAULT_CROP_SIZE,
                    VisionConstants.DEFAULT_CROP_SIZE
                );
            }
            else {
                LimelightHelpers.RawFiducial txncBig = null;
                LimelightHelpers.RawFiducial txncSmall = null;
                LimelightHelpers.RawFiducial tyncBig = null;
                LimelightHelpers.RawFiducial tyncSmall = null;
                double sideLength = 0;
                
                // Finds the txnc and tync that are furthest from the crosshair
                // (for largest bounding box that will include all targets on screen).
                for (LimelightHelpers.RawFiducial fiducial: limelightData.MegaTag2.rawFiducials) {
                    // This formula is explained below.
                    sideLength = Math.sqrt(fiducial.ta * VisionConstants.FOV_AREA) / 2;
                    
                    if (txncBig == null || fiducial.txnc + sideLength > txncBig.txnc) {
                        txncBig = fiducial;
                    }
                    if (txncSmall == null || fiducial.txnc - sideLength < txncSmall.txnc) {
                        txncSmall = fiducial;
                    }
                    
                    if (tyncBig == null || fiducial.tync + sideLength > tyncBig.tync) {
                        tyncBig = fiducial;
                    }
                    if (tyncSmall == null || fiducial.tync - sideLength < tyncSmall.tync) {
                        tyncSmall = fiducial;
                    }
                }

                double xSmall = (txncSmall.txnc - Math.sqrt(txncSmall.ta * VisionConstants.FOV_AREA) * (2 * Math.log(txncSmall.distToCamera + 1)))
                    / (VisionConstants.FOV_X / 2);
                double xBig = (txncBig.txnc + Math.sqrt(txncBig.ta * VisionConstants.FOV_AREA) * (2 * Math.log(txncBig.distToCamera + 1)))
                    / (VisionConstants.FOV_X / 2);
                
                LimelightHelpers.setCropWindow(
                    limelightData.name,
                    // In the x directions, 2.5x the size of the box if there are expected tags there.
                    // This allows the LL to lose the second tag for a few frame without cropping solely to
                    // the remaining one and no longer seeing the other (since crop only resets when both tags are lost).
                    //                           leftmost coordinate - 1.5 * (horizontal size of box) = a box 2.5x its original size
                    getNearbyTagDirection(txncSmall.id) < 0 ? xSmall - 1.5 * Math.abs(xBig - xSmall) : xSmall,
                    getNearbyTagDirection(txncBig.id) > 0 ? xBig + 1.5 * Math.abs(xBig - xSmall) : xBig,
                    (tyncSmall.tync - Math.sqrt(tyncSmall.ta * VisionConstants.FOV_AREA) * (2 * Math.log(tyncBig.distToCamera + 1)))
                        / (VisionConstants.FOV_Y / 2),
                    (tyncBig.tync + Math.sqrt(tyncBig.ta * VisionConstants.FOV_AREA) * (2 * Math.log(tyncSmall.distToCamera + 1)))
                        / (VisionConstants.FOV_Y / 2)
                );
            }
        }
    }

    //Config Later (If a tag is suspected on the left (-))
    private int getNearbyTagDirection(int id) {
        switch (id) {
            case 1:
            case 3:
            case 7:
            case 9:
            case 2:
            case 4:
            case 8:
            case 10:
            default:
                return 0;
        }
    }

    public void periodic() {
    }

     public Pose2d getEstimatedPose() {
        VisionData[] filteredLimelightDatas = getFilteredLimelightData(true);

        if (filteredLimelightDatas.length == 0) {
            System.err.println("getEstimatedPose() | NO LIMELIGHT DATA");
            return new Pose2d();
        }
        else if (filteredLimelightDatas.length == 1) {
            if (filteredLimelightDatas[0].MegaTag2.tagCount == 0) {
                return new Pose2d();
            }

            return new Pose2d(
                filteredLimelightDatas[0].MegaTag2.pose.getTranslation(),
                filteredLimelightDatas[0].canTrustRotation ?
                    filteredLimelightDatas[0].MegaTag.pose.getRotation() : driveRequire.getRotation2DHeading()
            );
        }
        else {
            if (filteredLimelightDatas[0].MegaTag2.tagCount == 0 || filteredLimelightDatas[1].MegaTag2.tagCount == 0) {
                return new Pose2d();
            }

            // Average them for best accuracy
            return new Pose2d(
                // (First translation + Second translation) / 2
                filteredLimelightDatas[0].MegaTag2.pose.getTranslation().plus(filteredLimelightDatas[1].MegaTag2.pose.getTranslation()).div(2),
                filteredLimelightDatas[0].canTrustRotation ?
                    // First rotation / 2 + Second rotation / 2
                    //
                    // This is done to avoid problems due to Rotation2d being [0, 360) 
                    // Ex : 180+180=0 followed by 0/2=0 when it should be 180+180=360 and 360/2=180.
                    filteredLimelightDatas[0].MegaTag.pose.getRotation().div(2)
                        .plus(filteredLimelightDatas[1].MegaTag.pose.getRotation().div(2)) :
                    driveRequire.getRotation2DHeading()
            );
        }
    }


}
