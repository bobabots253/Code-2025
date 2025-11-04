package frc.robot.subsystems.endeffector;

public class EndeffectorConstants {
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
