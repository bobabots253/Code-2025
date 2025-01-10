package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.math.controller.HolonomicDriveController;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.Constants.AutoConstants;
import frc.robot.subsystems.DriveSubsystem;

public class DriveToPose extends Command {
    private Supplier<Pose2d> target;
    private Pose2d tolerance;
    private final DriveSubsystem driveRequire = RobotContainer.getInstance().m_robotDrive;

    private final HolonomicDriveController holonomicDriveController;
    private final PIDController xController;
    private final PIDController yController;
    private final ProfiledPIDController rotController;

    //Note: Possibel Fix for Invalid Static Reference to DriveSubsys which has been causing the runtime crash
    // Vision Pose Estimation works but gets interefered by "estimated velocities"
    // Sometimes the position gets flipped which is unideal (find fix later)
    public DriveToPose(Supplier<Pose2d> target, Pose2d tolerance) {
        this.target = target;
        this.tolerance = tolerance;

        xController = new PIDController(.01, 0, 0);
        yController = new PIDController(.01, 0, 0);

        rotController = new ProfiledPIDController(1, 0, 0, new TrapezoidProfile.Constraints(4, 4));

        holonomicDriveController = new HolonomicDriveController(xController, yController, rotController);
        holonomicDriveController.setTolerance(this.tolerance);

        addRequirements(driveRequire);
    }

    @Override
    public void execute() {
        ChassisSpeeds chassisSpeeds = holonomicDriveController.calculate(driveRequire.getCurrentPose(), target.get(),
                AutoConstants.kMaxSpeedMetersPerSecond, target.get().getRotation());
        SwerveModuleState[] swerveModuleStates = Constants.DriveConstants.kDriveKinematics
                .toSwerveModuleStates(chassisSpeeds);
        driveRequire.setModuleStates(swerveModuleStates);
    }

    @Override
    public void end(boolean interrupted) {
        driveRequire.Xmode();
    }

    @Override
    public boolean isFinished() {
        System.out.println(driveRequire.getCurrentPose().getX());
        System.out.println(holonomicDriveController.atReference());
        return holonomicDriveController.atReference();
    }
}

