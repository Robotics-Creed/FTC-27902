// PedroPathing for robot starting on RED SQUARE

package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.TelemetryManager;
import com.bylazar.telemetry.PanelsTelemetry;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.follower.Follower;
import com.pedropathing.paths.PathChain;
import com.pedropathing.geometry.Pose;

@Autonomous(name = "Pedro Pathing Autonomous", group = "Autonomous")
@Configurable // Panels
public class PedroAutonomous extends OpMode {
  private TelemetryManager panelsTelemetry; // Panels Telemetry instance
  public Follower follower; // Pedro Pathing follower instance
  private int pathState; // Current autonomous path state (state machine)
  private Paths paths; // Paths defined in the Paths class

  @Override
  public void init() {
    panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();

    follower = Constants.createFollower(hardwareMap);
    follower.setStartingPose(new Pose(72, 8, Math.toRadians(90)));

    paths = new Paths(follower); // Build paths

    panelsTelemetry.debug("Status", "Initialized");
    panelsTelemetry.update(telemetry);
  }

  @Override
  public void loop() {
    follower.update(); // Update Pedro Pathing
    pathState = autonomousPathUpdate(); // Update autonomous state machine

    // Log values to Panels and Driver Station
    panelsTelemetry.debug("Path State", pathState);
    panelsTelemetry.debug("X", follower.getPose().getX());
    panelsTelemetry.debug("Y", follower.getPose().getY());
    panelsTelemetry.debug("Heading", follower.getPose().getHeading());
    panelsTelemetry.update(telemetry);
  }

  public static class Paths {
    public PathChain Path1;
    public PathChain Path2;
    public PathChain Path3;
    public PathChain Path4;
    public PathChain Path5;
    public PathChain Path6;
    public PathChain Path7;
    public PathChain Path8;
    public PathChain Path9;
    public PathChain Path10;
    public PathChain Path11;
    public PathChain Path12;
    public PathChain Path13;

    public Paths(Follower follower) {
      Path1 = follower.pathBuilder()
          .addPath(
            new BezierLine(
              new Pose(56.000, 8.000),
            new Pose(56.000, 36.000)
            )
          )
          .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(180))
          .build();

      Path2 = follower.pathBuilder()
          .addPath(
            new BezierLine(
              new Pose(56.000, 36.000),
            new Pose(12.664, 35.477)
            )
          )
          .setTangentHeadingInterpolation()
          .build();

      Path3 = follower.pathBuilder()
          .addPath(
            new BezierLine(
              new Pose(12.664, 35.477),
            new Pose(71.169, 71.873)
            )
          )
          .setTangentHeadingInterpolation()
          .build();

      Path4 = follower.pathBuilder()
          .addPath(
            new BezierLine(
              new Pose(71.169, 71.873),
            new Pose(127.936, 130.439)
            )
          )
          .setTangentHeadingInterpolation()
          .build();

      Path5 = follower.pathBuilder()
          .addPath(
            new BezierLine(
              new Pose(127.936, 130.439),
            new Pose(71.471, 61.463)
            )
          )
          .setTangentHeadingInterpolation()
          .build();

      Path6 = follower.pathBuilder()
          .addPath(
            new BezierLine(
              new Pose(71.471, 61.463),
            new Pose(15.433, 59.272)
            )
          )
          .setTangentHeadingInterpolation()
          .build();

      Path7 = follower.pathBuilder()
          .addPath(
            new BezierLine(
              new Pose(15.433, 59.272),
            new Pose(71.463, 71.173)
            )
          )
          .setTangentHeadingInterpolation()
          .build();

      Path8 = follower.pathBuilder()
          .addPath(
            new BezierLine(
              new Pose(71.463, 71.173),
            new Pose(127.646, 129.622)
            )
          )
          .setTangentHeadingInterpolation()
          .build();

      Path9 = follower.pathBuilder()
          .addPath(
            new BezierLine(
              new Pose(127.646, 129.622),
            new Pose(37.344, 83.978)
            )
          )
          .setTangentHeadingInterpolation()
          .build();

      Path10 = follower.pathBuilder()
          .addPath(
            new BezierLine(
              new Pose(37.344, 83.978),
            new Pose(14.207, 83.213)
            )
          )
          .setTangentHeadingInterpolation()
          .build();

      Path11 = follower.pathBuilder()
          .addPath(
            new BezierLine(
              new Pose(14.207, 83.213),
            new Pose(71.775, 71.744)
            )
          )
          .setTangentHeadingInterpolation()
          .build();

      Path12 = follower.pathBuilder()
          .addPath(
            new BezierLine(
              new Pose(71.775, 71.744),
            new Pose(128.223, 129.185)
            )
          )
          .setTangentHeadingInterpolation()
          .build();

      Path13 = follower.pathBuilder()
          .addPath(
            new BezierLine(
              new Pose(128.223, 129.185),
            new Pose(39.135, 33.177)
            )
          )
          .setTangentHeadingInterpolation()
          .build();
    }
  }

  public int autonomousPathUpdate() {
    // Add your state machine Here
    // Access paths with paths.pathName
    // Refer to the Pedro Pathing Docs (Auto Example) for an example state machine
    return 0;
  }
}
