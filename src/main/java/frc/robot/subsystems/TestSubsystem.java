
// package frc.robot.subsystems;

// import com.revrobotics.spark.SparkMax;
// import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
// import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
// import com.revrobotics.spark.config.SparkMaxConfig;
// import com.revrobotics.spark.SparkFlex;
// import com.revrobotics.spark.SparkLowLevel.MotorType;
// import com.revrobotics.RelativeEncoder;
// import com.revrobotics.spark.SparkBase.ControlType;
// import com.revrobotics.spark.SparkBase.PersistMode;
// import com.revrobotics.spark.SparkBase.ResetMode;
// import com.revrobotics.spark.SparkClosedLoopController;


// import java.text.DecimalFormat;

// import edu.wpi.first.wpilibj.motorcontrol.Spark;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import edu.wpi.first.wpilibj2.command.SubsystemBase;
// import frc.robot.States;
// import frc.robot.Configs;
// import frc.robot.Constants.TestSubsystemConstants;
// import frc.utils.Util;


// public class TestSubsystem extends SubsystemBase {
//     private static final SparkMax motorR = Util.createSparkMAX(TestSubsystemConstants.rightArmMotorID, MotorType.kBrushless, false); //ID,MotorType
//     private static final SparkMax motorL = Util.createSparkMAX(TestSubsystemConstants.leftArmMotorID, MotorType.kBrushless, false); //ID,MotorType
//     private final RelativeEncoder relativeEncoder;
//     /* READ ME:
//     * Creates a PID Controller which we use to control the motors movement
//     */
//     private SparkClosedLoopController pidController;
    
//     private static TestSubsystem instance;
//     public static TestSubsystem getInstance() {
//         if(instance == null) instance = new TestSubsystem();
//         return instance;

//     }
    
//     private TestSubsystem() {
//         resetEncoders();
//         relativeEncoder = motorR.getEncoder();
//         pidController = motorR.getClosedLoopController();
//         motorR.configure(Configs.TestSubsystem.TestConfig2, ResetMode.kResetSafeParameters,
//         PersistMode.kPersistParameters);
//         motorL.configure(Configs.TestSubsystem.TestConfig, ResetMode.kResetSafeParameters,
//         PersistMode.kPersistParameters);
//         register(); //Register Subsystem for Command Scheduluer to call in periodic
//     }
//     /**
//      * Runs motors at a value [-1 to 1]. Log current value on SmartDashboard
//      */
//     public void setOpenLoop(double value) {
//         motorR.set(value);
//         motorL.set(value);
//         SmartDashboard.putNumber("Open-loop Value", value);
//     }
    
//     /**
//      * Runs motors at a value 0 (stop).
//      */
//     public void stop() {
//         setOpenLoop(0);
//     }
    
//     /**
//      * Resets encoders to zero
//      */
//     public void resetEncoders() {
//         relativeEncoder.setPosition(0);
//     }
    
//     @Override
//     public void periodic() {
//         SmartDashboard.putNumber("Relative Encoder value [Rots]", relativeEncoder.getPosition());
//         SmartDashboard.putNumber("Right Motor Current [Amps]", motorR.getOutputCurrent());
//     }
    
//     /* READ ME:
//      * Select the Setpoint aka reference point for the PID Controller
//      */
//     public void setPosition(double position) {
//         pidController.setReference(position, ControlType.kPosition);
//         SmartDashboard.putNumber("Current SetPoint", position);
//     }

//     /* READ ME:
//      * Depending on what we input in (ie: button matching), we can select which setpoint we want to go to.
//      */
//     public void setState(States.TestPos state) {
//         SmartDashboard.putNumber("Position", state.val);
//         switch (state) {
//             case POS1:
//                 setPosition(TestSubsystemConstants.pos1);
//                 break;
//             default:
//                 setPosition(TestSubsystemConstants.pos2);
//                 break;
//         }
//     }

// }
