package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * SpindexerOpMode
 *
 * Controls a 3-slot spindexer (ball indexer) and a shooter/drivetrain.
 *
 * GAMEPAD CONTROLS (Gamepad 2 recommended for operator):
 *   Right Bumper (RB)  → Advance spindexer to next open slot
 *   Left Bumper  (LB)  → Fire / shoot the ball (runs shooter motor briefly)
 *
 * HARDWARE CONFIG NAMES (match these in your Robot Controller config):
 *   "spindexer"   → DcMotor controlling the rotating disc/spindexer
 *   "shooter"     → DcMotor controlling the flywheel / launcher
 *
 * HOW THE SPINDEXER WORKS:
 *   - The disc has 3 equally-spaced slots (120° apart).
 *   - Each press of RB rotates the disc by exactly ONE slot (120°).
 *   - A slot counter (0, 1, 2) tracks which slot is currently in the
 *     firing position, cycling back to 0 after slot 2.
 *   - After all 3 slots have been fired, the driver is notified via
 *     telemetry that the spindexer is empty.
 */
@TeleOp(name = "Spindexer TeleOp", group = "TeleOp")
public class SpindexerOpMode extends LinearOpMode {

    // ─── Hardware ────────────────────────────────────────────────────────────
    private DcMotor spindexerMotor;
    private DcMotor shooterMotor;

    // ─── Spindexer Constants ─────────────────────────────────────────────────

    /** Number of ball slots on the spindexer disc. */
    private static final int SLOT_COUNT = 3;

    /**
     * Encoder ticks per full revolution of the spindexer motor.
     * Common values:
     *   GoBILDA 5202 (312 RPM) = 537.7 ticks/rev
     *   REV HD Hex (40:1)      = 1120 ticks/rev
     *   Adjust to match YOUR motor + gearbox.
     */
    private static final double TICKS_PER_REV = 537.7;

    /** Ticks needed to rotate the disc by one slot (1/3 of a full revolution). */
    private static final int TICKS_PER_SLOT = (int) (TICKS_PER_REV / SLOT_COUNT);

    /** Motor power used while rotating to the next slot. */
    private static final double SPINDEXER_POWER = 0.5;

    // ─── Shooter Constants ────────────────────────────────────────────────────

    /** Power level for the shooter flywheel. */
    private static final double SHOOTER_POWER = 1.0;

    /**
     * How long (ms) to run the shooter motor when firing.
     * Tune this so the ball is actually launched.
     */
    private static final long SHOOT_DURATION_MS = 800;

    // ─── State ───────────────────────────────────────────────────────────────

    /** Index of the current slot that is loaded and ready to fire (0, 1, or 2). */
    private int currentSlot = 0;

    /** Tracks how many balls have been fired this match. */
    private int ballsFired = 0;

    /** Debounce timers so a single button press counts as one action. */
    private final ElapsedTime spindexerDebounce = new ElapsedTime();
    private final ElapsedTime shooterDebounce   = new ElapsedTime();

    /** Minimum ms between button re-triggers. */
    private static final double DEBOUNCE_MS = 300;

    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public void runOpMode() {

        // ── Hardware Mapping ────────────────────────────────────────────────
        spindexerMotor = hardwareMap.get(DcMotor.class, "spindexer");
        shooterMotor   = hardwareMap.get(DcMotor.class, "shooter");

        // Configure spindexer for encoder-based positioning
        spindexerMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        spindexerMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        spindexerMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Shooter runs open-loop (no encoder needed)
        shooterMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooterMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        // Flip direction if your motor is wired backwards
        // spindexerMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        // shooterMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.addLine("Spindexer ready. Waiting for start...");
        telemetry.update();

        waitForStart();

        // ── Main Loop ───────────────────────────────────────────────────────
        while (opModeIsActive()) {

            // ── Advance Spindexer (Right Bumper) ────────────────────────────
            if (gamepad2.right_bumper && spindexerDebounce.milliseconds() > DEBOUNCE_MS) {
                spindexerDebounce.reset();
                advanceSpindexer();
            }

            // ── Shoot Ball (Left Bumper) ─────────────────────────────────────
            if (gamepad2.left_bumper && shooterDebounce.milliseconds() > DEBOUNCE_MS) {
                shooterDebounce.reset();
                shootBall();
            }

            // ── Telemetry ────────────────────────────────────────────────────
            updateTelemetry();
        }

        // Safety: stop motors when OpMode ends
        spindexerMotor.setPower(0);
        shooterMotor.setPower(0);
    }

    // ─── Methods ──────────────────────────────────────────────────────────────

    /**
     * Rotates the spindexer forward by one slot (120°).
     * Uses RUN_TO_POSITION so the motor stops precisely at the next slot.
     */
    private void advanceSpindexer() {
        if (ballsFired >= SLOT_COUNT) {
            // Spindexer is empty — do not rotate further
            telemetry.addLine("⚠ Spindexer empty! Reload before spinning.");
            telemetry.update();
            return;
        }

        // Calculate the absolute target encoder position for the next slot
        int targetPosition = spindexerMotor.getCurrentPosition() + TICKS_PER_SLOT;

        spindexerMotor.setTargetPosition(targetPosition);
        spindexerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        spindexerMotor.setPower(SPINDEXER_POWER);

        // Wait until the motor reaches the target (non-blocking alternative below)
        while (opModeIsActive() && spindexerMotor.isBusy()) {
            telemetry.addData("Spindexer", "Moving to slot %d...", currentSlot + 1);
            telemetry.update();
        }

        // Stop and switch back to encoder mode for the next command
        spindexerMotor.setPower(0);
        spindexerMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Advance slot index (wraps 2 → 0)
        currentSlot = (currentSlot + 1) % SLOT_COUNT;
    }

    /**
     * Runs the shooter motor for SHOOT_DURATION_MS milliseconds to launch a ball.
     * Counts the ball as fired and alerts the driver if all slots are now empty.
     */
    private void shootBall() {
        if (ballsFired >= SLOT_COUNT) {
            telemetry.addLine("⚠ No balls loaded!");
            telemetry.update();
            return;
        }

        shooterMotor.setPower(SHOOTER_POWER);
        sleep(SHOOT_DURATION_MS);
        shooterMotor.setPower(0);

        ballsFired++;

        if (ballsFired >= SLOT_COUNT) {
            telemetry.addLine("✓ All balls fired! Reload spindexer.");
            telemetry.update();
        }
    }

    /** Pushes status information to the Driver Station screen. */
    private void updateTelemetry() {
        telemetry.addLine("=== Spindexer Status ===");
        telemetry.addData("Current Slot",    "%d / %d", currentSlot + 1, SLOT_COUNT);
        telemetry.addData("Balls Fired",     ballsFired);
        telemetry.addData("Balls Remaining", SLOT_COUNT - ballsFired);
        telemetry.addLine();
        telemetry.addLine("=== Controls (Gamepad 2) ===");
        telemetry.addLine("Right Bumper → Advance to next slot");
        telemetry.addLine("Left Bumper  → Fire / shoot ball");
        telemetry.addLine();
        telemetry.addData("Spindexer Encoder", spindexerMotor.getCurrentPosition());
        telemetry.addData("Shooter Power",     shooterMotor.getPower());
        telemetry.update();
    }
}
