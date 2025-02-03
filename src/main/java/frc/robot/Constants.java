// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.spark.*;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
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
  public static final class DriveConstants {
    // Driving Parameters - Note that these are not the maximum capable speeds of
    // the robot, rather the allowed maximum speeds
    public static final double kMaxSpeedMetersPerSecond = 4.8;
    public static final double kMaxAngularSpeed = 2 * Math.PI; // radians per second

    public static final double kDirectionSlewRate = 1.2; // radians per second
    public static final double kMagnitudeSlewRate = 1.8; // percent per second (1 = 100%)
    public static final double kRotationalSlewRate = 2.0; // percent per second (1 = 100%)

    // Chassis configuration
    public static final double kTrackWidth = Units.inchesToMeters(26.5);
    // Distance between centers of right and left wheels on robot
    public static final double kWheelBase = Units.inchesToMeters(26.5);
    // Distance between front and back wheels on robot
    public static final SwerveDriveKinematics kDriveKinematics = new SwerveDriveKinematics(
        new Translation2d(kWheelBase / 2, kTrackWidth / 2),
        new Translation2d(kWheelBase / 2, -kTrackWidth / 2),
        new Translation2d(-kWheelBase / 2, kTrackWidth / 2),
        new Translation2d(-kWheelBase / 2, -kTrackWidth / 2));

    // Angular offsets of the modules relative to the chassis in radians
    public static final double kFrontLeftChassisAngularOffset = -Math.PI / 2;
    public static final double kFrontRightChassisAngularOffset = 0;
    public static final double kBackLeftChassisAngularOffset = Math.PI;
    public static final double kBackRightChassisAngularOffset = Math.PI / 2;

    // SPARK MAX CAN IDs
    //Update all SPKMAX's for 2025 RevLib. - Check if cancoder is flipped.
    //Default SparkMAX ID's for 2024:
    //Driving CW frontR: 1,3,5,7
    //Turning CW frontR: 2,4,6,8
    public static final int kFrontLeftDrivingCanId = 1; 
    public static final int kRearLeftDrivingCanId = 7;
    public static final int kFrontRightDrivingCanId = 3;
    public static final int kRearRightDrivingCanId = 5;

    public static final int kFrontLeftTurningCanId = 2;
    public static final int kRearLeftTurningCanId = 8;
    public static final int kFrontRightTurningCanId = 4;
    public static final int kRearRightTurningCanId = 6;

    //Use to globally fix Gyro Flip
    public static final boolean kGyroReversed = false;
  }

  public static final class ModuleConstants {
    // The MAXSwerve module can be configured with one of three pinion gears: 12T, 13T, or 14T.
    // This changes the drive speed of the module (a pinion gear with more teeth will result in a
    // robot that drives faster).
    public static final int kDrivingMotorPinionTeeth = 14;

    // Invert the turning encoder, since the output shaft rotates in the opposite direction of
    // the steering motor in the MAXSwerve Module.
    public static final boolean kTurningEncoderInverted = true;

    // Calculations required for driving motor conversion factors and feed forward
    public static final double kDrivingMotorFreeSpeedRps = NeoMotorConstants.kFreeSpeedRpm / 60;
    public static final double kWheelDiameterMeters = 0.072475761; //Wheel Odometry relies on accurate measurements
    public static final double kWheelCircumferenceMeters = kWheelDiameterMeters * Math.PI;
    // 45 teeth on the wheel's bevel gear, 22 teeth on the first-stage spur gear, 15 teeth on the bevel pinion
    public static final double kDrivingMotorReduction = (45.0 * 22) / (kDrivingMotorPinionTeeth * 15);
    public static final double kDriveWheelFreeSpeedRps = (kDrivingMotorFreeSpeedRps * kWheelCircumferenceMeters)
        / kDrivingMotorReduction;

    public static final double kDrivingEncoderPositionFactor = (kWheelDiameterMeters * Math.PI)
        / kDrivingMotorReduction; // meters
    public static final double kDrivingEncoderVelocityFactor = ((kWheelDiameterMeters * Math.PI)
        / kDrivingMotorReduction) / 60.0; // meters per second

    public static final double kTurningEncoderPositionFactor = (2 * Math.PI); // radians
    public static final double kTurningEncoderVelocityFactor = (2 * Math.PI) / 60.0; // radians per second

    public static final double kTurningEncoderPositionPIDMinInput = 0; // radians
    public static final double kTurningEncoderPositionPIDMaxInput = kTurningEncoderPositionFactor; // radians

    public static final double kDrivingP = 0.04; //0.4 is 1/2 speed
    public static final double kDrivingI = 0;
    public static final double kDrivingD = 0;
    public static final double kDrivingFF = 1 / kDriveWheelFreeSpeedRps;
    public static final double kDrivingMinOutput = -1;
    public static final double kDrivingMaxOutput = 1;
    public static final double kAutoDrivingP = 0.08;

    public static final double kTurningP = 1;
    public static final double kTurningI = 0;
    public static final double kTurningD = 0;
    public static final double kTurningFF = 0;
    public static final double kTurningMinOutput = -1;
    public static final double kTurningMaxOutput = 1;
    public static final double kAutoTurningP = 1;

    public static final IdleMode kDrivingMotorIdleMode = IdleMode.kBrake;
    public static final IdleMode kTurningMotorIdleMode = IdleMode.kBrake;

    public static final int kDrivingMotorCurrentLimit = 40; // amps
    public static final int kTurningMotorCurrentLimit = 20; // amps
  }

  public static final class OIConstants {
    public static final int kDriverControllerPort = 0;
    public static final int kOperatorControllerPort = 1;
    public static final double kDriveDeadband = 0.07; //0.05, increase to compensate for stick drift
  }

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

  public static final class ElevatorConstants {
    public static final int masterLiftingCANId = 9;
    public static final int slaveLiftingCANId = 10;

    public static final double idealHomingLinearPosition = 0.000; //revs
    public static final double softZeroLinearPosition = 0.000; //revs
    public static final double L1Score = 0.120; //find when finished building
    public static final double L1Misc = 0.120; //Note to Self: Fast but don't break Elevator 
    public static final double L2Score = 0.120;
    public static final double L2Algae = 0.120;
    public static final double L2Misc = 0.120;
    public static final double L3Score = 0.120;
    public static final double L3Algae = 0.120;
    public static final double L3Misc = 0.120;
    public static final double pos1 = 0.120; 
    public static final double pos2 = 4.85; 
    public static final double codeStop = 0.120;
    public static final double hardStop = 0.120;
    public static final double Test1 = 0.120;
    public static final double Test2 = 0.120;
    public static final double Test3 = 0.120;
    public static final double pidOutputLow = -1; //max output is capped @ 1
    public static final double pidOutputHigh = 1;

    public static final double kIncrementalPostionP = 0.001; //5:1
    public static final double kIncrementalPostionI = 0.0; //5:1
    public static final double kIncrementalPositionD = 0.0005; //5:1
    public static final double kIncrementalPositionFF = 0.0005; //5:1

    //Universal Elevator Current Limits
    public static final int kUniversalSoftLimit = 40; 
    public static final int kUniversalHardLimit = 45;

    //Universal Elevator Output Limits
    public static final double kUniversalPIDOutputLow = -1; //max output is capped @ 1
    public static final double kUniversalPIDOutputHigh = 1;
  }

  public static final class EndEffectorConstants {
    public static final int pivotCANId = 12;
    public static final int intakeRollerCANId = 11;
    public static final int algaeRollerCANId = 13;

    //INTAKE/ROLLERS
    public static final double idealHoldingIntakeVelocity = 0.000; //RPM
    public static final double idealRunningIntakeVelocity = 0.000; //RPM
    public static final double idealStallIntakeVelocity = 0.000; //RPM
    public static final double idealSlowIntakeVelocity = 0.000; //RPM
    public static final double idealAlignIntakeVelocity = 0.000; //RPM
    public static final double kIntakeVelocityP = 0.001; //1:1
    public static final double kIntakeVelocityI = 0.0; //1:1
    public static final double kIntakeVelocityD = 0.0005; //1:1
    public static final double kIntakeVelocityFF = 0.0005; //1:1

    //PIVOT
    public static final double softZeroPivotPosition = 0.000; //degress
    public static final double idealStowAngle = 0.000; //degrees
    public static final double alternateStowAngle = 0.000; //degress
    public static final double L1Score = 0.120; //find when finished building
    public static final double L1Misc = 0.120; //Note to Self: Fast but don't break Wrist
    public static final double L2Score = 0.120; //degrees
    public static final double L2Algae = 0.120;
    public static final double L2Misc = 0.120;
    public static final double L3Score = 0.120;
    public static final double L3Algae = 0.120;
    public static final double L3Misc = 0.120;
    public static final double pos1 = 0.120; //degrees
    public static final double pos2 = 4.85; 
    public static final double topCodeStop = 0.120; //degrees
    public static final double botCodeStop = 0.120; 
    public static final double topHardStop = 0.120; 
    public static final double botHardStop = 0.120; 
    public static final double Test1 = 0.120; //degrees
    public static final double Test2 = 0.120;
    public static final double Test3 = 0.120;
    public static final double kPivotAbsolutePositionP = 0.001; //1:1
    public static final double kPivotAbsolutePositionI = 0.0; //1:1
    public static final double kPivotAbsolutePositionD = 0.0005; //1:1
    public static final double kPivotAbsolutePositionFF = 0.0005; //1:1

    //ALGAE 
    public static final double idealHoldingAlgaeVelocity = 0.000; //RPM
    public static final double idealRunningAlgaeVelocity = 0.000; //RPM
    public static final double idealStallAlgaeVelocity = 0.000; //RPM
    public static final double idealSlowAlgaeVelocity = 0.000; //RPM
    public static final double idealGroundAlgaeVelocity = 0.000; //RPM
    public static final double kAlgaeVelocityP = 0.001; //1:1
    public static final double kAlgaeVelocityI = 0.0; //1:1
    public static final double kAlgaeVelocityD = 0.0005; //1:1
    public static final double kAlgaeVelocityFF = 0.0005; //1:1
    
    //Universal EF Current Limits
    public static final int kUniversalSoftLimit = 40; 
    public static final int kUniversalHardLimit = 45;
    //Universal EF Output Limits
    public static final double kUniversalPIDOutputLow = -1; //max output is capped @ 1
    public static final double kUniversalPIDOutputHigh = 1;
  }

  public static final class VisionConstants{
      public static final double MIN_MT_TAG_COUNT = 2;
      public static final double AVG_MT2_TAG_DIST = 3;
      public static final double TRUSTWORTHY_DISTANCE = 4; //Meters
      public static final double MAX_ANGULAR = 180; //Degrees
      public static final String FRONT_LEFT_APRIL_TAG_LL = "limelight-green";
      public static final String FRONT_RIGHT_APRIL_TAG_LL = "limelight-blue";
      public static final double DEFAULT_CROP_SIZE = 0.85;
      public static final double FOV_X = 82; //for 3G's
      public static final double FOV_Y = 56.2;
      public static final double FOV_AREA = FOV_X * FOV_Y;
      public static final int[] ALL_TAG_IDS = new int[]{ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22};
    public static final double RECENT_DATA_CUTOFF = 3.5;
  }
}

//Old Code Ref:
  // public static final class HookConstants {
  //   public static final int motorID = 11;
  //   public static final double stow = 0.629; // 0.605; // 0.4515; //0.429; //0.169; //0.509; //0.169
  //   public static final double open = 0.457; // 0.433; // 0.280;  // 0.003; //0.343; // 0.003
  //   public static final double score = 0.453; // 0.4295; // 0.276; //0.356037;// 0.014 //0.380; //0.436 rel encoder -0.25
  //   public static final double pidOutputLow = -0.5;
  //   public static final double pidOutputHigh = 0.5;
  //   public static final double delay = 1.0;//0.95
  //   public static double kP = 1.5; //1.7;
  //   public static double kI = 0.0;//0.015;
  //   public static double kD = 0.0;
  // }

  // public static final class TestSubsystemConstants {
  //   // add id's
  //   // relative to absolute: relative = 5*absolute + 0.3
  //   public static final int rightArmMotorID = 9;
  //   public static final int leftArmMotorID = 10;
  //   public static final double pos1 = 0.120; //0.; // 0.318 abs 500:1 - 0.120
  //   public static final double pos2 = 4.85; //7.904655; //8.268729; //1.65;//relative encoder DO NOT use 1.65 // 500:1 score - 8.15
  //   public static final double max = 5; //8.21! //500:1 max - 8.16
  //   public static final double climb_up = 4.143260; //1.25; //500:1 - cu - 7.238
  //   public static final double climb_down = -.15; //0.148; // 500:1 cd - -.15
  //   public static final double pidOutputLow = -1;
  //   public static final double pidOutputHigh = 2.5;
  //   public static double kP = 2.5; //500:1 - 1.2
  //   public static double kI = 0.0;
  //   public static double kD = 0.3; // 500:1 - 0.3
  //   public static double climbP = 1; // 500:1 - 1.0
  //   public static double climbI = 0.0;
  //   public static double climbD = 0.0;

  // }