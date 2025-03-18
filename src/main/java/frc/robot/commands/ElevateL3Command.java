package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.HolonomicDriveController;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.States;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.EndEffectorSubsystem;
import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.ElevatorConstants;
import frc.robot.subsystems.DriveSubsystem;

public class ElevateL3Command extends Command {
    private double tolerance = 0.012;
    private final ElevatorSubsystem elevatorRequire = RobotContainer.getInstance().m_Elevator;
    private final EndEffectorSubsystem endEffectorRequire = RobotContainer.getInstance().m_Effector;
    //Note: Possibel Fix for Invalid Static Reference to DriveSubsys which has been causing the runtime crash
    // Vision Pose Estimation works but gets interefered by "estimated velocities"
    // Sometimes the position gets flipped which is unideal (find fix later)
    public ElevateL3Command() {
        addRequirements(
            elevatorRequire,
            endEffectorRequire
        );
    }

    @Override
    public void execute() {
        new RunCommand(() -> {
              elevatorRequire.setLazyElevatorState(States.ElevatorPos.L3SCORE);
              }, elevatorRequire)
          ;
    }

    @Override
    public void end(boolean interrupted) {
        // Auto Elevation Complete
        new RunCommand(() -> {
            elevatorRequire.setLazyElevatorState(States.ElevatorPos.HOLD);
            }, elevatorRequire)
        ;
    }

    @Override
    public boolean isFinished() {
        return MathUtil.isNear(elevatorRequire.m_LiftingEncoder.getPosition(), ElevatorConstants.L3Score, tolerance);
    }
}

