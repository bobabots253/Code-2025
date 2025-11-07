package frc.robot.subsystems.endeffector;

public class EndeffectorConstants {
    /*
     * Preface: The constants file is used to contain well, constants, values which do not change. Typically, we store:
     * CAN Id's : Unique numeric indentifiers used when defining our motor controllers in the subsystem folder.
     * PID Values : PID values used in the PID Closed Loop Controller, reference the configs files for this subsystem.
     * Universal Current Limits: amperage limits used for all motor controllers. For NEO 550 motors, we have set these at 20 amps respectively.
     * Universal PID Output Limits: prevents the PID loop from overrequesting the motor. Usually this is set to 1.0, -1.0, which is no limits.
     * 
     * Most of the use case of these constants are explained in the configs file of each subsystem.
     * Note: Notice the [final] modifier for each of these constant. This modifiers prevents any changes to the value once it has been set.
     * Remember: CAN Ids cannot be shared and duplicate IDs can cause code crashes or coupled actions, which is a severe danger.
     */

    //EF CAN Id's
    public static final int intakeRollerCANId = 11;

    //INTAKE ROLLERS
    public static final double kIntakeVelocityP = 0.001; //1:1
    public static final double kIntakeVelocityI = 0.0; //1:1
    public static final double kIntakeVelocityD = 0.0005; //1:1
    
    //Universal EF Current Limits
    public static final int kUniversalSoftLimit = 20; 
    public static final int kUniversalHardLimit = 25;

    //Universal EF Output Limits
    public static final double kUniversalPIDOutputLow = -1; //max output is capped @ 1
    public static final double kUniversalPIDOutputHigh = 1;
}
