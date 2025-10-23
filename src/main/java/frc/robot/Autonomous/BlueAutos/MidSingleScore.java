package frc.robot.Autonomous.BlueAutos;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.RobotContainer;
import frc.robot.Autonomous.AutoModeBase;
import frc.robot.Autonomous.DefaultCommands.ReturnAutoCommand;

public class MidSingleScore extends AutoModeBase {
    public static Pose2d requestPose2d = new Pose2d((new Translation2d(5.686646, 4.0200)), new Rotation2d(180 * (Math.PI/180)));
        public MidSingleScore (RobotContainer rContainer){
            super(rContainer);
        }
    
        public static Command ReturnAutoCommand(){
            return Commands.sequence(
                spinMove(),
                PIDPathfindToPose(requestPose2d).withTimeout(3),
            Commands.parallel(
                    setElevatorL1Auto().withTimeout(1.6).andThen(setElevatorStowAuto()),
                Commands.sequence(
                    new WaitCommand(0.8), //tune
                    rollerDefaultScore().withTimeout(1) //tune
                        .andThen(rollerDefaultStop()) 
                )
            )

        );
    }
}
