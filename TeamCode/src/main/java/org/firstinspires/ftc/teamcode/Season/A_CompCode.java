package org.firstinspires.ftc.teamcode.Season;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;

@TeleOp
public class A_CompCode extends LinearOpMode {

//---------------------------------------------------------------------------

    //Initialise Servo State
    boolean ClawOpen = false;
    boolean WristOut = false;

//---------------------------------------------------------------------------

    //Set Speed
    static final double ClimbSpeedUp = -0.6;
    static final double ClimbSpeedDown = 1;
    static final double ManualLiftSpeed = -0.5;
    static final double AutoLiftSpeed = -1;
    static final double LiftBounceDown = -1;
    static final double LiftBounceUp = 1;

//---------------------------------------------------------------------------

    //Set Endpoints
    int maxLiftEncoderCount = -5000;
    int minLiftEncoderCount = 0;
    int maxClimbEncoderCount = 5000;
    int minClimbEncoderCount = 0;

//---------------------------------------------------------------------------

    //Motor Set Points
    int LiftSetPtIntake = 10;
    int LiftSetPtLvl1 = 450;
    int LiftSetPtLvl2 = 900;
    int ClimbSetPtUp = -3000;
    int ClimbSetPtDown = -10;

//---------------------------------------------------------------------------

    //Servo Set Points
    static final double WristSetPtOut = 0.05;
    static final double WristSetPtIn = 0.3;
    static final double ClawSetPtClosed = 0;
    static final double ClawSetPtOpen = 0.2;

//---------------------------------------------------------------------------
    @Override
    public void runOpMode() throws InterruptedException {

//---------------------------------------------------------------------------

        //Motor Declaration
        DcMotor frontLeftMotor = hardwareMap.dcMotor.get("Leftfront");
        DcMotor backLeftMotor = hardwareMap.dcMotor.get("Leftback");
        DcMotor frontRightMotor = hardwareMap.dcMotor.get("Rightfront");
        DcMotor backRightMotor = hardwareMap.dcMotor.get("Rightback");
        DcMotor Lift = hardwareMap.dcMotor.get("Lift");
        DcMotor Climb = hardwareMap.dcMotor.get("Climb");

        //Motor Reverse
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        //Driving Variables
        double LeftStickY;
        double LeftStickX;

//---------------------------------------------------------------------------

        //Encoder Mode
        Lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Climb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Climb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //Enable Break
        Climb.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
        Lift.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);

//---------------------------------------------------------------------------

        //Servo Declaration
        Servo Wrist = hardwareMap.servo.get("Wrist");
        Servo Claw = hardwareMap.servo.get("Claw");

        //Initialise Servos
        Claw.setPosition(0.2);
        Wrist.setPosition(0.05);

//---------------------------------------------------------------------------

        //Track Previous State of Buttons
        boolean previousRBumperButtonState = false;
        //Claw Control
        boolean previousLBumperButtonState = false;
        //Wrist Control

//---------------------------------------------------------------------------

        //Verify Robot Waiting
        telemetry.addData(">", "Robot Ready.  Press Play.");
        telemetry.update();

//---------------------------------------------------------------------------

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {

//---------------------------------------------------------------------------

            //Initialise Encoders
            int currentLiftPosition = Lift.getCurrentPosition();
            int currentClimbPosition = Climb.getCurrentPosition();

            //Initialise Buttons
            //Claw Control
            boolean currentRBumperButtonState = gamepad1.right_bumper;
            //Wrist Control
            boolean currentLBumperButtonState = gamepad1.left_bumper;

//---------------------------------------------------------------------------

            //Drive Control
            //Slow Driving
            if (gamepad1.x) {
                LeftStickY = -gamepad1.left_stick_y * 0.1;
                LeftStickX = gamepad1.left_stick_x * 0.2;
            } else {
                LeftStickY = -gamepad1.left_stick_y;
                LeftStickX = gamepad1.left_stick_x * 0.5;
            }

            //Mecanum Driving with Triggers
            if (gamepad1.left_trigger > 0.1) {
                //Strafe Left
                frontLeftMotor.setPower(-gamepad1.left_trigger);
                frontRightMotor.setPower(gamepad1.left_trigger);
                backLeftMotor.setPower(gamepad1.left_trigger);
                backRightMotor.setPower(-gamepad1.left_trigger);
            } else if (gamepad1.right_trigger > 0.1) {
                //Strafe Right
                frontLeftMotor.setPower(gamepad1.right_trigger);
                frontRightMotor.setPower(-gamepad1.right_trigger);
                backLeftMotor.setPower(-gamepad1.right_trigger);
                backRightMotor.setPower(gamepad1.right_trigger);
            } else {
                //Normal POV Drive
                double drive = LeftStickY;
                double turn = LeftStickX;
                frontLeftMotor.setPower(Range.clip(drive + turn, -1.0, 1.0));
                backLeftMotor.setPower(Range.clip(drive + turn, -1.0, 1.0));
                frontRightMotor.setPower(Range.clip(drive - turn, -1.0, 1.0));
                backRightMotor.setPower(Range.clip(drive - turn, -1.0, 1.0));
            }

//---------------------------------------------------------------------------

            //Lift Control
            //A Button Pressed
            if (gamepad1.a) {
                //Set Target Position and Power to lowest position
                Lift.setTargetPosition(LiftSetPtIntake);
                Lift.setPower(AutoLiftSpeed);
                //Set Run Mode
                Lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                //Wait for Target Position
                while (opModeIsActive() && Lift.isBusy()) {
                    telemetry.addLine("Going to A");
                    telemetry.addData("Motor Position", Lift.getCurrentPosition());
                    telemetry.update();
                } // while
                //Reset Power
                Lift.setPower(0);
                //Reset Run Mode
                Lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //B Button Pressed
            } else if (gamepad1.b) {
                //Set Target Position and Power to set point 1
                Lift.setTargetPosition(LiftSetPtLvl1);
                Lift.setPower(AutoLiftSpeed);
                //Set Run Mode
                Lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                //Wait for Target Position
                while (opModeIsActive() && Lift.isBusy()) {
                    telemetry.addLine("Going to B");
                    telemetry.addData("Motor Position", Lift.getCurrentPosition());
                    telemetry.update();
                }
                //Reset Power
                Lift.setPower(0);
                //Reset Run Mode
                Lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //Y Button Pressed
            } else if (gamepad1.y) {
                //Set Target Position and Power to set point 2
                Lift.setTargetPosition(LiftSetPtLvl2);
                Lift.setPower(AutoLiftSpeed);
                //Set Run Mode
                Lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                //Wait for Target Position
                while (opModeIsActive() && Lift.isBusy()) {
                    telemetry.addLine("Going to B");
                    telemetry.addData("Motor Position", Lift.getCurrentPosition());
                    telemetry.update();
                }
                //Reset Power
                Lift.setPower(0);
                //Reset Run Mode
                Lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            } else {
                // No button (a,b,y) is pressed, so see if the joystick is moved, but keep lift within min <> max
                if (Lift.getCurrentPosition() < maxLiftEncoderCount && Lift.getCurrentPosition() > minLiftEncoderCount) {
                    // Lift is in a safe place (between max and min) so action what the joystick says
                    Lift.setPower(gamepad1.right_stick_y * AutoLiftSpeed);
                } else if (Lift.getCurrentPosition() >= maxLiftEncoderCount) {
                    // Lift is above max, so bounce down a little
                    Lift.setPower(LiftBounceDown * ManualLiftSpeed);
                } else if (Lift.getCurrentPosition() <= minLiftEncoderCount){
                    // Lift is below min so bounce up a little
                    Lift.setPower(LiftBounceUp * AutoLiftSpeed);
                } // end if lift in safe zone
            } // end if button pressed

//---------------------------------------------------------------------------

            //Climb Control
            //Dpad Up Pressed
            if (gamepad1.dpad_up) {
                //Set Target Position and Power
                Climb.setTargetPosition(ClimbSetPtUp);
                Climb.setPower(ClimbSpeedUp);
                //Set Run Mode
                Climb.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                //Wait for Target Position
                while (opModeIsActive() && Climb.isBusy()) {
                    telemetry.addLine("Going Up");
                    telemetry.addData("Motor Position", Climb.getCurrentPosition());
                    telemetry.update();
                }
                //Reset Power
                Climb.setPower(0);
                //Reset Run Mode
                Climb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //Dpad Down Pressed
            } else if (gamepad1.dpad_down) {
                //Set Target Position and Power
                Climb.setTargetPosition(ClimbSetPtDown);
                Climb.setPower(ClimbSpeedDown);
                //Set Run Mode
                Climb.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                //Wait for Target Position
                while (opModeIsActive() && Climb.isBusy()) {
                    telemetry.addLine("Going Down");
                    telemetry.addData("Motor Position", Climb.getCurrentPosition());
                    telemetry.update();
                }
                //Reset Power
                Climb.setPower(0);
                //Reset Run Mode
                Climb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }

//---------------------------------------------------------------------------

            //Wrist Toggle
            //Check if the button is currently pressed and was not pressed in the previous iteration
            if (currentLBumperButtonState && !previousLBumperButtonState) {
                if (WristOut) {
                    Wrist.setPosition(WristSetPtIn);
                    //Wrist In
                } else {
                    Wrist.setPosition(WristSetPtOut);
                    //Wrist Out
                }
                WristOut = !WristOut;
                //Toggle State
            }

//---------------------------------------------------------------------------

            //Claw Toggle
            //Check if the button is currently pressed and was not pressed in the previous iteration
            if (currentRBumperButtonState && !previousRBumperButtonState) {
                if (ClawOpen) {
                    Claw.setPosition(ClawSetPtClosed);
                    //Claw Closed
                } else {
                    Claw.setPosition(ClawSetPtOpen);
                    //Claw Open
                }
                ClawOpen = !ClawOpen;
                //Toggle State
            }

//---------------------------------------------------------------------------

            //Update previous button states
            previousRBumperButtonState = currentRBumperButtonState;
            //Claw Button State
            previousLBumperButtonState = currentLBumperButtonState;
            //Wrist Button State

//---------------------------------------------------------------------------

            //Telemetry Update
            //Drive Information
            telemetry.addData("Left Stick X", gamepad1.left_stick_x);
            telemetry.addData("Left Stick Y", gamepad1.left_stick_y);
            telemetry.addData("Strafe Left", gamepad1.left_trigger);
            telemetry.addData("Strafe Right", gamepad1.right_trigger);
            //Lift Information
            telemetry.addData("Lift Power", gamepad1.right_stick_y);
            telemetry.addData("Lift Position", Lift.getCurrentPosition());
            //Climb Information
            telemetry.addData("Climb State", gamepad1.dpad_up ? "Up" : "Down");
            telemetry.addData("Climb Position", Climb.getCurrentPosition());
            //Claw Information
            telemetry.addData("Claw State", ClawOpen ? "Open" : "Closed");
            //Wrist Information
            telemetry.addData("Wrist State", WristOut ? "Out" : "In");
            //Update
            telemetry.update();
        }
    }
}
