package frc.robot.Autonomous;

import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.wpilibj2.command.Command;

public final class GoAutoCommand {

public static Command driveAutoCommand(){
    return new PathPlannerAuto("B_DriveAwayStraight3mAuto");
    // INSERT AUTO NAME INTO THE CHOICE! 
  }
}