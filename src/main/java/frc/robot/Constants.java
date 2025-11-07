// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.spark.*;
import com.revrobotics.spark.SparkClosedLoopController.ArbFFUnits;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.trajectory.TrapezoidProfile;

import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import com.pathplanner.lib.path.PathConstraints;
import edu.wpi.first.math.util.Units;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean
 * constants. This class should not be used for any other purpose. All constants
 * should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

  public static final class AutoConstants {
    public static final double kMaxSpeedMetersPerSecond = 3; //more likely to 3.5 m/s
    public static final double kMaxAccelerationMetersPerSecondSquared = 3;
    public static final double kMaxAngularSpeedRadiansPerSecond = Math.PI;
    public static final double kMaxAngularSpeedRadiansPerSecondSquared = Math.PI;
    public static final PathConstraints pathConstraints = new PathConstraints(
      kMaxSpeedMetersPerSecond,
      kMaxAccelerationMetersPerSecondSquared,
      kMaxAngularSpeedRadiansPerSecond,
      kMaxAngularSpeedRadiansPerSecondSquared
    );

    public static final double kPXController = 1;
    public static final double kPYController = 1;
    public static final double kPThetaController = 1;

    // Constraint for the motion profiled robot angle controller
    public static final TrapezoidProfile.Constraints kThetaControllerConstraints = new TrapezoidProfile.Constraints(
        kMaxAngularSpeedRadiansPerSecond, kMaxAngularSpeedRadiansPerSecondSquared);
  }

  public static final class NeoMotorConstants {
    public static final double kFreeSpeedRpm = 5676;
  }

  public static final class VisionConstants{
    public static final String FRONT_LEFT_APRIL_TAG_LL = "limelight-purple";
    public static final String FRONT_RIGHT_APRIL_TAG_LL = "limelight-orange";
    public static final double FRONT_LEFT_LL_OFFSET_BLUE = 23;
    public static final double FRONT_RIGHT_LL_OFFSET_BLUE = -23;
    public static final double FRONT_LEFT_LL_OFFSET_RED = 203;
    public static final double FRONT_RIGHT_LL_OFFSET_RED = -203;
    //   public static final double MIN_MT_TAG_COUNT = 2;
    //   public static final double AVG_MT2_TAG_DIST = 3;
    //   public static final double TRUSTWORTHY_DISTANCE = 4; //Meters
    //   public static final double MAX_ANGULAR = 180; //Degrees
    //   public static final double DEFAULT_CROP_SIZE = 0.85;
    //   public static final double FOV_X = 82; //for 3G's
    //   public static final double FOV_Y = 56.2;
    //   public static final double FOV_AREA = FOV_X * FOV_Y;
    //   public static final int[] ALL_TAG_IDS = new int[]{ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22};
    //   public static final int[] TRUSTWORTHY_TAGS = new int[]{6, 7, 8, 9, 10, 11, 17, 18, 19, 20, 21, 22};
    // public static final double RECENT_DATA_CUTOFF = 3.5;
  }

  public static final class PPLibConstants{
    public static PathConstraints defaultPathfindingConstraints = new PathConstraints(3.0,4.0,
        Units.degreesToRadians(540), Units.degreesToRadians(720));
    public static PathConstraints handoffReefAlignmentConstraints = new PathConstraints(1.5, 2,
        Units.degreesToRadians(540), Units.degreesToRadians(720), 12, false);
    public static PathConstraints finalAlignmentConstraints = new PathConstraints(1.5,1.75,
        Units.degreesToRadians(540), Units.degreesToRadians(720), 12, false);
  }

  public static final class PPHolonomicConstants{
    public static final PIDConstants kTranslationPID = new PIDConstants(5.0,0,0); //tune
    public static final PIDConstants kRotationPID = new PIDConstants(5.0,0,0); //tune
    public static final double kRotationTolerance = 5; //degrees (0.0872665 rads)
    public static final double kPositionTolerance = 0.0508; //meters (2")
    public static final double TRUSTWORTHY_DISTANCE = 4; //Meters
    public static final double MAX_ANGULAR = 180; //Degrees
  }

  public static final boolean tuningMode = false;

  public static final class ClimbConstants{
    public static final int masterClimbCanID = 14;
    public static final int slaveClimbCanID = 15;
    public static final int kUniversalHardLimit = 45;
    public static final int kUniversalSoftLimit = 40;
    public static final int kSuperHighOutputLimit = 60;
    }

  public static final class SuperstructureConstants {
      public enum AutomationLevel {
        AUTO_RELEASE,
        AUTO_DRIVE_AND_MANUAL_RELEASE,
        MANUAL
      }

      public enum ReefSelectionMethod {
        POSE,
        ROTATION
      }
  }
  
}