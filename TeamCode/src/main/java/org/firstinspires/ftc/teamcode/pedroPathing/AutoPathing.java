package org.firstinspires.ftc.teamcode;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.pedropathing.util.Timer;

@TeleOp

  public class SampleAutoPathing extends OpMode {
    private Follower follower;
    private Timer pathTimer, opModeTimer;

    public enum PathState {
      // START POSITION_END POSITION
      // DRIVE > MOVEMENT STATE
      // SHOOT > ATTEMPT TO SCORE THE ARTIFACT
      DRIVE_STARTPOS_SHOOT_POS,
      SHOOT_PRELOAD
    }

    PathState pathState;
    private final Pose startPose = new Pose(x: 0, y: 0, Math.toRadians(0));
    private final Pose shootPose = new Pose(x: 0, y: 0, Math.toRadians(0));

    private PathChain driveStartPosShootPos;

    public void buildPaths() {
      driveStartPosShootPos = follower.pathBuilder()
        .addPath(new BezierLine(startPose, shootPose))
        .setLinearHeadingInterpolation(startPose.getHeading(), shootPose.getHeading())
        .build();
    }

    public void statePathUpdate() {
      switch(pathState) {
        case DRIVE_STARTPOS_SHOOT_POS:
          follower.followPath(driveStartPosShootPos, holdEnd: true);
          pathState = pathState.SHOOT_PRELOAD;
          break;
        cse SHOOT_PRELOAD:
          if (!follower.isBusy()) {
          telemetry.addLine(lineCaption: "Done Path 1");
        }
        break;
        default:
          telemetry.addLine(lineCaption: "No State Commanded");
          break;
    
  public void init() {
  }

  public void loop() {
  }
