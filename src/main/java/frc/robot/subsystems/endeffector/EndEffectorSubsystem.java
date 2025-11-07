package frc.robot.subsystems.endeffector;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

    /*
     * This files houses the EndEffector Subsystem which the Superstructure will reference when handling state transitions.
     * The End-Effector in the 2025 kitBot consists of a singular, 1 motor mechanism which is used only for L1 scoring in the trough.
     * Because the coral intake is passive (gravity based), we just need to shoot out the coral game-piece when the robot is aligned with the reef face.
     * So how do figure out what needs to coded? 
     * 
     * A) Break down the actions that this subsystem needs to take.
     *  1. Rollers remain passive when the coral is intaked from the human player station
     *  2. Run the rollers when the operator controller requests scoring for L1
     * B) Represent the action as a SystemState
     *  1. IDLING
     *  2. SHOOTING
     * C) Handling State Transitions
     *  1. IDLE --> SHOOTING
     *  2. SHOOTING --> IDLE
     * D) Applying the requested SystemState
     *  1. applyStates() in the periodic block
     * 
     * So can we just begin? ---- Not Quite. This subsystem has no idea what kinda motors are in play.
     * As a result, we must begin by defining what motors we plan on using , configuring them, and then creating functions that run the motors.
     * 
     * Documentation Links:
     * <SparkMAX> https://codedocs.revrobotics.com/java/com/revrobotics/spark/sparkmax
     */

public class EndEffectorSubsystem extends SubsystemBase{
    /*
     * Define the motor controller being used. 
     */
    private final SparkMax m_intakeRollerSparkMax;

    /*
     * In order to handle state transitions, we must define enums to call for:
     * WantedStates - We call which one we want in the Superstructure to switch to the desire SystemStates
     * (current) SystemStates - The current state this subsystem is in. 
     * PreviousState - Used to check if we the requested WantedState is the current SystemState or a different SystemState
     */
    public enum WantedState {
        IDLE,
        SHOOTING,
    }

    private enum SystemState {
        SHOOTING,
        IDLING,
    }

    /*
     * Initializing the rollers in the IDLE position, we can ensure that they don't spin up unless we request them to.
     */

    private WantedState wantedState = WantedState.IDLE;
    private WantedState previousWantedState = WantedState.IDLE;
    private SystemState systemState = SystemState.IDLING;

    /*
     * For this exercise, all motor controllers will be the REV SparkMAX
     * Above we had previously defined the sparkMAX object for this subsystem but had not specificied characteristics of the motor controller.
     * Scrolling down to the [Constructor Detail] section of <SparkMax>, we can see exactly how to set this motor controller object up.
     *  Inputing: <CAN ID> and what type of motor the SparkMAX is connected to.
     *      For all intensive purposes, the <MotorType> for all our motors will always be [kBrushless]
     * Apply the config we made in [endeffector\EndeffectorConstants.java]
     *  Note: <PersistMode>.[kPersistParameters] ensures the config we apply will save over power-cycling and brownouts.
     */

public EndEffectorSubsystem(){
    m_intakeRollerSparkMax = new SparkMax(EndeffectorConstants.intakeRollerCANId,MotorType.kBrushless);
    m_intakeRollerSparkMax.configure(EndeffectorConfigs.endEffectorConfig, ResetMode.kResetSafeParameters,
    PersistMode.kPersistParameters);
    }

    /*
     * The periodic block runs when the robot is enabled.
     * Instead of writing a direct trigger to handle state transisitions, we can just tell the subsystem to periodically check for changes and apply it.
     * Note: This block runs on a 20 ms loop.
     */

    @Override
    public void periodic() {
        systemState = handleStateTransition();
        applyState();
    }

    /*
     * Method to handle state transitions:
     */

    private SystemState handleStateTransition() {
        if (wantedState != previousWantedState) {
            previousWantedState = wantedState;
        }
        return switch (wantedState) {
            case IDLE -> SystemState.IDLING;
            case SHOOTING -> SystemState.SHOOTING;
            default -> SystemState.IDLING;
        };
    }

    /*
     * Based on the selected wantedState:
     *  post-handleStateTransistion, apply the wantedState. 
     */
    private void applyState() {
        double requestedSpeedIO = 0.0;
        double outtakeSpeed = 1.0; //heuristic
        double idleSpeed = 0.0;
        switch (systemState) {
            case IDLING:
                requestedSpeedIO = idleSpeed;
                break;
            case SHOOTING:
                requestedSpeedIO = outtakeSpeed;
                break;
            default:
                requestedSpeedIO = 0.0;
        }

        //This is the function to run the endEffector at [requestedSpeedIO] 
        setEndEffectorIO(requestedSpeedIO);
    }

    /*
     * Method to set the speed of the roller motor
     *  1.0 = 100% Speed
     *  0.0 = 0% Speed
     *  -1.0 = -100% Speed
     */

    public void setEndEffectorIO(double speedIO) {
        m_intakeRollerSparkMax.set(speedIO);
    }


    /*
     * Method to choose the next wanted state via the Superstructure
     */
    public void setWantedState(WantedState wantedState) {
        this.wantedState = wantedState;
    }

}

//Challenge: Coral Slam Fix