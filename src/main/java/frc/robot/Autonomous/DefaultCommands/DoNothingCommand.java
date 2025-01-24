package frc.robot.Autonomous.DefaultCommands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.RobotContainer;

public final class DoNothingCommand{

public static Command driveTime (double xspeed, double ySpeed, double rot, double sec){
    return new RunCommand(
      () -> RobotContainer.getInstance().m_robotDrive.drive(
          xspeed,
          ySpeed,
          rot,
          true,
          true),
      RobotContainer.getInstance().m_robotDrive
      ).withTimeout(sec);
  }

public static Command NoAuto(){
    return driveTime(0, 0, 0, 0);
}
}