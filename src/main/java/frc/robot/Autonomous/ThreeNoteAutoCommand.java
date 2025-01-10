package frc.robot.Autonomous;

import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.RobotContainer;
import frc.robot.States;

public class ThreeNoteAutoCommand {

    public static Command getPathPlannerCommand() {
    return new PathPlannerAuto("B_3Note");
  }
    public static Command getPathShorted(){
      return new PathPlannerAuto("Test");
    }


    public static Command score3NoteCommand(){
    return new SequentialCommandGroup(
      //ThreeNoteAutoCommand.getPathPlannerCommand()
      ThreeNoteAutoCommand.getPathPlannerCommand(),
      new WaitCommand(0)
    );
  }
  
}
