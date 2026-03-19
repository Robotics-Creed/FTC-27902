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
            new Pose(14.573, 36.009)
            )
          )
          .setTangentHeadingInterpolation()
          .build();

      Path3 = follower.pathBuilder()
          .addPath(
            new BezierLine(
              new Pose(14.573, 36.009),
            new Pose(71.312, 71.169)
            )
          )
          .setTangentHeadingInterpolation()
          .build();

      Path4 = follower.pathBuilder()
          .addPath(
            new BezierLine(
              new Pose(71.312, 71.169),
            new Pose(27.409, 125.438)
            )
          )
          .setTangentHeadingInterpolation()
          .build();

      Path5 = follower.pathBuilder()
          .addPath(
            new BezierLine(
              new Pose(27.409, 125.438),
            new Pose(68.831, 61.348)
            )
          )
          .setTangentHeadingInterpolation()
          .build();

      Path6 = follower.pathBuilder()
          .addPath(
            new BezierLine(
              new Pose(68.831, 61.348),
            new Pose(15.274, 58.773)
            )
          )
          .setTangentHeadingInterpolation()
          .build();

      Path7 = follower.pathBuilder()
          .addPath(
            new BezierLine(
              new Pose(15.274, 58.773),
            new Pose(71.333, 71.654)
            )
          )
          .setTangentHeadingInterpolation()
          .build();

      Path8 = follower.pathBuilder()
          .addPath(
            new BezierLine(
              new Pose(71.333, 71.654),
            new Pose(28.656, 125.422)
            )
          )
          .setTangentHeadingInterpolation()
          .build();

      Path9 = follower.pathBuilder()
          .addPath(
            new BezierLine(
              new Pose(28.656, 125.422),
            new Pose(47.861, 83.521)
            )
          )
          .setTangentHeadingInterpolation()
          .build();

      Path10 = follower.pathBuilder()
          .addPath(
            new BezierLine(
              new Pose(47.861, 83.521),
            new Pose(15.930, 83.757)
            )
          )
          .setTangentHeadingInterpolation()
          .build();

      Path11 = follower.pathBuilder()
          .addPath(
            new BezierLine(
              new Pose(15.930, 83.757),
            new Pose(27.787, 124.404)
            )
          )
          .setTangentHeadingInterpolation()
          .build();

      Path12 = follower.pathBuilder()
          .addPath(
            new BezierLine(
              new Pose(27.787, 124.404),
            new Pose(38.375, 33.330)
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
