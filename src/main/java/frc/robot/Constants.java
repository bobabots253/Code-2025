// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.trajectory.TrapezoidProfile;

import com.pathplanner.lib.config.PIDConstants;
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
    public static double kTimedTurnSpeed = 4; //2.0 rad/s ~= 115 deg/s
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
    //CAN Id's
    public static final int masterLiftingCANId = 9;
    public static final int slaveLiftingCANId = 10;

    //Elevator DIO Port
    public static final int pivotMasterHallEffectDIO = 0;
    public static final int pivotSlaveHallEffectDIO = 1;

    //Elevator trapezoidal profile
    /** In meters per second. Used for Profiled PID controller*/
    public static final double elevatorMaxVelocity = 8.5;//.685
    public static final double elevaotrMaxAccerleration = 13.16;//2.615
    public static final double profiledP = 2.25;//original 2.48
    public static final double profiledI = 0;
    public static final double profiledD = 0.0;//original 0.07
    public static final double gearRatio = 5.0;
    //meters
    public static final double gearRadius = 0.0254;

    public static final double idealHomingLinearPosition = 0.000; //revs
    public static final double softZeroLinearPosition = 0.050; //CCC Compensation
    public static final double L1Score = 8.2; // 2/24/25
    public static final double L1Misc = 0.120; //Note to Self: Fast but don't break Elevator 

    public static final double L2Score = 12.55; // 2/24/25 //works
    public static final double L2Algae = 0.120;
    public static final double L2Misc = 0.120;
    public static final double L3Score = 19.585; // 2/24/25 18.9
    public static final double L3Algae = 0.120;
    public static final double L3Misc = 0.120;
    public static final double L1Handoff = 5.9047;
    public static final double L1Flick = 12.55;
    public static final double pos1 = 0.120; 
    public static final double pos2 = 4.85; 
    public static final double codeStop = 0.120;
    public static final double hardStop = 0.120;
    public static final double Test1 = 0.120;
    public static final double Test2 = 0.120;
    public static final double Test3 = 0.120;
    public static final double pidOutputLow = -1; //max output is capped @ 1
    public static final double pidOutputHigh = 1;

    public static final double kIncrementalPostionP = 0.25; //5:1 //safe: 0.005; //008 //0.25
    public static final double kIncrementalPostionI = 0.000; //5:1 //0.001 //0065
    public static final double kIncrementalPositionD = 0.0; //5:1 // 0.05 //09 //3.2
    public static final double kIncrementalPositionFF = 0.75; //5:1 /0.75 -locked in


    //Universal Elevator Current Limits
    public static final int kUniversalSoftLimit = 40; 
    public static final int kUniversalHardLimit = 45;

    //Universal Elevator Output Limits
    public static final double kUniversalPIDOutputLow = -1; //max output is capped @ 1
    public static final double kUniversalPIDOutputHigh = 1;
    public static final double ELEVATOR_MAX_TRAVEL = 19.39500; //heuristic
    public static final double ELEVATOR_MIN_TRAVEL = -0.10000; //heuristic
    public static final double ELEVATOR_OUTPUT_LOW = -0.75;
    public static final double ELEVATOR_OUTPUT_HIGH = 0.75;
    public static final double arbFFVolatge = 0.05;
    public static final double kV = 473;
  }

  public static final class EndEffectorConstants {
    //EF CAN Id's
    public static final int pivotCANId = 12;
    public static final int intakeRollerCANId = 11;

    //EF DIO Port
    public static final int frontBeamBreakSensor = 2;
    public static final int backBeamBreakSensor = 3;

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
    public static final double softZeroPivotPosition = 0.015; //degress
    public static final double extendedPIvotPosition = 0.65;
    public static final double idealStowAngle = 0.000; //degrees
    public static final double alternateStowAngle = 0.245; //degress
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
    public static final double kPivotAbsolutePositionP = 0.5; //25:1
    public static final double kPivotAbsolutePositionI = 0.0; 
    public static final double kPivotAbsolutePositionD = 0.0005; 
    public static final double kPivotAbsolutePositionFF = 0.0005; 

    //ALGAE 
    public static final double idealHoldingAlgaeVelocity = 0.000; //RPM
    public static final double idealRunningAlgaeVelocity = 0.000; //RPM
    public static final double idealStallAlgaeVelocity = 0.000; //RPM
    public static final double idealSlowAlgaeVelocity = 0.000; //RPM
    public static final double idealGroundAlgaeVelocity = 0.000; //RPM
    public static final double kAlgaeVelocityP = 0.001; //12:1
    public static final double kAlgaeVelocityI = 0.0; 
    public static final double kAlgaeVelocityD = 0.0005; 
    public static final double kAlgaeVelocityFF = 0.0005; 
    
    //Universal EF Current Limits
    public static final int kUniversalSoftLimit = 20; 
    public static final int kUniversalHardLimit = 25;
    //Universal EF Output Limits
    public static final double kUniversalPIDOutputLow = -1; //max output is capped @ 1
    public static final double kUniversalPIDOutputHigh = 1;
    public static final double PIVOT_MAX_TRAVEL = 0.710;
    public static final double PIVOT_MIN_TRAVEL = 0.000;
    public static final double PIVOT_OUTPUT_HIGH = 0.3;
    public static final double PIVOT_OUTPUT_LOW = -0.2;
  }

  public static final class VisionConstants{
    public static final String FRONT_LEFT_APRIL_TAG_LL = "limelight-purple";
    public static final String FRONT_RIGHT_APRIL_TAG_LL = "limelight-orange";
    public static final double FRONT_LEFT_LL_OFFSET_BLUE = 23;
    public static final double FRONT_RIGHT_LL_OFFSET_BLUE = -23;
    public static final double FRONT_LEFT_LL_OFFSET_RED = 203;
    public static final double FRONT_RIGHT_LL_OFFSET_RED = -203;
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
  
}