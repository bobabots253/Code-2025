package frc.robot.subsystems.elevator;

public class ElevatorConstants {
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
    public static final double softZeroLinearPosition = 0.100; //revs0.023809418082237244
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
    public static final double ELEVATOR_MIN_TRAVEL = -0.20000; //heuristic
    public static final double ELEVATOR_OUTPUT_LOW = -0.75;
    public static final double ELEVATOR_OUTPUT_HIGH = 0.75;
    public static final double arbFFVolatge = 0.05;
    public static final double kV = 473;
  }

