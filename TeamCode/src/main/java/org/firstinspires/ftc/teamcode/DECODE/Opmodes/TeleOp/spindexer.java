package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * SpindexerOpMode — Non-Blocking State Machine Version
 *
 * Controls a 3-slot spindexer disc (servo) and a ball scoop (servo).
 * Uses a state machine so the main loop NEVER blocks — driving, other
 * mechanisms, and telemetry all continue running during scoop motion.
 *
 * GAMEPAD CONTROLS (Gamepad 2 - operator):
 *   Right Bumper (RB)  → Advance spindexer to the next slot
 *   Left Bumper  (LB)  → Trigger scoop: extend → hold → retract automatically
 *
 * HARDWARE CONFIG NAMES (match your Robot Controller app exactly):
 *   "spindexer"  → Servo rotating the 3-slot disc
 *   "scoop"      → Servo that scoops the ball onto the ramp
 *
 * TUNING:
 *   SLOT_POSITIONS[]    — Servo positions (0.0–1.0) for each of the 3 slots.
 *                         Estimates for a 180° servo: 0.00, 0.33, 0.67.
 *                         Tune until each slot physically aligns with the ramp.
 *   SCOOP_RETRACTED_POS — Resting position of the scoop (out of the ball's path).
 *   SCOOP_EXTENDED_POS  — Active position (sweeps ball onto ramp).
 *   SCOOP_HOLD_MS       — How long (ms) the scoop stays extended before retracting.
 */

@TeleOp(name = "Spindexer TeleOp", group = "TeleOp")
public class SpindexerOpMode extends LinearOpMode {

    // ─── Hardware ─────────────────────────────────────────────────────────────
    private Servo spindexerServo;
    private Servo scoopServo;

    // ─── Spindexer Slot Positions ─────────────────────────────────────────────
    private static final double[] SLOT_POSITIONS = {
        0.00,   // Slot 1 — home / starting position
        0.33,   // Slot 2 — 120° from slot 1
        0.67    // Slot 3 — 240° from slot 1
    };

    // ─── Scoop Servo Positions ────────────────────────────────────────────────
    private static final double SCOOP_RETRACTED_POS = 0.0;
    private static final double SCOOP_EXTENDED_POS  = 1.0;

    /** How long (ms) the scoop holds its extended position before retracting. */
    private static final double SCOOP_HOLD_MS = 600;

    // ─── Scoop State Machine ──────────────────────────────────────────────────
    /**
     * All possible states of the scoop.
     *
     *   IDLE       — Scoop is retracted and waiting for a button press.
     *   EXTENDING  — Scoop servo is moving to the extended position.
     *                We wait a brief moment for the servo to physically reach it.
     *   HOLDING    — Scoop is fully extended; waiting SCOOP_HOLD_MS before retracting.
     *   RETRACTING — Scoop servo is returning to the retracted position.
     */
    private enum ScoopState {
        IDLE,
        EXTENDING,
        HOLDING,
        RETRACTING
    }

    private ScoopState scoopState = ScoopState.IDLE;

    /**
     * How long (ms) to wait after commanding EXTENDING before assuming
     * the servo has physically reached the extended position.
     * Increase if your servo is slow or if HOLDING starts too early.
     */
    private static final double SERVO_TRAVEL_MS = 300;

    // ─── State ────────────────────────────────────────────────────────────────
    /** Index of the slot currently in the active position (0, 1, or 2). */
    private int currentSlot = 0;

    /** General-purpose timer reused for each state transition. */
    private final ElapsedTime stateTimer = new ElapsedTime();

    /** Debounce timers — prevent a held button from firing repeatedly. */
    private final ElapsedTime spindexerDebounce = new ElapsedTime();
    private final ElapsedTime scoopDebounce     = new ElapsedTime();

    private static final double DEBOUNCE_MS = 350;

    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public void runOpMode() {

        // ── Hardware Mapping ─────────────────────────────────────────────────
        spindexerServo = hardwareMap.get(Servo.class, "spindexer");
        scoopServo     = hardwareMap.get(Servo.class, "scoop");

        // Park servos at starting positions before the match
        spindexerServo.setPosition(SLOT_POSITIONS[currentSlot]);
        scoopServo.setPosition(SCOOP_RETRACTED_POS);

        telemetry.addLine("Spindexer ready — waiting for start...");
        telemetry.update();

        waitForStart();
        stateTimer.reset();

        // ── Main TeleOp Loop ──────────────────────────────────────────────────
        while (opModeIsActive()) {

            handleSpindexerInput();   // Check RB and rotate disc if needed
            runScoopStateMachine();   // Advance scoop state based on elapsed time
            handleScoopInput();       // Check LB and kick off scoop if IDLE

            updateTelemetry();

            // ── Add your drivetrain / other mechanism code below ─────────────
            // e.g. drive motors, lift control, etc. — none of it is blocked.
        }
    }

    // ─── Input Handlers ───────────────────────────────────────────────────────

    /**
     * Right Bumper → advance the spindexer to the next slot.
     * Instant servo command — no state machine needed.
     */
    private void handleSpindexerInput() {
        if (gamepad2.right_bumper && spindexerDebounce.milliseconds() > DEBOUNCE_MS) {
            spindexerDebounce.reset();
            currentSlot = (currentSlot + 1) % SLOT_POSITIONS.length;
            spindexerServo.setPosition(SLOT_POSITIONS[currentSlot]);
        }
    }

    /**
     * Left Bumper → start a scoop cycle, but only if the scoop is currently IDLE.
     * Pressing during an active scoop cycle is safely ignored.
     */
    private void handleScoopInput() {
        if (gamepad2.left_bumper
                && scoopDebounce.milliseconds() > DEBOUNCE_MS
                && scoopState == ScoopState.IDLE) {
            scoopDebounce.reset();
            transitionTo(ScoopState.EXTENDING);
        }
    }

    // ─── Scoop State Machine ──────────────────────────────────────────────────

    /**
     * Called every loop iteration. Checks elapsed time and advances the
     * scoop through its states without ever calling sleep().
     *
     * State flow:
     *   IDLE → EXTENDING → HOLDING → RETRACTING → IDLE
     */
    private void runScoopStateMachine() {
        switch (scoopState) {

            case IDLE:
                // Nothing to do — waiting for a button press via handleScoopInput().
                break;

            case EXTENDING:
                // Wait for the servo to physically reach the extended position,
                // then move into the HOLDING state.
                if (stateTimer.milliseconds() >= SERVO_TRAVEL_MS) {
                    transitionTo(ScoopState.HOLDING);
                }
                break;

            case HOLDING:
                // Scoop is extended and holding. Once SCOOP_HOLD_MS has elapsed,
                // command the servo to retract.
                if (stateTimer.milliseconds() >= SCOOP_HOLD_MS) {
                    transitionTo(ScoopState.RETRACTING);
                }
                break;

            case RETRACTING:
                // Wait for the servo to physically return to the retracted position,
                // then return to IDLE and allow the next scoop cycle.
                if (stateTimer.milliseconds() >= SERVO_TRAVEL_MS) {
                    transitionTo(ScoopState.IDLE);
                }
                break;
        }
    }

    /**
     * Moves the scoop state machine to a new state, commands the servo
     * to the appropriate position, and resets the state timer.
     *
     * Centralising servo commands here means each state only needs one
     * place to look for what action it triggers.
     */
    private void transitionTo(ScoopState newState) {
        scoopState = newState;
        stateTimer.reset();

        switch (newState) {
            case EXTENDING:
                scoopServo.setPosition(SCOOP_EXTENDED_POS);
                break;
            case RETRACTING:
                scoopServo.setPosition(SCOOP_RETRACTED_POS);
                break;
            case HOLDING:
            case IDLE:
                // No servo command needed for these transitions
                break;
        }
    }

    // ─── Telemetry ────────────────────────────────────────────────────────────

    private void updateTelemetry() {
        telemetry.addLine("─── Spindexer ───────────────────");
        telemetry.addData("Active Slot",    "%d / %d", currentSlot + 1, SLOT_POSITIONS.length);
        telemetry.addData("Servo Position", "%.2f",    spindexerServo.getPosition());

        telemetry.addLine("─── Scoop ───────────────────────");
        telemetry.addData("State",          scoopState);
        telemetry.addData("State Timer",    "%.0f ms",  stateTimer.milliseconds());
        telemetry.addData("Servo Position", "%.2f",     scoopServo.getPosition());

        telemetry.addLine("─── Controls (Gamepad 2) ────────");
        telemetry.addLine("RB → Next slot");
        telemetry.addLine("LB → Scoop ball to ramp");
        telemetry.update();
    }
}
